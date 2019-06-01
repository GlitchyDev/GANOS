package com.GlitchyDev.World;

import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.Region.RegionConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class World {
    // Loaded Regions for this world, probably loaded from a WorldRegionList
    private HashMap<UUID, Region> regions;
    private HashMap<UUID,EntityBase> entities;
    private HashMap<Location,TickableBlock> tickableBlocks;
    // A File loaded with the world that would contain the connections between regions
    private HashMap<UUID, HashMap<RegionConnection, ArrayList<UUID>>> regionConnections;
    // A File loaded with the world with the configuration of the regions connections

    public static final String FILETYPE = "worldInfo";
    private final UUID worldUUID;

    public World(UUID worldUUID) {
        this.worldUUID = worldUUID;
        this.regions = new HashMap<>();
        this.entities = new HashMap<>();
        this.tickableBlocks = new HashMap<>();

        this.regionConnections = new HashMap<>();
    }


    public void tick() {
        for(TickableBlock tickableBlock: tickableBlocks.values()) {
            tickableBlock.tick();
        }
        for(EntityBase entityBase: entities.values()) {
            entityBase.tick();
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

    // Getters

    public HashMap<UUID, Region> getRegions() {
        return regions;
    }

    public HashMap<Location, TickableBlock> getTickableBlocks() {
        return tickableBlocks;
    }

    public HashMap<UUID, EntityBase> getEntities() {
        return entities;
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
}
