package com.GlitchyDev.Rendering.Assets.Mesh;


import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Transformation;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Direction;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;

public class PartialCubicInstanceMesh extends InstancedMesh {

    protected InstancedGridTexture instancedGridTexture;
    protected final int textureVboId;
    protected FloatBuffer textureBuffer;
    protected FloatBuffer textureVboData;

    public PartialCubicInstanceMesh(Mesh mesh, int instanceChunkSize, InstancedGridTexture instancedGridTexture) {
        super(mesh, instanceChunkSize);
        this.instancedGridTexture = instancedGridTexture;
        mesh.setTexture(instancedGridTexture);

        textureBuffer = BufferUtils.createFloatBuffer((2 * 6) * instanceChunkSize);
        textureVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
        glBufferData(GL_ARRAY_BUFFER, (2 * 6) * 4 * instanceChunkSize, GL_STREAM_DRAW);
        vboIdList.add(textureVboId);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        addInstancedAttribute(getVaoId(), textureVboId, 6, 2, 2, 0);
        textureVboData = BufferUtils.createFloatBuffer(instanceChunkSize * 2);
    }

    @Override
    public void preRender() {
        super.preRender();
        GL20.glEnableVertexAttribArray(6);
    }

    @Override
    public void postRender() {
        super.preRender();
        glDisableVertexAttribArray(6);
    }

    private ArrayList<Matrix4f> modelViewMatrices = new ArrayList<>();
    private ArrayList<Vector2f> textureCords = new ArrayList<>();
    int apple = 0;
    public void renderPartialCubicBlocksInstanced(List<DesignerBlock> designerBlocks, Transformation transformation, Matrix4f viewMatrix) {
        preRender();

        apple++;
        // Collect all the rotations from each block

        modelViewMatrices.clear();
        textureCords.clear();

        for(DesignerBlock block: designerBlocks) {
            for(Direction direction: Direction.values()) {
                if(block.getFaceState(direction)) {
                    Vector3f rotation;
                    switch(direction) {
                        case ABOVE:
                            rotation = new Vector3f(0,90,0);
                            break;
                        case BELOW:
                            rotation = new Vector3f(180,90,0);
                            break;
                        case NORTH:
                            rotation = new Vector3f(90,-90,0);
                            break;
                        case EAST:
                            rotation = new Vector3f(0, 0,90);
                            break;
                        case SOUTH:
                            rotation = new Vector3f(90, -270,180);
                            break;
                        case WEST:
                            rotation = new Vector3f(180, apple,270);
                            break;
                        default:
                            rotation = new Vector3f();
                            break;
                    }
                    Matrix4f modelMatrix = transformation.buildModelMatrix(block.getLocation().getNormalizedPosition(),rotation);
                    System.out.println(modelMatrix);
                    System.out.println();

                    modelViewMatrices.add(transformation.buildModelViewMatrix(modelMatrix, viewMatrix));



                    int num = block.getTextureID(direction);
                    int x = num % instancedGridTexture.getHorizontalGridNam();
                    int y = num / instancedGridTexture.getHorizontalGridNam();
                    textureCords.add(new Vector2f(x,y));
                } 
            }
        }

        int length = modelViewMatrices.size();
        for (int i = 0; i < length; i += instanceChunkSize) {
            int end = Math.min(length, i + instanceChunkSize);
            renderPartialCubicBlocksInstanced(modelViewMatrices.subList(i, end), textureCords.subList(i, end), end-i);
        }

        postRender();
    }






    private void renderPartialCubicBlocksInstanced(List<Matrix4f> blocks, List<Vector2f> textureCords, int size) {
        matrixVboData.clear();
        textureVboData.clear();

        int offset = 0;
        System.out.println("*");
        for(int i = 0; i < size; i++) {
            System.out.println(i + " " + blocks.get(i));
            blocks.get(i).get(offset * 16, matrixVboData);
            textureCords.get(i).get(offset * 2, textureVboData);
            offset++;
        }
        updateVBO(matrixVboId, matrixVboData, matrixBuffer);
        updateVBO(textureVboId, textureVboData, textureBuffer);
        glDrawElementsInstanced(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, size);
    }


    public InstancedGridTexture getInstancedGridTexture() {
        return instancedGridTexture;
    }


}
