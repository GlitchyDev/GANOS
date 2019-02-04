package com.GlitchyDev.Old.Game.GameStates.Server;

import com.GlitchyDev.Old.Game.GameStates.Abstract.InputGameStateBase;
import com.GlitchyDev.Old.Game.GameStates.GameStateType;
import com.GlitchyDev.Old.Networking.ServerNetworkConnection;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Old.Utility.GlobalGameData;

import java.util.ArrayList;

public class DebugGameState2 extends InputGameStateBase {
    private ServerNetworkConnection serverNetworkConnection;

    private ArrayList<TextItem> hudItems;
    private ArrayList<SpriteItem> spriteItems;



    public DebugGameState2(GlobalGameData globalGameDataBase) {
        super(globalGameDataBase, GameStateType.DEBUG_2);
        init();

    }

    @Override
    public void init() {

    }

    @Override
    public void render() {

    }

    @Override
    public void logic() {



    }

    @Override
    public void enterState(GameStateType previousGameState) {

    }

    @Override
    public void exitState(GameStateType nextGameState) {

    }

    @Override
    public void windowClose() {

        //serverNetworkConnection.disableAcceptingClients();

    }
}
