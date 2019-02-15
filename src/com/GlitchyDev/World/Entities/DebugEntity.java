package com.GlitchyDev.World.Entities;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;

import java.io.IOException;
import java.util.UUID;

public class DebugEntity extends EntityBase {
    public DebugEntity(UUID uuid, Location location, Direction direction) {
        super(EntityType.DEBUG, uuid, location, direction);
    }

    public DebugEntity(InputBitUtility inputBitUtility) throws IOException {
        super(EntityType.DEBUG, inputBitUtility);
    }

    @Override
    public void tick() {

    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility, RegionBase hostRegion) throws IOException {
        super.writeData(outputBitUtility, hostRegion);
    }

    @Override
    public EntityBase getCopy() {
        return new DebugEntity(UUID.randomUUID(), getLocation(), getDirection());
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

}
