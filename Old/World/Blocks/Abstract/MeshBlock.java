package com.GlitchyDev.Old.World.Blocks.Abstract;

import com.GlitchyDev.Old.Rendering.Assets.Mesh;
import com.GlitchyDev.Old.World.Blocks.BlockType;
import com.GlitchyDev.Old.World.Direction;
import com.GlitchyDev.Old.World.Location;
import org.joml.Vector3f;

/**
 * A block with a specified mesh
 */
public class MeshBlock extends BlockBase {
    private Mesh mesh;

    public MeshBlock(BlockType blockType, Location location, Vector3f rotation, Mesh mesh) {
        super(BlockType.MESH, location, rotation);
        this.mesh = mesh;
    }

    @Override
    public boolean isUseless() {
        return true;
    }

    @Override
    public void rotate(Direction direction) {
        switch(direction) {
            case NORTH:
                setRotation(new Vector3f(0,0,0));
                break;
            case EAST:
                setRotation(new Vector3f(0,90,0));
                break;
            case SOUTH:
                setRotation(new Vector3f(0,180,0));
                break;
            case WEST:
                setRotation(new Vector3f(0,270,0));
                break;
        }
    }


    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }
}
