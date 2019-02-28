package com.GlitchyDev.Game.Player.Effects;

import com.GlitchyDev.Game.Player.Player;

public class EffectBase {
    private final EffectType effectType;
    private final Player player;

    public EffectBase(EffectType effectType, Player player) {
        this.effectType = effectType;
        this.player = player;
    }



    public EffectType getEffectType() {
        return effectType;
    }

    protected Player getPlayer() {
        return player;
    }
}
