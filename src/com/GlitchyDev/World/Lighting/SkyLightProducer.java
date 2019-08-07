package com.GlitchyDev.World.Lighting;

import com.GlitchyDev.World.Lighting.Abstract.LightProducer;

public interface SkyLightProducer extends LightProducer {
    int getWidth();
    int getLength();
    int xOffset();
    int zOffset();
    int getSkyLightLevel();
}
