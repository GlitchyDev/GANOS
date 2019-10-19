package com.GlitchyDev.World.Entities;

import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.AbstractEntities.TickableEntity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Events.Communication.CommunicationListener;
import com.GlitchyDev.World.Events.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Events.Communication.Constructs.Messages.CommunicationNoise;
import com.GlitchyDev.World.Events.Communication.Constructs.Source.CommunicationSource;
import com.GlitchyDev.World.Events.Communication.DetectionType;
import com.GlitchyDev.World.General.CustomTransparentRenderable;
import com.GlitchyDev.World.Lighting.RadiantLightProducer;
import com.GlitchyDev.World.Location;
import org.joml.Vector3f;

import java.io.IOException;
import java.util.UUID;

public class DebugCommunicationEntity extends Entity implements TickableEntity, CommunicationListener, CustomTransparentRenderable, RadiantLightProducer {
    private SpriteItem spriteItem;
    private TextItem textItem;

    public DebugCommunicationEntity(WorldGameState worldGameState, UUID currentRegionUUID,  Location location, Direction direction) {
        super(EntityType.DEBUG_COMMUNICATION_ENTITY,worldGameState, currentRegionUUID, location, direction);
    }

    public DebugCommunicationEntity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility) throws IOException {
        super(EntityType.DEBUG_COMMUNICATION_ENTITY, worldGameState, inputBitUtility, worldUUID, currentRegionUUID);

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

    @Override
    public void tick() {
        spriteItem.setPosition(getLocation().getNormalizedPosition());
        textItem.setPosition(getLocation().getNormalizedPosition().add(0,1,0));
    }

    @Override
    public void renderTransparency(Renderer renderer, Camera camera) {
        renderer.renderBillboard3DSprite(camera,spriteItem,"Default3D");
        renderer.render3DElement(camera,textItem,"Default3D");
    }

    @Override
    public double getDistance(Vector3f position) {
        return getLocation().getDistance(position);
    }


    @Override
    public Location getEmissionLocation() {
        return getLocation().getOffsetLocation(0,0,0);
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean doNeedLightUpdate() {
        return true;
    }


    @Override
    public int getLightLevel() {
        return 40;
    }


}
