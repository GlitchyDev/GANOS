package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;

public class AirBlock extends BlockBase {


    public AirBlock(Location location) {
        super(BlockType.AIR, location);
    }

    public AirBlock(InputBitUtility inputBitUtility) throws IOException {
        super(BlockType.AIR, inputBitUtility);


    }


    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
    }

    @Override
    public BlockBase getCopy() {
        return new AirBlock(getLocation().clone());
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
