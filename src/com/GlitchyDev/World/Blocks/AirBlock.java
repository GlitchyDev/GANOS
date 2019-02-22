package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;

public class AirBlock extends BlockBase {


    public AirBlock(WorldGameState worldGameState, Location location) {
        super(worldGameState, BlockType.AIR, location);
    }

    public AirBlock(WorldGameState worldGameState, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, BlockType.AIR, inputBitUtility);


    }


    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
    }

    @Override
    public BlockBase getCopy() {
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
