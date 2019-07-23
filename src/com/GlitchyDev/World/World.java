package com.GlitchyDev.World;

import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Effects.Abstract.TickableEffect;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.AbstractEntities.TickableEntity;
import com.GlitchyDev.World.Navigation.NavigableBlock;
import com.GlitchyDev.World.Region.Enum.RegionConnection;
import com.GlitchyDev.World.Region.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class World {
    // Loaded Regions for this world, probably loaded from a WorldRegionList
    private final HashMap<UUID, Region> regions;
    private final ArrayList<Entity> entities;
    // Tickables
    private final ArrayList<TickableEntity> tickableEntities;
    private final ArrayList<TickableBlock> tickableBlocks;
    private final ArrayList<TickableEffect> tickableEffects;
    // Navigation
    private final ArrayList<NavigableBlock> navigableBlocks;
    // A File loaded with the world that would contain the connections between regions
    private final HashMap<UUID, HashMap<RegionConnection, ArrayList<UUID>>> regionConnections;
    // A File loaded with the world with the configuration of the regions connections

    public static final String FILETYPE = "worldInfo";
    private final UUID worldUUID;

    public World(UUID worldUUID) {
        this.worldUUID = worldUUID;
        this.regions = new HashMap<>();
        this.entities = new ArrayList<>();
        this.tickableEntities = new ArrayList<>();
        this.tickableBlocks = new ArrayList<>();
        this.tickableEffects = new ArrayList<>();
        navigableBlocks = new ArrayList<>();

        this.regionConnections = new HashMap<>();
    }


    public void tick() {
        for(TickableEntity entity : tickableEntities) {
            entity.tick();
        }
        for(TickableBlock tickableBlock: tickableBlocks) {
            tickableBlock.tick();
        }
        for(TickableEffect effect : tickableEffects) {
            effect.tick();
        }
    }

    public boolean isARegionAtLocation(Location location) {
        for(Region region: regions.values()) {
            if(region.isLocationInRegion(location)) {
                return true;
            }
        }
        return false;
    }

    public int countRegionsAtLocation(Location location) {
        int count = 0;
        for(Region region: regions.values()) {
            if(region.isLocationInRegion(location)) {
                count++;
            }
        }
        return count;
    }


    public Region getRegionAtLocation(Location location) {
        for(Region region: regions.values()) {
            if(region.isLocationInRegion(location)) {
                return region;
            }
        }
        return null;
    }



    public ArrayList<Region> getRegionsAtLocation(Location location) {
        ArrayList<Region> presentRegions = new ArrayList<>();
        for(Region region: regions.values()) {
            if(region.isLocationInRegion(location)) {
                presentRegions.add(region);
            }
        }
        return presentRegions;
    }


    public void initNavigableBlockConnections() {
        for(NavigableBlock navigableBlock: navigableBlocks) {
            navigableBlock.initializeConnections();
        }
    }



    public Location getOriginLocation() {
        return new Location(0,0,0, getWorldUUID());
    }

    public void linkRegion(UUID regionUUID, UUID linkedRegion, RegionConnection regionConnection) {
        if(!regionConnections.containsKey(regionUUID)) {
            regionConnections.put(regionUUID,new HashMap<>());
        }
        if(!regionConnections.get(regionUUID).containsKey(regionConnection)) {
            regionConnections.get(regionUUID).put(regionConnection,new ArrayList<>());
        }
        regionConnections.get(regionUUID).get(regionConnection).add(linkedRegion);
    }

    public void unlinkRegion(UUID regionUUID, UUID linkedRegion, RegionConnection regionConnection) {
        regionConnections.get(regionUUID).get(regionConnection).remove(linkedRegion);
    }




    public boolean containsEntity(UUID entityUUID) {
        for(Entity entity: entities) {
            if(entity.getUUID().equals(entityUUID)) {
                return true;
            }
        }
        return false;
    }

    public Entity getEntity(UUID entityUUID) {
        for(Entity entity: entities) {
            if(entity.getUUID().equals(entityUUID)) {
                return entity;
            }
        }
        return null;
    }

    public void removeEntity(UUID entityUUID) {
        if(containsEntity(entityUUID)) {
            entities.remove(getEntity(entityUUID));
        }
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public HashMap<UUID, HashMap<RegionConnection, ArrayList<UUID>>> getRegionConnections() {
        return regionConnections;
    }



    @Override
    public String toString() {
        return "w@" + getWorldUUID();
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public HashMap<UUID, Region> getRegions() {
        return regions;
    }

    public ArrayList<TickableBlock> getTickableBlocks() {
        return tickableBlocks;
    }

    public ArrayList<TickableEffect> getTickableEffects() {
        return tickableEffects;
    }

    public ArrayList<TickableEntity> getTickableEntities() {
        return tickableEntities;
    }

    public ArrayList<NavigableBlock> getNavigableBlocks() {
        return navigableBlocks;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }
}
