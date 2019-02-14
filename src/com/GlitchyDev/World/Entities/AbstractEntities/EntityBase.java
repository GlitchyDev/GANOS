package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.IO.InputBitUtility;
import com.GlitchyDev.IO.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.EntityType;
import com.GlitchyDev.World.Location;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

public abstract class EntityBase {
    private final EntityType entityType;
    private final UUID uuid;
    private Location location;
    private Direction direction;


    // REPLICATE!!!!
    public EntityBase(EntityType entityType, Location location) {
        this.entityType = entityType;
        this.location = location;
        direction = Direction.NORTH;
        uuid = null;
    }

    public EntityBase(EntityType entityType, Location location, Direction direction) {
        this.entityType = entityType;
        this.location = location;
        this.direction = direction;
        uuid = null;
    }

    public abstract void tick();


    // MAKE A CONSTRUCTOR DUMBASS!!!!
    public abstract void readData(InputBitUtility inputBitUtility);

    // Do not write Location, as that can be refereed engineered from the read protocol
    public abstract void writeData(OutputBitUtility outputBitUtility);

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
