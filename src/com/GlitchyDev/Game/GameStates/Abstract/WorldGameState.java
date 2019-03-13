package com.GlitchyDev.Game.GameStates.Abstract;

import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.Region.RegionConnectionType;
import com.GlitchyDev.World.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public abstract class WorldGameState extends EnvironmentGameState {
    private HashMap<UUID, World> currentWorlds;

    public WorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
        currentWorlds = new HashMap<>();
    }


    public boolean hasWorld(UUID worldUUID) {
        return currentWorlds.containsKey(worldUUID);
    }

    protected World getWorld(UUID worldUUID) {
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
        for(EntityBase entity: getWorld(location.getWorldUUID()).getRegionAtLocation(location).getEntities()) {
            if(entity.getLocation().equals(location)) {
                return true;
            }
        }
        return false;
    }

    public EntityBase getEntityAtLocation(Location location) {
        for(EntityBase entity: getWorld(location.getWorldUUID()).getRegionAtLocation(location).getEntities()) {
            if(entity.getLocation().equals(location)) {
                return entity;
            }
        }
        return null;
    }

    public boolean isBlockAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).isARegionAtLocation(location);
    }

    public BlockBase getBlockAtLocation(Location location) {
        Location offset = getWorld(location.getWorldUUID()).getRegionAtLocation(location).getLocation().getLocationDifference(location);
        return getWorld(location.getWorldUUID()).getRegionAtLocation(location).getBlockRelative(offset);
    }

    public HashMap<UUID, HashMap<RegionConnectionType, ArrayList<UUID>>> getRegionConnections(UUID worldUUID) {
        return getWorld(worldUUID).getRegionConnections();
    }

    public void linkRegions(UUID worldUUID, UUID hostRegion, UUID connectedRegion, RegionConnectionType connectionType) {
        getWorld(worldUUID).linkRegion(hostRegion, connectedRegion, connectionType);
    }

    public void unlinkRegions(UUID worldUUID, UUID hostRegion, UUID connectedRegion, RegionConnectionType connectionType) {
        getWorld(worldUUID).unlinkRegion(hostRegion, connectedRegion, connectionType);
    }

    // Replicate functions

    public void addWorld(World world) {
        System.out.println("Added World " + world);
        currentWorlds.put(world.getWorldUUID(),world);
    }

    public void removeWorld(UUID worldUUID) {
        currentWorlds.remove(worldUUID);
    }


    public void addRegionToGame(Region region) {
        if(region == null) {
            System.out.println("R Oopsie!");
        }
        World world = getWorld(region.getWorldUUID());
        if(world == null) {
            System.out.println("W Oopsie! " + region.getWorldUUID() + " " + getWorlds().size());
        }
        world.getRegions().put(region.getRegionUUID(), region);
        for(EntityBase entity: region.getEntities()) {
            world.getEntities().put(entity.getUUID(), entity);
            entity.onSpawn(SpawnReason.REGION_MANUALLY_ADDED);
        }
        for(BlockBase block: region.getBlocksArray()) {
            if(block instanceof TickableBlock) {
                getWorld(region.getWorldUUID()).getTickableBlocks().put(block.getLocation(), (TickableBlock) block);
            }
        }
    }

    public void removeRegionFromGame(UUID regionUUID, UUID worldUUID) {
        World world = new World(worldUUID);
        Region region = getWorld(worldUUID).getRegions().get(regionUUID);
        for (EntityBase entity : region.getEntities()) {
            world.getEntities().remove(entity.getUUID());
        }
        for(BlockBase block: region.getBlocksArray()) {
            if(block instanceof TickableBlock) {
                getWorld(worldUUID).getTickableBlocks().remove(block.getLocation());
            }
        }
        getWorld(worldUUID).getRegions().remove(regionUUID);
    }

    // Replicate Functions

    public void spawnEntity(EntityBase entity, SpawnReason spawnReason) {
        World world = getWorld(entity.getLocation().getWorldUUID());
        world.getRegionAtLocation(entity.getLocation()).getEntities().add(entity);
        world.getEntities().put(entity.getUUID(),entity);
        entity.onSpawn(spawnReason);
    }

    public void despawnEntity(UUID entityUUID, UUID worldUUID, DespawnReason despawnReason) {
        EntityBase entity = getWorld(worldUUID).getEntities().get(entityUUID);
        Region hostRegion = getRegion(entity.getCurrentRegionUUID(), worldUUID);
        hostRegion.getEntities().remove(entity);
        getWorld(worldUUID).getRegionAtLocation(entity.getLocation()).getEntities().remove(entityUUID);
        getWorld(worldUUID).getEntities().remove(entityUUID);
        entity.onDespawn(despawnReason);

    }

    public EntityBase getEntity(UUID entityUUID, UUID worldUUID) {
        return getWorld(worldUUID).getEntities().get(entityUUID);
    }

    public void setBlocks(Collection<BlockBase> blocks) {
        for(BlockBase block: blocks) {
            setBlock(block);
        }
    }

    public void setBlock(BlockBase block) {
        World world = getWorld(block.getLocation().getWorldUUID());
        Region region = world.getRegionAtLocation(block.getLocation());
        Location relativeLocation = region.getLocation().getLocationDifference(block.getLocation());
        BlockBase previousBlock = region.getBlockRelative(relativeLocation);
        region.setBlockRelative(relativeLocation,block);
        if(block instanceof TickableBlock) {
            world.getTickableBlocks().put(block.getLocation(), (TickableBlock) block);
        }
        if(previousBlock instanceof TickableBlock) {
            world.getTickableBlocks().remove(previousBlock.getLocation());
        }
    }


}
