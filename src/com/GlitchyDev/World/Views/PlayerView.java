package com.GlitchyDev.World.Views;

import com.GlitchyDev.World.Entities.AbstractEntities.PlayerEntity;
import com.GlitchyDev.World.Region.RegionBase;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerView {
    private final PlayerEntity player;
    private ArrayList<RegionBase> viewableRegions;

    public PlayerView(PlayerEntity player) {
        this.player = player;
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
