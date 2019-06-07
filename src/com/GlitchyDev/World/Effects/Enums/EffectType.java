package com.GlitchyDev.World.Effects.Enums;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.ServerDebugEffect;

public enum EffectType {
    SERVER_DEBUG_EFFECT,


    ;

    public Effect getEffectFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState) {
        switch(this) {
            case SERVER_DEBUG_EFFECT:
                return new ServerDebugEffect(worldGameState, inputBitUtility);
            default:
                return null;
        }
    }
}
