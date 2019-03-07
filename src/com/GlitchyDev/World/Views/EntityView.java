package com.GlitchyDev.World.Views;

import com.GlitchyDev.World.Region.Region;

import java.util.ArrayList;
import java.util.UUID;

public class EntityView {
    private ArrayList<Region> viewableRegions;

    public EntityView() {
        this.viewableRegions = new ArrayList<>();
    }

    public ArrayList<Region> getViewableRegions() {
        return viewableRegions;
    }

    public void setViewableRegions(ArrayList<Region> viewableRegions) {
        this.viewableRegions = viewableRegions;
    }

    public boolean containsRegion(UUID regionUUID) {
        for(Region region: viewableRegions) {
            if(region.getRegionUUID() == regionUUID) {
                return true;
            }
        }
        return false;
    }
}
