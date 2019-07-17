package com.GlitchyDev.World.Navigation;

import com.GlitchyDev.World.Location;

public interface NavigableBlock {

    ConnectionNode getConnectionNode();
    // Register the actual blocks internally, since you can't do that till regions are finalized
    void initializeConnections();
    // Recalculate where your connections are supposed to be
    Location getLocation();

}
