package com.GlitchyDev.World.Effects.Enums;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.ServerDebugEffect;
import com.GlitchyDev.World.Effects.ServerPacketReplicationEffect;

public enum EffectType {
    SERVER_DEBUG_EFFECT,
    SERVER_PACKET_REPLICATION


    ;

    public Effect getEffectFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState) {
        switch(this) {
            case SERVER_DEBUG_EFFECT:
                return new ServerDebugEffect(worldGameState, inputBitUtility);
            case SERVER_PACKET_REPLICATION:
                return new ServerPacketReplicationEffect(worldGameState,inputBitUtility);
            default:
                return null;
        }
    }
}
