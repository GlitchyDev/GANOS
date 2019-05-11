package com.GlitchyDev.Rendering.Assets.Mesh;


import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;

public class PartialCubicInstanceMesh extends InstancedMesh {

    protected InstancedGridTexture instancedGridTexture;
    protected final int textureVboId;
    protected FloatBuffer textureBuffer;
    protected FloatBuffer textureVboData;

    public PartialCubicInstanceMesh(Mesh mesh, int instanceChunkSize, InstancedGridTexture instancedGridTexture) {
        super(mesh, instanceChunkSize);
        this.instancedGridTexture = instancedGridTexture;

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
        GL30.glBindVertexArray(getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, instancedGridTexture.getId());
    }

    @Override
    public void postRender() {
        super.postRender();
        glDisableVertexAttribArray(6);
    }


    /*
    private ArrayList<Matrix4f> modelViewMatrices = new ArrayList<>();
    private ArrayList<Vector2f> textureCords = new ArrayList<>();
    boolean[] faces;
    Vector3f rotation;
    public void renderPartialCubicBlocksInstanced(List<PartialCubicBlock> blocks, Transformation transformation, Matrix4f viewMatrix)
    {
        preRender();

        // Collect all the rotations from each block

        modelViewMatrices.clear();
        textureCords.clear();

        for(PartialCubicBlock block: blocks) {
            faces = block.getFaceStates();
            for(int i = 0; i < 6; i++) {
                if(faces[i]) {
                    rotation = new Vector3f(block.getRotation());
                    switch(i) {
                        case 0:
                            // 0
                            rotation.add(0,90,0);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 1:
                            // 1
                            rotation.add(180,90,0);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 2:
                            // 2*
                            rotation.add(90,-90,0);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 3:
                            // 3*
                            rotation.add(0, 0,90);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 4:
                            // 4*
                            rotation.add(90, -270,180);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                        case 5:
                            // 5*
                            rotation.add(180, 0,270);
                            modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                            break;
                    }

                    int num = block.getAssignedTextures()[i];
                    int x = num % instancedGridTexture.getTextureGridWidth();
                    int y = num / instancedGridTexture.getTextureGridWidth();

                    textureCords.add(new Vector2f(x,y));
                }
            }
        }
        int length = modelViewMatrices.size();
        for (int i = 0; i < length; i += instanceChunkSize) {
            int end = Math.min(length, i + instanceChunkSize);
            renderPartialCubicBlocksInstanced(modelViewMatrices.subList(i, end), textureCords.subList(i, end), end-i);
        }
    }


    int blocksRendered = 0;
    int blocksIgnored = 0;
    public void renderPartialCubicBlocksInstancedChunk(Collection<Chunk> chunks, Transformation transformation, Matrix4f viewMatrix, boolean useFrustumCullingFilter)
    {
        preRender();

        // Collect all the rotations from each block

        modelViewMatrices.clear();
        textureCords.clear();
        blocksRendered = 0;
        blocksIgnored = 0;



        for(Chunk chunk: chunks) {
            for(BlockBase[][] blockSelection: chunk.getBlocks()) {
                for(BlockBase[] blockLine: blockSelection) {
                    for(BlockBase block: blockLine) {
                        if(block != null && block instanceof PartialCubicBlock) {
                            if(!useFrustumCullingFilter || block.isInsideFrustum()) {
                                blocksRendered++;
                                faces = ((PartialCubicBlock) block).getFaceStates();
                                for (int i = 0; i < 6; i++) {
                                    if (faces[i]) {
                                        rotation = new Vector3f(block.getRotation());
                                        switch (i) {
                                            case 0:
                                                // 0 Top
                                                rotation.add(0, 90, 0);
                                                modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                                                break;
                                            case 1:
                                                // 1 Bottom
                                                rotation.add(180, 90, 0);
                                                modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                                                break;
                                            case 2:
                                                // 2 North
                                                rotation.add(90, -90, 0);
                                                modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                                                break;
                                            case 3:
                                                // 3 East
                                                rotation.add(0, 0, 90);
                                                modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                                                break;
                                            case 4:
                                                // 4 South
                                                rotation.add(90, -270, 180);
                                                modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                                                break;
                                            case 5:
                                                // 5 West
                                                rotation.add(180, 0, 270);
                                                modelViewMatrices.add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                                                break;
                                        }
                                        int num = ((PartialCubicBlock) block).getAssignedTextures()[i];
                                        int x = num % instancedGridTexture.getTextureGridWidth();
                                        int y = num / instancedGridTexture.getTextureGridWidth();

                                        textureCords.add(new Vector2f(x, y));
                                    }
                                }


                            }
                            else {
                                blocksIgnored++;
                            }
                        }
                    }
                }
            }
        }


        int length = modelViewMatrices.size();
        for (int i = 0; i < length; i += instanceChunkSize) {
            int end = Math.min(length, i + instanceChunkSize);
            renderPartialCubicBlocksInstanced(modelViewMatrices.subList(i, end), textureCords.subList(i, end), end-i);
        }
    }



    public void renderPartialCubicBlocksInstancedChunkTextures(ShaderProgram shader, Collection<Chunk> chunks, Transformation transformation, Matrix4f viewMatrix, boolean useFrustumCullingFilter)
    {
        preRender();


        // Collect all the rotations from each block

        blocksRendered = 0;
        blocksIgnored = 0;


        HashMap<InstancedGridTexture,ArrayList<Matrix4f>> textureGroupingModels = new HashMap<>();
        HashMap<InstancedGridTexture,ArrayList<Vector2f>> textureGroupingTextures = new HashMap<>();


        for(Chunk chunk: chunks) {
            for(BlockBase[][] blockSelection: chunk.getBlocks()) {
                for(BlockBase[] blockLine: blockSelection) {
                    for(BlockBase block: blockLine) {
                        if(block != null && block instanceof PartialCubicBlock) {
                            if(!useFrustumCullingFilter || block.isInsideFrustum()) {

                                PartialCubicBlock partialCubicBlock = (PartialCubicBlock) block;
                                // ****************************
                                blocksRendered++;
                                faces = ((PartialCubicBlock) block).getFaceStates();
                                for (int i = 0; i < 6; i++) {
                                    if (faces[i]) {
                                        rotation = new Vector3f(block.getRotation());
                                        switch (i) {
                                            case 0:
                                                // 0 Top
                                                rotation.add(0, 90, 0);
                                                if(!textureGroupingModels.containsKey(partialCubicBlock.getInstancedGridTexture())) {
                                                    textureGroupingModels.put(partialCubicBlock.getInstancedGridTexture(),new ArrayList<>());
                                                }
                                                textureGroupingModels.get(partialCubicBlock.getInstancedGridTexture()).add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));
                                                break;
                                            case 1:
                                                // 1 Bottom
                                                rotation.add(180, 90, 0);
                                                if(!textureGroupingModels.containsKey(partialCubicBlock.getInstancedGridTexture())) {
                                                    textureGroupingModels.put(partialCubicBlock.getInstancedGridTexture(),new ArrayList<>());
                                                }
                                                textureGroupingModels.get(partialCubicBlock.getInstancedGridTexture()).add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));                                                break;
                                            case 2:
                                                // 2 North
                                                rotation.add(90, -90, 0);
                                                if(!textureGroupingModels.containsKey(partialCubicBlock.getInstancedGridTexture())) {
                                                    textureGroupingModels.put(partialCubicBlock.getInstancedGridTexture(),new ArrayList<>());
                                                }
                                                textureGroupingModels.get(partialCubicBlock.getInstancedGridTexture()).add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));                                                break;
                                            case 3:
                                                // 3 East
                                                rotation.add(0, 0, 90);
                                                if(!textureGroupingModels.containsKey(partialCubicBlock.getInstancedGridTexture())) {
                                                    textureGroupingModels.put(partialCubicBlock.getInstancedGridTexture(),new ArrayList<>());
                                                }
                                                textureGroupingModels.get(partialCubicBlock.getInstancedGridTexture()).add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));                                                break;
                                            case 4:
                                                // 4 South
                                                rotation.add(90, -270, 180);
                                                if(!textureGroupingModels.containsKey(partialCubicBlock.getInstancedGridTexture())) {
                                                    textureGroupingModels.put(partialCubicBlock.getInstancedGridTexture(),new ArrayList<>());
                                                }
                                                textureGroupingModels.get(partialCubicBlock.getInstancedGridTexture()).add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));                                                break;
                                            case 5:
                                                // 5 West
                                                rotation.add(180, 0, 270);
                                                if(!textureGroupingModels.containsKey(partialCubicBlock.getInstancedGridTexture())) {
                                                    textureGroupingModels.put(partialCubicBlock.getInstancedGridTexture(),new ArrayList<>());
                                                }
                                                textureGroupingModels.get(partialCubicBlock.getInstancedGridTexture()).add(transformation.getModelViewMatrix(block.getNormalizedPosition(), rotation, block.getScale(), viewMatrix));                                                break;
                                        }
                                        int num = ((PartialCubicBlock) block).getAssignedTextures()[i];
                                        int x = num % partialCubicBlock.getInstancedGridTexture().getTextureGridWidth();
                                        int y = num / partialCubicBlock.getInstancedGridTexture().getTextureGridWidth();

                                        if(!textureGroupingTextures.containsKey(partialCubicBlock.getInstancedGridTexture())) {
                                            textureGroupingTextures.put(partialCubicBlock.getInstancedGridTexture(), new ArrayList<>());
                                        }
                                        textureGroupingTextures.get(partialCubicBlock.getInstancedGridTexture()).add(new Vector2f(x, y));
                                    }
                                }


                            }
                            else {
                                blocksIgnored++;
                            }
                        }
                    }
                }
            }
        }


        for(InstancedGridTexture instanceGridTexture: textureGroupingModels.keySet()) {
            glBindTexture(GL_TEXTURE_2D, instanceGridTexture.getId());

            shader.setUniform("textureGridSize",new Vector2f(instanceGridTexture.getTextureGridWidth(),instanceGridTexture.getTextureGridHeight()));

            ArrayList<Matrix4f> models = textureGroupingModels.get(instanceGridTexture);
            ArrayList<Vector2f> textures = textureGroupingTextures.get(instanceGridTexture);

            int length = models.size();
            for (int i = 0; i < length; i += instanceChunkSize) {
                int end = Math.min(length, i + instanceChunkSize);
                renderPartialCubicBlocksInstanced(models.subList(i, end), textures.subList(i, end), end-i);
            }
        }
    }

    private void renderPartialCubicBlocksInstanced(List<Matrix4f> blocks, List<Vector2f> textureCords, int size)
    {
        matrixVboData.clear();
        textureVboData.clear();

        int offset = 0;
        for(int i = 0; i < size; i++) {
            blocks.get(i).get(offset * 16, matrixVboData);
            textureCords.get(i).get(offset * 2, textureVboData);
            offset++;
        }
        updateVBO(matrixVboId, matrixVboData, matrixBuffer);
        updateVBO(textureVboId, textureVboData, textureBuffer);
        glDrawElementsInstanced(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, size);
    }

    */

    public InstancedGridTexture getInstancedGridTexture() {
        return instancedGridTexture;
    }

    /*

    public int getBlocksIgnored() {
        return blocksIgnored;
    }

    public int getBlocksRendered() {
        return blocksRendered;
    }

    */
}
