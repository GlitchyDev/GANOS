package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public abstract class DesignerBlock extends Block implements LightableBlock {
    private boolean[] faceStates;
    private int[] textureID;
    private int[] lightLevels;
    private InstancedGridTexture instancedGridTexture;

    public DesignerBlock(BlockType blockType,WorldGameState worldGameState, Location location, UUID regionUUID, InstancedGridTexture instancedGridTexture) {
        super(blockType, worldGameState, location, regionUUID);
        faceStates = new boolean[6];
        textureID = new int[6];
        lightLevels = new int[6];
        this.instancedGridTexture = instancedGridTexture;
    }

    public DesignerBlock(BlockType blockType, WorldGameState worldGameState, InputBitUtility inputBitUtility, UUID regionUUID, InstancedGridTexture instancedGridTexture) throws IOException {
        super(blockType, worldGameState, inputBitUtility, regionUUID);
        faceStates = new boolean[6];
        textureID = new int[6];
        lightLevels = new int[6];
        this.instancedGridTexture = instancedGridTexture;

        for(Direction direction: Direction.getCompleteCardinal()) {
            boolean faceState =  inputBitUtility.getNextBit();
            setFaceState(direction,faceState);
            if(faceState) {
                setTextureID(direction,inputBitUtility.getNextInteger());
            }
        }
    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
        for(Direction direction: Direction.getCompleteCardinal()) {
            outputBitUtility.writeNextBit(getFaceState(direction));
            if(getFaceState(direction)) {
                outputBitUtility.writeNextInteger(getTextureID(direction));
            }
        }
    }

    protected void copyInformation(DesignerBlock destinationBlock) {
        for(Direction direction: Direction.getCompleteCardinal()) {
            destinationBlock.setFaceState(direction,getFaceState(direction));
            destinationBlock.setTextureID(direction,getTextureID(direction));
        }
    }

    @Override
    public void setBlockLight(Direction direction, int lightLevel) {
        lightLevels[direction.ordinal()] = lightLevel;
    }

    @Override
    public int getLightLevel(Direction direction) {
        return lightLevels[direction.ordinal()];
    }

    @Override
    public void resetLight() {
        for(Direction direction: Direction.getCompleteCardinal()) {
            lightLevels[direction.ordinal()] = 0;
        }
    }




    public void setFaceState(Direction direction, boolean state) {
        faceStates[direction.ordinal()] = state;
    }

    public boolean getFaceState(Direction direction) {
        return faceStates[direction.ordinal()];
    }

    public void setTextureID(Direction direction, int texture) {
        textureID[direction.ordinal()] = texture;
    }

    public int getTextureID(Direction direction) {
        return textureID[direction.ordinal()];
    }

    public InstancedGridTexture getInstancedGridTexture() {
        return instancedGridTexture;
    }

    public void setInstancedGridTexture(InstancedGridTexture instancedGridTexture) {
        this.instancedGridTexture = instancedGridTexture;
    }
}
