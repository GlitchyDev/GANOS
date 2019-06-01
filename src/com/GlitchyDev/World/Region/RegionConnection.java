package com.GlitchyDev.World.Region;

public enum RegionConnection {
    NORMAL(true),
    HIDDEN_DEBUG_1(false),
    VISIBLE_DEBUG_1(true),


    ;

    private final boolean isVisibleByDefault;
    RegionConnection(boolean isVisibleByDefault) {
        this.isVisibleByDefault = isVisibleByDefault;
    }

    public boolean isVisibleByDefault() {
        return isVisibleByDefault;
    }
}

