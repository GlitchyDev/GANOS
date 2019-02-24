package com.GlitchyDev.Game.GameStates;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ClientWorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Utility.GlobalGameData;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;

public class DebugClientGameState extends ClientWorldGameState {
    private final ArrayList<TextItem> textItems;

    public DebugClientGameState(GlobalGameData globalGameDataBase) {
        super(globalGameDataBase, GameStateType.DEBUG_CLIENT);

        CustomFontTexture customTexture = new CustomFontTexture("DebugFont");
        textItems = new ArrayList<>();
        final int NUM_TEX_ITEMS = 16;
        for(int i = 0; i < NUM_TEX_ITEMS; i++) {
            TextItem item = new TextItem("",customTexture);
            item.setPosition(55*3,i*12,0);
            textItems.add(item);
        }

    }


    @Override
    public void render() {
        renderer.prepRender(globalGameData.getGameWindow());
        renderer.renderHUD(globalGameData.getGameWindow(),"Default2D",textItems);
    }

    @Override
    public void logic() {
        super.logic();
        if(gameInput.getKeyValue(GLFW_KEY_A) >= 1) {
            textItems.get(0).setText("A");
        } else {
            textItems.get(0).setText("");
        }
    }

    @Override
    public void processPacket(PacketBase packet) {

    }

    @Override
    public void processOutput() {

    }

    @Override
    public void connectedToServer() {

    }

    @Override
    public void disconnectedFromServer() {

    }

    @Override
    public void init() throws Exception {

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
}
