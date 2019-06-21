package com.GlitchyDev.World.Effects.Abstract;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Effects.Enums.EffectType;

import java.io.IOException;

public  abstract class Effect {
    private final EffectType effectType;
    private final WorldGameState worldGameState;

    public Effect(EffectType effectType, WorldGameState worldGameState) {
        this.effectType = effectType;
        this.worldGameState = worldGameState;

    }

    public Effect(EffectType effectType, WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        this.effectType = effectType;
        this.worldGameState = worldGameState;

    }

    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(effectType.ordinal());
    }

    public EffectType getEffectType() {
        return effectType;
    }

}
