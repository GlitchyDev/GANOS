package com.GlitchyDev.Old.Networking;

import com.GlitchyDev.Old.Networking.Packets.BasicPackets.GoodbyePacket;
import com.GlitchyDev.Old.Networking.Packets.NetworkDisconnectType;
import com.GlitchyDev.Old.Networking.Packets.Packet;
import com.GlitchyDev.Old.Networking.Packets.PacketType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A Basic Socket Connection, used for Either Client or Servers to facilitate sending and Recieving packets from the othe
 *
 * It supports putting recieved packets in a Threadsafe Arraylist for later processing, as well as sending packet objects
 */
public class GameSocket {
    private PacketReadThread packetReadThread;

    private Socket socket;
    private BufferedReader socketInput;
    private PrintWriter socketOutput;

    private final List<Packet> unprocessedPackets;


    public GameSocket(Socket socket)
    {
        this.socket = socket;
        try {
            socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketOutput = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        unprocessedPackets = Collections.synchronizedList(new ArrayList<>());

        packetReadThread = new PacketReadThread();
        packetReadThread.start();
    }

    public void sendPacket(Packet packet)
    {
        socketOutput.println(packet.getRawPacket());
        socketOutput.flush();
    }

    public void sendPackets(Collection<Packet> packets)
    {
        for(Packet packet : packets)
        {
            socketOutput.println(packet.getRawPacket());
        }
        socketOutput.flush();
    }

    public ArrayList<Packet> getUnprocessedPackets()
    {
        ArrayList<Packet> packets = new ArrayList<>();
        synchronized(unprocessedPackets) {
            for (Packet unprocessedPacket : unprocessedPackets) {
                packets.add(unprocessedPacket);
            }
            unprocessedPackets.clear();
        }
        return packets;
    }

    public boolean hasUnprocessedPackets()
    {
        return unprocessedPackets.size() != 0;
    }

    public void disconnect(NetworkDisconnectType reason)
    {
        sendPacket(new GoodbyePacket(reason));
        disconnect();
    }

    public void disconnect()
    {
        packetReadThread.killThread();
        try {
            socket.close();
            socketInput.close();
            socketOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class PacketReadThread extends Thread {
        private AtomicBoolean keepThreadAlive;


        public PacketReadThread()
        {
            keepThreadAlive = new AtomicBoolean(true);
        }

        @Override
        public void run() {
            try {
                while(keepThreadAlive.get())
                {
                            if(socketInput.ready())
                            {
                                Packet packet = new Packet(socketInput.readLine());
                                if(packet.getPacketType() == PacketType.A_GOODBYE)
                                {
                            disconnect();
                        }
                        unprocessedPackets.add(packet);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void killThread()
        {
            keepThreadAlive.set(false);
        }
    }
}
