#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in mat4 modelViewMatrix;
layout (location=6) in vec2 texSelect;

out vec2 outTexCoord;

uniform mat4 projectionMatrix;
uniform vec2 textureGridSize;

void main()
{
    gl_Position = projectionMatrix * modelViewMatrix * vec4((position) + gl_InstanceID, 1.0);
    float texXSize = 1.0/textureGridSize.x;
    float texYSize = 1.0/textureGridSize.y;


    outTexCoord = vec2(texXSize * texSelect.x + texXSize * texCoord.x, texYSize * texSelect.y + texYSize * texCoord.y);
}