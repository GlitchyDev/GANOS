package com.GlitchyDev.World;


import org.joml.Vector3i;

import java.util.UUID;

public class Location {
    private final Vector3i position;
    private final UUID worldUUID;


    public Location(UUID worldUUID) {
        position = new Vector3i();
        this.worldUUID = worldUUID;
    }

    public Location(int x, int y, int z, UUID worldUUID) {
        position = new Vector3i(x, y, z);
        this.worldUUID = worldUUID;
    }

    public Location(Location location) {
        position = new Vector3i(location.getPosition());
        this.worldUUID = location.getWorldUUID();
    }


    /**
     *
     * @param location
     * @ return Returns a Location offset by the provided Location from the current location
     */
    public Location getOffsetLocation(Location location) {
        return getOffsetLocation(location.getX(), location.getY(), location.getZ());
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     * @ return Returns a Location offset by the provided coordinates from the current location
     */
    public Location getOffsetLocation(int x, int y, int z) {
        return new Location(this.position.x + x, this.position.y + y, this.position.z + z, worldUUID);
    }

    /**
     *
     * @param direction
     * @return Outputs the location one unit in the cardinal direction
     */
    public Location getDirectionLocation(Direction direction) {
        switch (direction) {
            case ABOVE:
                return getAbove();
            case BELOW:
                return getBelow();
            case NORTH:
                return getNorth();
            case EAST:
                return getEast();
            case SOUTH:
                return getSouth();
            case WEST:
                return getWest();
        }
        return this;
    }

    /**
     *
     * @param direction
     * @param distance
     * @return Outputs the location X units in the cardinal direction
     */
    public Location getDirectionLocation(Direction direction, int distance) {
        Location output = this;
        for(int i = 0; i < distance; i++) {
            output = output.getDirectionLocation(direction);
        }
        return output;
    }

    public Location getAbove() {
        return getOffsetLocation(0, 1, 0);
    }

    public Location getBelow() {
        return getOffsetLocation(0, -1, 0);
    }

    public Location getNorth() {
        return getOffsetLocation(0, 0, -1);
    }

    public Location getEast() {
        return getOffsetLocation(1, 0, 0);
    }

    public Location getSouth() {
        return getOffsetLocation(0, 0, 1);
    }

    public Location getWest() {
        return getOffsetLocation(-1, 0, 0);
    }


    /**
     * Outputs a Location difference of the current location and the provided location
     * @param location
     * @return
     */
    public Location getLocationDifference(Location location) {
        return new Location(location.getX() - getX(), location.getY() - getY(), location.getZ() - getZ(), worldUUID);
    }

    /**
     *
     * @param location
     * @return The distance between the current and the provided Location
     */
    public double getDistance(Location location) {
        return Math.sqrt(Math.pow(location.getX() - getX(),2) + Math.pow(location.getY() - getY(),2) + Math.pow(location.getZ() - getZ(),2));
    }

    @Override
    public Location clone() {
        return new Location(getX(), getY(), getZ(), worldUUID);
    }

    @Override
    public String toString() {
        return "l@" + getX() + "," + getY() + "," + getZ();
    }

    // Getters

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public int getZ() {
        return position.z;
    }

    public Vector3i getPosition() {
        return position;
    }

    public Vector3i getNormalizedPosition() {
        return new Vector3i(position).mul(2);
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public boolean hasWorldUUID() {
        return worldUUID != null;
    }
}
