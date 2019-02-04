package com.GlitchyDev.Old.Game.GameStates.Abstract;

import com.GlitchyDev.Old.Game.GameStates.GameStateType;
import com.GlitchyDev.Old.GameInput.GameInput;
import com.GlitchyDev.Old.GameInput.GameInputTimings;
import com.GlitchyDev.Old.Utility.GlobalGameData;

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
