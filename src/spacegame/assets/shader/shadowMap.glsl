#type vertex
#version 460 core
layout (location=0) in float aPos;
layout (location=1) in float aColor; //Unused, the terrain has this assigned in the VAO/VBO
layout (location=2) in float aTexCoords; //Unused, the terrain has this assigned in the VAO/VBO
layout (location=3) in float aTexId; //Unused, the terrain has this assigned in the VAO/VBO

uniform mat4 combinedViewProjectionMatrix;
uniform vec3 chunkOffset;
uniform vec3 sunPosition;
uniform double time;
uniform bool wavyLeaves;

out float fTexID;
out vec2 fTexCoords;

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

vec3 decompressPosition(float posXY, float posZAndTexID){
    int combinedIntXY = floatBitsToInt(posXY);
    int combinedIntZ = floatBitsToInt(posZAndTexID);
    return vec3(halfToFloat((combinedIntXY >> 16) & 65535), halfToFloat(combinedIntXY & 65535), halfToFloat((combinedIntZ >> 16) & 65535));
}

vec2 decompressTextureCoordinates(float texCoord){
    int combinedInt = floatBitsToInt(texCoord);
    return vec2(((combinedInt >> 18) & 63) != 0 ? ((combinedInt >> 18) & 63) * 0.03125f : float((combinedInt >> 12) & 63), ((combinedInt >> 6) & 63) != 0 ? ((combinedInt >> 6) & 63) * 0.03125f : float(combinedInt & 63));
}


float decompressTexID(float posZAndTexID){
    int combinedInt = floatBitsToInt(posZAndTexID);
    return float(combinedInt & 65535);
}

float sinX(float x, float y, float z){
    float actualTime = float(time);
    int realY = int(y);
    int realZ = int(z);
    x += sin(actualTime/30 + (realY | realZ)) / 10;
    return x;
}

void main() {
    vec3 correctPos = vec3(chunkOffset + decompressPosition(aPos, aTexId));
    fTexID = decompressTexID(aTexId);
    fTexCoords = decompressTextureCoordinates(aTexCoords);
    if(int(fTexID) == 10 && wavyLeaves){ //Leaves
        correctPos.x = sinX(correctPos.x, correctPos.y, correctPos.z);
    }
    gl_Position = vec4(combinedViewProjectionMatrix * vec4(correctPos, 1.0));
}

#type fragment
#version 460 core

in float fTexID;
in vec2 fTexCoords;


uniform sampler2DArray textureArray;

void main(){
    float id = fTexID;
    vec4 color = texture(textureArray, vec3(fTexCoords, id));

     if(color.w < 0.1f){
         discard;
     }

}
