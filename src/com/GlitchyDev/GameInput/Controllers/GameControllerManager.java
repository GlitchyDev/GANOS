package com.GlitchyDev.GameInput.Controllers;

import com.GlitchyDev.GameInput.Controllers.GameController;
import com.GlitchyDev.GameInput.Controllers.PS4Controller;
import com.GlitchyDev.GameInput.Controllers.XBox360Controller;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.glfwGetJoystickName;
import static org.lwjgl.glfw.GLFW.glfwJoystickPresent;

public class GameControllerManager {
    private static ArrayList<GameController> activeControllers = new ArrayList<>();
    private static final int MAX_CONTROLLERS = 4;
    private static int loadedControllerCount = 0;

    public static void loadControllers() {
        activeControllers.clear();
        for(int i = 0; i < MAX_CONTROLLERS; i++) {
            if(glfwJoystickPresent(i)) {
                GameController controller = null;
                switch(glfwGetJoystickName(i)) {
                    case "Wireless Controller":
                        controller = new PS4Controller(i);
                        break;
                    case "Xbox Controller":
                        controller = new XBox360Controller(i);
                        break;
                    default:
                        controller = new XBox360Controller(i);
                }
                activeControllers.add(controller);
            }
        }
        loadedControllerCount = activeControllers.size();
    }


    public static ArrayList<GameController> getControllers() {
        return activeControllers;
    }

    public static int getLoadedControllerCount() {
        return loadedControllerCount;
    }
}
