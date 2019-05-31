package com.GlitchyDev.World.Elements.Communication.Sound;

import com.GlitchyDev.World.Elements.Communication.DetectionType;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Messages.SoundMessage;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Messages.SoundNoise;
import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Source.SoundSourceBase;
import com.GlitchyDev.World.Location;

public interface SoundListener {

    DetectionType generateDetectionType(Location origin, SoundSourceBase soundSource);
    void recieveMessage(SoundMessage message, Location origin, SoundSourceBase soundSourceBase);
    void recieveNoise(SoundNoise noise, Location origin, SoundSourceBase soundSourceBase);
}
