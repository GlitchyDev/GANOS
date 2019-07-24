package com.GlitchyDev.World.Lighting;

import com.GlitchyDev.Game.Player;
import com.GlitchyDev.World.Blocks.AbstractBlocks.LightableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;


// The Valid values of light is 0-255
public class LightingManager {
    private final HashMap<UUID,ArrayList<StaticLightProducer>> staticLightProducers;
    private final HashMap<UUID,ArrayList<DynamicLightProducer>> dynamicLightProducers;
    private final HashMap<UUID,HashMap<Location,LightableBlock>> lightableBlocks;

    public LightingManager() {
        staticLightProducers = new HashMap<>();
        dynamicLightProducers = new HashMap<>();
        lightableBlocks = new HashMap<>();
    }

    public void updateLighting(UUID world, Collection<Player> players) {
        boolean preformUpdate = false;
        for(DynamicLightProducer dynamicLightProducer: dynamicLightProducers.get(world)) {
            if(dynamicLightProducer.needLightingUpdate()) {
                preformUpdate = true;
                break;
            }
        }

        if(preformUpdate) {
            for(Player player: players) {
                preformPlayerLightingUpdate(player, world);
            }
            preformWorldLightingUpdate(world);
        }
    }



    //*****

    public void preformInitWorldLightingUpdate(UUID world) {
        doStaticLightUpdate(world);
        doDynamicLightUpdate(world);
        finalizeLighting(world);
    }

    public void preformWorldLightingUpdate(UUID world) {
        for(LightableBlock lightableBlock: lightableBlocks.get(world).values()) {
            lightableBlock.resetDynamicLight();
        }
        doDynamicLightUpdate(world);
        finalizeLighting(world);
    }

    public void preformPlayerLightingUpdate(Player player, UUID world) {
        // TODO CODE THIS :_:
    }

    private void doStaticLightUpdate(UUID world) {
        if(staticLightProducers.containsKey(world)) {
            for (LightableBlock lightableBlock : lightableBlocks.get(world).values()) {
                lightableBlock.resetDynamicLight();
            }
        }
    }
    private void doDynamicLightUpdate(UUID world) {
        if(dynamicLightProducers.containsKey(world)) {
            for (DynamicLightProducer dynamicLightProducer : dynamicLightProducers.get(world)) {
                for (Direction direction : dynamicLightProducer.getDirectionsProduced()) {
                    spreadDynamicLight(world, dynamicLightProducer.getEmissionLocation(), direction, dynamicLightProducer.getLightLevelProduced(direction));
                }
            }
        }
    }
    private void finalizeLighting(UUID world) {
        for(LightableBlock lightableBlock: lightableBlocks.get(world).values()) {
            lightableBlock.finalizeLight();
        }
    }




    public void spreadStaticLight(UUID world, Location currentLocation, Direction currentDirection, int currentLightLevel, boolean doUseNoVerticalDegradation) {
        for(Direction direction: Direction.getCompleteCardinal()) {
            if(currentDirection.reverse() != direction) {
                Location currentSpreadLocation = currentLocation.getOffsetDirectionLocation(direction);
                if(lightableBlocks.get(world).containsKey(currentSpreadLocation)) {
                    LightableBlock lightableBlock = lightableBlocks.get(world).get(currentSpreadLocation);
                    if(lightableBlock.getDynamicLightLevel(direction.reverse()) < currentLightLevel) {
                        lightableBlock.setDynamicLightLevel(direction.reverse(),currentLightLevel);
                    }
                } else {
                    if(degradeLight(currentLightLevel) > 0) {
                        if (direction == Direction.BELOW && doUseNoVerticalDegradation && currentSpreadLocation.getY() > 0) {
                            spreadDynamicLight(world, currentSpreadLocation, direction, currentLightLevel);
                        } else {
                            spreadDynamicLight(world, currentSpreadLocation, direction, degradeLight(currentLightLevel));
                        }
                    }
                }
            }
        }
    }

    public void spreadDynamicLight(UUID world, Location currentLocation, Direction currentDirection, int currentLightLevel) {
        for(Direction direction: Direction.getCompleteCardinal()) {
            if(currentDirection.reverse() != direction) {
                Location currentSpreadLocation = currentLocation.getOffsetDirectionLocation(direction);
                if(lightableBlocks.get(world).containsKey(currentSpreadLocation)) {
                    LightableBlock lightableBlock = lightableBlocks.get(world).get(currentSpreadLocation);
                    if(lightableBlock.getDynamicLightLevel(direction.reverse()) < currentLightLevel) {
                        lightableBlock.setDynamicLightLevel(direction.reverse(),currentLightLevel);
                    }
                } else {
                    if(degradeLight(currentLightLevel) > 0) {
                        spreadDynamicLight(world, currentSpreadLocation, direction, degradeLight(currentLightLevel));
                    }
                }
            }
        }
    }




    private int degradeLight(int initialLightLevel) {
        return Math.max(initialLightLevel - 1,0);
    }

    public static float getLightPercentage(int lightLevel) {
        return 1.0f/2.0f * lightLevel;
    }

    public void registerLightProducer(LightProducer lightProducer) {
        if(lightProducer instanceof DynamicLightProducer) {
            if(!dynamicLightProducers.containsKey(lightProducer.getWorldUUID())) {
                dynamicLightProducers.put(lightProducer.getWorldUUID(),new ArrayList<>());
            }
            dynamicLightProducers.get(lightProducer.getWorldUUID()).add((DynamicLightProducer) lightProducer);
        } else {
            if(lightProducer instanceof  StaticLightProducer) {
                if(!staticLightProducers.containsKey(lightProducer.getWorldUUID())) {
                    staticLightProducers.put(lightProducer.getWorldUUID(),new ArrayList<>());
                }
                staticLightProducers.get(lightProducer.getWorldUUID()).add((StaticLightProducer) lightProducer);
            } else {
                System.out.println("LightManager: Error! Added Lightproducer " + lightProducer + " is not an instance of Dynamic nor Static Light producer");
            }
        }
    }

    public void deregisterLightProducer(LightProducer lightProducer) {
        if(lightProducer instanceof DynamicLightProducer) {
            dynamicLightProducers.get(lightProducer.getWorldUUID()).remove(lightProducer);
        } else {
            if(lightProducer instanceof  StaticLightProducer) {
                staticLightProducers.get(lightProducer.getWorldUUID()).remove(lightProducer);
            } else {
                System.out.println("LightManager: Error! Removed Lightproducer " + lightProducer + " is not an instance of Dynamic nor Static Light producer");
                System.out.println("LightManager: Seriously questioning how and why you tried removing something that couldn't have even been added");

            }
        }
    }

    public void registerBlock(LightableBlock lightableBlock) {
        if(!lightableBlocks.containsKey(lightableBlock.getLocation().getWorldUUID())) {
            lightableBlocks.put(lightableBlock.getLocation().getWorldUUID(),new HashMap<>());
        }
        lightableBlocks.get(lightableBlock.getLocation().getWorldUUID()).put(lightableBlock.getLocation(),lightableBlock);
    }

    public void deregisterBlock(LightableBlock lightableBlock) {
        lightableBlocks.get(lightableBlock.getLocation().getWorldUUID()).remove(lightableBlock.getLocation());
    }


}
