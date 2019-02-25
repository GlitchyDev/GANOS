package com.GlitchyDev.Networking.Packets.AbstractPackets;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;

import java.io.IOException;

/**
 * These packets directly edit the WorldGameState, and thus do not need externally accessible variables
 */
public abstract class WorldStateModifyingPackets extends PacketBase {
    public WorldStateModifyingPackets(PacketType packetType) {
        super(packetType);
    }

    public WorldStateModifyingPackets(PacketType packetType, InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(packetType, inputBitUtility);
    }

    public abstract void executeModification(WorldGameState worldGameState);
}
