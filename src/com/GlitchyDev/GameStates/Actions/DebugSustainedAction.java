package com.GlitchyDev.GameStates.Actions;

import com.GlitchyDev.GameStates.Abstract.ActionableGameState;
import com.GlitchyDev.GameStates.Actions.Abstract.StateActionIdentifier;
import com.GlitchyDev.GameStates.Actions.Abstract.StateSustainedAction;
import com.GlitchyDev.Rendering.Assets.Texture.Texture;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11C.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11C.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13C.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13C.glActiveTexture;

public class DebugSustainedAction extends StateSustainedAction {
    private final Texture primaryImage;
    private final Texture bitmap;
    private final Texture replacementImage;

    private final SpriteItem spriteItem;

    public DebugSustainedAction(ActionableGameState actionableGameState) {
        super(StateActionIdentifier.DEBUG_SUSTAINED_ACTION, actionableGameState);
        primaryImage = AssetLoader.getTextureAsset("Thade_Color");
        bitmap = AssetLoader.getTextureAsset("Thade_Bitmap");
        replacementImage = AssetLoader.getTextureAsset("GlitchEffect");

        spriteItem = new SpriteItem(primaryImage,true);
    }

    public DebugSustainedAction(ActionableGameState actionableGameState, InputBitUtility inputBitUtility) {
        super(StateActionIdentifier.DEBUG_SUSTAINED_ACTION, actionableGameState, inputBitUtility);
        primaryImage = AssetLoader.getTextureAsset("Thade_Color");
        bitmap = AssetLoader.getTextureAsset("Thade_Bitmap");
        replacementImage = AssetLoader.getTextureAsset("GlitchEffect");

        spriteItem = new SpriteItem(primaryImage,true);
    }

    @Override
    public void onTick() {
        System.out.println(frameCount);
        frameCount++;
    }


    int frameCount = 0;
    @Override
    public void onRender(Renderer renderer) {

        System.out.println(bitmap.getId() + " " + replacementImage.getId() + " " + 0.5f);
        renderer.getShader("DebugShader2D").bind();
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, bitmap.getId());
        renderer.getShader("DebugShader2D").setUniform("bitmap",1);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, replacementImage.getId());
        renderer.getShader("DebugShader2D").setUniform("replacementImage", 2);
        renderer.getShader("DebugShader2D").setUniform("replacementOffset", new Vector2f(((float)Math.random()),((float)Math.random())));
        renderer.getShader("DebugShader2D").setUniform("replacementScaling", new Vector2f(((float)Math.random()),((float)Math.random())));
        //renderer.getShader("DebugShader2D").setUniform("replacementScaling", new Vector2f(1.0f,1.0f));

        renderer.render2DSpriteItem(spriteItem,"DebugShader2D");
    }

    @Override
    public void onApplication() {
        System.out.println("Successfully applied");
    }
}
