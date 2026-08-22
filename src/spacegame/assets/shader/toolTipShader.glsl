#type vertex
#version 460 core
layout (location = 0) in vec3 vPos;
layout (location = 1) in vec4 vColor;
layout (location = 2) in vec2 vTexCoords;
layout (location = 3) in float vTexIndex;
layout (location = 4) in float vImage;

uniform dmat4 uProjection;
uniform dmat4 uView;


out vec4 fColor;
out vec2 fTexCoords;
out float fTexIndex;
flat out float fImage;

void main() {
    fColor = vColor;
    fTexCoords = vTexCoords;
    fTexIndex = vTexIndex;
    fImage = vImage;

    gl_Position = vec4(uProjection * uView * vec4(vPos, 1.0));
}


#type fragment
#version 460 core

uniform sampler2D fontAtlas;
uniform sampler2D textBoxAtlas;
uniform sampler2DArray blockArray;
uniform sampler2DArray itemArray;
uniform sampler2D mouseIconAtlas;


uniform int width;
uniform int height;


const int FONT_ATLAS = 0;
const int TEXT_BOX_ATLAS = 1;
const int BLOCK_ARRAY = 2;
const int ITEM_ARRAY = 3;
const int MOUSE_ICON_ATLAS = 4;

in vec4 fColor;
in vec2 fTexCoords;
in float fTexIndex;
flat in float fImage;

out vec4 color;

void main(){

    switch(int(fImage)){
        case FONT_ATLAS:
        color = fColor * texture(fontAtlas, fTexCoords);
        break;

        case TEXT_BOX_ATLAS:
        color = fColor * texture(textBoxAtlas, fTexCoords);
        break;

        case BLOCK_ARRAY:
        color = fColor * texture(blockArray, vec3(fTexCoords, fTexIndex));
        break;

        case ITEM_ARRAY:
        color = fColor * texture(itemArray, vec3(fTexCoords, fTexIndex));
        break;

        case MOUSE_ICON_ATLAS:
        color = fColor * texture(mouseIconAtlas, fTexCoords);
        break;
    }

    //Units are in screen coordinates from the center of the viewport in distance squared
     float radius = 300.0;

     float x = gl_FragCoord.x - width * 0.5;
     float y = gl_FragCoord.y - height * 0.5;
     float distance = sqrt(x*x + y*y);

     // smooth fade mask: 0 at center → 1 at radius
     float fade = smoothstep(0.0, radius, distance);

     // apply fade AFTER texture alpha
     color.w = color.w * fade;





}