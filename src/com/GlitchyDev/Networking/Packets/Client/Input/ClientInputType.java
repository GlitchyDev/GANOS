package com.GlitchyDev.Networking.Packets.Client.Input;

import com.GlitchyDev.World.Direction;

public enum ClientInputType {
    MOVE_PLAYER_NORTH,
    MOVE_PLAYER_EAST,
    MOVE_PLAYER_SOUTH,
    MOVE_PLAYER_WEST,
    MOVE_PLAYER_UP,
    MOVE_PLAYER_DOWN;



    public static ClientInputType getInputDirection(Direction direction) {
        switch(direction) {
            case NORTH:
                return MOVE_PLAYER_NORTH;
            case EAST:
                return MOVE_PLAYER_EAST;
            case SOUTH:
                return MOVE_PLAYER_SOUTH;
            case WEST:
                return MOVE_PLAYER_WEST;
            case ABOVE:
                return MOVE_PLAYER_UP;
            case BELOW:
                return MOVE_PLAYER_DOWN;
            default:
                return MOVE_PLAYER_NORTH;
        }
    }

    public Direction getDirection() {
        switch(this) {
            case MOVE_PLAYER_NORTH:
                return Direction.NORTH;
            case MOVE_PLAYER_EAST:
                return Direction.EAST;
            case MOVE_PLAYER_SOUTH:
                return Direction.SOUTH;
            case MOVE_PLAYER_WEST:
                return Direction.WEST;
            case MOVE_PLAYER_UP:
                return Direction.ABOVE;
            case MOVE_PLAYER_DOWN:
                return Direction.BELOW;
            default:
                return Direction.NORTH;
        }
    }
}
