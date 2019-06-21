package com.GlitchyDev.Networking.Packets.Server.World.Entity;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;

import java.io.IOException;
import java.util.UUID;

public class ServerDespawnEntityPacket extends WorldStateModifyingPackets {
    private final UUID entityUUID;
    private final UUID worldUUID;

    public ServerDespawnEntityPacket(UUID entityUUID, UUID worldUUID) {
        super(PacketType.SERVER_DESPAWN_ENTITY);
        this.entityUUID = entityUUID;
        this.worldUUID = worldUUID;
    }

    public ServerDespawnEntityPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_DESPAWN_ENTITY, inputBitUtility, worldGameState);
        this.entityUUID = inputBitUtility.getNextUUID();
        this.worldUUID = inputBitUtility.getNextUUID();
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.despawnEntity(entityUUID,worldUUID, DespawnReason.PACKET_DESPAWN);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(entityUUID);
        outputBitUtility.writeNextUUID(worldUUID);
    }

    @Override
    public String toString() {
        return super.toString() + "," + entityUUID + "," + worldUUID;
    }
}
