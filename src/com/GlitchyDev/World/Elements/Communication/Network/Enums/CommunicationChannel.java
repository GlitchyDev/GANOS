package com.GlitchyDev.World.Elements.Communication.Network.Enums;

public enum CommunicationChannel {
    WALKIETALKIE_CHANNEL_0(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 0),
    WALKIETALKIE_CHANNEL_1(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 1),
    WALKIETALKIE_CHANNEL_2(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 2),
    WALKIETALKIE_CHANNEL_3(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 3),
    WALKIETALKIE_CHANNEL_4(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.SHORT_RANGE, 4),
    WALKIETALKIE_CHANNEL_5(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 5),
    WALKIETALKIE_CHANNEL_6(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 6),
    WALKIETALKIE_CHANNEL_7(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 7),
    WALKIETALKIE_CHANNEL_8(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 8),
    WALKIETALKIE_CHANNEL_9(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.MEDIUM_RANGE, 9),
    WALKIETALKIE_CHANNEL_UNKNOWN(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.UNLIMITED_RANGE, -1),

    RADIO_CHANNEL_0(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.LONG_RANGE, 0),
    RADIO_CHANNEL_1(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.LONG_RANGE, 1),
    RADIO_CHANNEL_2(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.LONG_RANGE, 2),
    RADIO_CHANNEL_3(CommunicationNetworkType.WALKIE_TALKIE, CommunicationChannelDistance.LONG_RANGE, 3),

    ;

    private final CommunicationNetworkType communicationNetworkType;
    private final CommunicationChannelDistance communicationChannelDistance;
    private final int channelNumber;

    CommunicationChannel(CommunicationNetworkType communicationNetworkType, CommunicationChannelDistance communicationChannelDistance, int channelNumber) {
        this.communicationNetworkType = communicationNetworkType;
        this.communicationChannelDistance = communicationChannelDistance;
        this.channelNumber = channelNumber;
    }

    public CommunicationNetworkType getCommunicationNetworkType() {
        return communicationNetworkType;
    }

    public CommunicationChannelDistance getCommunicationChannelDistance() {
        return communicationChannelDistance;
    }

    public int getChannelNumber() {
        return channelNumber;
    }
}
