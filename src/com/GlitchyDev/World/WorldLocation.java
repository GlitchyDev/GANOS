package com.GlitchyDev.World;

public class WorldLocation extends Location {
    private final World world;

    public WorldLocation(int x, int y, int z, World world) {
        super(x,y,z);
        this.world = world;
    }

    public WorldLocation(WorldLocation worldLocation) {
        super(worldLocation.getX(), worldLocation.getY(), worldLocation.getZ());
        this.world = worldLocation.getWorld();
    }

    public WorldLocation getOffsetWorldLocation(Location location) {
        return getOffsetWorldLocation(location.getX(), location.getY(), location.getZ());
    }

    public WorldLocation getOffsetWorldLocation(int x, int y, int z) {
        return new WorldLocation(getX() + x, getY() + y, getZ() + z, getWorld());
    }



    public World getWorld() {
        return world;
    }

}
