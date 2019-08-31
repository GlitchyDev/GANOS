package com.GlitchyDev.GameStates.Server;

import com.GlitchyDev.Game.GameWindow;
import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.Game.Player;
import com.GlitchyDev.GameInput.Controllers.ControllerDirectionPad;
import com.GlitchyDev.GameInput.Controllers.GameController;
import com.GlitchyDev.GameInput.Controllers.XBox360Controller;
import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.GameStates.Actions.DebugSustainedAction;
import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Client.Input.ClientSendInputPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Server.World.ServerSpawnWorldPacket;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.Mesh.Mesh;
import com.GlitchyDev.Rendering.Assets.Mesh.PartialCubicInstanceMesh;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.Utility.TTSVoiceManager;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.DebugBlock;
import com.GlitchyDev.World.Blocks.DebugCustomRenderBlock;
import com.GlitchyDev.World.Blocks.DebugNavigationBlock;
import com.GlitchyDev.World.Blocks.DesignerDebugBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Effects.ServerDebugEffect;
import com.GlitchyDev.World.Entities.DebugCommunicationEntity;
import com.GlitchyDev.World.Entities.DebugEntity;
import com.GlitchyDev.World.Entities.DebugPlayerEntity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Events.Communication.Constructs.Enums.LanguageType;
import com.GlitchyDev.World.Events.Communication.Constructs.Messages.CommunicationMessage;
import com.GlitchyDev.World.Events.Communication.Constructs.Source.CommunicationServerSource;
import com.GlitchyDev.World.Events.WalkieTalkie.WalkieTalkieBase;
import com.GlitchyDev.World.Events.WalkieTalkie.WalkieTalkieDisplay;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Navigation.NavigationManager;
import com.GlitchyDev.World.Region.Enum.RegionConnection;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.World;
import com.GlitchyDev.World.WorldFileType;
import org.joml.Vector2d;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.*;

public class DebugServerGameState extends ServerWorldGameState {
    private final ArrayList<TextItem> debugItems;
    private final Player testPlayer;
    private GameController controller;

    private final UUID spawnWorld;


    private final WalkieTalkieBase walkieTalkie;
    private final DebugEntity debugEntity;

    /*
    private final SpriteItem shaderTest;
    private final Texture shaderTestPrimary;
    private final Texture shaderTestBitmap;
    private final Texture shaderTestEffect1;
    private final Texture shaderTestEffect2;
    private final Texture shaderTestEffect3;
    */

    private final PartialCubicInstanceMesh instancedGridMesh;


    public DebugServerGameState(GlobalGameData globalGameData) {
        super(globalGameData, GameStateType.DEBUG_SERVER, 5000);

        CustomFontTexture customTexture = new CustomFontTexture("DebugFont");


        final int NUM_DEBUG_ITEMS = 25;
        debugItems = new ArrayList<>(NUM_DEBUG_ITEMS);
        final int debugXOffset = 0;
        final int debugYOffset = 0;
        final int debugTextYOffset = 12;
        final float scale = 1.0f;
        for(int i = 0; i < NUM_DEBUG_ITEMS; i++) {
            TextItem item = new TextItem("",customTexture);
            item.setPosition(debugXOffset,debugYOffset + i*debugTextYOffset*scale,0);
            item.setScale(scale);
            debugItems.add(item);
        }


        spawnWorld = UUID.fromString("0bca5dea-3e45-11e9-b210-d663bd873d93");

        Mesh tempMesh = new Mesh(AssetLoader.getMeshAsset("CubicMesh1"));
        instancedGridMesh = new PartialCubicInstanceMesh(tempMesh,1000,null);




        File file = new File(System.getProperty("user.home") + "/Desktop/WorldData.world");

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



            DebugCommunicationEntity debugCommunicationEntity = new DebugCommunicationEntity(this,getRegionAtLocation(new Location(6,1,6,spawnWorld)).getRegionUUID(), new Location(6,1,6,spawnWorld), Direction.NORTH);
            spawnEntity(debugCommunicationEntity, SpawnReason.DEBUG);


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
            file.delete();
        }




        debugEntity = new DebugEntity(this,getRegionAtLocation(new Location(5,1,0,spawnWorld)).getRegionUUID(), new Location(5,1,0,spawnWorld), Direction.NORTH);
        spawnEntity(debugEntity, SpawnReason.DEBUG);

        DebugPlayerEntity playerEntity = new DebugPlayerEntity(this,getRegionAtLocation(new Location(3,1,3,spawnWorld)).getRegionUUID(), new Location(3,1,3,spawnWorld), Direction.NORTH);
        this.testPlayer = new Player(this,UUID.randomUUID(),playerEntity);
        spawnEntity(playerEntity, SpawnReason.DEBUG);
        playerEntity.recalculateView();
        playerEntity.applyEffect(new ServerDebugEffect(this));


        getMainCamera().setPosition(-10, 7, 30);
        getMainCamera().setRotation(0, 45, -7);
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


        GameWindow gameWindow = globalGameData.getGameWindow();
        gameWindow.setDimensions(1500,500);
        gameWindow.setWindowPosition(0,25);
        gameWindow.setTitle("Blackout Server");
        InputStream icon1 = AssetLoader.class.getResourceAsStream("/Textures/Icon/Icon16x16.png");
        InputStream icon2 = AssetLoader.class.getResourceAsStream("/Textures/Icon/Icon24x24.png");
        gameWindow.setIcon(icon1,icon2);
        InputStream icon3 = AssetLoader.class.getResourceAsStream("/Textures/Icon/Cursor.png");
        gameWindow.registerCursor("DebugCursor",icon3,4,4);
        gameWindow.setCursor("DebugCursor");

        walkieTalkie = new WalkieTalkieBase();
        getWorld(spawnWorld).initNavigableBlockConnections();
        lightingManager.initServerStaticLight(spawnWorld,this);
        lightingManager.updateServerDynamicLighting(spawnWorld,this);



    }


    private final NumberFormat formatter = new DecimalFormat("#0.00");
    boolean isRunning = false;
    @Override
    public void logic() {
        super.logic();
        controller.tick();





        cameraControlsLogic();

        debugItems.get(0).setText("FPS: " + getCurrentFPS() + " WalkieTalkie: " + formatter.format(getRenderUtilization()) + " Logic: " + formatter.format(getLogicUtilization()));
        debugItems.get(1).setText("Camera Pos: " + formatter.format(getMainCamera().getPosition().x) + "," + formatter.format(getMainCamera().getPosition().y) + "," + formatter.format(getMainCamera().getPosition().z));
        debugItems.get(2).setText("Camera Rot: " + formatter.format(getMainCamera().getRotation().x) + "," + formatter.format(getMainCamera().getRotation().y) + "," + formatter.format(getMainCamera().getRotation().z));
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
                serverNetworkManager.getApprovedUsers().add(UUID.fromString("187954ba-2b12-4215-9a90-f7b810797562"));
                serverNetworkManager.getApprovedUsers().add(UUID.fromString("287954ba-2b12-4215-9a90-f7b810797562"));

                isRunning = true;
            }
        }

        int valueCount = 0;
        if(currentPlayers.size() > 0) {
            valueCount = currentPlayers.get(currentPlayers.keySet().toArray()[0]).getEntityView().countEntities();
        }

        debugItems.get(11).setText("Client Entity Count " + valueCount);
        debugItems.get(12).setText("Events State " + walkieTalkie.getCurrentWalkieTalkieState());
        debugItems.get(13).setText("Events Progress " + walkieTalkie.getStateProgress());
        debugItems.get(14).setText("Events Channel " + walkieTalkie.getCurrentChannel());
        debugItems.get(15).setText("Events Muted " + walkieTalkie.isMuted());
        debugItems.get(16).setText("Events Volume " + walkieTalkie.getCurrentVolume());
        debugItems.get(17).setText("Events Battery " + walkieTalkie.getCurrentBatteryLevel());
        debugItems.get(18).setText("Events Powered " + walkieTalkie.getCurrentWalkieTalkieState().isPowered());
        debugItems.get(19).setText("Events Transition " + walkieTalkie.getTransitionProgress());
        debugItems.get(21).setText("Focused " + globalGameData.getGameWindow().isFocused());




        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_0) == 1) {
            walkieTalkie.triggerSpeaker(WalkieTalkieDisplay.CALLSIGN_CROWN, false,false);
        }
        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_1) == 1) {
            walkieTalkie.endSpeaker();
        }


        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_U) == 1) {
            System.out.println("Broadcast Message");
            CommunicationMessage message = new CommunicationMessage(0, LanguageType.English,"Soma");
            communicationManager.transmitMessage(message,new Location(0,0,0,spawnWorld),new CommunicationServerSource());
        }


        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_KP_ENTER) == 1) {
            walkieTalkie.toggleTalking();
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
            walkieTalkie.toggleMute();
        }

        if(gameInputTimings.getActiveKeyTime(GLFW_KEY_B) == 1) {
            registerAction(new DebugSustainedAction(this));
        }


        walkieTalkie.tick();

        Vector2d mousePos = new Vector2d();
        mousePos.x = gameInput.getMouseX();
        mousePos.y = gameInput.getMouseY();

        debugItems.get(19).setText(gameInput.getMouseX() + " " + gameInput.getMouseY());

        ArrayList<Block> blockList = new ArrayList<>();
        for(Region region: testPlayer.getEntityView().getViewableRegions()) {
            for (Block block : region.getBlocksArray()) {
                if (block instanceof DebugBlock) {
                    blockList.add(block);
                }
            }
        }

        //375
        if(gameInputTimings.getActiveMouseButton1Time() == 1) {
            Block block = selectBlock2D(blockList,globalGameData.getGameWindow(),mousePos,getMainCamera());
            if (block != null && block instanceof DebugBlock) {
                ((DebugBlock) block).setTestValue((((DebugBlock) block).getTestValue() + 1) % 3);
            }
        }

        if(gameInputTimings.getActiveMouseButton2Time() == 1) {
            TTSVoiceManager.voiceSpeakAsynchronous("mbrola_us2","Legosi is my awkward adorable child I love him you better not hurt him");
        }


        if(gameInputTimings.getActiveMouseButton3Time() == 1) {
            System.out.println("Advance Navigation");
            NavigationManager.debugDirectPath(this,new Location(5,0,5,spawnWorld),new Location(13,0,-3,spawnWorld),debugEntity);
        }

    }



    private void cameraControlsLogic() {
        final float CAMERA_MOVEMENT_AMOUNT = 0.3f;
        final float CAMERA_ROTATION_AMOUNT = 3.0f;
        final float JOYSTICK_THRESHOLD = 0.2f;

        if(controller != null && controller.isCurrentlyActive()) {
            if (!controller.getLeftJoyStickButton() ) {
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

            if(controller.getToggleDirectionPad() != ControllerDirectionPad.NONE) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(controller.getDirectionPad().getDirection()), EntityMovementType.WALKING);
                testPlayer.getPlayerEntity().setDirection(controller.getDirectionPad().getDirection());
            }
            if(controller.getToggleNorthButton()) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(Direction.ABOVE),EntityMovementType.WALKING);
                testPlayer.getPlayerEntity().setDirection(Direction.ABOVE);
            }
            if(controller.getToggleSouthButton()) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(Direction.BELOW),EntityMovementType.WALKING);
                testPlayer.getPlayerEntity().setDirection(Direction.BELOW);
            }
        } else {
            if(gameInput.getKeyValue(GLFW_KEY_UP) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(Direction.NORTH),EntityMovementType.WALKING);
            }
            if(gameInput.getKeyValue(GLFW_KEY_LEFT) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(Direction.WEST),EntityMovementType.WALKING);
            }
            if(gameInput.getKeyValue(GLFW_KEY_DOWN) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(Direction.SOUTH),EntityMovementType.WALKING);
            }
            if(gameInput.getKeyValue(GLFW_KEY_RIGHT) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(Direction.EAST),EntityMovementType.WALKING);
            }

            if(gameInput.getKeyValue(GLFW_KEY_SPACE) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(Direction.ABOVE),EntityMovementType.WALKING);
            }
            if(gameInput.getKeyValue(GLFW_KEY_RIGHT_SHIFT) >= 1) {
                testPlayer.getPlayerEntity().move(testPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(Direction.BELOW),EntityMovementType.WALKING);
            }

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

    // For some reason the WalkieTalkie UTIL raises to fucking 80% when the alternate hidden region is loaded, wtf????

    @Override
    public void render() {

        renderer.prepWindowRender(globalGameData.getGameWindow());
        renderer.setRenderSpace(0,0,500,500);


        renderEnvironment(getMainCamera(),testPlayer.getPlayerEntity().getEntityView().getViewableRegions(), instancedGridMesh);
        renderer.enableTransparency();
        walkieTalkie.render(renderer,renderer.getRenderWidth());
        renderer.disableTransparency();



        renderer.setRenderSpace(500,0,500,500);
        if (currentPlayers.containsKey(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562"))) {
            renderEnvironment(getMainCamera(),currentPlayers.get(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562")).getEntityView().getViewableRegions(), instancedGridMesh);
        }

        renderer.setRenderSpace(1000,0,500,500);
        renderer.enableTransparency();
        renderer.render2DTextItems(debugItems, "Default2D");
        renderer.disableTransparency();

        /*
        if (currentPlayers.containsKey(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562"))) {
            renderer.setRenderSpace(500, 0, 500, 500);
            for (Region region : currentPlayers.get(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562")).getEntityView().getViewableRegions()) {
                for (Block block : region.getBlocksArray()) {
                    if (block instanceof CustomRenderBlock) {
                        ((CustomRenderBlock) block).renderCustomBlock(renderer, camera);
                    }
                }
                for (Entity entity : region.getEntities()) {
                    entity.render(renderer, camera);
                }
            }
        }
         */


        //renderer.setRenderSpace(500,0,280,500);
        //SpriteItem spriteItem = new SpriteItem(AssetLoader.getTextureAsset("Noise"),280,500,true);
        //spriteItem.setPosition(0,0,0);
        //renderer.render2DSpriteItem(spriteItem,"Default2D");
        //spriteItem.cleanup();



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
        System.out.println("Processing packet @ " + packet);
        System.out.println();
        if(packet instanceof ClientSendInputPacket) {
            Direction direction = ((ClientSendInputPacket) packet).getClientInputType().getDirection();
            Player movingPlayer = currentPlayers.get(playerUUID);
            movingPlayer.getPlayerEntity().move(movingPlayer.getPlayerEntity().getLocation().getOffsetDirectionLocation(direction), EntityMovementType.WALKING);
        }
    }


    @Override
    public void onPlayerLogin(UUID playerUUID) {
        Region spawnRegion = getRegionAtLocation(getWorld(spawnWorld).getOriginLocation());
        DebugPlayerEntity playerEntity = new DebugPlayerEntity(this,spawnRegion.getRegionUUID(), new Location(0,1,0,spawnWorld), Direction.NORTH);
        Player loginPlayer = new Player(this,playerUUID,playerEntity);
        try {
            serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerSpawnWorldPacket(spawnWorld));
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentPlayers.put(playerUUID, loginPlayer);

        playerEntity.recalculateView();
        spawnEntity(playerEntity,SpawnReason.LOGIN);

    }

    @Override
    public void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason) {
        despawnEntity(currentPlayers.get(playerUUID).getPlayerEntity(), DespawnReason.LOGOUT);
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
                RegionConnection regionConnection = RegionConnection.values()[inputBitUtility.getNextCorrectIntByte()]; // Type
                int regionsConnected = inputBitUtility.getNextCorrectIntByte(); // Regions Held
                for(int r = 0; r < regionsConnected; r++) {
                    UUID region = inputBitUtility.getNextUUID(); // UUID of Region
                    world.linkRegion(regionUUIDs[i],region, regionConnection);
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
            region.writeData(outputBitUtility, false);
        }



        for(UUID regionUUID: world.getRegionConnections().keySet()) { // For each Region
            HashMap<RegionConnection,ArrayList<UUID>> regionConnections = world.getRegionConnections().get(regionUUID);
            outputBitUtility.writeNextCorrectByteInt(regionConnections.size()); // Write num connection types

            for(RegionConnection regionConnection : regionConnections.keySet()) {
                outputBitUtility.writeNextCorrectByteInt(regionConnection.ordinal()); // Write Type
                ArrayList<UUID> regionsConnections = regionConnections.get(regionConnection);
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
