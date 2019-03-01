package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionHidingEffect;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.Region.RegionConnectionType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class PlayerEntityBase extends ViewingEntityBase {
    private Player player;

    public PlayerEntityBase(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, entityType, location, direction);
    }

    public PlayerEntityBase(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, entityType);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void recalculateView() {
        if(worldGameState instanceof ServerWorldGameState) {
            ArrayList<RegionBase> previousRegions = getEntityView().getViewableRegions();

            ArrayList<RegionBase> connectedRegions = new ArrayList<>();
            ArrayList<RegionBase> newlyConnected = new ArrayList<>();
            ArrayList<RegionBase> newlyRemoved = new ArrayList<>();

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
                    if(!previousRegions.contains(region)) {
                        newlyConnected.add(region);
                    }
                }
            }

            for(RegionBase region: previousRegions) {
                if(!connectedRegions.contains(region)) {
                    newlyRemoved.add(region);
                }
            }


            for(RegionBase region: newlyConnected) {
                try {
                    ((ServerWorldGameState) worldGameState).addRegion(getPlayer().getPlayerUUID(),region);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for(RegionBase region: newlyRemoved) {
                try {
                    ((ServerWorldGameState) worldGameState).removeRegion(getPlayer().getPlayerUUID(),region.getRegionUUID(),region.getWorldUUID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
