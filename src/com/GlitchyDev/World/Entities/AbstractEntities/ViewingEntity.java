package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.MovementTriggerableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.Abstract.RegionHidingEffect;
import com.GlitchyDev.World.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.Region.Enum.RegionConnection;
import com.GlitchyDev.World.Views.EntityView;
import com.GlitchyDev.World.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class ViewingEntity extends Entity {
    private EntityView entityView;


    public ViewingEntity(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, Location location, Direction direction) {
        super(entityType, worldGameState, currentRegionUUID, location, direction);
        this.entityView = new EntityView();
    }

    public ViewingEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(entityType, worldGameState, inputBitUtility, worldUUID, currentRegionUUID);
    }



    /**
     *
     * @param newLocation
     * @param movementType
     * @return Moved Successfully
     */
    public boolean move(Location newLocation, EntityMovementType movementType) {
        // Check and Verify we actually BE IN THE CORRECT PLACE DAWG
        Region oldRegion = worldGameState.getRegion(getCurrentRegionUUID(),getWorldUUID());
        Region newRegion = null;
        if(worldGameState.isARegionAtLocation(newLocation)) {
            boolean viableMovingRegion = false;
            if(worldGameState.countRegionsAtLocation(newLocation) == 1) {
                if(getEntityView().containsRegion(worldGameState.getRegionAtLocation(newLocation).getRegionUUID())) {
                    viableMovingRegion = true;
                    newRegion = worldGameState.getRegionAtLocation(newLocation);
                }
            } else {
                ArrayList<Region> regionsAtLocation = worldGameState.getRegionsAtLocation(newLocation);
                for(Region region: regionsAtLocation) {
                    if(getEntityView().containsRegion(region.getRegionUUID())) {
                        viableMovingRegion = true;
                        newRegion = region;
                    }
                }
            }

            if(viableMovingRegion) {

                Location oldOffset = oldRegion.getLocation().getLocationDifference(getLocation());
                Location newOffset = newRegion.getLocation().getLocationDifference(newLocation);

                Block startingBlock = worldGameState.getRegionAtLocation(getLocation()).getBlockRelative(oldOffset);
                Block endingBlock = worldGameState.getRegionAtLocation(newLocation).getBlockRelative(newOffset);

                // Check the blocks for preventing leave triggers
                if(!(startingBlock instanceof MovementTriggerableBlock) || ((MovementTriggerableBlock) startingBlock).attemptExitBlock(movementType,this)) {
                    if(!(endingBlock instanceof MovementTriggerableBlock) || ((MovementTriggerableBlock) endingBlock).attemptEnterBlock(movementType,this)) {



                        if (startingBlock instanceof MovementTriggerableBlock) {
                            ((MovementTriggerableBlock) startingBlock).exitBlockSuccessfully(movementType, this);
                        }

                        if (endingBlock instanceof MovementTriggerableBlock) {
                            ((MovementTriggerableBlock) endingBlock).enterBlockSccessfully(movementType, this);
                        }

                        // GO AHEAD AND CHANGE THE LOCATION
                        Location oldLocation = getLocation();
                        setLocation(newLocation);

                        // Find where we are moving, if there is no overlap, no problem, just go to that one, if overlap, go to one in view

                        // Change our current RegionID to match the correct stuff, and move the entity across the border
                        if (getCurrentRegionUUID() != newRegion.getRegionUUID()) {
                            worldGameState.getRegion(getCurrentRegionUUID(), getWorldUUID()).getEntities().remove(this);
                            setCurrentRegionUUID(newRegion.getRegionUUID());
                            worldGameState.getRegion(getCurrentRegionUUID(), getWorldUUID()).getEntities().add(this);
                            recalculateView();
                        }

                        // Replicate
                        ((ServerWorldGameState) worldGameState).replicateMoveEntity(getUUID(), oldLocation, newLocation);

                        return true;
                    } else {
                        System.out.println("ViewingEntityBase: Can't enter block " + newLocation);
                    }
                } else {
                    System.out.println("ViewingEntityBase: Can't exit block " + getLocation());
                }
            } else {
                System.out.println("ViewingEntityBase: No in Vision region at " + newLocation);
            }
        } else {
            System.out.println("ViewingEntityBase: No Valid region at " + newLocation);
        }
        return false;
    }


    public void recalculateView() {
        ArrayList<Region> connectedRegions = new ArrayList<>();
        HashMap<UUID, HashMap<RegionConnection, ArrayList<UUID>>> connections = worldGameState.getRegionConnections(getWorldUUID());

        ArrayList<RegionConnection> seeableConnectionTypes = new ArrayList<>();
        for(RegionConnection regionConnection: connections.get(getCurrentRegionUUID()).keySet()) {
            if(regionConnection.isVisibleByDefault()) {
                boolean hidden = false;
                for(Effect effect: getCurrentEffects()) {
                    if(effect instanceof RegionHidingEffect) {
                        if(((RegionHidingEffect) effect).doHideRegionConnection(regionConnection)) {
                            hidden = true;
                            break;
                        }
                    }
                }
                if(!hidden) {
                    seeableConnectionTypes.add(regionConnection);
                }
            } else {
                for(Effect effect: getCurrentEffects()) {
                    if(effect instanceof RegionRevealingEffect) {
                        if(((RegionRevealingEffect) effect).doShowRegionConnection(regionConnection)) {
                            seeableConnectionTypes.add(regionConnection);
                            break;
                        }
                    }
                }
            }
        }

        for(RegionConnection regionConnection : seeableConnectionTypes) {
            for(UUID regionUUID: connections.get(getCurrentRegionUUID()).get(regionConnection)) {
                Region region = worldGameState.getRegion(regionUUID, getWorldUUID());
                connectedRegions.add(region);
            }
        }

        entityView.getViewableRegions().clear();
        entityView.getViewableRegions().addAll(connectedRegions);

        // Call Server World Add region for Client
        // Call Server world remove Region for Client
    }

    public boolean regionConnectionVisible(RegionConnection regionConnection) {
        if(regionConnection.isVisibleByDefault()) {
            for(Effect effect: getCurrentEffects()) {
                if(effect instanceof RegionHidingEffect) {
                    if(((RegionHidingEffect) effect).doHideRegionConnection(regionConnection)) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            for(Effect effect: getCurrentEffects()) {
                if(effect instanceof RegionRevealingEffect) {
                    if(((RegionRevealingEffect) effect).doShowRegionConnection(regionConnection)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public boolean canRegionBeVisiblyConnected(UUID targetRegion) {
        if(getCurrentRegionUUID().equals(targetRegion)) {
            return true;
        }
        World currentWorld = worldGameState.getWorld(getWorldUUID());

        ArrayList<RegionConnection> visibleRegionConnections = new ArrayList<>();
        for(RegionConnection connection: RegionConnection.values()) {
            if(regionConnectionVisible(connection)) {
                visibleRegionConnections.add(connection);
            }
        }

        ArrayList<UUID> checkedRegions = new ArrayList<>();
        ArrayList<UUID> connectedRegions = new ArrayList<>();
        connectedRegions.add(getCurrentRegionUUID());

        while(connectedRegions.size() != 0) {
            UUID checkRegion = connectedRegions.get(0);

            for(RegionConnection validConnection: visibleRegionConnections) {
                if(currentWorld.getRegionConnections().containsKey(checkRegion)) {
                    if(currentWorld.getRegionConnections().get(checkRegion).containsKey(validConnection)) {
                        for(UUID foundRegion: currentWorld.getRegionConnections().get(checkRegion).get(validConnection)) {
                            if(!checkedRegions.contains(foundRegion)) {
                                connectedRegions.add(foundRegion);
                                if(foundRegion.equals(targetRegion)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

            checkedRegions.add(checkRegion);
            connectedRegions.remove(0);
        }
        return false;
    }





    public EntityView getEntityView() {
        return entityView;
    }
}
