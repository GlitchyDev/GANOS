package com.GlitchyDev.World.Entities.Enums;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.DebugEntity;

import java.io.IOException;

public enum EntityType {
    DEBUG;

    public EntityBase getEntityFromInput(InputBitUtility inputBitUtility) throws IOException {
        switch (this) {
            case DEBUG:
                return new DebugEntity(inputBitUtility);
            default:
                return new DebugEntity(inputBitUtility);
        }

    }




}
