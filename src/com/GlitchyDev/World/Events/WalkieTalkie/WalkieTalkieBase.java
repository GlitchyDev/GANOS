package com.GlitchyDev.World.Events.WalkieTalkie;


import com.GlitchyDev.Rendering.Assets.Texture.Texture;
import com.GlitchyDev.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
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
    private WalkieTalkieDisplay currentWalkieTalkieDisplay;
    private long stateStartTime;
    private boolean usingTransition = false;
    private WalkieTalkieStateTransition currentWalkieTalkieStateTransition = WalkieTalkieStateTransition.NONE;
    private long transitionStartTime = 0;
    private WalkieTalkieState nextWalkieTalkieState; // Used when moving out of transition

    // Attributes
    private final WalkieTalkieDisplay assignedSpeakingIcon = WalkieTalkieDisplay.CALLSIGN_UNITY; // How they appear to other users
    private int currentVolume = 3; // Volume 3 ( Speaker ), Volume 2 ( Nearby ), Volume 1 ( Self ), Volume 0 ( Light up only )
    private int currentBatteryLevel = 4; // 4-0, 0 being dead
    private int currentChannel = 0; // Channels 0-9 and also Unknown
    private boolean isMuted = false; // If muted, calls won't be received or displayed,

    // In the moment stuff
    private int verticalOffset = 0;
    private WalkieTalkieDisplay lastSpeaker;





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

        currentWalkieTalkieState = WalkieTalkieState.ON_IDLE;
        stateStartTime = System.currentTimeMillis();
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
        renderer.render2DSpriteItem(walkieTalkie_Body,"Default2D");


        if(!currentWalkieTalkieState.isHasCustomScreen()) {
            currentWalkieTalkieDisplay = currentWalkieTalkieState.getStateScreenDisplay();
        } else {
            switch(currentWalkieTalkieState) {
                case USE_ACTIVE:
                    currentWalkieTalkieDisplay = assignedSpeakingIcon;
                    break;
                case BOOTING_ACTIVE:
                    currentWalkieTalkieDisplay = WalkieTalkieDisplay.getProgressDisplay((int)(getStateProgress() * 6));
                    break;
                case SHOW_VOLUME_ACTIVE:
                    if(!isMuted()){
                        currentWalkieTalkieDisplay = WalkieTalkieDisplay.getVolumeDisplay(currentVolume);
                    } else {
                        currentWalkieTalkieDisplay = WalkieTalkieDisplay.VOLUME_MUTE;
                    }
                    break;
                case SHOW_CHANNEL_ACTIVE:
                    currentWalkieTalkieDisplay = WalkieTalkieDisplay.getChannelDisplay(currentChannel);
                    break;
                case SHOW_BATTERY_ACTIVE:
                    currentWalkieTalkieDisplay = WalkieTalkieDisplay.getBatteryDisplay(currentBatteryLevel);
                    break;
                case TALKING_ACTIVE:
                    currentWalkieTalkieDisplay = assignedSpeakingIcon;
                    break;
                case SPEAKER_VIEW:
                    currentWalkieTalkieDisplay = lastSpeaker;
                case MESSAGE_VIEW:
                    // Current Message
                case INHABITANT_VIEW:
                    // Current Inhabitant
            }
        }
        renderer.getShader("WalkieTalkieScreen2D").bind();
        renderer.getShader("WalkieTalkieScreen2D").setUniform("gridSize",new Vector2f(WalkieTalkieDisplay.getGridWidth(),WalkieTalkieDisplay.getGridHeight()));
        renderer.getShader("WalkieTalkieScreen2D").setUniform("selectedTexture",new Vector2f(currentWalkieTalkieDisplay.getX(),currentWalkieTalkieDisplay.getY()));
        renderer.getShader("WalkieTalkieScreen2D").setUniform("selectedTexture",new Vector2f(currentWalkieTalkieDisplay.getX(),currentWalkieTalkieDisplay.getY()));
        renderer.getShader("WalkieTalkieScreen2D").setUniform("dimScreen", currentWalkieTalkieState.usesDimScreen());
        renderer.render2DSpriteItem(walkieTalkie_Screen,"WalkieTalkieScreen2D");

        if(currentWalkieTalkieState == WalkieTalkieState.SPEAKER_VIEW || currentWalkieTalkieState == WalkieTalkieState.TALKING_ACTIVE) {
            renderer.getShader("TextureGrid2D").bind();
            renderer.getShader("TextureGrid2D").setUniform("gridSize", new Vector2f(1, 7));
            final double SIGNAL_ANIMATION_LENGTH = 0.5;
            int currentSignalFrame = (int)(7 * (1.0/SIGNAL_ANIMATION_LENGTH * (System.currentTimeMillis()%(int)(1000.0*SIGNAL_ANIMATION_LENGTH))/1000.0) );
            renderer.getShader("TextureGrid2D").setUniform("selectedTexture", new Vector2f(0, currentSignalFrame));
            renderer.render2DSpriteItem(walkieTalkie_Signal, "TextureGrid2D");
        }
    }





    public boolean togglePower() {
        if (currentWalkieTalkieState.isPowered() && !currentWalkieTalkieState.isDominatingState()) {
            if(enterState(WalkieTalkieState.OFF_ACTIVE)) {
                // Play a power down sound
                return true;
            }
        } else {
            if (!currentWalkieTalkieState.isPowered() && !currentWalkieTalkieState.isDominatingState()) {
                if(enterState(WalkieTalkieState.BOOTING_ACTIVE)) {
                    // Play a booting sound
                    return true;
                }
            }
        }
        return false;
    }

    public boolean toggleTalking() {
        if (currentWalkieTalkieState.isPowered()) {
            if (currentWalkieTalkieState != WalkieTalkieState.TALKING_ACTIVE) {
                if(!currentWalkieTalkieState.isDominatingState()) {
                    if (enterState(WalkieTalkieState.TALKING_ACTIVE)) {
                        // Play taking static
                        return true;
                    }
                }
            } else {
                if (enterState(WalkieTalkieState.USE_ACTIVE)) {
                    // Play static cease
                    return true;
                }
            }
        }
        return false;
    }

    public boolean togglePullUpWalkieTalkie() {
        if (currentWalkieTalkieState.getPosition() == WalkieTalkieStatePosition.IDLE) {
            if(currentWalkieTalkieState.isPowered()) {
                if(enterState(WalkieTalkieState.USE_ACTIVE)) {
                    // Play a swipe up sound
                    return true;
                }
            } else {
                if(getCurrentBatteryLevel() > 0) {
                    if(enterState(WalkieTalkieState.OFF_ACTIVE)) {
                        // Play a swipe up sound
                        return true;
                    }
                } else {
                    if(enterState(WalkieTalkieState.DEAD_ACTIVE)) {
                        // Play a empty dead sound??
                        return true;
                    }
                }

            }
        } else {
            if(currentWalkieTalkieState.getPosition() == WalkieTalkieStatePosition.ACTIVE) {
                if(!currentWalkieTalkieState.isDominatingState()) {
                    if(currentWalkieTalkieState.isPowered()) {
                        if(enterState(WalkieTalkieState.ON_IDLE)) {
                            // Play a swipe up sound
                            return true;
                        }
                    } else {
                        if(getCurrentBatteryLevel() > 0) {
                            if(enterState(WalkieTalkieState.OFF_IDLE)) {
                                // Play a swipe up sound
                                return true;
                            }
                        } else {
                            if(enterState(WalkieTalkieState.DEAD_IDLE)) {
                                // Play a empty dead sound??
                                return true;
                            }
                        }

                    }
                }
            }
        }
        return false;
    }



    /**
     *
     * @param speaker The Speakers Icon
     * @param overridePower If this trigger should override a dead battery
     * @param overrideMute If this trigger should override a mute
     * @return If Speaker properly engaged
     */
    // COME BACK ADD CUSTOM SPEAKER SUPPORT
    public boolean triggerSpeaker(WalkieTalkieDisplay speaker, boolean overridePower, boolean overrideMute) {
        if(((currentWalkieTalkieState.isPowered() && currentBatteryLevel > 0) || overridePower) && (!isMuted || overrideMute) ) {
            // Play intro speaker noise at volume level
            if (enterState(WalkieTalkieState.SPEAKER_VIEW)) {
                lastSpeaker = speaker;
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
        if(currentWalkieTalkieState.isPowered() && !currentWalkieTalkieState.isDominatingState()) {
            isMuted = !isMuted;
            if(isMuted) {
                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    return true;
                    // play a click sound no volume
                }
            } else {
                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    return true;
                    // Play flat volume change at correct volume
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
        if(currentWalkieTalkieState.isPowered() && !currentWalkieTalkieState.isDominatingState()) {
            if(isMuted) {
                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    // Play "Error" Events
                    return true;
                }
            } else {

                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    // Play acceding sound if works, some other "Stagnant volume" if not
                    if(currentVolume != 3) {
                        currentVolume++;
                    }
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
        if(currentWalkieTalkieState.isPowered() && !currentWalkieTalkieState.isDominatingState()) {
            if(isMuted) {
                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    // Play error sound
                }
            } else {

                if(enterState(WalkieTalkieState.SHOW_VOLUME_ACTIVE)) {
                    // Play Descending sound if works, some other "Stagnant volume" if not
                    if(currentVolume != 0) {
                        currentVolume--;
                    }
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
        if(currentWalkieTalkieState.isPowered() && !currentWalkieTalkieState.isDominatingState()) {
            // Play some sort of channel change sound
            if(enterState(WalkieTalkieState.SHOW_CHANNEL_ACTIVE)) {
                currentChannel = channel;
                return true;
            }
        }
        return false;
    }

    public boolean showBattery() {
        if(currentWalkieTalkieState.isPowered() && !currentWalkieTalkieState.isDominatingState()) {
            // Play some sort of channel change sound
            if(enterState(WalkieTalkieState.SHOW_BATTERY_ACTIVE)) {
                return true;
            }
        }
        return false;
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

    private void setPosition(int x, int y) {
        walkieTalkie_Body.setPosition(x,y,0);
        walkieTalkie_Screen.setPosition(x+(5*WALKIE_TALKIE_SCALE),y+(27*WALKIE_TALKIE_SCALE),0.3f);
        walkieTalkie_Signal.setPosition(x-(4*WALKIE_TALKIE_SCALE),y-(5*WALKIE_TALKIE_SCALE),0.5f);
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

    public WalkieTalkieDisplay getAssignedSpeakingIcon() {
        return assignedSpeakingIcon;
    }
}
