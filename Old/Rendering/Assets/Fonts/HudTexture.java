package com.GlitchyDev.Old.Rendering.Assets.Fonts;

import com.GlitchyDev.Old.Rendering.Assets.Texture;

import java.util.HashMap;
import java.util.Map;

/**
 * A abstract class that facilitates the management of Font based Textures for use in a HudItem
 */
public abstract class HudTexture {
    protected final String fontName;
    protected final Map<Character, CharInfo> charMap;
    protected Texture texture;
    protected int height;
    protected int width;



    public HudTexture(String fontName)
    {
        this.fontName = fontName;
        charMap = new HashMap<>();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Texture getTexture() {
        return texture;
    }

    public CharInfo getCharInfo(char c) {
        return charMap.get(c);
    }

    protected abstract void buildTexture();

    protected abstract String getAllAvailableChars(String charsetName);

    public static class CharInfo {
        private final int startX;
        private final int width;
        private final int leftBufferWidth;
        private final int rightBufferWidth;

        public CharInfo(int startX, int width) {
            this.startX = startX;
            this.width = width;
            this.leftBufferWidth = 0;
            this.rightBufferWidth = 0;
        }

        public CharInfo(int startX, int width, int leftBufferWidth, int rightBufferWidth) {
            this.startX = startX;
            this.width = width;
            this.leftBufferWidth = leftBufferWidth;
            this.rightBufferWidth = rightBufferWidth;
        }


        public int getStartX() {
            return startX;
        }

        public int getWidth() {
            return width;
        }

        public int getLeftBufferWidth(){
            return leftBufferWidth;
        }

        public int getRightBufferWidth(){
            return rightBufferWidth;
        }

    }
}
