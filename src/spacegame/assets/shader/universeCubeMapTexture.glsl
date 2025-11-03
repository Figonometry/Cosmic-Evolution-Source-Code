#type vertex
#version 460 core

layout (location=0) in vec3 aPos;
layout (location=1) in vec3 normal;

uniform dmat4 uProjection;
uniform dmat4 uView;
uniform mat4 uModel;
uniform vec3 position;
uniform vec3 normalizedLightDir;
uniform int lightColor;
uniform bool isStar;

out vec4 fColor;
out vec3 fTexCoords;

vec4 performNormals(vec3 normal){
    float dotProduct = dot(normal, normalizedLightDir);

    float intensity = max(0, dotProduct);

    int red = (lightColor >> 16) & 255;
    int green = (lightColor >> 8) & 255;
    int blue = lightColor & 255;

    red = min(255, max(0, int(red * intensity)));
    green = min(255, max(0, int(green * intensity)));
    blue = min(255, max(0, int(blue * intensity)));

    return vec4(red / 255f, green / 255f, blue / 255f, 1.0);
}


void main()
{

    if (isStar){
        fColor = vec4(((lightColor >> 16) & 255) / 255f, ((lightColor >> 8) & 255) / 255f, (lightColor & 255) / 255f, 1.0);
    } else {
        fColor = performNormals(normal);
    }

    vec3 transformedPos = (uModel * vec4(aPos, 1.0)).xyz;
    fTexCoords = normalize(transformedPos);
    fTexCoords.x = -fTexCoords.x;

    vec3 correctedPos = aPos + position;

    gl_Position = vec4(uProjection * uView * vec4(correctedPos, 1.0));
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
