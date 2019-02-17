package com.GlitchyDev.World.Region;

import com.GlitchyDev.Utility.HuffmanTreeUtility;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.BlockType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.EntityType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.*;

public class RegionBase {
    private final int id;
    // Placement of the bottom upper righthand corner of the region
    private Location location;
    private final BlockBase[][][] blocks;
    private final ArrayList<EntityBase> entities;
    // All regions that are connected here via openings, doors, mystical portals, EVERYTHING!!!!
    //private final ArrayList<RegionBase> connectedRegions;

    public static final RegionFileVersion CURRENT_VERSION = RegionFileVersion.VERSION_0;
    public static final RegionFileVersion LEAST_SUPPORTED_VERSION = RegionFileVersion.VERSION_0;


    public RegionBase(int id, BlockBase[][][] blocks, ArrayList<EntityBase> entities) {
        this.id = id;
        this.location = new Location(0,0,0);
        this.blocks = blocks;
        this.entities = entities;
        //this.connectedRegions = connectedRegions;
    }




    public RegionBase(InputBitUtility inputBitUtility) throws IOException {

        RegionFileVersion version = RegionFileVersion.values()[inputBitUtility.getNextCorrectIntByte()];
        //System.out.println(version);
        RegionFileType type = RegionFileType.values()[inputBitUtility.getNextCorrectIntByte()];
        //System.out.println(type);
        this.id = inputBitUtility.getNextCorrectIntByte();
        //System.out.println(id);

        int width = inputBitUtility.getNextCorrectIntByte() + 1;
        //System.out.println(width);
        int height = inputBitUtility.getNextCorrectIntByte() + 1;
        //System.out.println(height);
        int length = inputBitUtility.getNextCorrectIntByte() + 1;
        //System.out.println(length);
        this.blocks = new BlockBase[height][width][length];

        int blockPaletteSize = inputBitUtility.getNextCorrectIntByte();
        //System.out.println(blockPaletteSize);
        BlockBase[] palette = new BlockBase[blockPaletteSize];
        // Load Each Block in Pallete
        for(int i = 0; i < blockPaletteSize; i++) {
            BlockType blockType = BlockType.values()[inputBitUtility.getNextCorrectIntByte()];
            palette[i] = blockType.getBlockFromInput(inputBitUtility);
            //System.out.println(palette[i]);
        }
        HashMap<String,Object> huffmanMap = HuffmanTreeUtility.loadHuffmanTreeValues(inputBitUtility,palette);

        for(int y = 0; y < height; y++) {
            for(int x = 0; x < width; x++) {
                for(int z = 0; z < length; z++) {
                    String currentCode = "";
                    while(!huffmanMap.containsKey(currentCode)) {
                        currentCode += inputBitUtility.getNextBit() ? "1" : "0";
                    }
                    setBlockRelative(x,y,z,((BlockBase) huffmanMap.get(currentCode)).getCopy());
                }
            }
        }

        int totalEntities = inputBitUtility.getNextCorrectIntByte();
        //System.out.println(totalEntities);
        this.entities = new ArrayList<>(totalEntities);
        for(int i = 0; i < totalEntities; i++) {
            EntityType entityType = EntityType.values()[inputBitUtility.getNextCorrectIntByte()];
            entities.add(entityType.getEntityFromInput(inputBitUtility));
        }



    }

    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(CURRENT_VERSION.ordinal());
        //System.out.println(CURRENT_VERSION.ordinal());
        outputBitUtility.writeNextCorrectByteInt(RegionFileType.NORMAL.ordinal());
        //System.out.println(RegionFileType.NORMAL.ordinal());
        outputBitUtility.writeNextCorrectByteInt(id);
        //System.out.println(id);

        outputBitUtility.writeNextCorrectByteInt(getWidth()-1);
        //System.out.println(getWidth());
        outputBitUtility.writeNextCorrectByteInt(getHeight()-1);
        //System.out.println(getHeight());
        outputBitUtility.writeNextCorrectByteInt(getLength()-1);
        //System.out.println(getLength());


        // Count number of unique blocks, like ouch

        // Counts the total number of unique blocks, stores them block, count
        ArrayList<BlockBase> uniqueBlocks = new ArrayList<>();
        ArrayList<Integer> frequencies = new ArrayList<>();
        int uniques = 0;
        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                for(int z = 0; z < getLength(); z++) {
                    BlockBase blockAtSpot = getBlockRelative(x,y,z);
                    if(!uniqueBlocks.contains(blockAtSpot)) {
                        uniqueBlocks.add(blockAtSpot);
                        frequencies.add(1);
                        uniques++;
                    } else {
                        frequencies.set(uniqueBlocks.indexOf(blockAtSpot),frequencies.get(uniqueBlocks.indexOf(blockAtSpot)) + 1);
                    }
                }
            }
        }
        //System.out.println("Total Uniques " + uniques);


        Collections.sort(uniqueBlocks, Comparator.comparingInt(frequencies::indexOf));
        Collections.sort(frequencies);
        Collections.reverse(uniqueBlocks);
        Collections.reverse(frequencies);


        // Attempts to sort both these from most frequent to least frequent
        int[] frequency = new int[uniques];
        BlockBase[] palette = new BlockBase[uniques];

        int count = 0;
        for(int i: frequencies) {
            frequency[count] = i;
            count++;
        }
        //System.out.println();

        count = 0;
        //System.out.print("Unique: ");
        for(BlockBase b: uniqueBlocks) {
            //System.out.print(b + " " + frequencies.get(count));
            palette[count] = b;
            count++;
        }
        //System.out.println();
        // Finshed


        // Write Palette Length
        outputBitUtility.writeNextCorrectByteInt(palette.length);
        // Write Palette
        for(BlockBase block: palette) {
            block.writeData(outputBitUtility);
        }
        // Write Huffman Tree Values
        HuffmanTreeUtility.saveHuffmanTreeValues(outputBitUtility, palette, frequency);
        // Create Keyset
        HashMap<Object,String> keyset = HuffmanTreeUtility.generateEncodeHuffmanValues(palette,frequency);

        //System.out.println(keyset.size());

        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                for(int z = 0; z < getLength(); z++) {
                    String key = keyset.get(getBlockRelative(x,y,z));
                    for(char c: key.toCharArray()) {
                        outputBitUtility.writeNextBit(c == '1');
                    }
                }
            }
        }

        outputBitUtility.writeNextCorrectByteInt(getEntities().size());
        for(EntityBase entityBase: getEntities()) {
            entityBase.writeData(outputBitUtility, this);
        }

    }


    /**
     * When we want to place a region, or move a region
     * @param newLocation
     */
    public void placeRegion(Location newLocation) {
        Location difference = location.getLocationDifference(newLocation);
        location = newLocation;

        for(BlockBase block: getBlocksArray()) {
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

    public BlockBase[] getBlocksArray() {
        BlockBase[] blockArray = new BlockBase[getWidth() * getLength() * getHeight()];
        int i = 0;
        for(int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                for (int z = 0; z < getLength(); z++) {
                    blockArray[i] = blocks[y][x][z];
                    i++;
                }
            }
        }
        return blockArray;
    }

    public void recalculateConnections() {
        // Spread Algorithum
    }

    @Override
    public String toString() {
        return "R@" + id + "," + getLocation();
    }

    // Getters


    public BlockBase[][][] getBlocks() {
        return blocks;
    }

    public ArrayList<EntityBase> getEntities() {
        return entities;
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

    public int getId() {
        return id;
    }
}
