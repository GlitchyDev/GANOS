package com.GlitchyDev.World;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;

import java.util.ArrayList;

public class Chunk extends Region {

    public Chunk(int width, int length, int height) {
        super(new BlockBase[height][width][length], new ArrayList<>());
    }




}
