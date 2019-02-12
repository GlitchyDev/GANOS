package com.GlitchyDev.World.Region;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;

public class RegionBase {
    private Location location;
    private RegionLocation regionLocation;
    private final BlockBase[][][] blocks;
    private final ArrayList<EntityBase> entities;
    private final ArrayList<RegionBase> connectedRegions;



    public RegionBase(Location location, RegionLocation regionLocation, BlockBase[][][] blocks, ArrayList<EntityBase> entities, ArrayList<RegionBase> connectedRegions) {
        this.location = new Location(0,0,0,null);
        this.regionLocation = regionLocation;
        this.blocks = blocks;
        this.entities = entities;
        this.connectedRegions = connectedRegions;
        updateLocation(location);
    }

    /**
     * When we want to change the location of this region, or place it
     * @param newLocation
     */
    public void updateLocation(Location newLocation) {
        Location difference = location.getLocationDifference(newLocation);
        location = newLocation;

        for(BlockBase block: getBlocksArrayList()) {
            block.setLocation(block.getLocation().getOffsetLocation(difference));
        }
        for(EntityBase entity: entities) {
            entity.setLocation(entity.getLocation().getOffsetLocation(difference));
        }
    }

    public boolean isLocationInRegion(Location selectLocation) {
        if(getLocation().getY() <= selectLocation.getY() && selectLocation.getY()  < getLocation().getY() + getHeight()) {
            if(getLocation().getX() <= selectLocation.getX() && selectLocation.getX()  < getLocation().getX() + getWidth()) {
                if(getLocation().getZ() <= selectLocation.getZ() && selectLocation.getZ()  < getLocation().getZ() + getLength()) {
                    return true;
                }
            }
        }
        return false;
    }


    // Helper Methods
    public BlockBase getBlockRelative(int relativeX, int relativeY, int relativeZ) {
        return blocks[relativeY][relativeX][relativeZ];
    }

    public void setBlockRelative(int relativeX, int relativeY, int relativeZ, BlockBase block) {
        blocks[relativeY][relativeX][relativeZ] = block;
    }

    public ArrayList<BlockBase> getBlocksArrayList() {
        ArrayList<BlockBase> blockList = new ArrayList<>(getWidth() * getLength() * getHeight());
        for(int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                for (int z = 0; z < getLength(); z++) {
                    blockList.add(blocks[y][x][z]);
                }
            }
        }
        return blockList;
    }




    // Getters

    public BlockBase[][][] getBlocks() {
        return blocks;
    }
    public ArrayList<EntityBase> getEntities() {
        return entities;
    }

    public ArrayList<RegionBase> getConnectedRegions() {
        return connectedRegions;
    }

    public int getWidth() {
        return blocks[0].length;
    }

    public int getLength() {
        return blocks[0][0].length;
    }

    public int getHeight() {
        return blocks.length;
    }

    public Location getLocation() {
        return location;
    }
}
