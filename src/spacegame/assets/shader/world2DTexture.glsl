#type vertex
#version 460 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in vec3 normal;
layout (location=4) in float skyLightValue;

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform vec3 chunkOffset;
uniform mat4 lightViewProjectionMatrix;
uniform vec3 sunChunkOffset;
uniform float baseLight;
uniform vec4 lightColor;
uniform bool performNormals;
uniform vec3 normalizedLightVector;
uniform vec3 playerPositionInChunk;
uniform bool compressTest;

out vec4 fColor;
out vec2 fTexCoords;
out vec4 fragPosInLightSpace;
flat out int isInShadowRange;

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

vec4 setFinalColor(vec4 skyLightColor, vec4 vertexColor){
    vec4 finalColor = vec4(1.0, 1.0, 1.0, 1.0);
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
}

int floatToHalf(float value) {
    uint floatBits = floatBitsToUint(value);
    uint sign = (floatBits >> 16u) & 0x8000u;
    int exponent = int((floatBits >> 23u) & 0xFFu) - 112;
    uint mantissa = floatBits & 0x7FFFFFu;

    if (exponent <= 0) {
        return int(sign); // subnormal or zero
    } else if (exponent >= 31) {
        return int(sign | 0x7FFFu); // infinity or NaN
    } else {
        return int(sign | (uint(exponent) << 10u) | (mantissa >> 13u));
    }
}


float halfToFloat(int f16) {
    int sign = (f16 >> 15) & 0x1;
    int exponent = (f16 >> 10) & 0x1F;
    int mantissa = f16 & 0x3FF;

    if (exponent == 0) {
        return sign == 0? 0 : -0.0f;
    } else if (exponent == 31) {
        return intBitsToFloat(sign == 0 ? 0x7f800000 : 0xff800000);
    } else {
        exponent += 112;
        mantissa <<= 13;
        return intBitsToFloat((sign << 31) | (exponent << 23) | mantissa);
    }
}

vec3 compress(){
    float x = aPos.x;
    float y = aPos.y;
    float z = aPos.z;

    int x1 = floatToHalf(x);
    int y1 = floatToHalf(y);
    int z1 = floatToHalf(z);

    float x2 = halfToFloat(x1);
    float y2 = halfToFloat(y1);
    float z2 = halfToFloat(z1);

    return vec3(x2, y2, z2);
}

void main()
{

    vec4 skyLightColor = performLightingNormals(vec4(skyLightValue, skyLightValue, skyLightValue, 1.0), normal);
    fColor = setFinalColor(skyLightColor, aColor);

    fTexCoords = aTexCoords;


    vec3 correctPos = vec3(chunkOffset + aPos);
    if(compressTest){
        correctPos = vec3(chunkOffset + compress());
    }
    gl_Position = vec4(uProjection * uView * vec4(correctPos, 1.0));

    vec3 correctPosRelativeToSun = vec3(sunChunkOffset + aPos);
    fragPosInLightSpace = vec4(lightViewProjectionMatrix * vec4(correctPosRelativeToSun, 1.0));

    isInShadowRange = distance(correctPos, playerPositionInChunk) < 256.0 ? 1 : 0;
}


#type fragment
#version 460 core

in vec4 fColor;
in vec2 fTexCoords;
in vec4 fragPosInLightSpace;
flat in int isInShadowRange;

uniform sampler2D uTexture;
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
    color = fColor * texture(uTexture, fTexCoords);
    if (useFog){
        if(renderShadows && isInShadowRange == 1){
            float shadow = getShadowFactor(fragPosInLightSpace);
            color.xyz *= shadow;
        }

        if (underwater){
            color = setFogUnderwater(color);
        } else {
            color = setFog(color);
        }
    }
}