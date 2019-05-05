package com.GlitchyDev.Rendering;

import com.GlitchyDev.Rendering.Assets.Texture.RenderBuffer;
import com.GlitchyDev.Rendering.Assets.Texture.Texture;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Utility.GameWindow;

import static org.lwjgl.opengl.GL11.glViewport;

public class RenderPanel {
    private final RenderBuffer renderBuffer;
    private SpriteItem spriteItem;
    private int width;
    private int height;
    private int x;
    private int y;
    private int layer = 0;


    public RenderPanel(int width, int height, int x, int y) {
        renderBuffer = new RenderBuffer(width,height);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        updateSpriteItem();
    }


    private void updateSpriteItem() {
        if(spriteItem != null) {
            spriteItem.cleanup();
        }
        spriteItem = new SpriteItem(new Texture(renderBuffer),width,height,false);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
        spriteItem.setPosition(0,0,(layer/256));
    }

    public void renderToScreen(Renderer renderer, GameWindow gameWindow, String shader) {
        glViewport (x, y, width, height);
        renderer.render2DSprite(spriteItem, shader);
        glViewport (0, 0, gameWindow.getWidth(), gameWindow.getHeight());
    }

    /*
    You can enable from another panel, but before rendering the panel must disable the last one
     */
    public void enable() {
        renderBuffer.bindToRender();
    }

    public void disable(GameWindow gameWindow) {
        renderBuffer.unbindToRender(gameWindow.getWidth(), gameWindow.getHeight());
    }
}
