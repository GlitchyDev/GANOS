package com.GlitchyDev.Game;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.AbstractEntities.PlayerEntity;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.Views.EntityView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class Player {
    private final WorldGameState worldGameState;
    private final UUID playerUUID;
    private PlayerEntity playerEntity;
    private final ArrayList<Entity> controlledEntities;

    public Player(WorldGameState worldGameState, UUID playerUUID, PlayerEntity playerEntity) {
        this.worldGameState = worldGameState;
        this.playerUUID = playerUUID;
        this.playerEntity = playerEntity;
        this.playerEntity.setPlayer(this);
        this.controlledEntities = new ArrayList<>();
    }

    public Player(WorldGameState worldGameState, InputBitUtility inputBitUtility) throws IOException {
        this.worldGameState = worldGameState;
        this.playerUUID = inputBitUtility.getNextUUID();

        UUID worldUUID = inputBitUtility.getNextUUID();
        UUID regionUUID = inputBitUtility.getNextUUID();
        EntityType playerEntityType = EntityType.values()[inputBitUtility.getNextCorrectIntByte()];
        this.playerEntity = (PlayerEntity) playerEntityType.getEntityFromInput(inputBitUtility,worldGameState, worldUUID, regionUUID);
        this.playerEntity.setPlayer(this);
        // Place into world as loaded
        Region hostRegion = worldGameState.getRegion(playerEntity.getCurrentRegionUUID(),playerEntity.getWorldUUID());
        playerEntity.setLocation(hostRegion.getLocation().getOffsetLocation(playerEntity.getLocation()));
        worldGameState.spawnEntity(playerEntity, SpawnReason.READ_FILE);

        int totalControlledEntities = inputBitUtility.getNextCorrectIntByte();
        this.controlledEntities = new ArrayList<>(totalControlledEntities);
        for(int i = 0; i < totalControlledEntities; i++) {
            EntityType entityType = EntityType.values()[inputBitUtility.getNextCorrectIntByte()];
            Entity entity = entityType.getEntityFromInput(inputBitUtility,worldGameState, worldUUID, regionUUID);
            Region controlledEntityRegion = worldGameState.getRegion(entity.getCurrentRegionUUID(),entity.getWorldUUID());
            entity.setLocation(controlledEntityRegion.getLocation().getOffsetLocation(entity.getLocation()));
            worldGameState.spawnEntity(entity, SpawnReason.READ_FILE);

            controlledEntities.add(entity);
        }
    }

    public void saveToFile(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(playerUUID);

        outputBitUtility.writeNextUUID(playerEntity.getLocation().getWorldUUID());
        outputBitUtility.writeNextUUID(worldGameState.getRegionAtLocation(playerEntity.getLocation()).getRegionUUID());
        playerEntity.writeData(outputBitUtility, false);

        outputBitUtility.writeNextCorrectByteInt(controlledEntities.size());
        for(int i = 0; i < controlledEntities.size(); i++) {
            controlledEntities.get(i).writeData(outputBitUtility, false);
        }
    }




    public void setPlayerEntity(PlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    public PlayerEntity getPlayerEntity() {
        return playerEntity;
    }

    public ArrayList<Entity> getControlledEntities() {
        return controlledEntities;
    }

    public EntityView getEntityView() {
        return playerEntity.getEntityView();
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public String toString() {
        return "p@" + playerUUID + "," + getPlayerEntity();
    }
}
