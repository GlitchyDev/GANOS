package com.GlitchyDev.World.Elements.Communication.Sound.Communication.Source;

import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.SourceType;

public abstract class SoundSourceBase {
    private final SourceType sourceType;

    public SoundSourceBase(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }
}
