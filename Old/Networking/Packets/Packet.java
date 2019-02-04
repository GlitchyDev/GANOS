package com.GlitchyDev.Old.Networking.Packets;

/**
 * A basic construct used to relay information between Client and Server through sockets
 */
public class Packet {
    protected PacketType packetType;
    final protected String[] data;
    final protected String rawPacket;
    public static String DIV = "~";

    public Packet(String packetInfo)
    {
        rawPacket = packetInfo;
        data = packetInfo.split(DIV);
        packetType = PacketType.valueOf(data[0]);
    }


    public PacketType getPacketType()
    {
        return packetType;
    }

    public String getRawPacket()
    {
        return rawPacket;
    }
}
