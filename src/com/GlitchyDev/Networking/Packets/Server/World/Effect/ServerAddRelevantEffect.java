package com.GlitchyDev.Networking.Packets.Server.World.Effect;

import com.GlitchyDev.GameStates.Abstract.Replicated.ClientWorldGameState;
import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Effects.Abstract.EntityEffect;
import com.GlitchyDev.World.Effects.Enums.EffectType;

import java.io.IOException;

public class ServerAddRelevantEffect extends WorldStateModifyingPackets {
    private final EntityEffect effect;

    public ServerAddRelevantEffect(EntityEffect effect) {
        super(PacketType.SERVER_ADD_RELEVANT_EFFECT);
        this.effect = effect;
    }

    public ServerAddRelevantEffect(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_ADD_RELEVANT_EFFECT, inputBitUtility, worldGameState);
        EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
        effect = (EntityEffect) effectType.getEffectFromInput(inputBitUtility, worldGameState);
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        ((ClientWorldGameState)worldGameState).getReliventEffects().add(effect);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        effect.writeData(outputBitUtility);
    }
}
