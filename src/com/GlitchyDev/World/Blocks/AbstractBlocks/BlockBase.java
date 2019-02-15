package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.BlockType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.IOException;

public abstract class BlockBase {
    private final BlockType blockType;
    // Remember, when loading from Region, its location is its offset in the region
    private Location location;
    private boolean isDisableFrustumCulling = false;
    private boolean insideFrustum = false;
    // IS OPAQUE?

    public BlockBase(BlockType blockType, Location location) {
        this.blockType = blockType;
        this.location = location;
    }

    public BlockBase(BlockType blockType, InputBitUtility inputBitUtility) throws IOException {
        this.blockType = blockType;
        location = new Location(0,0,0,null);
    }

    // Do not write Location, as that can be refereed engineered from the read protocol
    public abstract void writeData(OutputBitUtility outputBitUtility);

    // DO NOT CLONE LOCATION OR FRUSTUM CULLING/ECT
    public abstract BlockBase clone();

    // Do not include location or disable fustruum culling, ect
    public abstract boolean isEqual(EntityBase entityBase);



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
