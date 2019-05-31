package com.GlitchyDev.World.Elements.Transmission.Communication;

import com.GlitchyDev.World.Elements.Transmission.DetectionType;
import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Messages.CommunicationNoise;
import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Source.CommunicationSource;
import com.GlitchyDev.World.Location;

public interface CommunicationListener {

    DetectionType generateDetectionType(Location origin, CommunicationSource soundSource);
    void recieveMessage(CommunicationMessage message, Location origin, CommunicationSource communicationSource);
    void recieveNoise(CommunicationNoise noise, Location origin, CommunicationSource communicationSource);
}
