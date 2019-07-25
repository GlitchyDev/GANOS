package com.GlitchyDev.World.Entities;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.CustomRenderEntity;
import com.GlitchyDev.World.Entities.AbstractEntities.PlayerEntity;
import com.GlitchyDev.World.Entities.AbstractEntities.TickableEntity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Lighting.DynamicLightProducer;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public class DebugPlayerEntity extends PlayerEntity implements CustomRenderEntity, DynamicLightProducer, TickableEntity {
    private GameItem gameItem;

    public DebugPlayerEntity(WorldGameState worldGameState, UUID currentRegionUUID, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, EntityType.DEBUG_PLAYER, location, direction);

    }

    public DebugPlayerEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, EntityType.DEBUG_PLAYER);

    }

    @Override
    public void onSpawn(SpawnReason spawnReason) {
        this.gameItem = new GameItem(AssetLoader.getMeshAsset("cube").clone());
        gameItem.getMesh().setTexture(AssetLoader.getTextureAsset("DefaultTexture"));
        gameItem.setPosition(getLocation().getNormalizedPosition());
    }


    @Override
    public void onDespawn(DespawnReason despawnReason) {

    }



    @Override
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        super.writeData(outputBitUtility);
    }


    @Override
    public void renderCustomEntity(Renderer renderer, Camera camera) {
        gameItem.setPosition(getLocation().getNormalizedPosition());
        renderer.render3DElement(camera,gameItem, "Default3D");
    }

    @Override
    public boolean needLightingUpdate() {
        return true;
    }

    @Override
    public Direction[] getDirectionsProduced() {
        Direction[] directions = new Direction[1];
        directions[0] = Direction.BELOW;
        return directions;
    }

    @Override
    public int getLightLevelProduced() {
        return 30;
    }

    @Override
    public Location getEmissionLocation() {
        return getLocation();
    }

    @Override
    public void tick() {

    }
}
