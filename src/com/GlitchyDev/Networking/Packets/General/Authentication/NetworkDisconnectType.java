package com.GlitchyDev.Networking.Packets.General.Authentication;

public enum NetworkDisconnectType {
    SERVER_INCORRECT_ID,
    SERVER_KICK,
    SERVER_BAN,
    SERVER_DUPLICATE_CONNECTION,
    SERVER_CLOSE_WINDOW,
    SERVER_CLOSE,

    CLIENT_LOGOUT,
    CLIENT_WINDOW_CLOSED,
    CLIENT_CRASH,

    GENERAL_SOCKET_CRASH,

}
