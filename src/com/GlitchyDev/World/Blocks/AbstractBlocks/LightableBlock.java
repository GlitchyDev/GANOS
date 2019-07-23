package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;

public interface LightableBlock {
    void setBlockLight(Direction direction, int lightLevel);
    int getLightLevel(Direction direction);
    void resetLight();
    Location getLocation();
}
