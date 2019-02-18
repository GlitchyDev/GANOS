package com.GlitchyDev.GameInput;

import org.lwjgl.glfw.GLFWDropCallback;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Game Input for Keyboard, mouse buttons, as well as Cursor positions, mouse scrolling, Dragged Files
 */
public class GameInput {
    public static int KEYBOARD_SIZE = 512;
    private int[] keyMapping = new int[KEYBOARD_SIZE];
    private ArrayList<Integer> activeKeys = new ArrayList<>();
    private int currentKeyModifier = 0;
    private double mouseX = 0;
    private double mouseY = 0;
    private int mouseButton1 = 0;
    private int mouseButton2 = 0;
    private int mouseButton3 = 0;
    private double mouseScroll = 0;
    private boolean mouseScrollEnabled = false;
    private boolean mouseInWindow = true;
    private ArrayList<String> draggedFiles = new ArrayList<>();


    public void bind(long windowHandle) {

        glfwSetKeyCallback(windowHandle, (windowID, key, scancode, action, mods) -> {
            if(key >= 0 && key <= keyMapping.length) {
                keyMapping[key] = action;
                if (!activeKeys.contains(key) && action == GLFW_PRESS) {
                    activeKeys.add(key);
                } else {
                    if (action == GLFW_RELEASE) {
                        activeKeys.remove(Integer.valueOf(key));
                    }
                }
                currentKeyModifier = mods;
            }
        });

        glfwSetCursorPosCallback(windowHandle, (windowID, xpos, ypos) -> {
            this.mouseX = xpos;
            this.mouseY = ypos;
        });

        glfwSetCursorEnterCallback(windowHandle, (windowID, entered) -> {
            mouseInWindow = entered;
        });

        glfwSetMouseButtonCallback(windowHandle, (windowID, button, action, mode) -> {
            switch(button)
            {
                case GLFW_MOUSE_BUTTON_1:
                    mouseButton1 = action;
                    break;
                case GLFW_MOUSE_BUTTON_2:
                    mouseButton2 = action;
                    break;
                case GLFW_MOUSE_BUTTON_3:
                    mouseButton3 = action;
                    break;
            }
        });

        glfwSetScrollCallback(windowHandle, (windowID, xoffset, yoffset) -> {
            mouseScroll = yoffset;
            mouseScrollEnabled = true;
        });

        glfwSetDropCallback(windowHandle, (windowID, count, paths) -> {
            for (int i = 0; i < count; i ++) {
                draggedFiles.add(GLFWDropCallback.getName(paths, i));
            }
        });

    }



    // Getters

    public int getKeyValue(int key)
    {
        return keyMapping[key];
    }

    public ArrayList<Integer> getActiveKeys()
    {
        return activeKeys;
    }

    public int getCurrentKeyModifier() {
        return currentKeyModifier;
    }

    public int getMouseButton1() {
        return mouseButton1;
    }

    public int getMouseButton2() {
        return mouseButton2;
    }

    public int getMouseButton3() {
        return mouseButton3;
    }

    public double getMouseScroll() {
        return mouseScroll;
    }

    public boolean isMouseScrollEnabled() {
        return mouseScrollEnabled;
    }

    public void setMouseScrollEnabled(boolean scrollEnabled) {
        mouseScrollEnabled = scrollEnabled;
    }

    public double getMouseX() {
        return mouseX;
    }

    public double getMouseY() {
        return mouseY;
    }

    public boolean isMouseInWindow() {
        return mouseInWindow;
    }

    public ArrayList<String> getDraggedFiles() {
        ArrayList<String> clone = draggedFiles;
        draggedFiles = new ArrayList<>();
        return clone;
    }
}
