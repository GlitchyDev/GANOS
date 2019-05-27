package com.GlitchyDev.World.Elements.WalkieTalkie.Enums;

public enum WalkieTalkieState {

    // Idle States
    ON_IDLE(WalkieTalkieStatePosition.IDLE, false), // Is able to receive messages, enter USE, battery drain
    OFF_IDLE(WalkieTalkieStatePosition.IDLE, false), // Is able to enter Booting, no passive drain
    DEAD_IDLE(WalkieTalkieStatePosition.IDLE, false), // No Battery, can not boot


    SPEAKER_VIEW(WalkieTalkieStatePosition.VIEW, true), // As long as required, shift to Idle
    MESSAGE_VIEW(WalkieTalkieStatePosition.VIEW, true), // As long as required, shift to Idle
    INHABITANT_VIEW(WalkieTalkieStatePosition.VIEW, true), // As long as required, shift to Idle

    OFF_ACTIVE(WalkieTalkieStatePosition.ACTIVE, false), // No passive battery drain, can enter boot
    BOOTING_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 10.0, false), // Enters USE, consumes some battery?
    DEAD_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 10.0, false), // Forced when battery is < 0% No Activity

    USE_ACTIVE(WalkieTalkieStatePosition.ACTIVE,10.0, false), // General Hub for all actions, returns to Idle
    TALKING_ACTIVE(WalkieTalkieStatePosition.ACTIVE, true), // As long as required, consumes Battery, emits signal, lights sign
    SHOW_VOLUME_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 5.0, false), // Shows volume level
    SHOW_BATTERY_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 1.0, false), // Shows battery level
    SHOW_CHANNEL_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 1.0, false), // Adjusts Channel
    ;


    private final double stateLength;
    private final boolean hasTimeLimit;
    private final WalkieTalkieStatePosition position;
    private final boolean dominatingState;
    WalkieTalkieState(WalkieTalkieStatePosition position, double stateLength, boolean dominatingState) {
        this.position = position;
        this.stateLength = stateLength;
        this.hasTimeLimit = true;
        this.dominatingState = dominatingState;
    }
    WalkieTalkieState(WalkieTalkieStatePosition position, boolean dominatingState) {
        this.position = position;
        this.stateLength = 0.0;
        this.hasTimeLimit = false;
        this.dominatingState = dominatingState;
    }

    public double getStateLength() {
        return stateLength;
    }
    public boolean hasSetTimeLimit() {
        return hasTimeLimit;
    }

    public WalkieTalkieStatePosition getPosition() {
        return position;
    }

    public boolean isDominatingState() {
        return dominatingState;
    }

    public WalkieTalkieState getNextState() {
        switch(this) {
            case SPEAKER_VIEW:
                return ON_IDLE;
            case MESSAGE_VIEW:
                return ON_IDLE;
            case INHABITANT_VIEW:
                return ON_IDLE;
            case OFF_ACTIVE:
                return OFF_IDLE;
            case BOOTING_ACTIVE:
                return USE_ACTIVE;
            case DEAD_ACTIVE:
                return DEAD_IDLE;
            case TALKING_ACTIVE:
            case SHOW_VOLUME_ACTIVE:
            case SHOW_BATTERY_ACTIVE:
            case SHOW_CHANNEL_ACTIVE:
                return USE_ACTIVE;
            default:
                return ON_IDLE;
        }
    }

    public int getOffset() {
        switch(this.position) {
            case IDLE:
                return 21;
            case VIEW:
                return 38;
            case ACTIVE:
                return 56;
            default:
                return 56;
        }
    }


}
