package com.GlitchyDev.World.Effects;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Effects.Abstract.EntityEffect;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;

public class ServerPacketReplicationEffect extends EntityEffect {
    public ServerPacketReplicationEffect(WorldGameState worldGameState) {
        super(EffectType.SERVER_PACKET_REPLICATION, true, worldGameState);
    }

    public ServerPacketReplicationEffect(WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        super(EffectType.SERVER_PACKET_REPLICATION, true, worldGameState, inputBitUtility);
    }

    @Override
    protected void onEntityApplyEffect(Entity entity) {
        System.out.println("Applied effect to " + entity);
    }

    @Override
    protected void onEntityRemoveEffect(Entity entity) {
        System.out.println("Removed effect from " + entity);
    }
}
