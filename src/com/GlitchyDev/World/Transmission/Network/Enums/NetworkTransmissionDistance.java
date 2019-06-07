package com.GlitchyDev.World.Transmission.Network.Enums;

public enum NetworkTransmissionDistance {
    SHORT_RANGE(500),
    MEDIUM_RANGE(1000),
    LONG_RANGE(10000),
    UNLIMITED_RANGE(Integer.MAX_VALUE)
    ;

    private final int effectiveCommunicationDistance;
    NetworkTransmissionDistance(int effectiveCommunicationDistance) {
        this.effectiveCommunicationDistance = effectiveCommunicationDistance;
    }

    public int getEffectiveCommunicationDistance() {
        return effectiveCommunicationDistance;
    }
}
