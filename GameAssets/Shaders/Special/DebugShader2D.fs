#version 330

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform sampler2D bitmap;
uniform sampler2D replacementImage;
uniform vec2 y;





void main()
{
    if(texture(texture_sampler, outTexCoord).a > 0.1) {
        fragColor = texture(texture_sampler, outTexCoord);
    } else {
        fragColor = texture(replacementImage, outTexCoord);

    }
}