package com.GlitchyDev.Networking.Packets.Client.Input;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public class ClientSendInputPacket extends PacketBase {
    private final ClientInputType clientInputType;

    public ClientSendInputPacket(PacketType packetType, ClientInputType clientInputType) {
        super(packetType);
        this.clientInputType = clientInputType;
    }

    public ClientSendInputPacket(InputBitUtility inputBitUtility) throws IOException {
        super(inputBitUtility);
        clientInputType = ClientInputType.values()[inputBitUtility.getNextCorrectIntByte()];
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(clientInputType.ordinal());
    }

    @Override
    public String toString() {
        return super.toString() + "," + clientInputType;
    }
}
