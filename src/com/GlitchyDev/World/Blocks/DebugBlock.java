package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomRenderBlock;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;

public class DebugBlock extends Block implements CustomRenderBlock {
    private int testValue;
    private final GameItem mesh;

    public DebugBlock(WorldGameState worldGameState, Location location, int testValue) {
        super(worldGameState, BlockType.DEBUG, location);
        this.testValue = testValue;
        this.mesh = new GameItem(AssetLoader.getMeshAsset("CubicMesh1").clone());
        switch(testValue) {
            case 0:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon16x16"));
                break;
            case 1:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon24x24"));
                break;
            case 2:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon32x32"));
                break;
            default:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Test_Floor_" + testValue));
        }
        mesh.setPosition(location.getNormalizedPosition());
    }

    public DebugBlock(WorldGameState worldGameState, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, BlockType.DEBUG, inputBitUtility);
        testValue = inputBitUtility.getNextCorrectIntByte();
        this.mesh = new GameItem(AssetLoader.getMeshAsset("CubicMesh1").clone());
        switch(testValue) {
            case 0:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon16x16"));
                break;
            case 1:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon24x24"));
                break;
            case 2:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon32x32"));
                break;
            default:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Test_Floor_" + testValue));
        }
        mesh.setPosition(getLocation().getNormalizedPosition());
    }


    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
        outputBitUtility.writeNextCorrectByteInt(testValue);
    }

    @Override
    public Block getCopy() {
        return new DebugBlock(worldGameState, getLocation().clone(), testValue);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DebugBlock) {
            return ((DebugBlock)obj).getTestValue() == getTestValue();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBlockType(), getTestValue());
    }

    public int getTestValue() {
        return testValue;
    }

    public void setTestValue(int newValue) {
        this.testValue = newValue;
        switch(testValue) {
            case 0:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon16x16"));
                break;
            case 1:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon24x24"));
                break;
            case 2:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon32x32"));
                break;
            default:
                mesh.getMesh().setTexture(AssetLoader.getTextureAsset("Test_Floor_" + testValue));
        }
    }

    @Override
    public void render(Renderer renderer, Camera camera, Player player) {
        renderer.render3DElement(camera, mesh, "Default3D");
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        mesh.setPosition(location.getNormalizedPosition());
    }

    @Override
    public String toString() {
        return super.toString() + "," + testValue;
    }
}
