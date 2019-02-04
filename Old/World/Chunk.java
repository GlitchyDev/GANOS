package com.GlitchyDev.Old.World;

import com.GlitchyDev.Old.World.Blocks.Abstract.BlockBase;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;


/**
 * The default storage container of blocks, entities, ect, stored in world. A
 */
public class Chunk extends Region implements Serializable {
    private int chunkHeight;
    private final ChunkCord chunkCord;

    public Chunk(ChunkCord chunkCord)
    {
        super(World.STANDARD_CHUNK_SIDE_LENGTH,World.STANDARD_CHUNK_SIDE_LENGTH,World.STANDARD_CHUNK_HEIGHT);
        chunkHeight = World.STANDARD_CHUNK_HEIGHT;
        this.chunkCord = chunkCord;
    }

    public Chunk(ChunkCord chunkCord, int height)
    {
        super(World.STANDARD_CHUNK_SIDE_LENGTH,World.STANDARD_CHUNK_SIDE_LENGTH,height);
        chunkHeight = height;
        this.chunkCord = chunkCord;
    }

    /* LOAD FROM IO

        public Chunk(ChunkCord chunkCord, int height) {
             blocks = new BlockBase[height][World.STANDARD_CHUNK_SIDE_LENGTH][World.STANDARD_CHUNK_SIDE_LENGTH];
             this.chunkCord = chunkCord;
         }
     */

    @Override
    public BlockBase getBlock(int relativeX, int relativeY, int relativeZ)
    {
        if(relativeY >= chunkHeight)
        {
            chunkHeight = relativeY+1;
            BlockBase[][][] blocksAdjustedHeight = new BlockBase[chunkHeight][blocks[0].length][blocks[0][0].length];
            for(int index = 0; index < blocks.length; index++)
            {
                blocksAdjustedHeight[index] = blocks[index];
            }
            blocks = blocksAdjustedHeight;
        }
        if(relativeY < 0)
        {
            return null;
        }
        return blocks[relativeY][relativeX][relativeZ];
    }


    public ChunkCord getChunkCord() {
        return chunkCord;
    }


    /**
     * ADJUST NEW CHUNK HEIGHT
     * @param in
     * @throws IOException
     */

    @Override
    public void readObject(ObjectInputStream in, Location location) throws IOException{
        super.readObject(in,location);
        this.chunkHeight = blocks.length;
    }


}
