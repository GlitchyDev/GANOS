package com.GlitchyDev.Game.GameStates.Client;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ClientWorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.GameInput.Controllers.ControllerDirectionPad;
import com.GlitchyDev.GameInput.Controllers.GameController;
import com.GlitchyDev.GameInput.Controllers.XBox360Controller;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Client.Input.ClientInputType;
import com.GlitchyDev.Networking.Packets.Client.Input.ClientSendInputPacket;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomRenderBlock;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.World;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;

public class DebugClientGameState extends ClientWorldGameState {
    private final ArrayList<TextItem> textItems;
    private boolean isConnected = false;
    private GameController controller;
    private Camera camera;

    public DebugClientGameState(GlobalGameData globalGameDataBase) {
        super(globalGameDataBase, GameStateType.DEBUG_CLIENT);

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

        controller = new XBox360Controller(0);

        camera = new Camera();
        camera.setPosition(-8.5f, 7f, -6f);
        camera.setRotation(5f, 122f, -0f);

    }


    private final NumberFormat formatter = new DecimalFormat("#0.00");
    @Override
    public void logic() {
        super.logic();
        controller.tick();
        cameraControlsLogic();

        textItems.get(0).setText("FPS: " + getCurrentFPS() + " Logic Util: " + formatter.format(getLogicUtilization()) + " Render Util: " + formatter.format(getRenderUtilization()));
        textItems.get(1).setText("Camera Pos: " + formatter.format(camera.getPosition().x) + "," + formatter.format(camera.getPosition().y) + "," + formatter.format(camera.getPosition().z));
        textItems.get(2).setText("Camera Rot: " + formatter.format(camera.getRotation().x) + "," + formatter.format(camera.getRotation().y) + "," + formatter.format(camera.getRotation().z));


        if(isConnected) {
            textItems.get(4).setText("Is Connected");
            if(controller.getToggleRightBumperButton()) {
                disconnectFromServer(NetworkDisconnectType.CLIENT_LOGOUT);
            }
        } else {
            textItems.get(4).setText("Not Connected");
            textItems.get(5).setText("" + gameInput.getKeyValue(GLFW_KEY_C) + "." + gameInputTimings.getActiveMouseButton1Time());
            if(controller.getToggleLeftBumperButton()) {
                try {
                    System.out.println("Attempt connection to server");
                    connectToServer(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562"), this, InetAddress.getLocalHost(), 813);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
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
        }

        if(isConnected && controller.getToggleDirectionPad() != ControllerDirectionPad.NONE) {
            try {
                getGameSocket().sendPacket(new ClientSendInputPacket(ClientInputType.getInputDirection(controller.getDirectionPad().getDirection())));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    @Override
    public void render() {
        renderer.prepRender(globalGameData.getGameWindow());
        renderer.renderHUD(globalGameData.getGameWindow(),"Default2D",textItems);

        if(getWorlds().size() == 1) {
            World world = (World) getWorlds().toArray()[0];
            for(RegionBase region: world.getRegions().values()) {
                for (BlockBase block : region.getBlocksArray()) {
                    if (block instanceof CustomRenderBlock) {
                        ((CustomRenderBlock) block).render(renderer, globalGameData.getGameWindow(), camera, null);
                    }

                    for (EntityBase entity : region.getEntities()) {
                        entity.render(renderer, globalGameData.getGameWindow(), camera);
                    }
                }
            }
        }


    }


    @Override
    public void processPacket(PacketBase packet) {
        if(packet instanceof WorldStateModifyingPackets) {
            System.out.println(packet);
            ((WorldStateModifyingPackets) packet).executeModification(this);
        } else {
            System.out.println(packet);
        }
    }

    @Override
    public void onConnectedToServer() {
        isConnected = true;
        System.out.println("Client: Connected to Server!");
    }

    @Override
    public void onDisconnectFromServer() {
        isConnected = false;
        System.out.println("Client: Disconnected from Server!");
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
        if(isConnected) {
            disconnectFromServer(NetworkDisconnectType.CLIENT_WINDOW_CLOSED);
        }
    }
}
