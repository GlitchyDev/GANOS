package com.GlitchyDev.World.Lighting;

import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.LightableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Lighting.Abstract.LightProducer;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;


// The Valid values of light is 0-255
public class LightingManager {
    private static final int MAX_LIGHT_LEVEL = 15;
    private final HashMap<UUID,ArrayList<LightProducer>> staticLightProducers;
    private final HashMap<UUID,ArrayList<LightProducer>> dynamicLightProducers;
    private final HashMap<UUID,HashMap<Location,LightableBlock>> lightableBlocks;

    public LightingManager() {
        staticLightProducers = new HashMap<>();
        dynamicLightProducers = new HashMap<>();
        lightableBlocks = new HashMap<>();
    }

    public boolean doRequireUpdate(UUID worldUUID) {
        for(LightProducer lightProducer: dynamicLightProducers.get(worldUUID)) {
            if(lightProducer.doNeedLightUpdate()) {
                return true;
            }
        }
        return false;
    }


    public void updateServerDynamicLighting(UUID world, ServerWorldGameState serverWorldGameState) {
        for(LightableBlock lightableBlock: lightableBlocks.get(world).values()) {
            lightableBlock.resetDynamicLight();
        }

        HashMap<Location, Integer> lightCache = new HashMap<>();
        for(LightProducer lightProducer: dynamicLightProducers.get(world)) {
            if(lightProducer instanceof RadiantLightProducer) {
                ArrayList<LightPropagationNode> lightToBePolled = new ArrayList<>();

                for (Direction direction : Direction.getCompleteCardinal()) {
                    if(serverWorldGameState.isARegionAtLocation(lightProducer.getEmissionLocation().getOffsetDirectionLocation(direction))) {
                        lightToBePolled.add(new LightPropagationNode(lightProducer.getEmissionLocation().getOffsetDirectionLocation(direction), direction, direction, ((RadiantLightProducer) lightProducer).getLightLevel()-1));
                    }
                }



                while (lightToBePolled.size() != 0) {
                    LightPropagationNode lightPropagationNode = lightToBePolled.get(0);
                    lightToBePolled.remove(0);
                    Location location = lightPropagationNode.getLocation();
                    Direction emissionDirection = lightPropagationNode.getEmissionDirection();
                    Direction currentDirection = lightPropagationNode.getCurrentDirection();
                    int lightLevel = lightPropagationNode.getLightLevel();

                    Block block = serverWorldGameState.getBlockAtLocation(location);
                    if (lightableBlocks.get(world).containsKey(block.getLocation())) {
                        LightableBlock lightableBlock = lightableBlocks.get(world).get(block.getLocation());
                        if(lightableBlock.getDynamicLightLevel(currentDirection) < lightLevel) {
                            lightableBlock.setDynamicLightLevel(currentDirection.reverse(), lightLevel);
                        }
                    } else {
                        for (Direction newDirection : Direction.getCompleteCardinal()) {
                            // We don't wanna go backwards in emission
                            if (newDirection != currentDirection.reverse()) {
                                int newLightLevel = degradeLight(lightLevel);
                                if (newLightLevel > 0) {
                                    Location newLocation = location.getOffsetDirectionLocation(newDirection);
                                    if (serverWorldGameState.isARegionAtLocation(newLocation)) {
                                        Location blockLocation = serverWorldGameState.getBlockAtLocation(newLocation).getLocation();
                                        if (lightCache.containsKey(blockLocation)) {
                                            if (lightCache.get(blockLocation) < newLightLevel) {
                                                lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                                lightCache.put(blockLocation,newLightLevel);
                                            } else {
                                                if(lightableBlocks.get(world).containsKey(blockLocation)) {
                                                    lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                                }
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
            } else {
                if(lightProducer instanceof SkyLightProducer) {
                    ArrayList<LightPropagationNode> lightToBePolled = new ArrayList<>();

                    int width = ((SkyLightProducer) lightProducer).getWidth();
                    int length = ((SkyLightProducer)lightProducer).getLength();

                    for(int x = 0; x < width; x++) {
                        for(int z = 0; z < length; z++) {
                            lightToBePolled.add(new LightPropagationNode(lightProducer.getEmissionLocation().getOffsetLocation(x,0,z), Direction.BELOW,Direction.BELOW,((SkyLightProducer) lightProducer).getSkyLightLevel()));
                        }
                    }



                    while (lightToBePolled.size() != 0) {
                        LightPropagationNode lightPropagationNode = lightToBePolled.get(0);
                        lightToBePolled.remove(0);
                        Location location = lightPropagationNode.getLocation();
                        Direction emissionDirection = lightPropagationNode.getEmissionDirection();
                        Direction currentDirection = lightPropagationNode.getCurrentDirection();
                        int lightLevel = lightPropagationNode.getLightLevel();

                        Block block = serverWorldGameState.getBlockAtLocation(location);
                        if (lightableBlocks.get(world).containsKey(block.getLocation())) {
                            LightableBlock lightableBlock = lightableBlocks.get(world).get(block.getLocation());
                            if (lightableBlock.getDynamicLightLevel(currentDirection) < lightLevel) {
                                lightableBlock.setDynamicLightLevel(currentDirection.reverse(), lightLevel);
                            }
                        } else {
                            for (Direction newDirection : Direction.getCompleteCardinal()) {
                                // We don't wanna go backwards in emission
                                if (newDirection != currentDirection.reverse() && newDirection != emissionDirection.reverse()) {
                                    int newLightLevel = degradeLight(lightLevel);
                                    if (newLightLevel > 0) {
                                        Location newLocation = location.getOffsetDirectionLocation(newDirection);
                                        if (serverWorldGameState.isARegionAtLocation(newLocation)) {
                                            Location blockLocation = serverWorldGameState.getBlockAtLocation(newLocation).getLocation();
                                            if (lightCache.containsKey(blockLocation)) {
                                                if (lightCache.get(blockLocation) < newLightLevel) {
                                                    lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                                    lightCache.put(blockLocation, newLightLevel);
                                                } else {
                                                    if (lightableBlocks.get(world).containsKey(blockLocation)) {
                                                        lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                                    }
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
                }
            }

            for (LightableBlock lightableBlock : lightableBlocks.get(world).values()) {
                lightableBlock.finalizeLight();
            }

            //System.out.println("Total polled " + totalPolled);
        }
    }


    public void initServerStaticLight(UUID world, ServerWorldGameState serverWorldGameState) {
        for(LightableBlock lightableBlock: lightableBlocks.get(world).values()) {
            lightableBlock.resetDynamicLight();
        }

        HashMap<Location, Integer> lightCache = new HashMap<>();
        for(LightProducer lightProducer: staticLightProducers.get(world)) {
            if(lightProducer instanceof RadiantLightProducer) {
                ArrayList<LightPropagationNode> lightToBePolled = new ArrayList<>();

                for (Direction direction : Direction.getCompleteCardinal()) {
                    if(serverWorldGameState.isARegionAtLocation(lightProducer.getEmissionLocation().getOffsetDirectionLocation(direction))) {
                        lightToBePolled.add(new LightPropagationNode(lightProducer.getEmissionLocation().getOffsetDirectionLocation(direction), direction, direction, ((RadiantLightProducer) lightProducer).getLightLevel()-1));
                    }
                }



                while (lightToBePolled.size() != 0) {
                    LightPropagationNode lightPropagationNode = lightToBePolled.get(0);
                    lightToBePolled.remove(0);
                    Location location = lightPropagationNode.getLocation();
                    Direction emissionDirection = lightPropagationNode.getEmissionDirection();
                    Direction currentDirection = lightPropagationNode.getCurrentDirection();
                    int lightLevel = lightPropagationNode.getLightLevel();

                    Block block = serverWorldGameState.getBlockAtLocation(location);
                    if (lightableBlocks.get(world).containsKey(block.getLocation())) {
                        LightableBlock lightableBlock = lightableBlocks.get(world).get(block.getLocation());
                        if(lightableBlock.getStaticLightLevel(currentDirection) < lightLevel) {
                            lightableBlock.setStaticLightLevel(currentDirection.reverse(), lightLevel);
                        }
                    } else {
                        for (Direction newDirection : Direction.getCompleteCardinal()) {
                            // We don't wanna go backwards in emission
                            if (newDirection != currentDirection.reverse()) {
                                int newLightLevel = degradeLight(lightLevel);
                                if (newLightLevel > 0) {
                                    Location newLocation = location.getOffsetDirectionLocation(newDirection);
                                    if (serverWorldGameState.isARegionAtLocation(newLocation)) {
                                        Location blockLocation = serverWorldGameState.getBlockAtLocation(newLocation).getLocation();
                                        if (lightCache.containsKey(blockLocation)) {
                                            if (lightCache.get(blockLocation) < newLightLevel) {
                                                lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                                lightCache.put(blockLocation,newLightLevel);
                                            } else {
                                                if(lightableBlocks.get(world).containsKey(blockLocation)) {
                                                    lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                                }
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
            } else {
                if(lightProducer instanceof SkyLightProducer) {
                    ArrayList<LightPropagationNode> lightToBePolled = new ArrayList<>();

                    int width = ((SkyLightProducer) lightProducer).getWidth();
                    int length = ((SkyLightProducer)lightProducer).getLength();

                    for(int x = 0; x < width; x++) {
                        for(int z = 0; z < length; z++) {
                            lightToBePolled.add(new LightPropagationNode(lightProducer.getEmissionLocation().getOffsetLocation(x,0,z), Direction.BELOW,Direction.BELOW,((SkyLightProducer) lightProducer).getSkyLightLevel()));
                        }
                    }



                    while (lightToBePolled.size() != 0) {
                        LightPropagationNode lightPropagationNode = lightToBePolled.get(0);
                        lightToBePolled.remove(0);
                        Location location = lightPropagationNode.getLocation();
                        Direction emissionDirection = lightPropagationNode.getEmissionDirection();
                        Direction currentDirection = lightPropagationNode.getCurrentDirection();
                        int lightLevel = lightPropagationNode.getLightLevel();

                        Block block = serverWorldGameState.getBlockAtLocation(location);
                        if (lightableBlocks.get(world).containsKey(block.getLocation())) {
                            LightableBlock lightableBlock = lightableBlocks.get(world).get(block.getLocation());
                            if (lightableBlock.getStaticLightLevel(currentDirection) < lightLevel) {
                                lightableBlock.setStaticLightLevel(currentDirection.reverse(), lightLevel);
                            }
                        } else {
                            for (Direction newDirection : Direction.getCompleteCardinal()) {
                                // We don't wanna go backwards in emission
                                if (newDirection != currentDirection.reverse() && newDirection != emissionDirection.reverse()) {
                                    int newLightLevel = degradeLight(lightLevel);
                                    if (newLightLevel > 0) {
                                        Location newLocation = location.getOffsetDirectionLocation(newDirection);
                                        if (serverWorldGameState.isARegionAtLocation(newLocation)) {
                                            Location blockLocation = serverWorldGameState.getBlockAtLocation(newLocation).getLocation();
                                            if (lightCache.containsKey(blockLocation)) {
                                                if (lightCache.get(blockLocation) < newLightLevel) {
                                                    lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                                    lightCache.put(blockLocation, newLightLevel);
                                                } else {
                                                    if (lightableBlocks.get(world).containsKey(blockLocation)) {
                                                        lightToBePolled.add(new LightPropagationNode(newLocation, emissionDirection, newDirection, newLightLevel));
                                                    }
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
                }
            }

            for (LightableBlock lightableBlock : lightableBlocks.get(world).values()) {
                lightableBlock.finalizeLight();
            }

            //System.out.println("Total polled " + totalPolled);
        }
    }






    private int degradeLight(int initialLightLevel) {
        return Math.max(initialLightLevel - 1,0);
    }

    public static float getLightPercentage(int lightLevel) {
        return (float) Math.min(1.0f / MAX_LIGHT_LEVEL * lightLevel, 1.0);

    }

    public void registerLightProducer(LightProducer lightProducer) {
        if(lightProducer.isDynamic()) {
            if(!dynamicLightProducers.containsKey(lightProducer.getWorldUUID())) {
                dynamicLightProducers.put(lightProducer.getWorldUUID(),new ArrayList<>());
            }
            dynamicLightProducers.get(lightProducer.getWorldUUID()).add(lightProducer);
        } else {
            if(!staticLightProducers.containsKey(lightProducer.getWorldUUID())) {
                staticLightProducers.put(lightProducer.getWorldUUID(),new ArrayList<>());
            }
            staticLightProducers.get(lightProducer.getWorldUUID()).add((lightProducer));
        }
    }

    public void deregisterLightProducer(LightProducer lightProducer) {
        if(lightProducer.isDynamic()) {
            dynamicLightProducers.get(lightProducer.getWorldUUID()).remove(lightProducer);
        } else {
            staticLightProducers.get(lightProducer.getWorldUUID()).remove(lightProducer);
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


    // Add a boolean called 1 time ignore, and allow for an emissions flow once???

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
