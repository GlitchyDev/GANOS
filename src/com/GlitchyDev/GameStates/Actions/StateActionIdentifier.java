package com.GlitchyDev.GameStates.Actions;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;

public enum StateActionIdentifier {
    DEBUG_ACTION,
    DEBUG_SUSTAINED_ACTION,


    ;


    public StateAction getActionFromInput(WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        switch(this) {
            default:
                return null;
        }
    }
}
