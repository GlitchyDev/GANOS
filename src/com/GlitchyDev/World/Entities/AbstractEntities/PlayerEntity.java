package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public class PlayerEntity extends EntityBase {

    public PlayerEntity(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, UUID uuid, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, entityType, uuid, location, direction);
    }

    public PlayerEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, entityType);
    }

    @Override
    public void tick() {

    }


    /**
     * Should not be called on player
     * @return
     */
    @Override
    public EntityBase getCopy() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
