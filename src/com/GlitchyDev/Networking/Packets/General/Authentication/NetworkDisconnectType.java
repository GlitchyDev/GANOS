package com.GlitchyDev.Networking.Packets.General.Authentication;

public enum NetworkDisconnectType {
    SERVER_INCORRECT_ID,
    SERVER_KICK,
    SERVER_BAN,
    SERVER_DUPLICATE_CONNECTION,
    SERVER_CLOSE_SOCKET,


    CLIENT_GAME_CRASH,
    CLIENT_LOGOUT,
    CLIENT_WINDOW_CLOSED,
}
