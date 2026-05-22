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
uniform bool isColorCorrected;
uniform float colorMultiplier;

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

    float lightVal = baseLight;
    float shadeFactor = 0.25 * lightVal;//50% of the baseLight
    float perpendicular = 0;

    shadeFactor = clamp(shadeFactor, 0.1, 1.0);

    if (angleCos < perpendicular){
        lightVal -= 0.25;
        shadeFactor = 0.125;
        lightVal -= (shadeFactor * (-angleCos));
        lightVal = clamp(lightVal, 0.1, 1.0);
        skyLightColor *= lightVal;
    } else {
        lightVal -= (shadeFactor * (1.0 - angleCos));
        lightVal = clamp(lightVal, 0.1, 1.0);
        skyLightColor *= lightVal;
    }

    skyLightColor *= lightColor;

    return skyLightColor;
}

vec4 setFinalColor(vec4 skyLightColor, vec4 vertexColor){
    vec4 finalColor = vec4(1.0, 1.0, 1.0, 1.0);


    if(isColorCorrected){
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
uniform bool isHoldingLight;

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

float getShadowFactor(vec4 fragPosInLightSpace)
{
    vec3 projCoords = fragPosInLightSpace.xyz / fragPosInLightSpace.w;
    projCoords = projCoords * 0.5 + 0.5;

    if (projCoords.z > 1.0)
    return 1.0;

    float currentDepth = projCoords.z;

    // texel size based on shadow map resolution
    float texelSize = 1.0 / 8192;

    float bias = 0.0008; // slightly larger for PCF

    float shadow = 0.0;

    // 3×3 PCF kernel
    for (int x = -1; x <= 1; x++)
    {
        for (int y = -1; y <= 1; y++)
        {
            float pcfDepth = texture(shadowMap, projCoords.xy + vec2(x, y) * texelSize).r;
            shadow += (currentDepth - bias > pcfDepth) ? 0.5 : 1.0;
        }
    }

    shadow /= 9.0;
    return shadow;
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

    if(isHoldingLight){
        float origR = color.x;
        float origG = color.y;
        float origB = color.z;

        float r = fColor.x;
        float g = fColor.y;
        float b = fColor.z;

        float highestChannel = max(r, g);
        highestChannel = max(highestChannel, b);

        float lightMultiplier = 1.0f / highestChannel;

        lightMultiplier = max(lightMultiplier, 0.1f);

        float maxDynamicLightDistance = 16.0;
        float distanceFromPlayer = distance(fragPosInWorldSpace, fPlayerPositionInChunk);
        if (distanceFromPlayer < maxDynamicLightDistance){
            color.x *= lightMultiplier * ((maxDynamicLightDistance - distanceFromPlayer) / maxDynamicLightDistance);
            color.y *= lightMultiplier * ((maxDynamicLightDistance - distanceFromPlayer) / maxDynamicLightDistance);
            color.z *= lightMultiplier * ((maxDynamicLightDistance - distanceFromPlayer) / maxDynamicLightDistance);

            color.y *= 0.9f;
            color.z *= 0.9f;

            if (color.x < origR){
                color.x = origR;
            }

            if (color.y < origG){
                color.y = origG;
            }

            if (color.z < origB){
                color.z = origB;
            }
        }
    }
}
