package com.GlitchyDev.GameStates.Abstract;

import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.GameInput.Controllers.GameController;
import com.GlitchyDev.GameStates.Actions.DebugSustainedAction;
import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.Mesh.Mesh;
import com.GlitchyDev.Rendering.Assets.Mesh.PartialCubicInstanceMesh;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.TTSVoiceManager;
import com.GlitchyDev.World.Blocks.*;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.LightableBlock;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.Abstract.TickableEffect;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.AbstractEntities.TickableEntity;
import com.GlitchyDev.World.Entities.DebugCommunicationEntity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Events.Communication.CommunicationManager;
import com.GlitchyDev.World.Events.Communication.Constructs.Enums.LanguageType;
import com.GlitchyDev.World.Events.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Events.Communication.Constructs.Source.CommunicationServerSource;
import com.GlitchyDev.World.Events.WorldEvents.WorldEventManager;
import com.GlitchyDev.World.Lighting.Abstract.LightProducer;
import com.GlitchyDev.World.Lighting.LightingManager;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Navigation.NavigableBlock;
import com.GlitchyDev.World.Region.Enum.RegionConnection;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.World;
import org.joml.Vector2d;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.*;

public class FallGameJamGameState extends ActionableGameState {
    // Managers
    protected final CommunicationManager communicationManager;
    protected final WorldEventManager worldEventManager;
    protected final LightingManager lightingManager;

    private final ArrayList<TextItem> hudItems;
    private GameController controller;
    private final UUID spawnWorld;
    private final PartialCubicInstanceMesh instancedGridMesh;

    private DebugCommunicationEntity debugCommunicationEntity;


    public FallGameJamGameState(GlobalGameData globalGameDataBase) {
        super(globalGameDataBase, GameStateType.FALL_GAME_JAM);

        this.communicationManager = new CommunicationManager();
        this.worldEventManager = new WorldEventManager();
        this.lightingManager = new LightingManager();


        CustomFontTexture customTexture = new CustomFontTexture("DebugFont");


        final int NUM_DEBUG_ITEMS = 25;
        hudItems = new ArrayList<>(NUM_DEBUG_ITEMS);
        final int debugXOffset = 0;
        final int debugYOffset = 0;
        final int debugTextYOffset = 12;
        final float scale = 1.0f;
        for(int i = 0; i < NUM_DEBUG_ITEMS; i++) {
            TextItem item = new TextItem("",customTexture);
            item.setPosition(debugXOffset,debugYOffset + i*debugTextYOffset*scale,0);
            item.setScale(scale);
            hudItems.add(item);
        }

        spawnWorld = UUID.fromString("0bca5dea-3e45-11e9-b210-d663bd873d93");

        Mesh tempMesh = new Mesh(AssetLoader.getMeshAsset("CubicMesh1"));
        instancedGridMesh = new PartialCubicInstanceMesh(tempMesh,1000,null);


        World world = new World(spawnWorld);
        addWorld(world);
        Region region1 = new Region(this,spawnWorld,10,10,10,new Location(0,0,0,spawnWorld));
        Region region2 = new Region(this,spawnWorld,10,10,10,new Location(10,0,0,spawnWorld));
        Region region3 = new Region(this,spawnWorld,10,10,10,new Location(10, 0,10,spawnWorld));
        Region region4 = new Region(this,spawnWorld,10,10,10,new Location(10, 0,-10,spawnWorld));


        System.out.println("-------------------");
        for(int x = 0; x < region1.getWidth(); x++) {
            for(int z = 0; z < region1.getLength(); z++) {
                for(int y = 0; y < region1.getHeight(); y++) {
                    Location blockLocation = region1.getLocation().getOffsetLocation(x, y, z);
                    if(y == 0) {
                        DesignerDebugBlock designerDebugBlock = new DesignerDebugBlock(this, blockLocation, region1.getRegionUUID());
                        region1.setBlockRelative(x, y, z, designerDebugBlock);
                        designerDebugBlock.setFaceState(Direction.ABOVE,true);
                        designerDebugBlock.setTextureID(Direction.ABOVE,0);
                    } else {
                        if(z == 0) {
                            DesignerDebugBlock designerDebugBlock = new DesignerDebugBlock(this, blockLocation, region1.getRegionUUID());
                            region1.setBlockRelative(x, y, z, designerDebugBlock);
                            designerDebugBlock.setFaceState(Direction.SOUTH,true);
                            designerDebugBlock.setTextureID(Direction.SOUTH,0);
                        }
                        if(z == region1.getLength()-1) {
                            DesignerDebugBlock designerDebugBlock = new DesignerDebugBlock(this, blockLocation, region1.getRegionUUID());
                            region1.setBlockRelative(x, y, z, designerDebugBlock);
                            designerDebugBlock.setFaceState(Direction.NORTH,true);
                            designerDebugBlock.setTextureID(Direction.NORTH,10);
                        }
                        if(x == 0) {
                            DesignerDebugBlock designerDebugBlock = new DesignerDebugBlock(this, blockLocation, region1.getRegionUUID());
                            region1.setBlockRelative(x, y, z, designerDebugBlock);
                            designerDebugBlock.setFaceState(Direction.EAST,true);
                            designerDebugBlock.setTextureID(Direction.EAST,11);
                        }
                    }
                }
            }
        }
        for(int x = 0; x < region2.getWidth(); x++) {
            for(int z = 0; z < region2.getLength(); z++) {
                Location blockLocation = region2.getLocation().getOffsetLocation(x,0,z);
                DebugNavigationBlock debugNavigationBlock = new DebugNavigationBlock(this,blockLocation,region2.getRegionUUID());
                region2.setBlockRelative(x,0,z,debugNavigationBlock);
            }
        }
        for(int x = 0; x < region3.getWidth(); x++) {
            for(int z = 0; z < region3.getLength(); z++) {
                Location blockLocation = region3.getLocation().getOffsetLocation(x,0,z);
                DebugNavigationBlock debugNavigationBlock = new DebugNavigationBlock(this,blockLocation,region3.getRegionUUID());
                region3.setBlockRelative(x,0,z,debugNavigationBlock);
            }
        }
        for(int x = 0; x < region4.getWidth(); x++) {
            for(int z = 0; z < region4.getLength(); z++) {
                Location blockLocation = region4.getLocation().getOffsetLocation(x,0,z);
                DebugNavigationBlock debugNavigationBlock = new DebugNavigationBlock(this,blockLocation,region4.getRegionUUID());
                region4.setBlockRelative(x,0,z,debugNavigationBlock);

            }
        }

        region1.setBlockRelative(0,0,0,new DebugCustomRenderBlock(this,region1.getLocation().getOffsetLocation(0,0,0), region1.getRegionUUID()));

        DesignerDebugBlock designerDebugBlock = new DesignerDebugBlock(this,region1.getLocation().getOffsetLocation(0,3,0), region1.getRegionUUID());
        for(Direction direction: Direction.getCompleteCardinal()) {
            designerDebugBlock.setFaceState(direction,true);
            designerDebugBlock.setTextureID(direction,10);
        }
        region1.setBlockRelative(0,3,0,designerDebugBlock);


        addRegionToGame(region1);
        addRegionToGame(region2);
        addRegionToGame(region3);
        addRegionToGame(region4);
        world.linkRegion(region1.getRegionUUID(),region2.getRegionUUID(), RegionConnection.NORMAL);

        world.linkRegion(region2.getRegionUUID(),region1.getRegionUUID(), RegionConnection.NORMAL);
        world.linkRegion(region2.getRegionUUID(),region3.getRegionUUID(), RegionConnection.VISIBLE_DEBUG_1);
        world.linkRegion(region2.getRegionUUID(),region4.getRegionUUID(), RegionConnection.HIDDEN_DEBUG_1);

        world.linkRegion(region3.getRegionUUID(),region2.getRegionUUID(), RegionConnection.NORMAL);
        world.linkRegion(region3.getRegionUUID(),region1.getRegionUUID(), RegionConnection.NORMAL);

        world.linkRegion(region4.getRegionUUID(),region2.getRegionUUID(), RegionConnection.NORMAL);
        world.linkRegion(region4.getRegionUUID(),region1.getRegionUUID(), RegionConnection.NORMAL);



        debugCommunicationEntity = new DebugCommunicationEntity(this,getRegionAtLocation(new Location(6,1,6,spawnWorld)).getRegionUUID(), new Location(6,1,6,spawnWorld), Direction.NORTH);
        spawnEntity(debugCommunicationEntity, SpawnReason.DEBUG);
    }


    private final NumberFormat formatter = new DecimalFormat("#0.00");
    @Override
    public void logic() {
        for(UUID worldUUID: getWorlds()) {
            if(lightingManager.doRequireUpdate(worldUUID)) {
                lightingManager.updateServerDynamicLighting(worldUUID,this);
            }
            getWorld(worldUUID).tick();
        }

        cameraControlsLogic();

        hudItems.get(0).setText("FPS: " + getCurrentFPS() + " WalkieTalkie: " + formatter.format(getRenderUtilization()) + " Logic: " + formatter.format(getLogicUtilization()));
        hudItems.get(1).setText("Camera Pos: " + formatter.format(getMainCamera().getPosition().x) + "," + formatter.format(getMainCamera().getPosition().y) + "," + formatter.format(getMainCamera().getPosition().z));
        hudItems.get(2).setText("Camera Rot: " + formatter.format(getMainCamera().getRotation().x) + "," + formatter.format(getMainCamera().getRotation().y) + "," + formatter.format(getMainCamera().getRotation().z));


        int startServerInfo = 6;




        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_U) == 1) {
            System.out.println("Broadcast Message");
            CommunicationMessage message = new CommunicationMessage(0, LanguageType.English,"Soma");
            communicationManager.transmitMessage(message,new Location(0,0,0,spawnWorld),new CommunicationServerSource());
        }


        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_B) == 1) {
            registerAction(new DebugSustainedAction(this));
        }


        Vector2d mousePos = new Vector2d();
        mousePos.x = gameInput.getMouseX();
        mousePos.y = gameInput.getMouseY();

        hudItems.get(19).setText(gameInput.getMouseX() + " " + gameInput.getMouseY());


        if(gameInputTimings.getActiveMouseButton2Time() == 1) {
            TTSVoiceManager.voiceSpeakAsynchronous("mbrola_us2","Legosi is my awkward adorable child I love him you better not hurt him");
        }



        ArrayList<Block> blockList = new ArrayList<>();
        for(Region region: getWorld(spawnWorld).getRegions().values()) {
            for (Block block : region.getBlocksArray()) {
                if (block instanceof AirBlock) {
                    blockList.add(block);
                }
            }
        }

        //375
        if(gameInputTimings.getActiveMouseButton1Time() == 1) {
            Block block = selectBlock2D(blockList,globalGameData.getGameWindow(),mousePos,getMainCamera());
            if (block != null) {
                System.out.println("Yo");
                debugCommunicationEntity.setLocation(block.getLocation().getAbove());
            }
        }
    }



    private void cameraControlsLogic() {
        final float CAMERA_MOVEMENT_AMOUNT = 0.3f;
        final float CAMERA_ROTATION_AMOUNT = 3.0f;
        final float JOYSTICK_THRESHOLD = 0.2f;

        if(controller != null && controller.isCurrentlyActive()) {
            if (!controller.getLeftJoyStickButton()) {
                if (controller.getLeftJoyStickY() < -JOYSTICK_THRESHOLD) {
                    getMainCamera().moveForward(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickY() > JOYSTICK_THRESHOLD) {
                    getMainCamera().moveBackwards(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickX() > JOYSTICK_THRESHOLD) {
                    getMainCamera().moveRight(controller.getLeftJoyStickX() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickX() < -JOYSTICK_THRESHOLD) {
                    getMainCamera().moveLeft(controller.getLeftJoyStickX() * CAMERA_MOVEMENT_AMOUNT);
                }
            } else {
                if (controller.getLeftJoyStickY() > JOYSTICK_THRESHOLD) {
                    getMainCamera().moveDown(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickY() < -JOYSTICK_THRESHOLD) {
                    getMainCamera().moveUp(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }

            }

            if (controller.getRightJoyStickX() > JOYSTICK_THRESHOLD || controller.getRightJoyStickX() < -JOYSTICK_THRESHOLD) {
                getMainCamera().moveRotation(0, controller.getRightJoyStickX() * CAMERA_ROTATION_AMOUNT, 0);
            }
            if (controller.getRightJoyStickY() > JOYSTICK_THRESHOLD || controller.getRightJoyStickY() < -JOYSTICK_THRESHOLD) {
                getMainCamera().moveRotation(controller.getRightJoyStickY() * CAMERA_ROTATION_AMOUNT, 0, 0);
            }


        } else {


            if(gameInput.getKeyValue(GLFW_KEY_Q) >= 1) {
                getMainCamera().moveRotation(0,-1,0);
            }
            if(gameInput.getKeyValue(GLFW_KEY_E) >= 1) {
                getMainCamera().moveRotation(0,1,0);
            }

            if(gameInput.getKeyValue(GLFW_KEY_W) >= 1) {
                getMainCamera().moveForward(1);
            }
            if(gameInput.getKeyValue(GLFW_KEY_S) >= 1) {
                getMainCamera().moveBackwards(1);
            }
            if(gameInput.getKeyValue(GLFW_KEY_D) >= 1) {
                getMainCamera().moveRight(1);
            }
            if(gameInput.getKeyValue(GLFW_KEY_A) >= 1) {
                getMainCamera().moveLeft(1);
            }
            if(gameInput.getKeyValue(GLFW_KEY_TAB) >= 1) {
                getMainCamera().moveUp(1);
            }
            if(gameInput.getKeyValue(GLFW_KEY_LEFT_SHIFT) >= 1) {
                getMainCamera().moveDown(1);
            }
        }
    }





    @Override
    public void render() {
        renderer.prepWindowRender(globalGameData.getGameWindow());
        renderer.setRenderSpace(0,0,1000,1000);
        renderEnvironment(getMainCamera(),getWorld(spawnWorld).getRegions().values(), instancedGridMesh);

        renderer.enableTransparency();
        renderer.render2DTextItems(hudItems, "Default2D");
        renderer.disableTransparency();
    }

    @Override
    public void init() {

    }

    @Override
    public void enterState(GameStateType previousGameState) {

    }

    @Override
    public void exitState(GameStateType nextGameState) {

    }

    @Override
    public void windowClose() {

    }

















    // Spawning entities triggers an viewcheck on EVERY Player in the world, the Server View should already show an update
    @Override
    public void spawnEntity(Entity entity, SpawnReason spawnReason) {
        // This is replicated, mark for Entities who can view its region
        super.spawnEntity(entity, spawnReason);

        World world = getWorld(entity.getWorldUUID());
        if(entity instanceof TickableEntity) {
            world.getTickableEntities().add((TickableEntity) entity);
        }
        for(Effect effect: entity.getCurrentEffects()) {
            if(effect instanceof TickableEffect) {
                world.getTickableEffects().add((TickableEffect) effect);
            }
        }
        if(entity instanceof LightProducer) {
            lightingManager.registerLightProducer((LightProducer) entity);
        }
        worldEventManager.triggerEntitySpawn(entity,spawnReason);
    }


    // Despawning entities triggers an viewcheck on EVERY Player in the world, the Server View should already show an update
    @Override
    public void despawnEntity(UUID entityUUID, UUID worldUUID, DespawnReason despawnReason) {
        Entity entity = getEntity(entityUUID,worldUUID);
        super.despawnEntity(entityUUID, worldUUID, despawnReason);

        World world = getWorld(entity.getWorldUUID());
        if(entity instanceof TickableEntity) {
            world.getTickableEntities().remove(entity);
        }
        for(Effect effect: entity.getCurrentEffects()) {
            if(effect instanceof TickableEffect) {
                world.getTickableEffects().remove(effect);
            }
        }
        if(entity instanceof LightProducer) {
            lightingManager.deregisterLightProducer((LightProducer) entity);
        }

        worldEventManager.triggerEntityDespawn(entity,despawnReason);
    }



    public void replicateMoveEntity(UUID entityUUID, Location oldLocation, Location newLocation) {
        // This is replicated, mark for entities who can view its region
        // Also mark if it enters and or exits Regions, update viewing

        Entity entity = getEntity(entityUUID, newLocation.getWorldUUID());
        UUID previousRegion = getRegionAtLocation(oldLocation).getRegionUUID();
        UUID newRegion = getRegionAtLocation(newLocation).getRegionUUID();

        worldEventManager.triggerEntityMove(entity,oldLocation,newLocation,previousRegion,newRegion);
    }


    // Replicate to players with this in its view
    public void replicateChangeDirectionEntity(UUID entityUUID, UUID worldUUID, Direction oldDirection, Direction newDirection) {
        // This is replicated, mark for entities who can view its region
        Entity entity = getEntity(entityUUID, worldUUID);
        worldEventManager.triggerEntityDirection(entity,oldDirection,newDirection);
    }


    private ArrayList<UUID> requireLightingUpdate = new ArrayList<>();
    public void replicateUpdatedLighting(UUID worldUUID) {
        requireLightingUpdate.add(worldUUID);
    }


    // Replicate to players with visible region, and check to make sure its not a custom visibility block
    @Override
    public void setBlock(Block block) {
        setBlock(block,getRegionAtLocation(block.getLocation()).getRegionUUID());
    }

    @Override
    public void setBlock(Block newBlock, UUID regionUUID) {
        World world = getWorld(newBlock.getWorldUUID());
        Region region = getRegion(regionUUID, newBlock.getLocation().getWorldUUID());
        Location difference = region.getLocation().getLocationDifference(newBlock.getLocation());
        Block previousBlock = region.getBlockRelative(difference);

        super.setBlock(newBlock,regionUUID);

        // Do all Server sided updates
        if (previousBlock instanceof TickableBlock) {
            world.getTickableBlocks().remove(previousBlock);
        }
        for(Effect effect: previousBlock.getCurrentEffects()) {
            if(effect instanceof TickableEffect) {
                world.getTickableEffects().remove(effect);
            }
        }
        if(previousBlock instanceof NavigableBlock) {
            world.getNavigableBlocks().remove(previousBlock);
        }
        if(previousBlock instanceof LightableBlock) {
            lightingManager.deregisterBlock((LightableBlock) previousBlock);
        }
        if(previousBlock instanceof LightProducer) {
            lightingManager.deregisterLightProducer((LightProducer) previousBlock);
        }
        //
        if (newBlock instanceof TickableBlock) {
            world.getTickableBlocks().add((TickableBlock) newBlock);
        }
        for(Effect effect: newBlock.getCurrentEffects()) {
            if(effect instanceof TickableEffect) {
                world.getTickableEffects().add((TickableEffect) effect);
            }
        }
        if(newBlock instanceof NavigableBlock) {
            world.getNavigableBlocks().add((NavigableBlock) newBlock);
        }
        if(newBlock instanceof LightableBlock) {
            lightingManager.registerBlock((LightableBlock) previousBlock);
        }
        if(newBlock instanceof LightProducer) {
            lightingManager.registerLightProducer((LightProducer) newBlock);
        }

        worldEventManager.triggerBlockChange(previousBlock, newBlock,region.getRegionUUID());
    }

    @Override
    public void addRegionToGame(Region region) {
        super.addRegionToGame(region);
        World world = getWorld(region.getWorldUUID());

        for (Entity entity : region.getEntities()) {
            if(entity instanceof TickableEntity) {
                world.getTickableEntities().add((TickableEntity) entity);
            }
            for(Effect effect: entity.getCurrentEffects()) {
                if(effect instanceof TickableEffect) {
                    world.getTickableEffects().add((TickableEffect) effect);
                }
            }
            if(entity instanceof LightProducer) {
                lightingManager.registerLightProducer((LightProducer) entity);
            }
        }

        for (Block block : region.getBlocksArray()) {
            if (block instanceof TickableBlock) {
                getWorld(region.getWorldUUID()).getTickableBlocks().add((TickableBlock) block);
            }
            for(Effect effect: block.getCurrentEffects()) {
                if(effect instanceof TickableEffect) {
                    world.getTickableEffects().add((TickableEffect) effect);
                }
            }
            if(block instanceof NavigableBlock) {
                world.getNavigableBlocks().add((NavigableBlock) block);
            }
            if(block instanceof LightableBlock) {
                lightingManager.registerBlock((LightableBlock) block);
            }
            if(block instanceof LightProducer) {
                lightingManager.registerLightProducer((LightProducer) block);
            }
        }
    }

    @Override
    public void removeRegionFromGame(UUID regionUUID, UUID worldUUID) {
        super.removeRegionFromGame(regionUUID, worldUUID);
        World world = new World(worldUUID);
        Region region = getWorld(worldUUID).getRegions().get(regionUUID);
        for (Entity entity : region.getEntities()) {
            world.getEntities().remove(entity.getUUID());
            if(entity instanceof TickableEntity) {
                world.getTickableEntities().remove(entity);
            }
            for(Effect effect: entity.getCurrentEffects()) {
                if(effect instanceof TickableEffect) {
                    world.getTickableEffects().remove(effect);
                }
            }
            if(entity instanceof LightProducer) {
                lightingManager.deregisterLightProducer((LightProducer) entity);
            }
        }
        for (Block block : region.getBlocksArray()) {
            if (block instanceof TickableBlock) {
                getWorld(worldUUID).getTickableBlocks().remove(block);
            }
            for(Effect effect: block.getCurrentEffects()) {
                if(effect instanceof TickableEffect) {
                    world.getTickableEffects().remove(effect);
                }
            }
            if(block instanceof NavigableBlock) {
                world.getNavigableBlocks().remove(block);
            }
            if(block instanceof LightableBlock) {
                lightingManager.deregisterBlock((LightableBlock) block);
            }
            if(block instanceof LightProducer) {
                lightingManager.deregisterLightProducer((LightProducer) block);
            }
        }
    }

    //
}
