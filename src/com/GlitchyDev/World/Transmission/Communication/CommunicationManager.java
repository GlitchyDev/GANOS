package com.GlitchyDev.World.Transmission.Communication;

import com.GlitchyDev.World.Transmission.DetectionType;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Enums.NoiseType;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Messages.CommunicationNoise;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Source.CommunicationSource;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;

public class CommunicationManager {
    private final ArrayList<CommunicationListener> listeners;

    public CommunicationManager() {
        this.listeners = new ArrayList<>();
    }

    public void join(CommunicationListener communicationListener) {
        listeners.add(communicationListener);
    }

    public void leave(CommunicationListener communicationListener) {
        listeners.remove(communicationListener);
    }

    public void transmitMessage(CommunicationMessage communicationMessage, Location origin, CommunicationSource communicationSource) {
        for(CommunicationListener communicationListener : listeners) {
            switch(communicationListener.generateDetectionType(origin, communicationSource)) {
                case COMPREHENSION:
                    communicationListener.recieveMessage(communicationMessage, origin, communicationSource);
                    break;
                case DETECTION:
                    communicationListener.recieveNoise(new CommunicationNoise(NoiseType.ENTITY_TALKING),origin, communicationSource);
                    break;
            }
        }
    }

    public void transmitNoise(CommunicationNoise communicationNoise, Location origin, CommunicationSource soundSource) {
        for(CommunicationListener communicationListener : listeners) {
            if(communicationListener.generateDetectionType(origin,soundSource) != DetectionType.NONE) {
                communicationListener.recieveNoise(communicationNoise,origin,soundSource);
            }
        }
    }

}
