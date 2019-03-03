package com.GlitchyDev.Game.GameStates.Client;

import com.GlitchyDev.Game.GameStates.Abstract.EnvironmentGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Old.IO.AssetLoader;
import com.GlitchyDev.Old.Networking.GameSocket;
import com.GlitchyDev.Old.Networking.Packets.NetworkDisconnectType;
import com.GlitchyDev.Old.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Old.Rendering.Assets.Fonts.FontTexture;
import com.GlitchyDev.Old.Rendering.Assets.Mesh;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Old.Utility.GlobalGameData;

import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A Debug Game State used currently to show Rendering Implementtion, Hud Text, and Controls
 */
public class DebugGameState1 extends EnvironmentGameState {
    // required for each gamestate
    private Camera camera;
    private ArrayList<GameItem> gameItems = new ArrayList<>();
    private ArrayList<TextItem> hudItems = new ArrayList<>();
    private ArrayList<SpriteItem> spriteItems = new ArrayList<>();
    private GameSocket gameSocket;


    private HashMap<String,Mesh> activeMeshes;


    public DebugGameState1(GlobalGameData globalGameDataBase) {
        super(globalGameDataBase, GameStateType.DEBUG_1);
        init();

    }


    @Override
    public void init() {
        camera = new Camera();

        activeMeshes = new HashMap<>();

        activeMeshes.put("Cube", AssetLoader.getMeshAsset("DefaultCube"));
        activeMeshes.get("Cube").setTexture(AssetLoader.getTextureAsset("UVMapCubeTexture"));

        activeMeshes.put("Floor", AssetLoader.getMeshAsset("DefaultSquare"));
        activeMeshes.get("Floor").setTexture(AssetLoader.getTextureAsset("Test_Floor"));

        activeMeshes.put("Wall", AssetLoader.getMeshAsset("flatWall1"));
        activeMeshes.get("Wall").setTexture(AssetLoader.getTextureAsset("Test_Floor"));



        int width = 100;
        int height = 1;
        int length = 100;


        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                for(int z = 0; z < length; z++)
                {
                    GameItem gameItem = new GameItem(activeMeshes.get("Floor"));
                    gameItem.setPosition(x*2,y,z*2);

                    gameItem.setScale(1.0f);
                    gameItems.add(gameItem);
                }
            }
        }






        final Font FONT = new Font("Greek", Font.PLAIN, 20);
        String CHARSET = "ISO-8859-1";

        FontTexture fontTexture = new FontTexture(FONT,CHARSET);
        CustomFontTexture customTexture = new CustomFontTexture("DebugFont");
        TextItem item = new TextItem("",customTexture);
        TextItem item2 = new TextItem("",customTexture);
        item2.setPosition(0,20,0);
        hudItems.add(item);
        hudItems.add(item2);


        //spriteItems.add(new SpriteItem(AssetLoader.getTextureAsset("Tomo"),true));




    }

    /*
            debugBuffer.bindToRender();
        renderer.prepRender(globalGameData.getGameWindow());
        renderer.render3DElements(globalGameData.getGameWindow(), "Default3D", camera, gameItems);
        renderer.renderSprites(globalGameData.getGameWindow(), "Default2D", spriteItems);
        debugBuffer.unbindToRender(globalGameData.getGameWindow().getWidth(),globalGameData.getGameWindow().getHeight());

        renderer.prepRender(globalGameData.getGameWindow());
        renderer.renderHUD(globalGameData.getGameWindow(), "Default2D", hudItems);
        renderer.renderSprites(globalGameData.getGameWindow(),"Default2D",new SpriteItem[]{debugItem});
     */
    @Override
    public void render() {


        renderer.prepRender(globalGameData.getGameWindow());


        renderer.render3DElements(globalGameData.getGameWindow(), "Default3D", camera,gameItems);
        renderer.renderSprites(globalGameData.getGameWindow(), "Default2D", spriteItems);
        renderer.renderHUD(globalGameData.getGameWindow(), "Default2D", hudItems);
    }


    NumberFormat formatter = new DecimalFormat("#0.00");
    @Override
    public void logic() {

        hudItems.get(0).setText("Render: " + formatter.format(getRenderUtilization()) + " Logic: " + formatter.format(getLogicUtilization()));
        hudItems.get(1).setText("FPS:" + getCurrentFPS());


        final double movementSpeed = 0.3;
        if(gameInput.getKeyValue(GLFW_KEY_W) != 0)
        {
            camera.getPosition().x += movementSpeed * (float)Math.sin(Math.toRadians(camera.getRotation().y));
            camera.getPosition().z -= movementSpeed * (float)Math.cos(Math.toRadians(camera.getRotation().y));
        }
        if(gameInput.getKeyValue(GLFW_KEY_S) != 0)
        {
            camera.getPosition().x -= movementSpeed * (float)Math.sin(Math.toRadians(camera.getRotation().y));
            camera.getPosition().z += movementSpeed * (float)Math.cos(Math.toRadians(camera.getRotation().y));
        }

        if(gameInput.getKeyValue(GLFW_KEY_A) != 0)
        {
            camera.getPosition().x -= movementSpeed * (float)Math.sin(Math.toRadians(camera.getRotation().y + 90));
            camera.getPosition().z += movementSpeed * (float)Math.cos(Math.toRadians(camera.getRotation().y + 90));
        }
        if(gameInput.getKeyValue(GLFW_KEY_D) != 0)
        {
            camera.getPosition().x -= movementSpeed * (float)Math.sin(Math.toRadians(camera.getRotation().y - 90));
            camera.getPosition().z += movementSpeed * (float)Math.cos(Math.toRadians(camera.getRotation().y - 90));
        }


        final double rotationSpeed = 3.0;
        if(gameInput.getKeyValue(GLFW_KEY_RIGHT_SHIFT) != 0)
        {
            camera.getPosition().y -= movementSpeed;
        }
        if(gameInput.getKeyValue(GLFW_KEY_SPACE) != 0)
        {
            camera.getPosition().y += movementSpeed;
        }


        if(gameInput.getKeyValue(GLFW_KEY_LEFT) != 0)
        {
            camera.getRotation().y -= rotationSpeed;
        }
        if(gameInput.getKeyValue(GLFW_KEY_RIGHT) != 0)
        {
            camera.getRotation().y += rotationSpeed;
        }

        if(gameInput.getKeyValue(GLFW_KEY_UP) != 0)
        {
            camera.getRotation().x -= rotationSpeed;
        }
        if(gameInput.getKeyValue(GLFW_KEY_DOWN) != 0)
        {
            camera.getRotation().x += rotationSpeed;
        }


        //GameItem gameItem1 = selectGameItem3D(gameItems,camera);
        //GameItem gameItem2 = selectGameItem2D(gameItems,globalGameData.getGameWindow(),new Vector2d(gameInput.getMouseX(),gameInput.getMouseY()),camera);

        /*
        if(gameItem1 != null)
        {
            gameItem1.setMeshItem(activeMeshes.get("Wall"));
            gameItem1.setRotation(0,90 * (int)(Math.random() * 4),0);
        }
        if(gameItem2 != null)
        {
            gameItem2.setMeshItem(activeMeshes.get("Cube"));
        }
        */



    }



    /*
    if (gameInput.getKeyValue(GLFW_KEY_ESCAPE) == 1) {
            globalGameData.getGameWindow().makeWindowClose();
            System.out.println("DebugGameState: CLOSING WINDOW");
        }

        if (gameInput.getKeyValue(GLFW_KEY_0) == 1) {

            if(gameSocket == null)
            {
                try {
                    Socket socket = new Socket("192.168.1.3",8001);
                    gameSocket = new GameSocket(socket);

                    gameSocket.sendPacket(new ClientIntroductionPacket("James"));
                    while(!gameSocket.hasUnprocessedPackets())
                    {
                        Thread.yield();
                    }
                    Packet packet = gameSocket.getUnprocessedPackets().get(0);
                    if(packet.getPacketType() == PacketType.A_GOODBYE)
                    {
                        gameSocket = null;
                    }
                    else
                    {
                        globalGameData.getGameWindow().setClearColor(1.0f,0.0f,0.0f,1.0f);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }






        if(gameInputTimings.getActiveMouseButton1Time() != 0) {
            hudItems[0].setText("" + gameInputTimings.getActiveMouseButton1Time());

            if(gameInputTimings.getActiveMouseButton1Time() > 60)
            {
                globalGameData.getGameWindow().makeWindowClose();
            }
        }


        spriteItems[0].getPosition().y += gameInputTimings.getActiveScroll() * 10;
        if(gameSocket != null) {
            gameSocket.sendPacket(new DebugScrollPacket(gameInputTimings.getActiveScroll() * 10));
        }
     */

    @Override
    public void enterState(GameStateType previousGameState) {
        globalGameData.getGameWindow().setCursor(AssetLoader.getInputStream("Icon16x16.png"),0,0);
        globalGameData.getGameWindow().setIcon(AssetLoader.getInputStream("Icon16x16.png"),AssetLoader.getInputStream("Icon32x32.png"));
    }

    @Override
    public void exitState(GameStateType nextGameState) {

    }

    @Override
    public void windowClose() {
        if(gameSocket != null)
        {
            gameSocket.disconnect(NetworkDisconnectType.WINDOW_CLOSED);
        }
        renderer.cleanup();

    }
}
