package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionHidingEffect;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.Region.RegionConnectionType;
import com.GlitchyDev.World.Views.EntityView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class ViewingEntityBase extends EntityBase {
    private EntityView entityView;


    public ViewingEntityBase(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, entityType, location, direction);
        this.entityView = new EntityView();
    }

    public final void recalculateView() {
        ArrayList<RegionBase> regions = entityView.getViewableRegions();
        entityView.getViewableRegions().clear();
        RegionBase currentRegion = worldGameState.getRegion(getCurrentRegionUUID(),getWorldUUID());
        HashMap<UUID, HashMap<RegionConnectionType, ArrayList<UUID>>> connections = worldGameState.getRegionConnections(getWorldUUID());

        for(RegionConnectionType regionConnectionType: connections.get(currentRegion).keySet()) {
            // Check if it gets hidden
            if(regionConnectionType.isVisibleByDefault()) {
                boolean gotHidden = false;
                for(EffectBase effect: getEffects()) {
                    if(effect instanceof RegionHidingEffect) {
                        if(((RegionHidingEffect) effect).doHideRegionConnection(regionConnectionType)) {
                            gotHidden = true;
                        }
                    }
                }
                if(!gotHidden) {
                    for(UUID regionUUID: connections.get(currentRegion.getRegionUUID()).get(regionConnectionType)) {
                        RegionBase region = worldGameState.getRegion(regionUUID,getWorldUUID());
                        entityView.getViewableRegions().add(region);
                    }
                }
            } else {
                // Check if it gets revealed
                boolean isRevealed = false;
                for(EffectBase effect: getEffects()) {
                    if(effect instanceof RegionRevealingEffect) {
                        if(((RegionRevealingEffect) effect).doShowRegionConnection(regionConnectionType)) {
                            isRevealed = true;
                        }
                    }
                }
                if(!isRevealed) {
                    for(UUID regionUUID: connections.get(currentRegion.getRegionUUID()).get(regionConnectionType)) {
                        RegionBase region = worldGameState.getRegion(regionUUID,getWorldUUID());
                        entityView.getViewableRegions().add(region);
                    }
                }
            }


        }


    }
}
