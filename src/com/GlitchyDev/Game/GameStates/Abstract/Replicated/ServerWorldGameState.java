package com.GlitchyDev.Game.GameStates.Abstract.Replicated;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.Packets.Server.World.Block.ServerChangeBlockPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Entity.ServerChangeDirectionEntityPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Entity.ServerDespawnEntityPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Entity.ServerMoveEntityPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Entity.ServerSpawnEntityPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Region.ServerDespawnRegionPacket;
import com.GlitchyDev.Networking.Packets.Server.World.Region.ServerSpawnRegionPacket;
import com.GlitchyDev.Networking.ServerNetworkManager;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.AbstractEntities.ViewingEntityBase;
import com.GlitchyDev.World.Entities.DebugPlayerEntityBase;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;
import com.GlitchyDev.World.Region.RegionConnectionType;
import com.GlitchyDev.World.World;

import java.io.IOException;
import java.util.*;

public abstract class ServerWorldGameState extends WorldGameState {
    protected final ServerNetworkManager serverNetworkManager;
    protected final ArrayList<Player> currentPlayers;

    public ServerWorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType, int assignedPort) {
        super(globalGameDataBase, gameStateType);
        serverNetworkManager = new ServerNetworkManager(this, assignedPort);
        currentPlayers = new ArrayList<>();
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

    public void onPlayerLogin(UUID playerUUID) {
        World world = null;
        for(UUID worldUUID: getWorlds()) {
            world = getWorld(worldUUID);
        }
        Location spawnLocation = world.getOriginLocation();
        UUID regionUUID = getRegionAtLocation(spawnLocation).getRegionUUID();
        DebugPlayerEntityBase playerEntity = new DebugPlayerEntityBase(this,regionUUID,spawnLocation,Direction.NORTH);
        Player player = new Player(this,playerUUID,playerEntity);
        currentPlayers.add(player);
        // Get the Player Entity
        replicateChanges();

    }

    public void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason) {
        Player removedPlayer = null;
        for(Player player: currentPlayers) {
            if(player.getPlayerUUID() == playerUUID) {
                removedPlayer = player;
            }
        }
        currentPlayers.remove(removedPlayer);
    }



    private void replicateChanges() {
        // Recalculate View List
        // This fixes
        for(RegionBase region: recalculateRegionEntityList) {
            for(EntityBase entity: region.getEntities()) {
                if(entity instanceof ViewingEntityBase) {
                    ((ViewingEntityBase) entity).recalculateView();
                }
            }
        }






    }


    /*
    Linking and unlinking regions shouldn't happen, Worlds should be relatively static
    private HashSet<RegionBase> recalculateRegionEntityList = new HashSet<>();
    @Override
    public void linkRegions(UUID worldUUID, UUID hostRegion, UUID connectedRegion, RegionConnectionType connectionType) {
        super.linkRegions(worldUUID, hostRegion, connectedRegion, connectionType);
        recalculateRegionEntityList.add(getRegion(hostRegion,worldUUID));
    }

    @Override
    public void unlinkRegions(UUID worldUUID, UUID hostRegion, UUID connectedRegion, RegionConnectionType connectionType) {
        super.unlinkRegions(worldUUID, hostRegion, connectedRegion, connectionType);
        recalculateRegionEntityList.add(getRegion(hostRegion,worldUUID));
    }
    */

    // Spawning entities triggers an viewcheck on EVERY Player in the world, the Server View should already show an update
    private HashMap<UUID, ArrayList<ServerSpawnEntityPacket>> spawnedEntities = new HashMap<>();
    @Override
    public void spawnEntity(EntityBase entity) {
        // This is replicated, mark for Entities who can view its region
        super.spawnEntity(entity);
        if(!spawnedEntities.containsKey(getRegionAtLocation(entity.getLocation()).getRegionUUID())) {
            spawnedEntities.put(getRegionAtLocation(entity.getLocation()).getRegionUUID(), new ArrayList<>());
        }
        spawnedEntities.get(getRegionAtLocation(entity.getLocation()).getRegionUUID()).add(new ServerSpawnEntityPacket(entity));
    }


    // Despawning entities triggers an viewcheck on EVERY Player in the world, the Server View should already show an update
    private HashMap<UUID, ArrayList<ServerDespawnEntityPacket>> despawnedEntities = new HashMap<>();
    @Override
    public void despawnEntity(UUID entityUUID, UUID worldUUID) {
        // This is replicated, mark for entities who can view its region
        super.despawnEntity(entityUUID, worldUUID);
        UUID regionUUID = getRegionAtLocation(getEntity(entityUUID,worldUUID).getLocation()).getRegionUUID();
        if(!despawnedEntities.containsKey(regionUUID)) {
            despawnedEntities.put(regionUUID, new ArrayList<>());
        }
        despawnedEntities.get(regionUUID).add(new ServerDespawnEntityPacket(entityUUID, worldUUID));
    }

    // Since they only need to test if their view contains both points!!!!
    private ArrayList<ServerMoveEntityPacket> movedEntitiesBoth = new ArrayList<>();
    private HashMap<UUID, ArrayList<ServerDespawnEntityPacket>> movedEntitiesOld = new HashMap<>();
    private HashMap<UUID, ArrayList<ServerSpawnEntityPacket>> movedEntitiesNew = new HashMap<>();
    // Every Player in the world will get checked if they personally have access to t
    public void replicateMoveEntity(UUID entityUUID, Location oldLocation, Location newLocation) {
        // This is replicated, mark for entities who can view its region
        // Also mark if it enters and or exits Regions, update viewing

        UUID previousRegion = getRegionAtLocation(oldLocation).getRegionUUID();
        UUID newRegion = getRegionAtLocation(newLocation).getRegionUUID();

        if(!movedEntitiesBoth.containsKey(newRegion)) {
            movedEntitiesBoth.put(newRegion, new ArrayList<>());
        }
        if(!movedEntitiesOld.containsKey(newRegion)) {
            movedEntitiesOld.put(previousRegion, new ArrayList<>());
        }
        if(!movedEntitiesNew.containsKey(newRegion)) {
            movedEntitiesNew.put(previousRegion, new ArrayList<>());
        }

        movedEntitiesBoth.get(newRegion).add(new ServerMoveEntityPacket(entityUUID, newLocation));
        movedEntitiesOld.get(newRegion).add(new ServerDespawnEntityPacket(entityUUID, oldLocation.getWorldUUID()));
        EntityBase entity = getEntity(entityUUID, newLocation.getWorldUUID());
        movedEntitiesNew.get(newRegion).add(new ServerSpawnEntityPacket(entity));
    }


    // A result if both areas are contained       move
    // Area if the new location is only contained spawn
    // Area if the old Location is only contained despawn
    private HashMap<UUID, ArrayList<ServerChangeDirectionEntityPacket>> changedDirections = new HashMap<>();
    public void replicateChangeDirectionEntity(UUID entityUUID, UUID worldUUID, Direction direction) {
        // This is replicated, mark for entities who can view its region
        UUID regionUUID = getRegionAtLocation(getEntity(entityUUID, worldUUID).getLocation()).getRegionUUID();
        if(!changedDirections.containsKey(regionUUID)) {
            changedDirections.put(regionUUID,new ArrayList<>());
        }
        changedDirections.get(regionUUID).add(new ServerChangeDirectionEntityPacket(entityUUID, worldUUID, direction));
    }

    private HashMap<UUID, ArrayList<ServerChangeBlockPacket>> changedBlocks = new HashMap<>();

    @Override
    public void setBlock(BlockBase block) {
        super.setBlock(block);
        // This is replicated, mark for entities who can view it
        UUID regionUUID = getRegionAtLocation(block.getLocation()).getRegionUUID();
        if(!changedBlocks.containsKey(regionUUID)) {
            changedBlocks.put(regionUUID, new ArrayList<>());
        }
        changedBlocks.get(regionUUID).add(new ServerChangeBlockPacket(block));
    }

    public void addRegion(UUID playerUUID, RegionBase region) throws IOException {
        serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerSpawnRegionPacket(region));
    }

    public void removeRegion(UUID playerUUID, UUID regionUUID, UUID worldUUID) throws IOException {
        serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerDespawnRegionPacket(regionUUID, worldUUID));
    }


}
