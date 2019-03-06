package com.GlitchyDev.World.Entities;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.GameWindow;
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
    private final SpriteItem spriteItem;


    public DebugEntity(WorldGameState worldGameState, UUID currentRegionUUID, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, EntityType.DEBUG_ENTITY, location, direction);
        spriteItem = new SpriteItem(AssetLoader.getTextureAsset("Icon32x32"),true);
        spriteItem.setPosition(getLocation().getNormalizedPosition());
    }


    public DebugEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, EntityType.DEBUG_ENTITY);
        spriteItem = new SpriteItem(AssetLoader.getTextureAsset("Icon32x32"),true);
        spriteItem.setPosition(getLocation().getNormalizedPosition());
    }

    @Override
    public void onSpawn(SpawnReason spawnReason) {

    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Renderer renderer, GameWindow gameWindow, Camera camera) {
        renderer.render3DElement(gameWindow,"Default3D",camera,spriteItem);
    }

    @Override
    public void onDespawn(DespawnReason despawnReason) {
        spriteItem.getMesh().cleanUp();
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
