#version 330

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform sampler2D test_texture;


void main()
{
    if(texture(test_texture, outTexCoord).a > 0) {
        fragColor = texture(test_texture, outTexCoord);
    } else {
        fragColor = texture(texture_sampler, outTexCoord);
    }
}