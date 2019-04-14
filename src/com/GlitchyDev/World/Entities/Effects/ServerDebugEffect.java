package com.GlitchyDev.World.Entities.Effects;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionHidingEffect;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Entities.Effects.Enums.EffectType;
import com.GlitchyDev.World.Region.RegionConnectionType;

public class ServerDebugEffect extends EffectBase implements RegionRevealingEffect, RegionHidingEffect {
    public ServerDebugEffect(WorldGameState worldGameState, EntityBase entity) {
        super(EffectType.SERVER_DEBUG_EFFECT, worldGameState, entity);
    }

    public ServerDebugEffect(WorldGameState worldGameState, EntityBase entity, InputBitUtility inputBitUtility) {
        super(EffectType.SERVER_DEBUG_EFFECT, worldGameState, entity, inputBitUtility);
    }

    @Override
    public boolean doHideRegionConnection(RegionConnectionType regionConnectionType) {
        return regionConnectionType == RegionConnectionType.VISIBLE_DEBUG_1;
    }

    @Override
    public boolean doShowRegionConnection(RegionConnectionType regionConnectionType) {
        return regionConnectionType.isVisibleByDefault() || regionConnectionType == RegionConnectionType.HIDDEN_DEBUG_1;
    }
}
