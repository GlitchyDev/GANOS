package com.GlitchyDev.World.Elements.Transmission.Network;

import com.GlitchyDev.World.Elements.Transmission.Network.Enums.NetworkChannel;

import java.util.HashMap;

public class CommunicationNetworkManager {
    private final HashMap<NetworkChannel, CommunicationNetwork> communcationNetworks;


    public CommunicationNetworkManager() {
        communcationNetworks = new HashMap<>();
    }

    public void registerChannel(CommunicationNetwork communicationDeviceNetwork) {
        communcationNetworks.put(communicationDeviceNetwork.getNetworkChannel(),communicationDeviceNetwork);
    }

    public CommunicationNetwork getCommunicationNetwork(NetworkChannel networkChannel) {
        return communcationNetworks.get(networkChannel);
    }



}
