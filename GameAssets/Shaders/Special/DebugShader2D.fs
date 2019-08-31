#version 330

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform sampler2D bitmap;
uniform sampler2D replacementImage;
uniform vec2 replacementOffset;
uniform vec2 replacementScaling;
uniform float debugScaling;






void main()
{
    if(texture(texture_sampler, outTexCoord).a > 0.0) {
        if(texture(bitmap, outTexCoord).r == 0.0) {
            fragColor = texture(texture_sampler, outTexCoord);
        } else {
          fragColor = texture(replacementImage, outTexCoord * replacementScaling + replacementOffset + 1.0/debugScaling * texture(bitmap, outTexCoord).g);
        }
    }
}