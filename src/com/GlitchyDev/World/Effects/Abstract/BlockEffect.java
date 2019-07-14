package com.GlitchyDev.World.Effects.Abstract;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Effects.Enums.EffectType;

public abstract class BlockEffect extends Effect {
    private Block block;
    public BlockEffect(EffectType effectType, boolean isReplicated, WorldGameState worldGameState) {
        super(effectType, isReplicated, worldGameState);
    }

    public BlockEffect(EffectType effectType, boolean isReplicated, WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        super(effectType, isReplicated, worldGameState,inputBitUtility);
    }

    public void applyBlockEffect(Block block) {
        onBlockApplyEffect(block);
        this.block = block;
        if(this instanceof TickableEffect) {
            worldGameState.getWorld(block.getLocation().getWorldUUID()).getTickableEffects().add((TickableEffect) this);
        }
    }
    public void removeBlockEffect() {
        onBlockRemoveEffect(block);
        if(this instanceof TickableEffect) {
            worldGameState.getWorld(block.getLocation().getWorldUUID()).getTickableEffects().remove(this);
        }
        this.block = null;
    }

    protected abstract void onBlockApplyEffect(Block block);
    protected abstract void onBlockRemoveEffect(Block Block);

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }
}
