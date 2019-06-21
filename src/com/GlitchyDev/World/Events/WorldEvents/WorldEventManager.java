package com.GlitchyDev.World.Events.WorldEvents;

import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Events.WorldEvents.Block.BlockChangeListener;
import com.GlitchyDev.World.Events.WorldEvents.Entity.EntityDespawnListener;
import com.GlitchyDev.World.Events.WorldEvents.Entity.EntityDirectionListener;
import com.GlitchyDev.World.Events.WorldEvents.Entity.EntityMoveListener;
import com.GlitchyDev.World.Events.WorldEvents.Entity.EntitySpawnListener;
import com.GlitchyDev.World.Location;

import java.util.ArrayList;
import java.util.UUID;

public class WorldEventManager {
    private final ArrayList<EntitySpawnListener> entitySpawnListeners;
    private final ArrayList<EntityDespawnListener> entityDespawnListeners;
    private final ArrayList<EntityMoveListener> entityMoveListeners;
    private final ArrayList<EntityDirectionListener> entityDirectionListeners;
    private final ArrayList<BlockChangeListener> blockChangeListeners;

    public WorldEventManager() {
        entitySpawnListeners = new ArrayList<>();
        entityDespawnListeners = new ArrayList<>();
        entityMoveListeners = new ArrayList<>();
        entityDirectionListeners = new ArrayList<>();
        blockChangeListeners = new ArrayList<>();
    }

    public void triggerEntitySpawn(Entity entity, SpawnReason spawnReason) {
        for(EntitySpawnListener entitySpawnListener: entitySpawnListeners) {
            entitySpawnListener.onTriggerEntitySpawn(entity,spawnReason);
        }
    }

    public void triggerEntityDespawn(Entity entity, DespawnReason despawnReason) {
        for(EntityDespawnListener entityDespawnListener: entityDespawnListeners) {
            entityDespawnListener.onTriggerEntityDespawn(entity,despawnReason);
        }
    }

    public void triggerEntityMove(Entity entity, Location oldLocation, Location newLocation, UUID oldRegion, UUID newRegion) {
        for(EntityMoveListener entityMoveListener: entityMoveListeners) {
            entityMoveListener.onTriggerEntityMovement(entity,oldLocation,newLocation,oldRegion,newRegion);
        }
    }

    public void triggerEntityDirection(Entity entity, Direction oldDirection, Direction newDirection) {
        for(EntityDirectionListener entityDirectionListener: entityDirectionListeners) {
            entityDirectionListener.onTriggerEntityDirection(entity,oldDirection,newDirection);
        }
    }

    public void triggerBlockChange(Block previousBlock, Block newBlock, UUID worldUUID, UUID regionUUID) {
        for(BlockChangeListener blockChangeListener: blockChangeListeners) {
            blockChangeListener.onTriggerBlockChange(previousBlock,newBlock,worldUUID,regionUUID);
        }
    }











    public ArrayList<EntitySpawnListener> getEntitySpawnListeners() {
        return entitySpawnListeners;
    }

    public ArrayList<EntityDespawnListener> getEntityDespawnListeners() {
        return entityDespawnListeners;
    }

    public ArrayList<EntityMoveListener> getEntityMoveListeners() {
        return entityMoveListeners;
    }

    public ArrayList<EntityDirectionListener> getEntityDirectionListeners() {
        return entityDirectionListeners;
    }
}
