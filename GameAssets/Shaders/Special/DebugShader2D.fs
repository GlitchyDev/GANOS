#version 330

in vec2 outTexCoord;
in vec3 mvPos;
out vec4 fragColor;

uniform sampler2D texture_sampler;
uniform sampler2D bitmap;
uniform sampler2D effect1;
uniform sampler2D effect2;
uniform sampler2D effect3;


uniform float frame;




void main()
{
    if(texture(bitmap, outTexCoord).a < 0.1) {
        fragColor = texture(texture_sampler, outTexCoord);
    } else {
        if(texture(bitmap, outTexCoord).r > 0.1) {
            fragColor = texture(effect1, vec2(outTexCoord.x + frame*3,outTexCoord.y + frame*1) * 3);
        } else {
            if(texture(bitmap, outTexCoord).g > 0.1) {
                fragColor = texture(effect1, vec2(outTexCoord.x - frame*2,outTexCoord.y + frame*2) * 2);
             } else {
                fragColor = texture(effect1, vec2(outTexCoord.x + frame*1,outTexCoord.y - frame*1));
             }
        }
    }
}