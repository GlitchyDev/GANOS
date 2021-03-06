package com.GlitchyDev.Networking.Packets.Server.World;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;
import java.util.UUID;

public class ServerDespawnWorldPacket extends WorldStateModifyingPackets {
    private final UUID worldUUID;

    public ServerDespawnWorldPacket(UUID worldUUID) {
        super(PacketType.SERVER_SPAWN_WORLD);
        this.worldUUID = worldUUID;
    }

    public ServerDespawnWorldPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_SPAWN_WORLD, inputBitUtility, worldGameState);
        this.worldUUID = inputBitUtility.getNextUUID();
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.getWorlds().remove(worldUUID);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(worldUUID);
    }

    public String toString() {
        return super.toString() + "," + worldUUID + "," + worldUUID;
    }

}
