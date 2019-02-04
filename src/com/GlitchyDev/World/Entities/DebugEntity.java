package com.GlitchyDev.World.Entities;

import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DebugEntity extends EntityBase {
    public DebugEntity(Location location) {
        super(EntityType.DEBUG, location);
    }

    @Override
    public void tick() {

    }

    @Override
    public void readData(ObjectInputStream objectInputStream) {

    }

    @Override
    public void writeData(ObjectOutputStream objectOutputStream) {

    }

    @Override
    public EntityBase clone() {
        return null;
    }
}
