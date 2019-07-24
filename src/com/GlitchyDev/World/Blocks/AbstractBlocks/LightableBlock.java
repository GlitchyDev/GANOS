package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

public interface LightableBlock {
    // A light minimum set by Static Lighting or Dynamic lighting
    void setStaticLightLevel(Direction direction, int lightLevel);
    void setDynamicLightLevel(Direction direction, int lightLevel);
    void setCurrentLightLevel(Direction direction, int lightLevel);


    // To check if a light is minimal
    int getStaticLightLevel(Direction direction);
    int getDynamicLightLevel(Direction direction);
    int getCurrentLightLevel(Direction direction);
    int getPreviousLightLevel(Direction direction);

    // Since resetting Static lights is unnecessary
    void resetDynamicLight();
    void finalizeLight();

    Location getLocation();
}
