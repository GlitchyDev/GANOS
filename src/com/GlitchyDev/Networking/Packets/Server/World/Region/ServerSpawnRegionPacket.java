package com.GlitchyDev.Networking.Packets.Server.World.Region;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Region.RegionBase;

import java.io.IOException;
import java.util.UUID;

public class ServerSpawnRegionPacket extends WorldStateModifyingPackets {
    private final RegionBase region;
    private final UUID worldUUID;

    public ServerSpawnRegionPacket(RegionBase region) {
        super(PacketType.SERVER_SPAWN_REGION);
        this.worldUUID = region.getWorldUUID();
        this.region = region;

    }

    public ServerSpawnRegionPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_SPAWN_REGION, inputBitUtility, worldGameState);
        this.worldUUID = inputBitUtility.getNextUUID();
        region = new RegionBase(inputBitUtility, worldGameState, worldUUID);
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.spawnRegion(region, worldUUID);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(worldUUID);
        region.writeData(outputBitUtility);
    }

    @Override
    public String toString() {
        return super.toString() + "," + region + "," + worldUUID;
    }
}
