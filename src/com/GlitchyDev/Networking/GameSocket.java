package com.GlitchyDev.Networking;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameSocket {
    private final Socket socket;
    private final OutputBitUtility output;
    private final InputBitUtility input;

    private final PacketReadThread packetReadThread;
    private final Collection<PacketBase> receivedPackets;



    public GameSocket(Socket socket) throws IOException {
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

    public void disconnect(NetworkDisconnectType reason) {
        //sendPacket(new GoodbyePacket(reason));
        disconnect();
    }

    public void disconnect() {
        packetReadThread.killThread();
        try {
            socket.close();
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private class PacketReadThread extends Thread {
        private AtomicBoolean keepThreadAlive;

        public PacketReadThread() {
            keepThreadAlive = new AtomicBoolean(true);
        }

        @Override
        public void run() {
            try {
                while(keepThreadAlive.get()) {
                    if(input.ready()) {
                        PacketType packetType = PacketType.values()[input.getNextCorrectIntByte()];
                        PacketBase packet = packetType.getPacketFromInput(input);
                        receivedPackets.add(packet);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void killThread() {
            keepThreadAlive.set(false);
        }
    }

}
