package com.GlitchyDev.Networking;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Client.Authentication.ClientAuthGreetingPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Server.Authentication.ServerAuthAcceptClient;
import com.GlitchyDev.Networking.Sockets.GameSocketBase;
import com.GlitchyDev.Networking.Sockets.ServerGameSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerNetworkManager {
    private final ServerWorldGameState worldGameState;
    private final int assignedPort;

    private final ConcurrentHashMap<UUID, GameSocketBase> connectedClients;
    private final Collection<UUID> connectedUsers;
    private final Collection<UUID> approvedUsers;

    private AcceptingClientThread acceptingClientThread;
    private AtomicBoolean acceptingClients = new AtomicBoolean(false);


    public ServerNetworkManager(ServerWorldGameState worldGameState, int assignedPort) {
        this.worldGameState = worldGameState;
        this.assignedPort = assignedPort;
        connectedClients = new ConcurrentHashMap<>();
        connectedUsers = Collections.synchronizedCollection(new ArrayList<>());
        approvedUsers = Collections.synchronizedCollection(new ArrayList<>());

    }


    public void enableAcceptingClients() {
        System.out.println("ServerNetwork: Enabled Login for Users");
        acceptingClientThread = new AcceptingClientThread(this);
        acceptingClients.set(true);
        acceptingClientThread.start();

    }

    public void disableAcceptingClients() {
        System.out.println("ServerNetwork: Disable Login for Users");
        acceptingClients.set(false);
        acceptingClientThread.stopServerSocket();
    }

    public void disconnectUser(UUID playerUUID, NetworkDisconnectType reason) {
        System.out.println("ServerNetwork: Disconnected User " + playerUUID + " for " + reason);
        connectedClients.get(playerUUID).disconnect(reason);
        connectedClients.remove(playerUUID);
        connectedUsers.remove(playerUUID);
    }

    public void disconnectAll(NetworkDisconnectType reason) {
        System.out.println("ServerNetwork: Disconnected All Users for " + reason);

        Iterator<UUID> userIterator = connectedUsers.iterator();
        while(userIterator.hasNext()) {
            disconnectUser(userIterator.next(), reason);
        }
    }

    public Collection<UUID> getApprovedUsers()
    {
        return approvedUsers;
    }

    public Collection<UUID> getConnectedUsers()
    {
        return connectedUsers;
    }

    public GameSocketBase getUsersGameSocket(UUID playerUUID)
    {
        return connectedClients.get(playerUUID);
    }

    public int getNumberOfConnectedUsers()
    {
        return connectedClients.size();
    }

    public void notifyConnectedPlayer(UUID playerUUID, ServerGameSocket gameSocket) {
        worldGameState.onPlayerLogin(playerUUID);

    }

    public void notifyDisconnectedPlayer(ServerGameSocket serverGameSocket, NetworkDisconnectType reason) {
        UUID belongingPlayerUUID = null;
        for(UUID uuid: connectedUsers) {
            if(connectedClients.get(uuid) == serverGameSocket) {
                belongingPlayerUUID = uuid;
                break;
            }
        }
        connectedUsers.remove(belongingPlayerUUID);
        connectedClients.remove(belongingPlayerUUID);
        worldGameState.onPlayerLogout(belongingPlayerUUID, reason);
    }





    private class AcceptingClientThread extends Thread {
        private ServerSocket serverSocket;
        private final ServerNetworkManager serverNetworkManager;

        public AcceptingClientThread(ServerNetworkManager serverNetworkManager) {
            this.serverNetworkManager = serverNetworkManager;
        }

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(assignedPort);
                while (acceptingClients.get()) {
                    Socket possibleSocket = serverSocket.accept();
                    ServerAuthenticationThread authenticatingClientThreat = new ServerAuthenticationThread(worldGameState,possibleSocket,serverNetworkManager);
                    authenticatingClientThreat.start();
                }
            } catch (IOException e) {
            }
        }

        public void stopServerSocket() {
            try {
                disconnectAll(NetworkDisconnectType.SERVER_CLOSE_SOCKET);
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ServerAuthenticationThread extends Thread {
        private final ServerWorldGameState worldGameState;
        private final Socket socket;
        private final ServerNetworkManager serverNetworkManager;
        private final ServerGameSocket gameSocket;

        public ServerAuthenticationThread(ServerWorldGameState worldGameState, Socket socket, ServerNetworkManager serverNetworkManager) throws IOException {
            this.worldGameState = worldGameState;
            this.socket = socket;
            this.serverNetworkManager = serverNetworkManager;
            gameSocket = new ServerGameSocket(worldGameState,socket,serverNetworkManager);
        }

        public void run() {
            try {
                System.out.println("ServerNetwork: Processing Incoming User");
                while (!gameSocket.hasUnprocessedPackets()) {
                    Thread.yield();
                }
                PacketBase initPacket = gameSocket.getUnprocessedPackets().get(0);
                if(initPacket instanceof  ClientAuthGreetingPacket) {
                    UUID playerUUID = ((ClientAuthGreetingPacket) initPacket).getPlayerUUID();
                    if (approvedUsers.contains(playerUUID)) {
                        if (connectedClients.contains(playerUUID)) {
                            System.out.println("ServerNetwork: Duplicate user detected! Denying Access");
                            gameSocket.disconnect(NetworkDisconnectType.SERVER_DUPLICATE_CONNECTION);
                        } else {
                            gameSocket.sendPacket(new ServerAuthAcceptClient());
                            connectedClients.put(playerUUID, gameSocket);
                            connectedUsers.add(playerUUID);
                            notifyConnectedPlayer(playerUUID,gameSocket);
                            System.out.println("ServerNetwork: User " + playerUUID + " Authenticated");
                        }
                    } else {
                        gameSocket.disconnect(NetworkDisconnectType.SERVER_INCORRECT_ID);
                        System.out.println("ServerNetwork: User " + playerUUID + " Denied Login, incorrect information");
                    }
                } else {

                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}