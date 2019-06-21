package com.GlitchyDev.Networking.Packets.Server.World.Region;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.UUID;

public class ServerSpawnRegionPacket extends WorldStateModifyingPackets {
    private final Location location;
    private final Region region;

    public ServerSpawnRegionPacket(Region region) {
        super(PacketType.SERVER_SPAWN_REGION);
        this.location = region.getLocation();
        this.region = region;

    }

    public ServerSpawnRegionPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_SPAWN_REGION, inputBitUtility, worldGameState);
        int x = inputBitUtility.getNextInteger();
        int y = inputBitUtility.getNextInteger();
        int z = inputBitUtility.getNextInteger();
        UUID worldUUID = inputBitUtility.getNextUUID();
        this.location = new Location(x,y,z,worldUUID);
        region = new Region(inputBitUtility, location, worldGameState);
        inputBitUtility.complete();
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
