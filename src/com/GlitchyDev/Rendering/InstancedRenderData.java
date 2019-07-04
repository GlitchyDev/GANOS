package com.GlitchyDev.Rendering;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;
import static org.lwjgl.opengl.GL33C.glVertexAttribDivisor;

public class InstancedRenderData {
    private int vaoID;

    private FloatBuffer matrixBuffer;
    private int vboMatrixDataID;
    private FloatBuffer vboMatrixData;


    private FloatBuffer textureBuffer;
    private int vboTextureDataID;
    private FloatBuffer vboTextureData;



    private final int BLOCK_SIZE = 1000;
    public InstancedRenderData() {
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        matrixBuffer = BufferUtils.createFloatBuffer(16 * BLOCK_SIZE);

        vboMatrixDataID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboMatrixDataID);
        glBufferData(GL_ARRAY_BUFFER, 16 * 4 * BLOCK_SIZE, GL_STREAM_DRAW);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        addInstancedAttribute(vaoID, vboMatrixDataID, 2, 4, 16, 0);
        addInstancedAttribute(vaoID, vboMatrixDataID, 3, 4, 16, 4);
        addInstancedAttribute(vaoID, vboMatrixDataID, 4, 4, 16, 8);
        addInstancedAttribute(vaoID, vboMatrixDataID, 5, 4, 16, 12);
        vboMatrixData = BufferUtils.createFloatBuffer(BLOCK_SIZE * 16);



        textureBuffer = BufferUtils.createFloatBuffer((2 * 6) * BLOCK_SIZE);
        vboTextureDataID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTextureDataID);
        glBufferData(GL_ARRAY_BUFFER, (2 * 6) * 4 * BLOCK_SIZE, GL_STREAM_DRAW);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        addInstancedAttribute(vaoID, vboTextureDataID, 6, 2, 2, 0);
        vboTextureData = BufferUtils.createFloatBuffer(BLOCK_SIZE * 2);
    }


    public void uploadModelViewMatrices(List<Matrix4f> matrices) {
        if(matrices.size() > BLOCK_SIZE) {
            System.out.println("InstancedRenderData: Uploaded Matrix data is above proper BlockSize");
        }
        int index = 0;
        for(Matrix4f matrix: matrices) {
            matrix.get(index * 16, vboMatrixData);
            index++;
        }
        updateVBO(vboMatrixDataID, vboMatrixData, matrixBuffer);
    }

    public void uploadTextures(List<Vector2f> textures) {
        if(textures.size() > BLOCK_SIZE) {
            System.out.println("InstancedRenderData: Uploaded Texture data is above proper BlockSize");
        }
        int index = 0;
        for(Vector2f texture: textures) {
            texture.get(index * 2, vboTextureData);
            index++;
        }
        updateVBO(vboTextureDataID, vboTextureData, textureBuffer);
    }

    public void enableAttributes() {
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        GL20.glEnableVertexAttribArray(6);
    }

    public void disableAttributes() {
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
        glDisableVertexAttribArray(5);
        glDisableVertexAttribArray(6);
    }




    private void updateVBO(int vbo, FloatBuffer data, FloatBuffer buffer) {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
        //glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBindVertexArray(vao);
        glVertexAttribPointer(attribute,dataSize, GL_FLOAT, false, instancedDataLength * 4, offset * 4);
        glVertexAttribDivisor(attribute,1);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        //glBindVertexArray(0);
    }

    public int getBLOCK_SIZE() {
        return BLOCK_SIZE;
    }
}
