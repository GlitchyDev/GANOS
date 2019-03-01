package com.GlitchyDev.World.Entities.Effects;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Entities.Effects.Enums.EffectType;
import com.GlitchyDev.World.Region.RegionConnectionType;

import java.io.IOException;

public class DebugSeeEffect extends EffectBase implements RegionRevealingEffect {

    public DebugSeeEffect(EffectType effectType, WorldGameState worldGameState, EntityBase entity) {
        super(effectType, worldGameState, entity);
    }

    public DebugSeeEffect(WorldGameState worldGameState, EntityBase entity, InputBitUtility inputBitUtility) {
        super(EffectType.SEE_DEBUG_1, worldGameState, entity, inputBitUtility);
    }


    @Override
    public boolean doShowRegionConnection(RegionConnectionType regionConnectionType) {
        return regionConnectionType.isVisibleByDefault() || regionConnectionType == RegionConnectionType.HIDDEN_DEBUG_1;
    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
        //
    }
}
