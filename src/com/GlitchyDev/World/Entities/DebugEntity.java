package com.GlitchyDev.World.Entities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public class DebugEntity extends EntityBase {
    public DebugEntity(WorldGameState worldGameState, UUID currentRegionUUID, UUID uuid, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, EntityType.DEBUG, uuid, location, direction);
    }


    public DebugEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, EntityType.DEBUG);
    }

    @Override
    public void onSpawn(SpawnReason spawnReason) {

    }

    @Override
    public void tick() {

    }

    @Override
    public void onDespawn(DespawnReason despawnReason) {

    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

}
