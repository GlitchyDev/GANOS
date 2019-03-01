package com.GlitchyDev.World.Entities.Effects.Abstract;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Effects.Enums.EffectType;
import com.GlitchyDev.Game.Player.Player;

import java.io.IOException;

public class EffectBase {
    private final EffectType effectType;
    private final WorldGameState worldGameState;
    private final EntityBase entity;

    public EffectBase(EffectType effectType, WorldGameState worldGameState, EntityBase entity) {
        this.effectType = effectType;
        this.worldGameState = worldGameState;
        this.entity = entity;
    }

    public EffectBase(EffectType effectType, WorldGameState worldGameState, EntityBase entity, InputBitUtility inputBitUtility) {
        this.effectType = effectType;
        this.worldGameState = worldGameState;
        this.entity = entity;
    }

    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(effectType.ordinal());
    }



    public EffectType getEffectType() {
        return effectType;
    }

    protected EntityBase getEntity() {
        return entity;
    }
}
