package com.GlitchyDev.World.Entities.Effects.Enums;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.Effects.ServerDebugEffect;

public enum EffectType {
    SERVER_DEBUG_EFFECT,


    ;

    public EffectBase getEffectFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState, EntityBase entity) {
        switch(this) {
            case SERVER_DEBUG_EFFECT:
                return new ServerDebugEffect(worldGameState, entity, inputBitUtility);
            default:
                return null;
        }
    }
}
