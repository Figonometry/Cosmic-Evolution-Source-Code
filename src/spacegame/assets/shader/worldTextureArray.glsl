#type vertex
#version 460 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;
layout (location=4) in vec3 normal;
layout (location=5) in float skyLightValue;

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform mat4 lightViewProjectionMatrix;
uniform vec3 chunkOffset;
uniform vec3 sunChunkOffset;
uniform double time;
uniform float baseLight;
uniform vec4 lightColor;
uniform bool performNormals;
uniform vec3 normalizedLightVector;
uniform vec3 playerPositionInChunk;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out vec4 fragPosInLightSpace;
out vec3 fPlayerPositionInChunk;
out vec3 fragPosInWorldSpace;
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
            baseLight -= 0.5;
            shadeFactor = 0.25;
            baseLight -= (shadeFactor * (-angleCos));
            baseLight = clamp(baseLight, 0.1, 1.0);
            skyLightColor *= baseLight;
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

void main()
{
    fTexCoords = aTexCoords;
    fTexId = aTexId;

    vec3 correctPos = vec3(chunkOffset + aPos);
    fragPosInWorldSpace = correctPos;
    gl_Position = vec4(uProjection * uView * vec4(correctPos, 1.0));

    vec4 skyLightColor = performLightingNormals(vec4(skyLightValue, skyLightValue, skyLightValue, 1.0), normal);

    fColor = setFinalColor(skyLightColor, aColor);

    fPlayerPositionInChunk = playerPositionInChunk;

    vec3 correctPosRelativeToSun = vec3(sunChunkOffset + aPos);
    fragPosInLightSpace = vec4(lightViewProjectionMatrix * vec4(correctPosRelativeToSun, 1.0));

    isInShadowRange = distance(correctPos, playerPositionInChunk) < 256.0 ? 1 : 0;
}


#type fragment
#version 460 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
flat in int isInShadowRange;
in vec4 fragPosInLightSpace;
in vec3 fragPosInWorldSpace;
in vec3 fPlayerPositionInChunk;

uniform sampler2DArray textureArray;
uniform sampler2D shadowMap;
uniform bool useFog;
uniform float fogDistance;
uniform bool underwater;
uniform bool renderShadows;
uniform bool shadowMapSetting;

uniform bool raining;
uniform double playerAbsoluteHeight;
uniform float rainFogFactor;

uniform float fogRed;
uniform float fogGreen;
uniform float fogBlue;

out vec4 color;

vec4 setFog(vec4 color){
    float fogStart = (fogDistance - 128);
    float fogEnd = (fogDistance - 64);
    float distanceFromPlayer = distance(fragPosInWorldSpace, fPlayerPositionInChunk);
    float fogDepth;

    if (raining && (fragPosInWorldSpace.y) < 10){
        float rainFog = rainFogFactor;
        if(rainFog < 0){
            rainFog = 0;
        }
        if(rainFog > 0.75f){
            rainFog = 0.75f;
        }

        float distance = distance(fragPosInWorldSpace.y, fPlayerPositionInChunk.y);
        float distanceThreshold = float(playerAbsoluteHeight) - 10;
        if (distance > distanceThreshold){
            float thresholdDif = distance - distanceThreshold;
            if (playerAbsoluteHeight < 10){
                if(fragPosInWorldSpace.y > playerAbsoluteHeight){
                    distance *= -1;
                    distanceThreshold = float(playerAbsoluteHeight) - 10;
                    thresholdDif = distance - distanceThreshold;
                }
                fogDepth += rainFog - (rainFog * ((10 - thresholdDif) / 10.0));
            } else {
                fogDepth += rainFog - (rainFog * ((10 - thresholdDif) / 10.0));
            }
        }
    } else if(raining == false && rainFogFactor >= 0 && rainFogFactor <= 0.75f){
        float rainFog = rainFogFactor;
        if(rainFog < 0){
            rainFog = 0;
        }
        if(rainFog > 0.75f){
            rainFog = 0.75f;
        }

        float distance = distance(fragPosInWorldSpace.y, fPlayerPositionInChunk.y);
        float distanceThreshold = float(playerAbsoluteHeight) - 10;
        if (distance > distanceThreshold){
            float thresholdDif = distance - distanceThreshold;
            if (playerAbsoluteHeight < 10){
                if(fragPosInWorldSpace.y > playerAbsoluteHeight){
                    distance *= -1;
                    distanceThreshold = float(playerAbsoluteHeight) - 10;
                    thresholdDif = distance - distanceThreshold;
                }
                fogDepth += rainFog - (rainFog * ((10 - thresholdDif) / 10.0));
            } else {
                fogDepth += rainFog - (rainFog * ((10 - thresholdDif) / 10.0));
            }
        }
    }

    if (distanceFromPlayer > fogStart){
        fogDepth += ((distanceFromPlayer - fogStart) / fogEnd);
    }

    if (distanceFromPlayer > fogStart || (raining && fragPosInWorldSpace.y < 10)){
        fogDepth = clamp(fogDepth, 0.0, 0.99);
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
        if (useFog){
            if(renderShadows && isInShadowRange == 1 && shadowMapSetting){
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
}
