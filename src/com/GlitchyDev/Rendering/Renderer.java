package com.GlitchyDev.Rendering;


import com.GlitchyDev.Game.GameWindow;
import com.GlitchyDev.Rendering.Assets.Mesh.PartialCubicInstanceMesh;
import com.GlitchyDev.Rendering.Assets.Shaders.ShaderProgram;
import com.GlitchyDev.Rendering.Assets.WorldElements.*;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.FrustumCullingFilter;
import com.GlitchyDev.World.Blocks.AbstractBlocks.DesignerBlock;
import com.GlitchyDev.World.Region.Region;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * A rendering assistant for rendering GameItems in OpenGL using Shaders
 */
public class Renderer {
    private final Transformation transformation = new Transformation();
    private String previousShader = "";
    private int renderWidth;
    private int renderHeight;

    // All the currently Loaded Shaders
    private HashMap<String,ShaderProgram> loadedShaders = new HashMap<>();

    // Initialize after AssetLoading
    public Renderer() {
        for(String shaderName: AssetLoader.getConfigListAsset("Shaders"))
        {
            try {
                loadedShaders.put(shaderName,new ShaderProgram(shaderName));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * A prep method that prepares the current rendering location for rendering
     * Use when switching between different Rendering Locations and at the beginning of each frame
     * @param window
     */
    public void prepWindowRender(GameWindow window) {
        clear();
    }

    /**
     * Clear the current Rendering Location
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void setRenderSpace( int x, int y, int width, int height) {
        glViewport(x, y, width, height);
        renderWidth = width;
        renderHeight = height;
    }







    public void render3DElements(Camera camera, List<GameItem> gameItems, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(renderWidth, renderHeight);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem
        for (GameItem gameItem : gameItems) {
            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItem, viewMatrix);
            shader.setUniform("modelViewMatrix", modelViewMatrix);
            gameItem.getMesh().render();
        }

        //shader.unbind();
    }

    public void render3DElement(Camera camera, GameItem gameItem, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(renderWidth, renderHeight);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItem, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);
        gameItem.getMesh().render();


        //shader.unbind();
    }


    public void renderBillboard3DElements(Camera camera, List<GameItem> gameItems, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(renderWidth, renderHeight);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem
        for (GameItem gameItem : gameItems) {
            Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);
            viewMatrix.transpose3x3(modelMatrix);
            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
            modelViewMatrix.scale(gameItem.getScale());//
            shader.setUniform("modelViewMatrix", modelViewMatrix);
            gameItem.getMesh().render();
        }

        //shader.unbind();
    }

    public void renderBillboard3DElement(Camera camera, GameItem gameItem, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(renderWidth, renderHeight);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem
        Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);
        viewMatrix.transpose3x3(modelMatrix);
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);
        gameItem.getMesh().render();


        //shader.unbind();
    }

    public void renderBillboard3DSprites(Camera camera, List<GameItem> gameItems, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(renderWidth, renderHeight);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem
        for (GameItem gameItem : gameItems) {
            Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);
            viewMatrix.transpose3x3(modelMatrix);
            modelMatrix.rotateY((float) Math.toRadians(180));
            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
            modelViewMatrix.scale(gameItem.getScale());//
            shader.setUniform("modelViewMatrix", modelViewMatrix);
            gameItem.getMesh().render();
        }

        //shader.unbind();
    }

    public void renderBillboard3DSprite(Camera camera, GameItem gameItem, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(renderWidth, renderHeight);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem
        Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);
        viewMatrix.transpose3x3(modelMatrix);
        modelMatrix.rotateY((float) Math.toRadians(180));
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);
        gameItem.getMesh().render();


        //shader.unbind();
    }



    public void renderDesignerBlocks(Camera camera, ArrayList<DesignerBlock> designerBlocks, PartialCubicInstanceMesh partialCubicInstanceMesh, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(renderWidth, renderHeight);
        shader.setUniform("projectionMatrix", projectionMatrix);


        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem

        shader.setUniform("textureGridSize", new Vector2f(partialCubicInstanceMesh.getInstancedGridTexture().getHorizontalGridNam(),partialCubicInstanceMesh.getInstancedGridTexture().getVerticalGridNum()));


        partialCubicInstanceMesh.renderPartialCubicBlocksInstanced(designerBlocks,transformation,transformation.getCameraViewMatrix(camera));
    }

    public void renderInstancingDefault(Camera camera, GameItem gameItem, int size, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(renderWidth, renderHeight);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(gameItem, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);
        gameItem.getMesh().renderInstanced(size);

    }

    public void enableTransparency() {
        glEnable(GL_BLEND);
    }

    public void disableTransparency() {
        glDisable(GL_BLEND);
    }




    /*--
    public void renderInstanced3DElements(GameWindow window, String shaderName, Camera camera, InstancedMesh instancedMesh, List<GameItem> gameItems) {
         ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // WalkieTalkie each gameItem
        instancedMesh.renderMeshInstanceList(gameItems,transformation,viewMatrix);

        //shader.unbind();
    }
    */

    /*
    public void renderInstancedPartialCubic(GameWindow window, String shaderName, Camera camera, PartialCubicInstanceMesh instancedMesh, List<PartialCubicBlock> blocks) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);

        shader.setUniform("textureGridSize",new Vector2f(instancedMesh.getInstancedGridTexture().getTextureGridWidth(),instancedMesh.getInstancedGridTexture().getTextureGridHeight()));
        // WalkieTalkie each gameItem
        instancedMesh.renderPartialCubicBlocksInstanced(blocks,transformation,viewMatrix);

        //shader.unbind();
    }
    */

    /*
    public void renderInstancedPartialCubicChunk(GameWindow window, String shaderName, Camera camera, PartialCubicInstanceMesh instancedMesh, Collection<Chunk> chunks, boolean useFrustumCullingFilter) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getCameraViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);

       // shader.setUniform("textureGridSize",new Vector2f(instancedMesh.getInstancedGridTexture().getTextureGridWidth(),instancedMesh.getInstancedGridTexture().getTextureGridHeight()));
        // WalkieTalkie each gameItem
        instancedMesh.renderPartialCubicBlocksInstancedChunkTextures(shader,chunks,transformation,viewMatrix, useFrustumCullingFilter);

        //shader.unbind();
    }
    */


    public void render2DSpriteItems(List<SpriteItem> spriteItems, String shaderName)
    {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, renderWidth, renderHeight, 0);

        shader.setUniform("texture_sampler", 0);


        for (SpriteItem spriteItem : spriteItems) {
            // Set ortohtaphic and model matrix for this HUD item
            Matrix4f orthModelMatrix = transformation.buildOrtoProjModelMatrix(spriteItem, ortho);
            shader.setUniform("projModelMatrix", orthModelMatrix);
            spriteItem.getMesh().render();
        }


        shader.unbind();
    }


    public void render2DSpriteItem(SpriteItem spriteItem, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, renderWidth, renderHeight, 0);

        shader.setUniform("texture_sampler", 0);


        // Set ortohtaphic and model matrix for this HUD item
        Matrix4f projModelMatrix = transformation.buildOrtoProjModelMatrix(spriteItem, ortho);
        shader.setUniform("projModelMatrix", projModelMatrix);
        spriteItem.getMesh().render();

        shader.unbind();
    }



    public void render2DTextItems(List<TextItem> textItems, String shaderName)
    {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, renderWidth, renderHeight, 0);

        shader.setUniform("texture_sampler", 0);


        for (GameItem gameItem : textItems) {
            // Set ortohtaphic and model matrix for this HUD item
            Matrix4f orthModelMatrix = transformation.buildOrtoProjModelMatrix(gameItem, ortho);
            shader.setUniform("projModelMatrix", orthModelMatrix);
            gameItem.getMesh().render();
        }


        shader.unbind();
    }


    public void render2DTextItem(TextItem textItem, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        Matrix4f ortho = transformation.getOrtho2DProjectionMatrix(0, renderWidth, renderHeight, 0);

        shader.setUniform("texture_sampler", 0);


        // Set ortohtaphic and model matrix for this HUD item
        Matrix4f projModelMatrix = transformation.buildOrtoProjModelMatrix(textItem, ortho);
        shader.setUniform("projModelMatrix", projModelMatrix);
        textItem.getMesh().render();

        shader.unbind();
    }



    public void cleanup() {
        for(String shader :loadedShaders.keySet()) {
            loadedShaders.get(shader).cleanup();
        }
    }

    public Transformation getTransformation()
    {
        return transformation;
    }

    public void updateFrustumCullingFilter(Camera camera, Collection<Region> regions) {
        FrustumCullingFilter.updateFrustum(transformation.getProjectionMatrix(renderWidth, renderHeight),transformation.getCameraViewMatrix(camera));
        FrustumCullingFilter.filter(regions);
    }

    public ShaderProgram getShader(String name) {
        return loadedShaders.get(name);
    }
}
