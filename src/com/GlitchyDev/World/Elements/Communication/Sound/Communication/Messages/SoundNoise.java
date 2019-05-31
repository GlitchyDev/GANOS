package com.GlitchyDev.World.Elements.Communication.Sound.Communication.Messages;

import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.NoiseType;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.SoundType;

public class SoundNoise extends SoundBase {
    private final NoiseType noiseType;

    public SoundNoise(NoiseType noiseType) {
        super(SoundType.NOISE);
        this.noiseType = noiseType;
    }

    public NoiseType getNoiseType() {
        return noiseType;
    }
}
