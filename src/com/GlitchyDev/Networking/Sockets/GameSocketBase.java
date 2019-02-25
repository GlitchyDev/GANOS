package com.GlitchyDev.Networking.Sockets;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Networking.Packets.General.Authentication.GeneralAuthDisconnectPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class GameSocketBase {
    protected WorldGameState worldGameState;
    private final Socket socket;
    private final OutputBitUtility output;
    private final InputBitUtility input;

    private AtomicBoolean isConnected = new AtomicBoolean(true);

    private AtomicBoolean keepThreadAlive;
    private final PacketReadThread packetReadThread;
    private final Collection<PacketBase> receivedPackets;


    public GameSocketBase(WorldGameState worldGameState, InetAddress ipAddress, int port) throws IOException {
        this.worldGameState = worldGameState;
        this.socket = new Socket(ipAddress, port);
        output = new OutputBitUtility(socket.getOutputStream());
        input = new InputBitUtility(socket.getInputStream());

        receivedPackets = Collections.synchronizedCollection(new ArrayList<>());

        packetReadThread = new PacketReadThread();
        packetReadThread.start();
    }

    public GameSocketBase(WorldGameState worldGameState, Socket socket) throws IOException {
        this.worldGameState = worldGameState;
        this.socket = socket;
        input = new InputBitUtility(socket.getInputStream());
        output = new OutputBitUtility(socket.getOutputStream());

        receivedPackets = Collections.synchronizedCollection(new ArrayList<>());

        packetReadThread = new PacketReadThread();
        packetReadThread.start();
    }



    public void sendPacket(PacketBase packet) throws IOException {
        packet.transmit(output);
        output.flush();
    }

    public void sendPackets(Collection<PacketBase> packets) throws IOException {
        for(PacketBase packet: packets) {
            packet.transmit(output);
        }
        output.flush();
    }

    public ArrayList<PacketBase> getUnprocessedPackets() {
        ArrayList<PacketBase> packets = new ArrayList<>();
        packets.addAll(receivedPackets);
        receivedPackets.clear();
        return packets;
    }

    public boolean hasUnprocessedPackets() {
        return receivedPackets.size() != 0;
    }

    public void disconnect(NetworkDisconnectType reason) throws IOException {
        System.out.println("Disconnecting for reason " + reason);
        sendPacket(new GeneralAuthDisconnectPacket(reason));
        //disconnect();
    }

    public void disconnect() {
        try {
            keepThreadAlive.set(false);
            socket.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void notifyDisconnect(NetworkDisconnectType networkDisconnectType);
    public boolean isConnected() {
        return isConnected.get();
    }



    private class PacketReadThread extends Thread {
        public PacketReadThread() {
            keepThreadAlive = new AtomicBoolean(true);
        }

        @Override
        public void run() {
            isConnected.set(true);
            try {
                while(keepThreadAlive.get()) {
                    PacketType packetType = PacketType.values()[input.getNextCorrectIntByte()];
                    PacketBase packet = packetType.getPacketFromInput(input, worldGameState);
                    if (packet instanceof GeneralAuthDisconnectPacket) {
                        keepThreadAlive.set(false);
                        socket.close();
                        notifyDisconnect(((GeneralAuthDisconnectPacket) packet).getDisconnectType());
                    }
                    receivedPackets.add(packet);
                }
            } catch (IOException e) {
                // Eventually add an way to notify GameState if disconnected for other reasons like PING
                e.printStackTrace();
            }
        }
    }

}
