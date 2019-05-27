package com.GlitchyDev.World.Elements.WalkieTalkie;

import com.GlitchyDev.Rendering.Assets.Texture.Texture;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.World.Elements.WalkieTalkie.Enums.WalkieTalkieScreenDisplay;
import com.GlitchyDev.World.Elements.WalkieTalkie.Enums.WalkieTalkieState;
import com.GlitchyDev.World.Elements.WalkieTalkie.Enums.WalkieTalkieStateTransition;
import org.joml.Vector2f;

public class WalkieTalkieBase {
    // Sprites and Rendering
    private final float WALKIE_TALKIE_SCALE = 3.0f;
    private final SpriteItem walkieTalkie_Body;
    private final SpriteItem walkieTalkie_Screen;
    private final SpriteItem walkieTalkie_Signal;
    private final Texture walkieTalkie_Body_Texture;
    private final Texture walkieTalkie_Screen_Texture;
    private final Texture walkieTalkie_Signal_Texture;
    // States and Transitions
    private WalkieTalkieState currentWalkieTalkieState;
    private WalkieTalkieScreenDisplay currentWalkieTalkieDisplay;
    private long stateStartTime;
    private boolean usingTransition = false;
    private WalkieTalkieStateTransition currentWalkieTalkieStateTransition = WalkieTalkieStateTransition.NONE;
    private long transitionStartTime = 0;
    private WalkieTalkieState nextWalkieTalkieState; // Used when moving out of transition
    // Attributes
    private final WalkieTalkieScreenDisplay assignedSpeakingIcon = WalkieTalkieScreenDisplay.CALLSIGN_UNITY; // How they appear to other users
    private int currentVolume = 3; // Volume 3 ( Speaker ), Volume 2 ( Nearby ), Volume 1 ( Self ), Volume 0 ( Light up only )
    private int currentBatteryLevel = 4; // 4-0, 0 being dead
    private int currentChannel = 0; // Channels 0-9 and also Unknown
    private boolean isMuted = false; // If muted, calls won't be received or displayed,
    // In the moment stuff
    private int verticalOffset = 0;





    public WalkieTalkieBase() {
        walkieTalkie_Body_Texture = AssetLoader.getTextureAsset("WalkieTalkie_Body");
        walkieTalkie_Screen_Texture = AssetLoader.getTextureAsset("WalkieTalkie_Screen");
        walkieTalkie_Signal_Texture = AssetLoader.getTextureAsset("WalkieTalkie_Signal");

        walkieTalkie_Body = new SpriteItem(walkieTalkie_Body_Texture,true);
        walkieTalkie_Screen = new SpriteItem(walkieTalkie_Screen_Texture,13,9,true);
        walkieTalkie_Signal = new SpriteItem(walkieTalkie_Signal_Texture, 20, 13,true);
        walkieTalkie_Body.setScale(WALKIE_TALKIE_SCALE);
        walkieTalkie_Screen.setScale(WALKIE_TALKIE_SCALE);
        walkieTalkie_Signal.setScale(WALKIE_TALKIE_SCALE);

        currentWalkieTalkieState = WalkieTalkieState.USE_ACTIVE;
        stateStartTime = System.currentTimeMillis();
        currentWalkieTalkieDisplay = assignedSpeakingIcon;

    }


    // **************************************************

    public void tick() {
        if(usingTransition) {
            if(System.currentTimeMillis() >= transitionStartTime + currentWalkieTalkieStateTransition.getLength() * 1000) {
                usingTransition = false;
                currentWalkieTalkieStateTransition = WalkieTalkieStateTransition.NONE;
                currentWalkieTalkieState = nextWalkieTalkieState;
                stateStartTime = System.currentTimeMillis();
            }
            verticalOffset = (int) (currentWalkieTalkieState.getOffset() * WALKIE_TALKIE_SCALE) + (int)(currentWalkieTalkieStateTransition.getOffsetDifference() * getTransitionProgress() * WALKIE_TALKIE_SCALE);
        } else {
            if (currentWalkieTalkieState.hasSetTimeLimit()) {
                if (System.currentTimeMillis() >= stateStartTime + currentWalkieTalkieState.getStateLength() * 1000) {
                    enterState(currentWalkieTalkieState.getNextState());
                }
            } else {

            }
            verticalOffset = (int) (currentWalkieTalkieState.getOffset() * WALKIE_TALKIE_SCALE);
        }

    }

    public void render(Renderer renderer, int windowHeight) {

        setPosition((int) (4 * WALKIE_TALKIE_SCALE), windowHeight - verticalOffset);
        renderer.render2DSprite(walkieTalkie_Body,"Default2D");

        renderer.getShader("TextureGrid2D").bind();
        renderer.getShader("TextureGrid2D").setUniform("gridSize",new Vector2f(19,10));
        renderer.getShader("TextureGrid2D").setUniform("selectedTexture",new Vector2f(currentWalkieTalkieDisplay.getX(),currentWalkieTalkieDisplay.getY()));
        renderer.render2DSprite(walkieTalkie_Screen,"TextureGrid2D");

        if(currentWalkieTalkieState == WalkieTalkieState.SPEAKER_VIEW || currentWalkieTalkieState == WalkieTalkieState.TALKING_ACTIVE) {
            renderer.getShader("TextureGrid2D").bind();
            renderer.getShader("TextureGrid2D").setUniform("gridSize", new Vector2f(1, 7));
            final double SIGNAL_ANIMATION_LENGTH = 0.5;
            int currentSignalFrame = (int)(7 * (1.0/SIGNAL_ANIMATION_LENGTH * (System.currentTimeMillis()%(int)(1000.0*SIGNAL_ANIMATION_LENGTH))/1000.0) );
            renderer.getShader("TextureGrid2D").setUniform("selectedTexture", new Vector2f(0, currentSignalFrame));
            renderer.render2DSprite(walkieTalkie_Signal, "TextureGrid2D");
        }
    }


    /**
     *
     * @param speaker The Speakers Icon
     * @param overrideBattery If this trigger should override a dead battery
     * @param overrideMute If this trigger should override a mute
     * @return If Speaker properly engaged
     */
    public boolean triggerSpeaker(WalkieTalkieScreenDisplay speaker, boolean overrideBattery, boolean overrideMute) {
        if((currentBatteryLevel > 0 || overrideBattery) && (!isMuted || overrideMute) ) {
            if (enterState(WalkieTalkieState.SPEAKER_VIEW)) {
                currentWalkieTalkieDisplay = speaker;
                return true;
            }
        }
        return false;
    }


    /**
     * Used to end a Speakers talking
     * @return If speaker properly disengaged
     */
    public boolean endSpeaker() {
        if(currentWalkieTalkieState == WalkieTalkieState.SPEAKER_VIEW) {
            if(enterState(WalkieTalkieState.ON_IDLE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Used to toggle the current Mute condition of the Walkie Talkie
     * @return If mute has been toggled properly
     */
    public boolean toggleMute() {
        if(!currentWalkieTalkieState.isDominatingState()) {
            isMuted = !isMuted;
            if(isMuted) {
                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    currentWalkieTalkieDisplay = WalkieTalkieScreenDisplay.VOLUME_MUTE;
                    return true;
                }
            } else {
                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    currentWalkieTalkieDisplay = WalkieTalkieScreenDisplay.getVolumeDisplay(currentVolume);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Increases the Walkie Talkies volume ( To a Max of 3 )
     * @return If a volume Increase attempt was successful ( Volume may already be max )
     */
    public boolean increaseVolume() {
        if(!currentWalkieTalkieState.isDominatingState()) {
            if(isMuted) {
                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    currentWalkieTalkieDisplay = WalkieTalkieScreenDisplay.VOLUME_MUTE;
                    return true;
                }
            } else {

                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    if(currentVolume != 3) {
                        currentVolume++;
                    }
                    currentWalkieTalkieDisplay = WalkieTalkieScreenDisplay.getVolumeDisplay(currentVolume);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @return
     */
    public boolean decreaseVolume() {
        if(!currentWalkieTalkieState.isDominatingState()) {
            if(isMuted) {
                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    currentWalkieTalkieDisplay = WalkieTalkieScreenDisplay.VOLUME_MUTE;
                }
            } else {

                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    if(currentVolume != 0) {
                        currentVolume--;
                    }
                    currentWalkieTalkieDisplay = WalkieTalkieScreenDisplay.getVolumeDisplay(currentVolume);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Decreases the Walkie Talkies volume ( To a Min of 0 )
     * @return If a volume decrease attempt was successful ( Volume may already be min )
     * @param channel
     * @return
     */
    public boolean changeChannel(int channel) {
        if(!currentWalkieTalkieState.isDominatingState()) {
            if(enterState(WalkieTalkieState.SHOW_CHANNEL_ACTIVE)) {
                currentChannel = channel;
                currentWalkieTalkieDisplay = WalkieTalkieScreenDisplay.getChannelDisplay(currentChannel);
                return true;
            }
         }
        return false;
    }



    private void setPosition(int x, int y) {
        walkieTalkie_Body.setPosition(x,y,0);
        walkieTalkie_Screen.setPosition(x+(5*WALKIE_TALKIE_SCALE),y+(27*WALKIE_TALKIE_SCALE),0.3f);
        walkieTalkie_Signal.setPosition(x-(4*WALKIE_TALKIE_SCALE),y-(5*WALKIE_TALKIE_SCALE),0.5f);
    }

    /**
     *
     * @param nextState
     * @return Success of entering State
     */
    private boolean enterState(WalkieTalkieState nextState) {
        if(!usingTransition) {
            WalkieTalkieStateTransition transition = WalkieTalkieStateTransition.getWalkieTalkieTransition(currentWalkieTalkieState, nextState);
            if (transition == WalkieTalkieStateTransition.NONE) {
                currentWalkieTalkieState = nextState;
                stateStartTime = System.currentTimeMillis();
            } else {
                usingTransition = true;
                currentWalkieTalkieStateTransition = transition;
                transitionStartTime = System.currentTimeMillis();
                nextWalkieTalkieState = nextState;
            }
            return true;
        } else {
            System.out.println("Walkie Talkie State Ignored ( Already Transitioning ) " + nextState);
            return false;
        }
    }

    public double getStateProgress() {
        if(currentWalkieTalkieState.hasSetTimeLimit()) {
            return 1.0 / currentWalkieTalkieState.getStateLength() * ((System.currentTimeMillis() - stateStartTime) / 1000.0);
        }
        return -1;
    }

    public double getTransitionProgress() {
        if(usingTransition) {
            return 1.0/ currentWalkieTalkieStateTransition.getLength() * ((System.currentTimeMillis() - transitionStartTime)/1000.0);
        }
        return -1;
    }

    public int getCurrentBatteryLevel() {
        return currentBatteryLevel;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public int getCurrentChannel() {
        return currentChannel;
    }

    public int getCurrentVolume() {
        return currentVolume;
    }

    public WalkieTalkieState getCurrentWalkieTalkieState() {
        return currentWalkieTalkieState;
    }

    public WalkieTalkieScreenDisplay getAssignedSpeakingIcon() {
        return assignedSpeakingIcon;
    }
}
