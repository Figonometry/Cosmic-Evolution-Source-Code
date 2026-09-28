#type vertex
#version 460 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec2 aTexCoords;
layout (location=2) in vec3 normal;

//Must do color shift based on precipitaiton value, copied from CPU side
//Must upload a model matrix to translate from model space to the proper location from the camera
//

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform vec3 chunkOffset;
uniform float scale;
uniform float precipitation;
uniform float strength;
uniform mat4 lightViewProjectionMatrix;
uniform float baseLight;
uniform vec4 lightColor;
uniform vec3 normalizedLightVector;
uniform vec3 playerPositionInChunk;

out vec3 fragPosInWorldSpace;
out vec3 fPlayerPositionInChunk;
out vec4 fColor;
out vec2 fTexCoords;

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

float calculateAlpha(){
    return strength <= 1f ? ((int(strength * 255)) / 255f) : 1f;
}

float calculateColor(){
    return  (255 - (int((precipitation * 0.5f) * 255))) / 255f;
}


void main()
{
    float colorComponent = calculateColor();

    vec4 skyLightColor = performLightingNormals(vec4(colorComponent, colorComponent, colorComponent, calculateAlpha()), normal); //the first 3 componenets are normally the skylight value however the clouds will always be 1 due to them being above the ground



    fColor = skyLightColor;


    fTexCoords = aTexCoords;

    vec3 modelSpaceCoord = vec3(aPos);

    modelSpaceCoord.xyz *= scale;

    vec3 correctPos = modelSpaceCoord + chunkOffset;

    fPlayerPositionInChunk = playerPositionInChunk;
    fragPosInWorldSpace = correctPos;

    gl_Position = vec4(uProjection * uView * vec4(correctPos, 1.0));
}


#type fragment
#version 460 core

in vec4 fColor;
in vec2 fTexCoords;
in vec3 fPlayerPositionInChunk;
in vec3 fragPosInWorldSpace;

uniform sampler2D uTexture;
uniform float fogDistance;
uniform bool raining;
uniform float rainFogFactor;
uniform double playerAbsoluteHeight;
uniform float fogRed;
uniform float fogGreen;
uniform float fogBlue;

out vec4 color;


vec4 setFog(vec4 color){
    float fogStart = (fogDistance - 128);
    float fogEnd = (fogDistance);
    fogStart += 128;
    fogEnd += 128;
    float distanceFromPlayer = distance(fragPosInWorldSpace, fPlayerPositionInChunk);
    float fogDepth = 0.0;

    if (distanceFromPlayer > fogStart){
        fogDepth += ((distanceFromPlayer - fogStart) / fogEnd);
    }

    if (distanceFromPlayer > fogStart){
        fogDepth = clamp(fogDepth, 0.0, 0.99);
        color.x -= (color.x - fogRed) * ((fogDepth));
        color.y -= (color.y - fogGreen) * ((fogDepth));
        color.z -= (color.z - fogBlue) * ((fogDepth));
    }



    return color;
}

void main()
{
    color = fColor * texture(uTexture, fTexCoords);
    color = setFog(color);
}
