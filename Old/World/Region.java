package com.GlitchyDev.Old.World;


import com.GlitchyDev.Old.IO.AssetLoader;
import com.GlitchyDev.Old.World.Blocks.Abstract.BlockBase;
import com.GlitchyDev.Old.World.Blocks.Abstract.PartialCubicBlock;
import com.GlitchyDev.Old.World.Entities.EntityBase;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * An Rectangle of blocks and entities that can be loaded into the world map. Used to load/build structures
 */
public class Region {
    protected BlockBase[][][] blocks;
    protected ArrayList<EntityBase> entities;


    public Region(int width, int length, int height) {
        this.blocks = new BlockBase[height][width][length];
    }

    public Region(ObjectInputStream in, Location location) {
        try {
            readObject(in, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }









    public BlockBase getBlock(int relativeX, int relativeY, int relativeZ) {
        return blocks[relativeY][relativeX][relativeZ];
    }



    public void setBlock(int relativeX, int relativeY, int relativeZ, BlockBase block)
    {
        blocks[relativeY][relativeX][relativeZ] = block;
    }

    public BlockBase[][][] getBlocks() {
        return blocks;
    }

    public void setBlocks(BlockBase[][][] blocks) {
        this.blocks = blocks;
    }

    private byte setByteValue(byte b, int pos) {
        if(pos == 0) {
            return b |= 1 << 7;
        }
        return b |= 1 << (pos-1);
    }

    public void writeObject(ObjectOutputStream oos) throws IOException {
        String[] instanceTextureRegistryCache = new String[AssetLoader.getConfigOptionAsset("InstanceTextureRegistry").keySet().size()];
        int index = 0;
        for(String string: AssetLoader.getConfigOptionAsset("InstanceTextureRegistry").keySet()) {
            instanceTextureRegistryCache[index] = string;
            index++;
        }


        // Write Chunk Information here NOW Y X Z
        oos.writeInt(blocks.length);
        oos.writeInt(blocks[0].length);
        oos.writeInt(blocks[0][0].length);

        BlockBase[] totalBlocks = new BlockBase[blocks.length * blocks[0].length * blocks[0][0].length];
        int count = 0;
        for(int y = 0; y < blocks.length; y++) {
            for(int x = 0; x < blocks[0].length; x++) {
                for(int z = 0; z < blocks[0][0].length; z++) {
                    totalBlocks[count] = blocks[y][x][z];
                    count++;
                }
            }
        }

        // Blank blocks that we have already marked in a BlankByte
        boolean[] skipBlocks = new boolean[totalBlocks.length];

        for(int i = 0; i < totalBlocks.length; i++) {
            if(!skipBlocks[i]) {
                BlockBase blockBase = totalBlocks[i];
                if (blockBase == null) {
                    byte blankByte = (byte) 0b00000000;
                    // First byte starts a blank block sequence and includes the current block, each 1 represents a nonBlank Block, each 0 represents a blank block with its position relative to the current one
                    // Essentially, we can skip and write those areas as "Already Blank", and the next bytes X ( X being the number of 1's ) will represent the 1's in this sequence, and their dats
                    // TLDR we get to compress 1-8 blank blocks into 1 byte if we can, if not we have to use at least 1 byte to represent a block anyway d_d


                    // The previews, we already set byte pos 0 as that is the current block
                    for (int a = 1; a < 8; a++) {
                        if (i + a < totalBlocks.length) {
                            BlockBase preview = totalBlocks[i + a];
                            if (preview != null) {
                                blankByte = setByteValue(blankByte,a);
                            } else {
                                skipBlocks[i + a] = true;
                            }
                        }
                    }

                    // Write Byte
                    oos.write(blankByte);

                    //System.out.print("Block " + i + " ");
                    for (int a = 0; a < 8; a++) {
                        //System.out.print((blankByte >> (a - 1)) & 1);
                    }
                    //System.out.println();

                } else {
                    if (blockBase instanceof PartialCubicBlock) {
                        // Byte 1 represents a valid block
                        byte partialCubicBlockByte = 0;
                        partialCubicBlockByte = setByteValue(partialCubicBlockByte,0);
                        partialCubicBlockByte = setByteValue(partialCubicBlockByte,1);


                        // 7th bit is first?? 0th bit second

                        // Pack remaining 6 orientation states as bits into our byte
                        int directionCount = 0;
                        for(Direction direction: Direction.values()) {
                            if(((PartialCubicBlock) blockBase).getDirectionFaceState(direction)) {
                                partialCubicBlockByte = setByteValue(partialCubicBlockByte,2 + directionCount);
                            }
                            directionCount++;
                        }

                        oos.write(partialCubicBlockByte);


                        int id = 0;
                        for(String textureName: instanceTextureRegistryCache) {
                            if(textureName.equals(((PartialCubicBlock) blockBase).getInstancedGridTexture().getName())) {
                                break;
                            }
                            id++;
                        }
                        //int correction = (z<0) ? (127 + (129 + z)) : z;
                        oos.writeByte(id);


                        // Replace with byte texture ID later
                        //String textureName = ((PartialCubicBlock) blockBase).getInstancedGridTexture().getName();
                        //oos.writeUTF(textureName);



                        for(Direction direction: Direction.values()) {
                            if(((PartialCubicBlock) blockBase).getDirectionFaceState(direction)) {
                                byte textureID = (byte) (-128 + ((PartialCubicBlock) blockBase).getDirectionTexture(direction));
                                oos.writeByte(textureID);
                            }
                        }

                        // Add code for modifier


                        //System.out.print("Block B " + i + " ");
                        for (int a = 0; a < 8; a++) {
                            //System.out.print((partialCubicBlockByte >> (a - 1)) & 1);
                        }
                        //System.out.println();
                    } else {
                        // Non PartialCubicBlock Support here in future
                        //System.out.println("ERROR! NO BLOCKTYPE HANDLER IO");

                    }
                }
            }
        }


        // Future you may have to write Entity Information as well


    }

    /**
     * ADJUST NEW CHUNK HEIGHT
     * @param in
     * @throws IOException
     */

    public void readObject(ObjectInputStream in, Location location) throws IOException{

        String[] instanceTextureRegistryCache = new String[AssetLoader.getConfigOptionAsset("InstanceTextureRegistry").keySet().size()];
        int index = 0;
        for(String string: AssetLoader.getConfigOptionAsset("InstanceTextureRegistry").keySet()) {
            instanceTextureRegistryCache[index] = string;
            index++;
        }

        int chunkHeight = in.readInt();
        int chunkWidth = in.readInt();
        int chunkLength = in.readInt();

        // System.out.println("Chunk Dim " + chunkHeight + " " + chunkWidth + " " + chunkLength);

        BlockBase[][][] loadedBlocks = new BlockBase[chunkHeight][chunkWidth][chunkLength];
        BlockBase[] totalBlocks = new BlockBase[chunkHeight * chunkWidth * chunkLength];

        boolean[] alreadyCheckedBlocks = new boolean[chunkHeight * chunkWidth * chunkLength];
        for(int i = 0; i < totalBlocks.length; i++) {
            // See if block is marked as null
            if(!alreadyCheckedBlocks[i]) {
                byte initByte = in.readByte();
                //System.out.print("Block " + i + " ");
                for (int a = 0; a < 8; a++) {
                    // System.out.print((initByte >> (a - 1)) & 1);
                }
                //System.out.println();
                boolean firstBit = ((initByte >> (0 - 1)) & 1) == 1;
                if (firstBit) {
                    // Block is Partial Cubic Block
                    PartialCubicBlock block = new PartialCubicBlock(new Location((World) null));
                    int directionCount = 0;
                    int totalValid = 0;
                    for (Direction direction : Direction.values()) {
                        if (((initByte >> ((directionCount + 2) - 1)) & 1) == 1) {
                            block.setDirectionFaceState(direction, true);
                            totalValid++;
                        }
                        else {
                            block.setDirectionFaceState(direction, false);
                        }
                        directionCount++;
                    }
                    // First Byte Complete
                    byte textureId = in.readByte();
                    int correctedId = (textureId<0) ? (127 + (129 + textureId)) : textureId;


                    String textureName = instanceTextureRegistryCache[correctedId];
                    block.setInstancedGridTexture(AssetLoader.getInstanceGridTexture(textureName));
                    // Load Texture data for sides
                    for (int a = 0; a < totalValid; a++) {
                        byte id = in.readByte();
                        int texID = id - (-128);
                        int intenalCount = 0;
                        for (Direction direction : Direction.values()) {
                            if (block.getDirectionFaceState(direction)) {
                                if (intenalCount == a) {
                                    block.setDirectionTexture(direction, texID);
                                }
                                intenalCount++;
                            }
                        }
                    }
                    totalBlocks[i] = block;
                } else {
                    // We know this is an air block
                    for(int b = 1; b < 8; b++) {
                        if(((initByte >> (b - 1)) & 1) == 0) {
                            if(i + b < alreadyCheckedBlocks.length) {
                                alreadyCheckedBlocks[i + b] = true;
                            }
                        }
                    }
                }
            }
        }


        int count = 0;
        for(int y = 0; y < loadedBlocks.length; y++) {
            for(int x = 0; x < loadedBlocks[0].length; x++) {
                for(int z = 0; z < loadedBlocks[0][0].length; z++) {
                    loadedBlocks[y][x][z] = totalBlocks[count];
                    if(totalBlocks[count] != null) {
                        totalBlocks[count].setLocation(location.getOffsetLocation(x, y, z));
                    }
                    count++;
                }
            }
        }
        setBlocks(loadedBlocks);


    }

}
