package com.GlitchyDev.World.Elements.WalkieTalkie.Enums;

public enum CommunicationChannel {
    WALKIETALKIE_CHANNEL_0(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 0),
    WALKIETALKIE_CHANNEL_1(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 1),
    WALKIETALKIE_CHANNEL_2(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 2),
    WALKIETALKIE_CHANNEL_3(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 3),
    WALKIETALKIE_CHANNEL_4(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 4),
    WALKIETALKIE_CHANNEL_5(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 5),
    WALKIETALKIE_CHANNEL_6(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 6),
    WALKIETALKIE_CHANNEL_7(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 7),
    WALKIETALKIE_CHANNEL_8(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 8),
    WALKIETALKIE_CHANNEL_9(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 9),
    WALKIETALKIE_CHANNEL_UNKNOWN(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.UNLIMITED_RANGE, -1),

    RADIO_CHANNEL_0(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.LONG_RANGE, 0),
    RADIO_CHANNEL_1(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.LONG_RANGE, 1),
    RADIO_CHANNEL_2(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.LONG_RANGE, 2),
    RADIO_CHANNEL_3(CommunicationDeviceType.WALKIE_TALKIE, CommunicationChannelDistance.LONG_RANGE, 3),

    ;

    private final CommunicationDeviceType communicationDeviceType;
    private final CommunicationChannelDistance communicationChannelDistance;
    private final int channelNumber;

    CommunicationChannel(CommunicationDeviceType communicationDeviceType, CommunicationChannelDistance communicationChannelDistance, int channelNumber) {
        this.communicationDeviceType = communicationDeviceType;
        this.communicationChannelDistance = communicationChannelDistance;
        this.channelNumber = channelNumber;
    }

    public CommunicationDeviceType getCommunicationDeviceType() {
        return communicationDeviceType;
    }

    public CommunicationChannelDistance getCommunicationChannelDistance() {
        return communicationChannelDistance;
    }

    public int getChannelNumber() {
        return channelNumber;
    }
}
