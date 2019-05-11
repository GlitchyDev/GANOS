package com.GlitchyDev.World.Entities;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.CustomVisibleEntity;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.UUID;

public class DebugEntity extends EntityBase implements CustomVisibleEntity {
    private SpriteItem spriteItem;


    public DebugEntity(WorldGameState worldGameState, UUID currentRegionUUID, Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, EntityType.DEBUG_ENTITY, location, direction);

    }


    public DebugEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, EntityType.DEBUG_ENTITY);

    }
    public DebugEntity(WorldGameState worldGameState, UUID worldUUID, Region region, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, region, inputBitUtility, EntityType.DEBUG_ENTITY);

    }


    @Override
    public void onSpawn(SpawnReason spawnReason) {
        spriteItem = new SpriteItem(AssetLoader.getTextureAsset("Icon32x32"),1,1, true);
        spriteItem.setPosition(getLocation().getNormalizedPosition());


    }

    int tickCount = 0;
    @Override
    public void tick() {
        if(tickCount % 120 == 0) {
            ((ServerWorldGameState)worldGameState).updateEntityViability(this);
        }
        tickCount++;
    }

    @Override
    public void render(Renderer renderer, Camera camera) {

        renderer.render3DElement(camera,spriteItem, "Default3D");
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
        System.out.println(spriteItem);
        spriteItem.setPosition(getLocation().getNormalizedPosition());
    }

    @Override
    public boolean doSeeEntity(Player player) {
        return Math.random() < 0.5;
    }
}
