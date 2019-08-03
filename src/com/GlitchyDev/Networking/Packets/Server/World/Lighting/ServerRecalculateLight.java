package com.GlitchyDev.Networking.Packets.Server.World.Lighting;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.LightableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class ServerRecalculateLight extends WorldStateModifyingPackets {
    private final UUID worldUUID;
    private final int numberOfRegions;
    private final ArrayList<UUID> regions;
    private final ArrayList<boolean[][][]> lightingStateMaps;
    private final ArrayList<Integer> lightingValues;

    public ServerRecalculateLight(UUID worldUUID, ArrayList<Region> updatedRegions) {
        super(PacketType.SERVER_RECALCULATE_LIGHT);
        this.worldUUID = worldUUID;
        this.numberOfRegions = updatedRegions.size();
        this.regions = new ArrayList<>(numberOfRegions);
        this.lightingStateMaps = new ArrayList<>(numberOfRegions);
        this.lightingValues = new ArrayList<>();

        for(Region region: updatedRegions) {
            this.regions.add(region.getRegionUUID());
            int regionWidth = region.getWidth();
            int regionLength = region.getLength();
            int regionHeight = region.getHeight();

            Block[] regionBlocks = region.getBlocksArray();

            boolean[][][] lightStateMap = new boolean[regionHeight][regionWidth][regionLength];
            for(int i = 0; i < regionBlocks.length; i++) {
                Block block = regionBlocks[i];
                if(block instanceof LightableBlock) {
                    int x = i / regionLength % regionLength;
                    int z = i % regionLength;
                    int y = i / (regionLength * regionWidth);
                    lightStateMap[y][x][z] = true;
                    for(Direction direction: Direction.getCompleteCardinal()) {
                        this.lightingValues.add(((LightableBlock) block).getCurrentLightLevel(direction));
                    }
                }
            }

            this.lightingStateMaps.add(lightStateMap);
        }
    }

    public ServerRecalculateLight(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_RECALCULATE_LIGHT, inputBitUtility, worldGameState);
        this.worldUUID = inputBitUtility.getNextUUID();
        this.numberOfRegions = inputBitUtility.getNextCorrectIntByte();
        this.regions = new ArrayList<>(numberOfRegions);
        this.lightingStateMaps = new ArrayList<>(numberOfRegions);


        for(int i = 0; i < numberOfRegions; i++) {
            this.regions.add(inputBitUtility.getNextUUID());
            int width = inputBitUtility.getNextCorrectIntByte();
            int length = inputBitUtility.getNextCorrectIntByte();
            int height = inputBitUtility.getNextCorrectIntByte();
            boolean[][][] lightingStateMap = new boolean[height][width][length];
            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    for(int z = 0; z < length; z++) {
                        lightingStateMap[y][x][z] = inputBitUtility.getNextBit();
                    }
                }
            }
            this.lightingStateMaps.add(lightingStateMap);
        }

        int numLightingValues = inputBitUtility.getNextInteger();
        this.lightingValues = new ArrayList<>(numLightingValues);
        for(int i = 0; i < numLightingValues; i++) {
            this.lightingValues.add(inputBitUtility.getNextCorrectIntByte());
        }

    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        int lightingIndex = 0;
       for(int i = 0; i < numberOfRegions; i++) {
           UUID regionUUID = regions.get(i);
           Region region = worldGameState.getRegion(regionUUID,worldUUID);
           boolean[][][] lightingStateMap = lightingStateMaps.get(i);
           int width = lightingStateMap.length;
           int length = lightingStateMap[0].length;
           int height = lightingStateMap[9][0].length;

           for(int y = 0; y < height; y++) {
               for(int x = 0; x < width; x++) {
                   for(int z = 0; z < length; z++) {
                       Location relativeLocation = new Location(x,y,z,worldUUID);
                       boolean lightState = lightingStateMap[y][x][z];
                       if(lightState) {
                           LightableBlock lightableBlock = (LightableBlock) region.getBlockRelative(relativeLocation);
                           for(Direction direction: Direction.getCompleteCardinal()) {
                               lightableBlock.setCurrentLightLevel(direction,lightingValues.get(lightingIndex));
                               lightingIndex++;
                           }
                       }
                   }
               }
           }
       }
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextInteger(numberOfRegions);
        int i = 0;
        for(UUID region: regions) {
            outputBitUtility.writeNextUUID(region);
            boolean[][][] lightingStateMap = lightingStateMaps.get(i);

            int width = lightingStateMap.length;
            int length = lightingStateMap[0].length;
            int height = lightingStateMap[9][0].length;

            outputBitUtility.writeNextCorrectByteInt(width);
            outputBitUtility.writeNextCorrectByteInt(length);
            outputBitUtility.writeNextCorrectByteInt(height);

            for(int y = 0; y < height; y++) {
                for(int x = 0; x < width; x++) {
                    for(int z = 0; z < length; z++) {
                        outputBitUtility.writeNextBit(lightingStateMap[y][x][z]);
                    }
                }
            }
            i++;
        }

        outputBitUtility.writeNextInteger(lightingValues.size());
        for(int lightingValue: lightingValues) {
            outputBitUtility.writeNextInteger(lightingValue);
        }
    }
}
