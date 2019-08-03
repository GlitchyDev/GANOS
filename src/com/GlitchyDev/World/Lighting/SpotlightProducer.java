package com.GlitchyDev.World.Lighting;

import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Lighting.Abstract.LightProducer;

public interface SpotlightProducer extends LightProducer {
    Direction getEmissionDirection();
    int getLightStrength();
}
