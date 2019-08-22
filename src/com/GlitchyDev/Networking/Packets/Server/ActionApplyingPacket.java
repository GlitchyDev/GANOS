package com.GlitchyDev.Networking.Packets.Server;

import com.GlitchyDev.GameStates.Abstract.ActionableGameState;
import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.GameStates.Actions.Abstract.StateAction;
import com.GlitchyDev.GameStates.Actions.Abstract.StateActionIdentifier;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;

import java.io.IOException;

public class ActionApplyingPacket extends WorldStateModifyingPackets {
    private final StateAction stateAction;
    public ActionApplyingPacket(StateAction stateAction) {
        super(PacketType.SERVER_APPLY_ACTION);
        this.stateAction = stateAction;
    }

    public ActionApplyingPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_APPLY_ACTION, inputBitUtility, worldGameState);
        StateActionIdentifier stateActionIdentifier = StateActionIdentifier.values()[inputBitUtility.getNextCorrectIntByte()];
        this.stateAction = stateActionIdentifier.getActionFromInput(worldGameState,inputBitUtility);
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        if(worldGameState instanceof ActionableGameState) {
            ((ActionableGameState) worldGameState).registerAction(stateAction);
        }
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(stateAction.getStateActionIdentifier().ordinal());
        stateAction.writeData(outputBitUtility);
    }
}
