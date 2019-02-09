package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.IO.InputBitUtility;
import com.GlitchyDev.IO.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.BlockType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DebugBlock extends BlockBase {

    public DebugBlock(Location location) {
        super(BlockType.DEBUG, location);
    }


    @Override
    public void readData(InputBitUtility inputBitUtility) {

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
