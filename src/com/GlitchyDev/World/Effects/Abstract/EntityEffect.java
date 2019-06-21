package com.GlitchyDev.World.Effects.Abstract;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;

public abstract class EntityEffect extends Effect {
    private Entity entity;
    public EntityEffect(EffectType effectType, WorldGameState worldGameState) {
        super(effectType, worldGameState);
    }

    public EntityEffect(EffectType effectType, WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        super(effectType, worldGameState, inputBitUtility);
    }

    public final void applyEntityEffect(Entity entity) {
        onEntityApplyEffect(entity);
        this.entity = entity;
    }
    public final void removeEntityEffect() {
        onEntityRemoveEffect(entity);
        this.entity = null;
    }

    public abstract void onEntityApplyEffect(Entity entity);
    public abstract void onEntityRemoveEffect(Entity entity);

    public Entity getEntity() {
        return entity;
    }
}
