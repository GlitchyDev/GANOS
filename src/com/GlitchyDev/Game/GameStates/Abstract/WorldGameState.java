package com.GlitchyDev.Game.GameStates.Abstract;

import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.World;

import java.util.HashMap;

public abstract class WorldGameState extends EnvironmentGameState {
    private HashMap<Integer, World> currentWorlds;

    public WorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
        currentWorlds = new HashMap<>();
    }

    @Override
    public void logic() {
        super.doLogic();
        for(World world: currentWorlds.values()) {
            world.tick();
        }
    }

    public void addWorld(World world) {
        currentWorlds.put(world.getWorldID(),world);  
    }

    public void removeWorld(int id) {
        currentWorlds.remove(id);
    }

    public boolean hasWorld(int id) {
        return currentWorlds.containsKey(id);
    }

    public World getWorld(int id) {
        return currentWorlds.get(id);
    }

    public void spawnRegion(RegionBase regionBase) {

    }

    public void despawnRegion(RegionBase)
}
