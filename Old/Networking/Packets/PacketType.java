package com.GlitchyDev.Old.Networking.Packets;

public enum PacketType {

    // Basic
    A_GOODBYE,
    A_DEBUGSCROLL,

    // Server *****

    // OverworldPacket
    S_SPAWN_ENTITY, // EntityType, UUID, X, Y
    S_MOVE_ENTITY, // Entity UUID, X, Y
    S_DESPAWN_ENTITY, // Entity UUID, X, Y

    // DebugMain
    S_RETURN_GREETING,

    // Client *****

    // DebugMain
    C_INTRODUCTION,


}
