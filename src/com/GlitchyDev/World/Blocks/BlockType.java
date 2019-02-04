package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;

public enum BlockType {
    DEBUG,
    AIR;



    public BlockBase getBlockClass() {
        switch(this) {
            case DEBUG:
                return new DebugBlock(null);
            case AIR:
                return new AirBlock(null);
            default:
                return new DebugBlock(null);
        }

    }
}
