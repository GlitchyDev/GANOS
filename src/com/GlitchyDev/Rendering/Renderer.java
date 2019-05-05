package com.GlitchyDev.Rendering;


import com.GlitchyDev.Rendering.Assets.Shaders.ShaderProgram;
import com.GlitchyDev.Rendering.Assets.WorldElements.*;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.FrustumCullingFilter;
import com.GlitchyDev.Utility.GameWindow;
import com.GlitchyDev.World.Region.Region;
import org.joml.Matrix4f;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * A rendering assistant for rendering GameItems in OpenGL using Shaders
 */
public class Renderer {
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
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
        if ( window.hasBeenResized() ) {
            setRenderSpace(0,0,window.getWidth(), window.getHeight());
            window.setIfResized(false);
        }
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
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, renderWidth, renderWidth, Z_NEAR, Z_FAR);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // Render each gameItem
        for (GameItem gameItem : gameItems) {
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
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
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, renderWidth, renderWidth, Z_NEAR, Z_FAR);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // Render each gameItem
        Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
        shader.setUniform("modelViewMatrix", modelViewMatrix);
        gameItem.getMesh().render();


        //shader.unbind();
    }


    /*
    public void renderInstanced3DElements(GameWindow window, String shaderName, Camera camera, InstancedMesh instancedMesh, List<GameItem> gameItems) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shader.setUniform("projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);
        // Render each gameItem
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
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);

        shader.setUniform("textureGridSize",new Vector2f(instancedMesh.getInstancedGridTexture().getTextureGridWidth(),instancedMesh.getInstancedGridTexture().getTextureGridHeight()));
        // Render each gameItem
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
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        shader.setUniform("texture_sampler", 0);

       // shader.setUniform("textureGridSize",new Vector2f(instancedMesh.getInstancedGridTexture().getTextureGridWidth(),instancedMesh.getInstancedGridTexture().getTextureGridHeight()));
        // Render each gameItem
        instancedMesh.renderPartialCubicBlocksInstancedChunkTextures(shader,chunks,transformation,viewMatrix, useFrustumCullingFilter);

        //shader.unbind();
    }
    */


    public void renderHUD(List<TextItem> hudItems, String shaderName)
    {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, renderWidth, renderHeight, 0);

        shader.setUniform("texture_sampler", 0);


        for (GameItem gameItem : hudItems) {
            // Set ortohtaphic and model matrix for this HUD item
            Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(gameItem, ortho);
            shader.setUniform("projModelMatrix", projModelMatrix);
            gameItem.getMesh().render();
        }


        shader.unbind();
    }



    public void render2DSprites(List<SpriteItem> spriteItems, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, renderWidth, renderHeight, 0);

        shader.setUniform("texture_sampler", 0);
        for (GameItem gameItem : spriteItems) {
            // Set ortohtaphic and model matrix for this HUD item
            Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(gameItem, ortho);
            shader.setUniform("projModelMatrix", projModelMatrix);
            gameItem.getMesh().render();
        }

        shader.unbind();
    }

    public void render2DSprite(SpriteItem spriteItem, String shaderName) {
        ShaderProgram shader = loadedShaders.get(shaderName);
        if(!previousShader.equals(shaderName)) {
            shader.bind();
        }

        Matrix4f ortho = transformation.getOrthoProjectionMatrix(0, renderWidth, renderHeight, 0);

        shader.setUniform("texture_sampler", 0);


        // Set ortohtaphic and model matrix for this HUD item
        Matrix4f projModelMatrix = transformation.getOrtoProjModelMatrix(spriteItem, ortho);
        shader.setUniform("projModelMatrix", projModelMatrix);
        spriteItem.getMesh().render();

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

    public void updateFrustumCullingFilter(GameWindow gameWindow, Camera camera, Collection<Region> regions) {
        FrustumCullingFilter.updateFrustum(transformation.getProjectionMatrix(FOV, gameWindow.getWidth(), gameWindow.getHeight(), Z_NEAR, Z_FAR),transformation.getViewMatrix(camera));
        FrustumCullingFilter.filter(regions);
    }
}
