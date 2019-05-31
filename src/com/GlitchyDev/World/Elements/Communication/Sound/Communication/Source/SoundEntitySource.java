package com.GlitchyDev.World.Elements.Communication.Sound.Communication.Source;

import com.GlitchyDev.World.Elements.Communication.Sound.Communication.Enums.SourceType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;

public class SoundEntitySource extends SoundSourceBase {
    private final EntityBase entitySource;

    public SoundEntitySource(EntityBase entitySource) {
        super(SourceType.ENTITY);
        this.entitySource = entitySource;
    }

    public EntityBase getEntitySource() {
        return entitySource;
    }
}
