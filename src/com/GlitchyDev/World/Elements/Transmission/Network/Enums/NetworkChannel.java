package com.GlitchyDev.World.Elements.Transmission.Network.Enums;

public enum NetworkChannel {
    WALKIETALKIE_CHANNEL_0(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.SHORT_RANGE, 0),
    WALKIETALKIE_CHANNEL_1(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.SHORT_RANGE, 1),
    WALKIETALKIE_CHANNEL_2(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.SHORT_RANGE, 2),
    WALKIETALKIE_CHANNEL_3(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.SHORT_RANGE, 3),
    WALKIETALKIE_CHANNEL_4(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.SHORT_RANGE, 4),
    WALKIETALKIE_CHANNEL_5(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.MEDIUM_RANGE, 5),
    WALKIETALKIE_CHANNEL_6(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.MEDIUM_RANGE, 6),
    WALKIETALKIE_CHANNEL_7(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.MEDIUM_RANGE, 7),
    WALKIETALKIE_CHANNEL_8(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.MEDIUM_RANGE, 8),
    WALKIETALKIE_CHANNEL_9(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.MEDIUM_RANGE, 9),
    WALKIETALKIE_CHANNEL_UNKNOWN(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.UNLIMITED_RANGE, -1),

    RADIO_CHANNEL_0(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.LONG_RANGE, 0),
    RADIO_CHANNEL_1(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.LONG_RANGE, 1),
    RADIO_CHANNEL_2(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.LONG_RANGE, 2),
    RADIO_CHANNEL_3(NetworkType.WALKIE_TALKIE, NetworkTransmissionDistance.LONG_RANGE, 3),

    ;

    private final NetworkType networkType;
    private final NetworkTransmissionDistance networkTransmissionDistance;
    private final int channelNumber;

    NetworkChannel(NetworkType networkType, NetworkTransmissionDistance networkTransmissionDistance, int channelNumber) {
        this.networkType = networkType;
        this.networkTransmissionDistance = networkTransmissionDistance;
        this.channelNumber = channelNumber;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public NetworkTransmissionDistance getNetworkTransmissionDistance() {
        return networkTransmissionDistance;
    }

    public int getChannelNumber() {
        return channelNumber;
    }
}
