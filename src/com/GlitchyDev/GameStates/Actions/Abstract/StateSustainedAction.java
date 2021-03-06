package com.GlitchyDev.GameStates.Actions.Abstract;

import com.GlitchyDev.GameStates.Abstract.ActionableGameState;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.InputBitUtility;

public abstract class StateSustainedAction extends StateAction {

    protected StateSustainedAction(StateActionIdentifier stateActionIdentifier, ActionableGameState actionableGameState) {
        super(stateActionIdentifier, actionableGameState);
    }

    protected StateSustainedAction(StateActionIdentifier stateActionIdentifier, ActionableGameState actionableGameState, InputBitUtility inputBitUtility) {
        super(stateActionIdentifier, actionableGameState);
    }

    public abstract void onTick();

    public abstract void onRender(Renderer renderer);

    public final void terminate() {
        actionableGameState.terminateSustainedAction(this);
    }
}
