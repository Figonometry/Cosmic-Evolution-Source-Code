#type vertex
#version 460 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor; //Unused, assigned for the VAO
layout (location=2) in vec2 aTexCoords;  //Unused, assigned for the VAO
layout (location=3) in vec3 normal;  //Unused, assigned for the VAO
layout (location=4) in float skyLightValue;  //Unused, assigned for the VAO

uniform mat4 combinedViewProjectionMatrix;
uniform vec3 chunkOffset;


void main() {
    vec3 correctPos = vec3(chunkOffset + aPos);
    gl_Position = vec4(combinedViewProjectionMatrix * vec4(correctPos, 1.0));
}

#type fragment
#version 460 core


void main(){

}

