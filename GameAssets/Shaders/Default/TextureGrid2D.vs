#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;
layout (location=2) in vec3 vertexNormal;

out vec2 outTexCoord;

uniform mat4 projModelMatrix;
uniform vec2 selectedTexture;
uniform vec2 gridSize;

void main()
{
    gl_Position = projModelMatrix * vec4(position, 1.0);

    float gridPercentageX = 1.0/gridSize.x;
    float gridPercentageY = 1.0/gridSize.y;
    outTexCoord = vec2(gridPercentageX * selectedTexture.x + gridPercentageX * texCoord.x, gridPercentageY * selectedTexture.y + gridPercentageY * texCoord.y);
}