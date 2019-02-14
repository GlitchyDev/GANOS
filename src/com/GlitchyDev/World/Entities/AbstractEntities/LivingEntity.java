package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.EntityType;
import com.GlitchyDev.World.Location;
import jdk.internal.util.xml.impl.Input;

import java.io.IOException;

public abstract class LivingEntity extends EntityBase {
    private boolean isAlive = true;
    private int health = 1;
    private final long birthTime;
    private long lastTickTime = -1;
    private long timeExistance = 0;
    private long timeAlive = 0;
    private long timeDead = 0;


    public LivingEntity(EntityType entityType, Location location, Direction direction) {
        super(entityType, location, direction);
        this.birthTime = System.currentTimeMillis();
    }

    public LivingEntity(EntityType entityType, InputBitUtility inputBitUtility) throws IOException {
        super(entityType, inputBitUtility);
        // Grab Birth time
        this.birthTime = System.currentTimeMillis();
    }




    @Override
    public void tick() {
        if(lastTickTime == -1L) {
            lastTickTime = System.currentTimeMillis();
        }
        long tickDifference = System.currentTimeMillis() - lastTickTime;

        timeExistance += tickDifference;
        if(isAlive) {
            tickAlive();
            timeAlive += tickDifference;
        } else {
            tickDead();
            timeDead += tickDifference;
        }
    }


    /**
     * Will Tick while Entity is Alive
     */
    public abstract void tickAlive();

    /**
     * Will Tick while Entity is Dead
     */
    public abstract void tickDead();

    /**
     * Called on Entity Spawn/Birth
     */
    public abstract void birthed();

    /**
     * Called on Entity Death
     */
    public abstract void killed();

    /**
     * Called on Entity Revival
     */
    public abstract void revived();



    // Helper Methods

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
        if(isAlive) {
            timeAlive = 0;
        } else {
            timeDead = 0;
        }

    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public long getBirthTime() {
        return birthTime;
    }

    public long getTimeAlive() {
        return timeAlive;
    }

    public long getTimeDead() {
        return timeDead;
    }

    public long getLastTickTime() {
        return lastTickTime;
    }

    public long getTimeExistance() {
        return timeExistance;
    }
}
