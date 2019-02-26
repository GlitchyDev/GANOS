package com.GlitchyDev.Game.GameStates.Client;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ClientWorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.GameInput.Controllers.GameController;
import com.GlitchyDev.GameInput.Controllers.PS4Controller;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Utility.GlobalGameData;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;

public class DebugClientGameState extends ClientWorldGameState {
    private final ArrayList<TextItem> textItems;
    private boolean isConnected = false;
    private GameController gameController;

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

        gameController = new PS4Controller(0);


    }


    int lastKey = 0;
    @Override
    public void logic() {
        super.logic();

        if(isConnected) {
            textItems.get(0).setText("Is Connected");
            if(lastKey == 0 && gameInput.getKeyValue(GLFW_KEY_C) == 1) {
                disconnectFromServer(NetworkDisconnectType.CLIENT_LOGOUT);
            }
        } else {
            textItems.get(0).setText("Not Connected");
            textItems.get(1).setText("" + gameInput.getKeyValue(GLFW_KEY_C) + "." + gameInputTimings.getActiveMouseButton1Time());
            if(lastKey == 0 && gameInput.getKeyValue(GLFW_KEY_C) == 1) {
                try {
                    System.out.println("Attempt connection to server");
                    connectToServer(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562"), this, InetAddress.getLocalHost(), 813);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
            lastKey = gameInput.getKeyValue(GLFW_KEY_C);
        }

        if(gameInputTimings.getActiveMouseButton1Time() > 60) {
            System.exit(0);
        }
    }

    @Override
    public void render() {
        renderer.prepRender(globalGameData.getGameWindow());
        renderer.renderHUD(globalGameData.getGameWindow(),"Default2D",textItems);
    }


    @Override
    public void processPacket(PacketBase packet) {
        if(packet instanceof WorldStateModifyingPackets) {
            ((WorldStateModifyingPackets) packet).executeModification(this);
        } else {
            switch(packet.getPacketType()) {
                default:
                    System.out.println(packet);
            }
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
