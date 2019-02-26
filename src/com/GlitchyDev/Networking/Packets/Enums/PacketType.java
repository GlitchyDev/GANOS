package com.GlitchyDev.Networking.Packets.Enums;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Client.Authentication.ClientAuthGreetingPacket;
import com.GlitchyDev.Networking.Packets.Client.Input.ClientSendInputPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.GeneralAuthDisconnectPacket;
import com.GlitchyDev.Networking.Packets.Server.Authentication.ServerAuthAcceptClient;
import com.GlitchyDev.Networking.Packets.Server.World.Block.ServerChangeBlockPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Entity.ServerDespawnEntityPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Entity.ServerChangeDirectionEntityPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Entity.ServerMoveEntityPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Entity.ServerSpawnEntityPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Region.ServerDespawnRegionPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Region.ServerSpawnRegionPacket;
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
    // Authentication
    SERVER_AUTH_ACCEPT_CLIENT,
    // World
    SERVER_SPAWN_WORLD,
    SERVER_DESPAWN_WORLD,
    // Region
    SERVER_SPAWN_REGION,
    SERVER_DESPAWN_REGION,
    // Entity
    SERVER_SPAWN_ENTITY,
    SERVER_DESPAWN_ENTITY,
    SERVER_MOVE_ENTITY,
    SERVER_CHANGE_DIRECTION_ENTITY,
    // Block
    SERVER_CHANGE_BLOCK

    ;




    public PacketBase getPacketFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        switch (this) {
            case GENERAL_AUTH_DISCONNECT:
                return new GeneralAuthDisconnectPacket(inputBitUtility);
            case CLIENT_AUTH_GREETING_PACKET:
                return new ClientAuthGreetingPacket(inputBitUtility);
            case CLIENT_SEND_INPUT_PACKET:
                return new ClientSendInputPacket(inputBitUtility);
            case SERVER_AUTH_ACCEPT_CLIENT:
                return new ServerAuthAcceptClient(inputBitUtility);
            case SERVER_SPAWN_REGION:
                return new ServerSpawnRegionPacket(inputBitUtility, worldGameState);
            case SERVER_DESPAWN_REGION:
                return new ServerDespawnRegionPacket(inputBitUtility, worldGameState);
            case SERVER_SPAWN_ENTITY:
                return new ServerSpawnEntityPacket(inputBitUtility, worldGameState);
            case SERVER_DESPAWN_ENTITY:
                return new ServerDespawnEntityPacket(inputBitUtility, worldGameState);
            case SERVER_MOVE_ENTITY:
                return new ServerMoveEntityPacket(inputBitUtility, worldGameState);
            case SERVER_CHANGE_DIRECTION_ENTITY:
                return new ServerChangeDirectionEntityPacket(inputBitUtility, worldGameState);
            case SERVER_CHANGE_BLOCK:
                return new ServerChangeBlockPacket(inputBitUtility, worldGameState);
            default:
        }
        return null;
    }

}
