package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.EntityType;
import com.GlitchyDev.World.Location;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class EntityBase {
    private final EntityType entityType;
    private Location location;
    private Direction direction;


    public EntityBase(EntityType entityType, Location location) {
        this.entityType = entityType;
        this.location = location;
        direction = Direction.NORTH;
    }

    public EntityBase(EntityType entityType, Location location, Direction direction) {
        this.entityType = entityType;
        this.location = location;
        this.direction = direction;
    }

    public abstract void tick();


    public abstract void readData(ObjectInputStream objectInputStream);

    // Do not write Location, as that can be refereed engineered from the read protocol
    public abstract void writeData(ObjectOutputStream objectOutputStream);

    public abstract EntityBase clone();

    public abstract boolean isEqual(EntityBase entityBase);








    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
