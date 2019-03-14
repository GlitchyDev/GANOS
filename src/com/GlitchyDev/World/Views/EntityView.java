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
            if(region.getRegionUUID() == regionUUID) {
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
            if(region.getRegionUUID() == regionUUID) {
                return region;
            }
        }
        return null;
    }
}
