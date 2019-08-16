package com.GlitchyDev.GameStates.Abstract;

import com.GlitchyDev.Game.GlobalGameData;
import com.GlitchyDev.GameStates.Actions.StateAction;
import com.GlitchyDev.GameStates.Actions.StateActionIdentifier;
import com.GlitchyDev.GameStates.Actions.StateSustainedAction;
import com.GlitchyDev.GameStates.GameStateType;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ActionableGameState extends WorldGameState{
    private ArrayList<StateAction> unprocessedActions;
    private HashMap<StateActionIdentifier,StateSustainedAction> sustainedActions;
    private ArrayList<StateSustainedAction> removedSustainedActions;

    public ActionableGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);

        unprocessedActions = new ArrayList<>();
        sustainedActions = new HashMap<>();
        removedSustainedActions = new ArrayList<>();
    }

    @Override
    public void doLogic() {
        super.doLogic();
        for(StateAction gameStateAction: unprocessedActions) {
            gameStateAction.onApplication();
        }
        unprocessedActions.clear();

        for(StateSustainedAction stateSustainedAction: sustainedActions.values()) {
            stateSustainedAction.onTick();
        }
        for(StateSustainedAction stateSustainedAction: removedSustainedActions) {
            sustainedActions.remove(stateSustainedAction.getStateActionIdentifier());
        }
        removedSustainedActions.clear();
    }


    @Override
    public void doRender() {
        super.doRender();
        for(StateSustainedAction gameStateSustainedAction: sustainedActions.values()) {
            gameStateSustainedAction.onRender(renderer);
        }
    }

    public void terminateSustainedAction(StateSustainedAction sustainedAction) {
        removedSustainedActions.add(sustainedAction);
    }

    public void registerAction(StateAction stateAction) {
        if(stateAction instanceof StateSustainedAction) {
            sustainedActions.put(stateAction.getStateActionIdentifier(), (StateSustainedAction) stateAction);
        } else {
            unprocessedActions.add(stateAction);
        }
    }
}
