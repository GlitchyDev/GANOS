package com.GlitchyDev.World.Blocks.Enums;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Blocks.DebugBlock;

import java.io.IOException;

public enum BlockType {
    DEBUG,
    AIR;



    public BlockBase getBlockFromInput(InputBitUtility inputBitUtility) throws IOException {
        switch(this) {
            case DEBUG:
                return new DebugBlock(inputBitUtility);
            case AIR:
                return new AirBlock(inputBitUtility);
            default:
                return new AirBlock(inputBitUtility);
        }

    }
}
