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
import com.GlitchyDev.Rendering.Assets.Texture.Texture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomRenderBlock;
import com.GlitchyDev.World.Blocks.DebugBlock;
import com.GlitchyDev.World.Blocks.DebugCustomRenderBlock;
import com.GlitchyDev.World.Direction;
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
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;

public class DebugServerGameState extends ServerWorldGameState {
    private final ArrayList<TextItem> textItems;
    private final Player testPlayer;
    private final Camera camera;
    private GameController controller;

    private final UUID spawnWorld;

    private final SpriteItem shaderTest;
    private final Texture shaderTex;



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
        camera.setPosition(-8.5f, 10f, -6f);
        camera.setRotation(5f, 122f, -0f);
        controller = new XBox360Controller(0);

        shaderTest = new SpriteItem(AssetLoader.getTextureAsset("Standing_Mirror"),true);
        shaderTest.setPosition(0,0,0);
        shaderTex = AssetLoader.getTextureAsset("Standing_Mirror_Bitmap");

        globalGameDataBase.getGameWindow().setDimensions(500,500);
        globalGameDataBase.getGameWindow().centerWindow();
        globalGameDataBase.getGameWindow().setTitle("Blackout Server");




    }


    private final NumberFormat formatter = new DecimalFormat("#0.00");
    boolean isRunning = false;
    @Override
    public void logic() {
        super.logic();
        controller.tick();

        cameraControlsLogic();

        textItems.get(0).setText("FPS: " + getCurrentFPS() + " Render: " + formatter.format(getRenderUtilization()) + " Logic: " + formatter.format(getLogicUtilization()));
        textItems.get(1).setText("Camera Pos: " + formatter.format(camera.getPosition().x) + "," + formatter.format(camera.getPosition().y) + "," + formatter.format(camera.getPosition().z));
        textItems.get(2).setText("Camera Rot: " + formatter.format(camera.getRotation().x) + "," + formatter.format(camera.getRotation().y) + "," + formatter.format(camera.getRotation().z));
        textItems.get(3).setText("P: " + getRegionAtLocation(testPlayer.getPlayerEntity().getLocation()).getEntities().size());
        textItems.get(4).setText("BlockType " + getBlockAtLocation(testPlayer.getPlayerEntity().getLocation()).getBlockType());

        int startServerInfo = 6;
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
        renderer.renderHUD(textItems, "Default2D");


        renderer.getShader("DebugShader2D").bind();

        glActiveTexture(GL_TEXTURE1);
        glBindTexture(GL_TEXTURE_2D, shaderTex.getId());
        renderer.getShader("DebugShader2D").setUniform("test_texture", 1);
        shaderTest.setPosition((float)gameInput.getMouseX(),(float)gameInput.getMouseY(),0);
        renderer.render2DSprite(shaderTest, "DebugShader2D");



    }

    @Override
    public void processPacket(UUID playerUUID, PacketBase packet) {
        System.out.println();
        System.out.println("Recieved packet " + packet);
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
