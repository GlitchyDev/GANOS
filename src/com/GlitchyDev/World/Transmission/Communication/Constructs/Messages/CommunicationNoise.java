package com.GlitchyDev.World.Transmission.Communication.Constructs.Messages;

import com.GlitchyDev.World.Transmission.Communication.Constructs.Enums.NoiseType;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Enums.CommunicationType;

public class CommunicationNoise extends Communication {
    private final NoiseType noiseType;

    public CommunicationNoise(NoiseType noiseType) {
        super(CommunicationType.NOISE);
        this.noiseType = noiseType;
    }

    public NoiseType getNoiseType() {
        return noiseType;
    }
}
