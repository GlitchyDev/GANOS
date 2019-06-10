package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;

public abstract class Block {
    protected final WorldGameState worldGameState;

    private final BlockType blockType;
    private Location location; // Remember, when loading from Region, its location is its offset in the region
    // No loaded variables
    private boolean isDisableFrustumCulling = false;
    private boolean insideFrustum = false;
    // IS OPAQUE?

    public Block(WorldGameState worldGameState, BlockType blockType, Location location) {
        this.worldGameState = worldGameState;
        this.blockType = blockType;
        this.location = location;
    }

    public Block(WorldGameState worldGameState, BlockType blockType, InputBitUtility inputBitUtility) throws IOException {
        this.worldGameState = worldGameState;

        this.blockType = blockType;
        this.location = new Location(0,0,0,null);
    }

    // Do not write Location, as that can be refereed engineered from the read protocol
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(blockType.ordinal());
    }

    // DO NOT CLONE LOCATION OR FRUSTUM CULLING/ECT
    public abstract Block getCopy();

    // Do not include location or disable fustruum culling, ect


    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();




//
    // Getters


    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public boolean isDisableFrustumCulling() {
        return isDisableFrustumCulling;
    }

    public void setDisableFrustumCulling(boolean disableFrustumCulling) {
        isDisableFrustumCulling = disableFrustumCulling;
    }

    public boolean isInsideFrustum() {
        return insideFrustum;
    }

    public void setInsideFrustum(boolean insideFrustum) {
        this.insideFrustum = insideFrustum;
    }

    @Override
    public String toString() {
        return "e@" + blockType + "," + location;
    }
}