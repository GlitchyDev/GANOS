package com.GlitchyDev.World.Elements;

import com.GlitchyDev.Rendering.Assets.Texture.Texture;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import org.joml.Vector2f;

public class WalkieTalkie {
    private final SpriteItem walkieTalkie_Body;
    private final SpriteItem walkieTalkie_Screen;
    private final SpriteItem walkieTalkie_Signal;
    private final Texture walkieTalkie_Body_Texture;
    private final Texture walkieTalkie_Screen_Texture;
    private final Texture walkieTalkie_Signal_Texture;
    private final float WALKIE_TALKIE_SCALE = 3.0f;
    // Attributes
    private int currentVolume = 3;
    private int currentBattery = 4;
    private int currentChannel = 0;
    private int assignedSpeakingIcon = 0;
    // States and Transitions
    private WalkieTalkieState currentWalkieTalkieState;
    private long stateStartTime;

    private WalkieTalkieTransition currentWalkieTalkieTransition = WalkieTalkieTransition.NONE;
    private long transitionStartTime = 0;
    private boolean usingTransition = false;
    private WalkieTalkieState nextWalkieTalkieState;
    // In the moment stuff
    private int curentSpeakerIcon = 0;
    private double speakerLength = 10.0;

    private int offset = 0;





    public WalkieTalkie() {
        walkieTalkie_Body_Texture = AssetLoader.getTextureAsset("WalkieTalkie_Body");
        walkieTalkie_Screen_Texture = AssetLoader.getTextureAsset("WalkieTalkie_Screen");
        walkieTalkie_Signal_Texture = AssetLoader.getTextureAsset("WalkieTalkie_Signal");

        walkieTalkie_Body = new SpriteItem(walkieTalkie_Body_Texture,true);
        walkieTalkie_Screen = new SpriteItem(walkieTalkie_Screen_Texture,13,9,true);
        walkieTalkie_Signal = new SpriteItem(walkieTalkie_Signal_Texture, 20, 13,true);
        walkieTalkie_Body.setScale(WALKIE_TALKIE_SCALE);
        walkieTalkie_Screen.setScale(WALKIE_TALKIE_SCALE);
        walkieTalkie_Signal.setScale(WALKIE_TALKIE_SCALE);

        currentWalkieTalkieState = WalkieTalkieState.TALKING_ACTIVE;
        stateStartTime = System.currentTimeMillis();

    }


    // **************************************************

    public void tick() {
        if(usingTransition) {
            System.out.println("TRANSITION " + getTransitionProgress());
            if(System.currentTimeMillis() >= transitionStartTime + currentWalkieTalkieTransition.getLength() * 1000) {
                usingTransition = false;
                currentWalkieTalkieTransition = WalkieTalkieTransition.NONE;
                currentWalkieTalkieState = nextWalkieTalkieState;
                stateStartTime = System.currentTimeMillis();
            }
            offset = (int) (currentWalkieTalkieState.getOffset() * WALKIE_TALKIE_SCALE) + (int)(currentWalkieTalkieTransition.getOffsetDifference() * getTransitionProgress() * WALKIE_TALKIE_SCALE);
        } else {
            System.out.println(currentWalkieTalkieState);

            if (currentWalkieTalkieState.hasTimeLimit()) {
                if (System.currentTimeMillis() >= stateStartTime + currentWalkieTalkieState.getStateLength() * 1000) {
                    enterState(currentWalkieTalkieState.getNextState());
                }
            }
            offset = (int) (currentWalkieTalkieState.getOffset() * WALKIE_TALKIE_SCALE);
        }

    }

    public void render(Renderer renderer, int windowHeight) {

        setPosition((int) (4 * WALKIE_TALKIE_SCALE), 500 - offset);
        renderer.render2DSprite(walkieTalkie_Body,"Default2D");
        renderer.getShader("TextureGrid2D").bind();
        renderer.getShader("TextureGrid2D").setUniform("gridSize",new Vector2f(19,10));
        renderer.getShader("TextureGrid2D").setUniform("selectedTexture",new Vector2f(0,0));
        renderer.render2DSprite(walkieTalkie_Screen,"TextureGrid2D");

        if(currentWalkieTalkieState == WalkieTalkieState.SPEAKER_VIEW || currentWalkieTalkieState == WalkieTalkieState.TALKING_ACTIVE) {
            renderer.getShader("TextureGrid2D").bind();
            renderer.getShader("TextureGrid2D").setUniform("gridSize", new Vector2f(1, 7));
            final double SIGNAL_ANIMATION_LENGTH = 0.5;
            int currentSignalFrame = (int)(7 * (1.0/SIGNAL_ANIMATION_LENGTH * (System.currentTimeMillis()%(int)(1000.0*SIGNAL_ANIMATION_LENGTH))/1000.0) );
            System.out.println(currentSignalFrame);
            renderer.getShader("TextureGrid2D").setUniform("selectedTexture", new Vector2f(0, currentSignalFrame));
            renderer.render2DSprite(walkieTalkie_Signal, "TextureGrid2D");
        }
    }



    private void setPosition(int x, int y) {
        walkieTalkie_Body.setPosition(x,y,0);
        walkieTalkie_Screen.setPosition(x+(5*WALKIE_TALKIE_SCALE),y+(27*WALKIE_TALKIE_SCALE),0.3f);
        walkieTalkie_Signal.setPosition(x-(4*WALKIE_TALKIE_SCALE),y-(5*WALKIE_TALKIE_SCALE),0.5f);
    }

    public void enterState(WalkieTalkieState nextState) {
        WalkieTalkieTransition transition = WalkieTalkieTransition.getWalkieTalkieTransition(currentWalkieTalkieState,nextState);
        if(transition == WalkieTalkieTransition.NONE) {
            currentWalkieTalkieState = nextState;
            stateStartTime = System.currentTimeMillis();
        } else {
            usingTransition = true;
            currentWalkieTalkieTransition = transition;
            transitionStartTime = System.currentTimeMillis();
            nextWalkieTalkieState = nextState;
        }

    }

    public void triggerSpeaker(int speaker, double length, boolean overrideBattery) {
        if(currentBattery > 0 || overrideBattery) {
            enterState(WalkieTalkieState.SPEAKER_VIEW);
            curentSpeakerIcon = speaker;
            speakerLength = length;
        }
    }


    private double getStateProgress() {
        if(currentWalkieTalkieState.hasTimeLimit) {
            return 1.0 / currentWalkieTalkieState.getStateLength() * ((System.currentTimeMillis() - stateStartTime) / 1000.0);
        }
        return -1;
    }

    private double getTransitionProgress() {
        if(usingTransition) {
            return 1.0/currentWalkieTalkieTransition.getLength() * ((System.currentTimeMillis() - transitionStartTime)/1000.0);
        }
        return -1;
    }

    private enum WalkieTalkieState {
        // Idle States
        ON_IDLE(WalkieTalkiePosition.IDLE), // Is able to receive messages, enter USE, battery drain
        OFF_IDLE(WalkieTalkiePosition.IDLE), // Is able to enter Booting, no passive drain
        DEAD_IDLE(WalkieTalkiePosition.IDLE), // No Battery, can not boot


        SPEAKER_VIEW(WalkieTalkiePosition.VIEW), // As long as required, shift to Idle
        MESSAGE_VIEW(WalkieTalkiePosition.VIEW), // As long as required, shift to Idle
        INHABITANT_VIEW(WalkieTalkiePosition.VIEW), // As long as required, shift to Idle

        OFF_ACTIVE(WalkieTalkiePosition.ACTIVE), // No passive battery drain, can enter boot
        BOOTING_ACTIVE(WalkieTalkiePosition.ACTIVE, 10.0), // Enters USE, consumes some battery?
        DEAD_ACTIVE(WalkieTalkiePosition.ACTIVE, 10.0), // Forced when battery is < 0% No Activity

        USE_ACTIVE(WalkieTalkiePosition.ACTIVE,10.0), // General Hub for all actions, returns to Idle
        TALKING_ACTIVE(WalkieTalkiePosition.ACTIVE), // As long as required, consumes Battery, emits signal, lights sign
        SHOW_VOLUME_ACTIVE(WalkieTalkiePosition.ACTIVE, 5.0), // Shows volume level
        ADJUST_VOLUME_ACTIVE(WalkieTalkiePosition.ACTIVE, 1.0), // Adjusts volume, returns to Show Volume
        SHOW_BATTERY_ACTIVE(WalkieTalkiePosition.ACTIVE, 1.0), // Shows battery level
        SHOW_CHANNEL_ACTIVE(WalkieTalkiePosition.ACTIVE, 1.0), // Adjusts Channel
        ;


        private final double stateLength;
        private final boolean hasTimeLimit;
        private final WalkieTalkiePosition position;
        WalkieTalkieState(WalkieTalkiePosition position, double stateLength) {
            this.position = position;
            this.stateLength = stateLength;
            this.hasTimeLimit = true;
        }
        WalkieTalkieState(WalkieTalkiePosition position) {
            this.position = position;
            this.stateLength = 0.0;
            this.hasTimeLimit = false;
        }

        public double getStateLength() {
            return stateLength;
        }
        public boolean hasTimeLimit() {
            return hasTimeLimit;
        }

        public WalkieTalkiePosition getPosition() {
            return position;
        }

        public WalkieTalkieState getNextState() {
            switch(this) {
                case SPEAKER_VIEW:
                    return ON_IDLE;
                case MESSAGE_VIEW:
                    return ON_IDLE;
                case INHABITANT_VIEW:
                    return ON_IDLE;
                case OFF_ACTIVE:
                    return OFF_IDLE;
                case BOOTING_ACTIVE:
                    return USE_ACTIVE;
                case DEAD_ACTIVE:
                    return DEAD_IDLE;
                case TALKING_ACTIVE:
                case SHOW_VOLUME_ACTIVE:
                case ADJUST_VOLUME_ACTIVE:
                case SHOW_BATTERY_ACTIVE:
                case SHOW_CHANNEL_ACTIVE:
                    return USE_ACTIVE;
                default:
                    return ON_IDLE;
            }
        }

        public int getOffset() {
            switch(this.position) {
                case IDLE:
                    return 21;
                case VIEW:
                    return 38;
                case ACTIVE:
                    return 56;
                default:
                    return 56;
            }
        }
    }


    private enum WalkieTalkiePosition {
        IDLE,
        VIEW,
        ACTIVE,
    }

    private enum WalkieTalkieTransition {
        NONE(0.0),

        IDLE_TO_VIEW(0.5),
        VIEW_TO_IDLE(0.2),

        IDLE_TO_ACTIVE(0.5),
        ACTIVE_TO_IDLE(0.2);

        private final double length;
        WalkieTalkieTransition(double length) {
            this.length = length;
        }

        public static WalkieTalkieTransition getWalkieTalkieTransition(WalkieTalkieState previous, WalkieTalkieState next) {
            switch(previous.position) {
                case IDLE:
                    switch(next.position) {
                        case VIEW:
                            return IDLE_TO_VIEW;
                        case ACTIVE:
                            return IDLE_TO_ACTIVE;
                    }
                case VIEW:
                    return VIEW_TO_IDLE;
                case ACTIVE:
                    return ACTIVE_TO_IDLE;
            }
            return NONE;
        }

        public double getLength() {
            return length;
        }

        public int getOffsetDifference() {
            switch(this) {
                case IDLE_TO_VIEW:
                    return 17;
                case VIEW_TO_IDLE:
                    return -17;
                case IDLE_TO_ACTIVE:
                    return 35;
                case ACTIVE_TO_IDLE:
                    return -35;
                default:
                    return 0;
            }
        }
    }
}
