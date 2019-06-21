package com.GlitchyDev.World.Events.Communication.Constructs.Messages;

import com.GlitchyDev.World.Events.Communication.Constructs.Enums.CommunicationType;

public abstract class Communication {
    private final CommunicationType communicationType;

    public Communication(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }
}
