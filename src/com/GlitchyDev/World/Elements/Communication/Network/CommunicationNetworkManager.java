package com.GlitchyDev.World.Elements.Communication.Network;

import com.GlitchyDev.World.Elements.Communication.Network.Enums.CommunicationChannel;

import java.util.HashMap;

public class CommunicationNetworkManager {
    private final HashMap<CommunicationChannel, CommunicationNetwork> communcationNetworks;


    public CommunicationNetworkManager() {
        communcationNetworks = new HashMap<>();
    }

    public void registerChannel(CommunicationNetwork communicationDeviceNetwork) {
        communcationNetworks.put(communicationDeviceNetwork.getCommunicationChannel(),communicationDeviceNetwork);
    }

    public CommunicationNetwork getCommunicationNetwork(CommunicationChannel communicationChannel) {
        return communcationNetworks.get(communicationChannel);
    }



}
