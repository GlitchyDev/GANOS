package com.GlitchyDev.World.Entities.Enums;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.DebugCommunicationEntity;
import com.GlitchyDev.World.Entities.DebugEntity;
import com.GlitchyDev.World.Entities.DebugPlayerEntityBase;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.UUID;

public enum EntityType {
    DEBUG_ENTITY,
    DEBUG_PLAYER,
    DEBUG_COMMUNICATION_ENTITY,

    ;

    public EntityBase getEntityFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState, UUID worldUUID, UUID regionUUID) throws IOException {
        switch (this) {
            case DEBUG_ENTITY:
                return new DebugEntity(worldGameState, worldUUID, regionUUID, inputBitUtility);
            case DEBUG_PLAYER:
                return new DebugPlayerEntityBase(worldGameState, worldUUID, regionUUID, inputBitUtility);
            case DEBUG_COMMUNICATION_ENTITY:
                return new DebugCommunicationEntity(worldGameState,worldUUID,regionUUID,inputBitUtility);
            default:
                System.out.println("ERROR: Entity not registered in EntityType");
                return null;
        }

    }

    public EntityBase getEntityFromInput(InputBitUtility inputBitUtility, WorldGameState worldGameState, UUID worldUUID, Region region) throws IOException {
        switch (this) {
            case DEBUG_ENTITY:
                return new DebugEntity(worldGameState, worldUUID, region, inputBitUtility);
            case DEBUG_PLAYER:
                return new DebugPlayerEntityBase(worldGameState, worldUUID, region, inputBitUtility);
            case DEBUG_COMMUNICATION_ENTITY:
                return new DebugCommunicationEntity(worldGameState, worldUUID, region, inputBitUtility);
            default:
                System.out.println("ERROR: Entity not registered in EntityType");
                return null;
        }

    }

}
