package com.GlitchyDev.Game.Player;

import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;

import java.util.UUID;

public class Player {
    private final UUID playerUUID;
    private final String name;
    private EntityBase playerCharacter;
    // Inventory
    // View
    // Effects ect


    public Player(UUID playerUUID, String name, EntityBase playerCharacter) {
        this.playerUUID = playerUUID;
        this.name = name;
        this.playerCharacter = playerCharacter;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getName() {
        return name;
    }

    public EntityBase getPlayerCharacter() {
        return playerCharacter;
    }
}
