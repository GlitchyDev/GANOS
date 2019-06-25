package com.GlitchyDev.World.Effects;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Effects.Abstract.EntityEffect;
import com.GlitchyDev.World.Effects.Abstract.RegionHidingEffect;
import com.GlitchyDev.World.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Region.Enum.RegionConnection;

public class ServerDebugEffect extends EntityEffect implements RegionRevealingEffect, RegionHidingEffect {
    public ServerDebugEffect(WorldGameState worldGameState) {
        super(EffectType.SERVER_DEBUG_EFFECT, false, worldGameState);
    }

    public ServerDebugEffect(WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        super(EffectType.SERVER_DEBUG_EFFECT, false, worldGameState, inputBitUtility);
    }

    @Override
    public boolean doHideRegionConnection(RegionConnection regionConnection) {
        return regionConnection == RegionConnection.VISIBLE_DEBUG_1;
    }

    @Override
    public boolean doShowRegionConnection(RegionConnection regionConnection) {
        return regionConnection.isVisibleByDefault() || regionConnection == RegionConnection.HIDDEN_DEBUG_1;
    }

    @Override
    public void onEntityApplyEffect(Entity entity) {
        // NA
    }

    @Override
    public void onEntityRemoveEffect(Entity entity) {
        // NA
    }
}
