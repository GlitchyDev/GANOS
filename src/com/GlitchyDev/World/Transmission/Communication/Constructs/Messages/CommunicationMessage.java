package com.GlitchyDev.World.Transmission.Communication.Constructs.Messages;

import com.GlitchyDev.World.Transmission.Communication.Constructs.Enums.LanguageType;
import com.GlitchyDev.World.Transmission.Communication.Constructs.Enums.CommunicationType;

public class CommunicationMessage extends Communication {
    private final int identifier;
    private final LanguageType language;
    private final String message;

    public CommunicationMessage(int identifier, LanguageType language, String message) {
        super(CommunicationType.LANGUAGE);
        this.identifier = identifier;
        this.language = language;
        this.message = message;
    }

    public int getIdentifier() {
        return identifier;
    }

    public LanguageType getLanguage() {
        return language;
    }

    public String getMessage() {
        return message;
    }
}
