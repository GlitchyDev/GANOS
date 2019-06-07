package com.GlitchyDev.Game.GameStates.Abstract;

import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.Region.RegionConnection;
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
        for (Entity entity : getWorld(location.getWorldUUID()).getRegionAtLocation(location).getEntities()) {
            if (entity.getLocation().equals(location)) {
                return true;
            }
        }
        return false;
    }

    public Entity getEntityAtLocation(Location location) {
        for (Entity entity : getWorld(location.getWorldUUID()).getRegionAtLocation(location).getEntities()) {
            if (entity.getLocation().equals(location)) {
                return entity;
            }
        }
        return null;
    }

    public boolean isBlockAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).isARegionAtLocation(location);
    }

    public Block getBlockAtLocation(Location location) {
        Location offset = getWorld(location.getWorldUUID()).getRegionAtLocation(location).getLocation().getLocationDifference(location);
        return getWorld(location.getWorldUUID()).getRegionAtLocation(location).getBlockRelative(offset);
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

    // Replicate functions

    public void addWorld(World world) {
        System.out.println("Added World " + world);
        currentWorlds.put(world.getWorldUUID(), world);
    }

    public void removeWorld(UUID worldUUID) {
        currentWorlds.remove(worldUUID);
    }


    public void addRegionToGame(Region region) {
        if (region == null) {
            System.out.println("R Oopsie!");
        }
        World world = getWorld(region.getWorldUUID());
        if (world == null) {
            System.out.println("W Oopsie! " + region.getWorldUUID() + " " + getWorlds().size());
        }
        world.getRegions().put(region.getRegionUUID(), region);
        for (Entity entity : region.getEntities()) {
            world.getEntities().put(entity.getUUID(), entity);
            entity.onSpawn(SpawnReason.REGION_MANUALLY_ADDED);
        }
        for (Block block : region.getBlocksArray()) {
            if (block instanceof TickableBlock) {
                getWorld(region.getWorldUUID()).getTickableBlocks().put(block.getLocation(), (TickableBlock) block);
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

        World world = getWorld(entity.getLocation().getWorldUUID());

        Location regionLocation = getRegion(entity.getCurrentRegionUUID(), entity.getWorldUUID()).getLocation();
        entity.setLocation(entity.getLocation().getOffsetLocation(regionLocation));

        world.getRegionAtLocation(entity.getLocation()).getEntities().add(entity);
        world.getEntities().put(entity.getUUID(), entity);

    }

    public void despawnEntity(UUID entityUUID, UUID worldUUID, DespawnReason despawnReason) {
        Entity entity = getWorld(worldUUID).getEntities().get(entityUUID);
        Region hostRegion = getRegion(entity.getCurrentRegionUUID(), worldUUID);
        hostRegion.getEntities().remove(entity);
        getWorld(worldUUID).getRegionAtLocation(entity.getLocation()).getEntities().remove(entityUUID);
        getWorld(worldUUID).getEntities().remove(entityUUID);
        entity.onDespawn(despawnReason);

    }

    public Entity getEntity(UUID entityUUID, UUID worldUUID) {
        return getWorld(worldUUID).getEntities().get(entityUUID);
    }

    public void setBlocks(Collection<Block> blocks) {
        for (Block block : blocks) {
            setBlock(block);
        }
    }

    public void setBlock(Block block) {
        World world = getWorld(block.getLocation().getWorldUUID());
        Region region = world.getRegionAtLocation(block.getLocation());
        Location relativeLocation = region.getLocation().getLocationDifference(block.getLocation());
        Block previousBlock = region.getBlockRelative(relativeLocation);
        region.setBlockRelative(relativeLocation, block);
        if (block instanceof TickableBlock) {
            world.getTickableBlocks().put(block.getLocation(), (TickableBlock) block);
        }
        if (previousBlock instanceof TickableBlock) {
            world.getTickableBlocks().remove(previousBlock.getLocation());
        }
    }



}
