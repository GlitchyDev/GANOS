package com.GlitchyDev.Networking.Packets.Server.World.Entity;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.UUID;

public class ServerSpawnEntityPacket extends WorldStateModifyingPackets {
    private final Entity entity;

    public ServerSpawnEntityPacket(Entity entity) {
        super(PacketType.SERVER_SPAWN_ENTITY);
        this.entity = entity;
    }

    // PacketType | WorldUUID | RegionUUID | EntityData
    public ServerSpawnEntityPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_SPAWN_ENTITY, inputBitUtility, worldGameState);
        UUID worldUUID = inputBitUtility.getNextUUID();
        UUID regionUUID = inputBitUtility.getNextUUID();
        EntityType entityType = EntityType.values()[inputBitUtility.getNextCorrectIntByte()];
        this.entity = entityType.getEntityFromInput(inputBitUtility,worldGameState,worldUUID, regionUUID);
        inputBitUtility.complete();
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        Region hostRegion = worldGameState.getRegion(entity.getCurrentRegionUUID(),entity.getWorldUUID());
        entity.setLocation(hostRegion.getLocation().getOffsetLocation(entity.getLocation()));
        worldGameState.spawnEntity(entity, SpawnReason.PACKET_SPAWN);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(entity.getLocation().getWorldUUID());
        outputBitUtility.writeNextUUID(entity.getCurrentRegionUUID());
        entity.writeData(outputBitUtility,true);
        //entityBase.writeData(outputBitUtility);
    }

    @Override
    public String toString() {
        return super.toString() + "," + entity;
    }
}
