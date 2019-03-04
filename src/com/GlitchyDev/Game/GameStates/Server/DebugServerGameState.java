package com.GlitchyDev.Game.GameStates.Server;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
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
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomRenderBlock;
import com.GlitchyDev.World.Blocks.AirBlock;
import com.GlitchyDev.World.Blocks.DebugBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.DebugPlayerEntityBase;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.Region.RegionConnectionType;
import com.GlitchyDev.World.World;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.*;

public class DebugServerGameState extends ServerWorldGameState {
    private final ArrayList<TextItem> textItems;
    private final Player testPlayer;
    private final Camera camera;
    private GameController controller;

    private final UUID spawnWorld;

    public DebugServerGameState(GlobalGameData globalGameDataBase) {
        super(globalGameDataBase, GameStateType.DEBUG_SERVER, 5000);

        CustomFontTexture customTexture = new CustomFontTexture("DebugFont");
        textItems = new ArrayList<>();
        final int NUM_TEX_ITEMS = 16;
        final int XOffset = 0;
        final int YOffset = 0;
        for(int i = 0; i < NUM_TEX_ITEMS; i++) {
            TextItem item = new TextItem("",customTexture);
            item.setPosition(XOffset,YOffset + i*12,0);
            textItems.add(item);
        }

        spawnWorld = UUID.fromString("0bca5dea-3e45-11e9-b210-d663bd873d93");
        World world = new World(spawnWorld);
        addWorld(world);
        RegionBase region1 = new RegionBase(this,spawnWorld,5,5,5,new Location(0,0,0,spawnWorld));
        RegionBase region2 = new RegionBase(this,spawnWorld,10,10,10,new Location(10,0,0,spawnWorld));
        RegionBase region3 = new RegionBase(this,spawnWorld,10,10,10,new Location(10, 0,10,spawnWorld));


        System.out.println("-------------------");
        for(int x = 0; x < region1.getWidth(); x++) {
            for(int z = 0; z < region1.getLength(); z++) {
                BlockBase block = region1.getBlockRelative(x,0,z);
                Location relativeLocation = region1.getLocation().getLocationDifference(block.getLocation());
                region1.setBlockRelative(relativeLocation,new DebugBlock(this,block.getLocation(),5));
            }
        }

        try {
            System.out.println("Load and read Regions");
            File file = new File(System.getProperty("user.home") + "/Desktop/Test.crp");
            OutputBitUtility fileOutputBitUtility = new OutputBitUtility(file);
            region1.writeData(fileOutputBitUtility);
            fileOutputBitUtility.close();
            InputBitUtility inputBitUtility = new InputBitUtility(file);
            region1 = new RegionBase(inputBitUtility, new Location(spawnWorld),this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int y = 0; y < region1.getHeight(); y++) {
            for(int z = 0; z < region1.getLength(); z++) {
                for(int x = 0; x < region1.getWidth(); x++) {
                    System.out.print(!(region1.getBlockRelative(x,y,z) instanceof AirBlock) ? "1" : "0");
                }
                System.out.println();
            }
        }

        for(int x = 0; x < region2.getWidth(); x++) {
            for(int z = 0; z < region2.getLength(); z++) {
                BlockBase block = region2.getBlockRelative(x,0,z);
                Location relativeLocation = region2.getLocation().getLocationDifference(block.getLocation());
                region2.setBlockRelative(relativeLocation,new DebugBlock(this,block.getLocation(),2));
            }
        }
        for(int x = 0; x < region3.getWidth(); x++) {
            for(int z = 0; z < region3.getLength(); z++) {
                BlockBase block = region3.getBlockRelative(x,0,z);
                Location relativeLocation = region3.getLocation().getLocationDifference(block.getLocation());
                region3.setBlockRelative(relativeLocation,new DebugBlock(this,block.getLocation(),3));
            }
        }


        addRegionToGame(region1);
        addRegionToGame(region2);
        addRegionToGame(region3);
        world.linkRegion(region1.getRegionUUID(),region2.getRegionUUID(), RegionConnectionType.NORMAL);
        world.linkRegion(region2.getRegionUUID(),region1.getRegionUUID(), RegionConnectionType.NORMAL);
        world.linkRegion(region2.getRegionUUID(),region3.getRegionUUID(), RegionConnectionType.NORMAL);
        world.linkRegion(region3.getRegionUUID(),region2.getRegionUUID(), RegionConnectionType.NORMAL);
        world.linkRegion(region3.getRegionUUID(),region1.getRegionUUID(), RegionConnectionType.NORMAL);

        DebugPlayerEntityBase playerEntity = new DebugPlayerEntityBase(this,region1.getRegionUUID(), new Location(0,1,0,spawnWorld), Direction.NORTH);
        this.testPlayer = new Player(this,UUID.randomUUID(),playerEntity);
        spawnEntity(playerEntity);
        playerEntity.recalculateView();

        camera = new Camera();
        camera.setPosition(-8.5f, 10f, -6f);
        camera.setRotation(5f, 122f, -0f);
        controller = new XBox360Controller(0);


    }


    private final NumberFormat formatter = new DecimalFormat("#0.00");
    boolean isRunning = false;
    @Override
    public void logic() {
        super.logic();
        controller.tick();

        cameraControlsLogic();

        textItems.get(0).setText("FPS: " + getCurrentFPS() + " Logic Util: " + formatter.format(getLogicUtilization()) + " Render Util: " + formatter.format(getRenderUtilization()));
        textItems.get(1).setText("Camera Pos: " + formatter.format(camera.getPosition().x) + "," + formatter.format(camera.getPosition().y) + "," + formatter.format(camera.getPosition().z));
        textItems.get(2).setText("Camera Rot: " + formatter.format(camera.getRotation().x) + "," + formatter.format(camera.getRotation().y) + "," + formatter.format(camera.getRotation().z));
        textItems.get(3).setText("P: " + countRegionsAtLocation(testPlayer.getPlayerEntity().getLocation()));

        int startServerInfo = 5;
        if(isRunning) {
            textItems.get(startServerInfo).setText("Connected Players");
            for(int i = startServerInfo + 1; i < textItems.size(); i++) {
                textItems.get(i).setText("");
            }
            int i = startServerInfo;
            for (UUID uuid : serverNetworkManager.getConnectedUsers()) {
                textItems.get(i).setText("Player " + i + ": " + uuid);
                i++;
            }
        } else {
            textItems.get(startServerInfo).setText("Not Online");
            if(controller.isCurrentlyActive() && controller.getToggleLeftHomeButton() || gameInput.getKeyValue(GLFW_KEY_C) >= 1) {
                System.out.println("Starting up Server");
                serverNetworkManager.enableAcceptingClients(813);
                serverNetworkManager.getApprovedUsers().add(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562"));
                isRunning = true;
            }
        }

        testPlayer.getPlayerEntity().tick();
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


    @Override
    public void render() {
        renderer.prepRender(globalGameData.getGameWindow());
        for(RegionBase region: testPlayer.getPlayerEntity().getEntityView().getViewableRegions()) {
            for(BlockBase block: region.getBlocksArray()) {
                if(block instanceof CustomRenderBlock) {
                    ((CustomRenderBlock) block).render(renderer,globalGameData.getGameWindow(),camera,testPlayer);
                }
            }
            for(EntityBase entity: region.getEntities()) {
                entity.render(renderer,globalGameData.getGameWindow(),camera);
            }
        }
        renderer.renderHUD(globalGameData.getGameWindow(),"Default2D",textItems);



    }

    @Override
    public void processPacket(UUID playerUUID, PacketBase packet) {
        //System.out.println(packet);
        if(packet instanceof ClientSendInputPacket) {
            Direction direction = ((ClientSendInputPacket) packet).getClientInputType().getDirection();
            Player movingPlayer = currentPlayers.get(playerUUID);
            movingPlayer.getPlayerEntity().move(movingPlayer.getPlayerEntity().getLocation().getDirectionLocation(direction), EntityMovementType.WALKING);
            //System.out.println("Client moved " + direction);
        }
    }

    @Override
    public void onPlayerLogin(UUID playerUUID) {
        RegionBase spawnRegion = getRegionAtLocation(getWorld(spawnWorld).getOriginLocation());
        DebugPlayerEntityBase playerEntity = new DebugPlayerEntityBase(this,spawnRegion.getRegionUUID(), new Location(0,1,0,spawnWorld), Direction.NORTH);
        Player loginPlayer = new Player(this,playerUUID,playerEntity);
        try {
            serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerSpawnWorldPacket(spawnWorld));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPlayers.put(playerUUID,loginPlayer);
        spawnEntity(playerEntity);
        playerEntity.recalculateView();
    }

    @Override
    public void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason) {
        System.out.println("DebugServer: User " + playerUUID + " disconnected for " + reason);
        despawnEntity(currentPlayers.get(playerUUID).getPlayerEntity().getUUID(), currentPlayers.get(playerUUID).getPlayerEntity().getWorldUUID());
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
}
