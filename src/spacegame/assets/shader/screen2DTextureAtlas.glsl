#type vertex
#version 460 core
layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aTexCoords;
layout (location=3) in float aTexId;

uniform dmat4 uProjection;
uniform dmat4 uView;

out vec4 fColor;
out vec2 fTexCoords;
out float fTexId;


void main()
{
    fColor = aColor;
    fTexCoords = aTexCoords;
    fTexId = aTexId;

    gl_Position = vec4(uProjection * uView * vec4(aPos, 1.0));

}


#type fragment
#version 460 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

uniform sampler2D uTextures[256];

out vec4 color;


void main()
{
        int id = int(fTexId);
        color = fColor * texture(uTextures[id], fTexCoords);
}
