package com.GlitchyDev.Old.Networking.Packets.ClientPackets;

import com.GlitchyDev.Old.Networking.Packets.Packet;
import com.GlitchyDev.Old.Networking.Packets.PacketType;

/**
 * A Client packet designated to start a connection to a server
 */
public class ClientIntroductionPacket extends Packet {
    private final PacketType packetType = PacketType.C_INTRODUCTION;

    public ClientIntroductionPacket(Packet packet) {
            super(packet.getRawPacket());
    }

    public ClientIntroductionPacket(String uuid)
    {
        super(PacketType.C_INTRODUCTION + Packet.DIV + uuid);
    }

    public String getUUID()
    {
        return data[1];
    }
}
