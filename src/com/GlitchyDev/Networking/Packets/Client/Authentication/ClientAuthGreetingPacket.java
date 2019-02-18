package com.GlitchyDev.Networking.Packets.Client.Authentication;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public class ClientAuthGreetingPacket extends PacketBase {
    private final String playerName;

    public ClientAuthGreetingPacket(String playerName) {
        super(PacketType.CLIENT_AUTH_GREETING_PACKET);
        this.playerName = playerName;
    }

    public ClientAuthGreetingPacket(InputBitUtility inputBitUtility) throws IOException {
        super(PacketType.CLIENT_AUTH_GREETING_PACKET);
        playerName = inputBitUtility.getNextString();
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextString(playerName);
    }

    @Override
    public String toString() {
        return super.toString() + "," + playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
