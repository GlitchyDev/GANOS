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
    protected WorldGameState worldGameState;
    protected UUID currentRegionUUID;
    // Saved to file
    protected final EntityType entityType;
    protected final UUID uuid;
    protected Location location;
    protected Direction direction;
    protected final ArrayList<EntityEffect> effects;


    /**
     * Creates Entities naturally
     * @param worldGameState
     * @param currentRegionUUID
     * @param entityType
     * @param location
     * @param direction
     */
    public Entity(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, Location location, Direction direction) {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;
        this.entityType = entityType;
        this.uuid = UUID.randomUUID();
        this.location = location;
        this.direction = direction;
        this.effects = new ArrayList<>();
    }

    /**
     * Allows Entities to be constructed from IO/Packets, PlayerFiles, Region Files, Ect
     * Only apparent limitation is that the "Location" is encoded as a relative within its Region, and must be calibrated
     * @param worldGameState
     * @param worldUUID
     * @param currentRegionUUID
     * @param inputBitUtility
     * @param entityType
     * @throws IOException
     */

    // This particular constructor is used to retrieve a single entity from file, or from a spawn packet, or from region files
    public Entity(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;
        this.entityType = entityType;
        this.uuid = inputBitUtility.getNextUUID();
        Location relativeLocation = new Location(inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), worldUUID);
        // This location will be updated on Spawn\
        this.location = relativeLocation;
        this.direction = Direction.values()[inputBitUtility.getNextCorrectedIntBit(3)];
        int totalEffects = inputBitUtility.getNextCorrectIntByte();
        this.effects = new ArrayList<>(totalEffects);
        for(int i = 0; i < totalEffects; i++) {
            EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
            EntityEffect effect = (EntityEffect) effectType.getEffectFromInput(inputBitUtility, worldGameState);
            effect.applyEntityEffect(this);
            effects.add(effect);
        }
    }

    /**
     * Writes file to IO and Packet
     * @param outputBitUtility
     * @throws IOException
     */
    public void writeData(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextCorrectByteInt(entityType.ordinal());
        outputBitUtility.writeNextUUID(uuid);

        Region currentRegion = worldGameState.getRegionAtLocation(location);
        Location internalOffset = currentRegion.getLocation().getLocationDifference(getLocation());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getX());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getY());
        outputBitUtility.writeNextCorrectByteInt(internalOffset.getZ());

        outputBitUtility.writeNextCorrectedBitsInt(direction.ordinal(),3);

        outputBitUtility.writeNextCorrectByteInt(effects.size());
        for(int i = 0; i < effects.size(); i++) {
            effects.get(i).writeData(outputBitUtility);
        }

    }

    public abstract void onSpawn(SpawnReason spawnReason);

    public abstract void onDespawn(DespawnReason despawnReason);

    public abstract void tick();


    public void setDirection(Direction newDirection) {
        Direction oldDirection = direction;
        this.direction = newDirection;
        if(worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState) worldGameState).replicateChangeDirectionEntity(uuid,getLocation().getWorldUUID(),oldDirection,newDirection);
        }
    }

    public void applyEffect(EntityEffect effect) {
        effect.applyEntityEffect(this);
        effects.add(effect);
        if(effect.isReplicatedEffect() && worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState)worldGameState).replicateEntityEffectAdded(this, effect);
        }
    }

    public void removeEffect(EntityEffect effect) {
        if(effect.isReplicatedEffect() && worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState)worldGameState).replicateEntityEffectRemoved(this, effect);
        }
        effect.removeEntityEffect();
        effects.remove(effect);
    }




    public UUID getUUID() {
        return uuid;
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

    public ArrayList<EntityEffect> getEffects() {
        return effects;
    }

    @Override
    public String toString() {
        return "e@" + entityType + "," + uuid + "," + location + "," + direction;
    }
}
