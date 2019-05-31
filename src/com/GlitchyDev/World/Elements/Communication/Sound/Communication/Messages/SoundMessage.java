package com.GlitchyDev.World.Elements.Communication.Sound.Communication.Messages;

import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.LanguageType;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.SoundType;

public class SoundMessage extends SoundBase {
    private final int identifier;
    private final LanguageType language;
    private final String message;

    public SoundMessage(int identifier, LanguageType language, String message) {
        super(SoundType.LANGUAGE);
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
