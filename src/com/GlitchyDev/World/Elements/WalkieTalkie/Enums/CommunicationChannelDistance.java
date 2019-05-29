package com.GlitchyDev.World.Elements.WalkieTalkie.Enums;

public enum CommunicationChannelDistance {
    SHORT_RANGE(500),
    MEDIUM_RANGE(1000),
    LONG_RANGE(10000),
    UNLIMITED_RANGE(Integer.MAX_VALUE)
    ;

    private final int effectiveCommunicationDistance;
    CommunicationChannelDistance(int effectiveCommunicationDistance) {
        this.effectiveCommunicationDistance = effectiveCommunicationDistance;
    }

    public int getEffectiveCommunicationDistance() {
        return effectiveCommunicationDistance;
    }
}
