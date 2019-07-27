package com.GlitchyDev.World.Lighting;

import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Lighting.Abstract.LightProducer;

public interface RadiantLightProducer extends LightProducer {
    Direction[] getDirectionsProduced();
    int getLightLevelFromDirection(Direction direction);
}
