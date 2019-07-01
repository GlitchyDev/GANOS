package com.GlitchyDev.Rendering.Assets.Texture;

public class InstancedGridTexture extends Texture {
    private final int horizontalGridNam;
    private final int verticalGridNum;

    public InstancedGridTexture(Texture texture, int horizontalGridNam, int verticalGridNum) {
        super(texture);
        this.horizontalGridNam = horizontalGridNam;
        this.verticalGridNum = verticalGridNum;
    }



    public float getGridWidthPercent() {
        return 1.0f/super.getWidth();
    }
    public float getGridHeightPercent() {
        return 1.0f/super.getHeight();
    }


    public int getHorizontalGridNam() {
        return horizontalGridNam;
    }

    public int getVerticalGridNum() {
        return verticalGridNum;
    }
}
