package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
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

public class DebugNavigationBlock extends DesignerBlock implements NavigableBlock, TickableBlock {
    private final ConnectionNode connectionNode;
    private int textureValue = 0;

    public DebugNavigationBlock(WorldGameState worldGameState, Location location, UUID regionUUID) {
        super(BlockType.DEBUG_NAVIGATION_BLOCK,worldGameState, location, regionUUID, AssetLoader.getInstanceGridTexture("Navigation_Grid"));
        this.connectionNode = new ConnectionNode(this);
    }

    public DebugNavigationBlock(WorldGameState worldGameState, InputBitUtility inputBitUtility, UUID regionUUID) throws IOException {
        super(BlockType.DEBUG_NAVIGATION_BLOCK, worldGameState, inputBitUtility, regionUUID, AssetLoader.getInstanceGridTexture("Navigation_Grid"));
        this.connectionNode = new ConnectionNode(this);
    }

    @Override
    public Block getCopy() {
        return new DebugNavigationBlock(worldGameState, location, regionUUID);
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
        int count = 0;
        for(Direction direction: Direction.getCardinal()) {
            Location directionLocation = location.getOffsetDirectionLocation(direction);


                    // Does a block exist here at all
            boolean foundDirection = false;
            if(worldGameState.isBlockAtLocation(directionLocation)) {
                // If this block is in the same region we don't need to check for magical multi-overlap region nonsense
                if(worldGameState.isLocationInSameRegion(directionLocation,getRegionUUID())) {
                    Block foundBlock = worldGameState.getBlockAtLocation(directionLocation);
                    if(foundBlock instanceof DebugNavigationBlock) {
                        connectionNode.getPathConnections().put(((DebugNavigationBlock) foundBlock).getConnectionNode(), PathType.FLOOR);
                        foundDirection = true;
                    }
                } else {
                    // Multiple Overlap regions! Quick add a connection type for each region interfering in our grand plans
                    Region hostRegion = worldGameState.getRegion(getRegionUUID(),location.getWorldUUID());

                    HashMap<RegionConnection,ArrayList<UUID>> hostRegionConnections = worldGameState.getRegionConnections(location.getWorldUUID()).get(hostRegion.getRegionUUID());

                    ArrayList<Region> regions = worldGameState.getRegionsAtLocation(directionLocation);
                    for(Region region: regions) {
                        Block foundBlock = worldGameState.getBlockAtLocation(directionLocation,region.getRegionUUID());
                        // Is this block even worth checking
                        if(foundBlock instanceof DebugNavigationBlock) {


                            // Lets find out how this blocks region is connected to our host blocks ._.
                            RegionConnection blockRegionConnection = null;
                            for (RegionConnection regionConnection : hostRegionConnections.keySet()) {
                                if (hostRegionConnections.get(regionConnection).contains(region.getRegionUUID())) {
                                    blockRegionConnection = regionConnection;
                                }
                            }

                            switch (blockRegionConnection) {
                                case NORMAL:
                                    connectionNode.getPathConnections().put(((DebugNavigationBlock) foundBlock).getConnectionNode(), PathType.FLOOR);
                                    foundDirection = true;
                                    break;
                                default:
                                    connectionNode.getPathConnections().put(((DebugNavigationBlock) foundBlock).getConnectionNode(), PathType.DEBUG);
                                    foundDirection = true;
                                    break;
                            }
                        }
                    }
                }
            }
            textureValue += foundDirection ? Math.pow(2.0,count) : 0;

            count++;
        }

        setFaceState(Direction.ABOVE,true);
        setTextureID(Direction.ABOVE,textureValue);

        // Now we set our top plate of our block to show our current region connection, lol


    }


    @Override
    public void tick() {
        if(connectionNode.getPreviousNode() == null) {
            setInstancedGridTexture(AssetLoader.getInstanceGridTexture("Navigation_Grid_Unused"));
        } else {
            setInstancedGridTexture(AssetLoader.getInstanceGridTexture("Navigation_Grid"));
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
