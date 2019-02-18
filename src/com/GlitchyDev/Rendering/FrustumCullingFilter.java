package com.GlitchyDev.Rendering;

import com.GlitchyDev.Old.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Old.World.Blocks.Abstract.BlockBase;
import com.GlitchyDev.Old.World.Chunk;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.Collection;
import java.util.List;

public class FrustumCullingFilter {

    public static final Matrix4f prjViewMatrix = new Matrix4f();

    public static FrustumIntersection frustumInt= new FrustumIntersection();


    public static void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        // Calculate projection view matrix
        prjViewMatrix.set(projMatrix);
        prjViewMatrix.mul(viewMatrix);
        // Update frustum intersection class
        frustumInt.set(prjViewMatrix);
    }

    public static void filter(List<GameItem> gameItems, float meshBoundingRadius) {
        float boundingRadius;
        Vector3f pos;
        for (GameItem gameItem : gameItems) {
            if (!gameItem.isDisableFrustumCulling()) {
                boundingRadius = gameItem.getScale() * meshBoundingRadius;
                pos = gameItem.getPosition();
                gameItem.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
            }
        }
    }

    public static void filter(Collection<Chunk> chunks) {
        float boundingRadius;
        Vector3i pos;
        for(Chunk chunk: chunks) {
            for(BlockBase[][] blockSelection: chunk.getBlocks()) {
                for(BlockBase[] blockLine: blockSelection) {
                    for(BlockBase block: blockLine) {
                        if (block != null && !block.isDisableFrustumCulling()) {
                            boundingRadius = block.getScale() * 1.80f;
                            pos = block.getNormalizedPosition();
                            block.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
                        }
                    }
                }
            }
        }
    }

    public static boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
        return frustumInt.testSphere(x0, y0, z0, boundingRadius);
    }
}