package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.World.Blocks.BlockType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class BlockBase {
    private final BlockType blockType;
    private Location location;
    protected boolean isDisableFrustumCulling = false;
    protected boolean insideFrustum = false;

    public BlockBase(BlockType blockType, Location location) {
        this.blockType = blockType;
        this.location = location;
    }

    public abstract void readData(ObjectInputStream objectInputStream);

    // Do not write Location, as that can be refereed engineered from the read protocol
    public abstract void writeData(ObjectOutputStream objectOutputStream);

    public abstract BlockBase clone();





    // Getters
    public boolean isDisableFrustumCulling() {
        return isDisableFrustumCulling;
    }

    public boolean isInsideFrustum() {
        return insideFrustum;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public BlockType getBlockType() {
        return blockType;
    }
}
