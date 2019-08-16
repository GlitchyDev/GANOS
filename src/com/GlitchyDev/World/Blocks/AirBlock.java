package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class AirBlock extends Block {


    public AirBlock(WorldGameState worldGameState, Location location, UUID regionUUID) {
        super(BlockType.AIR,worldGameState, location, regionUUID);
    }

    public AirBlock(WorldGameState worldGameState, InputBitUtility inputBitUtility, UUID regionUUID) throws IOException {
        super(BlockType.AIR,worldGameState, inputBitUtility, regionUUID);


    }


    @Override
    public void writeData(OutputBitUtility outputBitUtility, boolean isReplicated) throws IOException {
        super.writeData(outputBitUtility, isReplicated);
    }

    @Override
    public Block getCopy() {
        return new AirBlock(worldGameState, getLocation(), regionUUID);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof AirBlock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBlockType());
    }
}
