package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.IO.InputBitUtility;
import com.GlitchyDev.IO.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.EntityType;
import com.GlitchyDev.World.Location;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
