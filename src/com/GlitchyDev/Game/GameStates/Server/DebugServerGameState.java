package com.GlitchyDev.Game.GameStates.Server;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.GameInput.Controllers.ControllerDirectionPad;
import com.GlitchyDev.GameInput.Controllers.GameController;
import com.GlitchyDev.GameInput.Controllers.XBox360Controller;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Client.Input.ClientSendInputPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Server.World.ServerSpawnWorldPacket;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomRenderBlock;
import com.GlitchyDev.World.Blocks.DebugBlock;
import com.GlitchyDev.World.Blocks.DebugCustomRenderBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Elements.WalkieTalkie.Enums.WalkieTalkieDisplay;
import com.GlitchyDev.World.Elements.WalkieTalkie.WalkieTalkieBase;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.DebugEntity;
import com.GlitchyDev.World.Entities.DebugPlayerEntityBase;
import com.GlitchyDev.World.Entities.Effects.ServerDebugEffect;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.Region.RegionConnectionType;
import com.GlitchyDev.World.World;
import com.GlitchyDev.World.WorldFileType;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.*;

public class DebugServerGameState extends ServerWorldGameState {
    private final ArrayList<TextItem> hudItems;
    private final ArrayList<TextItem> debugItems;
    private final Player testPlayer;
    private final Camera camera;
    private GameController controller;

    private final UUID spawnWorld;


    private final WalkieTalkieBase walkieTalkie;

    /*
    private final SpriteItem shaderTest;
    private final Texture shaderTestPrimary;
    private final Texture shaderTestBitmap;
    private final Texture shaderTestEffect1;
    private final Texture shaderTestEffect2;
    private final Texture shaderTestEffect3;
    */



    public DebugServerGameState(GlobalGameData globalGameData) {
        super(globalGameData, GameStateType.DEBUG_SERVER, 5000);

        CustomFontTexture customTexture = new CustomFontTexture("DebugFont");
        final int NUM_HUD_ITEMS = 20;
        hudItems = new ArrayList<>(NUM_HUD_ITEMS);
        final int hudXOffset = 0;
        final int hudYOffset = 0;
        final int hudTextYOffset = 12;
        for(int i = 0; i < NUM_HUD_ITEMS; i++) {
            TextItem item = new TextItem("",customTexture);
            item.setPosition(hudXOffset,hudYOffset + i*hudTextYOffset,0);
            hudItems.add(item);
        }

        final int NUM_DEBUG_ITEMS = 20;
        debugItems = new ArrayList<>(NUM_DEBUG_ITEMS);
        final int debugXOffset = 0;
        final int debugYOffset = 0;
        final int debugTextYOffset = 12;
        for(int i = 0; i < NUM_DEBUG_ITEMS; i++) {
            TextItem item = new TextItem("",customTexture);
            item.setPosition(debugXOffset,debugYOffset + i*debugTextYOffset,0);
            debugItems.add(item);
        }


        spawnWorld = UUID.fromString("0bca5dea-3e45-11e9-b210-d663bd873d93");






        File file = new File(System.getProperty("user.home") + "/Desktop/WorldData.crp");

        if(!file.exists()) {
            World world = new World(spawnWorld);
            addWorld(world);
            Region region1 = new Region(this,spawnWorld,10,10,10,new Location(0,0,0,spawnWorld));
            Region region2 = new Region(this,spawnWorld,10,10,10,new Location(10,0,0,spawnWorld));
            Region region3 = new Region(this,spawnWorld,10,10,10,new Location(10, 0,10,spawnWorld));
            Region region4 = new Region(this,spawnWorld,10,10,10,new Location(10, 0,-10,spawnWorld));


            System.out.println("-------------------");
            for(int x = 0; x < region1.getWidth(); x++) {
                for(int z = 0; z < region1.getLength(); z++) {
                    BlockBase block = region1.getBlockRelative(x,0,z);
                    Location relativeLocation = region1.getLocation().getLocationDifference(block.getLocation());
                    region1.setBlockRelative(relativeLocation,new DebugBlock(this,block.getLocation(),(int)(3 * Math.random()) ));
                }
            }
            for(int x = 0; x < region2.getWidth(); x++) {
                for(int z = 0; z < region2.getLength(); z++) {
                    BlockBase block = region2.getBlockRelative(x,0,z);
                    Location relativeLocation = region2.getLocation().getLocationDifference(block.getLocation());
                    region2.setBlockRelative(relativeLocation,new DebugBlock(this,block.getLocation(),(int)(3 * Math.random()) ));
                }
            }
            for(int x = 0; x < region3.getWidth(); x++) {
                for(int z = 0; z < region3.getLength(); z++) {
                    BlockBase block = region3.getBlockRelative(x,0,z);
                    Location relativeLocation = region3.getLocation().getLocationDifference(block.getLocation());
                    region3.setBlockRelative(relativeLocation,new DebugBlock(this,block.getLocation(),(int)(3 * Math.random()) ));
                }
            }
            for(int x = 0; x < region4.getWidth(); x++) {
                for(int z = 0; z < region4.getLength(); z++) {
                    BlockBase block = region4.getBlockRelative(x,0,z);
                    Location relativeLocation = region4.getLocation().getLocationDifference(block.getLocation());
                    region4.setBlockRelative(relativeLocation,new DebugBlock(this,block.getLocation(),(int)(3 * Math.random()) ));
                }
            }

            region1.setBlockRelative(0,0,0,new DebugCustomRenderBlock(this,region1.getLocation().getOffsetLocation(0,0,0)));


            addRegionToGame(region1);
            addRegionToGame(region2);
            addRegionToGame(region3);
            addRegionToGame(region4);
            world.linkRegion(region1.getRegionUUID(),region2.getRegionUUID(), RegionConnectionType.NORMAL);

            world.linkRegion(region2.getRegionUUID(),region1.getRegionUUID(), RegionConnectionType.NORMAL);
            world.linkRegion(region2.getRegionUUID(),region3.getRegionUUID(), RegionConnectionType.VISIBLE_DEBUG_1);
            world.linkRegion(region2.getRegionUUID(),region4.getRegionUUID(), RegionConnectionType.HIDDEN_DEBUG_1);

            world.linkRegion(region3.getRegionUUID(),region2.getRegionUUID(), RegionConnectionType.NORMAL);
            world.linkRegion(region3.getRegionUUID(),region1.getRegionUUID(), RegionConnectionType.NORMAL);

            world.linkRegion(region4.getRegionUUID(),region2.getRegionUUID(), RegionConnectionType.NORMAL);
            world.linkRegion(region4.getRegionUUID(),region1.getRegionUUID(), RegionConnectionType.NORMAL);


            DebugEntity debugEntity = new DebugEntity(this,getRegionAtLocation(new Location(5,1,0,spawnWorld)).getRegionUUID(), new Location(5,1,0,spawnWorld), Direction.NORTH);
            spawnEntity(debugEntity, SpawnReason.DEBUG);
            try {
                saveWorld(file, world);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                loadWorld(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.deleteOnExit();
        }





        DebugPlayerEntityBase playerEntity = new DebugPlayerEntityBase(this,getRegionAtLocation(new Location(0,1,0,spawnWorld)).getRegionUUID(), new Location(0,1,0,spawnWorld), Direction.NORTH);
        this.testPlayer = new Player(this,UUID.randomUUID(),playerEntity);
        spawnEntity(playerEntity, SpawnReason.DEBUG);
        playerEntity.recalculateView();
        playerEntity.getEffects().add(new ServerDebugEffect(this,playerEntity));

        camera = new Camera();
        camera.setPosition(-8.5f, 7f, -6f);
        camera.setRotation(5f, 122f, -0f);
        controller = new XBox360Controller(0);


        /*
        shaderTestPrimary = AssetLoader.getTextureAsset("WalkieTalkie_Body");
        shaderTestBitmap = AssetLoader.getTextureAsset("Terrob_Effect_Bitmap");
        shaderTestEffect1 = AssetLoader.getTextureAsset("GlitchEffect");
        shaderTestEffect2 = AssetLoader.getTextureAsset("Noise");
        shaderTestEffect3 = AssetLoader.getTextureAsset("DefaultTexture");
        shaderTest = new SpriteItem(shaderTestPrimary,true);
        shaderTest.setPosition(0,0,0);
        shaderTest.setScale(2);
        */


        globalGameData.getGameWindow().setDimensions(1280,500);
        globalGameData.getGameWindow().centerWindow();
        globalGameData.getGameWindow().adjustWindowPosition(0,-240);
        globalGameData.getGameWindow().setTitle("Blackout Server");

        walkieTalkie = new WalkieTalkieBase();



    }


    private final NumberFormat formatter = new DecimalFormat("#0.00");
    boolean isRunning = false;
    @Override
    public void logic() {
        super.logic();
        controller.tick();

        cameraControlsLogic();

        debugItems.get(0).setText("FPS: " + getCurrentFPS() + " Render: " + formatter.format(getRenderUtilization()) + " Logic: " + formatter.format(getLogicUtilization()));
        debugItems.get(1).setText("Camera Pos: " + formatter.format(camera.getPosition().x) + "," + formatter.format(camera.getPosition().y) + "," + formatter.format(camera.getPosition().z));
        debugItems.get(2).setText("Camera Rot: " + formatter.format(camera.getRotation().x) + "," + formatter.format(camera.getRotation().y) + "," + formatter.format(camera.getRotation().z));
        debugItems.get(3).setText("P: " + getRegionAtLocation(testPlayer.getPlayerEntity().getLocation()).getEntities().size());
        debugItems.get(4).setText("BlockType " + getBlockAtLocation(testPlayer.getPlayerEntity().getLocation()).getBlockType());

        int startServerInfo = 6;
        if(isRunning) {
            debugItems.get(startServerInfo).setText("Connected Players");
            for(int i = startServerInfo + 1; i < debugItems.size(); i++) {
                debugItems.get(i).setText("");
            }
            int i = startServerInfo;
            for (UUID uuid : serverNetworkManager.getConnectedUsers()) {
                debugItems.get(i).setText("Player " + i + ": " + uuid);
                i++;
            }
        } else {
            debugItems.get(startServerInfo).setText("Not Online");
            if(controller.isCurrentlyActive() && controller.getToggleLeftHomeButton() || gameInput.getKeyValue(GLFW_KEY_C) >= 1) {
                System.out.println("Starting up Server");
                serverNetworkManager.enableAcceptingClients(813);
                serverNetworkManager.getApprovedUsers().add(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562"));
                isRunning = true;
            }
        }

        int valueCount = 0;
        if(currentPlayers.size() > 0) {
            valueCount = currentPlayers.get(currentPlayers.keySet().toArray()[0]).getEntityView().countEntities();
        }

        debugItems.get(11).setText("Client Entity Count " + valueCount);
        debugItems.get(12).setText("WalkieTalkie State " + walkieTalkie.getCurrentWalkieTalkieState());
        debugItems.get(13).setText("WalkieTalkie Progress " + walkieTalkie.getStateProgress());
        debugItems.get(14).setText("WalkieTalkie Channel " + walkieTalkie.getCurrentChannel());
        debugItems.get(15).setText("WalkieTalkie Muted " + walkieTalkie.isMuted());
        debugItems.get(16).setText("WalkieTalkie Volume " + walkieTalkie.getCurrentVolume());
        debugItems.get(17).setText("WalkieTalkie Battery " + walkieTalkie.getCurrentBatteryLevel());
        debugItems.get(18).setText("WalkieTalkie Powered " + walkieTalkie.getCurrentWalkieTalkieState().isPowered());
        debugItems.get(19).setText("WalkieTalkie Transition " + walkieTalkie.getTransitionProgress());



        testPlayer.getPlayerEntity().tick();
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_0) == 1) {
            walkieTalkie.triggerSpeaker(WalkieTalkieDisplay.CALLSIGN_CROWN, false,false);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_1) == 1) {
            walkieTalkie.endSpeaker();
        }



        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_ENTER) == 1) {
            walkieTalkie.toggleMute();
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_ADD) == 1) {
            walkieTalkie.increaseVolume();
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_SUBTRACT) == 1) {
            walkieTalkie.decreaseVolume();
        }

        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_DIVIDE) == 1) {
            walkieTalkie.togglePower();
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_MULTIPLY) == 1) {
            walkieTalkie.showBattery();
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_NUM_LOCK) == 1) {
            walkieTalkie.togglePullUpWalkieTalkie();
        }

        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_0) == 1) {
            walkieTalkie.changeChannel(0);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_1) == 1) {
            walkieTalkie.changeChannel(1);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_2) == 1) {
            walkieTalkie.changeChannel(2);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_3) == 1) {
            walkieTalkie.changeChannel(3);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_4) == 1) {
            walkieTalkie.changeChannel(4);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_5) == 1) {
            walkieTalkie.changeChannel(5);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_6) == 1) {
            walkieTalkie.changeChannel(6);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_7) == 1) {
            walkieTalkie.changeChannel(7);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_8) == 1) {
            walkieTalkie.changeChannel(8);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_9) == 1) {
            walkieTalkie.changeChannel(9);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_DECIMAL) == 1) {
            walkieTalkie.changeChannel(-13);
        }

        walkieTalkie.tick();

    }


    private void cameraControlsLogic() {
        final float CAMERA_MOVEMENT_AMOUNT = 0.3f;
        final float CAMERA_ROTATION_AMOUNT = 3.0f;
        final float JOYSTICK_THRESHOLD = 0.2f;

        if(controller != null && controller.isCurrentlyActive()) {
            if (!controller.getLeftJoyStickButton() ) {
                if (controller.getLeftJoyStickY() < -JOYSTICK_THRESHOLD) {
                    camera.moveForward(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickY() > JOYSTICK_THRESHOLD) {
                    camera.moveBackwards(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickX() > JOYSTICK_THRESHOLD) {
                    camera.moveRight(controller.getLeftJoyStickX() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickX() < -JOYSTICK_THRESHOLD) {
                    camera.moveLeft(controller.getLeftJoyStickX() * CAMERA_MOVEMENT_AMOUNT);
                }
            } else {
                if (controller.getLeftJoyStickY() > JOYSTICK_THRESHOLD) {
                    camera.moveDown(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickY() < -JOYSTICK_THRESHOLD) {
                    camera.moveUp(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }

            }

            if (controller.getRightJoyStickX() > JOYSTICK_THRESHOLD || controller.getRightJoyStickX() < -JOYSTICK_THRESHOLD) {
                camera.moveRotation(0, controller.getRightJoyStickX() * CAMERA_ROTATION_AMOUNT, 0);
            }
            if (controller.getRightJoyStickY() > JOYSTICK_THRESHOLD || controller.getRightJoyStickY() < -JOYSTICK_THRESHOLD) {
                camera.moveRotation(controller.getRightJoyStickY() * CAMERA_ROTATION_AMOUNT, 0, 0);
            }

            if(controller.getToggleDirectionPad() != ControllerDirectionPad.NONE) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(controller.getDirectionPad().getDirection()), EntityMovementType.WALKING);
            }
            if(controller.getToggleNorthButton()) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(Direction.ABOVE),EntityMovementType.WALKING);
            }
            if(controller.getToggleSouthButton()) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(Direction.BELOW),EntityMovementType.WALKING);
            }
        } else {
            if(gameInput.getKeyValue(GLFW_KEY_UP) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(Direction.NORTH),EntityMovementType.WALKING);
            }
            if(gameInput.getKeyValue(GLFW_KEY_LEFT) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(Direction.EAST),EntityMovementType.WALKING);
            }
            if(gameInput.getKeyValue(GLFW_KEY_DOWN) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(Direction.SOUTH),EntityMovementType.WALKING);
            }
            if(gameInput.getKeyValue(GLFW_KEY_RIGHT) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(Direction.WEST),EntityMovementType.WALKING);
            }

            if(gameInput.getKeyValue(GLFW_KEY_SPACE) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(Direction.ABOVE),EntityMovementType.WALKING);
            }
            if(gameInput.getKeyValue(GLFW_KEY_RIGHT_SHIFT) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getDirectionLocation(Direction.BELOW),EntityMovementType.WALKING);
            }
        }



    }

    // For some reason the Render UTIL raises to fucking 80% when the alternate hidden region is loaded, wtf????

    @Override
    public void render() {
        renderer.prepWindowRender(globalGameData.getGameWindow());
        renderer.setRenderSpace(0,0,500,500);
        for(Region region: testPlayer.getPlayerEntity().getEntityView().getViewableRegions()) {
            for(BlockBase block: region.getBlocksArray()) {
                if(block instanceof CustomRenderBlock) {
                    ((CustomRenderBlock) block).render(renderer,camera,testPlayer);
                }
            }
            for(EntityBase entity: region.getEntities()) {
                entity.render(renderer,camera);
            }
        }
        renderer.renderHUD(hudItems, "Default2D");
        walkieTalkie.render(renderer,500);

        if(currentPlayers.size() > 0) {
            renderer.setRenderSpace(500,0,500,500);
            for(Region region: currentPlayers.get(currentPlayers.keySet().toArray()[0]).getEntityView().getViewableRegions()) {
                for(BlockBase block: region.getBlocksArray()) {
                    if(block instanceof CustomRenderBlock) {
                        ((CustomRenderBlock) block).render(renderer,camera,testPlayer);
                    }
                }
                for(EntityBase entity: region.getEntities()) {
                    entity.render(renderer,camera);
                }
            }
        }

        renderer.setRenderSpace(1000,0,500,500);
        renderer.renderHUD(debugItems, "Default2D");


        /*
        renderer.getShader("DebugShader2D").bind();
        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, shaderTestBitmap.getId());
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, shaderTestEffect1.getId());
        glActiveTexture(GL_TEXTURE3);
        glBindTexture(GL_TEXTURE_2D, shaderTestEffect2.getId());
        glActiveTexture(GL_TEXTURE4);
        glBindTexture(GL_TEXTURE_2D, shaderTestEffect3.getId());
        renderer.getShader("DebugShader2D").setUniform("bitmap", 1);
        renderer.getShader("DebugShader2D").setUniform("effect1", 2);
        renderer.getShader("DebugShader2D").setUniform("effect2", 3);
        renderer.getShader("DebugShader2D").setUniform("effect3", 4);
        i++;
        i %= 600;
        float framePercent = i / 600.0f;
        renderer.getShader("DebugShader2D").setUniform("frame", framePercent);
        System.out.println(i);
        shaderTest.setPosition((float)gameInput.getMouseX(),(float)gameInput.getMouseY(),0);
        renderer.render2DSprite(shaderTest, "DebugShader2D");
        */



    }

    //int i = 0;

    @Override
    public void processPacket(UUID playerUUID, PacketBase packet) {
        System.out.println();
        System.out.println("Received packet " + packet);
        System.out.println();
        if(packet instanceof ClientSendInputPacket) {
            Direction direction = ((ClientSendInputPacket) packet).getClientInputType().getDirection();
            Player movingPlayer = currentPlayers.get(playerUUID);
            movingPlayer.getPlayerEntity().move(movingPlayer.getPlayerEntity().getLocation().getDirectionLocation(direction), EntityMovementType.WALKING);
        }
    }

    @Override
    public void onPlayerLogin(UUID playerUUID) {
        Region spawnRegion = getRegionAtLocation(getWorld(spawnWorld).getOriginLocation());
        DebugPlayerEntityBase playerEntity = new DebugPlayerEntityBase(this,spawnRegion.getRegionUUID(), new Location(0,1,0,spawnWorld), Direction.NORTH);
        Player loginPlayer = new Player(this,playerUUID,playerEntity);
        try {
            serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerSpawnWorldPacket(spawnWorld));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPlayers.put(playerUUID,loginPlayer);
        playerEntity.recalculateView();
        spawnEntity(playerEntity, SpawnReason.LOGIN);
    }

    @Override
    public void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason) {
        System.out.println("DebugServer: User " + playerUUID + " disconnected for " + reason);
        despawnEntity(currentPlayers.get(playerUUID).getPlayerEntity().getUUID(), currentPlayers.get(playerUUID).getPlayerEntity().getWorldUUID(), DespawnReason.LOGOUT);
        currentPlayers.remove(playerUUID);
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
        try {
            serverNetworkManager.disconnectAll(NetworkDisconnectType.SERVER_CLOSE_WINDOW);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void loadWorld(File file) throws IOException {
        System.out.println("DebugServerGameState: Loading world file " + file.getName());
        InputBitUtility inputBitUtility = new InputBitUtility(file);


        WorldFileType worldFileType = WorldFileType.values()[inputBitUtility.getNextCorrectIntByte()];

        UUID worldUUID = inputBitUtility.getNextUUID();
        World world = new World(worldUUID);
        addWorld(world);

        int numRegions = inputBitUtility.getNextCorrectIntByte();
        UUID[] regionUUIDs = new UUID[numRegions];
        for(int i = 0; i < numRegions; i++) {
            Location location = new Location(inputBitUtility.getNextInteger(), inputBitUtility.getNextInteger(), inputBitUtility.getNextInteger(), worldUUID);
            Region region = new Region(inputBitUtility, location, this);
            regionUUIDs[i] = region.getRegionUUID();
            addRegionToGame(region);
        }


        for(int i = 0; i < numRegions; i++) { // For Each Region
            int connectionTypeCount = inputBitUtility.getNextCorrectIntByte(); // Num Connection Types
            for(int c = 0; c < connectionTypeCount; c++) {
                RegionConnectionType regionConnectionType = RegionConnectionType.values()[inputBitUtility.getNextCorrectIntByte()]; // Type
                int regionsConnected = inputBitUtility.getNextCorrectIntByte(); // Regions Held
                for(int r = 0; r < regionsConnected; r++) {
                    UUID region = inputBitUtility.getNextUUID(); // UUID of Region
                    world.linkRegion(regionUUIDs[i],region, regionConnectionType);
                }

            }
        }
    }

    private void saveWorld(File file, World world) throws IOException {
        System.out.println("DebugServerGameState: Saving world " + world.getWorldUUID() + " to file " + file.getName());
        OutputBitUtility outputBitUtility = new OutputBitUtility(file);

        System.out.println("Writing Value " + WorldFileType.NORMAL.ordinal());
        outputBitUtility.writeNextCorrectByteInt(WorldFileType.NORMAL.ordinal());

        outputBitUtility.writeNextUUID(world.getWorldUUID());

        int numRegions = world.getRegions().size();
        outputBitUtility.writeNextCorrectByteInt(numRegions);
        for(UUID regionUUID: world.getRegions().keySet()) {
            Region region = world.getRegions().get(regionUUID);
            Location location = region.getLocation();

            outputBitUtility.writeNextInteger(location.getX());
            outputBitUtility.writeNextInteger(location.getY());
            outputBitUtility.writeNextInteger(location.getZ());
            region.writeData(outputBitUtility);
        }



        for(UUID regionUUID: world.getRegionConnections().keySet()) { // For each Region
            HashMap<RegionConnectionType,ArrayList<UUID>> regionConnections = world.getRegionConnections().get(regionUUID);
            outputBitUtility.writeNextCorrectByteInt(regionConnections.size()); // Write num connection types

            for(RegionConnectionType regionConnectionType: regionConnections.keySet()) {
                outputBitUtility.writeNextCorrectByteInt(regionConnectionType.ordinal()); // Write Type
                ArrayList<UUID> regionsConnections = regionConnections.get(regionConnectionType);
                outputBitUtility.writeNextCorrectByteInt(regionsConnections.size()); // Write Size
                for(UUID uuid: regionsConnections) { // Write UUIDs
                    outputBitUtility.writeNextUUID(uuid);
                }
            }

        }


        outputBitUtility.close();
    }

    public Player getTestPlayer() {
        return testPlayer;
    }
}
