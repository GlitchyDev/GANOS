package com.GlitchyDev.Rendering.Assets.Mesh;


import com.GlitchyDev.Rendering.Assets.Texture.InstancedGridTexture;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15C.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL31C.glDrawElementsInstanced;

public class DesignerInstanceMesh extends InstancedMesh {
    protected InstancedGridTexture instancedGridTexture;
    protected final int textureVboId;
    protected FloatBuffer textureBuffer;
    protected FloatBuffer textureVboData;

    public DesignerInstanceMesh(Mesh mesh, int instanceChunkSize, InstancedGridTexture instancedGridTexture) {
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



    public void renderPartialCubicBlocksInstanced(List<Matrix4f> blocks, List<Vector2f> textureCords, int size) {

        for (int i = 0; i < size; i += instanceChunkSize) {
            int end = Math.min(size, i + instanceChunkSize);
            renderPartialCubicBlocksInstanced(blocks.subList(i, end), textureCords.subList(i, end), end-i);
        }


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



}
