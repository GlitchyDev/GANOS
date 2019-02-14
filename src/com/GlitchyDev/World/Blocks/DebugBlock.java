package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.IOException;

public class DebugBlock extends BlockBase {

    public DebugBlock(Location location) {
        super(BlockType.DEBUG, location);
    }

    public DebugBlock(InputBitUtility inputBitUtility) throws IOException {
        super(BlockType.DEBUG, inputBitUtility);
    }


    @Override
    public void writeData(OutputBitUtility outputBitUtility) {

    }

    @Override
    public BlockBase clone() {
        return new DebugBlock(getLocation().clone());
    }

    @Override
    public boolean isEqual(EntityBase entityBase) {
        return false;
    }
}
