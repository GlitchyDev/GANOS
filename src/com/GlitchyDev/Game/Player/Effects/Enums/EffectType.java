package com.GlitchyDev.Game.Player.Effects.Enums;

import com.GlitchyDev.Game.Player.Effects.Abstract.EffectBase;
import com.GlitchyDev.Utility.InputBitUtility;

public enum EffectType {
    SEE_DEBUG_1,
    TICK_DEBUG,

    ;

    public EffectBase getEffectFromInput(InputBitUtility inputBitUtility) {
        switch(this) {
            case SEE_DEBUG_1:
            case TICK_DEBUG:
            default:
                return null;
        }
    }
}
