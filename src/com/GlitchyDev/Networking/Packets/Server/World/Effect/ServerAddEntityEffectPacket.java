package com.GlitchyDev.Networking.Packets.Server.World.Effect;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Effects.Abstract.EntityEffect;
import com.GlitchyDev.World.Effects.Enums.EffectType;

import java.io.IOException;
import java.util.UUID;

public class ServerAddEntityEffectPacket extends WorldStateModifyingPackets {
    private final EntityEffect entityEffect;
    private final UUID entityUUID;
    private final UUID worldUUID;


    public ServerAddEntityEffectPacket(EntityEffect entityEffect) {
        super(PacketType.SERVER_ADD_ENTITY_EFFECT);
        this.entityEffect = entityEffect;
        this.entityUUID = entityEffect.getEntity().getUUID();
        this.worldUUID = entityEffect.getEntity().getWorldUUID();
    }

    public ServerAddEntityEffectPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_ADD_ENTITY_EFFECT, inputBitUtility, worldGameState);
        EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
        this.entityEffect = (EntityEffect) effectType.getEffectFromInput(inputBitUtility,worldGameState);
        this.entityUUID = inputBitUtility.getNextUUID();
        this.worldUUID = inputBitUtility.getNextUUID();
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.getWorld(worldUUID).getEntity(entityUUID).applyEffect(entityEffect);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        entityEffect.writeData(outputBitUtility);
        outputBitUtility.writeNextUUID(entityUUID);
        outputBitUtility.writeNextUUID(worldUUID);
    }
}
