package com.GlitchyDev.World.Blocks.Enums;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Blocks.DebugBlock;
import com.GlitchyDev.World.Blocks.DebugCustomRenderBlock;

import java.io.IOException;

public enum BlockType {
    DEBUG,
    AIR,
    DEBUG_CUSTOM_RENDER,

    ;




    public BlockBase getBlockFromInput(WorldGameState worldGameState, InputBitUtility inputBitUtility) throws IOException {
        switch(this) {
            case DEBUG:
                return new DebugBlock(worldGameState, inputBitUtility);
            case AIR:
                return new AirBlock(worldGameState, inputBitUtility);
            case DEBUG_CUSTOM_RENDER:
                return new DebugCustomRenderBlock(worldGameState,inputBitUtility);
            default:
                System.out.println("ERROR BlockType " + this + " not registered");
                return new AirBlock(worldGameState, inputBitUtility);
        }

    }
}
