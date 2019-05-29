#version 330

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform int dimScreen;


void main()
{
    if(dimScreen == 0) {
        fragColor = texture(texture_sampler, outTexCoord);
    } else {
        fragColor = texture(texture_sampler, outTexCoord) * 3;
    }
}