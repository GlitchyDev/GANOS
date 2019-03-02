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





    @Override
    public void move(Location newLocation, EntityMovementType movementType) {
        BlockBase currentBlock = worldGameState.getBlockAtLocation(getLocation());
        if(currentBlock instanceof TriggerableBlock) {
            ((TriggerableBlock) currentBlock).exitBlockSuccessfully(movementType,this);
        }
        BlockBase nextBlock = worldGameState.getBlockAtLocation(newLocation);
        if(nextBlock instanceof TriggerableBlock) {
            ((TriggerableBlock) newLocation).enterBlockSccessfully(movementType,this);
        }

        Location oldLocation = getLocation();
        setLocation(newLocation);

        RegionBase selectedRegion = null;
        if(worldGameState.countRegionsAtLocation(oldLocation) > 1) {
            ArrayList<RegionBase> overlappingRegion = worldGameState.getRegionsAtLocation(getLocation());
            for(RegionBase viewableRegion: getEntityView().getViewableRegions()) {
                if(overlappingRegion.contains(viewableRegion)) {
                    selectedRegion = viewableRegion;
                }
            }
        } else {
            selectedRegion = worldGameState.getRegionAtLocation(getLocation());
        }

        worldGameState.getRegion(getCurrentRegionUUID(),getWorldUUID()).getEntities().remove(this);
        setCurrentRegionUUID(selectedRegion.getRegionUUID());
        worldGameState.getRegion(getCurrentRegionUUID(),getWorldUUID()).getEntities().add(this);

        if(worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState) worldGameState).replicateMoveEntity(getUUID(),oldLocation,newLocation);
        }
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

        // Call Server World Add region for Client
        // Call Server world remove Region for Client
    }

    public EntityView getEntityView() {
        return entityView;
    }
}
