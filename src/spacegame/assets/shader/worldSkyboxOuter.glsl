#type vertex
#version 460 core

layout (location=0) in vec3 aPos;

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform mat4 uModel;


out vec3 fTexCoords;

void main()
{

    vec3 transformedPos = (uModel * vec4(aPos, 1.0)).xyz;
    fTexCoords = normalize(transformedPos).xyz;

    gl_Position = vec4(uProjection * uView * vec4(aPos, 1.0));

}


#type fragment
#version 460 core

in vec3 fTexCoords;
uniform samplerCube starTexture;
uniform float starVisibility;

out vec4 color;

void main() {
    vec3 rotatedTexCoords = vec3(fTexCoords.y, -fTexCoords.x, fTexCoords.z);

    vec3 stars = texture(starTexture, rotatedTexCoords).xyz;
    // Blend stars with base sky color
    color = vec4(stars * starVisibility, 1.0); //1.0 is starvisiblity
}
