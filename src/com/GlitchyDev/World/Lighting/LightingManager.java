package com.GlitchyDev.World.Lighting;

import com.GlitchyDev.World.Blocks.AbstractBlocks.LightableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

//
public class LightingManager {
    private final HashMap<UUID,ArrayList<LightProducer>> lightProducers;
    private final HashMap<UUID,HashMap<Location,LightableBlock>> lightableBlocks;

    public LightingManager() {
        lightProducers = new HashMap<>();
        lightableBlocks = new HashMap<>();
    }

    public void updateLighting(UUID world) {
        boolean updateAll = false;
        for(LightProducer lightProducer: lightProducers.get(world)) {
            if(lightProducer.needsUpdate()) {
                updateAll = true;
                break;
            }
        }
        if(updateAll) {
            for (LightableBlock lightableBlock : lightableBlocks.get(world).values()) {
                lightableBlock.resetLight();
            }

            for (LightProducer lightProducer : lightProducers.get(world)) {
                for (Direction lightSpreadDirections : lightProducer.getDirectionsProduced()) {
                    int lightLevel = lightProducer.getLightLevelProduced(lightSpreadDirections);
                    final Location emissionLocation = lightProducer.getEmissionLocation();
                    spreadLight(world,lightSpreadDirections,emissionLocation,lightLevel);
                }
            }
        }
    }

    private void spreadLight(UUID world, Direction lightTravelDirection, Location currentLocation, int currentLightLevel) {
        for(Direction cardinalDirections: Direction.getCompleteCardinal()) {
            if(cardinalDirections.reverse() != lightTravelDirection) {

                Location directionLocation = currentLocation.getOffsetDirectionLocation(cardinalDirections);
                if(lightableBlocks.get(world).containsKey(directionLocation)) {
                    LightableBlock lightableBlock = lightableBlocks.get(world).get(directionLocation);
                    if(checkLightLevel(lightableBlock,cardinalDirections,currentLightLevel)) {
                        lightableBlock.setBlockLight(cardinalDirections,currentLightLevel);
                    }
                }
                
                int newLightLevel = degradeLight(currentLightLevel);
                if(newLightLevel > 0) {
                    spreadLight(world,cardinalDirections,directionLocation,newLightLevel);
                }
            }
        }
    }

    private boolean checkLightLevel(LightableBlock lightableBlock, Direction direction, int light) {
        return lightableBlock.getLightLevel(direction) < light;
    }

    private int degradeLight(int initialLightLevel) {
        return initialLightLevel - 1;
    }

    public static float getLightPercentage(int lightLevel) {
        return 1.0f/15 * lightLevel;
    }

    public void registerBlock(LightableBlock lightableBlock) {
        lightableBlocks.get(lightableBlock.getLocation().getWorldUUID()).put(lightableBlock.getLocation(),lightableBlock);
    }

    public void deregisterBlock(LightableBlock lightableBlock) {
        lightableBlocks.get(lightableBlock.getLocation().getWorldUUID()).remove(lightableBlock.getLocation());
    }


}
