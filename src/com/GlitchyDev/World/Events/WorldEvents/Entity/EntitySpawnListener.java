package com.GlitchyDev.World.Events.WorldEvents.Entity;

import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;

public interface EntitySpawnListener {
    void onTriggerEntitySpawn(Entity entity, SpawnReason spawnReason);
}
