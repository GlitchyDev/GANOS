package com.GlitchyDev.World.Entities;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

public class DebugEntity extends EntityBase {
    public DebugEntity(Location location) {
        super(EntityType.DEBUG, location);
    }

    @Override
    public void tick() {

    }

    @Override
    public void readData(InputBitUtility inputBitUtility) {

    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility) {

    }

    @Override
    public EntityBase clone() {
        return new DebugEntity(getLocation().clone());
    }

    @Override
    public boolean isEqual(EntityBase entityBase) {
        return (this.getEntityType() == entityBase.getEntityType());
    }
}
