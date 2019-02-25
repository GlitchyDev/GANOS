package com.GlitchyDev.Game.GameStates.Abstract.Replicated;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Sockets.ClientGameSocket;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Utility.GlobalGameData;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

public abstract class ClientWorldGameState extends WorldGameState {
    private ClientGameSocket gameSocket;

    public ClientWorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);

    }

    @Override
    public void logic() {
        if(gameSocket != null && gameSocket.isConnected()) {
            for (PacketBase packet : gameSocket.getUnprocessedPackets()) {
                processPacket(packet);
            }
        }
    }

    public abstract void processPacket(PacketBase packet);
    // ((WorldStateModifyingPackets) packet).executeModification(this);


    protected void connectToServer(UUID playerUUID, ClientWorldGameState worldGameState, InetAddress ipAddress, int port) {
        try {
            gameSocket = new ClientGameSocket(playerUUID, worldGameState, ipAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void disconnectFromServer(NetworkDisconnectType reason) {
        try {
            gameSocket.disconnect(reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public abstract void onConnectedToServer();

    public abstract void onDisconnectFromServer();

    // Send input to server
}