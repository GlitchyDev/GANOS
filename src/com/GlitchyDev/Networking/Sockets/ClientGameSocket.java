package com.GlitchyDev.Networking.Sockets;

import com.GlitchyDev.GameStates.Abstract.Replicated.ClientWorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Client.Authentication.ClientAuthGreetingPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Server.Authentication.ServerAuthAcceptClient;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public class ClientGameSocket extends GameSocketBase {


    public ClientGameSocket(UUID playerUUID, ClientWorldGameState worldGameState, InetAddress ipAddress, int port) throws IOException {
        super(worldGameState, ipAddress, port);

        // Intial Client Connection
        System.out.println("ClientSocket: Sending Auth Information");
        sendPacket(new ClientAuthGreetingPacket(playerUUID));
        while(!hasUnprocessedPackets()) {
            Thread.yield();
        }
        PacketBase initPacket = getUnprocessedPackets().get(0);
        if(initPacket instanceof ServerAuthAcceptClient) {
            System.out.println("ClientSocket: Received reply! Accepted!");

            notifyConnection();
        }
    }


    public void notifyConnection() {
        if(worldGameState instanceof ClientWorldGameState) {
            ((ClientWorldGameState) worldGameState).onConnectedToServer();
        }
    }

    @Override
    public void notifyDisconnect(NetworkDisconnectType networkDisconnectType) {
        if(worldGameState instanceof ClientWorldGameState) {
            ((ClientWorldGameState) worldGameState).onDisconnectFromServer();
        }
    }

}
