package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

public class AirBlock extends BlockBase {


    public AirBlock(Location location) {
        super(BlockType.AIR, location);
    }


    @Override
    public void readData(InputBitUtility inputBitUtility) {

    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility) {

    }

    @Override
    public BlockBase clone() {
        return null;
    }

    @Override
    public boolean isEqual(EntityBase entityBase) {
        return false;
    }
}
