package com.GlitchyDev.World.Views;

import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Region.RegionBase;

import java.util.ArrayList;

public class ViewBase {
    // What entity is tied to this view, what will be updated when
    private final EntityBase tiedEntity;
    private ArrayList<RegionBase> viewableRegions;
    private ArrayList<EntityBase> effectViewedEntities;
    private ArrayList<BlockBase> effectViewedBlocks;


    public ViewBase(EntityBase entityBase) {
        this.tiedEntity = entityBase;
        this.viewableRegions = new ArrayList<>();
        this.effectViewedEntities = new ArrayList<>();
        this.effectViewedBlocks = new ArrayList<>();
    }


    public void updateView() {


    }
}
