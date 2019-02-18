package com.GlitchyDev.World;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Region.RegionBase;
import org.lwjgl.system.CallbackI;

import java.util.HashMap;
import java.util.UUID;

public class World {
    private HashMap<Integer,RegionBase> regions;
    private HashMap<UUID,EntityBase> entities;
    private HashMap<Location,TickableBlock> tickableBlocks;
    private final int worldID;

    public World(int worldID) {
        this.worldID = worldID;
        this.regions = new HashMap<>();
        this.entities = new HashMap<>();
        this.tickableBlocks = new HashMap<>();
    }

    public void tick() {
        for(TickableBlock tickableBlock: tickableBlocks.values()) {
            tickableBlock.tick();
        }
        for(EntityBase entityBase: entities.values()) {
            entityBase.tick();
        }
        // Hopefully regions don't need to be ticked d_d
    }




    // Remember to replicate
    public void spawnRegion(RegionBase region, Location location) {
        // ADD REGION TO REGIONS
        boolean passesOverlap = true;
        for(RegionBase testRegion: regions.values()) {
            if(testRegion.doRegionsIntersect(region)) {
                passesOverlap = false;
            }
        }
        if(passesOverlap) {
            region.placeRegion(location);
            for (EntityBase entity : region.getEntities()) {
                entities.put(entity.getUUID(), entity);
            }
            for (BlockBase block : region.getBlocksArray()) {
                if (block instanceof TickableBlock) {
                    tickableBlocks.put(block.getLocation(), (TickableBlock) block);
                }
            }
        } else {
            System.out.println("World: Error, spawned region overlaps existing region");
        }
    }

    // Remember to replicate
    public void spawnEntity(EntityBase entity) {
        if(isRegionAtLocation(entity.getLocation())) {
            entities.put(entity.getUUID(), entity);
            getRegionAtLocation(entity.getLocation()).getEntities().add(entity);
        } else {
            System.out.println("World: Error, Entity can not be spawn outside region");
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
        } else {
            System.out.println("World: Error, Block can not be spawn outside region");
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
        if(isRegionAtLocation(entity.getLocation())) {
            entities.remove(entity.getUUID(), entity);
            getRegionAtLocation(entity.getLocation()).getEntities().remove(entity);
        } else {
            System.out.println("World: Error, Entity can not be despawn outside region");
        }
    }

    // Remember to replicate
    public void despawnBlock(BlockBase blockBase) {
        if(isRegionAtLocation(blockBase.getLocation())) {
            if(blockBase instanceof TickableBlock) {
                tickableBlocks.remove(blockBase.getLocation());
            }

            RegionBase placeRegion = getRegionAtLocation(blockBase.getLocation());
            Location relativeLocation = blockBase.getLocation().getLocationDifference(placeRegion.getLocation());
            placeRegion.setBlockRelative(relativeLocation,new AirBlock(blockBase.getLocation()));
        } else {
            System.out.println("World: Error, Block can not be despawn outside region");
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
        return new Location(0,0,0,this);
    }

    public int getWorldID() {
        return worldID;
    }
}
