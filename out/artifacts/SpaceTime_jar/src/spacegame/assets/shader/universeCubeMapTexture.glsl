#type vertex
#version 460 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform mat4 uModel;
uniform vec3 position;
uniform bool skybox;

out vec4 fColor;
out vec3 fTexCoords;

void main()
{
    fColor = aColor;


    vec3 transformedPos = (uModel * vec4(aPos, 1.0)).xyz;
    fTexCoords = normalize(transformedPos);

    vec3 correctedPos = aPos + position;

    if(skybox){
        gl_Position = vec4(uProjection * dmat4(dmat3(uView)) * vec4(correctedPos, 1.0));
    } else {
        gl_Position = vec4(uProjection * uView * vec4(correctedPos, 1.0));
    }
}


#type fragment
#version 460 core

in vec4 fColor;
in vec3 fTexCoords;

uniform samplerCube cubeTexture;

out vec4 color;

void main()
{
        color = fColor * texture(cubeTexture, fTexCoords);
}
