package com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Messages;

import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Enums.CommunicationType;

public abstract class Communication {
    private final CommunicationType communicationType;

    public Communication(CommunicationType communicationType) {
        this.communicationType = communicationType;
    }

    public CommunicationType getCommunicationType() {
        return communicationType;
    }
}
