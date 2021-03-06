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
import com.GlitchyDev.World.Entities.AbstractEntities.TickableEntity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.General.CustomTransparentRenderable;
import com.GlitchyDev.World.Navigation.NavigatingEntity;
import com.GlitchyDev.World.Navigation.PathType;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.UUID;

public class DebugEntity extends Entity implements CustomVisibleEntity, CustomTransparentRenderable, TickableEntity, NavigatingEntity {
    private GameItem gameItem;
    private SpriteItem spriteItem;


    public DebugEntity(WorldGameState worldGameState, UUID currentRegionUUID, Location location, Direction direction) {
        super(EntityType.DEBUG_ENTITY, worldGameState, currentRegionUUID, location, direction);
    }


    public DebugEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(EntityType.DEBUG_ENTITY, worldGameState, inputBitUtility, worldUUID, currentRegionUUID);

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
        spriteItem.setRotation(0,tickCount,0);
        if(tickCount % 120 == 0) {


            ((ServerWorldGameState)worldGameState).updateEntityViability(this);
            isVisible = !isVisible;



            /*
            for(Entity entity: worldGameState.getWorldUUID(getWorldUUID()).getEntities()) {

                if(isVisible) {
                    entity.applyEffect(new ServerPacketReplicationEffect(worldGameState));
                } else {
                    ArrayList<EntityEffect> removedEffects = new ArrayList<>();
                    for(EntityEffect effect: entity.getCurrentEffects()) {
                        if(effect.getEffectType() == EffectType.SERVER_PACKET_REPLICATION) {
                            removedEffects.add(effect);
                        }
                    }
                    for(EntityEffect effect: removedEffects) {
                        entity.removeEffect(effect);
                    }
                }
            }
             */
        }
        tickCount++;

    }

    @Override
    public void onDespawn(DespawnReason despawnReason) {
        gameItem.getMesh().cleanUp();
    }

    @Override
    public void writeData(OutputBitUtility outputBitUtility, boolean isReplicated) throws IOException {
        super.writeData(outputBitUtility, isReplicated);
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

    @Override
    public void renderTransparency(Renderer renderer, Camera camera) {
        gameItem.setPosition(getLocation().getNormalizedPosition());
        spriteItem.setPosition(getLocation().getNormalizedPosition());

        renderer.render3DElement(camera,gameItem, "Default3D");
        renderer.render3DElement(camera,spriteItem, "Default3D");
    }

    @Override
    public double getDistance(Vector3f position) {
        return getLocation().getDistance(position);
    }

    @Override
    public double getMovementCost(PathType pathType) {
        return 0.1;
    }
}
