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


    public Location getOriginLocation() {
        return new Location(0,0,0,this);
    }
}
