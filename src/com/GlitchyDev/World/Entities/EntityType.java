package com.GlitchyDev.World.Entities;

import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;

public enum EntityType {
    DEBUG;

    public EntityBase getEntityClass() {
        switch(this) {
            case DEBUG:
                return new DebugEntity(null);
            default:
                return new DebugEntity(null);
        }

    }



}
