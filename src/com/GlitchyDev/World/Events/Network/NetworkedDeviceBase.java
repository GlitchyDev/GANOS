package com.GlitchyDev.World.Events.Network;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Events.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Events.Communication.Constructs.Messages.CommunicationNoise;
import com.GlitchyDev.World.Events.Communication.Constructs.Source.CommunicationSource;
import com.GlitchyDev.World.Events.Communication.DetectionType;
import com.GlitchyDev.World.Events.Network.Enums.NetworkChannel;
import com.GlitchyDev.World.Events.Network.Enums.NetworkTransmissionDistance;
import com.GlitchyDev.World.Location;

public abstract class NetworkedDeviceBase {
    protected final WorldGameState worldGameState;
    private final NetworkManager networkManager;

    public NetworkedDeviceBase(WorldGameState worldGameState, NetworkManager networkManager) {
        this.worldGameState = worldGameState;
        this.networkManager = networkManager;
    }

    public abstract void receiveMessage(CommunicationMessage message, Location origin, NetworkTransmissionDistance networkTransmissionDistance, CommunicationSource communicationSource);
    public abstract void receiveNoise(CommunicationNoise noise, Location origin, NetworkTransmissionDistance networkTransmissionDistance, CommunicationSource communicationSource);
    public abstract Location getTransmissionLocation();
    public abstract DetectionType generateDetectionType(Location origin, NetworkTransmissionDistance networkTransmissionDistance, CommunicationSource soundSource);


    public final void joinNetwork(Network communicationDeviceNetwork) {
        communicationDeviceNetwork.deviceJoin(this);
    }

    public final void leaveNetwork(Network communicationDeviceNetwork) {
        communicationDeviceNetwork.deviceLeave(this);
    }

    public final void transmitMessage(NetworkChannel networkChannel, CommunicationMessage communicationMessage, CommunicationSource communicationSource) {
        networkManager.getCommunicationNetwork(networkChannel).emitMessage(communicationMessage, getTransmissionLocation(),communicationSource);
    }

    public final void transmitNoise(NetworkChannel networkChannel, CommunicationNoise communicationNoise, CommunicationSource communicationSource) {
        networkManager.getCommunicationNetwork(networkChannel).emitNoise(communicationNoise, getTransmissionLocation(),communicationSource);
    }


}
