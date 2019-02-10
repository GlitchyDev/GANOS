package com.GlitchyDev.World;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Region.RegionBase;

import java.util.ArrayList;

public class Chunk extends RegionBase {

    public Chunk(int width, int length, int height) {
        super(new BlockBase[height][width][length], new ArrayList<>());
    }




}
