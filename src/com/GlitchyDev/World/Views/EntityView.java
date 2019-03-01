package com.GlitchyDev.World.Views;

import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.AbstractEntities.PlayerEntityBase;
import com.GlitchyDev.World.Region.RegionBase;

import java.util.ArrayList;
import java.util.UUID;

public class EntityView {
    private ArrayList<RegionBase> viewableRegions;

    public EntityView() {
        this.viewableRegions = new ArrayList<>();
    }

    public ArrayList<RegionBase> getViewableRegions() {
        return viewableRegions;
    }

    public boolean containsRegion(UUID regionUUID) {
        for(RegionBase region: viewableRegions) {
            if(region.getRegionUUID() == regionUUID) {
                return true;
            }
        }
        return false;
    }
}
