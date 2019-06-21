package com.GlitchyDev.GameStates.Client;


import com.GlitchyDev.GameStates.Abstract.InputGameStateBase;
import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.Old.Utility.GlobalGameData;


public class DebugGameState3 extends InputGameStateBase {
    // required for each gamestate

    public DebugGameState3(GlobalGameData globalGameDataBase) {
        super(globalGameDataBase, GameStateType.DEBUG_3);
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
        renderer.cleanup();

    }
}
