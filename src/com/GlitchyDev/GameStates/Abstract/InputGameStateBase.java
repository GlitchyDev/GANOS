package com.GlitchyDev.GameStates.Abstract;

import com.GlitchyDev.GameStates.GameStateType;
import com.GlitchyDev.GameInput.Keyboard.GameInput;
import com.GlitchyDev.GameInput.Keyboard.GameInputTimings;
import com.GlitchyDev.Game.GlobalGameData;

/**
 * A rendering framework for GameStates that include Keyboard Inputs,
 */
public abstract class InputGameStateBase extends MonitoredGameStateBase {
    protected GameInput gameInput;
    protected GameInputTimings gameInputTimings;


    public InputGameStateBase(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
        gameInput = new GameInput();
        gameInput.bind(getWindowHandle());
        gameInputTimings = new GameInputTimings(gameInput);
    }


    @Override
    public void doLogic() {
        gameInputTimings.updateTimings();
        super.doLogic();
    }


}
