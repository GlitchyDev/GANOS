package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;

public class DebugBlock extends BlockBase {
    private final int testValue;

    public DebugBlock(Location location, int testValue) {
        super(BlockType.DEBUG, location);
        this.testValue = testValue;
    }

    public DebugBlock(InputBitUtility inputBitUtility) throws IOException {
        super(BlockType.DEBUG, inputBitUtility);
        testValue = inputBitUtility.getNextCorrectIntByte();
    }


    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
        outputBitUtility.writeNextCorrectByteInt(testValue);
    }

    @Override
    public BlockBase getCopy() {
        return new DebugBlock(getLocation().clone(), testValue);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DebugBlock) {
            return ((DebugBlock)obj).getTestValue() == getTestValue();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBlockType(), getTestValue());
    }

    public int getTestValue() {
        return testValue;
    }
}
