package com.GlitchyDev.World.Entities;

import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.CustomVisibleEntity;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public class DebugEntity extends Entity implements CustomVisibleEntity {
    private GameItem gameItem;
    private SpriteItem spriteItem;


    public DebugEntity(WorldGameState worldGameState, UUID currentRegionUUID, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, EntityType.DEBUG_ENTITY, location, direction);
    }


    public DebugEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, EntityType.DEBUG_ENTITY);

    }



    @Override
    public void onSpawn(SpawnReason spawnReason) {
        gameItem = new GameItem(AssetLoader.getMeshAsset("CubicMesh6"));
        gameItem.getMesh().setTexture(AssetLoader.getTextureAsset("grassblock"));
        gameItem.setPosition(getLocation().getNormalizedPosition());

        spriteItem = new SpriteItem(AssetLoader.getTextureAsset("Standing_Mirror"),5,9,false,true);
        spriteItem.setPosition(getLocation().getNormalizedPosition());

    }

    int tickCount = 0;
    boolean isVisible = true;
    @Override
    public void tick() {
        if(tickCount % 120 == 0) {
            ((ServerWorldGameState)worldGameState).updateEntityViability(this);
            isVisible = !isVisible;
        }
        tickCount++;


    }

    @Override
    public void render(Renderer renderer, Camera camera) {
        gameItem.setPosition(getLocation().getNormalizedPosition());
        spriteItem.setPosition(getLocation().getNormalizedPosition());

        renderer.renderBillboard3DElement(camera,gameItem, "Default3D");
        renderer.render3DElement(camera,spriteItem, "Default3D");
    }

    @Override
    public void onDespawn(DespawnReason despawnReason) {
        gameItem.getMesh().cleanUp();
    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DebugEntity) {
            if(((DebugEntity) obj).getUUID() == getUUID()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
    }

    @Override
    public boolean doSeeEntity(Entity entity) {
        return isVisible;
    }
}
