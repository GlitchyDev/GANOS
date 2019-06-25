package com.GlitchyDev.World.Views;

import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.util.ArrayList;
import java.util.UUID;

public class EntityView {
    private final ArrayList<Region> viewableRegions;

    public EntityView() {
        this.viewableRegions = new ArrayList<>();
    }

    public ArrayList<Region> getViewableRegions() {
        return viewableRegions;
    }


    public boolean containsRegion(UUID regionUUID) {
        for(Region region: viewableRegions) {
            System.out.println(region + " " + regionUUID);
            if(region.getRegionUUID().equals(regionUUID)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLocationInView(Location location) {
        for(Region region: viewableRegions) {
            if(region.isLocationInRegion(location)) {
                return true;
            }
        }
        return false;
    }

    public Region getRegion(UUID regionUUID) {
        for(Region region: viewableRegions) {
            if(region.getRegionUUID().equals(regionUUID)) {
                return region;
            }
        }
        return null;
    }

    public boolean containsEntity(UUID entityUUID) {
        for(Region region: viewableRegions) {
            if(region.containsEntity(entityUUID)) {
                return true;
            }
        }
        return false;
    }

    public void clearEntity(UUID entityUUID) {
        for(Region region: viewableRegions) {
            region.removeEntity(entityUUID);
        }
    }

    public int countEntities() {
        int count = 0;
        for(Region region: viewableRegions) {
            count += region.getEntities().size();
        }
        return count;
    }
}
