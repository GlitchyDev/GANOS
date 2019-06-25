package com.GlitchyDev.World.Blocks.Enums;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Blocks.DebugBlock;
import com.GlitchyDev.World.Blocks.DebugCustomRenderBlock;

import java.io.IOException;
import java.util.UUID;

public enum BlockType {
    DEBUG,
    AIR,
    DEBUG_CUSTOM_RENDER,

    ;




    public Block getBlockFromInput(WorldGameState worldGameState, UUID reguinUUID, InputBitUtility inputBitUtility) throws IOException {
        switch(this) {
            case DEBUG:
                return new DebugBlock(worldGameState,reguinUUID, inputBitUtility);
            case AIR:
                return new AirBlock(worldGameState,reguinUUID, inputBitUtility);
            case DEBUG_CUSTOM_RENDER:
                return new DebugCustomRenderBlock(worldGameState,reguinUUID,inputBitUtility);
            default:
                System.out.println("ERROR BlockType " + this + " not registered");
                return new AirBlock(worldGameState,reguinUUID, inputBitUtility);
        }

    }
}
