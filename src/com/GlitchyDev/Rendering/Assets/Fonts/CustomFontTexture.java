package com.GlitchyDev.Rendering.Assets.Fonts;

import com.GlitchyDev.Old.IO.AssetLoader;
import com.GlitchyDev.Rendering.Assets.Texture;


public class CustomFontTexture extends HudTexture {


    public CustomFontTexture(String customFont) {
        super(customFont);
        buildTexture();
    }


    public Texture getTexture() {
        return texture;
    }

    public CharInfo getCharInfo(char c) {
        return charMap.get(c);
    }

    protected String getAllAvailableChars(String charsetName) {
        return String.join("@",AssetLoader.getConfigListAsset(charsetName + "Config"));
    }

    protected void buildTexture() {
        // Get the font metrics for each character for the selected font by using image
        texture = AssetLoader.getTextureAsset(fontName);

        // Possibly integrate into a array method
        String allChars = getAllAvailableChars(fontName);
        this.width = 0;
        this.height = texture.getHeight();
        for (String s : allChars.split("@")) {
            // Get the size for each character and update global image size
            String[] formatted = s.split("\\|");
            int leftCharSpacing = Integer.valueOf(formatted[1]);
            int charWidth = Integer.valueOf(formatted[2]);
            int rightCharSpacing = Integer.valueOf(formatted[3]);

            CharInfo charInfo = new CharInfo(width, charWidth, leftCharSpacing, rightCharSpacing);
            for(char c: formatted[0].toCharArray()) {
                charMap.put(c, charInfo);
            }
            width += charInfo.getWidth();

        }

    }


}
