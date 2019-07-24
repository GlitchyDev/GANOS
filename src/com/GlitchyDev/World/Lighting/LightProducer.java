package com.GlitchyDev.World.Lighting;

import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

import java.util.UUID;

public interface LightProducer {
    // Further optimizations could allow for it to only update lighting within its "Old Update radius"
    Direction[] getDirectionsProduced();
    int getLightLevelProduced(Direction direction);
    Location getEmissionLocation();
    UUID getWorldUUID();
}
