package com.GlitchyDev.World.Lighting;

import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.LightableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;
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



    public void updateDynamicLighting(UUID world, ServerWorldGameState serverWorldGameState) {
        boolean requireUpdate = false;
        for(DynamicLightProducer dynamicLightProducer: dynamicLightProducers.get(world)) {
            if(dynamicLightProducer.needLightingUpdate()) {
                requireUpdate = true;
                break;
            }
        }

        if(requireUpdate) {
            for(LightableBlock lightableBlock: lightableBlocks.get(world).values()) {
                lightableBlock.resetDynamicLight();
            }


            // Light sources who intersect will share Light cache
            HashMap<Location, Integer> lightCache = new HashMap<>();
            int totalPolled = 0;
            for(DynamicLightProducer dynamicLightProducer: dynamicLightProducers.get(world)) {
                ArrayList<LightPropagationNode> lightToBePolled = new ArrayList<>();


                for (Direction direction : dynamicLightProducer.getDirectionsProduced()) {
                    lightToBePolled.add(new LightPropagationNode(dynamicLightProducer.getEmissionLocation(), direction, direction, dynamicLightProducer.getLightLevelProduced()));
                }

                while (lightToBePolled.size() != 0) {
                    totalPolled++;
                    LightPropagationNode lightPropagationNode = lightToBePolled.get(0);
                    lightToBePolled.remove(0);
                    Location location = lightPropagationNode.getLocation();
                    Direction emissionDirection = lightPropagationNode.getEmissionDirection();
                    Direction currentDirection = lightPropagationNode.getCurrentDirection();
                    int lightLevel = lightPropagationNode.getLightLevel();

                    Block block = serverWorldGameState.getBlockAtLocation(location);
                    if (lightableBlocks.get(world).containsKey(block.getLocation())) {
                        LightableBlock lightableBlock = lightableBlocks.get(world).get(block.getLocation());
                        lightableBlock.setDynamicLightLevel(currentDirection.reverse(), lightLevel);
                    } else {
                        for (Direction newDirection : Direction.getCompleteCardinal()) {
                            // We don't wanna go backwards in emission
                            if (newDirection != emissionDirection.reverse() && newDirection != currentDirection.reverse()) {
                                int newLightLevel = degradeLight(lightLevel);
                                if (newLightLevel > 0) {
                                    Location newLocation = location.getOffsetDirectionLocation(newDirection);
                                    if(serverWorldGameState.isBlockAtLocation(newLocation)) {
                                        Location blockLocation = serverWorldGameState.getBlockAtLocation(newLocation).getLocation();
                                        if (lightCache.containsKey(blockLocation)) {
                                            if (lightCache.get(blockLocation) < newLightLevel) {
                                                lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                            }
                                        } else {
                                            lightCache.put(blockLocation, newLightLevel);
                                            lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                for (LightableBlock lightableBlock : lightableBlocks.get(world).values()) {
                    lightableBlock.finalizeLight();
                }
            }
            //System.out.println("Total polled " + totalPolled);
        }
    }





    private int degradeLight(int initialLightLevel) {
        return Math.max(initialLightLevel - 1,0);
    }

    public static float getLightPercentage(int lightLevel) {
        return (float) Math.min(1.0f/16.0f * lightLevel,1.0);
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



    private class LightPropagationNode {
        private final Location location;
        private final Direction emissionDirection;
        private final Direction currentDirection;
        private final int lightLevel;

        public LightPropagationNode(Location location, Direction emissionDirection, Direction currentDirection, int lightLevel) {
            this.location = location;
            this.emissionDirection = emissionDirection;
            this.currentDirection = currentDirection;
            this.lightLevel = lightLevel;
        }


        public Location getLocation() {
            return location;
        }

        public Direction getEmissionDirection() {
            return emissionDirection;
        }

        public Direction getCurrentDirection() {
            return currentDirection;
        }

        public int getLightLevel() {
            return lightLevel;
        }

    }
}
