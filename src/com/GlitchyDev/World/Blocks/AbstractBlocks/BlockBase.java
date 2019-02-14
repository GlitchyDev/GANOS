package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.BlockType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

public abstract class BlockBase {
    private final BlockType blockType;
    private Location location;
    private boolean isDisableFrustumCulling = false;
    private boolean insideFrustum = false;
    // IS OPAQUE?

    public BlockBase(BlockType blockType, Location location) {
        this.blockType = blockType;
        this.location = location;
    }

    public abstract void readData(InputBitUtility inputBitUtility);

    // Do not write Location, as that can be refereed engineered from the read protocol
    public abstract void writeData(OutputBitUtility outputBitUtility);

    public abstract BlockBase clone();

    public abstract boolean isEqual(EntityBase entityBase);



//
    // Getters


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
}
