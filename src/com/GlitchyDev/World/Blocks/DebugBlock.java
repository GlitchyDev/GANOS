package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
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
import java.util.UUID;

public class DebugBlock extends Block implements CustomRenderBlock {
    private int testValue;
    private final GameItem gameItemMesh;

    public DebugBlock(WorldGameState worldGameState, Location location, UUID regionUUID, int testValue) {
        super(worldGameState, BlockType.DEBUG, location,regionUUID);
        this.testValue = testValue;
        this.gameItemMesh = new GameItem(AssetLoader.getMeshAsset("CubicMesh1").clone());
        switch(testValue) {
            case 0:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon16x16"));
                break;
            case 1:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon24x24"));
                break;
            case 2:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon32x32"));
                break;
            default:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Test_Floor_" + testValue));
        }
        gameItemMesh.setPosition(location.getNormalizedPosition());
    }

    public DebugBlock(WorldGameState worldGameState, UUID regionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, BlockType.DEBUG,regionUUID, inputBitUtility);
        testValue = inputBitUtility.getNextCorrectIntByte();
        this.gameItemMesh = new GameItem(AssetLoader.getMeshAsset("CubicMesh1").clone());
        switch(testValue) {
            case 0:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon16x16"));
                break;
            case 1:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon24x24"));
                break;
            case 2:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon32x32"));
                break;
            default:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Test_Floor_" + testValue));
        }
        gameItemMesh.setPosition(getLocation().getNormalizedPosition());
    }


    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
        outputBitUtility.writeNextCorrectByteInt(testValue);
    }

    @Override
    public Block getCopy() {
        return new DebugBlock(worldGameState, getLocation().clone(), regionUUID, testValue);
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
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon16x16"));
                break;
            case 1:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon24x24"));
                break;
            case 2:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Icon32x32"));
                break;
            default:
                gameItemMesh.getMesh().setTexture(AssetLoader.getTextureAsset("Test_Floor_" + testValue));
        }
    }

    @Override
    public void renderCustomBlock(Renderer renderer, Camera camera) {
        renderer.render3DElement(camera, gameItemMesh,"DefaultInstancing");
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        gameItemMesh.setPosition(location.getNormalizedPosition());
    }

    @Override
    public String toString() {
        return super.toString() + "," + testValue;
    }
}
