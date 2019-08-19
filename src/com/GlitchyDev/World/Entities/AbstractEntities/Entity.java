package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Effects.Abstract.EntityEffect;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public abstract class Entity {
    // Utill
    protected final WorldGameState worldGameState;
    // Saved to file
    protected final EntityType entityType;
    protected final UUID entityUUID;
    protected UUID currentRegionUUID;
    protected Location location;
    protected Direction direction;
    protected final ArrayList<EntityEffect> currentEffects;


    /**
     * Creates Entities naturally
     * @param entityType
     * @param worldGameState
     * @param currentRegionUUID
     * @param location
     * @param direction
     */
    public Entity(EntityType entityType, WorldGameState worldGameState, UUID currentRegionUUID, Location location, Direction direction) {
        this.entityType = entityType;
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;
        this.entityUUID = UUID.randomUUID();
        this.location = location;
        this.direction = direction;
        this.currentEffects = new ArrayList<>();
    }


    /**
     * Allows Entities to be constructed from IO/Packets, PlayerFiles, Region Files, Ect
     * Only apparent limitation is that the "Location" is encoded as a relative within its Region, and must be calibrated
     * @param entityType
     * @param worldGameState
     * @param inputBitUtility
     * @param worldUUID
     * @param currentRegionUUID
     * @throws IOException
     */
    // This particular constructor is used to retrieve a single entity from file, or from a spawn packet, or from region files
    public Entity(EntityType entityType, WorldGameState worldGameState, InputBitUtility inputBitUtility, UUID worldUUID, UUID currentRegionUUID) throws IOException {
        this.entityType = entityType;
        this.worldGameState = worldGameState;
        this.entityUUID = inputBitUtility.getNextUUID();
        Location relativeLocation = new Location(inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), worldUUID);
        this.currentRegionUUID = currentRegionUUID;
        // This location will be updated on Spawn\
        this.location = relativeLocation;
        this.direction = Direction.getCompleteCardinal()[inputBitUtility.getNextCorrectedIntBit(3)];
        int totalEffects = inputBitUtility.getNextCorrectIntByte();
        this.currentEffects = new ArrayList<>(totalEffects);
        for(int i = 0; i < totalEffects; i++) {
            EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
            EntityEffect effect = (EntityEffect) effectType.getEffectFromInput(inputBitUtility, worldGameState);
            effect.applyEntityEffect(this);
            currentEffects.add(effect);
        }
    }

    /**
     * Writes file to IO and Packet
     * @param outputBitUtility
     * @throws IOException
     */
    public void writeData(OutputBitUtility outputBitUtility, boolean isReplicated) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(entityType.ordinal());
        outputBitUtility.writeNextUUID(entityUUID);

        Region currentRegion = worldGameState.getRegionAtLocation(location);
        Location internalOffset = currentRegion.getLocation().getLocationDifference(getLocation());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getX());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getY());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getZ());

        outputBitUtility.writeNextCorrectedBitsInt(direction.ordinal(),3);

        int replicatedEffectCount = 0;
        for(int i = 0; i < currentEffects.size(); i++) {
            if(!isReplicated || currentEffects.get(i).isReplicatedEffect() ) {
                replicatedEffectCount++;
            }
        }

        outputBitUtility.writeNextCorrectByteInt(replicatedEffectCount);
        for(int i = 0; i < currentEffects.size(); i++) {
            if(!isReplicated || currentEffects.get(i).isReplicatedEffect() ) {
                currentEffects.get(i).writeData(outputBitUtility);
            }
        }

    }

    public abstract void onSpawn(SpawnReason spawnReason);

    public abstract void onDespawn(DespawnReason despawnReason);




    public void setDirection(Direction newDirection) {
        Direction oldDirection = direction;
        this.direction = newDirection;
        if(worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState) worldGameState).replicateChangeDirectionEntity(entityUUID,getLocation().getWorldUUID(),oldDirection,newDirection);
        }
    }

    public void applyEffect(EntityEffect effect) {
        effect.applyEntityEffect(this);
        currentEffects.add(effect);
        if(effect.isReplicatedEffect() && worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState)worldGameState).replicateEntityEffectAdded(this, effect);
        }
    }

    public void removeEffect(EntityEffect effect) {
        if(effect.isReplicatedEffect() && worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState)worldGameState).replicateEntityEffectRemoved(this, effect);
        }
        effect.removeEntityEffect();
        currentEffects.remove(effect);
    }




    public UUID getUUID() {
        return entityUUID;
    }

    public UUID getCurrentRegionUUID() {
        return currentRegionUUID;
    }

    public void setCurrentRegionUUID(UUID newRegionUUID) {
        this.currentRegionUUID = newRegionUUID;
    }

    public UUID getWorldUUID() {
        return getLocation().getWorldUUID();
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Direction getDirection() {
        return direction;
    }


    public EntityType getEntityType() {
        return entityType;
    }

    public ArrayList<EntityEffect> getCurrentEffects() {
        return currentEffects;
    }

    @Override
    public String toString() {
        return "e@" + entityType + "," + entityUUID + "," + location + "," + direction;
    }
}
