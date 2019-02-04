package com.GlitchyDev.Old.World.Blocks.Abstract;

import com.GlitchyDev.Old.World.Blocks.BlockType;
import com.GlitchyDev.Old.World.Direction;
import com.GlitchyDev.Old.World.Location;
import org.joml.Vector3i;

/**
 * A Block, the basic building units of the world map. Blocks should contain and run its own logic for
 * - Rendering
 * - Ticking
 * - Pathfinding ( Like a portal block should provide pathfinding AI the connection detail to its other side
 */
public abstract class BlockBase {
    protected final BlockType blockType;
    // Serialize only as
    protected Direction direction;
    protected float scale;
    // DO NOT serialize, as it is relative to the position of its accompanying region
    protected Location location;
    private boolean isDisableFrustumCulling = false;
    private boolean insideFrustum = false;

    public BlockBase(BlockType blockType, Location location, Direction direction) {
        this.blockType = blockType;
        this.location = location;
        this.direction = direction;
        this.scale = 1.0f;
    }
    public BlockBase(BlockType blockType, Location location) {
        this.blockType = blockType;
        this.location = location;
        this.direction = Direction.NORTH;
        this.scale = 1.0f;
    }

    public abstract boolean isUseless();

    public abstract void rotate(Direction direction);


    public BlockType getBlockType() {
        return blockType;
    }

    public Direction getDirection()
    {
        return direction;
    }

    public float getScale() {
        return scale;
    }

    public Location getLocation()
    {
        return location;
    }

    public Vector3i getNormalizedPosition(){
        return new Vector3i(location.getPosition()).mul(2);
    }

    public boolean isDisableFrustumCulling() {
        return isDisableFrustumCulling;
    }

    public boolean isInsideFrustum() {
        return insideFrustum;
    }




    public void setDirection(Direction direction)
    {
        this.direction = direction;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setInsideFrustum(boolean insideFrustum) {
        this.insideFrustum = insideFrustum;
    }

    public void setDisableFrustumCulling(boolean disableFrustumCulling) {
        isDisableFrustumCulling = disableFrustumCulling;
    }

    // Add Serialization
}
