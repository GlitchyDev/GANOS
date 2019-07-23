package com.GlitchyDev.Rendering.Assets.Mesh;


import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import com.GlitchyDev.Rendering.Assets.WorldElements.Transformation;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Lighting.LightingManager;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
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
    protected final FloatBuffer textureBuffer;
    protected final FloatBuffer textureVboData;

    protected final int lightVboId;
    protected final FloatBuffer lightBuffer;
    protected final FloatBuffer lightVboData;

    public PartialCubicInstanceMesh(Mesh mesh, int instanceChunkSize, InstancedGridTexture instancedGridTexture) {
        super(mesh, instanceChunkSize);
        this.instancedGridTexture = instancedGridTexture;
        mesh.setTexture(instancedGridTexture);

        textureBuffer = BufferUtils.createFloatBuffer((2 * 6) * instanceChunkSize);
        textureVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, textureVboId);
        glBufferData(GL_ARRAY_BUFFER, (2 * 6) * instanceChunkSize, GL_STREAM_DRAW);
        vboIdList.add(textureVboId);
        addInstancedAttribute(getVaoId(), textureVboId, 6, 2, 2, 0);
        textureVboData = BufferUtils.createFloatBuffer(instanceChunkSize * 2);

        lightBuffer = BufferUtils.createFloatBuffer((1 * 6) * instanceChunkSize);
        lightVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, lightVboId);
        glBufferData(GL_ARRAY_BUFFER, (1 * 6) * instanceChunkSize, GL_STREAM_DRAW);
        vboIdList.add(lightVboId);
        addInstancedAttribute(getVaoId(), lightVboId, 7, 1, 1, 0);
        lightVboData = BufferUtils.createFloatBuffer(instanceChunkSize);
    }

    @Override
    public void preRender() {
        super.preRender();
        GL20.glEnableVertexAttribArray(6);
        GL20.glEnableVertexAttribArray(7);
    }

    @Override
    public void postRender() {
        super.preRender();
        glDisableVertexAttribArray(6);
        glDisableVertexAttribArray(7);
    }

    private ArrayList<Matrix4f> modelViewMatrices = new ArrayList<>();
    private ArrayList<Vector2f> textureCords = new ArrayList<>();
    private ArrayList<Float> lights = new ArrayList<>();
    public void renderPartialCubicBlocksInstanced(List<DesignerBlock> designerBlocks, Transformation transformation, Matrix4f viewMatrix) {
        preRender();

        // Collect all the rotations from each block

        modelViewMatrices.clear();
        textureCords.clear();
        lights.clear();

        for(DesignerBlock block: designerBlocks) {
            for(Direction direction: Direction.getCompleteCardinal()) {
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
                            rotation = new Vector3f(180, 0,270);
                            break;
                        default:
                            rotation = new Vector3f();
                            break;
                    }
                    Matrix4f modelMatrix = transformation.buildModelMatrix(block.getLocation().getNormalizedPosition(),rotation);
                    Matrix4f modelViewMatrix = new Matrix4f(transformation.buildModelViewMatrix(modelMatrix, viewMatrix));
                    modelViewMatrices.add(modelViewMatrix);



                    int num = block.getTextureID(direction);
                    int x = num % instancedGridTexture.getHorizontalGridNam();
                    int y = num / instancedGridTexture.getHorizontalGridNam();
                    textureCords.add(new Vector2f(x,y));

                    lights.add(LightingManager.getLightPercentage(block.getLightLevel(direction)));
                }
            }
        }

        int length = modelViewMatrices.size();
        for (int i = 0; i < length; i += instanceChunkSize) {
            int end = Math.min(length, i + instanceChunkSize);
            renderPartialCubicBlocksInstanced(modelViewMatrices.subList(i, end), textureCords.subList(i, end),lights, end-i);
        }

        postRender();
    }






    private void renderPartialCubicBlocksInstanced(List<Matrix4f> blocks, List<Vector2f> textureCords, ArrayList<Float> lights, int size) {
        matrixVboData.clear();
        textureVboData.clear();
        lightVboData.clear();

        for(int i = 0; i < size; i++) {
            blocks.get(i).get(i * 16, matrixVboData);
            textureCords.get(i).get(i * 2, textureVboData);
            lightVboData.put(i, lights.get(i));
        }


        updateVBO(matrixVboId, matrixVboData, matrixBuffer);
        updateVBO(textureVboId, textureVboData, textureBuffer);
        updateVBO(lightVboId, lightVboData, lightBuffer);
        glDrawElementsInstanced(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, size);
    }


    public void setInstancedGridTexture(InstancedGridTexture instancedGridTexture) {
        this.instancedGridTexture = instancedGridTexture;
        setTexture(instancedGridTexture);
    }

    public InstancedGridTexture getInstancedGridTexture() {
        return instancedGridTexture;
    }
}
