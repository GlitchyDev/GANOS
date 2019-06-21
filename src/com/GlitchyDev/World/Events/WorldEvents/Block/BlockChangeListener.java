package com.GlitchyDev.World.Events.WorldEvents.Block;

import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;

import java.util.UUID;

public interface BlockChangeListener {
    void onTriggerBlockChange(Block previousBlock, Block newBlock, UUID regionUUID);
}
