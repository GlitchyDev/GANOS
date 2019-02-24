package com.GlitchyDev.Game.GameStates.Abstract.Replicated;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
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
            processOutput();
        }
    }

    public abstract void processPacket(PacketBase packet);
    // ((WorldStateModifyingPackets) packet).executeModification(this);

    public abstract void processOutput();

    protected void connectToServer(UUID playerUUID, ClientWorldGameState worldGameState, InetAddress ipAddress, int port) {
        try {
            this.gameSocket = new ClientGameSocket(playerUUID, worldGameState, ipAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public abstract void connectedToServer();

    public abstract void disconnectedFromServer();

    // Send input to server
}
