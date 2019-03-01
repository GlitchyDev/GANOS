package com.GlitchyDev.Game.Player;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.AbstractEntities.PlayerEntityBase;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Views.EntityView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Player {
    private final WorldGameState worldGameState;
    private final UUID playerUUID;
    private PlayerEntityBase playerEntity;
    private final ArrayList<EntityBase> controlledEntities;
    private final EntityView entityView;
    private final ArrayList<EffectBase> effects;

    public Player(WorldGameState worldGameState, UUID playerUUID, PlayerEntityBase playerEntity) {
        this.worldGameState = worldGameState;
        this.playerUUID = playerUUID;
        this.playerEntity = playerEntity;
        this.playerEntity.setPlayer(this);
        this.controlledEntities = new ArrayList<>();
        this.entityView = new EntityView();
        this.effects = new ArrayList<>();
    }

    public Player(WorldGameState worldGameState, InputBitUtility inputBitUtility) throws IOException {
        this.worldGameState = worldGameState;
        this.playerUUID = inputBitUtility.getNextUUID();

        UUID worldUUID = inputBitUtility.getNextUUID();
        UUID regionUUID = inputBitUtility.getNextUUID();
        EntityType playerEntityType = EntityType.values()[inputBitUtility.getNextCorrectIntByte()];
        this.playerEntity = (PlayerEntityBase) playerEntityType.getEntityFromInput(inputBitUtility,worldGameState, worldUUID, regionUUID);
        this.playerEntity.setPlayer(this);
        // Place into world as loaded
        worldGameState.spawnEntity(playerEntity);

        int totalControlledEntities = inputBitUtility.getNextCorrectIntByte();
        this.controlledEntities = new ArrayList<>(totalControlledEntities);
        for(int i = 0; i < totalControlledEntities; i++) {
            // Grab and add ControlledEntities to list
            // Controllable entites can be living or non living
            // Spawn Entity4summon 
        }
        this.entityView = new EntityView();
        int totalEffects = inputBitUtility.getNextCorrectIntByte();
        this.effects = new ArrayList<>(totalEffects);
        for(int i = 0; i < totalEffects; i++) {
            // Grab and add Effects to list
        }
    }

    public void saveToFile(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(playerUUID);

        outputBitUtility.writeNextUUID(playerEntity.getLocation().getWorldUUID());
        outputBitUtility.writeNextUUID(worldGameState.getRegionAtLocation(playerEntity.getLocation()).getRegionUUID());
        playerEntity.writeData(outputBitUtility);

        outputBitUtility.writeNextCorrectByteInt(controlledEntities.size());
        for(int i = 0; i < controlledEntities.size(); i++) {
            // Grab and add ControlledEntities to list
            // Controllable entites can be living or non living
        }

        outputBitUtility.writeNextCorrectByteInt(effects.size());
        for(int i = 0; i < effects.size(); i++) {
            // Grab and add Effects to list
        }
    }




    public void setPlayerEntity(PlayerEntityBase playerEntity) {
        this.playerEntity = playerEntity;
    }

    public PlayerEntityBase getPlayerEntity() {
        return playerEntity;
    }

    public ArrayList<EntityBase> getControlledEntities() {
        return controlledEntities;
    }

    public EntityView getEntityView() {
        return entityView;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public ArrayList<EffectBase> getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        return "p@" + playerUUID + "," + getPlayerEntity();
    }
}
