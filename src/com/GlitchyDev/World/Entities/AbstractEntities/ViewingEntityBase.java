package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TriggerableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionHidingEffect;
import com.GlitchyDev.World.Entities.Effects.Abstract.RegionRevealingEffect;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;
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

    public ViewingEntityBase(WorldGameState worldGameState, UUID worldUUID, Region region, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(worldGameState, worldUUID, region, inputBitUtility, entityType);
    }




    @Override
    public void move(Location newLocation, EntityMovementType movementType) {
        // Check and Verify we actually BE IN THE CORRECT PLACE DAWG
        if(worldGameState.isARegionAtLocation(newLocation)) {
            boolean canMove = false;
            if(worldGameState.countRegionsAtLocation(newLocation) == 1) {
                if(getEntityView().containsRegion(worldGameState.getRegionAtLocation(newLocation).getRegionUUID())) {
                    canMove = true;
                }
            } else {
                ArrayList<Region> regionsAtLocation = worldGameState.getRegionsAtLocation(newLocation);
                for(Region region: regionsAtLocation) {
                    if(getEntityView().containsRegion(region.getRegionUUID())) {
                        canMove = true;
                    }
                }
            }

            if(canMove) {

                // Check the blocks for preventing leave triggers
                BlockBase currentBlock = worldGameState.getBlockAtLocation(getLocation());
                if (currentBlock instanceof TriggerableBlock) {
                    ((TriggerableBlock) currentBlock).exitBlockSuccessfully(movementType, this);
                }
                BlockBase nextBlock = worldGameState.getBlockAtLocation(newLocation);
                if (nextBlock instanceof TriggerableBlock) {
                    ((TriggerableBlock) newLocation).enterBlockSccessfully(movementType, this);
                }

                // GO AHEAD AND CHANGE THE LOCATION
                Location oldLocation = getLocation();
                setLocation(newLocation);

                // Find where we are moving, if there is no overlap, no problem, just go to that one, if overlap, go to one in view
                Region selectedRegion = null;
                if (worldGameState.countRegionsAtLocation(oldLocation) > 1) {
                    ArrayList<Region> overlappingRegion = worldGameState.getRegionsAtLocation(getLocation());
                    for (Region viewableRegion : getEntityView().getViewableRegions()) {
                        if (overlappingRegion.contains(viewableRegion)) {
                            selectedRegion = viewableRegion;
                        }
                    }
                } else {
                    selectedRegion = worldGameState.getRegionAtLocation(getLocation());
                }

                // Change our current RegionID to match the correct stuff, and move the entity across the border
                if (getCurrentRegionUUID() != selectedRegion.getRegionUUID()) {
                    worldGameState.getRegion(getCurrentRegionUUID(), getWorldUUID()).getEntities().remove(this);
                    setCurrentRegionUUID(selectedRegion.getRegionUUID());
                    worldGameState.getRegion(getCurrentRegionUUID(), getWorldUUID()).getEntities().add(this);
                    recalculateView();
                }

                // Replicate
                if (worldGameState instanceof ServerWorldGameState) {
                    ((ServerWorldGameState) worldGameState).replicateMoveEntity(getUUID(), oldLocation, newLocation);
                }
            } else {
                System.out.println("ViewingEntityBase: No in Vision region at " + newLocation);
            }
        } else {
            System.out.println("ViewingEntityBase: No Valid region at " + newLocation);
        }
    }


    public void recalculateView() {
        ArrayList<Region> connectedRegions = new ArrayList<>();
        HashMap<UUID, HashMap<RegionConnectionType, ArrayList<UUID>>> connections = worldGameState.getRegionConnections(getWorldUUID());
        ArrayList<RegionConnectionType> seeableConnectionTypes = new ArrayList<>();


        for(RegionConnectionType regionConnection: connections.get(getCurrentRegionUUID()).keySet()) {
            if(regionConnection.isVisibleByDefault()) {
                boolean hidden = false;
                for(EffectBase effect: getEffects()) {
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
                Region region = worldGameState.getRegion(regionUUID, getWorldUUID());
                connectedRegions.add(region);
            }
        }

        entityView.getViewableRegions().clear();
        entityView.getViewableRegions().addAll(connectedRegions);



        // Call Server World Add region for Client
        // Call Server world remove Region for Client
    }

    public EntityView getEntityView() {
        return entityView;
    }
}
