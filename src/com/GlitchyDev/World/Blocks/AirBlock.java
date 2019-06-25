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


    public AirBlock(WorldGameState worldGameState, Location location) {
        super(worldGameState, BlockType.AIR, location);
    }

    public AirBlock(WorldGameState worldGameState, UUID regionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, BlockType.AIR, regionUUID, inputBitUtility);


    }


    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
    }

    @Override
    public Block getCopy() {
        return new AirBlock(worldGameState, getLocation().clone());
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
