package com.GlitchyDev.World.Elements.WalkieTalkie.Enums;

public enum WalkieTalkieStateTransition {
    NONE(0.0),

    IDLE_TO_VIEW(0.5),
    VIEW_TO_IDLE(0.2),

    VIEW_TO_ACTIVE(0.2),
    ACTIVE_TO_VIEW(0.2),

    IDLE_TO_ACTIVE(0.5),
    ACTIVE_TO_IDLE(0.2);

    private final double length;
    WalkieTalkieStateTransition(double length) {
        this.length = length;
    }

    public static WalkieTalkieStateTransition getWalkieTalkieTransition(WalkieTalkieState previous, WalkieTalkieState next) {
        System.out.println("Determining transition " + previous + " " + next);
        System.out.println("Determining transition " + previous.getPosition() + " " + next.getPosition());
        switch(previous.getPosition()) {
            case IDLE:
                switch(next.getPosition()) {
                    case VIEW:
                        return IDLE_TO_VIEW;
                    case ACTIVE:
                        return IDLE_TO_ACTIVE;
                }
                break;
            case VIEW:
                switch(next.getPosition()) {
                    case IDLE:
                        return VIEW_TO_IDLE;
                    case ACTIVE:
                        return VIEW_TO_ACTIVE;
                }
                break;
            case ACTIVE:
                switch(next.getPosition()) {
                    case IDLE:
                        return ACTIVE_TO_IDLE;
                    case VIEW:
                        return ACTIVE_TO_VIEW;
                }
                break;
        }
        return NONE;
    }

    public double getLength() {
        return length;
    }

    public int getOffsetDifference() {
        switch(this) {
            case IDLE_TO_VIEW:
                return 17;
            case VIEW_TO_IDLE:
                return -17;
            case VIEW_TO_ACTIVE:
                return 18;
            case IDLE_TO_ACTIVE:
                return 35;
            case ACTIVE_TO_IDLE:
                return -35;
            case ACTIVE_TO_VIEW:
                return -18;
            default:
                return 0;
        }
    }
}
