package com.GlitchyDev.Game.GameStates.Abstract.Replicated;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Server.World.ServerSpawnWorldPacket;
import com.GlitchyDev.Networking.ServerNetworkManager;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.World;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public abstract class ServerWorldGameState extends WorldGameState {
    protected final ServerNetworkManager serverNetworkManager;

    public ServerWorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
        serverNetworkManager = new ServerNetworkManager(this, 5000);
    }

    @Override
    public void logic() {
        for(UUID playerUUID: serverNetworkManager.getConnectedUsers()) {
            ArrayList<PacketBase> packetsFromUser = serverNetworkManager.getUsersGameSocket(playerUUID).getUnprocessedPackets();
            for(PacketBase packet: packetsFromUser) {
                processPacket(playerUUID, packet);
            }
        }
        for(UUID worldUUID: getWorlds()) {
            getWorld(worldUUID).tick();
        }


        // Replicate


    }

    public abstract void processPacket(UUID uuid, PacketBase packet);

    public abstract void onPlayerLogin(UUID playerUUID);

    public abstract void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason);



    @Override
    public void addWorld(World world) {
        super.addWorld(world);
        // No replication needed, players won't start initially in a newly made world, must be manually moved
    }

    @Override
    public void removeWorld(UUID worldUUID) {
        super.removeWorld(worldUUID);
        // No replication needed. Players won't be in a world that is removed
    }


    @Override
    public void spawnRegion(RegionBase regionBase, UUID worldUUID) {
        super.spawnRegion(regionBase, worldUUID);
        // No replication needed, Players wouldn't be able to see it by default
    }

    @Override
    public void despawnRegion(UUID regionUUID, UUID worldUUID) {
        super.despawnRegion(regionUUID, worldUUID);
        // No replication needed, Regions should not be despawned during normal play
    }


    @Override
    public void spawnEntity(EntityBase entity) {
        // This is replicated, mark for Entities who can view its region
        super.spawnEntity(entity);
    }

    @Override
    public void despawnEntity(UUID entityUUID, UUID worldUUID) {
        // This is replicated, mark for entities who can view its region
        super.despawnEntity(entityUUID, worldUUID);
    }

    public void replicateMoveEntity(UUID entityUUID, UUID worldUUID, Location oldLocation, Location newLocation) {
        // This is replicated, mark for entities who can view its region
        // Also mark if it enters and or exits Regions, update viewing
    }

    public void replicateChangeDirectionEntity(UUID entityUUID, Direction direction) {
        // This is replicated, mark for entities who can view its region
    }

    @Override
    public void setBlock(BlockBase block) {
        // This is replicated, mark for entities who can view it
        super.setBlock(block);
    }


}
