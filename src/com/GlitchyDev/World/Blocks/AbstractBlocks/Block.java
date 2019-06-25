package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Effects.Abstract.BlockEffect;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Block {
    protected final WorldGameState worldGameState;

    private final BlockType blockType;
    protected boolean doDisableFrustumCulling = false;
    protected boolean isInsideFrustum = false;
    // Others
    protected Location location;
    protected UUID regionUUID;
    protected ArrayList<BlockEffect> effects;



    public Block(WorldGameState worldGameState, BlockType blockType, Location location) {
        this.worldGameState = worldGameState;
        this.blockType = blockType;
        this.location = location;
        effects = new ArrayList<>();
        regionUUID = null;
    }

    public Block(WorldGameState worldGameState, BlockType blockType, UUID regionUUID, InputBitUtility inputBitUtility) throws IOException {
        this.worldGameState = worldGameState;
        this.blockType = blockType;
        this.regionUUID = regionUUID;
        this.location = new Location(0,0,0,null);
        int effectSize = inputBitUtility.getNextCorrectIntByte();
        effects = new ArrayList<>(effectSize);
        for(int i = 0; i < effectSize; i++) {
            EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
            BlockEffect effect = (BlockEffect) effectType.getEffectFromInput(inputBitUtility,worldGameState);
            applyEffect(effect);
        }
    }

    // Do not write Location, as that can be refereed engineered from the read protocol
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(blockType.ordinal());
        outputBitUtility.writeNextCorrectByteInt(effects.size());
        for(int i = 0; i < effects.size(); i++) {
            effects.get(i).writeData(outputBitUtility);
        }
    }

    // DO NOT CLONE LOCATION OR FRUSTUM CULLING/ECT
    public abstract Block getCopy();

    // Do not include location or disable fustruum culling, ect


    @Override
    public abstract int hashCode();


    public void applyEffect(BlockEffect effect) {
        effect.applyBlockEffect(this);
        effects.add(effect);
        if(effect.isReplicatedEffect()) {
            ((ServerWorldGameState)worldGameState).replicateBlockEffectAdded(this, effect);
        }
    }

    public void removeEffect(BlockEffect effect) {
        effect.applyBlockEffect(this);
        effects.remove(effect);
        if(effect.isReplicatedEffect()) {
            ((ServerWorldGameState)worldGameState).replicateBlockEffectRemoved(this, effect);
        }
    }

    /**
     * Reminder, this is for equivalency of nonLocation, not if they are the same block, which an == could handle
     * @param obj
     * @return
     */
    @Override
    public abstract boolean equals(Object obj);


    // Getters

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public Location getRegionOffsetLocation() {
        Region region = worldGameState.getRegion(regionUUID,location.getWorldUUID());
        return region.getLocation().getLocationDifference(location);
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public boolean isDoDisableFrustumCulling() {
        return doDisableFrustumCulling;
    }

    public void setDoDisableFrustumCulling(boolean doDisableFrustumCulling) {
        this.doDisableFrustumCulling = doDisableFrustumCulling;
    }

    public boolean isInsideFrustum() {
        return isInsideFrustum;
    }

    public void setInsideFrustum(boolean insideFrustum) {
        this.isInsideFrustum = insideFrustum;
    }

    public ArrayList<BlockEffect> getEffects() {
        return effects;
    }

    public UUID getRegionUUID() {
        return regionUUID;
    }

    public void setRegionUUID(UUID regionUUID) {
        this.regionUUID = regionUUID;
    }

    @Override
    public String toString() {
        return "e@" + blockType + "," + location;
    }
}
