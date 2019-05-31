package com.GlitchyDev.World.Elements.Communication.Sound.Communication.Source;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.SourceType;

public class SoundBlockSource extends SoundSourceBase {
    private final BlockBase blockSource;

    public SoundBlockSource(BlockBase blockSource) {
        super(SourceType.ENTITY);
        this.blockSource = blockSource;
    }

    public BlockBase getBlockSource() {
        return blockSource;
    }

}
