package com.GlitchyDev.Networking.Packets.Server.World.Effect;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.Abstract.EntityEffect;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;

import java.io.IOException;
import java.util.UUID;

public class ServerRemoveEntityEffectPacket extends WorldStateModifyingPackets {
    private final EntityEffect entityEffect;
    private final UUID entityUUID;
    private final UUID worldUUID;


    public ServerRemoveEntityEffectPacket(EntityEffect entityEffect, Entity entity) {
        super(PacketType.SERVER_REMOVE_ENTITY_EFFECT);
        this.entityEffect = entityEffect;
        System.out.println(entityEffect);
        this.entityUUID = entity.getUUID();
        this.worldUUID = entity.getWorldUUID();
    }

    public ServerRemoveEntityEffectPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_REMOVE_ENTITY_EFFECT, inputBitUtility, worldGameState);
        EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
        this.entityEffect = (EntityEffect) effectType.getEffectFromInput(inputBitUtility,worldGameState);
        this.entityUUID = inputBitUtility.getNextUUID();
        this.worldUUID = inputBitUtility.getNextUUID();
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        Entity entity = worldGameState.getWorld(worldUUID).getEntity(entityUUID);
        entityEffect.setEntity(worldGameState.getWorld(worldUUID).getEntity(entityUUID));
        EntityEffect foundEffect = null;
        for(Effect effect: entity.getEffects()) {
            if(effect.getEffectType() == entityEffect.getEffectType()) {
                foundEffect = (EntityEffect) effect;
            }
        }
        foundEffect.removeEntityEffect();
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        entityEffect.writeData(outputBitUtility);
        outputBitUtility.writeNextUUID(entityUUID);
        outputBitUtility.writeNextUUID(worldUUID);
    }
}
