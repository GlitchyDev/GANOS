package com.GlitchyDev.Rendering.Assets.WorldElements;

import com.GlitchyDev.Rendering.Assets.Fonts.HudTexture;
import com.GlitchyDev.Rendering.Assets.Mesh;
import com.GlitchyDev.Rendering.Assets.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * A Game Item Dedicated to displaying Text, in either the 2D or 3D fashion
 */
public class TextItem extends GameItem {

    private static final float ZPOS = 0.0f;

    private static final int VERTICES_PER_QUAD = 4;

    private final HudTexture hudTexture;
    
    private String text;


    public TextItem(String text, HudTexture hudTexture) {
        super(null);
        this.text = text;
        this.hudTexture = hudTexture;
        setMesh(buildMesh());
    }

    private Mesh buildMesh() {


        List<Float> positions = new ArrayList();
        List<Float> textCoords = new ArrayList();
        List<Integer> indices = new ArrayList();
        char[] characters = text.toCharArray();
        int numChars = characters.length;

        float startx = 0;
        for(int i=0; i<numChars; i++) {
            HudTexture.CharInfo charInfo = hudTexture.getCharInfo(characters[i]);
            if(charInfo == null)
            {
                System.out.println("TEXTITEM: ERROR CHAR NOT FOUND " + characters[i]);
            }
            // Build a character tile composed by two triangles

            // Left Top vertex
            positions.add(startx + charInfo.getLeftBufferWidth()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add( (float)charInfo.getStartX() / (float)hudTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD);

            // Left Bottom vertex
            positions.add(startx + charInfo.getLeftBufferWidth()); // x
            positions.add((float)hudTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)charInfo.getStartX() / (float)hudTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 1);

            // Right Bottom vertex
            positions.add(startx + charInfo.getWidth()  + charInfo.getLeftBufferWidth()); // x
            positions.add((float)hudTexture.getHeight()); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)hudTexture.getWidth());
            textCoords.add(1.0f);
            indices.add(i*VERTICES_PER_QUAD + 2);

            // Right Top vertex
            positions.add(startx + charInfo.getWidth()  + charInfo.getLeftBufferWidth()); // x
            positions.add(0.0f); //y
            positions.add(ZPOS); //z
            textCoords.add((float)(charInfo.getStartX() + charInfo.getWidth() )/ (float)hudTexture.getWidth());
            textCoords.add(0.0f);
            indices.add(i*VERTICES_PER_QUAD + 3);

            // Add indices por left top and bottom right vertices
            indices.add(i*VERTICES_PER_QUAD);
            indices.add(i*VERTICES_PER_QUAD + 2);

            startx += charInfo.getWidth() + charInfo.getRightBufferWidth() + charInfo.getLeftBufferWidth();
        }

        float[] posArr = Utils.listToArray(positions);
        float[] textCoordsArr = Utils.listToArray(textCoords);
        int[] indicesArr = indices.stream().mapToInt(i->i).toArray();
        Mesh mesh = new Mesh(posArr, textCoordsArr, indicesArr);
        mesh.setTexture(hudTexture.getTexture());
        return mesh;
    }

    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
        getMesh().deleteBuffers();
        setMesh(buildMesh());
    }


}