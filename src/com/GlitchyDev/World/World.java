package com.GlitchyDev.World;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Region.RegionBase;

import javax.swing.plaf.synth.Region;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class World {
    private HashMap<UUID,RegionBase> regions;
    private HashMap<UUID,EntityBase> entities;
    private HashMap<Location,TickableBlock> tickableBlocks;
    private final String name;

    public World(String name) {
        this.name = name;
        this.regions = new HashMap<>();
        this.entities = new HashMap<>();
        this.tickableBlocks = new HashMap<>();
    }

    public void tick() {
        // Tick Region Blocks
    }




    // Remember to replicate
    public void spawnRegion(RegionBase region, Location location) {
        // ADD REGION TO REGIONS
        region.placeRegion(location);
        for(EntityBase entity: region.getEntities()) {
            entities.put(entity.getUUID(), entity);
        }
        for(BlockBase block: region.getBlocksArrayList()) {
            if(block instanceof TickableBlock) {
                tickableBlocks.put(block.getLocation(), (TickableBlock) block);
            }
        }
    }

    // Remember to replicate
    public void spawnEntity(EntityBase entity) {
        if(isRegionAtLocation(entity.getLocation())) {
            entities.put(entity.getUUID(), entity);
            getRegionAtLocation(entity.getLocation()).getEntities().add(entity);
        }
    }

    // Remember to replicate
    public void spawnBlock(BlockBase blockBase, Location location) {
        if(isRegionAtLocation(location)) {
            if(blockBase instanceof TickableBlock) {
                tickableBlocks.put(location, (TickableBlock) blockBase);
            }
            RegionBase placeRegion = getRegionAtLocation(location);
            Location relativeLocation = location.getLocationDifference(placeRegion.getLocation());
            placeRegion.setBlockRelative(relativeLocation,blockBase);
        }
    }

    // Remember to replicate
    public void despawnRegion(RegionBase region) {
        // Remove Region from Regions
        for(EntityBase entity: region.getEntities()) {
            entities.remove(entity.getUUID(), entity);
        }
        for(Location tickableBlockLocation: tickableBlocks.keySet()) {
            if(region.isLocationInRegion(tickableBlockLocation)) {
                tickableBlocks.remove(tickableBlockLocation);
            }
        }
    }

    // Remember to replicate
    public void despawnEntity(EntityBase entity) {
        entities.remove(entity.getUUID(), entity);
        if(isRegionAtLocation(entity.getLocation())) {
            getRegionAtLocation(entity.getLocation()).getEntities().remove(entity);
        }
    }

    // Remember to replicate
    public void despawnBlock(BlockBase blockBase) {
        if(blockBase instanceof TickableBlock) {
            tickableBlocks.remove(blockBase.getLocation());
        }
        if(isRegionAtLocation(blockBase.getLocation())) {
            RegionBase placeRegion = getRegionAtLocation(blockBase.getLocation());
            Location relativeLocation = blockBase.getLocation().getLocationDifference(placeRegion.getLocation());
            placeRegion.setBlockRelative(relativeLocation,blockBase);
        }
    }





    // Use Chunks, Efficiencies
    public boolean isRegionAtLocation(Location location) {
        for(RegionBase region: regions.values()) {
            if(region.isLocationInRegion(location)) {
                return true;
            }
        }
        return false;
    }

    // Use Chunks, Efficiencies
    public RegionBase getRegionAtLocation(Location location) {
        for(RegionBase region: regions.values()) {
            if(region.isLocationInRegion(location)) {
                return region;
            }
        }
        return null;
    }

    // Use Chunks, Efficiencies
    public Location getOriginLocation() {
        return new Location(0,0,0,this);
    }
}
