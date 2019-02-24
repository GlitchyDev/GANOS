package com.GlitchyDev.World.Region;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.HuffmanTreeUtility;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.*;


/**
 * A Holder class for Regions, used to keep track of Blocks, Entities, the connections between it and other Regions, Ect
 */
public class RegionBase {
    public static final RegionFileVersion CURRENT_VERSION = RegionFileVersion.VERSION_0;
    public static final RegionFileVersion LEAST_SUPPORTED_VERSION = RegionFileVersion.VERSION_0;
    private final UUID worldUUID;

    private final UUID regionUUID; // Identifies the region as UNIQUE
    private final BlockBase[][][] blocks;
    private final ArrayList<EntityBase> entities;
    private Location location;     // Placement of the bottom upper right hand corner of the region


    public RegionBase(WorldGameState worldGameState, UUID worldUUID, int width, int length, int height) {
        this.worldUUID = worldUUID;

        this.regionUUID = UUID.randomUUID();
        this.location = new Location(0,0,0);
        this.blocks = new BlockBase[height][width][length];
        this.entities = new ArrayList<>();

        populateRegions(worldGameState);
    }

    /**
     * Fills newly created regions so they literally can't be null
     * @param worldGameState
     */
    private void populateRegions(WorldGameState worldGameState) {
        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                for(int z = 0; z < getLength(); z++) {
                    setBlockRelative(x,y,z, new AirBlock(worldGameState, new Location(x,y,z)));
                }
            }
        }
    }

    public RegionBase(InputBitUtility inputBitUtility, WorldGameState worldGameState, UUID worldUUID) throws IOException {
        this.worldUUID = worldUUID;

        RegionFileVersion version = RegionFileVersion.values()[inputBitUtility.getNextCorrectIntByte()];
        RegionFileType type = RegionFileType.values()[inputBitUtility.getNextCorrectIntByte()];

        this.regionUUID = inputBitUtility.getNextUUID();

        int width = inputBitUtility.getNextCorrectIntByte() + 1;
        int height = inputBitUtility.getNextCorrectIntByte() + 1;
        int length = inputBitUtility.getNextCorrectIntByte() + 1;
        this.blocks = new BlockBase[height][width][length];

        int blockPaletteSize = inputBitUtility.getNextCorrectIntByte();
        BlockBase[] palette = new BlockBase[blockPaletteSize];
        for(int i = 0; i < blockPaletteSize; i++) {
            BlockType blockType = BlockType.values()[inputBitUtility.getNextCorrectIntByte()];
            palette[i] = blockType.getBlockFromInput(worldGameState, inputBitUtility);
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
                    getBlockRelative(x,y,z).setLocation(new Location(x,y,z));
                }
            }
        }

        int totalEntities = inputBitUtility.getNextCorrectIntByte();
        this.entities = new ArrayList<>(totalEntities);
        for(int i = 0; i < totalEntities; i++) {
            EntityType entityType = EntityType.values()[inputBitUtility.getNextCorrectIntByte()];
            EntityBase entity = entityType.getEntityFromInput(inputBitUtility, worldGameState, regionUUID, worldUUID);
            entities.add(entity);
        }



    }

    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(CURRENT_VERSION.ordinal());
        outputBitUtility.writeNextCorrectByteInt(RegionFileType.NORMAL.ordinal());
        outputBitUtility.writeNextUUID(regionUUID);

        outputBitUtility.writeNextCorrectByteInt(getWidth()-1);
        outputBitUtility.writeNextCorrectByteInt(getHeight()-1);
        outputBitUtility.writeNextCorrectByteInt(getLength()-1);
        // Count number of unique blocks, like ouch
        // Counts the total number of unique blocks, stores them block, count
        HashMap<BlockBase,Integer> countMap = new HashMap<>();
        int uniques = 0;
        for(int y = 0; y < getHeight(); y++) {
            for(int x = 0; x < getWidth(); x++) {
                for(int z = 0; z < getLength(); z++) {
                    BlockBase blockAtSpot = getBlockRelative(x,y,z);
                    if(!countMap.containsKey(blockAtSpot)) {
                        countMap.put(blockAtSpot,1);
                        uniques++;
                    } else {
                        countMap.put(blockAtSpot,countMap.get(blockAtSpot));
                    }
                }
            }
        }

        ArrayList<BlockBase> uniqueBlocks = new ArrayList<>();
        ArrayList<Integer> frequencies = new ArrayList<>();

        for(BlockBase block: countMap.keySet()) {
            uniqueBlocks.add(block);
            frequencies.add(countMap.get(block));
        }

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

        count = 0;
        for(BlockBase b: uniqueBlocks) {
            palette[count] = b;
            count++;
        }
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
            entityBase.writeData(outputBitUtility);
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


    public void setLocation(Location location) {
        Location oldLocation = getLocation();
        Location offset = oldLocation.getLocationDifference(location);
        for(EntityBase entity: entities) {
            entity.setLocation(entity.getLocation().getOffsetLocation(offset));
        }
        for(BlockBase block: getBlocksArray()) {
            block.setLocation(block.getLocation().getOffsetLocation(offset));
        }
        this.location = location;
    }


    // Helper Methods

    public BlockBase getBlockRelative(Location relativeLocation) {
        return getBlockRelative(relativeLocation.getX(), relativeLocation.getY(), relativeLocation.getZ());
    }

    public void setBlockRelative(Location relativeLocation, BlockBase block) {
        setBlockRelative(relativeLocation.getX(), relativeLocation.getY(), relativeLocation.getZ(), block);
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
                }
            }
        }
        return blockArray;
    }

    @Override
    public String toString() {
        return "R@" + regionUUID + "," + getLocation();
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

    public UUID getRegionUUID() {
        return regionUUID;
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }
}
