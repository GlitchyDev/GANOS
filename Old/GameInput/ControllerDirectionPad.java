package com.GlitchyDev.Old.GameInput;

import com.GlitchyDev.Old.World.Direction;

public enum ControllerDirectionPad {
    NONE,
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public ControllerDirectionPad reverse()
    {
        switch(this)
        {
            case NONE:
                return NONE;
            case NORTH:
                return SOUTH;
            case NORTH_EAST:
                return SOUTH_WEST;
            case EAST:
                return WEST;
            case SOUTH_EAST:
                return NORTH_WEST;
            case SOUTH:
                return NORTH;
            case SOUTH_WEST:
                return NORTH_EAST;
            case WEST:
                return EAST;
            case NORTH_WEST:
                return SOUTH_EAST;
            default:
                return NONE;
        }
    }

    public Direction getDirection() {
        switch(this)
        {
            case NORTH:
                return Direction.NORTH;
            case EAST:
                return Direction.EAST;
            case SOUTH:
                return Direction.SOUTH;
            case WEST:
                return Direction.WEST;
        }
        return Direction.NORTH;
    }
}
