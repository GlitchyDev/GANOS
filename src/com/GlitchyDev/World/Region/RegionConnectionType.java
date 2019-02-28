package com.GlitchyDev.World.Region;

public enum RegionConnectionType {
    NORMAL(true),
    HIDDEN_DEBUG_1(false),
    VISIBLE_DEBUG_1(true),


    ;
    private final boolean isVisibleByDefault;
    RegionConnectionType(boolean isVisibleByDefault) {
        this.isVisibleByDefault = isVisibleByDefault;
    }

    public boolean isVisibleByDefault() {
        return isVisibleByDefault;
    }
}

