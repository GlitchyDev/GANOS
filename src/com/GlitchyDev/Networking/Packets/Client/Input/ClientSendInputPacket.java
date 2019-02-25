package com.GlitchyDev.Networking.Packets.Client.Input;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public class ClientSendInputPacket extends PacketBase {
    private final ClientInputType clientInputType;

    public ClientSendInputPacket(ClientInputType clientInputType) {
        super(PacketType.CLIENT_SEND_INPUT_PACKET);
        this.clientInputType = clientInputType;
    }

    public ClientSendInputPacket(InputBitUtility inputBitUtility) throws IOException {
        super(PacketType.CLIENT_SEND_INPUT_PACKET, inputBitUtility);
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

    public ClientInputType getClientInputType() {
        return clientInputType;
    }
}
