package com.GlitchyDev.World.Navigation;

import com.GlitchyDev.World.Location;

public interface NavigableBlock {

    ConnectionNode getConnectionNode();
    void recalculateConnections();
    Location getLocation();

}
