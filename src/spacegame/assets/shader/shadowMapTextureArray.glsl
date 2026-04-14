#type vertex
#version 460 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor; //Unused, assigned for the VAO
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float texID;
layout (location=4) in vec3 normal;  //Unused, assigned for the VAO
layout (location=5) in float skyLightValue;  //Unused, assigned for the VAO

uniform mat4 combinedViewProjectionMatrix;
uniform vec3 chunkOffset;

out float fTexID;
out vec2 fTexCoords;


void main() {
    fTexID = texID;
    fTexCoords = aTexCoords;
    vec3 correctPos = vec3(chunkOffset + aPos);
    gl_Position = vec4(combinedViewProjectionMatrix * vec4(correctPos, 1.0));
}

#type fragment
#version 460 core

uniform sampler2DArray textureArray;

in float fTexID;
in vec2 fTexCoords;

void main(){
    float id = fTexID;
    vec4 color = texture(textureArray, vec3(fTexCoords, id));

    if(color.w < 0.1f){
        discard;
    }
}