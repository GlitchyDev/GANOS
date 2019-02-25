package com.GlitchyDev.Networking.Packets.Client.Authentication;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;
import java.util.UUID;

public class ClientAuthGreetingPacket extends PacketBase {
    private final UUID playerUUID;

    public ClientAuthGreetingPacket(UUID playerUUID) {
        super(PacketType.CLIENT_AUTH_GREETING_PACKET);
        this.playerUUID = playerUUID;
    }

    public ClientAuthGreetingPacket(InputBitUtility inputBitUtility) throws IOException {
        super(PacketType.CLIENT_AUTH_GREETING_PACKET, inputBitUtility);
        this.playerUUID = inputBitUtility.getNextUUID();
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(playerUUID);
    }

    @Override
    public String toString() {
        return super.toString() + "," + playerUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
