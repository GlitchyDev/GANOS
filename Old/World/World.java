package com.GlitchyDev.Old.World;

import com.GlitchyDev.Old.World.Blocks.Abstract.BlockBase;
import com.GlitchyDev.Old.World.Blocks.Abstract.TickableBlock;
import com.GlitchyDev.Old.World.Entities.EntityBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;


/**
 * A dimension that can be observed, made up of Chunks, Entities, ect. Multiple worlds can exist at once, and Entities can move and interact between them
 * A World can tick all active structures within it
 */
public class World {
    private final String worldName;
    private HashMap<String,Chunk> chunks;
    private ArrayList<TickableBlock> tickableBlocks;
    private ArrayList<EntityBase> entities;


    public static final int STANDARD_CHUNK_SIDE_LENGTH = 10;
    public static final int STANDARD_CHUNK_HEIGHT = 5;
    // Entities

    public World(String worldName) {
        this.worldName = worldName;
        chunks = new HashMap<>();
        tickableBlocks = new ArrayList<>();
        entities = new ArrayList<>();
    }

    public String getWorldName() {
        return worldName;
    }

    public boolean doesChunkExist(ChunkCord chunkCord)
    {
        return chunks.containsKey(chunkCord.toString());
    }

    public Chunk getChunk(ChunkCord chunkCord) {
        if(!chunks.containsKey(chunkCord.toString())) {
            chunks.put(chunkCord.toString(),new Chunk(chunkCord));
        }

        return chunks.get(chunkCord.toString());
    }


    public void setBlock(Location location, BlockBase block) {
        BlockBase previousBlock = getBlock(location);
        if(previousBlock instanceof TickableBlock) {
            tickableBlocks.remove(previousBlock);
        }

        if(location.getY() >= 0) {
            int chunkX = getChunkNumfromCordNum(location.getX());
            int chunkZ = getChunkNumfromCordNum(location.getZ());
            ChunkCord chunkCord = new ChunkCord(chunkX,chunkZ);
            if (!doesChunkExist(chunkCord)) {
                chunks.put(chunkCord.toString(),new Chunk(chunkCord));
            }
            getChunk(chunkCord).setBlock(Math.abs(location.getX() - getPosNumFromChunkNum(chunkX)), location.getY(), Math.abs(location.getZ() - getPosNumFromChunkNum(chunkZ)), block);
            if(block != null) {
                block.setLocation(location);

                if(block instanceof TickableBlock) {
                    tickableBlocks.add((TickableBlock) block);
                }
            }
        } else {
            System.out.println("World: Block place error of " + block.getBlockType() + " @ " + location);
        }
    }

    public BlockBase getBlock(Location location) {

        int chunkX = getChunkNumfromCordNum(location.getX());
        int chunkZ = getChunkNumfromCordNum(location.getZ());
        ChunkCord chunkCord = new ChunkCord(chunkX,chunkZ);
        if(!doesChunkExist(chunkCord)) {
            chunks.put(chunkCord.toString(),new Chunk(chunkCord));
        }
        return getChunk(chunkCord).getBlock(Math.abs(location.getX() - getPosNumFromChunkNum(chunkX)), location.getY(), Math.abs(location.getZ() - getPosNumFromChunkNum(chunkZ)));
    }

    public void addRegion(Region region, Location location, Direction direction) {
        BlockBase[][][] blocks = region.getBlocks();
        switch(direction) {
            case NORTH:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            setBlock(location.getOffsetLocation(x,y,z),blocks[y][x][z]);
                            if(getBlock(location.getOffsetLocation(x,y,z)) != null) {
                                getBlock(location.getOffsetLocation(x,y,z)).rotate(direction);
                            }

                        }
                    }
                }
                break;
            case EAST:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            setBlock(location.getOffsetLocation(z,y,-x),blocks[y][x][z]);
                            if(getBlock(location.getOffsetLocation(z,y,-x)) != null) {
                                getBlock(location.getOffsetLocation(z,y,-x)).rotate(direction);
                            }
                        }
                    }
                }
                break;
            case SOUTH:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            setBlock(location.getOffsetLocation(-x,y,-z),blocks[y][x][z]);
                            if(getBlock(location.getOffsetLocation(-x,y,-z)) != null) {
                                getBlock(location.getOffsetLocation(-x,y,-z)).rotate(direction);
                            }
                        }
                    }
                }
                break;
            case WEST:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            setBlock(location.getOffsetLocation(-z,y,-x),blocks[y][x][z]);
                            if(getBlock(location.getOffsetLocation(-z,y,-x)) != null) {
                                getBlock(location.getOffsetLocation(-z,y,-x)).rotate(direction);
                            }                        }
                    }
                }
                break;
                /*
            case ABOVE:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            setBlock(location.getOffsetLocation(x,z,-y),blocks[y][x][z]);
                        }
                    }
                }
                break;
            case BELOW:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            setBlock(location.getOffsetLocation(x,-z,y),blocks[y][x][z]);
                        }
                    }
                }
                break;
                */
        }
    }

    /**
     * Doesn't properly rotate Blocks
     * @param location
     * @param direction
     * @param width
     * @param length
     * @param height
     * @return
     */
    public Region createRegion(Location location, Direction direction, int width, int length, int height) {
        BlockBase[][][] blocks = new BlockBase[height][width][length];

        switch(direction) {
            case NORTH:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            blocks[y][x][z] = getBlock(location.getOffsetLocation(x,y,z));
                        }
                    }
                }
                break;
            case EAST:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            blocks[y][x][z] = getBlock(location.getOffsetLocation(z,y,-x));
                        }
                    }
                }
                break;
            case SOUTH:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            blocks[y][x][z] = getBlock(location.getOffsetLocation(-x,y,-z));
                        }
                    }
                }
                break;
            case WEST:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            blocks[y][x][z] = getBlock(location.getOffsetLocation(-z,y,-x));
                        }
                    }
                }
                break;
            default:
                for(int y = 0; y < blocks.length; y++) {
                    for (int x = 0; x < blocks[0].length; x++) {
                        for (int z = 0; z < blocks[0][0].length; z++) {
                            blocks[y][x][z] = getBlock(location.getOffsetLocation(x,y,z));
                        }
                    }
                }
        }

        return new Region(blocks);
    }

    private int getChunkNumfromCordNum(int z) {
        boolean isNeg = z < 0;
        if((z / World.STANDARD_CHUNK_SIDE_LENGTH == 0)) {
            return (isNeg ? -1 : 0);
        }
        else {
            return (isNeg ? z / World.STANDARD_CHUNK_SIDE_LENGTH + -1 : z / World.STANDARD_CHUNK_SIDE_LENGTH);
        }

    }

    public int getPosNumFromChunkNum(int z) {
        return (z==0 ? 0 : (z >= 0) ? z * World.STANDARD_CHUNK_SIDE_LENGTH : z * World.STANDARD_CHUNK_SIDE_LENGTH + 1);
    }

    public Collection<Chunk> getChunks() {
        return chunks.values();
    }
}
