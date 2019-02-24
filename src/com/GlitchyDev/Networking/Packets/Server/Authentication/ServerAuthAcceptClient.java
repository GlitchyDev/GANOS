package com.GlitchyDev.Networking.Packets.Server.Authentication;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public class ServerAuthAcceptClient extends PacketBase {
    public ServerAuthAcceptClient() {
        super(PacketType.SERVER_AUTH_ACCEPT_CLIENT);
    }

    public ServerAuthAcceptClient(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(inputBitUtility, worldGameState);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        // Remember, packet is just a notifier
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
