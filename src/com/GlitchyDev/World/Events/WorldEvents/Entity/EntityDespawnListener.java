package com.GlitchyDev.World.Events.WorldEvents.Entity;

import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;

public interface EntityDespawnListener {
    void onTriggerEntityDespawn(Entity entity, DespawnReason despawnRegion);
}
