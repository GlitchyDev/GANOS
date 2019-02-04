package com.GlitchyDev.Old.Networking.Packets.ServerPackets.General;

import com.GlitchyDev.Old.Networking.Packets.Packet;
import com.GlitchyDev.Old.Networking.Packets.PacketType;

/**
 * A Server packet designated to reply to an introduction connection packet from a Client
 */
public class ServerReturnGreetingPacket extends Packet {
    private final PacketType packetType = PacketType.S_RETURN_GREETING;

    public ServerReturnGreetingPacket(Packet packet) {
        super(packet.getRawPacket());
    }

    public ServerReturnGreetingPacket()
    {
        super(PacketType.S_RETURN_GREETING + "");
    }

}
