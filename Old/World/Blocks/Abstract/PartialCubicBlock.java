package com.GlitchyDev.Old.World.Blocks.Abstract;

import com.GlitchyDev.Old.Rendering.Assets.InstancedGridTexture;
import com.GlitchyDev.Old.World.Blocks.BlockType;
import com.GlitchyDev.Old.World.Direction;
import com.GlitchyDev.Old.World.Location;
import org.joml.Vector3f;



/**
 * A BlockType meant to be a 6 sided cube, that can have the exture and mesh of any particular face removed/changed as needed. Used for map building
 */
public class PartialCubicBlock extends BlockBase {
    // Assigned Pool Grid Texture
    private InstancedGridTexture instancedGridTexture;
    // Be 6 sized,
    private boolean[] faceStates;
    // -1 for non texture
    private int[] assignedTextures;
    //private ArrayList<String> modifiers;



    public PartialCubicBlock(Location location) {
        super(BlockType.PARTIAL_CUBIC_BLOCK, location);

        rotation = new Vector3f();

        this.faceStates = new boolean[6];
        for(int index = 0; index < 6; index++)
        {
            faceStates[index] = true;
        }
        this.assignedTextures = new int[6];
        //readObject(aInputStream);

    }


    // Unused?
    public PartialCubicBlock(Location location, Vector3f rotation, InstancedGridTexture instancedGridTexture, boolean[] faceStates, int[] assignedTextures)
    {
        super(BlockType.PARTIAL_CUBIC_BLOCK, location, rotation);
        this.instancedGridTexture = instancedGridTexture;
        this.faceStates = faceStates;
        this.assignedTextures = assignedTextures;
        //this.modifiers = modifiers;
    }

    public PartialCubicBlock(Location location, InstancedGridTexture instancedGridTexture, boolean[] faceStates, int[] assignedTextures)
    {
        super(BlockType.PARTIAL_CUBIC_BLOCK, location);
        rotation = new Vector3f();
        this.instancedGridTexture = instancedGridTexture;
        this.faceStates = faceStates;
        this.assignedTextures = assignedTextures;
        //this.modifiers = modifiers;
    }

    // More Likely to be used
    public PartialCubicBlock(Location location, InstancedGridTexture instancedGridTexture)
    {
        super(BlockType.PARTIAL_CUBIC_BLOCK, location);
        this.instancedGridTexture = instancedGridTexture;

        rotation = new Vector3f();
        this.faceStates = new boolean[6];

        for(int index = 0; index < 6; index++)
        {
            faceStates[index] = true;
        }
        this.assignedTextures = new int[6];
    }


    public boolean[] getFaceStates() {
        return faceStates;
    }

    public int[] getAssignedTextures() {
        return assignedTextures;
    }

    /**
     * Return true if this Block contributes nothing to the current world
     * @return
     */
    public boolean isUseless()
    {
        return (!(getAboveFaceState() ||  getBelowFaceState() || getNorthFaceState() || getEastFaceState() || getSouthFaceState() || getWestFaceState()));
    }

    // Face States
    public void setFaceStates(boolean[] faceStates) {
        this.faceStates = faceStates;
    }

    public void setAboveFaceState(boolean state)
    {
        faceStates[0] = state;
    }
    public void setBelowFaceState(boolean state)
    {
        faceStates[1] = state;
    }
    public void setNorthFaceState(boolean state)
    {
        faceStates[2] = state;
    }
    public void setEastFaceState(boolean state)
    {
        faceStates[3] = state;
    }
    public void setSouthFaceState(boolean state)
    {
        faceStates[4] = state;
    }
    public void setWestFaceState(boolean state)
    {
        faceStates[5] = state;
    }

    public void setDirectionFaceState(Direction direction, boolean state)
    {
        switch(direction)
        {
            case ABOVE:
               setAboveFaceState(state);
                break;
            case BELOW:
                setBelowFaceState(state);
                break;
            case NORTH:
                setNorthFaceState(state);
                break;
            case EAST:
                setEastFaceState(state);
                break;
            case SOUTH:
                setSouthFaceState(state);
                break;
            case WEST:
                setWestFaceState(state);
                break;
        }
    }

    // Get Face States
    public boolean getAboveFaceState()
    {
        return faceStates[0];
    }
    public boolean getBelowFaceState()
    {
        return faceStates[1];
    }
    public boolean getNorthFaceState()
    {
        return faceStates[2];
    }
    public boolean getEastFaceState()
    {
        return faceStates[3];
    }
    public boolean getSouthFaceState()
    {
        return faceStates[4];
    }
    public boolean getWestFaceState()
    {
        return faceStates[5];
    }

    public boolean getDirectionFaceState(Direction direction)
    {
        switch(direction)
        {
            case ABOVE:
                return getAboveFaceState();
            case BELOW:
                return getBelowFaceState();
            case NORTH:
                return getNorthFaceState();
            case EAST:
                return getEastFaceState();
            case SOUTH:
                return getSouthFaceState();
            case WEST:
                return getWestFaceState();
            default:
                return getAboveFaceState();
        }
    }

    public void setAssignedTextures(int[] assignedTextures) {
        this.assignedTextures = assignedTextures;
    }

    public void setAboveTexture(int texture)
    {
        assignedTextures[0] = texture;
    }
    public void setBelowTexture(int texture)
    {
        assignedTextures[1] = texture;
    }
    public void setNorthTexture(int texture)
    {
        assignedTextures[2] = texture;
    }
    public void setEastTexture(int texture)
    {
        assignedTextures[3] = texture;
    }
    public void setSouthTexture(int texture)
    {
        assignedTextures[4] = texture;
    }
    public void setWestTexture(int texture)
    {
        assignedTextures[5] = texture;
    }

    public void setDirectionTexture(Direction direction, int texture)
    {
        switch(direction)
        {
            case ABOVE:
                setAboveTexture(texture);
                break;
            case BELOW:
                setBelowTexture(texture);
                break;
            case NORTH:
                setNorthTexture(texture);
                break;
            case EAST:
                setEastTexture(texture);
                break;
            case SOUTH:
                setSouthTexture(texture);
                break;
            case WEST:
                setWestTexture(texture);
                break;
        }
    }

    public int getAboveTexture()
    {
        return assignedTextures[0];
    }
    public int getBelowTexture()
    {
        return assignedTextures[1];
    }
    public int getNorthTexture()
    {
        return assignedTextures[2];
    }
    public int getEastTexture()
    {
        return assignedTextures[3];
    }
    public int getSouthTexture()
    {
        return assignedTextures[4];
    }
    public int getWestTexture()
    {
        return assignedTextures[5];
    }

    public int getDirectionTexture(Direction direction)
    {
        switch(direction)
        {
            case ABOVE:
                return getAboveTexture();
            case BELOW:
                return getBelowTexture();
            case NORTH:
                return getNorthTexture();
            case EAST:
                return getEastTexture();
            case SOUTH:
                return getSouthTexture();
            case WEST:
                return getWestTexture();
            default:
                return getAboveTexture();
        }
    }

    public InstancedGridTexture getInstancedGridTexture() {
        return instancedGridTexture;
    }

    public void setInstancedGridTexture(InstancedGridTexture instancedGridTexture) {
        this.instancedGridTexture = instancedGridTexture;
    }

    @Override
    public void rotate(Direction direction) {
        boolean[] orientation = getFaceStates();
        int[] assignedTextures = getAssignedTextures();

        switch(direction) {
            case NORTH:
                break;
            case EAST:
                setDirectionFaceState(Direction.NORTH,orientation[5]);
                setDirectionFaceState(Direction.EAST,orientation[2]);
                setDirectionFaceState(Direction.SOUTH,orientation[3]);
                setDirectionFaceState(Direction.WEST,orientation[4]);

                setDirectionTexture(Direction.NORTH,assignedTextures[5]);
                setDirectionTexture(Direction.EAST,assignedTextures[2]);
                setDirectionTexture(Direction.SOUTH,assignedTextures[3]);
                setDirectionTexture(Direction.WEST,assignedTextures[4]);
                break;
            case SOUTH:
                setDirectionFaceState(Direction.NORTH,orientation[4]);
                setDirectionFaceState(Direction.EAST,orientation[5]);
                setDirectionFaceState(Direction.SOUTH,orientation[2]);
                setDirectionFaceState(Direction.WEST,orientation[3]);

                setDirectionTexture(Direction.NORTH,assignedTextures[4]);
                setDirectionTexture(Direction.EAST,assignedTextures[5]);
                setDirectionTexture(Direction.SOUTH,assignedTextures[2]);
                setDirectionTexture(Direction.WEST,assignedTextures[3]);
                break;
            case WEST:
                setDirectionFaceState(Direction.NORTH,orientation[3]);
                setDirectionFaceState(Direction.EAST,orientation[4]);
                setDirectionFaceState(Direction.SOUTH,orientation[5]);
                setDirectionFaceState(Direction.WEST,orientation[2]);

                setDirectionTexture(Direction.NORTH,assignedTextures[3]);
                setDirectionTexture(Direction.EAST,assignedTextures[4]);
                setDirectionTexture(Direction.SOUTH,assignedTextures[5]);
                setDirectionTexture(Direction.WEST,assignedTextures[2]);
                break;
        }
    }

    /*
    public ArrayList<String> getModifiers() {
        return modifiers;
    }

    public void setModifiers(ArrayList<String> modifiers) {
        this.modifiers = modifiers;
    }
    */
}
