package com.GlitchyDev.GameStates.Actions;

import com.GlitchyDev.GameStates.Abstract.ActionableGameState;
import com.GlitchyDev.GameStates.Actions.Abstract.StateActionIdentifier;
import com.GlitchyDev.GameStates.Actions.Abstract.StateSustainedAction;
import com.GlitchyDev.Rendering.Assets.Texture.Texture;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;

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
        renderer.getShader("DebugShader2D").setUniform("bitmap",bitmap.getId());
        renderer.getShader("DebugShader2D").setUniform("replacementImage",replacementImage.getId());
        renderer.getShader("DebugShader2D").setUniform("progress",0.5f);
        renderer.render2DSpriteItem(spriteItem,"DebugShader2D");
    }

    @Override
    public void onApplication() {
        System.out.println("Successfully applied");
    }
}
