package com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Source;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Enums.SourceType;

public class CommunicationBlockSource extends CommunicationSource {
    private final BlockBase blockSource;

    public CommunicationBlockSource(BlockBase blockSource) {
        super(SourceType.ENTITY);
        this.blockSource = blockSource;
    }

    public BlockBase getBlockSource() {
        return blockSource;
    }

}
