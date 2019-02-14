package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;

import java.io.IOException;
import java.util.UUID;

public abstract class EntityBase {
    private final EntityType entityType;
    private final UUID uuid;
    private Location location;
    private Direction direction;


    // REPLICATE!!!!


    public EntityBase(EntityType entityType, Location location, Direction direction) {
        this.entityType = entityType;
        this.location = location;
        this.direction = direction;
        uuid = null;
    }

    public EntityBase(EntityType entityType, InputBitUtility inputBitUtility) throws IOException {
        // Entity Type has already been found
        this.entityType = entityType;
        this.uuid = inputBitUtility.getNextUUID();
        this.location = new Location(inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), null);
        this.direction = Direction.values()[inputBitUtility.getNextCorrectedIntBit(3)];
    }

    public abstract void tick();

    // Do not write Location, as that can be refereed engineered from the read protocol
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(entityType.ordinal());
        outputBitUtility.writeNextUUID(uuid);
        // Get Local Location in Region
        RegionBase currentRegion = location.getWorld().getRegionAtLocation(location);
        Location o

    }

    // DO NOT COPY UUID FOR THE LOVE OF CHRIST HOLY FUCK
    public abstract EntityBase clone();

    public abstract boolean isEqual(EntityBase entityBase);

    // Add a move method that will move it along regions, triggering triggerable blocks and shit like its supposed too



    public UUID getUUID() {
        return uuid;
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

}
