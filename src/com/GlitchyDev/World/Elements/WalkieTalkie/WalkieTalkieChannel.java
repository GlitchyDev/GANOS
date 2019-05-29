package com.GlitchyDev.World.Elements.WalkieTalkie;

import com.GlitchyDev.World.Elements.WalkieTalkie.Enums.CommunicationChannel;

import java.util.ArrayList;

public class CommunicationDeviceNetwork {
    private final CommunicationChannel communicationChannel;
    private final ArrayList<CommunicationDeviceBase> connectedDevices;

    public CommunicationDeviceNetwork(CommunicationChannel communicationChannel) {
        this.communicationChannel = communicationChannel;
        connectedDevices = new ArrayList<>();
    }

    public void deviceJoin(CommunicationDeviceBase device) {
        connectedDevices.add(device);
    }

    public void deviceLeave(CommunicationDeviceBase device) {
        connectedDevices.remove(device);
    }

    // Use transmitted device to a distance scan of all connected devices ( Minus Itself ) and send message to those compatible
    public int transmitMessage() {
        return -1;
    }

}
