package com.GlitchyDev.World.Views;

import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.AbstractEntities.PlayerEntity;
import com.GlitchyDev.World.Region.RegionBase;

import java.util.ArrayList;

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
}
