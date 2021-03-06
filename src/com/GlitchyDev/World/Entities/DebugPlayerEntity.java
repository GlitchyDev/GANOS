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
import com.GlitchyDev.World.Lighting.RadiantLightProducer;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public class DebugPlayerEntity extends PlayerEntity implements CustomRenderEntity, RadiantLightProducer, TickableEntity {
    private GameItem gameItem;
    private Location previousLocation;

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

        previousLocation = getLocation();
    }


    @Override
    public void onDespawn(DespawnReason despawnReason) {

    }



    @Override
    public void writeData(OutputBitUtility outputBitUtility, boolean isReplicated) throws IOException {
        super.writeData(outputBitUtility, isReplicated);
    }


    @Override
    public void renderCustomEntity(Renderer renderer, Camera camera) {
        gameItem.setPosition(getLocation().getNormalizedPosition());
        renderer.render3DElement(camera,gameItem, "Default3D");
    }

    @Override
    public boolean doNeedLightUpdate() {
        if(previousLocation.equals(getLocation())) {
            return false;
        }
        previousLocation = getLocation();
        return true;
    }

    @Override
    public Location getEmissionLocation() {
        return getLocation();
    }

    @Override
    public boolean isDynamic() {
        return true;
    }



    @Override
    public void tick() {

    }


    @Override
    public int getLightLevel() {
        return 10;
    }
}
