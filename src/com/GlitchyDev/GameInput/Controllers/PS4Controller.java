package com.GlitchyDev.GameInput.Controllers;

public class PS4Controller extends GameController {


    public PS4Controller(int controllerID) {
        super(controllerID);
    }


    // South
    public boolean getEastButton() {
        return buttons[0].get(2) == 1;
    }
    // East
    public boolean getWestButton() {
        return buttons[0].get(0) == 1;
    }
    // West
    public boolean getSouthButton() {
        return buttons[0].get(1) == 1;
    }
    public boolean getNorthButton() {
        return buttons[0].get(3) == 1;
    }


    public boolean getLeftBumperButton() {
        return buttons[0].get(4) == 1;
    }
    public boolean getRightBumperButton() {
        return buttons[0].get(5) == 1;
    }

    public boolean getLeftHomeButton() {
        return buttons[0].get(8) == 1;
    }
    public boolean getRightHomeButton() {
        return buttons[0].get(9) == 1;
    }

    @Override
    public boolean getLeftJoyStickButton() {
        return buttons[0].get(10) == 1;
    }

    @Override
    public boolean getRightJoyStickButton() {
        return buttons[0].get(11) == 1;
    }

    public ControllerDirectionPad getDirectionPad() {
        if(buttons[0].get(14) == 1) {
            if(buttons[0].get(15) == 1) {
                return ControllerDirectionPad.NORTH_EAST;
            }
            if(buttons[0].get(17) == 1) {
                return ControllerDirectionPad.NORTH_WEST;
            }
            return ControllerDirectionPad.NORTH;
        }
        if(buttons[0].get(16) == 1) {
            if(buttons[0].get(15) == 1) {
                return ControllerDirectionPad.SOUTH_EAST;
            }
            if(buttons[0].get(17) == 1) {
                return ControllerDirectionPad.SOUTH_WEST;
            }
            return ControllerDirectionPad.SOUTH;
        }
        if(buttons[0].get(15) == 1) {
            return ControllerDirectionPad.EAST;
        }
        if(buttons[0].get(17) == 1) {
            return ControllerDirectionPad.WEST;
        }
        return ControllerDirectionPad.NONE;
    }

    public float getLeftJoyStickX() {
        return axes[0].get(0);
    }
    public float getLeftJoyStickY() {
        return axes[0].get(1);
    }

    @Override
    public float getRightJoyStickX() {
        return axes[0].get(2);
    }

    @Override
    public float getRightJoyStickY() {
        return axes[0].get(5);
    }

    @Override
    public float getLeftTrigger() {
        return axes[0].get(3);
    }

    @Override
    public float getRightTrigger() {
        return axes[0].get(4);
    }




    public boolean getPreviousEastButton() {
        return buttons[1].get(2) == 1;
    }
    // East
    public boolean getPreviousWestButton() {
        return buttons[1].get(0) == 1;
    }
    // West
    public boolean getPreviousSouthButton() {
        return buttons[1].get(1) == 1;
    }
    public boolean getPreviousNorthButton() {
        return buttons[1].get(3) == 1;
    }


    public boolean getPreviousLeftBumperButton() {
        return buttons[1].get(4) == 1;
    }
    public boolean getPreviousRightBumperButton() {
        return buttons[1].get(5) == 1;
    }

    public boolean getPreviousLeftHomeButton() {
        return buttons[1].get(8) == 1;
    }
    public boolean getPreviousRightHomeButton() {
        return buttons[1].get(9) == 1;
    }

    @Override
    public boolean getPreviousLeftJoyStickButton() {
        return buttons[1].get(10) == 1;
    }

    @Override
    public boolean getPreviousRightJoyStickButton() {
        return buttons[1].get(11) == 1;
    }

    @Override
    public ControllerDirectionPad getPreviousDirectionPad() {
        if(buttons[1].get(14) == 1) {
            if(buttons[1].get(15) == 1) {
                return ControllerDirectionPad.NORTH_EAST;
            }
            if(buttons[1].get(17) == 1) {
                return ControllerDirectionPad.NORTH_WEST;
            }
            return ControllerDirectionPad.NORTH;
        }
        if(buttons[1].get(16) == 1) {
            if(buttons[1].get(15) == 1) {
                return ControllerDirectionPad.SOUTH_EAST;
            }
            if(buttons[1].get(17) == 1) {
                return ControllerDirectionPad.SOUTH_WEST;
            }
            return ControllerDirectionPad.SOUTH;
        }
        if(buttons[1].get(15) == 1) {
            return ControllerDirectionPad.EAST;
        }
        if(buttons[1].get(17) == 1) {
            return ControllerDirectionPad.WEST;
        }
        return ControllerDirectionPad.NONE;
    }


    @Override
    public float getPreviousLeftTrigger() {
        return axes[1].get(3);
    }

    @Override
    public float getPreviousRightTrigger() {
        return axes[1].get(4);
    }



    public float getPreviousLeftJoyStickX() {
        return axes[1].get(0);
    }
    public float getPreviousLeftJoyStickY() {
        return axes[1].get(1);
    }

    @Override
    public float getPreviousRightJoyStickX() {
        return axes[1].get(2);
    }

    @Override
    public float getPreviousRightJoyStickY() {
        return axes[1].get(5);
    }

}
