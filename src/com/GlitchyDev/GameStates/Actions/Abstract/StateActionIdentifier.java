package com.GlitchyDev.GameStates.Actions.Abstract;

import com.GlitchyDev.GameStates.Abstract.ActionableGameState;
import com.GlitchyDev.GameStates.Actions.DebugSustainedAction;
import com.GlitchyDev.Utility.InputBitUtility;

public enum StateActionIdentifier {
    DEBUG_ACTION,
    DEBUG_SUSTAINED_ACTION,


    ;


    public StateAction getActionFromInput(ActionableGameState actionableGameState, InputBitUtility inputBitUtility) {
        switch(this) {
            case DEBUG_SUSTAINED_ACTION:
                return new DebugSustainedAction(actionableGameState,inputBitUtility);
            default:
                return null;
        }
    }
}
