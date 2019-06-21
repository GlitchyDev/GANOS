package com.GlitchyDev.GameStates.Abstract;

import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.Game.GlobalGameData;

/**
 * Implements Performance Metrics and Measurements for a GameState, as well as methods to access the specified information
 */
public abstract class MonitoredGameStateBase extends GameStateBase {
    protected int currentFPS = 0;
    private int fpsCount = 0;
    private long lastFPSCount = 0;
    protected double renderUtilization = 0.0;
    protected double logicUtilization = 0.0;

    public MonitoredGameStateBase(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
    }

    // Abstract Functions

    public abstract void logic();

    public abstract void render();

    // Functions
    public void doRender() {
        long renderStart = System.nanoTime();
        render();
        long renderEnd = System.nanoTime();
        renderUtilization = (100.0)/(1000000000.0/60.0) * (renderEnd-renderStart);

        fpsCount++;
        if(System.currentTimeMillis() > lastFPSCount + 1000)
        {
            currentFPS = fpsCount;
            fpsCount = 0;
            lastFPSCount = System.currentTimeMillis();
        }
    }

    public void doLogic() {
        long logicStart = System.nanoTime();
        logic();
        long logicEnd = System.nanoTime();
        logicUtilization = (100.0)/(1000000000.0/60.0) * (logicEnd-logicStart);
    }

    private void resetTimings()
    {
        lastFPSCount = System.currentTimeMillis();
        currentFPS = 0;
        fpsCount = 0;
        renderUtilization = 0;
        logicUtilization = 0;

    }

    // Getters
    public int getCurrentFPS() {
        return currentFPS;
    }

    public int getFPSCount() {
        return fpsCount;
    }

    public double getRenderUtilization() {
        return renderUtilization;
    }

    public double getLogicUtilization() {
        return logicUtilization;
    }

}
