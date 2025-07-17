#type vertex
#version 460 core
layout (location=0) in float aPos;
layout (location=1) in float aColor; //Unused, the terrain has this assigned in the VAO/VBO
layout (location=2) in float aTexCoords; //Unused, the terrain has this assigned in the VAO/VBO
layout (location=3) in float aTexId; //Unused, the terrain has this assigned in the VAO/VBO

uniform mat4 uProjection;
uniform mat4 uView;
uniform vec3 chunkOffset;

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

void main() {
    vec3 correctPos = vec3(chunkOffset + decompressPosition(aPos, aTexId));
    gl_Position = vec4(uProjection * uView * vec4(correctPos, 1.0));
}

#type fragment
#version 460 core

void main(){
    //This is intentionally blank, the depth buffer will be written to in here
}
