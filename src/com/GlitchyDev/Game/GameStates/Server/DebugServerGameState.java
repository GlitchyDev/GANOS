package com.GlitchyDev.Game.GameStates.Server;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_C;

public class DebugServerGameState extends ServerWorldGameState {
    private final ArrayList<TextItem> textItems;


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

    }


    boolean isRunning = false;
    int lastKey = 0;
    @Override
    public void logic() {
        super.logic();

        if(isRunning) {
            textItems.get(0).setText("Connected Players");


            for(int i = 1; i < textItems.size(); i++) {
                textItems.get(i).setText("");
            }
            int i = 1;
            for (UUID uuid : serverNetworkManager.getConnectedUsers()) {
                textItems.get(i).setText("Player " + i + ": " + uuid);
                i++;
            }
        } else {
            textItems.get(0).setText("Not Online");
            if(lastKey == 0 && gameInput.getKeyValue(GLFW_KEY_C) == 1) {

                System.out.println("Starting up Server");
                serverNetworkManager.enableAcceptingClients(813);
                serverNetworkManager.getApprovedUsers().add(UUID.fromString("087954ba-2b12-4215-9a90-f7b810797562"));
                isRunning = true;
            }
        }

        lastKey = gameInput.getKeyValue(GLFW_KEY_C);

    }

    @Override
    public void render() {
        renderer.prepRender(globalGameData.getGameWindow());
        renderer.renderHUD(globalGameData.getGameWindow(),"Default2D",textItems);

    }

    @Override
    public void processPacket(UUID uuid, PacketBase packet) {

    }

    @Override
    public void onPlayerLogin(UUID playerUUID) {

    }

    @Override
    public void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason) {
        System.out.println("DebugServer: User " + playerUUID + " disconnected for " + reason);
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
