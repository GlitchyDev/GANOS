package com.GlitchyDev.Rendering.Assets.Mesh;

import com.GlitchyDev.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Rendering.Assets.WorldElements.Transformation;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11C.GL_FLOAT;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL31C.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33C.glVertexAttribDivisor;

public class InstancedMesh extends Mesh {
    protected final int instanceChunkSize;

    protected final int matrixVboId;
    protected FloatBuffer matrixBuffer;
    protected FloatBuffer matrixVboData;


    public InstancedMesh(Mesh mesh, int instanceChunkSize) {
        super(mesh);
        this.instanceChunkSize = instanceChunkSize;


        matrixBuffer = BufferUtils.createFloatBuffer(16 * instanceChunkSize);
        matrixVboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, matrixVboId);
        glBufferData(GL_ARRAY_BUFFER, 16 * 4 * instanceChunkSize, GL_STREAM_DRAW);
        vboIdList.add(matrixVboId);
        glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        addInstancedAttribute(getVaoId(), matrixVboId, 2, 4, 16, 0);
        addInstancedAttribute(getVaoId(), matrixVboId, 3, 4, 16, 4);
        addInstancedAttribute(getVaoId(), matrixVboId, 4, 4, 16, 8);
        addInstancedAttribute(getVaoId(), matrixVboId, 5, 4, 16, 12);

        matrixVboData = BufferUtils.createFloatBuffer(instanceChunkSize * 16);



    }

    public void updateVBO(int vbo, FloatBuffer data, FloatBuffer buffer)
    {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, buffer.capacity() * 4, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, buffer);
        //glBindBuffer(GL_ARRAY_BUFFER, 0);
    }



    @Override
    public void preRender()
    {
        // Activate firs texture bank
        // Bind the texture

        // Draw the mesh
        GL30.glBindVertexArray(getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        GL20.glEnableVertexAttribArray(4);
        GL20.glEnableVertexAttribArray(5);
        //GL20.glEnableVertexAttribArray(6);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, getTexture().getId());

    }

    @Override
    public void postRender()
    {
        // Restore state
        glBindTexture(GL_TEXTURE_2D, 0);
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
        glDisableVertexAttribArray(5);
        GL30.glBindVertexArray(0);
    }

    public void addInstancedAttribute(int vao, int vbo, int attribute, int dataSize, int instancedDataLength, int offset)
    {
        glBindBuffer(GL_ARRAY_BUFFER,vbo);
        glBindVertexArray(vao);
        glVertexAttribPointer(attribute,dataSize, GL_FLOAT, false, instancedDataLength * 4, offset * 4);
        glVertexAttribDivisor(attribute,1);
        glBindBuffer(GL_ARRAY_BUFFER,0);
        //glBindVertexArray(0);
    }


    public void renderMeshInstanceList(List<GameItem> gameItems, Transformation transformation, Matrix4f viewMatrix)
    {
        preRender();
        int length = gameItems.size();
        for (int i = 0; i < length; i += instanceChunkSize) {
            //System.out.println(i);
            int end = Math.min(length, i + instanceChunkSize);
            renderMeshInstanced(gameItems.subList(i, end),  transformation, viewMatrix, end-i);
        }
        postRender();

    }

    public void renderMeshInstanced(List<GameItem> gameItems, Transformation transformation, Matrix4f viewMatrix, int size)
    {
        matrixVboData = BufferUtils.createFloatBuffer(size * 16);
        int offset = 0;
        for(int i = 0; i < size; i++)
        {
            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItems.get(i), viewMatrix);
            modelViewMatrix.get(offset * 16, matrixVboData);
            offset++;
        }
        updateVBO(matrixVboId, matrixVboData, matrixBuffer);
        glDrawElementsInstanced(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, size);
    }



}
