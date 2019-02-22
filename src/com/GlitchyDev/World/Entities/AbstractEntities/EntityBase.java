package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
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

    public EntityBase(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, UUID uuid, Location location, Direction direction) {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;

        this.entityType = entityType;
        this.uuid = uuid;
        this.location = location;
        this.direction = direction;
    }

    public EntityBase(WorldGameState worldGameState, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;

        this.entityType = entityType;
        this.uuid = inputBitUtility.getNextUUID();
        this.location = new Location(inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte());
        this.direction = Direction.values()[inputBitUtility.getNextCorrectedIntBit(3)];
    }

    public abstract void tick();

    // Do not write Location, as that can be refereed engineered from the read protocol
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

    // DO NOT COPY UUID FOR THE LOVE OF CHRIST HOLY FUCK
    public abstract EntityBase getCopy();

    // Copmpare basic stuff here, and super it for further editing
    public abstract boolean equals(Object obj);

    // Add a move method that will move it along regions, triggering triggerable blocks and shit like its supposed too



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

    // Replicate!
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    @Override
    public String toString() {
        return "e@" + entityType + "," + uuid + "," + location + "," + direction;
    }
}
