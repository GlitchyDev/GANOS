package com.GlitchyDev.GameInput.Controllers;


import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.GameWindow;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

/**
 * ADD KEY HOLD FRAME COUNT AND time since held
 */
public abstract class GameController {
    // For Input
    private final int controllerID;
    protected String name;
    protected ByteBuffer[] buttons =  new ByteBuffer[2];
    protected FloatBuffer[] axes = new FloatBuffer[2];;
    // For Visualization
    private final HashMap<String, SpriteItem> buttonSprites = new HashMap<>();
    // For Toggle Tracking
    protected boolean currentlyActive = false;
    protected boolean previousCurrentlyActive = false;
    private boolean previousLeftTriggerToggleState = false;
    private boolean currentLeftTriggerToggleState = false;
    private boolean previousRightTriggerToggleState = false;
    private boolean currentRightTriggerToggleState = false;



    private final int SCALING = 3;
    private final int CONTROLLER_WIDTH = 55;
    private final int OFFSET_X = 0;

    public GameController(int controllerID) {
        this.controllerID = controllerID;

        int controllerOffset = OFFSET_X + CONTROLLER_WIDTH * controllerID * SCALING;
        buttonSprites.put("ControllerBase",new SpriteItem(AssetLoader.getTextureAsset("ControllerBase"),true));
        buttonSprites.get("ControllerBase").setPosition(controllerOffset,7*SCALING,0.1f);
        buttonSprites.get("ControllerBase").setScale(SCALING);
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("LeftBumper"+i,new SpriteItem(AssetLoader.getTextureAsset("LeftBumper"+i),true));
            buttonSprites.get("LeftBumper"+i).setPosition(controllerOffset + 4 * SCALING, 5 * SCALING,0.2f);
            buttonSprites.get("LeftBumper"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("RightBumper"+i,new SpriteItem(AssetLoader.getTextureAsset("RightBumper"+i),true));
            buttonSprites.get("RightBumper"+i).setPosition(controllerOffset + 39 * SCALING, 5 * SCALING,0.2f);
            buttonSprites.get("RightBumper"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("LeftJoyStickButton"+i,new SpriteItem(AssetLoader.getTextureAsset("Button"+i),true));
            buttonSprites.get("LeftJoyStickButton"+i).setPosition(controllerOffset + 9 * SCALING, 13 * SCALING,0.3f);
            buttonSprites.get("LeftJoyStickButton"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("RightJoyStickButton"+i,new SpriteItem(AssetLoader.getTextureAsset("Button"+i),true));
            buttonSprites.get("RightJoyStickButton"+i).setPosition(controllerOffset + 32 * SCALING, 22 * SCALING,0.3f);
            buttonSprites.get("RightJoyStickButton"+i).setScale(SCALING);
        }
        for(int i = 0; i < 6; i++) {
            int offset = (i<3) ? i : 3;
            buttonSprites.put("LeftTrigger"+i,new SpriteItem(AssetLoader.getTextureAsset("Trigger"+i),true));
            buttonSprites.get("LeftTrigger"+i).setPosition(controllerOffset + 9 * SCALING, offset * SCALING,0.2f);
            buttonSprites.get("LeftTrigger"+i).setScale(SCALING);
        }
        for(int i = 0; i < 6; i++) {
            int offset = (i<3) ? i : 3;
            buttonSprites.put("RightTrigger"+i,new SpriteItem(AssetLoader.getTextureAsset("Trigger"+i),true));
            buttonSprites.get("RightTrigger"+i).setPosition(controllerOffset + 43 * SCALING, offset * SCALING,0.2f);
            buttonSprites.get("RightTrigger"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("LeftHome"+i,new SpriteItem(AssetLoader.getTextureAsset("SideHome"+i),true));
            buttonSprites.get("LeftHome"+i).setPosition(controllerOffset + 20 * SCALING, 12 * SCALING,0.2f);
            buttonSprites.get("LeftHome"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("Home"+i,new SpriteItem(AssetLoader.getTextureAsset("Home"+i),true));
            buttonSprites.get("Home"+i).setPosition(controllerOffset + 26 * SCALING, 11 * SCALING,0.2f);
            buttonSprites.get("Home"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("RightHome"+i,new SpriteItem(AssetLoader.getTextureAsset("SideHome"+i),true));
            buttonSprites.get("RightHome"+i).setPosition(controllerOffset + 33 * SCALING, 12 * SCALING,0.2f);
            buttonSprites.get("RightHome"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("NorthButton"+i,new SpriteItem(AssetLoader.getTextureAsset("Button"+i),true));
            buttonSprites.get("NorthButton"+i).setPosition(controllerOffset + 43 * SCALING, 10 * SCALING,0.2f);
            buttonSprites.get("NorthButton"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("EastButton"+i,new SpriteItem(AssetLoader.getTextureAsset("Button"+i),true));
            buttonSprites.get("EastButton"+i).setPosition(controllerOffset + 46 * SCALING, 13 * SCALING,0.2f);
            buttonSprites.get("EastButton"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("SouthButton"+i,new SpriteItem(AssetLoader.getTextureAsset("Button"+i),true));
            buttonSprites.get("SouthButton"+i).setPosition(controllerOffset + 43 * SCALING, 16 * SCALING,0.2f);
            buttonSprites.get("SouthButton"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("WestButton"+i,new SpriteItem(AssetLoader.getTextureAsset("Button"+i),true));
            buttonSprites.get("WestButton"+i).setPosition(controllerOffset + 40 * SCALING, 13 * SCALING,0.2f);
            buttonSprites.get("WestButton"+i).setScale(SCALING);
        }

        for(int i = 0; i < 3; i++) {
            buttonSprites.put("NorthVertical"+i,new SpriteItem(AssetLoader.getTextureAsset("Vertical"+i),true));
            buttonSprites.get("NorthVertical"+i).setPosition(controllerOffset + 21 * SCALING, 20 * SCALING,0.2f);
            buttonSprites.get("NorthVertical"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("SouthVertical"+i,new SpriteItem(AssetLoader.getTextureAsset("Vertical"+i),true));
            buttonSprites.get("SouthVertical"+i).setPosition(controllerOffset + 21 * SCALING, 24 * SCALING,0.2f);
            buttonSprites.get("SouthVertical"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("EastHorizontal"+i,new SpriteItem(AssetLoader.getTextureAsset("Horizontal"+i),true));
            buttonSprites.get("EastHorizontal"+i).setPosition(controllerOffset + 22 * SCALING, 23 * SCALING,0.2f);
            buttonSprites.get("EastHorizontal"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("WestHorizontal"+i,new SpriteItem(AssetLoader.getTextureAsset("Horizontal"+i),true));
            buttonSprites.get("WestHorizontal"+i).setPosition(controllerOffset + 18 * SCALING, 23 * SCALING,0.2f);
            buttonSprites.get("WestHorizontal"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("NorthEast"+i,new SpriteItem(AssetLoader.getTextureAsset("NorthEast"+i),true));
            buttonSprites.get("NorthEast"+i).setPosition(controllerOffset + 23 * SCALING, 20 * SCALING,0.2f);
            buttonSprites.get("NorthEast"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("SouthEast"+i,new SpriteItem(AssetLoader.getTextureAsset("SouthEast"+i),true));
            buttonSprites.get("SouthEast"+i).setPosition(controllerOffset + 23 * SCALING, 25 * SCALING,0.2f);
            buttonSprites.get("SouthEast"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("NorthWest"+i,new SpriteItem(AssetLoader.getTextureAsset("NorthWest"+i),true));
            buttonSprites.get("NorthWest"+i).setPosition(controllerOffset + 18 * SCALING, 20 * SCALING,0.2f);
            buttonSprites.get("NorthWest"+i).setScale(SCALING);
        }
        for(int i = 0; i < 3; i++) {
            buttonSprites.put("SouthWest"+i,new SpriteItem(AssetLoader.getTextureAsset("SouthWest"+i),true));
            buttonSprites.get("SouthWest"+i).setPosition(controllerOffset + 18 * SCALING, 25 * SCALING,0.2f);
            buttonSprites.get("SouthWest"+i).setScale(SCALING);
        }

        buttons[0] = BufferUtils.createByteBuffer(18);
        buttons[1] = BufferUtils.createByteBuffer(18);
        axes[0] = BufferUtils.createFloatBuffer(6);
        axes[1] = BufferUtils.createFloatBuffer(6);

        name = glfwGetJoystickName(controllerID);


        // 14
        // 4
    }


    public void tick() {
        previousCurrentlyActive = currentlyActive;
        if(previousCurrentlyActive) {
            buttons[1] = clone(buttons[0]);
            axes[1] = clone(axes[0]);
        }
        currentlyActive = glfwJoystickPresent(controllerID);
        if(currentlyActive) {
            buttons[0] = glfwGetJoystickButtons(controllerID);
            axes[0] = glfwGetJoystickAxes(controllerID);
            name = glfwGetJoystickName(controllerID);
        }

        previousLeftTriggerToggleState = currentLeftTriggerToggleState;
        currentLeftTriggerToggleState = getLeftTrigger() >= 0.95f;

        previousRightTriggerToggleState = currentRightTriggerToggleState;
        currentRightTriggerToggleState = getRightTrigger() >= 0.95f;


    }

    public void render(Renderer renderer, GameWindow gameWindow, String shader)
    {
        ArrayList<SpriteItem> items = new ArrayList<>();

        items.add(buttonSprites.get("ControllerBase"));
        // Bumper
        int leftBumperId = getLeftBumperButton() ? (getToggleLeftBumperButton() ? 1 : 2) : 0;
        items.add(buttonSprites.get("LeftBumper" + leftBumperId));
        int rightBumperId = getRightBumperButton() ? (getToggleRightBumperButton() ? 1 : 2) : 0;
        items.add(buttonSprites.get("RightBumper" + rightBumperId));
        //JoyStick
        int offsetLeftX = (int) (getLeftJoyStickX()/0.35);
        int offsetLeftY = (int) (getLeftJoyStickY()/0.35);
        int leftJoyStickId = getLeftJoyStickButton() ? (getToggleLeftJoyStickButton() ? 1 : 2) : 0;
        buttonSprites.get("LeftJoyStickButton" + leftJoyStickId).setPosition(OFFSET_X + (9 + offsetLeftX )* SCALING, (13+ offsetLeftY ) * SCALING,0.3f);
        items.add(buttonSprites.get("LeftJoyStickButton" + leftJoyStickId));
        int offsetRightX = (int) (getRightJoyStickX()/0.35);
        int offsetRightY = (int) (getRightJoyStickY()/0.35);
        int rightJoyStickId = getRightJoyStickButton() ? (getToggleRightJoyStickButton() ? 1 : 2) : 0;
        buttonSprites.get("RightJoyStickButton" + rightJoyStickId).setPosition(OFFSET_X + (32 + offsetRightX )* SCALING, (22 + offsetRightY ) * SCALING,0.3f);
        items.add(buttonSprites.get("RightJoyStickButton" + rightJoyStickId));
        // Trigger
        int leftTriggerId = (getLeftTrigger() > -0.50) ? ((getLeftTrigger() > 0) ? ((getLeftTrigger() > 0.50) ? ((getLeftTrigger() >= 0.94) ? (getToggleLeftTrigger() ? 4 : 5): 3) : 2) : 1) : 0;
        items.add(buttonSprites.get("LeftTrigger" + leftTriggerId));
        int rightTriggerId = (getRightTrigger() > -0.50) ? ((getRightTrigger() > 0) ? ((getRightTrigger() > 0.50) ? ((getRightTrigger() >= 0.94) ? (getToggleRightTrigger() ? 4 : 5): 3) : 2) : 1) : 0;
        items.add(buttonSprites.get("RightTrigger" + rightTriggerId));
        // Home
        int leftHomeId = getLeftHomeButton() ? (getToggleLeftHomeButton() ? 1 : 2) : 0;
        items.add(buttonSprites.get("LeftHome" + leftHomeId));
        int homeId = isCurrentlyActive() ? (getToggleCurrentlyActive() ? 1 : 2) : 0;
        items.add(buttonSprites.get("Home" + homeId));
        int rightHomeId = getRightHomeButton() ? (getToggleRightHomeButton() ? 1 : 2) : 0;
        items.add(buttonSprites.get("RightHome" + rightHomeId));
        // Buttons
        int northButtonId = getNorthButton() ? (getToggleNorthButton() ? 1 : 2) : 0;
        items.add(buttonSprites.get("NorthButton" + northButtonId));
        int eastButtonId = getEastButton() ? (getToggleEastButton() ? 1 : 2) : 0;
        items.add(buttonSprites.get("EastButton" + eastButtonId));
        int southButtonId = getSouthButton() ? (getToggleSouthButton() ? 1 : 2) : 0;
        items.add(buttonSprites.get("SouthButton" + southButtonId));
        int westButtonId = getWestButton() ? (getToggleWestButton() ? 1 : 2) : 0;
        items.add(buttonSprites.get("WestButton" + westButtonId));
        // DirectionPad
        int northDirectionId = getDirectionPad() == ControllerDirectionPad.NORTH ? (getToggleDirectionPad() == ControllerDirectionPad.NORTH ? 1 : 2) : 0;
        items.add(buttonSprites.get("NorthVertical" + northDirectionId));
        int southDirectionId = getDirectionPad() == ControllerDirectionPad.SOUTH ? (getToggleDirectionPad() == ControllerDirectionPad.SOUTH ? 1 : 2) : 0;
        items.add(buttonSprites.get("SouthVertical" + southDirectionId));
        int eastDirectionId = getDirectionPad() == ControllerDirectionPad.EAST ? (getToggleDirectionPad() == ControllerDirectionPad.EAST ? 1 : 2) : 0;
        items.add(buttonSprites.get("EastHorizontal" + eastDirectionId));
        int westDirectionId = getDirectionPad() == ControllerDirectionPad.WEST ? (getToggleDirectionPad() == ControllerDirectionPad.WEST ? 1 : 2) : 0;
        items.add(buttonSprites.get("WestHorizontal" + westDirectionId));
        int northEastDirectionId = getDirectionPad() == ControllerDirectionPad.NORTH_EAST ? (getToggleDirectionPad() == ControllerDirectionPad.NORTH_EAST ? 1 : 2) : 0;
        items.add(buttonSprites.get("NorthEast" + northEastDirectionId));
        int northWestDirectionId = getDirectionPad() == ControllerDirectionPad.NORTH_WEST ? (getToggleDirectionPad() == ControllerDirectionPad.NORTH_WEST ? 1 : 2) : 0;
        items.add(buttonSprites.get("NorthWest" + northWestDirectionId));
        int southEastDirectionId = getDirectionPad() == ControllerDirectionPad.SOUTH_EAST ? (getToggleDirectionPad() == ControllerDirectionPad.SOUTH_EAST ? 1 : 2) : 0;
        items.add(buttonSprites.get("SouthEast" + southEastDirectionId));
        int southWestDirectionId = getDirectionPad() == ControllerDirectionPad.SOUTH_WEST ? (getToggleDirectionPad() == ControllerDirectionPad.SOUTH_WEST ? 1 : 2) : 0;
        items.add(buttonSprites.get("SouthWest" + southWestDirectionId));

        renderer.render2DSprites(gameWindow,shader,items);
    }

    public boolean getToggleSouthButton() {
        return getSouthButton() && !getPreviousSouthButton();
    }

    public boolean getToggleEastButton() {
        return getEastButton() && !getPreviousEastButton();
    }

    public boolean getToggleWestButton() {
        return getWestButton() && !getPreviousWestButton();
    }

    public boolean getToggleNorthButton() {
        return getNorthButton() && !getPreviousNorthButton();
    }

    public boolean getToggleLeftBumperButton() {
        return getLeftBumperButton() && !getPreviousLeftBumperButton();
    }

    public boolean getToggleRightBumperButton() {
        return getRightBumperButton() && !getPreviousRightBumperButton();
    }

    public boolean getToggleLeftHomeButton() {
        return getLeftHomeButton() && !getPreviousLeftHomeButton();
    }

    public boolean getToggleRightHomeButton() {
        return getRightHomeButton() && !getPreviousRightHomeButton();
    }

    public boolean getToggleLeftJoyStickButton() {
        return getLeftJoyStickButton() && !getPreviousLeftJoyStickButton();
    }

    public boolean getToggleRightJoyStickButton() {
        return getRightJoyStickButton() && !getPreviousRightJoyStickButton();
    }

    public ControllerDirectionPad getToggleDirectionPad() {
        if(getPreviousDirectionPad() == ControllerDirectionPad.NONE && getDirectionPad() != ControllerDirectionPad.NONE) {
            return getDirectionPad();
        }
        return ControllerDirectionPad.NONE;
    }

    public boolean getToggleLeftTrigger() {
        return (currentLeftTriggerToggleState && !previousLeftTriggerToggleState);
    }

    public boolean getToggleRightTrigger() {
        return (currentRightTriggerToggleState && !previousRightTriggerToggleState);

    }

    public float getRightJoyStickXDifference() {
        return getPreviousRightJoyStickX() - getRightJoyStickX();
    }

    public float getRightJoyStickYDifference() {
        return getPreviousRightJoyStickY() - getRightJoyStickY();
    }

    public float getLeftJoyStickXDifference() {
        return getPreviousLeftJoyStickX() - getLeftJoyStickX();
    }

    public float getLeftJoyStickYDifference() {
        return getPreviousLeftJoyStickY() - getLeftJoyStickY();
    }

    public float getRightTriggerDifference() {
        return getPreviousRightTrigger() - getRightTrigger();
    }


    public float getLeftTriggerDifference() {
        return getPreviousLeftTrigger() - getLeftTrigger();
    }

    public boolean getToggleCurrentlyActive() {
        return (currentlyActive && !previousCurrentlyActive);
    }


    public boolean isCurrentlyActive() {
        return currentlyActive;
    }

    public String getName() {
        return name;
    }

    public int getControllerID() { return controllerID; }


    private ByteBuffer clone(ByteBuffer original) {
        ByteBuffer clone = ByteBuffer.allocate(original.capacity());
        original.rewind();//copy from the beginning
        clone.put(original);
        original.rewind();
        clone.flip();
        return clone;
    }

    private FloatBuffer clone(FloatBuffer original) {
        FloatBuffer clone = FloatBuffer.allocate(original.capacity());
        original.rewind();//copy from the beginning
        clone.put(original);
        original.rewind();
        clone.flip();
        return clone;
    }



    public abstract boolean getSouthButton();
    public abstract boolean getEastButton();
    public abstract boolean getWestButton();
    public abstract boolean getNorthButton();

    public abstract boolean getLeftBumperButton();
    public abstract boolean getRightBumperButton();
    public abstract boolean getLeftHomeButton();
    public abstract boolean getRightHomeButton();

    public abstract boolean getLeftJoyStickButton();
    public abstract boolean getRightJoyStickButton();

    public abstract ControllerDirectionPad getDirectionPad();

    public abstract float getLeftJoyStickX();
    public abstract float getLeftJoyStickY();

    public abstract float getRightJoyStickX();
    public abstract float getRightJoyStickY();

    public abstract float getLeftTrigger();
    public abstract float getRightTrigger();

    // Get Previous
    public abstract boolean getPreviousSouthButton();
    public abstract boolean getPreviousEastButton();
    public abstract boolean getPreviousWestButton();
    public abstract boolean getPreviousNorthButton();

    public abstract boolean getPreviousLeftBumperButton();
    public abstract boolean getPreviousRightBumperButton();
    public abstract boolean getPreviousLeftHomeButton();
    public abstract boolean getPreviousRightHomeButton();

    public abstract boolean getPreviousLeftJoyStickButton();
    public abstract boolean getPreviousRightJoyStickButton();

    public abstract ControllerDirectionPad getPreviousDirectionPad();

    public abstract float getPreviousLeftJoyStickX();
    public abstract float getPreviousLeftJoyStickY();

    public abstract float getPreviousRightJoyStickX();
    public abstract float getPreviousRightJoyStickY();

    public abstract float getPreviousLeftTrigger();
    public abstract float getPreviousRightTrigger();

    public boolean isPreviousCurrentlyActive() {
        return previousCurrentlyActive;
    }
}
