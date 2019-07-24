package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public abstract class DesignerBlock extends Block implements LightableBlock {
    private boolean[] faceStates;
    private int[] textureID;
    private int[] staticLightLevels;
    private int[] dynamicLightLevels;
    private int[] currentLightLevels;
    private int[] previousLightLevels;
    private InstancedGridTexture instancedGridTexture;

    public DesignerBlock(BlockType blockType,WorldGameState worldGameState, Location location, UUID regionUUID, InstancedGridTexture instancedGridTexture) {
        super(blockType, worldGameState, location, regionUUID);
        faceStates = new boolean[6];
        textureID = new int[6];
        staticLightLevels = new int[6];
        dynamicLightLevels = new int[6];
        currentLightLevels = new int[6];
        previousLightLevels = new int[6];
        this.instancedGridTexture = instancedGridTexture;
    }

    // Reminder, light blocks will be without light on spawn

    public DesignerBlock(BlockType blockType, WorldGameState worldGameState, InputBitUtility inputBitUtility, UUID regionUUID, InstancedGridTexture instancedGridTexture) throws IOException {
        super(blockType, worldGameState, inputBitUtility, regionUUID);
        faceStates = new boolean[6];
        textureID = new int[6];
        staticLightLevels = new int[6];
        dynamicLightLevels = new int[6];
        currentLightLevels = new int[6];
        previousLightLevels = new int[6];
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

    //*********************

    @Override
    public void setStaticLightLevel(Direction direction, int lightLevel) {
        staticLightLevels[direction.ordinal()] = lightLevel;
    }
    @Override
    public void setDynamicLightLevel(Direction direction, int lightLevel) {
        dynamicLightLevels[direction.ordinal()] = lightLevel;
    }
    @Override
    public void setCurrentLightLevel(Direction direction, int lightLevel) {
        currentLightLevels[direction.ordinal()] = lightLevel;
    }


    // To check if a light is minimal
    @Override
    public int getStaticLightLevel(Direction direction) {
        return staticLightLevels[direction.ordinal()];
    }
    @Override
    public int getDynamicLightLevel(Direction direction) {
        return dynamicLightLevels[direction.ordinal()];
    }
    @Override
    public int getCurrentLightLevel(Direction direction) {
        return currentLightLevels[direction.ordinal()];
    }
    @Override
    public int getPreviousLightLevel(Direction direction) {
        return previousLightLevels[direction.ordinal()];
    }

    // Since resetting Static lights is unnecessary
    @Override
    public void resetDynamicLight() {
        Arrays.fill(dynamicLightLevels,0);
        for(Direction direction: Direction.getCompleteCardinal()) {
            currentLightLevels[direction.ordinal()] = staticLightLevels[direction.ordinal()];
        }
    }

    @Override
    public void finalizeLight() {
        for(Direction direction: Direction.getCompleteCardinal()) {
            currentLightLevels[direction.ordinal()] = Math.max(staticLightLevels[direction.ordinal()], dynamicLightLevels[direction.ordinal()]);
            previousLightLevels[direction.ordinal()] = currentLightLevels[direction.ordinal()];
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
