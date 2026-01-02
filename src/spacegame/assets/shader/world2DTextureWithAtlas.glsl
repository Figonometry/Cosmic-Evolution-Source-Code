#type vertex
#version 460 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform vec3 chunkOffset;
uniform double time;
uniform vec3 playerPositionInChunk;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out vec3 fPlayerPositionInChunk;
out vec3 fragPosInWorldSpace;

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

void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;

    vec3 correctPos = vec3(chunkOffset + aPos);
    fragPosInWorldSpace = correctPos;
    fPlayerPositionInChunk = playerPositionInChunk;
    gl_Position = vec4(uProjection * uView * vec4(correctPos, 1.0));

}


#type fragment
#version 460 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;
in vec3 fPlayerPositionInChunk;
in vec3 fragPosInWorldSpace;

uniform sampler2D uTextures;
uniform bool useFog;
uniform float fogDistance;
uniform bool invertColor;
uniform bool underwater;
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


void main()
{
    int id = int(fTexId);
    color = fColor * texture(uTextures, fTexCoords);
    if (useFog){
        if (underwater){
            color = setFogUnderwater(color);
        } else {
            color = setFog(color);
        }
    }
}
