package com.GlitchyDev.World.Effects.Abstract;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;

public abstract class EntityEffect extends Effect {
    private Entity entity;

    public EntityEffect(EffectType effectType, boolean isReplicated, WorldGameState worldGameState) {
        super(effectType, isReplicated, worldGameState);
    }

    public EntityEffect(EffectType effectType, boolean isReplicated, WorldGameState worldGameState, InputBitUtility inputBitUtility) {
        super(effectType, isReplicated, worldGameState, inputBitUtility);
    }

    public final void applyEntityEffect(Entity entity) {
        onEntityApplyEffect(entity);
        this.entity = entity;
    }
    public final void removeEntityEffect() {
        onEntityRemoveEffect(entity);
        this.entity = null;
    }

    protected abstract void onEntityApplyEffect(Entity entity);
    protected abstract void onEntityRemoveEffect(Entity entity);

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
