package com.GlitchyDev.Networking.Sockets;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ClientWorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Client.Authentication.ClientAuthGreetingPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.GeneralAuthDisconnectPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Server.Authentication.ServerAuthAcceptClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class ClientGameSocket extends GameSocketBase {


    public ClientGameSocket(UUID playerUUID, ClientWorldGameState worldGameState, InetAddress ipAddress, int port) throws IOException {
        super(worldGameState, ipAddress, port);

        // Intial Client Connection
        sendPacket(new ClientAuthGreetingPacket(playerUUID));
        while(hasUnprocessedPackets()) {
            Thread.yield();
        }
        PacketBase initPacket = getUnprocessedPackets().get(0);
        if(initPacket instanceof ServerAuthAcceptClient) {
            notifyConnection();
        } else {
            if(initPacket instanceof GeneralAuthDisconnectPacket) {
                notifyDisconnect(((GeneralAuthDisconnectPacket) initPacket).getDisconnectType());
            }
        }
    }


    public void notifyConnection() {
        if(worldGameState instanceof ClientWorldGameState) {
            ((ClientWorldGameState) worldGameState).connectedToServer();
        }
    }

    @Override
    public void notifyDisconnect(NetworkDisconnectType networkDisconnectType) {
        if(worldGameState instanceof ClientWorldGameState) {
            ((ClientWorldGameState) worldGameState).disconnectedFromServer();
        }
    }

}
