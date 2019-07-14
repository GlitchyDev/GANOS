package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class DesignerDebugBlock extends DesignerBlock {

    public DesignerDebugBlock(WorldGameState worldGameState, Location location, UUID regionUUID) {
        super(worldGameState, BlockType.DESIGNER_DEBUG_BLOCK, location, regionUUID, AssetLoader.getInstanceGridTexture("School_Tiles"));
    }

    public DesignerDebugBlock(WorldGameState worldGameState, InputBitUtility inputBitUtility, UUID regionUUID) throws IOException {
        super(BlockType.DESIGNER_DEBUG_BLOCK,worldGameState, inputBitUtility, regionUUID, AssetLoader.getInstanceGridTexture("School_Tiles"));
    }

    @Override
    public Block getCopy() {
        DesignerDebugBlock designerDebugBlock = new DesignerDebugBlock(worldGameState,location,regionUUID);
        copyInformation(designerDebugBlock);
        return designerDebugBlock;
    }


    @Override
    public int hashCode() {
        return Objects.hash(getBlockType());
    }



    @Override
    public boolean equals(Object obj) {
        return obj instanceof DesignerDebugBlock;
    }
}
