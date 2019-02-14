package com.GlitchyDev.World.Entities;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.IOException;

public class DebugEntity extends EntityBase {
    public DebugEntity(Location location, Direction direction) {
        super(EntityType.DEBUG, location, direction);
    }

    public DebugEntity(InputBitUtility inputBitUtility) throws IOException {
        super(EntityType.DEBUG, inputBitUtility);
    }

    @Override
    public void tick() {

    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility) {

    }

    @Override
    public EntityBase clone() {
        return new DebugEntity(getLocation(), getDirection());
    }

    @Override
    public boolean isEqual(EntityBase entityBase) {
        return (this.getEntityType() == entityBase.getEntityType());
    }
}
