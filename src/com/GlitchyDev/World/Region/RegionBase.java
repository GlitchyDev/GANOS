package com.GlitchyDev.World.Region;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.ArrayList;

public class RegionBase {
    private int id;
    // Placement of the bottom upper righthand corner of the region
    private Location location;
    private final BlockBase[][][] blocks;
    private final ArrayList<EntityBase> entities;
    // All regions that are connected here via openings, doors, mystical portals, EVERYTHING!!!!
    private final ArrayList<RegionBase> connectedRegions;



    public RegionBase(BlockBase[][][] blocks, ArrayList<EntityBase> entities, ArrayList<RegionBase> connectedRegions) {
        this.location = new Location(0,0,0,null);
        this.blocks = blocks;
        this.entities = entities;
        this.connectedRegions = connectedRegions;
    }

    public RegionBase(InputBitUtility inputBitUtility) {
        try {
            RegionFileVersion version = RegionFileVersion.values()[inputBitUtility.getNextCorrectIntByte()];
            RegionFileType type = RegionFileType.values()[inputBitUtility.getNextCorrectIntByte()];
            this.id = inputBitUtility.getNextCorrectIntByte();
            this.blocks = new BlockBase[inputBitUtility.getNextCorrectIntByte()][inputBitUtility.getNextCorrectIntByte()][inputBitUtility.getNextCorrectIntByte()];
            int blockPalleteSize = inputBitUtility.getNextCorrectIntByte();
            int totalEntities = inputBitUtility.getNextCorrectIntByte();

            BlockBase[] pallete = new BlockBase[blockPalleteSize];
            




        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Data format
     *
     * Version | Format | ID | Height | Width | Length | Block Palette Size | Entity Length | Block Palette | Entities | < Later Modifiers >
     *
     *
     */

    /**
     * When we want to place a region, or move a region
     * @param newLocation
     */
    public void placeRegion(Location newLocation) {
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

    public boolean doRegionsIntersect(RegionBase comparedRegion) {
        if(getLocation().getY() + getHeight() < comparedRegion.getLocation().getY()) {
            return false;
        }
        if(comparedRegion.getLocation().getY() + comparedRegion.getHeight() < getLocation().getY()) {
            return false;
        }
        if(getLocation().getX() + getWidth() < comparedRegion.getLocation().getX()) {
            return false;
        }
        if(comparedRegion.getLocation().getX() + comparedRegion.getWidth() < getLocation().getX()) {
            return false;
        }
        if(getLocation().getZ() + getLength() < comparedRegion.getLocation().getZ()) {
            return false;
        }
        if(comparedRegion.getLocation().getZ() + comparedRegion.getLength() < getLocation().getZ()) {
            return false;
        }


        return true;

    }


    // Helper Methods

    public BlockBase getBlockRelative(Location location) {
        return getBlockRelative(location.getX(), location.getY(), location.getZ());
    }

    public void setBlockRelative(Location location, BlockBase block) {
        setBlockRelative(location.getX(), location.getY(), location.getZ(), block);
    }

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

    public void recalculateConnections() {
        // Spread Algorithum
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
