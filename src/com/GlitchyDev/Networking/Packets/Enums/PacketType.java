package com.GlitchyDev.Networking.Packets.Enums;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.ClientGreetingPacket;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.DebugEntity;

import java.io.IOException;

public enum PacketType {
    CLIENT_GREETING_PACKET,
    CLIENT_SEND_INPUT_PACKET,
    CLIENT_GOODBYE,

    SERVER_REPLY_PACKET,

    SERVER_SPAWN_REGION,
    SERVER_SPAWN_ENTITY,
    SERVER_SPAWN_BLOCK,

    SERVER_DESPAWN_REGION,
    SERVER_DESPAWN_ENTITY,
    SERVER_DESPAWN_BLOCK,

    SERVER_MOVE_ENTITY;

    public PacketBase getPacketFromInput(InputBitUtility inputBitUtility) throws IOException {
        switch (this) {
            case CLIENT_GREETING_PACKET:
                return new ClientGreetingPacket(inputBitUtility);
            case CLIENT_SEND_INPUT_PACKET:

                break;
            case CLIENT_GOODBYE:

                break;
            case SERVER_REPLY_PACKET:

                break;
            case SERVER_SPAWN_REGION:

                break;
            case SERVER_SPAWN_ENTITY:

                break;
            case SERVER_SPAWN_BLOCK:

                break;
            case SERVER_DESPAWN_REGION:

                break;
            case SERVER_DESPAWN_ENTITY:

                break;
            case SERVER_DESPAWN_BLOCK:

                break;
            case SERVER_MOVE_ENTITY:

                break;
            default:

                break;
        }
        return null;
    }

}
