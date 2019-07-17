package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Navigation.ConnectionNode;
import com.GlitchyDev.World.Navigation.NavigableBlock;
import com.GlitchyDev.World.Navigation.PathType;
import com.GlitchyDev.World.Region.Enum.RegionConnection;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class DebugNavigationBlock extends DesignerBlock implements NavigableBlock {
    private final ConnectionNode connectionNode;

    public DebugNavigationBlock(WorldGameState worldGameState, Location location, UUID regionUUID, InstancedGridTexture instancedGridTexture) {
        super(BlockType.DEBUG_NAVIGATION_BLOCK,worldGameState, location, regionUUID, instancedGridTexture);
        this.connectionNode = new ConnectionNode(this);
    }

    public DebugNavigationBlock(WorldGameState worldGameState, InputBitUtility inputBitUtility, UUID regionUUID, InstancedGridTexture instancedGridTexture) throws IOException {
        super(BlockType.DEBUG_NAVIGATION_BLOCK, worldGameState, inputBitUtility, regionUUID, instancedGridTexture);
        this.connectionNode = new ConnectionNode(this);
    }

    @Override
    public Block getCopy() {
        return new DebugNavigationBlock(worldGameState, location, regionUUID, getInstancedGridTexture());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBlockType());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DebugNavigationBlock;
    }

    @Override
    public ConnectionNode getConnectionNode() {
        return connectionNode;
    }

    @Override
    public void initializeConnections() {
        // This block is only looking at the 4 Cardinal Locations
        for(Direction direction: Direction.getCardinal()) {
            Location directionLocation = location.getOffsetDirectionLocation(direction);

            // Does a block exist here at all
            if(worldGameState.isBlockAtLocation(directionLocation)) {
                // If this block is in the same region we don't need to check for magical multi-overlap region nonsense
                if(worldGameState.isLocationInSameRegion(directionLocation,getRegionUUID())) {
                    Block block = worldGameState.getBlockAtLocation(directionLocation);
                    if(block instanceof DebugNavigationBlock) {
                        connectionNode.getPathConnections().put(((DebugNavigationBlock) block).getConnectionNode(), PathType.FLOOR);
                    }
                } else {
                    // Multiple Overlap regions! Quick add a connection type for each region interfering in our grand plans
                    Region hostRegion = worldGameState.getRegion(getRegionUUID(),location.getWorldUUID());
                    HashMap<RegionConnection,ArrayList<UUID>> hostRegionConnections = worldGameState.getRegionConnections(location.getWorldUUID()).get(hostRegion);

                    ArrayList<Region> regions = worldGameState.getRegionsAtLocation(directionLocation);
                    for(Region region: regions) {
                        Block block = worldGameState.getBlockAtLocation(directionLocation,region.getRegionUUID());
                        // Is this block even worth checking
                        if(block instanceof DebugNavigationBlock) {

                            // Lets find out how this blocks region is connected to our host blocks ._.
                            RegionConnection blockRegionConnection = null;
                            for (RegionConnection regionConnection : hostRegionConnections.keySet()) {
                                if (hostRegionConnections.get(regionConnection).contains(region.getRegionUUID())) {
                                    blockRegionConnection = regionConnection;
                                }
                            }

                            switch (blockRegionConnection) {
                                case NORMAL:
                                    connectionNode.getPathConnections().put(((DebugNavigationBlock) block).getConnectionNode(), PathType.FLOOR);
                                    break;
                                default:
                                    connectionNode.getPathConnections().put(((DebugNavigationBlock) block).getConnectionNode(), PathType.DEBUG);
                                    break;
                            }
                        }
                    }
                }
            }
        }

        // Now we set our top plate of our block to show our current region connection, lol


    }


}
