package com.GlitchyDev.GameStates.Abstract;

import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Enum.RegionConnection;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.World;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import sun.security.jca.GetInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public abstract class WorldGameState extends EnvironmentGameState {
    private final HashMap<UUID, World> currentWorlds;

    public WorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
        this.currentWorlds = new HashMap<>();
    }


    public boolean hasWorld(UUID worldUUID) {
        return currentWorlds.containsKey(worldUUID);
    }

    public World getWorld(UUID worldUUID) {
        return currentWorlds.get(worldUUID);
    }

    public Collection<UUID> getWorlds() {
        return currentWorlds.keySet();
    }

    public Region getRegion(UUID regionUUID, UUID worldUUID) {
        return getWorld(worldUUID).getRegions().get(regionUUID);
    }


    public boolean isARegionAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).isARegionAtLocation(location);
    }

    public int countRegionsAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).countRegionsAtLocation(location);
    }

    public ArrayList<Region> getRegionsAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).getRegionsAtLocation(location);
    }

    public Region getRegionAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).getRegionAtLocation(location);
    }

    public boolean isEntityAtLocation(Location location) {
        for(Region region: getRegionsAtLocation(location)) {
            for(Entity entity: region.getEntities()) {
                if(entity.getLocation().equals(location)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Entity getEntityAtLocation(Location location) {
        for(Region region: getRegionsAtLocation(location)) {
            for(Entity entity: region.getEntities()) {
                if(entity.getLocation().equals(location)) {
                    return entity;
                }
            }
        }
        return null;
    }


    public ArrayList<Entity> getEntitiesAtLocation(Location location) {
        ArrayList<Entity> entities = new ArrayList<>();
        for(Region region: getRegionsAtLocation(location)) {
            for(Entity entity: region.getEntities()) {
                if(entity.getLocation().equals(location)) {
                    entities.add(entity);
                }
            }
        }
        return entities;
    }

    public boolean isBlockAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).isARegionAtLocation(location);
    }

    public Block getBlockAtLocation(Location location) {
        return getBlockAtLocation(location,getRegionAtLocation(location).getRegionUUID());
    }

    public Block getBlockAtLocation(Location location, UUID regionUUID) {
        Location offset = getRegion(regionUUID,location.getWorldUUID()).getLocation().getLocationDifference(location);
        return getRegion(regionUUID,location.getWorldUUID()).getBlockRelative(offset);
    }

    public HashMap<UUID, HashMap<RegionConnection, ArrayList<UUID>>> getRegionConnections(UUID worldUUID) {
        return getWorld(worldUUID).getRegionConnections();
    }

    public void linkRegions(UUID worldUUID, UUID hostRegion, UUID connectedRegion, RegionConnection connectionType) {
        getWorld(worldUUID).linkRegion(hostRegion, connectedRegion, connectionType);
    }

    public void unlinkRegions(UUID worldUUID, UUID hostRegion, UUID connectedRegion, RegionConnection connectionType) {
        getWorld(worldUUID).unlinkRegion(hostRegion, connectedRegion, connectionType);
    }


    public void addWorld(World world) {
        System.out.println("Added World " + world);
        currentWorlds.put(world.getWorldUUID(), world);
    }

    public void removeWorld(UUID worldUUID) {
        currentWorlds.remove(worldUUID);
    }

    // Replicate functions


    public void setBlock(Block block) {
        UUID regionUUID = getRegionAtLocation(block.getLocation()).getRegionUUID();
        setBlock(block,regionUUID);
    }

    public void setBlock(Block block, UUID regionUUID) {
        Region region = getRegion(regionUUID,block.getLocation().getWorldUUID());
        Location difference = region.getLocation().getLocationDifference(block.getLocation());
        Block previousBlock = region.getBlockRelative(difference);
        if(previousBlock instanceof TickableBlock) {
            getWorld(block.getLocation().getWorldUUID()).getTickableBlocks().remove(previousBlock);
        }
        block.setRegionUUID(regionUUID);
        region.setBlockRelative(difference,block);
        if(block instanceof TickableBlock) {
            getWorld(block.getLocation().getWorldUUID()).getTickableBlocks().add((TickableBlock) block);
        }
    }

    public void addRegionToGame(Region region) {
        World world = getWorld(region.getWorldUUID());
        world.getRegions().put(region.getRegionUUID(), region);
        for (Entity entity : region.getEntities()) {
            world.addEntity(entity);
            entity.onSpawn(SpawnReason.REGION_MANUALLY_ADDED);
        }
        for (Block block : region.getBlocksArray()) {
            if (block instanceof TickableBlock) {
                getWorld(region.getWorldUUID()).getTickableBlocks().add((TickableBlock) block);
            }
        }
    }

    public void removeRegionFromGame(UUID regionUUID, UUID worldUUID) {
        World world = new World(worldUUID);
        Region region = getWorld(worldUUID).getRegions().get(regionUUID);
        for (Entity entity : region.getEntities()) {
            world.getEntities().remove(entity.getUUID());
        }
        for (Block block : region.getBlocksArray()) {
            if (block instanceof TickableBlock) {
                getWorld(worldUUID).getTickableBlocks().remove(block.getLocation());
            }
        }
        getWorld(worldUUID).getRegions().remove(regionUUID);
    }

    // Replicate Functions



    public void spawnEntity(Entity entity, SpawnReason spawnReason) {
        entity.onSpawn(spawnReason);
        getRegion(entity.getCurrentRegionUUID(), entity.getWorldUUID()).getEntities().add(entity);
        getWorld(entity.getLocation().getWorldUUID()).addEntity(entity);
    }


    public void despawnEntity(Entity entity, DespawnReason despawnReason) {
        despawnEntity(entity.getUUID(), entity.getWorldUUID(), despawnReason);
    }
    public void despawnEntity(UUID entityUUID, UUID worldUUID, DespawnReason despawnReason) {
        Entity entity = getWorld(worldUUID).getEntity(entityUUID);
        Region hostRegion = getRegion(entity.getCurrentRegionUUID(), worldUUID);
        hostRegion.getEntities().remove(entity);
        getWorld(worldUUID).removeEntity(entity);
        entity.onDespawn(despawnReason);
    }

    public Entity getEntity(UUID entityUUID, UUID worldUUID) {
        return getWorld(worldUUID).getEntity(entityUUID);
    }


    private boolean requiresRecache;
    private HashMap<UUID,HashMap<InstancedGridTexture,ArrayList<Matrix4f>>> cachedDesignerBlockModels;
    private HashMap<UUID,HashMap<InstancedGridTexture,ArrayList<Integer>>> cachedDesignerBlockTextures;
    private HashMap<UUID,ArrayList<Block>> cachedBlocks;
    public void renderWorld(World world, Camera camera) {

    }

    public void updateCache(UUID world) {
        requiresRecache = false;

        for(InstancedGridTexture texture: cachedDesignerBlockModels.get(world).keySet()) {
            cachedDesignerBlockModels.get(world).get(texture).clear();
            cachedDesignerBlockTextures.get(world).get(texture).clear();
            cachedBlocks.get(world).clear();
        }
        for(Region region: getWorld(world).getRegions().values()) {
            for(Block block: region.getBlocksArray()) {
                if(block instanceof DesignerBlock) {
                    InstancedGridTexture texture = ((DesignerBlock) block).getInstancedGridTexture();\
                    DesignerBlock designerBlock = (DesignerBlock) block;
                    if (!cachedDesignerBlockModels.get(world).containsKey(texture)) {
                        cachedDesignerBlockModels.get(world).put((texture, new ArrayList<>());
                        cachedDesignerBlockTextures.get(world).put(texture, new ArrayList<>());
                    }

                    cachedDesignerBlockModels.get(world).get(texture).add(renderer.getTransformation().buildModelMatrix(block.getLocation().getNormalizedPosition(),new Vector3f()));
                    cachedDesignerBlockTextures.get(world).get(texture).add(0);

                    for(Direction direction: Direction.values()) {
                        if (designerBlock.getFaceState(direction)) {
                            Rotatio
                            switch(direction) {
                                case ABOVE:
                                    cachedDesignerBlockModels
                                    break;
                                case BELOW:
                                case NORTH:
                                case EAST:
                                case SOUTH:
                                case WEST:
                            }
                        }
                    }



                    /*
                    case 0:
                            // 0
                            rotation.add(0,90,0);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 1:
                            // 1
                            rotation.add(180,90,0);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 2:
                            // 2*
                            rotation.add(90,-90,0);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 3:
                            // 3*
                            rotation.add(0, 0,90);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 4:
                            // 4*
                            rotation.add(90, -270,180);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 5:
                            // 5*
                            rotation.add(180, 0,270);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                     */


                } else {
                    cachedBlocks.get(world).add(block);
                }
            }
        }


    }









}
