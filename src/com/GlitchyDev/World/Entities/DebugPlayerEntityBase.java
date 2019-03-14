package com.GlitchyDev.World.Entities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.GameWindow;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.PlayerEntityBase;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.UUID;

public class DebugPlayerEntityBase extends PlayerEntityBase {
    private final GameItem gameItem;

    public DebugPlayerEntityBase(WorldGameState worldGameState, UUID currentRegionUUID, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, EntityType.DEBUG_PLAYER, location, direction);
        this.gameItem = new GameItem(AssetLoader.getMeshAsset("cube").clone());
        gameItem.getMesh().setTexture(AssetLoader.getTextureAsset("DefaultTexture"));
        gameItem.setPosition(getLocation().getNormalizedPosition());
    }

    public DebugPlayerEntityBase(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, EntityType.DEBUG_PLAYER);
        this.gameItem = new GameItem(AssetLoader.getMeshAsset("cube").clone());
        gameItem.getMesh().setTexture(AssetLoader.getTextureAsset("DefaultTexture"));
        gameItem.setPosition(getLocation().getNormalizedPosition());
    }

    public DebugPlayerEntityBase(WorldGameState worldGameState, UUID worldUUID, Region region, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, region, inputBitUtility, EntityType.DEBUG_PLAYER);
        this.gameItem = new GameItem(AssetLoader.getMeshAsset("cube").clone());
        gameItem.getMesh().setTexture(AssetLoader.getTextureAsset("DefaultTexture"));
        gameItem.setPosition(getLocation().getNormalizedPosition());
    }

    @Override
    public void onSpawn(SpawnReason spawnReason) {
        gameItem.setPosition(getLocation().getNormalizedPosition());
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Renderer renderer, GameWindow gameWindow, Camera camera) {
        renderer.render3DElement(gameWindow,"Default3D",camera,gameItem);
    }

    @Override
    public void onDespawn(DespawnReason despawnReason) {
    }



    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        gameItem.setPosition(getLocation().getNormalizedPosition());
    }
}
