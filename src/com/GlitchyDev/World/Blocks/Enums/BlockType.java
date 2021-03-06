package com.GlitchyDev.World.Blocks.Enums;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.*;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;

import java.io.IOException;
import java.util.UUID;

public enum BlockType {
    DEBUG,
    AIR,
    DEBUG_CUSTOM_RENDER,
    DESIGNER_DEBUG_BLOCK,
    DEBUG_NAVIGATION_BLOCK,
    ;




    public Block getBlockFromInput(WorldGameState worldGameState, UUID regionUUID, InputBitUtility inputBitUtility) throws IOException {
        switch(this) {
            case DEBUG:
                return new DebugBlock(worldGameState, inputBitUtility, regionUUID);
            case AIR:
                return new AirBlock(worldGameState, inputBitUtility, regionUUID);
            case DEBUG_CUSTOM_RENDER:
                return new DebugCustomRenderBlock(worldGameState, inputBitUtility, regionUUID);
            case DESIGNER_DEBUG_BLOCK:
                return new DesignerDebugBlock(worldGameState, inputBitUtility, regionUUID);
            case DEBUG_NAVIGATION_BLOCK:
                return new DebugNavigationBlock(worldGameState,inputBitUtility,regionUUID);
            default:
                System.out.println("ERROR BlockType " + this + " not registered");
                return new AirBlock(worldGameState, inputBitUtility, regionUUID);
        }

    }
}
