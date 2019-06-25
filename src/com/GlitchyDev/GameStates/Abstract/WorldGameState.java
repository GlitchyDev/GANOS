package com.GlitchyDev.GameStates.Abstract;

import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.Region.Enum.RegionConnection;
import com.GlitchyDev.World.World;

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





}
