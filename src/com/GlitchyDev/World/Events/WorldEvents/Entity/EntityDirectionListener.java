package com.GlitchyDev.World.Events.WorldEvents.Entity;

import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;

public interface EntityDirectionListener {
    void onTriggerEntityDirection(Entity entity, Direction oldDirection, Direction newDirection);
}
