package com.GlitchyDev.Utility;

import com.GlitchyDev.Game.GameStates.Abstract.GameStateBase;
import com.GlitchyDev.Game.GameStates.GameStateType;


import java.util.HashMap;

/**
 * A collection and holder of all pertinent game information accessible by any internal components
 */
public class GlobalGameData {
    private GameWindow gameWindow;
    private HashMap<GameStateType, GameStateBase> loadedGameStates;
    private GameStateType currentGameState;

    public GlobalGameData(GameWindow gameWindow)
    {
        this.gameWindow = gameWindow;
        loadedGameStates = new HashMap<>();
        currentGameState = null;
    }


    public void switchGameState(GameStateType gameStateType)
    {
        GameStateType previousState = currentGameState;
        loadedGameStates.get(previousState).exitState(gameStateType);
        currentGameState = gameStateType;
        loadedGameStates.get(currentGameState).enterState(previousState);
    }

    public GameStateBase getCurrentGameState()
    {
        return loadedGameStates.get(currentGameState);
    }

    public void registerGameState(GameStateBase gameState)
    {
        loadedGameStates.put(gameState.getGameStateType(),gameState);
        if(currentGameState == null)
        {
            currentGameState = gameState.getGameStateType();
            gameState.enterState(GameStateType.NONE);
        }
    }

    // Getters

    public GameWindow getGameWindow() {
        return gameWindow;
    }

}
