package com.GlitchyDev.Rendering.Assets.WorldElements;

import com.GlitchyDev.Rendering.Assets.Mesh.Mesh;
import com.GlitchyDev.Rendering.Assets.Texture.Texture;
import com.GlitchyDev.Utility.Utils;

import java.util.ArrayList;

/**
 * A GameItem that loads a Texture for use as a 2D image
 */
public class SpriteItem extends GameItem {


    public SpriteItem(Texture spriteTexture, boolean loadedTexture) {
        super(null);
        setMesh(buildMesh(spriteTexture,loadedTexture));
    }

    public SpriteItem(Texture spriteTexture, int width, int height, boolean loadedTexture) {
        super(null);
        setMesh(buildMesh(spriteTexture, width, height, loadedTexture));
    }


    public SpriteItem(Texture spriteTexture, int width, int height, boolean loadedTexture, boolean centered) {
        super(null);
        if(centered) {
            setMesh(buildCenteredMesh(spriteTexture, width, height, loadedTexture));
        } else {
            setMesh(buildMesh(spriteTexture, width, height, loadedTexture));
        }
    }



    public void setSprite(Texture spriteTexture, boolean loadedTexture) {
        getMesh().cleanUp();
        setMesh(buildMesh(spriteTexture,loadedTexture));
    }

    public void setSprite(Texture spriteTexture, int width, int height, boolean loadedTexture) {
        getMesh().cleanUp();
        setMesh(buildMesh(spriteTexture, width, height, loadedTexture));
    }

    public void setCenteredSprite(Texture spriteTexture, int width, int height, boolean loadedTexture) {
        getMesh().cleanUp();
        setMesh(buildCenteredMesh(spriteTexture, width, height, loadedTexture));
    }
    
    public void rebuildMesh(int width, int height, boolean loadedTexture) {
        Mesh m = getMesh();
        setMesh(buildMesh(m.getTexture(), width, height, loadedTexture));
        m.cleanUp();;
    }

    private Mesh buildMesh(Texture spriteTexture, boolean loadedTexture) {
        ArrayList<Float> positions = new ArrayList();
        ArrayList<Float> textCoords = new ArrayList();
        ArrayList<Integer> indices = new ArrayList();




        // Left Top vertex
        positions.add(0.0f); // x
        positions.add(0.0f); //y
        positions.add(0.0f); //z
        textCoords.add(0.0f);
        textCoords.add(1.0f);
        indices.add(0);

        // Left Bottom vertex
        positions.add(0.0f); // x
        positions.add((float) spriteTexture.getHeight()); //y
        positions.add(0.0f); //z
        textCoords.add(0.0f);
        textCoords.add(0.0f);
        indices.add(1);

        // Right Bottom vertex
        positions.add((float) spriteTexture.getWidth()); // x
        positions.add((float) spriteTexture.getHeight()); //y
        positions.add(0.0f); //z
        textCoords.add(1.0f);
        textCoords.add(0.0f);
        indices.add(2);

        // Right Top vertex
        positions.add((float) spriteTexture.getWidth()); // x
        positions.add(0.0f); //y
        positions.add(0.0f); //z
        textCoords.add(1.0f);
        textCoords.add(1.0f);
        indices.add(3);

        // Add indices por left top and bottom right vertices
        indices.add(0);
        indices.add(2);


        float[] posArr = Utils.listToArray(positions);
        if(loadedTexture)
        {
            for(int i = 0; i < textCoords.size()/4; i++) {
                float bufferX = textCoords.get(0 + i * 4);
                float bufferY = textCoords.get(1 + i * 4);
                textCoords.set(0 + i * 4,textCoords.get(2 + i * 4));
                textCoords.set(1 + i * 4,textCoords.get(3 + i * 4));
                textCoords.set(2 + i * 4,bufferX);
                textCoords.set(3 + i * 4,bufferY);
            }
        }
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, indicesArr);
        try {
            mesh.setTexture(spriteTexture);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mesh;
    }


    private Mesh buildCenteredMesh(Texture spriteTexture, int width, int height, boolean loadedTexture) {
        ArrayList<Float> positions = new ArrayList();
        ArrayList<Float> textCoords = new ArrayList();
        ArrayList<Integer> indices = new ArrayList();




        // Left Top vertex
        positions.add((float) (-width/2)); // x
        positions.add((float) (-height/2)); //y
        positions.add(0.0f); //z
        textCoords.add(0.0f);
        textCoords.add(1.0f);
        indices.add(0);

        // Left Bottom vertex
        positions.add((float) (-height/2)); // x
        positions.add((float) height/2); //y
        positions.add(0.0f); //z
        textCoords.add(0.0f);
        textCoords.add(0.0f);
        indices.add(1);

        // Right Bottom vertex
        positions.add((float) (width/2)); // x
        positions.add((float) (height/2)); //y
        positions.add(0.0f); //z
        textCoords.add(1.0f);
        textCoords.add(0.0f);
        indices.add(2);

        // Right Top vertex
        positions.add((float) (width/2)); // x
        positions.add((float) (-height/2)); //y
        positions.add(0.0f); //z
        textCoords.add(1.0f);
        textCoords.add(1.0f);
        indices.add(3);

        // Add indices por left top and bottom right vertices
        indices.add(0);
        indices.add(2);


        float[] posArr = Utils.listToArray(positions);
        if(loadedTexture)
        {
            for(int i = 0; i < textCoords.size()/4; i++) {
                float bufferX = textCoords.get(0 + i * 4);
                float bufferY = textCoords.get(1 + i * 4);
                textCoords.set(0 + i * 4,textCoords.get(2 + i * 4));
                textCoords.set(1 + i * 4,textCoords.get(3 + i * 4));
                textCoords.set(2 + i * 4,bufferX);
                textCoords.set(3 + i * 4,bufferY);
            }
        }
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, indicesArr);
        try {
            mesh.setTexture(spriteTexture);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mesh;
    }





    private Mesh buildMesh(Texture spriteTexture, int width, int height, boolean loadedTexture) {
        ArrayList<Float> positions = new ArrayList();
        ArrayList<Float> textCoords = new ArrayList();
        ArrayList<Integer> indices = new ArrayList();




        // Left Top vertex
        positions.add(0.0f); // x
        positions.add(0.0f); //y
        positions.add(0.0f); //z
        textCoords.add(0.0f);
        textCoords.add(1.0f);
        indices.add(0);

        // Left Bottom vertex
        positions.add(0.0f); // x
        positions.add((float) height); //y
        positions.add(0.0f); //z
        textCoords.add(0.0f);
        textCoords.add(0.0f);
        indices.add(1);

        // Right Bottom vertex
        positions.add((float) width); // x
        positions.add((float) height); //y
        positions.add(0.0f); //z
        textCoords.add(1.0f);
        textCoords.add(0.0f);
        indices.add(2);

        // Right Top vertex
        positions.add((float) width); // x
        positions.add(0.0f); //y
        positions.add(0.0f); //z
        textCoords.add(1.0f);
        textCoords.add(1.0f);
        indices.add(3);

        // Add indices por left top and bottom right vertices
        indices.add(0);
        indices.add(2);


        float[] posArr = Utils.listToArray(positions);
        if(loadedTexture)
        {
            for(int i = 0; i < textCoords.size()/4; i++) {
                float bufferX = textCoords.get(0 + i * 4);
                float bufferY = textCoords.get(1 + i * 4);
                textCoords.set(0 + i * 4,textCoords.get(2 + i * 4));
                textCoords.set(1 + i * 4,textCoords.get(3 + i * 4));
                textCoords.set(2 + i * 4,bufferX);
                textCoords.set(3 + i * 4,bufferY);
            }
        }
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, indicesArr);
        try {
            mesh.setTexture(spriteTexture);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mesh;
    }

    public void cleanup() {
        getMesh().cleanUp();
    }



}
