package com.GlitchyDev.Game.GameStates.Abstract;

import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.World;

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

    public RegionBase getRegion(UUID regionUUID, UUID worldUUID) {
        return getWorld(worldUUID).getRegions().get(regionUUID);
    }


    public boolean isRegionAtLocation(Location location) {
        return getWorld(location.getWorldUUID()).isRegionAtLocation(location);
    }

    public RegionBase getRegionAtLocation(Location location) {
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
        return getWorld(location.getWorldUUID()).isRegionAtLocation(location);
    }

    public BlockBase getBlockAtLocation(Location location) {
        Location offset = getWorld(location.getWorldUUID()).getRegionAtLocation(location).getLocation().getLocationDifference(location);
        return getWorld(location.getWorldUUID()).getRegionAtLocation(location).getBlockRelative(offset);
    }



    // Replicate functions

    public void addWorld(World world) {
        currentWorlds.put(world.getWorldID(),world);
    }

    public void removeWorld(UUID worldUUID) {
        currentWorlds.remove(worldUUID);
    }


    public void spawnRegion(RegionBase regionBase) {
        getWorld(regionBase.getWorldUUID()).getRegions().put(regionBase.getRegionUUID(),regionBase);
        for(EntityBase entity: regionBase.getEntities()) {
            spawnEntity(entity);
        }
        for(BlockBase block: regionBase.getBlocksArray()) {
            if(block instanceof TickableBlock) {
                getWorld(regionBase.getWorldUUID()).getTickableBlocks().put(block.getLocation(), (TickableBlock) block);
            }
        }
    }

    public void despawnRegion(UUID regionUUID, UUID worldUUID) {
        RegionBase region = getWorld(worldUUID).getRegions().get(regionUUID);
        for(EntityBase entity: region.getEntities()) {
            despawnEntity(entity.getUUID(),worldUUID);
        }
        for(BlockBase block: region.getBlocksArray()) {
            if(block instanceof TickableBlock) {
                getWorld(worldUUID).getTickableBlocks().remove(block.getLocation());
            }
        }
        getWorld(worldUUID).getRegions().remove(regionUUID);
    }

    // Replicate Functions

    public void spawnEntity(EntityBase entity) {
        World world = getWorld(entity.getLocation().getWorldUUID());
        world.getRegionAtLocation(entity.getLocation()).getEntities().add(entity);
        world.getEntities().put(entity.getUUID(),entity);

    }

    public void despawnEntity(UUID entityUUID, UUID worldUUID) {
        EntityBase entity = getWorld(worldUUID).getEntities().get(entityUUID);
        getWorld(worldUUID).getRegionAtLocation(entity.getLocation()).getEntities().remove(entityUUID);
        getWorld(worldUUID).getEntities().remove(entityUUID);

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
        RegionBase region = world.getRegionAtLocation(block.getLocation());
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
