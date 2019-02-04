package com.GlitchyDev.Old.World;

import org.joml.Vector3i;


/**
 * A Position within a world, each block and entity requires one to operate
 */
public class Location {
    private final Vector3i position;
    private final World world;


    public Location(World world) {

        position = new Vector3i();
        this.world = world;
    }

    public Location(int x, int y, int z, World world) {

        position = new Vector3i(x, y, z);
        this.world = world;
    }

    public Location(Vector3i position, World world) {
        this.position = position;
        this.world = world;
    }

    public Location(Location location)
    {
        position = new Vector3i(location.getPosition());
        this.world = location.getWorld();
    }



    public Location getOffsetLocation(int x, int y, int z) {

        Location offsetCopy = this.clone();
        offsetCopy.getPosition().add(x,y,z);
        return offsetCopy;
    }


    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public int getZ() {
        return position.z;
    }

    public World getWorld() {return world;}



    public Vector3i getPosition() {
        return position;
    }

    public Location getAbove()
    {
        return getOffsetLocation(0,1,0);
    }
    public Location getBelow()
    {
        return getOffsetLocation(0,-1,0);
    }
    public Location getNorth()
    {
        return getOffsetLocation(0,0,-1);
    }
    public Location getEast()
    {
        return getOffsetLocation(1,0,0);
    }
    public Location getSouth()
    {
        return getOffsetLocation(0,0,1);
    }
    public Location getWest()
    {
        return getOffsetLocation(-1,0,0);
    }

    public Location clone()
    {
        return new Location(this);
    }


    public Location getDirectionLocation(Direction direction)
    {
        switch(direction)
        {
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

    @Override
    public String toString() {
        return "l@" + getX() + "," + getY() + "," + getZ();
    }
}
