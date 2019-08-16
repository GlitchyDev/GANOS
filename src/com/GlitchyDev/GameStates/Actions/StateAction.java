package com.GlitchyDev.GameStates.Actions;

import com.GlitchyDev.GameStates.Abstract.ActionableGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public abstract class StateAction {
    protected final StateActionIdentifier stateActionIdentifier;
    protected final ActionableGameState actionableGameState;


    protected StateAction(StateActionIdentifier stateActionIdentifier, ActionableGameState actionableGameState) {
        this.stateActionIdentifier = stateActionIdentifier;
        this.actionableGameState = actionableGameState;
    }

    protected StateAction(StateActionIdentifier stateActionIdentifier, ActionableGameState actionableGameState, InputBitUtility inputBitUtility) {
        this.stateActionIdentifier = stateActionIdentifier;
        this.actionableGameState = actionableGameState;
    }

    public abstract void onApplication();

    public StateActionIdentifier getStateActionIdentifier() {
        return stateActionIdentifier;
    }

    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(stateActionIdentifier.ordinal());
    }
}
