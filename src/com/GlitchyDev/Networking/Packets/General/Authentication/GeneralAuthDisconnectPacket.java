package com.GlitchyDev.Networking.Packets.General.Authentication;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public class GeneralAuthDisconnectPacket extends PacketBase {
    private final NetworkDisconnectType disconnectType;

    public GeneralAuthDisconnectPacket(PacketType packetType, NetworkDisconnectType disconnectType) {
        super(packetType);
        this.disconnectType = disconnectType;
    }

    public GeneralAuthDisconnectPacket(InputBitUtility inputBitUtility) throws IOException {
        super(inputBitUtility);
        disconnectType = NetworkDisconnectType.values()[inputBitUtility.getNextCorrectIntByte()];
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(disconnectType.ordinal());
    }

    @Override
    public String toString() {
        return super.toString() + "," + disconnectType;
    }

    public NetworkDisconnectType getDisconnectType() {
        return disconnectType;
    }
}
