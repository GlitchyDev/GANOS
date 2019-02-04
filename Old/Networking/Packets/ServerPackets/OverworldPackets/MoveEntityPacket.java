package com.GlitchyDev.Old.Networking.Packets.ServerPackets.OverworldPackets;

import com.GlitchyDev.Old.Networking.Packets.Packet;
import com.GlitchyDev.Old.Networking.Packets.PacketType;

import java.util.UUID;

/**
 * Packet Usage: Server Command: Move Target Entity by UUID. Is an absolute location ( May upgrade to Long Measurements later
 */
/**
 * A Server packet designated to move an entity in Client View
 */
public class MoveEntityPacket extends Packet {
    private final PacketType packetType = PacketType.S_SPAWN_ENTITY;

    public MoveEntityPacket(Packet packet) {
        super(packet.getRawPacket());
    }

    public MoveEntityPacket(UUID uuid, int x, int y)
    {
        super(PacketType.S_SPAWN_ENTITY + Packet.DIV + uuid + Packet.DIV + x + Packet.DIV + y);
    }

    public UUID getUUID()
    {
        return UUID.fromString(data[1]);
    }

    public int getX()
    {
        return Integer.valueOf(data[2]);
    }

    public int getY()
    {
        return Integer.valueOf(data[3]);
    }
}

