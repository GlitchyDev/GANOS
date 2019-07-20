package com.GlitchyDev.World;


import org.joml.Vector3f;

import java.util.UUID;

public class Location {
    private final Vector3f position;
    private final UUID worldUUID;


    public Location(UUID worldUUID) {
        position = new Vector3f();
        this.worldUUID = worldUUID;
    }

    public Location(int x, int y, int z, UUID worldUUID) {
        position = new Vector3f(x, y, z);
        this.worldUUID = worldUUID;
    }

    public Location(Location location) {
        position = new Vector3f(location.getPosition());
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
        return new Location((int)(this.position.x + x), (int)(this.position.y + y), (int)(this.position.z + z), worldUUID);
    }

    /**
     *
     * @param direction
     * @return Outputs the location one unit in the cardinal direction
     */
    public Location getOffsetDirectionLocation(Direction direction) {
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
    public Location getOffsetDirectionLocation(Direction direction, int distance) {
        Location output = this;
        for(int i = 0; i < distance; i++) {
            output = output.getOffsetDirectionLocation(direction);
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
        return new Location(Math.abs(location.getX() - getX()), Math.abs(location.getY() - getY()), Math.abs(location.getZ() - getZ()), worldUUID);
    }

    /**
     *
     * @param location
     * @return The distance between the current and the provided Location
     */
    public double getDistance(Location location) {
        if(worldUUID.equals(location.getWorldUUID())) {
            return Math.sqrt(Math.pow(location.getX() - getX(), 2) + Math.pow(location.getY() - getY(), 2) + Math.pow(location.getZ() - getZ(), 2));
        } else {
            return Double.MAX_VALUE;
        }
    }

    public double getDistance(Vector3f position) {
        return Math.sqrt(Math.pow(position.x() - getX(), 2) + Math.pow(position.y() - getY(), 2) + Math.pow(position.z() - getZ(), 2));

    }

    public boolean isLocationCloser(Location location1, Location location2) {
        return getDistance(location1) < getDistance(location2);
    }

    @Override
    public Location clone() {
        return new Location(getX(), getY(), getZ(), worldUUID);
    }

    @Override
    public String toString() {
        return "l@" + getX() + "," + getY() + "," + getZ() + "," + worldUUID;
    }

    // Getters


    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Location) {
            return ((Location) obj).getX() == getX() && ((Location) obj).getY() == getY() && ((Location) obj).getZ() == getZ() && ((Location) obj).getWorldUUID().equals(getWorldUUID());
        }
        return false;
    }

    public int getX() {
        return (int) position.x;
    }

    public int getY() {
        return (int) position.y;
    }

    public int getZ() {
        return (int) position.z;
    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector3f getNormalizedPosition() {
        return new Vector3f(position).mul(2);
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }
}
