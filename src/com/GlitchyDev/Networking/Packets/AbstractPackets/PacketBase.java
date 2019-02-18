package com.GlitchyDev.Networking.Packets.AbstractPackets;

import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public abstract class PacketBase {
    private final PacketType packetType;

    public PacketBase(PacketType packetType) {
        this.packetType = packetType;
    }

    public PacketBase(InputBitUtility inputBitUtility) throws IOException {
        this.packetType = PacketType.values()[inputBitUtility.getNextCorrectIntByte()];
    }

    public void transmit(OutputBitUtility outputBitUtility) throws IOException {
        transmitPacketBody(outputBitUtility);
    }

    protected abstract void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException;


    @Override
    public String toString() {
        return "p@" + packetType;
    }

    public PacketType getPacketType() {
        return packetType;
    }

}
