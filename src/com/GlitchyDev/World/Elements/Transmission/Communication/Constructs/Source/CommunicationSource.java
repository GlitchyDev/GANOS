package com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Source;

import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Enums.SourceType;

public abstract class CommunicationSource {
    private final SourceType sourceType;

    public CommunicationSource(SourceType sourceType) {
        this.sourceType = sourceType;
    }

    public SourceType getSourceType() {
        return sourceType;
    }
}
