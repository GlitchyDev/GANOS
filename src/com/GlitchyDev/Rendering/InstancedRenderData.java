package com.GlitchyDev.Rendering;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
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



        textureBuffer = BufferUtils.createFloatBuffer((2 * 6) * BLOCK_SIZE);
        vboTextureDataID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboTextureDataID);
        glBufferData(GL_ARRAY_BUFFER, (2 * 6) * 4 * BLOCK_SIZE, GL_STREAM_DRAW);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        addInstancedAttribute(vaoID, vboTextureDataID, 6, 2, 2, 0);
        vboTextureData = BufferUtils.createFloatBuffer(BLOCK_SIZE * 2);
    }




    public void updateVBO(int vbo, FloatBuffer data, FloatBuffer buffer) {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
        //glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    public void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset) {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBindVertexArray(vao);
        glVertexAttribPointer(attribute,dataSize, GL_FLOAT, false, instancedDataLength * 4, offset * 4);
        glVertexAttribDivisor(attribute,1);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        //glBindVertexArray(0);
    }
}
