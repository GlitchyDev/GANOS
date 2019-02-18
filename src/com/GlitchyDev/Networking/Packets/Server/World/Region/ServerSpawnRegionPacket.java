package com.GlitchyDev.Networking.Packets.Server.World.Region;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Region.RegionBase;

import java.io.IOException;

public class ServerSpawnRegionPacket extends PacketBase {
    private final RegionBase region;

    public ServerSpawnRegionPacket(RegionBase region) {
        super(PacketType.SERVER_SPAWN_REGION);
        this.region = region;
    }

    public ServerSpawnRegionPacket(InputBitUtility inputBitUtility) throws IOException {
        super(inputBitUtility);
        region = new RegionBase(inputBitUtility);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        region.writeData(outputBitUtility);
    }

    @Override
    public String toString() {
        return super.toString() + "," + region;
    }

    public RegionBase getRegion() {
        return region;
    }
}
