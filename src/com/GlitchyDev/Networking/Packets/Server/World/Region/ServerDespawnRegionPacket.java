package com.GlitchyDev.Networking.Packets.Server.World.Region;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public class ServerDespawnRegionPacket extends PacketBase {
    private final int despawnID;

    public ServerDespawnRegionPacket(int despawnID) {
        super(PacketType.SERVER_DESPAWN_REGION);
        this.despawnID = despawnID;
    }

    public ServerDespawnRegionPacket(InputBitUtility inputBitUtility) throws IOException {
        super(inputBitUtility);
        this.despawnID = inputBitUtility.getNextCorrectIntByte();
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(despawnID);
    }

    @Override
    public String toString() {
        return super.toString() + "," + despawnID;
    }

    public int getDespawnID() {
        return despawnID;
    }
}
