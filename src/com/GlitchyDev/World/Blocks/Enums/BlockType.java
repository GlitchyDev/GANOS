package com.GlitchyDev.World.Blocks.Enums;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Blocks.DebugBlock;
import com.GlitchyDev.World.Blocks.DebugCustomRenderBlock;
import com.GlitchyDev.World.Blocks.DesignerDebugBlock;

import java.io.IOException;
import java.util.UUID;

public enum BlockType {
    DEBUG,
    AIR,
    DEBUG_CUSTOM_RENDER,
    DESIGNER_DEBUG_BLOCK,

    ;




    public Block getBlockFromInput(WorldGameState worldGameState, UUID regionUUID, InputBitUtility inputBitUtility) throws IOException {
        switch(this) {
            case DEBUG:
                return new DebugBlock(worldGameState, regionUUID, inputBitUtility);
            case AIR:
                return new AirBlock(worldGameState, regionUUID, inputBitUtility);
            case DEBUG_CUSTOM_RENDER:
                return new DebugCustomRenderBlock(worldGameState, regionUUID,inputBitUtility);
            case DESIGNER_DEBUG_BLOCK:
                return new DesignerDebugBlock(worldGameState,regionUUID,inputBitUtility);
            default:
                System.out.println("ERROR BlockType " + this + " not registered");
                return new AirBlock(worldGameState, regionUUID, inputBitUtility);
        }

    }
}
