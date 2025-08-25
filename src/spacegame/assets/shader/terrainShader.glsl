#type vertex
#version 460 core
layout (location=0) in float aPos;
layout (location=1) in float aColor;
layout (location=2) in float aTexCoords;
layout (location=3) in float aTexId;
layout (location=4) in vec2 normalAndSkyLightValue;

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform vec3 chunkOffset;
uniform double time;
uniform mat4 lightViewProjectionMatrix;
uniform vec3 sunChunkOffset;
uniform vec3 normalizedLightVector;
uniform float baseLight;
uniform vec3 playerPositionInChunk;
uniform vec4 lightColor;
uniform bool performNormals;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out vec4 fragPosInLightSpace;
out vec3 fragPosInWorldSpace;
flat out int isInShadowRange;
out vec3 fPlayerPositionInChunk;

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

//encoded in increments of 8 bits as red, green, blue, the most significant byte is unused as alpha is hard coded to 1
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

vec4 performLightingNormals(vec4 skyLightColor, vec3 vertexNormal){
    vertexNormal = normalize(vertexNormal);
    float angleCos = dot(vertexNormal, normalizedLightVector);

    float baseLight = baseLight;
    float shadeFactor = 0.5 * baseLight;//50% of the baseLight
    float shaded = 0.5 * baseLight;//Fully shaded is a reduction of 50% of the baseLight
    float perpendicular = 0;

    shadeFactor= clamp(shadeFactor, 0.1, 1.0);
    shaded = clamp(shaded, 0.1, 1.0);

    if (performNormals){
        if (angleCos < perpendicular){
            skyLightColor *= shaded;
        } else {
            baseLight -= (shadeFactor * (1.0 - angleCos));
            baseLight = clamp(baseLight, 0.1, 1.0);
            skyLightColor *= baseLight;
        }
    } else {
        skyLightColor *= shaded;
    }

    skyLightColor *= lightColor;

    return skyLightColor;
}

vec3 decompressNormal(vec2 normalAndSkyLightValue){
    int normalXY = floatBitsToInt(normalAndSkyLightValue.x);
    int normalZAndSkyLightValue = floatBitsToInt(normalAndSkyLightValue.y);

    int normalX = (normalXY >> 16) & 65535;
    int normalY = normalXY & 65535;
    int normalZ = (normalZAndSkyLightValue >> 16) & 65535;

    return vec3(halfToFloat(normalX), halfToFloat(normalY), halfToFloat(normalZ));
}

vec4 decompressSkyLightValue(vec2 normalAndSkyLightValue){
    int normalZAndSkyLightValue = floatBitsToInt(normalAndSkyLightValue.y);
    float skyLightValue = halfToFloat(normalZAndSkyLightValue & 65535);
    return vec4(skyLightValue, skyLightValue, skyLightValue, 1.0);
}

vec4 setFinalColor(vec4 skyLightColor, vec4 vertexColor){
    vec4 finalColor = vec4(1.0, 1.0, 1.0, 1.0);

    int texID = int(fTexId);
    if(texID == 0 || texID == 2){
        int upperByte = (floatBitsToInt(aColor) >> 24) & 255;
        int lowerByte = (floatBitsToInt(aTexCoords) >> 24) & 255;
        float colorMultiplier = halfToFloat((upperByte << 8) | lowerByte) * 2;
        vec4 grassColor = vec4(vertexColor.x * colorMultiplier, vertexColor.y * colorMultiplier, vertexColor.z * colorMultiplier, 1.0);
        skyLightColor *= grassColor;
    }




    if(skyLightColor.x > vertexColor.x){
        finalColor.x = skyLightColor.x;
    } else {
        finalColor.x = vertexColor.x;
    }

    if(skyLightColor.y > vertexColor.y){
        finalColor.y = skyLightColor.y;
    } else {
        finalColor.y = vertexColor.y;
    }

    if(skyLightColor.z > vertexColor.z){
        finalColor.z = skyLightColor.z;
    } else {
        finalColor.z = vertexColor.z;
    }


    return finalColor;

    //Reconstruct the color of the grass/ whatever grayscale image needs to be colored by multiplying the vertex color by a precalculated value on the CPU side, the value's formula is 1 / vertexColor = y.
    //This value can be converted from a float to half, split into two bytes and stuck onto aColor and aTexCoords, they are seperated on the CPU side. Reconstruct using those two to return it back to a float
    //Take this float and multiply it by the vertex color to get the grass color value (some form of green). Take this value and multiply it by the skylightcolor vec4. Then perform the comparison to determine which color is brighter for lighting calcs
    //This code should only run when doing any kind of grayscale coloring, this is worthless to do on dirt stone snow, etc.
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

    vec3 normal = decompressNormal(normalAndSkyLightValue);

    vec4 skyLightColor = performLightingNormals(decompressSkyLightValue(normalAndSkyLightValue), normal);

    fColor = setFinalColor(skyLightColor, color);

    isInShadowRange = distance(correctPos, playerPositionInChunk) < 256.0 ? 1 : 0; //This value should be the size of the shadowmap's orthographic projection

    fPlayerPositionInChunk = playerPositionInChunk;
    fragPosInWorldSpace = correctPos;

    fragPosInLightSpace = vec4(lightViewProjectionMatrix * vec4(correctPosRelativeToSun, 1.0));
    gl_Position = vec4(uProjection * uView * vec4(correctPos, 1.0));
}


#type fragment
#version 460 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in vec4 fragPosInLightSpace;
flat in int isInShadowRange;
in vec3 fragPosInWorldSpace;
in vec3 fPlayerPositionInChunk;

uniform sampler2DArray textureArray;
uniform sampler2D shadowMap;
uniform bool useFog;
uniform float fogDistance;
uniform bool underwater;
uniform bool renderShadows;
uniform bool shadowMapSetting;

uniform float fogRed;
uniform float fogGreen;
uniform float fogBlue;

out vec4 color;

vec4 setFog(vec4 color){
    float fogStart = (fogDistance - 128);
    float fogEnd = (fogDistance - 64);
    float distanceFromPlayer = distance(fragPosInWorldSpace, fPlayerPositionInChunk);
    if(distanceFromPlayer > fogStart){
        float fogDepth = ((distanceFromPlayer - fogStart) / fogEnd);
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
        if (renderShadows && isInShadowRange == 1 && shadowMapSetting){
            float shadow = getShadowFactor(fragPosInLightSpace);
            color.xyz *= shadow;
        }

        if (useFog){
            if (underwater){
                color = setFogUnderwater(color);
            } else {
                color = setFog(color);
            }
        }
    }
   // float maxDynamicLightDistance = 16.0;
   // float distanceFromPlayer = distance(fragPosInWorldSpace, fPlayerPositionInChunk);
   // if(distanceFromPlayer < maxDynamicLightDistance){
   //     color.x *= 10.0 * ((maxDynamicLightDistance - distanceFromPlayer) / maxDynamicLightDistance);
   //     color.y *= 10.0 * ((maxDynamicLightDistance - distanceFromPlayer) / maxDynamicLightDistance);
   //     color.z *= 10.0 * ((maxDynamicLightDistance - distanceFromPlayer) / maxDynamicLightDistance);
   // }
}