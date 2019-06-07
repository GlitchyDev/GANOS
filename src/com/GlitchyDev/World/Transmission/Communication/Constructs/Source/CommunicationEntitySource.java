package com.GlitchyDev.World.Transmission.Communication.Constructs.Source;

import com.GlitchyDev.World.Transmission.Communication.Constructs.Enums.SourceType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;

public class CommunicationEntitySource extends CommunicationSource {
    private final Entity entitySource;

    public CommunicationEntitySource(Entity entitySource) {
        super(SourceType.ENTITY);
        this.entitySource = entitySource;
    }

    public Entity getEntitySource() {
        return entitySource;
    }
}
