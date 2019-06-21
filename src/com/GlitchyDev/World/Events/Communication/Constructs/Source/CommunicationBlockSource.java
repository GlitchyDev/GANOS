package com.GlitchyDev.World.Events.Communication.Constructs.Source;

import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Events.Communication.Constructs.Enums.SourceType;

public class CommunicationBlockSource extends CommunicationSource {
    private final Block blockSource;

    public CommunicationBlockSource(Block blockSource) {
        super(SourceType.ENTITY);
        this.blockSource = blockSource;
    }

    public Block getBlockSource() {
        return blockSource;
    }

}
