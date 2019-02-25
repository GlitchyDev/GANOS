package com.GlitchyDev.Networking.Sockets;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.ServerNetworkManager;

import java.io.IOException;
import java.net.Socket;

public class ServerGameSocket extends GameSocketBase {
    private final ServerNetworkManager serverNetworkManager;

    public ServerGameSocket(WorldGameState worldGameState, Socket socket, ServerNetworkManager serverNetworkManager) throws IOException {
        super(worldGameState, socket);
        this.serverNetworkManager = serverNetworkManager;

    }

    @Override
    public void notifyDisconnect(NetworkDisconnectType networkDisconnectType) {
        serverNetworkManager.notifyDisconnectedPlayer(this,networkDisconnectType);
    }


}