package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Views.PlayerView;

import java.io.IOException;
import java.util.UUID;

public abstract class PlayerEntity extends EntityBase {
    private final Player player;

    public PlayerEntity(Player player, WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, UUID uuid, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, entityType, uuid, location, direction);
        this.player = player;
        player.setPlayerEntity(this);
    }

    public PlayerEntity(Player player, WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, entityType);
        this.player = player;
        player.setPlayerEntity(this);
    }

    public Player getPlayer() {
        return player;
    }
}
