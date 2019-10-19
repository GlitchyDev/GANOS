package com.GlitchyDev.Game;

import com.GlitchyDev.GameStates.Abstract.FallGameJamGameState;
import com.GlitchyDev.GameStates.Abstract.GameStateBase;
import com.GlitchyDev.GameStates.Server.DebugServerGameState;
import com.GlitchyDev.Utility.AssetLoader;

/**
 * The Base Wrapper for the Game
 */
public class GANOSGame {

    protected GameWindow gameWindow;
    protected GlobalGameData globalGameData;

    public GANOSGame(String[] args) throws Exception {
        // Add potential permanent declaration
        gameWindow = new GameWindow(args[0] + "_GANOS",1000,1000,true);
        globalGameData = new GlobalGameData(gameWindow);
        gameWindow.init();

        AssetLoader.loadAssets();
        initGameStates(args);
        gameWindow.showWindow();

        run();
    }

    /**
     * Load and Register the starting Game State for the Game Args
     * @param args
     */
    public void initGameStates(String[] args) {
        if(args.length != 0) {
            switch (args[0]) {
                case "SERVER":
                    globalGameData.registerGameState(new DebugServerGameState(globalGameData));
                    break;
                case "GAME_JAM":
                    globalGameData.registerGameState(new FallGameJamGameState(globalGameData));
                    break;
            }
        }
    }


    public void run() {
        GameStateBase currentGameState;
        while(!globalGameData.getGameWindow().shouldWindowClose()) {
            gameWindow.update();

            currentGameState = globalGameData.getCurrentGameState();

            currentGameState.doLogic();
            currentGameState.doRender();

            if(gameWindow.shouldWindowClose()) {
                currentGameState.windowClose();
            }
        }
    }


}
