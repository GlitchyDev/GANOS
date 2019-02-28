package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TriggerableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;

import java.io.IOException;
import java.util.UUID;

public abstract class EntityBase {
    // Utill
    protected WorldGameState worldGameState;
    private UUID currentRegionUUID;
    // Saved to file
    private final EntityType entityType;
    private final UUID uuid;
    private Location location;
    private Direction direction;




    // REPLICATE!!!!

    // Entities and Blocks should be provided their World/Region on spawn

    /**
     * Creates Entities naturally
     * @param worldGameState
     * @param currentRegionUUID
     * @param entityType
     * @param uuid
     * @param location
     * @param direction
     */
    public EntityBase(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, UUID uuid, Location location, Direction direction) {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;

        this.entityType = entityType;
        this.uuid = uuid;
        this.location = location;
        this.direction = direction;
    }


    /**
     * Allows Entities to be constructed from IO/Packets
     * @param worldGameState
     * @param worldUUID
     * @param currentRegionUUID
     * @param inputBitUtility
     * @param entityType
     * @throws IOException
     */
    public EntityBase(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;

        this.entityType = entityType;
        this.uuid = inputBitUtility.getNextUUID();
        Location regionLocation = worldGameState.getRegion(currentRegionUUID,worldUUID).getLocation();
        Location relativeLocation = new Location(inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte());
        this.location = regionLocation.getOffsetLocation(relativeLocation);
        this.direction = Direction.values()[inputBitUtility.getNextCorrectedIntBit(3)];
    }



    public abstract void tick();

    /**
     *
     * @param newLocation
     * @param movementType
     * @return If successful
     */
    public boolean attemptMove(Location newLocation, EntityMovementType movementType) {
        BlockBase currentBlock = worldGameState.getBlockAtLocation(getLocation());
        if(currentBlock instanceof TriggerableBlock) {
            if(!((TriggerableBlock) currentBlock).attemptExitBlock(movementType,this)) {
                return false;
            }
        }
        BlockBase nextBlock = worldGameState.getBlockAtLocation(newLocation);
        if(nextBlock instanceof TriggerableBlock) {
            if(!((TriggerableBlock) nextBlock).attemptExitBlock(movementType,this)) {
                return false;
            }
        }
        move(newLocation, movementType);
        return true;
    }

    /**
     *
     * @param newLocation
     * @param movementType
     */
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

        if(worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState) worldGameState).replicateMoveEntity(uuid,getLocation().getWorldUUID(),oldLocation,newLocation);
        }
    }

    public void setDirection(Direction newDirection) {
        this.direction = newDirection;
        if(worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState) worldGameState).replicateChangeDirectionEntity(uuid,newDirection);
        }
    }

    /**
     * Writes file to IO and Packet
     * @param outputBitUtility
     * @throws IOException
     */
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(entityType.ordinal());
        outputBitUtility.writeNextUUID(uuid);

        RegionBase currentRegion = worldGameState.getRegionAtLocation(location);
        Location internalOffset = currentRegion.getLocation().getLocationDifference(getLocation());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getX());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getY());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getZ());

        outputBitUtility.writeNextCorrectedBitsInt(direction.ordinal(),3);

    }



    public UUID getUUID() {
        return uuid;
    }

    public UUID getCurrentRegionUUID() {
        return currentRegionUUID;
    }

    public void setCurrentRegionUUID(UUID currentRegionUUID) {
        this.currentRegionUUID = currentRegionUUID;
    }

    public Location getLocation() {
        return location;
    }

    // Replicate!
    public void setLocation(Location location) {
        this.location = location;
    }

    public Direction getDirection() {
        return direction;
    }


    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "e@" + entityType + "," + uuid + "," + location + "," + direction;
    }
}
