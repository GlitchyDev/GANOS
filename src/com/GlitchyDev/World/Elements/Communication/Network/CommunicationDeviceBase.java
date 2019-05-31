package com.GlitchyDev.World.Elements.Communication.Network;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Elements.Communication.DetectionType;
import com.GlitchyDev.World.Elements.Communication.Network.Enums.CommunicationChannel;
import com.GlitchyDev.World.Location;

public abstract class CommunicationDeviceBase {
    private final WorldGameState worldGameState;
    private final CommunicationNetworkManager communicationNetworkManager;

    public CommunicationDeviceBase(WorldGameState worldGameState,  CommunicationNetworkManager communicationNetworkManager) {
        this.worldGameState = worldGameState;
        this.communicationNetworkManager = communicationNetworkManager;
    }
    // Currently Selected Channel
    // Type

    // Listeners only
    public void joinNetwork(CommunicationNetwork communicationDeviceNetwork) {
        communicationDeviceNetwork.deviceJoin(this);
    }

    public void leaveNetwork(CommunicationNetwork communicationDeviceNetwork) {
        communicationDeviceNetwork.deviceLeave(this);
    }

    public void transmitMessage(CommunicationChannel communicationChannel) {
        communicationChannel.
    }

    public abstract DetectionType determineDetection(Location transmissionSource);

    public abstract Location getLocation();


}
