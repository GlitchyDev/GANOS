package com.GlitchyDev.Old.World;


/**
 * A wrapper for a Chunks Location
 */
public class ChunkCord {
    private int x;
    private int z;

    public ChunkCord(int x, int z)
    {
        this.x = x;
        this.z = z;
    }

    public ChunkCord getOffset(int xOffset, int zOffset)
    {
        return new ChunkCord(this.x + x, this.z + z);
    }

    @Override
    public String toString() {
        return x + "," + z;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
