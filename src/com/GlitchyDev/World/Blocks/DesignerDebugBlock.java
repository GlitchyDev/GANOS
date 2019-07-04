package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class DesignerDebugBlock extends DesignerBlock {

    public DesignerDebugBlock(WorldGameState worldGameState, Location location, UUID regionUUID) {
        super(worldGameState, BlockType.DESIGNER_DEBUG_BLOCK, location, regionUUID, AssetLoader.getInstanceGridTexture("School_Tiles"));
    }

    public DesignerDebugBlock(WorldGameState worldGameState, UUID regionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, BlockType.DESIGNER_DEBUG_BLOCK, regionUUID, inputBitUtility, AssetLoader.getInstanceGridTexture("School_Tiles"));
    }

    @Override
    public Block getCopy() {
        return new DesignerDebugBlock(worldGameState,location,regionUUID);
    }


    @Override
    public int hashCode() {
        return Objects.hash(getBlockType());
    }


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DesignerDebugBlock) {
            if(((DesignerDebugBlock) obj).getInstancedGridTexture().equals(getInstancedGridTexture())) {
                for (Direction direction : Direction.values()) {
                    if (((DesignerDebugBlock) obj).getFaceState(direction) == getFaceState(direction)) {
                        if (getFaceState(direction)) {
                            if (((DesignerDebugBlock) obj).getTextureID(direction) != getTextureID(direction)) {
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
