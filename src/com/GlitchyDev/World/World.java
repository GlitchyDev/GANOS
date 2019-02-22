package com.GlitchyDev.World;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.Region.RegionConnectionType;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class World {
    // Loaded Regions for this world, probably loaded from a WorldRegionList
    private HashMap<UUID,RegionBase> regions;
    private HashMap<UUID,EntityBase> entities;
    private HashMap<Location,TickableBlock> tickableBlocks;
    // A File loaded with the world that would contain the connections between regions
    private HashMap<UUID, HashMap<RegionConnectionType, ArrayList<UUID>>> regionConnections;
    // A File loaded with the world with the configuration of the regions connections
    private HashMap<UUID, Location> regionLocations;
    private final UUID worldUUID;

    public World(UUID worldUUID) {
        this.worldUUID = worldUUID;
        this.regions = new HashMap<>();
        this.entities = new HashMap<>();
        this.tickableBlocks = new HashMap<>();

        this.regionConnections = new HashMap<>();
        this.regionLocations = new HashMap<>();
    }

    public void tick() {
        for(TickableBlock tickableBlock: tickableBlocks.values()) {
            tickableBlock.tick();
        }
        for(EntityBase entityBase: entities.values()) {
            entityBase.tick();
        }
    }

    public boolean isRegionAtLocation(Location location) {
        for(RegionBase region: regions.values()) {
            if(region.isLocationInRegion(location)) {
                return true;
            }
        }
        return false;
    }

    public RegionBase getRegionAtLocation(Location location) {
        for(RegionBase region: regions.values()) {
            if(region.isLocationInRegion(location)) {
                return region;
            }
        }
        return null;
    }

    public Location getOriginLocation() {
        return new Location(0,0,0,getWorldID());
    }

    public void linkRegion(UUID regionUUID, UUID linkedRegion, RegionConnectionType regionConnectionType) {
        if(!regionConnections.containsKey(regionUUID)) {
            regionConnections.put(regionUUID,new HashMap<>());
        }
        if(regionConnections.get(regionUUID).containsKey(regionConnectionType)) {
            regionConnections.get(regionUUID).put(regionConnectionType,new ArrayList<>());
        }
        regionConnections.get(regionUUID).get(regionConnectionType).add(linkedRegion);
    }


    // Getters

    public HashMap<UUID, RegionBase> getRegions() {
        return regions;
    }

    public HashMap<Location, TickableBlock> getTickableBlocks() {
        return tickableBlocks;
    }

    public HashMap<UUID, EntityBase> getEntities() {
        return entities;
    }

    public HashMap<UUID, HashMap<RegionConnectionType, ArrayList<UUID>>> getRegionConnections() {
        return regionConnections;
    }

    public HashMap<UUID, Location> getRegionLocations() {
        return regionLocations;
    }

    @Override
    public String toString() {
        return "w@" + getWorldID();
    }

    public UUID getWorldID() {
        return worldUUID;
    }
}
