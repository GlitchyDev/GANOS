package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.MovementTriggerableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.UUID;

public abstract class NonViewingEntity extends Entity {
    public NonViewingEntity(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, Location location, Direction direction) {
        super(entityType, worldGameState, currentRegionUUID, location, direction);
    }

    public NonViewingEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(entityType, worldGameState, inputBitUtility, worldUUID, currentRegionUUID);
    }

    public void teleport(Location newLocation) {
        Region selectedRegion = worldGameState.getRegionAtLocation(newLocation);
        teleport(newLocation,selectedRegion.getRegionUUID());
    }

    public void teleport(Location newLocation, UUID newRegionUUID) {
        Region oldRegion = worldGameState.getRegion(getCurrentRegionUUID(),getWorldUUID());
        Region newRegion = worldGameState.getRegion(newRegionUUID,newLocation.getWorldUUID());

        Location oldOffset = oldRegion.getLocation().getLocationDifference(getLocation());
        Location newOffset = newRegion.getLocation().getLocationDifference(newLocation);

        Block startingBlock = worldGameState.getRegionAtLocation(getLocation()).getBlockRelative(oldOffset);
        Block endingBlock = worldGameState.getRegionAtLocation(newLocation).getBlockRelative(newOffset);

        if (startingBlock instanceof MovementTriggerableBlock) {
            ((MovementTriggerableBlock) startingBlock).exitBlockSuccessfully(EntityMovementType.TELEPORT, this);
        }

        if (endingBlock instanceof MovementTriggerableBlock) {
            ((MovementTriggerableBlock) endingBlock).enterBlockSccessfully(EntityMovementType.TELEPORT, this);
        }


        oldRegion.removeEntity(getUUID());
        newRegion.addEntity(this);
        if(newLocation.getWorldUUID() != getWorldUUID()) {
            worldGameState.getWorld(getWorldUUID()).removeEntity(this);
            worldGameState.getWorld(newLocation.getWorldUUID()).addEntity(this);
        }

        setLocation(newLocation);
        currentRegionUUID = newRegionUUID;
    }
}
