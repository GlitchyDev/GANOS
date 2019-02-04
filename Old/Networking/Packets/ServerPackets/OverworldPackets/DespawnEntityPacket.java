package com.GlitchyDev.Old.Networking.Packets.ServerPackets.OverworldPackets;

import com.GlitchyDev.Old.Networking.Packets.Packet;
import com.GlitchyDev.Old.Networking.Packets.PacketType;

import java.util.UUID;

/**
 * Packet Usage: Server Command: Despawn Target Entity by UUID from Client View ( May still actually exist on Server )
 */
/**
 * A Server packet designated to depsawn an entity from Client View
 */
public class DespawnEntityPacket extends Packet {
    private final PacketType packetType = PacketType.S_DESPAWN_ENTITY;

    public DespawnEntityPacket(Packet packet) {
        super(packet.getRawPacket());
    }

    public DespawnEntityPacket(UUID uuid)
    {
        super(PacketType.S_DESPAWN_ENTITY + Packet.DIV + uuid);
    }

    public UUID getUUID()
    {
        return UUID.fromString(data[1]);
    }
}
