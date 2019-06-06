package com.GlitchyDev.World.Effects.Abstract;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Effects.Enums.EffectType;

import java.io.IOException;

public  abstract class Effect {
    private final EffectType effectType;
    private final WorldGameState worldGameState;
    private final Entity entity;

    public Effect(EffectType effectType, WorldGameState worldGameState, Entity entity) {
        this.effectType = effectType;
        this.worldGameState = worldGameState;
        this.entity = entity;
    }

    public Effect(EffectType effectType, WorldGameState worldGameState, Entity entity, InputBitUtility inputBitUtility) {
        this.effectType = effectType;
        this.worldGameState = worldGameState;
        this.entity = entity;
    }

    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(effectType.ordinal());
    }

    public abstract void applyEffect();



    public EffectType getEffectType() {
        return effectType;
    }

    protected Entity getEntity() {
        return entity;
    }
}
