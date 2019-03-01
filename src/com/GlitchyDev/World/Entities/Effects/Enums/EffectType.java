package com.GlitchyDev.World.Entities.Effects.Enums;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.Effects.DebugSeeEffect;

public enum EffectType {
    SEE_DEBUG_1,
    TICK_DEBUG,


    ;

    public EffectBase getEffectFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState, EntityBase entity) {
        switch(this) {
            case SEE_DEBUG_1:
                return new DebugSeeEffect(worldGameState, entity, inputBitUtility);
            case TICK_DEBUG:
            default:
                return null;
        }
    }
}
