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
import com.GlitchyDev.World.Region.Region;
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

    public PlayerEntityBase(WorldGameState worldGameState, UUID worldUUID, Region region, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(worldGameState, worldUUID, region, inputBitUtility, entityType);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void recalculateView() {
        System.out.println("Recalculating view for Player " + player.getPlayerUUID());
        if(worldGameState instanceof ServerWorldGameState) {
            ArrayList<Region> previouslyConnectedRegions = getEntityView().getViewableRegions();

            ArrayList<Region> connectedRegions = new ArrayList<>();
            ArrayList<Region> newlyConnected = new ArrayList<>();
            ArrayList<Region> newlyRemoved = new ArrayList<>();

            HashMap<UUID, HashMap<RegionConnectionType, ArrayList<UUID>>> connections = worldGameState.getRegionConnections(getWorldUUID());




            Region startingRegion = worldGameState.getRegion(getCurrentRegionUUID(),getWorldUUID());
            System.out.println("Starting Region " + getCurrentRegionUUID());
            if(!previouslyConnectedRegions.contains(startingRegion)) {
                newlyConnected.add(startingRegion);
            }
            connectedRegions.add(startingRegion);




            ArrayList<RegionConnectionType> seeableConnectionTypes = new ArrayList<>();
            for(RegionConnectionType regionConnection: connections.get(getCurrentRegionUUID()).keySet()) {
                if(regionConnection.isVisibleByDefault()) {
                    boolean hideRegion = false;
                    for(EffectBase effect: getEffects()) {
                        if(effect instanceof RegionHidingEffect) {
                            if(((RegionHidingEffect) effect).doHideRegionConnection(regionConnection)) {
                               hideRegion = true;
                            }
                        }
                    }
                    if(!hideRegion) {
                        seeableConnectionTypes.add(regionConnection);
                    }

                } else {
                    boolean showRegion = false;
                    for(EffectBase effect: getEffects()) {
                        if(effect instanceof RegionRevealingEffect) {
                            if(((RegionRevealingEffect) effect).doShowRegionConnection(regionConnection)) {
                                showRegion = true;
                                break;
                            }
                        }
                    }
                    if(showRegion) {
                        seeableConnectionTypes.add(regionConnection);
                    }
                }
            }

            for(RegionConnectionType regionConnectionType: seeableConnectionTypes) {
                for(UUID regionUUID: connections.get(getCurrentRegionUUID()).get(regionConnectionType)) {
                    Region region = worldGameState.getRegion(regionUUID, getWorldUUID());
                    connectedRegions.add(region);
                    if(!previouslyConnectedRegions.contains(region)) {
                        newlyConnected.add(region);
                    }
                }
            }

            for(Region region: previouslyConnectedRegions) {
                if(!connectedRegions.contains(region)) {
                    newlyRemoved.add(region);
                }
            }

            getEntityView().getViewableRegions().removeAll(newlyRemoved);
            getEntityView().getViewableRegions().addAll(newlyConnected);




            for(Region addedRegion: newlyConnected) {
                try {
                    ((ServerWorldGameState) worldGameState).playerAddRegionToView(getPlayer().getPlayerUUID(),addedRegion);
                    System.out.println("Adding Region " + addedRegion.getRegionUUID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for(Region removedRegion: newlyRemoved) {
                try {
                    ((ServerWorldGameState) worldGameState).playerRemoveRegionFromView(getPlayer().getPlayerUUID(),removedRegion.getRegionUUID(),removedRegion.getWorldUUID());
                    System.out.println("Removed Region " + removedRegion.getRegionUUID());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
