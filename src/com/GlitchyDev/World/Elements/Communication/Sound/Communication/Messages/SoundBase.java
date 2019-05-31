package com.GlitchyDev.World.Elements.Communication.Sound.Communication.Messages;

import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.SoundType;

public abstract class SoundBase {
    private final SoundType soundType;

    public SoundBase(SoundType soundType) {
        this.soundType = soundType;
    }

    public SoundType getSoundType() {
        return soundType;
    }
}
