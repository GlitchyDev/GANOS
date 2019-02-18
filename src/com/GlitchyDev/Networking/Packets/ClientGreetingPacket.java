package com.GlitchyDev.Networking.Packets;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;
import java.util.UUID;

public class ClientGreetingPacket extends PacketBase {
    private final UUID playerUUID;

    public ClientGreetingPacket(UUID playerUUID) {
        super(PacketType.CLIENT_GREETING_PACKET);
        this.playerUUID = playerUUID;
    }

    public ClientGreetingPacket(InputBitUtility inputBitUtility) throws IOException {
        super(PacketType.CLIENT_GREETING_PACKET);
        playerUUID = inputBitUtility.getNextUUID();
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(playerUUID);
    }

    @Override
    public String toString() {
        return super.toString() + "," + playerUUID;
    }

}
