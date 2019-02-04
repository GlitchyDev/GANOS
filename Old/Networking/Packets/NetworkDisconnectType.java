package com.GlitchyDev.Old.Networking.Packets;

/**
 * Required for using a GoodBye Packet, would allow a Server or client to engage saving measures on disconnection
 */
public enum NetworkDisconnectType {
    WINDOW_CLOSED,
    GAME_TERMINATED,
    KICKED,
}
