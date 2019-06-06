package com.GlitchyDev.World.Effects;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.Abstract.RegionHidingEffect;
import com.GlitchyDev.World.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Region.RegionConnection;

public class ServerDebugEffect extends Effect implements RegionRevealingEffect, RegionHidingEffect {
    public ServerDebugEffect(WorldGameState worldGameState, Entity entity) {
        super(EffectType.SERVER_DEBUG_EFFECT, worldGameState, entity);
    }

    public ServerDebugEffect(WorldGameState worldGameState, Entity entity, InputBitUtility inputBitUtility) {
        super(EffectType.SERVER_DEBUG_EFFECT, worldGameState, entity, inputBitUtility);
    }

    @Override
    public boolean doHideRegionConnection(RegionConnection regionConnection) {
        return regionConnection == RegionConnection.VISIBLE_DEBUG_1;
    }

    @Override
    public boolean doShowRegionConnection(RegionConnection regionConnection) {
        return regionConnection.isVisibleByDefault() || regionConnection == RegionConnection.HIDDEN_DEBUG_1;
    }
}
