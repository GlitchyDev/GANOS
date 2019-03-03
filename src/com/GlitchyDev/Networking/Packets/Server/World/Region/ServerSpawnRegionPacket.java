package com.GlitchyDev.Networking.Packets.Server.World.Region;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;

import java.io.IOException;

public class ServerSpawnRegionPacket extends WorldStateModifyingPackets {
    private final Location location;
    private final RegionBase region;

    public ServerSpawnRegionPacket(RegionBase region) {
        super(PacketType.SERVER_SPAWN_REGION);
        this.location = region.getLocation();
        this.region = region;

    }

    public ServerSpawnRegionPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_SPAWN_REGION, inputBitUtility, worldGameState);
        this.location = new Location(inputBitUtility.getNextInteger(), inputBitUtility.getNextInteger(), inputBitUtility.getNextInteger(), inputBitUtility.getNextUUID());
        region = new RegionBase(inputBitUtility, location, worldGameState);
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.addRegionToGame(region);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextInteger(location.getX());
        outputBitUtility.writeNextInteger(location.getY());
        outputBitUtility.writeNextInteger(location.getZ());
        outputBitUtility.writeNextUUID(location.getWorldUUID());
        region.writeData(outputBitUtility);
    }

    @Override
    public String toString() {
        return super.toString() + "," + region;
    }
}
