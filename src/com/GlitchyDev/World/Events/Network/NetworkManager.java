package com.GlitchyDev.World.Events.Network;

import com.GlitchyDev.World.Events.Network.Enums.NetworkChannel;

import java.util.HashMap;

public class NetworkManager {
    private final HashMap<NetworkChannel, Network> communcationNetworks;


    public NetworkManager() {
        communcationNetworks = new HashMap<>();
    }

    public void registerChannel(Network communicationDeviceNetwork) {
        communcationNetworks.put(communicationDeviceNetwork.getNetworkChannel(),communicationDeviceNetwork);
    }

    public Network getCommunicationNetwork(NetworkChannel networkChannel) {
        return communcationNetworks.get(networkChannel);
    }



}
