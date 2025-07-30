#type vertex
#version 460 core
layout (location=0) in float aPos;
layout (location=1) in float aColor;
layout (location=2) in float aTexCoords;
layout (location=3) in float aTexId;
layout (location=4) in vec3 normal;

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform vec3 chunkOffset;
uniform double time;
uniform mat4 lightViewProjectionMatrix;
uniform vec3 sunChunkOffset;
uniform vec3 normalizedLightVector;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out vec4 fragPosInLightSpace;

float sinX(float x, float y, float z){
    float actualTime = float(time);
    int realY = int(y);
    int realZ = int(z);
    x += sin(actualTime/30 + (realY | realZ)) / 10;
    return x;
}

float sinY(float x, float y, float z){
    float actualTime = float(time);
    int realX = int(x);
    int realZ = int(z);
    y += sin(actualTime/30 + (realX | realZ)) / 10;
    return y;
}

float sinZ(float x, float y, float z){
    float actualTime = float(time);
    int realX = int(x);
    int realY = int(y);
    z += sin(actualTime/30 + (realX | realY)) / 10;
    return z;
}

float halfToFloat(int f16) {
    int sign = (f16 >> 15) & 0x1;
    int exponent = (f16 >> 10) & 0x1F;
    int mantissa = f16 & 0x3FF;

    if (exponent == 0) {
        return sign == 0? 0 : -0.0f;
    } else if (exponent == 31) {
        return sign == 0 ? 0x7f800000 : 0xff800000;
    } else {
        exponent += 112;
        mantissa <<= 13;
        return intBitsToFloat((sign << 31) | (exponent << 23) | mantissa);
    }
}





//first 8 bits are unused, bit order in increments of 6 (x less than 1, x greater than 1, y less than 1, y greater than 1)
vec2 decompressTextureCoordinates(float texCoord){
    int combinedInt = floatBitsToInt(texCoord);
    return vec2(((combinedInt >> 18) & 63) != 0 ? ((combinedInt >> 18) & 63) * 0.03125f : float((combinedInt >> 12) & 63), ((combinedInt >> 6) & 63) != 0 ? ((combinedInt >> 6) & 63) * 0.03125f : float(combinedInt & 63));
}

//encoded in increments of 8 bits as alpha, red, green, blue
vec4 decompressColor(float color) {
    int combinedInt = floatBitsToInt(color);
    return vec4((combinedInt >> 16) & 255, (combinedInt >> 8)  & 255, combinedInt & 255, 255.0) / 255.0; //Alpha is a constant hardcoded value of 1
}

vec3 decompressPosition(float posXY, float posZAndTexID){
    int combinedIntXY = floatBitsToInt(posXY);
    int combinedIntZ = floatBitsToInt(posZAndTexID);
    return vec3(halfToFloat((combinedIntXY >> 16) & 65535), halfToFloat(combinedIntXY & 65535), halfToFloat((combinedIntZ >> 16) & 65535));
}

float decompressTexID(float posZAndTexID){
    int combinedInt = floatBitsToInt(posZAndTexID);
    return float(combinedInt & 65535);
}

float distanceFromCamera(vec3 correctPos){
    float a = abs(correctPos.x);
    float b = abs(correctPos.y);
    float c = abs(correctPos.z);

    if(a > b && a > c){
        return a;
    } else if(b > a && b > c){
        return b;
    } else {
        return c;
    }
}

vec4 performLightingNormals(vec4 color, vec3 vertexNormal){
    float angleCos = dot(vertexNormal, normalizedLightVector);

    float baseLight = 1;
    float shadeFactor = 0.7;
    float shaded = 0.3;
    float perpendicular = 0;

    if(angleCos < perpendicular){
        color *= shaded;
    } else {
        baseLight -= (shadeFactor * (1.0 - angleCos));
        color *= baseLight;
    }

    return color;
}

void main()
{
    vec4 color = decompressColor(aColor);
    fTexCoords = decompressTextureCoordinates(aTexCoords);
    fTexId = decompressTexID(aTexId);

    vec3 correctPos = vec3(chunkOffset + decompressPosition(aPos, aTexId));
    vec3 correctPosRelativeToSun = vec3(sunChunkOffset + decompressPosition(aPos, aTexId));
        switch(int(fTexId)){
            case 4://water
            correctPos.y -= 0.1F;
            correctPos.y = sinY(correctPos.x, correctPos.y, correctPos.z);
            fColor.xyz -= 0.5F;
            fColor.w = max(fColor.w, 0.5f);
            fTexCoords.xy += sin(float(time/150));
            break;
            case 10://leaves
            correctPos.x = sinX(correctPos.x, correctPos.y, correctPos.z);
            break;
            case 18://fire
            fTexCoords.xy += vec2(sin(correctPos.x * 2.0 + float(time) * 0.1) * 0.05, cos(correctPos.y * 3.0 + float(time) * 0.15)  * 0.20);
            fTexCoords.y = clamp(fTexCoords.y, 0.0, 1.0);
            break;

        }
   // float maxCameraDifference = distanceFromCamera(correctPos);

    color = performLightingNormals(color, normal);
    fColor = color;

    fragPosInLightSpace = vec4(lightViewProjectionMatrix * vec4(correctPosRelativeToSun, 1.0));
    gl_Position = vec4(uProjection * uView * vec4(correctPos, 1.0));
}


#type fragment
#version 460 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in vec4 fragPosInLightSpace;

uniform sampler2DArray textureArray;
uniform sampler2D shadowMap;
uniform bool useFog;
uniform float fogDistance;
uniform bool underwater;

uniform float fogRed;
uniform float fogGreen;
uniform float fogBlue;

out vec4 color;

vec4 setFog(vec4 color){
    float fogStart = (fogDistance - 128) * gl_FragCoord.w;
    float fogEnd = (fogDistance - 64) * gl_FragCoord.w;
    if(gl_FragCoord.z > fogStart){
        float fogDepth = (gl_FragCoord.z / fogEnd) - fogStart;
        if (fogDepth < 0){
            fogDepth = 0;
        }
        if (fogDepth > 1){
            fogDepth = 0.99;
        }
        color.x -= (color.x - fogRed) * ((fogDepth));
        color.y -= (color.y - fogGreen) * ((fogDepth));
        color.z -= (color.z - fogBlue) * ((fogDepth));
    }
    return color;
}

vec4 setFogUnderwater(vec4 color){
    float fogDepth = (gl_FragCoord.z / (50 * gl_FragCoord.w));
    if (fogDepth < 0){
        fogDepth = 0;
    }
    if (fogDepth > 1){
        fogDepth = 0.99;
    }
    color.x -= (color.x - 0.01F) * ((fogDepth));
    color.y -= (color.y - 0.01F) * ((fogDepth));
    color.z -= (color.z - 0.25F) * ((fogDepth));
    return color;
}

float getShadowFactor(vec4 fragPosInLightSpace){
    vec3 projCoords = fragPosInLightSpace.xyz / fragPosInLightSpace.w;
    projCoords = projCoords * 0.5 + 0.5;
    projCoords.xy = clamp(projCoords.xy, 0.0, 1.0);
    if(projCoords.z > 1.0)return 1.0;

    float closestDepth = texture(shadowMap, projCoords.xy).r;
    float currentDepth = projCoords.z;


    float bias = 0.0005;
    return currentDepth - bias > closestDepth ? 0.7 : 1.0;

}


void main()
{
    float id = fTexId;
    color = fColor * texture(textureArray, vec3(fTexCoords, id));
    if (color.w > 0){
        float shadow = getShadowFactor(fragPosInLightSpace);
        color.xyz *= shadow;

        if (useFog){
            if (underwater){
                color = setFogUnderwater(color);
            } else {
                color = setFog(color);
            }
        }
    }
}