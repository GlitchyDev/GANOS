package com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Source;

import com.GlitchyDev.World.Elements.Transmission.Communication.Constructs.Enums.SourceType;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;

public class CommunicationEntitySource extends CommunicationSource {
    private final EntityBase entitySource;

    public CommunicationEntitySource(EntityBase entitySource) {
        super(SourceType.ENTITY);
        this.entitySource = entitySource;
    }

    public EntityBase getEntitySource() {
        return entitySource;
    }
}
