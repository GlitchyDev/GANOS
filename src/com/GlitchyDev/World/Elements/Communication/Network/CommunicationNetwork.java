package com.GlitchyDev.World.Elements.Communication.Network;

import com.GlitchyDev.World.Elements.Communication.Network.Enums.CommunicationChannel;

import java.util.ArrayList;

public class CommunicationNetwork {
    private final CommunicationChannel communicationChannel;
    private final ArrayList<CommunicationDeviceBase> connectedDevices;

    public CommunicationNetwork(CommunicationChannel communicationChannel) {
        this.communicationChannel = communicationChannel;
        connectedDevices = new ArrayList<>();
    }

    public void deviceJoin(CommunicationDeviceBase device) {
        connectedDevices.add(device);
    }

    public void deviceLeave(CommunicationDeviceBase device) {
        connectedDevices.remove(device);
    }

    public void transmitMessage(CommunicationMessage communicationMessage) {

    }


    public CommunicationChannel getCommunicationChannel() {
        return communicationChannel;
    }

    public ArrayList<CommunicationDeviceBase> getConnectedDevices() {
        return connectedDevices;
    }
}
