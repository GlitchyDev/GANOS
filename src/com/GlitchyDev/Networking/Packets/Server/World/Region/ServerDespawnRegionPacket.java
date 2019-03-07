package com.GlitchyDev.Networking.Packets.Server.World.Region;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;
import java.util.UUID;

public class ServerDespawnRegionPacket extends WorldStateModifyingPackets {
    private final UUID regionUUID;
    private final UUID worldUUID;

    public ServerDespawnRegionPacket(UUID regionUUID, UUID worldUUID) {
        super(PacketType.SERVER_DESPAWN_REGION);
        this.regionUUID = regionUUID;
        this.worldUUID = worldUUID;
    }

    public ServerDespawnRegionPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_DESPAWN_REGION, inputBitUtility, worldGameState);
        this.regionUUID = inputBitUtility.getNextUUID();
        this.worldUUID = inputBitUtility.getNextUUID();
        inputBitUtility.complete();
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.removeRegionFromGame(regionUUID,worldUUID);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(regionUUID);
        outputBitUtility.writeNextUUID(worldUUID);
    }

    @Override
    public String toString() {
        return super.toString() + "," + regionUUID + "," + worldUUID;
    }

}
