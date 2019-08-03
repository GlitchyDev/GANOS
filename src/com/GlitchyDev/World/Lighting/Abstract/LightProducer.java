package com.GlitchyDev.World.Lighting.Abstract;

import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Location;

import java.util.UUID;

public interface LightProducer {
    // Further optimizations could allow for it to only update lighting within its "Old Update radius"
    Location getEmissionLocation();
    UUID getWorldUUID();

    boolean isDynamic();
    boolean doNeedLightUpdate();
    boolean doSeeLight(Entity entity);
}
