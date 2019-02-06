package com.GlitchyDev.World;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;

import java.util.ArrayList;

public class Region {
    private final BlockBase[][][] blocks;
    private final ArrayList<EntityBase> entities;



    public Region(BlockBase[][][] blocks, ArrayList<EntityBase> entities) {
        this.blocks = blocks;
        this.entities = entities;
    }



    // Helper Methods
    public BlockBase getBlockRelative(int relativeX, int relativeY, int relativeZ) {
        return blocks[relativeY][relativeX][relativeZ];
    }

    public void setBlockRelative(int relativeX, int relativeY, int relativeZ, BlockBase block) {
        blocks[relativeY][relativeX][relativeZ] = block;
    }




    // Getters

    public BlockBase[][][] getBlocks() {
        return blocks;
    }
    public ArrayList<EntityBase> getEntities() {
        return entities;
    }
}
