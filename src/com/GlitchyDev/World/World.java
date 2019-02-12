package com.GlitchyDev.World;

import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Region.RegionBase;

import java.util.ArrayList;

public class World {
    private ArrayList<RegionBase> regions;
    private ArrayList<EntityBase> entities;
    private final String name;


    public World(String name) {
        this.name = name;
    }

    public void tick() {
        // Tick Region Blocks
    }








    public boolean isRegionsAtLocation(Location location) {
        for(RegionBase region: regions) {
            if(region.isLocationInRegion(location)) {
                return true;
            }
        }
        return false;
    }

    public int countRegionsAtLocation(Location location) {
        int count = 0;
        for(RegionBase region: regions) {
            if(region.isLocationInRegion(location)) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<RegionBase> getRegionsAtLocation(Location location) {
        ArrayList<RegionBase> regionsAtLocation = new ArrayList<>();
        for(RegionBase region: regions) {
            if(region.isLocationInRegion(location)) {
                regionsAtLocation.add(region);
            }
        }
        return regions;
    }

    public Location getOriginLocation() {
        return new Location(0,0,0,this);
    }
}
