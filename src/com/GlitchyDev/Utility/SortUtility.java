package com.GlitchyDev.Utility;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;

import java.util.*;

public class SortUtility {


    public static void sort(BlockBase[] blocks, int[] frequencies) {
        ArrayList<BlockValue> blockValues = new ArrayList<>(blocks.length);
        for(int i = 0; i < blocks.length; i++) {
            blockValues.add(new BlockValue(blocks[i],frequencies[i]));
        }


        Collections.sort(blockValues, (o1, o2) -> {
            if(o1.getFrequency() > o2.getFrequency()) {
                return 1;
            }
            if(o1.getFrequency() < o2.getFrequency()) {
                return -1;
            }
            return 0;
        });

        Collections.reverse(blockValues);



        for(int i = 0; i < blockValues.size(); i++) {
            blocks[i] = (blockValues.get(i).getBlock());
            frequencies[i] = (blockValues.get(i).getFrequency());
        }
    }

    private static class BlockValue {
        private final BlockBase block;
        private final int frequency;

        public BlockValue(BlockBase block, int frequency) {
            this.block = block;
            this.frequency = frequency;
        }

        public BlockBase getBlock() {
            return block;
        }

        public int getFrequency() {
            return frequency;
        }
    }

}
