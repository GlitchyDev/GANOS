package com.GlitchyDev.Networking.Packets.Enums;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Client.Authentication.ClientAuthGreetingPacket;
import com.GlitchyDev.Networking.Packets.Client.Input.ClientSendInputPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.GeneralAuthDisconnectPacket;
import com.GlitchyDev.Utility.InputBitUtility;

import java.io.IOException;

public enum PacketType {
    // GENERAL
    GENERAL_AUTH_DISCONNECT,

    // CLIENT
    // Authentication
    CLIENT_AUTH_GREETING_PACKET,
    // INPUT
    CLIENT_SEND_INPUT_PACKET,


    // SERVER
    // Spawn
    SERVER_SPAWN_REGION,
    SERVER_SPAWN_ENTITY,
    SERVER_SPAWN_BLOCK,
    // Despawn
    SERVER_DESPAWN_REGION,
    SERVER_DESPAWN_ENTITY,
    SERVER_DESPAWN_BLOCK,
    // Move
    SERVER_MOVE_ENTITY,
    SERVER_MOVE_REGION;

    public PacketBase getPacketFromInput(InputBitUtility inputBitUtility) throws IOException {
        switch (this) {
            case GENERAL_AUTH_DISCONNECT:
                return new GeneralAuthDisconnectPacket(inputBitUtility);
            case CLIENT_AUTH_GREETING_PACKET:
                return new ClientAuthGreetingPacket(inputBitUtility);
            case CLIENT_SEND_INPUT_PACKET:
                return new ClientSendInputPacket(inputBitUtility);
            case SERVER_SPAWN_REGION:
            case SERVER_SPAWN_ENTITY:
            case SERVER_SPAWN_BLOCK:
            case SERVER_DESPAWN_REGION:
            case SERVER_DESPAWN_ENTITY:
            case SERVER_DESPAWN_BLOCK:
            case SERVER_MOVE_ENTITY:
            default:
        }
        return null;
    }

}
