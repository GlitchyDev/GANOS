package com.GlitchyDev.World.Effects.Abstract;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;

public abstract class BlockEffect extends Effect {
    private Block block;
    public BlockEffect(EffectType effectType, WorldGameState worldGameState) {
        super(effectType, worldGameState);
    }

    public BlockEffect(EffectType effectType, WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        super(effectType, worldGameState, inputBitUtility);
    }

    public void applyBlockEffect(Block block) {
        this.block = block;
    }
    public void removeBlockEffect() {
        this.block = null;
    }

    public abstract void onBlockApplyEffect(Entity entity);
    public abstract void onBlockRemoveEffect(Entity entity);

    public Block getBlock() {
        return block;
    }
}
