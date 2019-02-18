package com.GlitchyDev.Networking;

import com.GlitchyDev.Networking.Packets.Client.Authentication.ClientAuthGreetingPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServerNetworkConnector {
    private ConcurrentHashMap<String, GameSocket> connectedClients;
    private Collection<String> connectedUsers;
    private Collection<String> approvedUsers;

    private AcceptingClientThread acceptingClientThread;
    private AtomicBoolean acceptingClients = new AtomicBoolean(false);


    public ServerNetworkConnector() {
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
            System.out.println("ServerNetwork: Processing Incoming User");
            while (!gameSocket.hasUnprocessedPackets()) {
                Thread.yield();
            }
            ClientAuthGreetingPacket intro = (ClientAuthGreetingPacket) gameSocket.getUnprocessedPackets().get(0);
            if (approvedUsers.contains(clientIntroductionPacket.getUUID())) {
                gameSocket.sendPacket(new ServerReturnGreetingPacket());
                connectedClients.put(clientIntroductionPacket.getUUID(), gameSocket);
                connectedUsers.add(clientIntroductionPacket.getUUID());
                System.out.println("ServerNetwork: User " + clientIntroductionPacket.getUUID() + " Authenticated");
            } else {
                gameSocket.disconnect(NetworkDisconnectType.KICKED);
                System.out.println("ServerNetwork: User " + clientIntroductionPacket.getUUID() + " Denied Login");
            }
        }
    }
}