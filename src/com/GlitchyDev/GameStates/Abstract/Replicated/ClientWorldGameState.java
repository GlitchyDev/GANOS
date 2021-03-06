package com.GlitchyDev.GameStates.Abstract.Replicated;

import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.GameStates.Abstract.ActionableGameState;
import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Sockets.ClientGameSocket;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.Abstract.EntityEffect;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.UUID;

public abstract class ClientWorldGameState extends ActionableGameState {
    private ClientGameSocket gameSocket;
    private ArrayList<Effect> relivantEffects;

    public ClientWorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
        relivantEffects = new ArrayList<>();
    }

    @Override
    public void logic() {
        if(gameSocket != null && gameSocket.isConnected() && gameSocket.hasUnprocessedPackets()) {
            for (PacketBase packet : gameSocket.getUnprocessedPackets()) {
                processPacket(packet);
            }
        }
    }

    public abstract void processPacket(PacketBase packet);


    protected final void connectToServer(UUID playerUUID, ClientWorldGameState worldGameState, InetAddress ipAddress, int port) {
        try {
            gameSocket = new ClientGameSocket(playerUUID, worldGameState, ipAddress, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    protected final void disconnectFromServer(NetworkDisconnectType reason) {
        try {
            gameSocket.disconnect(reason);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public abstract void onConnectedToServer();

    public abstract void onDisconnectFromServer();

    protected final ClientGameSocket getGameSocket() {
        return gameSocket;
    }

    public final ArrayList<Effect> getRelevantEffects() {
        return relivantEffects;
    }

    public final void addRelevantEffect(EntityEffect effect) {
        relivantEffects.add(effect);
    }

    public final void removeRelevantEffect(EntityEffect effect) {
        relivantEffects.remove(effect);
    }



    // Send input to server
}
