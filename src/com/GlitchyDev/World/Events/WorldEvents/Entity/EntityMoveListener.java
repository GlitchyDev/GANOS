package com.GlitchyDev.World.Events.WorldEvents.Entity;

import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Location;

import java.util.UUID;

public interface EntityMoveListener {
    void onTriggerEntityMovement(Entity entity, Location oldLocation, Location newLocation, UUID oldRegionUUID, UUID newRegionUUID);
}
