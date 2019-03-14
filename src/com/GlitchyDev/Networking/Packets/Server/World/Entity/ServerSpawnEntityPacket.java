package com.GlitchyDev.Networking.Packets.Server.World.Entity;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;

import java.io.IOException;
import java.util.UUID;

public class ServerSpawnEntityPacket extends WorldStateModifyingPackets {
    private final EntityBase entityBase;

    public ServerSpawnEntityPacket(EntityBase entityBase) {
        super(PacketType.SERVER_SPAWN_ENTITY);
        this.entityBase = entityBase;
    }

    // PacketType | WorldUUID | RegionUUID | EntityData
    public ServerSpawnEntityPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_SPAWN_ENTITY, inputBitUtility, worldGameState);
        UUID worldUUID = inputBitUtility.getNextUUID();
        UUID regionUUID = inputBitUtility.getNextUUID();
        EntityType entityType = EntityType.values()[inputBitUtility.getNextCorrectIntByte()];
        this.entityBase = entityType.getEntityFromInput(inputBitUtility,worldGameState,worldUUID, regionUUID);
        inputBitUtility.complete();
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.spawnEntity(entityBase, SpawnReason.PACKET_SPAWN);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(entityBase.getLocation().getWorldUUID());
        outputBitUtility.writeNextUUID(entityBase.getCurrentRegionUUID());
        entityBase.writeData(outputBitUtility);
        //entityBase.writeData(outputBitUtility);
    }

    @Override
    public String toString() {
        return super.toString() + "," + entityBase;
    }
}
