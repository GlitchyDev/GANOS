package com.GlitchyDev.World.Elements.Transmission.Network;

import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Enums.NoiseType;
import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Messages.CommunicationNoise;
import com.GlitchyDev.World.Elements.Transmission.DetectionType;
import com.GlitchyDev.World.Elements.Transmission.Network.Enums.NetworkChannel;
import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Source.CommunicationSource;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;

public class CommunicationNetwork {
    private final NetworkChannel networkChannel;
    private final ArrayList<NetworkedDeviceBase> connectedDevices;

    public CommunicationNetwork(NetworkChannel networkChannel) {
        this.networkChannel = networkChannel;
        connectedDevices = new ArrayList<>();
    }

    public void deviceJoin(NetworkedDeviceBase device) {
        connectedDevices.add(device);
    }

    public void deviceLeave(NetworkedDeviceBase device) {
        connectedDevices.remove(device);
    }

    public void transmitMessage(CommunicationMessage communicationMessage, Location origin, CommunicationSource communicationSource) {
        for(NetworkedDeviceBase communcationDevice: connectedDevices) {
            switch(communcationDevice.generateDetectionType(origin, networkChannel.getNetworkTransmissionDistance(), communicationSource)) {
                case COMPREHENSION:
                    communcationDevice.recieveMessage(communicationMessage, origin, networkChannel.getNetworkTransmissionDistance(), communicationSource);
                    break;
                case DETECTION:
                    communcationDevice.recieveNoise(new CommunicationNoise(NoiseType.ENTITY_TALKING),origin,networkChannel.getNetworkTransmissionDistance(), communicationSource);
                    break;
            }
        }
    }

    public void transmitNoise(CommunicationNoise communicatioNoise, Location origin, CommunicationSource communicationSource) {
        for(NetworkedDeviceBase communcationDevice: connectedDevices) {
            if(communcationDevice.generateDetectionType(origin, networkChannel.getNetworkTransmissionDistance(), communicationSource) != DetectionType.NONE) {
                communcationDevice.recieveNoise(communicatioNoise,origin, networkChannel.getNetworkTransmissionDistance(), communicationSource);
            }
        }
    }


    public NetworkChannel getNetworkChannel() {
        return networkChannel;
    }

    public ArrayList<NetworkedDeviceBase> getConnectedDevices() {
        return connectedDevices;
    }
}
