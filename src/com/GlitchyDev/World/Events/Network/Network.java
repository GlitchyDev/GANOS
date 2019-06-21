package com.GlitchyDev.World.Events.Network;

import com.GlitchyDev.World.Events.Communication.Constructs.Enums.NoiseType;
import com.GlitchyDev.World.Events.Communication.Constructs.Messages.CommunicationNoise;
import com.GlitchyDev.World.Events.Communication.DetectionType;
import com.GlitchyDev.World.Events.Network.Enums.NetworkChannel;
import com.GlitchyDev.World.Events.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Events.Communication.Constructs.Source.CommunicationSource;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;

public class Network {
    private final NetworkChannel networkChannel;
    private final ArrayList<NetworkedDeviceBase> connectedDevices;

    public Network(NetworkChannel networkChannel) {
        this.networkChannel = networkChannel;
        connectedDevices = new ArrayList<>();
    }

    public void deviceJoin(NetworkedDeviceBase device) {
        connectedDevices.add(device);
    }

    public void deviceLeave(NetworkedDeviceBase device) {
        connectedDevices.remove(device);
    }

    public void emitMessage(CommunicationMessage communicationMessage, Location origin, CommunicationSource communicationSource) {
        for(NetworkedDeviceBase communcationDevice: connectedDevices) {
            switch(communcationDevice.generateDetectionType(origin, networkChannel.getNetworkTransmissionDistance(), communicationSource)) {
                case COMPREHENSION:
                    communcationDevice.receiveMessage(communicationMessage, origin, networkChannel.getNetworkTransmissionDistance(), communicationSource);
                    break;
                case DETECTION:
                    communcationDevice.receiveNoise(new CommunicationNoise(NoiseType.ENTITY_TALKING),origin,networkChannel.getNetworkTransmissionDistance(), communicationSource);
                    break;
            }
        }
    }

    public void emitNoise(CommunicationNoise communicatioNoise, Location origin, CommunicationSource communicationSource) {
        for(NetworkedDeviceBase communcationDevice: connectedDevices) {
            if(communcationDevice.generateDetectionType(origin, networkChannel.getNetworkTransmissionDistance(), communicationSource) != DetectionType.NONE) {
                communcationDevice.receiveNoise(communicatioNoise,origin, networkChannel.getNetworkTransmissionDistance(), communicationSource);
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
