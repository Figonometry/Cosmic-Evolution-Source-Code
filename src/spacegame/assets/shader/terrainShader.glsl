#type vertex
#version 460 core
layout (location=0) in float aPos;
layout (location=1) in float aColor;
layout (location=2) in float aTexCoords;
layout (location=3) in float aTexId;
layout (location=4) in vec2 normalAndSkyLightValue;

//Movemment related
const int WATER_TOP_TEXTURE = 4;
const int WATER_SIDE_TEXTURE = 63;
const int WATER_NORTH_FLOW_TEXTURE = 65;
const int WATER_SOUTH_FLOW_TEXTURE = 66;
const int WATER_EAST_FLOW_TEXTURE = 67;
const int WATER_WEST_FLOW_TEXTURE = 68;
const int WATER_SIDE_TEXTURE_2 = 69;
const int LEAF_OPAQUE_TEXTURE = 10;
const int LEAF_TRANSPARENT_TEXTURE = 24;
const int FIRE_TEXTURE = 18;

//Color corrected, some overlap with movement
const int GRASS_FULL_TOP_TEXTURE = 0;
const int GRASS_FULL_SIDE_TEXTURE = 2;
const int TALL_GRASS_TEXTURE = 30;
const int BERRY_BUSH_TOP_BASE_TEXTURE = 11;
const int BERRY_BUSH_SIDE_BASE_TEXTURE = 12;
const int EMPTY_COLOR_TEXTURE = 19;
const int GRASS_SMALL_PATCHES_TOP_TEXTURE = 33;
const int GRASS_SMALL_PATCHES_SIDE_TEXTURE = 34;
const int GRASS_LARGE_PATCHES_TOP_TEXTURE = 35;
const int GRASS_LARGE_PATCHES_SIDE_TEXTURE = 36;

//Shifted on standard block model, heavy overlap with color corrected
const int SNOWY_GRASS_FULL_SIDE_TEXTURE = 96;
const int SNOWY_GRASS_SMALL_PATCHES_SIDE_TEXTURE = 37;
const int SNOWY_GRASS_LARGE_PATCHES_SIDE_TEXTURE = 41;

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
uniform bool wavyWater;
uniform bool wavyLeaves;
uniform bool windy;
uniform float windDirection;
uniform float windIntensity;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;
out vec4 fragPosInLightSpace;
out vec3 fragPosInWorldSpace;
flat out int isInShadowRange;
flat out int frostFactor;
flat out vec3 fNormal;
out vec3 fPlayerPositionInChunk;
out vec3 lightDir;

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
    return sin(actualTime/30 + (realX | realZ)) / 10;
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
        return intBitsToFloat(sign == 0 ? 0x7f800000 : 0xff800000);
    } else {
        exponent += 112;
        mantissa <<= 13;
        return intBitsToFloat((sign << 31) | (exponent << 23) | mantissa);
    }
}


int decompressFrostFactor(float texCoord){
    return (floatBitsToInt(texCoord) >> 8) & 3;
}



//In order, grayscale image mult, xUV, yUV, frost factor, unused byte, 8 bits, 7 bits, 7 bits, 2 bits and 8 bits
vec2 decompressTextureCoordinates(float texCoord){
    const float fractMult = 0.03125f;
    int combinedInt = floatBitsToInt(texCoord);
    float xUV = (combinedInt >> 18) & 63;
    xUV *= ((combinedInt >> 17) & 1) == 1 ? fractMult : 1;

    float yUV = (combinedInt >> 11) & 63;
    yUV *= ((combinedInt >> 10) & 1) == 1 ? fractMult : 1;

    return vec2(xUV,yUV);
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

bool isTexIDColorCorrected(int texID){
    switch(texID){
        case GRASS_FULL_TOP_TEXTURE: //Grass Top
        return true;
        case GRASS_FULL_SIDE_TEXTURE: //Grass Side
        return true;
        case LEAF_OPAQUE_TEXTURE: //Leaf Opaque
        return true;
        case LEAF_TRANSPARENT_TEXTURE: //Leaf Transparent
        return true;
        case TALL_GRASS_TEXTURE: //Tall Grass
        return true;
        case BERRY_BUSH_TOP_BASE_TEXTURE: //Berry Bush Base Side
        return true;
        case BERRY_BUSH_SIDE_BASE_TEXTURE: //Berry Bush Base Top
        return true;
        case EMPTY_COLOR_TEXTURE: //Empty color for item blocks
        return true;
        case GRASS_LARGE_PATCHES_SIDE_TEXTURE:
        return true;
        case GRASS_LARGE_PATCHES_TOP_TEXTURE:
        return true;
        case GRASS_SMALL_PATCHES_TOP_TEXTURE:
        return true;
        case GRASS_SMALL_PATCHES_SIDE_TEXTURE:
        return true;
        default:
        return false;
    }
}

vec4 setFinalColor(vec4 skyLightColor, vec4 vertexColor){
    vec4 finalColor = vec4(1.0, 1.0, 1.0, 1.0);

    int texID = int(fTexId);
    if(isTexIDColorCorrected(texID)){
        int upperByte = (floatBitsToInt(aColor) >> 24) & 255;
        int lowerByte = (floatBitsToInt(aTexCoords) >> 24) & 255;
        float colorMultiplier = halfToFloat((upperByte << 8) | lowerByte);
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
    //This code should only run when doing any kind of grayscale coloring, this is worthless to do on soil stone snow, etc.
}

vec3 windyGrass(vec3 vertexPos){
    if(fTexCoords.y == 1)return vertexPos;

    float xMove = sin(windDirection - (0.5 * 3.14159)) * (0.5f * windIntensity);
    float zMove = sin(windDirection - 3.14159) * (0.5f * windIntensity);

    vertexPos.x += xMove;
    vertexPos.z += zMove;

    return vertexPos;
}

void main()
{
    vec4 color = decompressColor(aColor);
    fTexCoords = decompressTextureCoordinates(aTexCoords);
    fTexId = decompressTexID(aTexId);

    vec3 correctPos = vec3(chunkOffset + decompressPosition(aPos, aTexId));
    vec3 correctPosRelativeToSun = vec3(sunChunkOffset + decompressPosition(aPos, aTexId));
        switch(int(fTexId)){
            case WATER_TOP_TEXTURE://water top
            fColor.xyz -= 0.5F;
            fColor.w = max(fColor.w, 0.5f);
            if (wavyWater){
                correctPos.y += (sinY(correctPos.x + 1, correctPos.y, correctPos.z + 1) * 0.5f);
                correctPos.y += (sinY(correctPos.x - 2, correctPos.y, correctPos.z - 3) * 0.25f);
                correctPos.y += (sinY(correctPos.x + 4, correctPos.y, correctPos.z + 7) * 0.125f);
                correctPos.y += (sinY(correctPos.x + 8, correctPos.y, correctPos.z - 7) * 0.0625f);
            }
            break;

            case WATER_SIDE_TEXTURE://water side
            fColor.xyz -= 0.5F;
            fColor.w = max(fColor.w, 0.5f);
            if (wavyWater && fTexCoords.y != 1.0){
                correctPos.y += (sinY(correctPos.x + 1, correctPos.y, correctPos.z + 1) * 0.5f);
                correctPos.y += (sinY(correctPos.x - 2, correctPos.y, correctPos.z - 3) * 0.25f);
                correctPos.y += (sinY(correctPos.x + 4, correctPos.y, correctPos.z + 7) * 0.125f);
                correctPos.y += (sinY(correctPos.x + 8, correctPos.y, correctPos.z - 7) * 0.0625f);
            }
            fTexCoords.y -= (float(time/60));
            break;

            case WATER_NORTH_FLOW_TEXTURE://water flowing north
            fColor.xyz -= 0.5F;
            fColor.w = max(fColor.w, 0.5f);
            fTexCoords.x += (float(time/60));
            if (wavyWater){
                correctPos.y += (sinY(correctPos.x + 1, correctPos.y, correctPos.z + 1) * 0.5f);
                correctPos.y += (sinY(correctPos.x - 2, correctPos.y, correctPos.z - 3) * 0.25f);
                correctPos.y += (sinY(correctPos.x + 4, correctPos.y, correctPos.z + 7) * 0.125f);
                correctPos.y += (sinY(correctPos.x + 8, correctPos.y, correctPos.z - 7) * 0.0625f);
            }
            break;

            case WATER_SOUTH_FLOW_TEXTURE://water flowing south
            fColor.xyz -= 0.5F;
            fColor.w = max(fColor.w, 0.5f);
            fTexCoords.x -= (float(time/60));
            if (wavyWater){
                correctPos.y += (sinY(correctPos.x + 1, correctPos.y, correctPos.z + 1) * 0.5f);
                correctPos.y += (sinY(correctPos.x - 2, correctPos.y, correctPos.z - 3) * 0.25f);
                correctPos.y += (sinY(correctPos.x + 4, correctPos.y, correctPos.z + 7) * 0.125f);
                correctPos.y += (sinY(correctPos.x + 8, correctPos.y, correctPos.z - 7) * 0.0625f);
            }
            break;

            case WATER_EAST_FLOW_TEXTURE://water flowing east
            fTexCoords.y += (float(time/60));
            fColor.xyz -= 0.5F;
            fColor.w = max(fColor.w, 0.5f);
            if (wavyWater){
                correctPos.y += (sinY(correctPos.x + 1, correctPos.y, correctPos.z + 1) * 0.5f);
                correctPos.y += (sinY(correctPos.x - 2, correctPos.y, correctPos.z - 3) * 0.25f);
                correctPos.y += (sinY(correctPos.x + 4, correctPos.y, correctPos.z + 7) * 0.125f);
                correctPos.y += (sinY(correctPos.x + 8, correctPos.y, correctPos.z - 7) * 0.0625f);
            }
            break;

            case WATER_WEST_FLOW_TEXTURE://water flowing west
            fTexCoords.y -= (float(time/60));
            fColor.xyz -= 0.5F;
            fColor.w = max(fColor.w, 0.5f);
            if (wavyWater){
                correctPos.y += (sinY(correctPos.x + 1, correctPos.y, correctPos.z + 1) * 0.5f);
                correctPos.y += (sinY(correctPos.x - 2, correctPos.y, correctPos.z - 3) * 0.25f);
                correctPos.y += (sinY(correctPos.x + 4, correctPos.y, correctPos.z + 7) * 0.125f);
                correctPos.y += (sinY(correctPos.x + 8, correctPos.y, correctPos.z - 7) * 0.0625f);
            }
            break;

            case WATER_SIDE_TEXTURE_2://water side fullwater
            fTexCoords.y -= (float(time/60));
            fColor.xyz -= 0.5F;
            fColor.w = max(fColor.w, 0.5f);
            if (fTexCoords.y == 0.0f){
                if (wavyWater){
                    correctPos.y += (sinY(correctPos.x + 1, correctPos.y, correctPos.z + 1) * 0.5f);
                    correctPos.y += (sinY(correctPos.x - 2, correctPos.y, correctPos.z - 3) * 0.25f);
                    correctPos.y += (sinY(correctPos.x + 4, correctPos.y, correctPos.z + 7) * 0.125f);
                    correctPos.y += (sinY(correctPos.x + 8, correctPos.y, correctPos.z - 7) * 0.0625f);
                }
            }
            break;

            case LEAF_OPAQUE_TEXTURE://leaves
            if(wavyLeaves){
                correctPos.x = sinX(correctPos.x, correctPos.y, correctPos.z);
            }
            break;

            case LEAF_TRANSPARENT_TEXTURE://leaves transparent
            if(wavyLeaves){
                correctPos.x = sinX(correctPos.x, correctPos.y, correctPos.z);
            }
            break;

            case FIRE_TEXTURE://fire
            fTexCoords.xy += vec2(sin(correctPos.x * 2.0 + float(time) * 0.1) * 0.05, cos(correctPos.y * 3.0 + float(time) * 0.15)  * 0.20);
            fTexCoords.y = clamp(fTexCoords.y, 0.0, 1.0);
            break;

        }

    vec3 normal = decompressNormal(normalAndSkyLightValue);

    fNormal = normal;

    vec4 skyLightColor = performLightingNormals(decompressSkyLightValue(normalAndSkyLightValue), normal);

    fColor = setFinalColor(skyLightColor, color);

    lightDir = normalizedLightVector;

    isInShadowRange = distance(correctPos, playerPositionInChunk) < 64.0 ? 1 : 0; //This value should be the size of the shadowmap's orthographic projection

    fPlayerPositionInChunk = playerPositionInChunk;

    if(windy && int(fTexId) == 30){
        correctPos = windyGrass(correctPos);
    }

    fragPosInWorldSpace = correctPos;

    frostFactor = decompressFrostFactor(aTexCoords);


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
flat in int frostFactor;
flat in vec3 fNormal;
in vec3 fragPosInWorldSpace;
in vec3 fPlayerPositionInChunk;
in vec3 lightDir;


//This is for the frost exclusion list
const int TORCH_TEXTURE = 3;
const int WATER_TOP_TEXTURE = 4;
const int CAMPFIRE_BASE_TEXTURE = 16;
const int REED_CHEST_TEXTURE = 32;
const int TORCH_UNLIT_TEXTURE = 38;
const int TORCH_BURNED_OUT_TEXTURE = 39;
const int WATER_SIDE_TEXTURE = 63;
const int WATER_BOTTOM_TEXTURE = 64;
const int WATER_NORTH_FLOW_TEXTURE = 65;
const int WATER_SOUTH_FLOW_TEXTURE = 66;
const int WATER_EAST_FLOW_TEXTURE = 67;
const int WATER_WEST_FLOW_TEXTURE = 68;
const int WATER_SIDE_TEXTURE_2 = 69;

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
uniform bool isHoldingLight;
uniform int heldLightColor;

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
    float texelSize = 1.0 / 8192.0;

    // Slope-scaled bias (fixes streaks on slopes)
    float bias = max(0.0005 * (1.0 - dot(normalize(fNormal), normalize(lightDir))),
    0.0005);


    float shadow = 0.0;
    int samples = 0;

    for (int x = -1; x <= 1; x++)
    {
        for (int y = -1; y <= 1; y++)
        {
            vec2 shadowCoords = projCoords.xy + vec2(x, y) * texelSize;

            // Skip invalid samples instead of returning early
            if (shadowCoords.x < 0.0 || shadowCoords.x > 1.0 ||
            shadowCoords.y < 0.0 || shadowCoords.y > 1.0)
            continue;

            float pcfDepth = texture(shadowMap, shadowCoords).r;
            shadow += (currentDepth - bias > pcfDepth) ? 0.5 : 1.0;
            samples++;
        }
    }

    // Normalize by number of valid samples
    if (samples > 0)
    shadow /= float(samples);
    else
    shadow = 1.0;

    return shadow;
}

float hash(vec2 p) {
    p = fract(p * 0.3183099 + vec2(0.71, 0.113));
    p *= 17.0;
    return fract(p.x * p.y * (p.x + p.y));
}

float valueNoise(vec2 p) {
    vec2 i = floor(p);
    vec2 f = fract(p);

    vec2 u = f * f * (3.0 - 2.0 * f);

    float a = hash(i);
    float b = hash(i + vec2(1.0, 0.0));
    float c = hash(i + vec2(0.0, 1.0));
    float d = hash(i + vec2(1.0, 1.0));

    return mix(mix(a, b, u.x), mix(c, d, u.x), u.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float amplitude = 0.5;

    for (int i = 0; i < 4; i++) {
        value += amplitude * valueNoise(p);
        p *= 2.0;
        amplitude *= 0.5;
    }

    return value;
}


bool notInExclusionList(int id){

    switch(id){
        case TORCH_TEXTURE:
        return false;
        case WATER_TOP_TEXTURE:
        return false;
        case CAMPFIRE_BASE_TEXTURE:
        return false;
        case REED_CHEST_TEXTURE:
        return false;
        case TORCH_UNLIT_TEXTURE:
        return false;
        case TORCH_BURNED_OUT_TEXTURE:
        return false;
        case WATER_SIDE_TEXTURE:
        return false;
        case WATER_BOTTOM_TEXTURE:
        return false;
        case WATER_NORTH_FLOW_TEXTURE:
        return false;
        case WATER_SOUTH_FLOW_TEXTURE:
        return false;
        case WATER_EAST_FLOW_TEXTURE:
        return false;
        case WATER_WEST_FLOW_TEXTURE:
        return false;
        case WATER_SIDE_TEXTURE_2:
        return false;
        default:
        return true;
    }
}



void main()
{
    float id = fTexId;
    vec4 textureColor = texture(textureArray, vec3(fTexCoords, id));
    color = fColor * textureColor;
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

            float heldLightRed = ((heldLightColor >> 16) & 255) / 255f;
            float heldLightGreen = ((heldLightColor >> 8) & 255) / 255f;
            float heldLightBlue = (heldLightColor & 255) / 255f;

            color.x *= heldLightRed;
            color.y *= heldLightGreen;
            color.z *= heldLightBlue;

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

    int texID = int(round(fTexId));

    if (frostFactor != 0 && notInExclusionList(texID)) {
        float frostPatchSize = 1.5; //Decrease this to increase patach size

        float frostMask = fbm(
        fNormal.y == 1.0 || fNormal.y == -1.0 ? fragPosInWorldSpace.xz * frostPatchSize :
        fNormal.x == 1.0 || fNormal.x == -1.0 ? fragPosInWorldSpace.zy * frostPatchSize :
        fragPosInWorldSpace.xy * frostPatchSize
        );

        float frostStateFactor = float(frostFactor) / 3.0;
        float frost = frostMask * frostStateFactor;
        float frostBoost = pow(frost, 0.75);

        // Compute brightness from final lit color
        float brightness = dot(color.rgb, vec3(0.299, 0.587, 0.114));

        // Additive frost (day)
        vec3 frostAdd = mix(color.rgb, vec3(0.85, 0.9, 1.0), frostBoost);

        // Multiplicative frost (night)
        vec3 frostMul = color.rgb * (1.0 - frostBoost * 0.5);

        // Blend between modes based on brightness
        float mode = clamp(brightness * 2.0, 0.0, 1.0);
        vec3 finalColor = mix(frostMul, frostAdd, mode);

        color = vec4(finalColor, color.a);
    }




    //Frost effect occurs here with fractal sampling, need to figure out how to tell the fragment shader to perform frosting
}