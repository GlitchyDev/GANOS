#version 330

in vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform sampler2D test_texture;



void main()
{

    fragColor = texture(texture_sampler, outTexCoord) * texture(test_texture, outTexCoord);

}