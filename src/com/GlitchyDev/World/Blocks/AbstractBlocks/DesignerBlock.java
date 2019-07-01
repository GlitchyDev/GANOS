package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public abstract class DesignerBlock extends Block {
    private boolean[] faceStates;
    private int[] textureID;
    private InstancedGridTexture instancedGridTexture;

    public DesignerBlock(WorldGameState worldGameState, BlockType blockType, Location location, UUID regionUUID, InstancedGridTexture instancedGridTexture) {
        super(worldGameState, blockType, location, regionUUID);
        faceStates = new boolean[6];
        textureID = new int[6];
        this.instancedGridTexture = instancedGridTexture;
    }

    public DesignerBlock(WorldGameState worldGameState, BlockType blockType, UUID regionUUID, InputBitUtility inputBitUtility, InstancedGridTexture instancedGridTexture) throws IOException {
        super(worldGameState, blockType, regionUUID, inputBitUtility);
        faceStates = new boolean[6];
        textureID = new int[6];
        this.instancedGridTexture = instancedGridTexture;
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
