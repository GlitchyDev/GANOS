package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public abstract class PlayerEntityBase extends EntityBase {
    private Player player;

    public PlayerEntityBase(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, entityType, location, direction);
    }

    public PlayerEntityBase(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, entityType);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
