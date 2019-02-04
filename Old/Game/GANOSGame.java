package com.GlitchyDev.Old.Game;

import com.GlitchyDev.Old.Game.GameStates.Abstract.GameStateBase;
import com.GlitchyDev.Old.Game.GameStates.Client.DebugGameState1;
import com.GlitchyDev.Old.Game.GameStates.Client.DebugGameState3;
import com.GlitchyDev.Old.Game.GameStates.Client.MapBuilderGameState;
import com.GlitchyDev.Old.Game.GameStates.Server.DebugGameState2;
import com.GlitchyDev.Old.IO.AssetLoader;
import com.GlitchyDev.Old.Utility.GameWindow;
import com.GlitchyDev.Old.Utility.GlobalGameData;

/**
 * The Base Wrapper for the Game
 */
public class GANOSGame {

    protected GameWindow gameWindow;
    protected GlobalGameData globalGameData;

    public GANOSGame(String[] args) throws Exception {
        // Add potential permanent declaration
        gameWindow = new GameWindow(args[0] + "_GANOS",800,800,true);
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
    public void initGameStates(String[] args)
    {
        if(args.length != 0) {
            switch (args[0]) {
                case "CLIENT":
                    globalGameData.registerGameState(new DebugGameState1(globalGameData));
                    break;
                case "SERVER":
                    globalGameData.registerGameState(new DebugGameState2(globalGameData));
                    break;
                case "GENERAL":
                    globalGameData.registerGameState(new DebugGameState3(globalGameData));
                    break;
                case "MAPBUILDER":
                    globalGameData.registerGameState(new MapBuilderGameState(globalGameData));
                    break;
            }
        }
        else
        {
           // Debug GameState
            globalGameData.registerGameState(new DebugGameState1(globalGameData));
        }

    }


    public void run()
    {
        GameStateBase currentGameState;
        while(!globalGameData.getGameWindow().getWindowShouldClose()) {
            gameWindow.update();

            currentGameState = globalGameData.getCurrentGameState();

            currentGameState.doLogic();
            currentGameState.doRender();

            if(gameWindow.getWindowShouldClose())
            {
                currentGameState.windowClose();
            }
        }
    }
}
