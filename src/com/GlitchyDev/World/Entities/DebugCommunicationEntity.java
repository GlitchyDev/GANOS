package com.GlitchyDev.World.Entities;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Transmission.Communication.CommunicationListener;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Messages.CommunicationNoise;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Source.CommunicationSource;
import com.GlitchyDev.World.Transmission.DetectionType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.UUID;

public class DebugCommunicationEntity extends Entity implements CommunicationListener {
    private SpriteItem spriteItem;
    private TextItem textItem;

    public DebugCommunicationEntity(WorldGameState worldGameState, UUID currentRegionUUID,  Location location, Direction direction) {
        super(worldGameState, currentRegionUUID, EntityType.DEBUG_COMMUNICATION_ENTITY, location, direction);
    }

    public DebugCommunicationEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, currentRegionUUID, inputBitUtility, EntityType.DEBUG_COMMUNICATION_ENTITY);

    }
    public DebugCommunicationEntity(WorldGameState worldGameState, UUID worldUUID, Region region, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, worldUUID, region, inputBitUtility, EntityType.DEBUG_COMMUNICATION_ENTITY);

    }

    @Override
    public void onSpawn(SpawnReason spawnReason) {
        CustomFontTexture customTexture = new CustomFontTexture("DebugFont");

        spriteItem = new SpriteItem(AssetLoader.getTextureAsset("Standing_Mirror"),1,1, true);
        spriteItem.setPosition(getLocation().getNormalizedPosition());

        textItem = new TextItem("NO MESSAGE", customTexture);
        textItem.setScale(0.2f);
        textItem.setPosition(getLocation().getNormalizedPosition().add(0,1,0));
        textItem.setRotation(0,-45,180);

        if(worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState) worldGameState).getCommunicationManager().join(this);
        }

    }

    @Override
    public void tick() {
        spriteItem.setPosition(getLocation().getNormalizedPosition());
        textItem.setPosition(getLocation().getNormalizedPosition().add(0,1,0));
    }

    @Override
    public void render(Renderer renderer, Camera camera) {
        renderer.render3DElement(camera,spriteItem,"Default3D");
        renderer.render3DElement(camera,textItem,"Default3D");

    }

    @Override
    public void onDespawn(DespawnReason despawnReason) {

    }

    @Override
    public DetectionType generateDetectionType(Location origin, CommunicationSource soundSource) {
        if(getLocation().getDistance(origin) < 10) {
            return DetectionType.COMPREHENSION;
        }
        if(getLocation().getDistance(origin) < 20) {
            return DetectionType.DETECTION;
        }
        return DetectionType.NONE;
    }

    @Override
    public void recieveMessage(CommunicationMessage message, Location origin, CommunicationSource communicationSource) {
        textItem.setText(message.getMessage());
    }

    @Override
    public void recieveNoise(CommunicationNoise noise, Location origin, CommunicationSource communicationSource) {
        textItem.setText("NOISE " + noise.getNoiseType());
    }
}
