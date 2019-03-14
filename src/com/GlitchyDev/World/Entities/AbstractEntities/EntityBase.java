package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.GameWindow;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TriggerableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.Effects.Abstract.EffectBase;
import com.GlitchyDev.World.Entities.Effects.Enums.EffectType;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.EntityMovementType;
import com.GlitchyDev.World.Entities.Enums.EntityType;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public abstract class EntityBase {
    // Utill
    protected WorldGameState worldGameState;
    private UUID currentRegionUUID;
    // Saved to file
    private final EntityType entityType;
    private final UUID uuid;
    private Location location;
    private Direction direction;
    private final ArrayList<EffectBase> effects;


    /**
     * Creates Entities naturally
     * @param worldGameState
     * @param currentRegionUUID
     * @param entityType
     * @param location
     * @param direction
     */
    public EntityBase(WorldGameState worldGameState, UUID currentRegionUUID, EntityType entityType, Location location, Direction direction) {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;
        this.entityType = entityType;
        this.uuid = UUID.randomUUID();
        this.location = location;
        this.direction = direction;
        this.effects = new ArrayList<>();
    }

    /**
     * Allows Entities to be constructed from IO/Packets
     * @param worldGameState
     * @param worldUUID
     * @param currentRegionUUID
     * @param inputBitUtility
     * @param entityType
     * @throws IOException
     */

    // This particular constructor is used to retrieve a single entity from file, or from a spawn packet
    public EntityBase(WorldGameState worldGameState, UUID worldUUID, UUID currentRegionUUID, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = currentRegionUUID;
        this.entityType = entityType;
        this.uuid = inputBitUtility.getNextUUID();
        Location relativeLocation = new Location(inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), worldUUID);
        // This location will be updated on Spawn
        this.location = relativeLocation;
        this.direction = Direction.values()[inputBitUtility.getNextCorrectedIntBit(3)];
        int totalEffects = inputBitUtility.getNextCorrectIntByte();
        this.effects = new ArrayList<>(totalEffects);
        for(int i = 0; i < totalEffects; i++) {
            EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
            EffectBase effect = effectType.getEffectFromInput(inputBitUtility, worldGameState, this);
            effects.add(effect);
        }
    }

    // Exclusively from reading from a Region File
    public EntityBase(WorldGameState worldGameState, UUID worldUUID, Region region, InputBitUtility inputBitUtility, EntityType entityType) throws IOException {
        this.worldGameState = worldGameState;
        this.currentRegionUUID = region.getRegionUUID();
        this.entityType = entityType;
        this.uuid = inputBitUtility.getNextUUID();
        Location regionLocation = region.getLocation();
        Location relativeLocation = new Location(inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), inputBitUtility.getNextCorrectIntByte(), worldUUID);
        this.location = regionLocation.getOffsetLocation(relativeLocation);
        this.direction = Direction.values()[inputBitUtility.getNextCorrectedIntBit(3)];
        int totalEffects = inputBitUtility.getNextCorrectIntByte();
        this.effects = new ArrayList<>(totalEffects);
        for(int i = 0; i < totalEffects; i++) {
            EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
            EffectBase effect = effectType.getEffectFromInput(inputBitUtility, worldGameState, this);
            effects.add(effect);
        }
    }



    public abstract void onSpawn(SpawnReason spawnReason);

    public abstract void tick();

    public abstract void render(Renderer renderer, GameWindow gameWindow, Camera camera);

    public abstract void onDespawn(DespawnReason despawnReason);

    /**
     *
     * @param newLocation
     * @param movementType
     * @return If successful
     */
    public boolean attemptMove(Location newLocation, EntityMovementType movementType) {
        BlockBase currentBlock = worldGameState.getBlockAtLocation(getLocation());
        if(currentBlock instanceof TriggerableBlock) {
            if(!((TriggerableBlock) currentBlock).attemptExitBlock(movementType,this)) {
                return false;
            }
        }
        BlockBase nextBlock = worldGameState.getBlockAtLocation(newLocation);
        if(nextBlock instanceof TriggerableBlock) {
            if(!((TriggerableBlock) nextBlock).attemptExitBlock(movementType,this)) {
                return false;
            }
        }
        move(newLocation, movementType);
        return true;
    }

    /**
     *
     * @param newLocation
     * @param movementType
     */
    public void move(Location newLocation, EntityMovementType movementType) {
        // Find our TRIGGER WARNINGS,
        BlockBase currentBlock = worldGameState.getBlockAtLocation(getLocation());
        if(worldGameState.isARegionAtLocation(newLocation)) {
            if (currentBlock instanceof TriggerableBlock) {
                ((TriggerableBlock) currentBlock).exitBlockSuccessfully(movementType, this);
            }
            BlockBase nextBlock = worldGameState.getBlockAtLocation(newLocation);
            if (nextBlock instanceof TriggerableBlock) {
                ((TriggerableBlock) newLocation).enterBlockSccessfully(movementType, this);
            }

            Location oldLocation = getLocation();
            worldGameState.getRegion(currentRegionUUID, getWorldUUID()).getEntities().remove(this);
            setLocation(newLocation);
            setCurrentRegionUUID(worldGameState.getRegionAtLocation(getLocation()).getRegionUUID());
            worldGameState.getRegion(currentRegionUUID, getWorldUUID()).getEntities().add(this);

            if (worldGameState instanceof ServerWorldGameState) {
                ((ServerWorldGameState) worldGameState).replicateMoveEntity(getUUID(), oldLocation, newLocation);
            }
        } else {
            System.out.println("EntityBase: No Valid region at " + newLocation);
        }


    }

    public void setDirection(Direction newDirection) {
        this.direction = newDirection;
        if(worldGameState instanceof ServerWorldGameState) {
            ((ServerWorldGameState) worldGameState).replicateChangeDirectionEntity(uuid,getLocation().getWorldUUID(),newDirection);
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
            effects.get(0).writeData(outputBitUtility);
        }

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

    // Replicate!
    public void setLocation(Location location) {
        this.location = location;
    }

    public Direction getDirection() {
        return direction;
    }


    public EntityType getEntityType() {
        return entityType;
    }

    public ArrayList<EffectBase> getEffects() {
        return effects;
    }


    @Override
    public String toString() {
        return "e@" + entityType + "," + uuid + "," + location + "," + direction;
    }
}
