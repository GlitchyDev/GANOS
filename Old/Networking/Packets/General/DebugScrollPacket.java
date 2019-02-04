package com.GlitchyDev.Old.Networking.Packets.General;

import com.GlitchyDev.Old.Networking.Packets.Packet;
import com.GlitchyDev.Old.Networking.Packets.PacketType;

/**
 * Debug Packet
 */
public class DebugScrollPacket extends Packet {
    private final PacketType packetType = PacketType.A_DEBUGSCROLL;

    public DebugScrollPacket(Packet packet) {
        super(packet.getRawPacket());
    }

    public DebugScrollPacket(double unit)
    {
        super(PacketType.A_DEBUGSCROLL + Packet.DIV + unit);
    }

    public double getUnit()
    {
        return Double.valueOf(data[1]);
    }

}
