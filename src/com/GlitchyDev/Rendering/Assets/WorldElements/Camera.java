package com.GlitchyDev.Rendering.Assets.WorldElements;

import org.joml.Vector3f;

public class Camera {

    private final Vector3f position;
    private final Vector3f rotation;
    
    public Camera() {
        position = new Vector3f(0, 0, 0);
        rotation = new Vector3f(0, 0, 0);

    }
    
    public Camera(Vector3f position, Vector3f rotation) {
        this.position = position;
        this.rotation = rotation;
    }

    public Vector3f getPosition() {
        return position;
    }


    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }
    
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        position.add(offsetX,offsetY,offsetZ);
    }

    public Vector3f getRotation() {
        return rotation;
    }
    
    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    public void moveForward(float movementAmount)
    {
        movementAmount = Math.abs(movementAmount);
        movePosition(movementAmount * (float)Math.sin(Math.toRadians(rotation.y)),0,-movementAmount * (float)Math.cos(Math.toRadians(rotation.y)));
    }
    public void moveBackwards(float movementAmount)
    {
        movementAmount = Math.abs(movementAmount);
        movePosition(-movementAmount * (float)Math.sin(Math.toRadians(rotation.y)),0,movementAmount * (float)Math.cos(Math.toRadians(rotation.y)));
    }
    public void moveLeft(float movementAmount)
    {
        movementAmount = Math.abs(movementAmount);
        movePosition(-movementAmount * (float)Math.sin(Math.toRadians(rotation.y + 90)),0,movementAmount * (float)Math.cos(Math.toRadians(rotation.y + 90)));
    }
    public void moveRight(float movementAmount)
    {
        movementAmount = Math.abs(movementAmount);
        movePosition(-movementAmount * (float)Math.sin(Math.toRadians(rotation.y - 90)),0,movementAmount * (float)Math.cos(Math.toRadians(rotation.y - 90)));
    }
    public void moveUp(float movementAmount)
    {
        movementAmount = Math.abs(movementAmount);
        movePosition(0,movementAmount,0);
    }
    public void moveDown(float movementAmount)
    {
        movementAmount = Math.abs(movementAmount);
        movePosition(0,-movementAmount,0);
    }

}