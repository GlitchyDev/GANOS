package com.GlitchyDev.World.Elements.Communication.Sound;

import com.GlitchyDev.World.Elements.Communication.DetectionType;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.NoiseType;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Messages.SoundMessage;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Messages.SoundNoise;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Source.SoundSourceBase;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;

public class SoundTransmissionManager {
    private final ArrayList<SoundListener> listeners;

    public SoundTransmissionManager() {
        this.listeners = new ArrayList<>();
    }

    public void join(SoundListener soundListener) {
        listeners.add(soundListener);
    }

    public void leave(SoundListener soundListener) {
        listeners.remove(soundListener);
    }

    public void transmitMessage(SoundMessage soundMessage, Location origin, SoundSourceBase soundSourceBase) {
        for(SoundListener soundListener: listeners) {
            switch(soundListener.generateDetectionType(origin, soundSourceBase)) {
                case COMPREHENSION:
                    soundListener.recieveMessage(soundMessage, origin, soundSourceBase);
                    break;
                case DETECTION:
                    soundListener.recieveNoise(new SoundNoise(NoiseType.ENTITY_TALKING),origin, soundSourceBase);
                    break;
            }
        }
    }

    public void transmitNoise(SoundNoise soundNoise, Location origin, SoundSourceBase soundSource) {
        for(SoundListener soundListener: listeners) {
            if(soundListener.generateDetectionType(origin,soundSource) != DetectionType.NONE) {
                soundListener.recieveNoise(soundNoise,origin,soundSource);
            }
        }
    }

}
