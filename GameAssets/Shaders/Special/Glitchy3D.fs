#version 330

in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;


void main()
{
    fragColor = texture(texture_sampler, vec2(outTexCoord.x,1.0-outTexCoord.y));
}