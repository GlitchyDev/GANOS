package com.GlitchyDev.World;

public enum Direction {
    ABOVE,
    BELOW,
    NORTH,
    EAST,
    SOUTH,
    WEST,
    ;


    private static final Direction[] cardinal = {NORTH,EAST,SOUTH,WEST};
    private static final Direction[] completeCardinal = {ABOVE,BELOW,NORTH,EAST,SOUTH,WEST};


    public Direction reverse() {
        switch(this) {
            case ABOVE:
                return BELOW;
            case BELOW:
                return ABOVE;
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
        }
        return ABOVE;
    }

    public int getRotation() {
        switch(this) {
            case NORTH:
                return 0;
            case EAST:
                return 90;
            case SOUTH:
                return 180;
            case WEST:
                return 270;
        }
        return 0;
    }

    public static Direction[] getCardinal() {
        return Direction.cardinal;
    }

    public static Direction[] getCompleteCardinal() {
        return completeCardinal;
    }

}
