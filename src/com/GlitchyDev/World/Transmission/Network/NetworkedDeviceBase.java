package com.GlitchyDev.World.Transmission.Network;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Messages.CommunicationNoise;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Source.CommunicationSource;
import com.GlitchyDev.World.Transmission.DetectionType;
import com.GlitchyDev.World.Transmission.Network.Enums.NetworkChannel;
import com.GlitchyDev.World.Transmission.Network.Enums.NetworkTransmissionDistance;
import com.GlitchyDev.World.Location;

public abstract class NetworkedDeviceBase {
    protected final WorldGameState worldGameState;
    private final CommunicationNetworkManager communicationNetworkManager;

    public NetworkedDeviceBase(WorldGameState worldGameState, CommunicationNetworkManager communicationNetworkManager) {
        this.worldGameState = worldGameState;
        this.communicationNetworkManager = communicationNetworkManager;
    }

    public abstract void recieveMessage(CommunicationMessage message, Location origin, NetworkTransmissionDistance networkTransmissionDistance, CommunicationSource communicationSource);
    public abstract void recieveNoise(CommunicationNoise noise, Location origin, NetworkTransmissionDistance networkTransmissionDistance, CommunicationSource communicationSource);
    public abstract Location getTransmissionLocation();
    public abstract DetectionType generateDetectionType(Location origin, NetworkTransmissionDistance networkTransmissionDistance, CommunicationSource soundSource);


    public final void joinNetwork(CommunicationNetwork communicationDeviceNetwork) {
        communicationDeviceNetwork.deviceJoin(this);
    }

    public final void leaveNetwork(CommunicationNetwork communicationDeviceNetwork) {
        communicationDeviceNetwork.deviceLeave(this);
    }

    public final void transmitMessage(NetworkChannel networkChannel, CommunicationMessage communicationMessage, CommunicationSource communicationSource) {
        communicationNetworkManager.getCommunicationNetwork(networkChannel).transmitMessage(communicationMessage, getTransmissionLocation(),communicationSource);
    }

    public final void transmitNoise(NetworkChannel networkChannel, CommunicationNoise communicationNoise, CommunicationSource communicationSource) {
        communicationNetworkManager.getCommunicationNetwork(networkChannel).transmitNoise(communicationNoise, getTransmissionLocation(),communicationSource);
    }


}
