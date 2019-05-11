package com.GlitchyDev.Game.GameStates.Abstract;

import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Game.GlobalGameData;

/**
 * The Basic GameState for separating district states of the Game into distinct computational units
 */
public abstract class GameStateBase {
    // A required GlobalGameData which allows to access all game created Information
    protected GlobalGameData globalGameData;
    // A identifier for the current Game Type
    protected final GameStateType gameStateType;
    // A rendering helper for using Shaders and Cameras
    protected final Renderer renderer;

    public GameStateBase(GlobalGameData globalGameData, GameStateType gameStateType) {
        this.globalGameData = globalGameData;
        this.gameStateType = gameStateType;
        this.renderer = new Renderer();
    }

    public abstract void init();

    public abstract void doRender();

    public abstract void doLogic();

    /**
     * Called on Entering the State
     * @param previousGameState
     */
    public abstract void enterState(GameStateType previousGameState);

    /**
     * Called on Exiting the State
     * @param nextGameState
     */
    public abstract void exitState(GameStateType nextGameState);

    /**
     * Called on Window closing
     */
    public abstract void windowClose();

    // Functions

    protected long getWindowHandle() {
        return globalGameData.getGameWindow().getWindowHandle();
    }

    public GameStateType getGameStateType() {
        return gameStateType;
    }
}
