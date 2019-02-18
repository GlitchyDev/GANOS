package com.GlitchyDev.Rendering.Assets;

public class InstancedGridTexture extends Texture {
    private final int texWidth;
    private final int texHeight;
    private final String name;

    public InstancedGridTexture(Texture texture, String name, int texWidth, int texHeight) {
        super(texture);
        this.name = name;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }


    public int getTextureGridWidth() {
        return texWidth;
    }

    public int getTextureGridHeight() {
        return texHeight;
    }


    public float getTexturePortionWidth() {
        return 1.0f/super.getWidth();
    }


    public float getTexturePortionHeight() {
        return 1.0f/super.getHeight();
    }

    public int getCubeSideLength() {
        return getWidth()/texWidth;
    }

    public String getName() {
        return name;
    }
}
