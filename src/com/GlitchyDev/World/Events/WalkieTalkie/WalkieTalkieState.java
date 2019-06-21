package com.GlitchyDev.World.Events.WalkieTalkie;

public enum WalkieTalkieState {

    // Idle States
    ON_IDLE(WalkieTalkieStatePosition.IDLE, false, true, false, WalkieTalkieDisplay.BLANK), // Is able to receive messages, enter USE, battery drain
    OFF_IDLE(WalkieTalkieStatePosition.IDLE, false, false, false, WalkieTalkieDisplay.BLANK), // Is able to enter Booting, no passive drain
    DEAD_IDLE(WalkieTalkieStatePosition.IDLE, false, false, false, WalkieTalkieDisplay.BLANK), // No Battery, can not boot

    // View
    SPEAKER_VIEW(WalkieTalkieStatePosition.VIEW, true, true, false), // As long as required, shift to Idle
    MESSAGE_VIEW(WalkieTalkieStatePosition.VIEW, true, true, false), // As long as required, shift to Idle
    INHABITANT_VIEW(WalkieTalkieStatePosition.VIEW, true, true, false), // As long as required, shift to Idle


    // Active
    BOOTING_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 5.0, true, true, false), // Enters USE, consumes some battery?
    USE_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 3.0, false, true, true), // General Hub for all actions, returns to Idle
    TALKING_ACTIVE(WalkieTalkieStatePosition.ACTIVE, true, true, false), // As long as required, consumes Battery, emits signal, lights sign
    SHOW_VOLUME_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 1.0, false, true, false), // Shows volume level
    SHOW_BATTERY_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 1.0, false, true, false), // Shows battery level
    SHOW_CHANNEL_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 1.0, false, true, false), // Adjusts Channel

    OFF_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 3.0,false, false, false, WalkieTalkieDisplay.BLANK), // No passive battery drain, can enter boot
    DEAD_ACTIVE(WalkieTalkieStatePosition.ACTIVE, 3.0, false, false, false, WalkieTalkieDisplay.BLANK), // Forced when battery is < 0% No Activity

    ;


    private final double stateLength;
    private final boolean hasTimeLimit;
    private final WalkieTalkieStatePosition position;
    private final boolean dominatingState;
    private final boolean isPowered;
    private final boolean hasCustomScreen;
    private final WalkieTalkieDisplay stateScreenDisplay;
    private final boolean usesDimScreen;

    WalkieTalkieState(WalkieTalkieStatePosition position, double stateLength, boolean dominatingState, boolean isPowered, boolean usesDimScreen, WalkieTalkieDisplay stateScreenDisplay ) {
        this.position = position;
        this.stateLength = stateLength;
        this.hasTimeLimit = true;
        this.dominatingState = dominatingState;
        this.isPowered = isPowered;
        this.usesDimScreen = usesDimScreen;
        this.stateScreenDisplay = stateScreenDisplay;
        this.hasCustomScreen = false;
    }
    WalkieTalkieState(WalkieTalkieStatePosition position, boolean dominatingState, boolean isPowered, boolean usesDimScreen, WalkieTalkieDisplay stateScreenDisplay) {
        this.position = position;
        this.stateLength = 0.0;
        this.hasTimeLimit = false;
        this.dominatingState = dominatingState;
        this.isPowered = isPowered;
        this.usesDimScreen = usesDimScreen;
        this.stateScreenDisplay = stateScreenDisplay;
        this.hasCustomScreen = false;
    }

    WalkieTalkieState(WalkieTalkieStatePosition position, double stateLength, boolean dominatingState, boolean isPowered, boolean usesDimScreen) {
        this.position = position;
        this.stateLength = stateLength;
        this.hasTimeLimit = true;
        this.dominatingState = dominatingState;
        this.isPowered = isPowered;
        this.usesDimScreen = usesDimScreen;
        this.stateScreenDisplay = WalkieTalkieDisplay.BLANK;
        this.hasCustomScreen = true;
    }
    WalkieTalkieState(WalkieTalkieStatePosition position, boolean dominatingState, boolean isPowered, boolean usesDimScreen) {
        this.position = position;
        this.stateLength = 0.0;
        this.hasTimeLimit = false;
        this.dominatingState = dominatingState;
        this.isPowered = isPowered;
        this.usesDimScreen = usesDimScreen;
        this.stateScreenDisplay = WalkieTalkieDisplay.BLANK;
        this.hasCustomScreen = true;
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

    public boolean isPowered() {
        return isPowered;
    }



    public boolean isHasCustomScreen() {
        return hasCustomScreen;
    }

    public boolean usesDimScreen() {
        return usesDimScreen;
    }

    public WalkieTalkieDisplay getStateScreenDisplay() {
        return stateScreenDisplay;
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
