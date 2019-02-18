package com.GlitchyDev.GameInput;

import java.util.HashMap;

/**
 * Takes the information gained from GameInput, and allows for timings to be associated with keys
 */
public class GameInputTimings {
    private final GameInput gameInput;
    private HashMap<Integer,Integer> activeKeyTime = new HashMap<>();
    private int activeMouseButton1Time = 0;
    private int activeMouseButton2Time = 0;
    private int activeMouseButton3Time = 0;
    private double currentScroll = 0.0;

    public GameInputTimings(GameInput gameInput)
    {

        this.gameInput = gameInput;
        for(int i = 0; i < GameInput.KEYBOARD_SIZE; i++) {
            activeKeyTime.put(i,0);
        }
    }

    public void updateTimings() {
        updateActiveKeyTime();
        updateMouseKeyTime();
    }

    public void updateActiveKeyTime() {
        for(int i = 0; i < GameInput.KEYBOARD_SIZE; i++) {
            activeKeyTime.put(i,gameInput.getActiveKeys().contains(i) ? activeKeyTime.get(i) + 1: 0);
        }

    }

    public void updateMouseKeyTime()
    {
        if(activeMouseButton1Time == 0) {
            if(gameInput.getMouseButton1() == 1) {
                activeMouseButton1Time = 1;
            }
        } else {
            if(gameInput.getMouseButton1() == 0) {
                activeMouseButton1Time = 0;
            } else {
                activeMouseButton1Time++;
            }
        }
        // **
        if(activeMouseButton2Time == 0)
        {
            if(gameInput.getMouseButton2() == 1) {
                activeMouseButton2Time = 1;
            }
        } else {
            if(gameInput.getMouseButton2() == 0) {
                activeMouseButton2Time = 0;
            } else {
                activeMouseButton2Time++;
            }
        }
        // **
        if(activeMouseButton3Time == 0) {
            if(gameInput.getMouseButton3() == 1) {
                activeMouseButton3Time = 1;
            }
        }
        else
        {
            if(gameInput.getMouseButton3() == 0) {
                activeMouseButton3Time = 0;
            } else {
                activeMouseButton3Time++;
            }
        }
        if(gameInput.isMouseScrollEnabled()) {
            currentScroll = gameInput.getMouseScroll();
            gameInput.setMouseScrollEnabled(false);
        }
        else {
            currentScroll = 0;
        }
    }


    public int getActiveKeyTime(int key)
    {
        return activeKeyTime.get(key);
    }

    public int getActiveMouseButton1Time()
    {
        return activeMouseButton1Time;
    }

    public int getActiveMouseButton2Time()
    {
        return activeMouseButton2Time;
    }

    public int getActiveMouseButton3Time()
    {
        return activeMouseButton3Time;
    }

    public double getActiveScroll() {

        return currentScroll;

    }
}
