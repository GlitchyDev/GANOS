package com.GlitchyDev.Old.Networking;


import com.GlitchyDev.Old.IO.AssetLoader;
import com.GlitchyDev.Old.Networking.Packets.ClientPackets.ClientIntroductionPacket;
import com.GlitchyDev.Old.Networking.Packets.NetworkDisconnectType;
import com.GlitchyDev.Old.Networking.Packets.ServerPackets.General.ServerReturnGreetingPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Connection Hub for a Server to manage its connections to each Client and their respective GameSocket
 */
public class ServerNetworkConnection {
    private ConcurrentHashMap<String,GameSocket> connectedClients;
    private Collection<String> connectedUsers;
    private Collection<String> approvedUsers;

    private AcceptingClientThread acceptingClientThread;
    private AtomicBoolean acceptingClients = new AtomicBoolean(false);



    public ServerNetworkConnection()
    {
        connectedClients = new ConcurrentHashMap<>();

        connectedUsers = Collections.synchronizedCollection(new ArrayList<>());
        approvedUsers = Collections.synchronizedCollection(new ArrayList<>());

        System.out.println("ServerNetwork: Loading Approved Users");
        approvedUsers.addAll(AssetLoader.getConfigListAsset("ApprovedUsers"));

    }


    public void enableAcceptingClients()
    {
        System.out.println("ServerNetwork: Enabled Login for Users");
        acceptingClientThread = new AcceptingClientThread();
        acceptingClients.set(true);
        acceptingClientThread.start();

    }

    public void disableAcceptingClients()
    {
        acceptingClients.set(false);
        acceptingClientThread.stopServerSocket();
    }

    public void disconnectUser(String name, NetworkDisconnectType reason)
    {
        System.out.println("ServerNetwork: Disconnected User " + name);

        connectedClients.get(name).disconnect(reason);
        connectedClients.remove(name);
        connectedUsers.remove(name);
    }

    public void disconnectAll(NetworkDisconnectType reason)
    {
        System.out.println("ServerNetwork: Disconnected All Users");

        Iterator<String> userIterator = connectedUsers.iterator();
        while(userIterator.hasNext()) {
            String name = userIterator.next();
            System.out.println("ServerNetwork: Disconnected User " + name);
            connectedClients.get(name).disconnect(reason);
            connectedClients.remove(name);
            userIterator.remove();
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
                while(acceptingClients.get())
                {
                    Socket possibleSocket = serverSocket.accept();
                    AuthenticatingClientThread authenticatingClientThreat = new AuthenticatingClientThread(new GameSocket(possibleSocket));
                    authenticatingClientThreat.start();
                }
            } catch (IOException e) {
            }
        }

        public void stopServerSocket()
        {
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
            ClientIntroductionPacket clientIntroductionPacket = new ClientIntroductionPacket(gameSocket.getUnprocessedPackets().get(0));
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

