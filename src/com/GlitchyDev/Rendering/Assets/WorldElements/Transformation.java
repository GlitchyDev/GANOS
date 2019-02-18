package com.GlitchyDev.Rendering.Assets.WorldElements;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class Transformation {

    private Matrix4f projectionMatrix = new Matrix4f();

    private Matrix4f modelViewMatrix = new Matrix4f();

    private Matrix4f viewMatrix = new Matrix4f();

    private Matrix4f orthoMatrix = new Matrix4f();



    public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
        float aspectRatio = width / height;
        projectionMatrix.identity();
        projectionMatrix.setPerspective(fov, aspectRatio, zNear, zFar);
        return projectionMatrix;
    }

    private Vector3f cameraPos;
    Vector3f rotation3f;
    final Vector3f orientation1 = new Vector3f(1, 0, 0);
    final Vector3f orientation2 = new Vector3f(0, 1, 0);

    public Matrix4f getViewMatrix(Camera camera) {
        cameraPos = camera.getPosition();
        rotation3f = camera.getRotation();

        return viewMatrix.identity()
                .rotate((float) Math.toRadians(rotation3f.x), orientation1).rotate((float) Math.toRadians(rotation3f.y), orientation2)
                .translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
    }

    public final Matrix4f getOrthoProjectionMatrix(float left, float right, float bottom, float top) {
        return orthoMatrix.identity().setOrtho2D(left, right, bottom, top);
    }

    public static Matrix4f updateGenericViewMatrix(Vector3f position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }
    public static Matrix4f updateGenericViewMatrix(Vector3i position, Vector3f rotation, Matrix4f matrix) {
        // First do the rotation so camera rotates over its position
        return matrix.rotationX((float)Math.toRadians(rotation.x))
                .rotateY((float)Math.toRadians(rotation.y))
                .translate(-position.x, -position.y, -position.z);
    }


    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
        rotation3f = gameItem.getRotation();
        return new Matrix4f(viewMatrix).mul( modelViewMatrix.identity().translate(gameItem.getPosition()).
                rotateX((float) Math.toRadians(-rotation3f.x)).
                rotateY((float) Math.toRadians(-rotation3f.y)).
                rotateZ((float) Math.toRadians(-rotation3f.z)).
                scale(gameItem.getScale()));
    }

    public Matrix4f getModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix, int normalizeMultiplier) {
        rotation3f = gameItem.getRotation().mul(normalizeMultiplier);
        return new Matrix4f(viewMatrix).mul( modelViewMatrix.identity().translate(gameItem.getPosition()).
                rotateX((float) Math.toRadians(-rotation3f.x)).
                rotateY((float) Math.toRadians(-rotation3f.y)).
                rotateZ((float) Math.toRadians(-rotation3f.z)).
                scale(gameItem.getScale()));
    }

    public Matrix4f getModelViewMatrix(Vector3f position, Vector3f rotation, float scale, Matrix4f viewMatrix) {
        return new Matrix4f(viewMatrix).mul(modelViewMatrix.identity().translate(position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(scale));
    }

    private Vector3f convertVector = new Vector3f();
    public Matrix4f getModelViewMatrix(Vector3i position, Vector3f rotation, float scale, Matrix4f viewMatrix) {
        convertVector = new Vector3f(position);
        return new Matrix4f(viewMatrix).mul(modelViewMatrix.identity().translate(convertVector).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(scale));
    }

    public Matrix4f getModelViewMatrix(Vector3i position, Vector3f rotation, float scale, Matrix4f viewMatrix, int normalizeMultiplier) {
        convertVector = new Vector3f(position).mul(normalizeMultiplier);
        return new Matrix4f(viewMatrix).mul(modelViewMatrix.identity().translate(new Vector3f(position).mul(normalizeMultiplier)).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(scale));
    }


    private Matrix4f orthoMatrixCurr;
    public Matrix4f getOrtoProjModelMatrix(GameItem gameItem, Matrix4f orthoMatrix) {
        rotation3f = gameItem.getRotation();
        orthoMatrixCurr = new Matrix4f(orthoMatrix);
        return orthoMatrixCurr.mul(modelMatrix.identity().translate(gameItem.getPosition()).
                rotateX((float) Math.toRadians(-rotation3f.x)).
                rotateY((float) Math.toRadians(-rotation3f.y)).
                rotateZ((float) Math.toRadians(-rotation3f.z)).
                scale(gameItem.getScale()));
    }
    public Matrix4f getOrtoProjModelMatrix(Vector3f position, Vector3f rotation, float scale, Matrix4f orthoMatrix) {
        orthoMatrixCurr = new Matrix4f(orthoMatrix);
        return orthoMatrixCurr.mul(modelMatrix.identity().translate(position).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(scale));
    }
    public Matrix4f getOrtoProjModelMatrix(Vector3i position, Vector3f rotation, float scale, Matrix4f orthoMatrix) {
        orthoMatrixCurr = new Matrix4f(orthoMatrix);
        return orthoMatrixCurr.mul(modelMatrix.identity().translate(position.x,position.y,position.z).
                rotateX((float) Math.toRadians(-rotation.x)).
                rotateY((float) Math.toRadians(-rotation.y)).
                rotateZ((float) Math.toRadians(-rotation.z)).
                scale(scale));
    }

    Matrix4f modelMatrix = new Matrix4f();
    Quaternionf QuatAroundX = new Quaternionf();
    Quaternionf QuatAroundY = new Quaternionf();
    Quaternionf QuatAroundZ = new Quaternionf();
    Quaternionf rotationQ = new Quaternionf();
    public Matrix4f buildModelMatrix(GameItem gameItem) {
        QuatAroundX.identity().add(1.0f,0.0f,0.0f, gameItem.getPosition().x );
        QuatAroundY.identity().add( 0.0f,1.0f,0.0f, gameItem.getPosition().y );
        QuatAroundZ.identity().add( 0.0f,0.0f,1.0f, gameItem.getPosition().z );
        rotationQ.identity().mul(QuatAroundX).mul(QuatAroundY).mul(QuatAroundZ);
        return modelMatrix.translationRotateScale(
                gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z,
                rotationQ.x, rotationQ.y, rotationQ.z, rotationQ.w,
                gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
    }


    public Matrix4f buildModelMatrix(Vector3i position, Quaternionf rotation, float scale) {
        return modelMatrix.translationRotateScale(
                position.x, position.y, position.z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                scale, scale, scale);
    }

    public Matrix4f buildModelViewMatrix(GameItem gameItem, Matrix4f viewMatrix) {
        return buildModelViewMatrix(buildModelMatrix(gameItem), viewMatrix);
    }

    public Matrix4f buildModelViewMatrix(Matrix4f modelMatrix, Matrix4f viewMatrix) {
        return viewMatrix.mulAffine(modelMatrix, modelViewMatrix);
    }
}

/*
public Matrix4f buildModelMatrix(GameItem gameItem) {
        Quaternionf QuatAroundX = new Quaternionf( 1.0f,0.0f,0.0f, gameItem.getPosition().x );
        Quaternionf QuatAroundY = new Quaternionf( 1.0f,0.0f,0.0f, gameItem.getPosition().y );
        Quaternionf QuatAroundZ = new Quaternionf( 1.0f,0.0f,0.0f, gameItem.getPosition().z );
        Quaternionf rotation = new Quaternionf();
        rotation.mul(QuatAroundX).mul(QuatAroundY).mul(QuatAroundZ);
        return modelMatrix.translationRotateScale(
                gameItem.getPosition().x, gameItem.getPosition().y, gameItem.getPosition().z,
                rotation.x, rotation.y, rotation.z, rotation.w,
                gameItem.getScale(), gameItem.getScale(), gameItem.getScale());
    }
 */

