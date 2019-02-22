package com.GlitchyDev.World.Entities.Enums;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.DebugEntity;

import java.io.IOException;
import java.util.UUID;

public enum EntityType {
    DEBUG;

    public EntityBase getEntityFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState, UUID regionUUID) throws IOException {
        switch (this) {
            case DEBUG:
                return new DebugEntity(worldGameState, regionUUID, inputBitUtility);
            default:
                return new DebugEntity(worldGameState, regionUUID, inputBitUtility);
        }

    }




}
