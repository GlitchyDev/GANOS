package com.GlitchyDev.Networking;

import com.GlitchyDev.Networking.Packets.Client.Authentication.ClientAuthGreetingPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Server.Authentication.ServerAuthAcceptClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerNetworkManager {
    private ConcurrentHashMap<String, GameSocket> connectedClients;
    private Collection<String> connectedUsers;
    private Collection<String> approvedUsers;

    private AcceptingClientThread acceptingClientThread;
    private AtomicBoolean acceptingClients = new AtomicBoolean(false);


    public ServerNetworkManager() {
        connectedClients = new ConcurrentHashMap<>();
        connectedUsers = Collections.synchronizedCollection(new ArrayList<>());
        approvedUsers = Collections.synchronizedCollection(new ArrayList<>());

        System.out.println("ServerNetwork: Loading Approved Users");
        //approvedUsers.addAll(AssetLoader.getConfigListAsset("ApprovedUsers"));
        approvedUsers.add("James");
    }


    public void enableAcceptingClients() {
        System.out.println("ServerNetwork: Enabled Login for Users");
        acceptingClientThread = new AcceptingClientThread();
        acceptingClients.set(true);
        acceptingClientThread.start();

    }

    public void disableAcceptingClients() {
        System.out.println("ServerNetwork: Disable Login for Users");
        acceptingClients.set(false);
        acceptingClientThread.stopServerSocket();
    }

    public void disconnectUser(String name, NetworkDisconnectType reason) {
        System.out.println("ServerNetwork: Disconnected User " + name + " for " + reason);
        connectedClients.get(name).disconnect(reason);
        connectedClients.remove(name);
        connectedUsers.remove(name);
    }

    public void disconnectAll(NetworkDisconnectType reason) {
        System.out.println("ServerNetwork: Disconnected All Users for " + reason);

        Iterator<String> userIterator = connectedUsers.iterator();
        while(userIterator.hasNext()) {
            disconnectUser(userIterator.next(), reason);
        }
    }

    public Collection<String> getApprovedUsers()
    {
        return approvedUsers;
    }

    public Collection<String> getConnectedUsers()
    {
        return connectedUsers;
    }

    public GameSocket getUsersGameSocket(String name)
    {
        return connectedClients.get(name);
    }

    public int getNumberOfConnectedUsers()
    {
        return connectedClients.size();
    }






    private class AcceptingClientThread extends Thread {
        private ServerSocket serverSocket;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(8001);
                while (acceptingClients.get()) {
                    Socket possibleSocket = serverSocket.accept();
                    AuthenticatingClientThread authenticatingClientThreat = new AuthenticatingClientThread(new GameSocket(possibleSocket));
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

    private class AuthenticatingClientThread extends Thread {
        private GameSocket gameSocket;

        public AuthenticatingClientThread(GameSocket gameSocket) {
            this.gameSocket = gameSocket;
        }

        @Override
        public void run() {
            try {
                System.out.println("ServerNetwork: Processing Incoming User");
                while (!gameSocket.hasUnprocessedPackets()) {
                    Thread.yield();
                }
                ClientAuthGreetingPacket clientAuthGreetingPacket = (ClientAuthGreetingPacket) gameSocket.getUnprocessedPackets().get(0);
                String playerName = clientAuthGreetingPacket.getPlayerName();
                if (approvedUsers.contains(playerName)) {
                    if(connectedClients.contains(playerName)) {
                        System.out.println("ServerNetwork: Duplicate user detected! Denying Access");
                        gameSocket.disconnect(NetworkDisconnectType.SERVER_DUPLICATE_CONNECTION);
                    } else {
                        gameSocket.sendPacket(new ServerAuthAcceptClient());
                        connectedClients.put(playerName, gameSocket);
                        connectedUsers.add(playerName);
                        System.out.println("ServerNetwork: User " + playerName + " Authenticated");
                    }
                } else {
                    gameSocket.disconnect(NetworkDisconnectType.SERVER_INCORRECT_ID);
                    System.out.println("ServerNetwork: User " + playerName + " Denied Login, incorrect information");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}