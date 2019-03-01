package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionHidingEffect;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.Region.RegionConnectionType;
import com.GlitchyDev.World.Views.EntityView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class ViewingEntityBase extends EntityBase {
    private EntityView entityView;


    public ViewingEntityBase(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, entityType, location, direction);
        this.entityView = new EntityView();
    }

    public ViewingEntityBase(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, entityType);
    }

    public void recalculateView() {
        ArrayList<RegionBase> connectedRegions = new ArrayList<>();
        HashMap<UUID, HashMap<RegionConnectionType, ArrayList<UUID>>> connections = worldGameState.getRegionConnections(getWorldUUID());
        ArrayList<RegionConnectionType> seeableConnectionTypes = new ArrayList<>();


        for(RegionConnectionType regionConnection: connections.get(getCurrentRegionUUID()).keySet()) {
            if(regionConnection.isVisibleByDefault()) {
                for(EffectBase effect: getEffects()) {
                    if(effect instanceof RegionHidingEffect) {
                        if(((RegionHidingEffect) effect).doHideRegionConnection(regionConnection)) {
                            seeableConnectionTypes.add(regionConnection);
                            break;
                        }
                    }
                }
            } else {
                for(EffectBase effect: getEffects()) {
                    if(effect instanceof RegionRevealingEffect) {
                        if(((RegionRevealingEffect) effect).doShowRegionConnection(regionConnection)) {
                            seeableConnectionTypes.add(regionConnection);
                            break;
                        }
                    }
                }
            }
        }

        for(RegionConnectionType regionConnectionType: seeableConnectionTypes) {
            for(UUID regionUUID: connections.get(getCurrentRegionUUID()).get(regionConnectionType)) {
                RegionBase region = worldGameState.getRegion(regionUUID, getWorldUUID());
                connectedRegions.add(region);
            }
        }
    }

    public EntityView getEntityView() {
        return entityView;
    }
}
