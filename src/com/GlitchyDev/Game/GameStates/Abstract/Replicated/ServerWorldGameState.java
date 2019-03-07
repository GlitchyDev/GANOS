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
import com.GlitchyDev.Networking.Packets.Server.World.ServerSpawnWorldPacket;
import com.GlitchyDev.Networking.ServerNetworkManager;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomVisableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.DebugPlayerEntityBase;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.World;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class ServerWorldGameState extends WorldGameState {
    protected final ServerNetworkManager serverNetworkManager;
    protected final HashMap<UUID,Player> currentPlayers;

    public ServerWorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType, int assignedPort) {
        super(globalGameDataBase, gameStateType);
        serverNetworkManager = new ServerNetworkManager(this, assignedPort);
        currentPlayers = new HashMap<>();
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
        try {
            serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerSpawnWorldPacket(world.getWorldUUID()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentPlayers.put(playerUUID,player);
        playerEntity.recalculateView();
    }

    public void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason) {
        currentPlayers.remove(playerUUID);
    }



    private void replicateChanges() {
        // Set blocks
        // Chance Directions

        // Spawn Entities global removal
        // Despawn Entities global Removal
        // Move entities, spawn respawn and move








    }


    /**
    Linking and unlinking regions shouldn't happen, Worlds should be relatively static
    private HashSet<Region> recalculateRegionEntityList = new HashSet<>();
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
        EntityBase entity = getEntity(entityUUID,worldUUID);
        super.despawnEntity(entityUUID, worldUUID);
        UUID regionUUID = getRegionAtLocation(entity.getLocation()).getRegionUUID();
        if(!despawnedEntities.containsKey(regionUUID)) {
            despawnedEntities.put(regionUUID, new ArrayList<>());
        }
        despawnedEntities.get(regionUUID).add(new ServerDespawnEntityPacket(entityUUID, worldUUID));
    }



    // Entities who move between regions
    private HashMap<UUID, ArrayList<ServerMoveEntityPacket>> movedEntitiesBoth = new HashMap<>();
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
        if(!movedEntitiesOld.containsKey(previousRegion)) {
            movedEntitiesOld.put(previousRegion, new ArrayList<>());
        }
        if(!movedEntitiesNew.containsKey(newRegion)) {
            movedEntitiesNew.put(newRegion, new ArrayList<>());
        }

        movedEntitiesBoth.get(newRegion).add(new ServerMoveEntityPacket(entityUUID, newLocation));
        movedEntitiesOld.get(previousRegion).add(new ServerDespawnEntityPacket(entityUUID, oldLocation.getWorldUUID()));
        EntityBase entity = getEntity(entityUUID, newLocation.getWorldUUID());
        movedEntitiesNew.get(newRegion).add(new ServerSpawnEntityPacket(entity));
    }


    // Replicate to players with this in its view
    private HashMap<UUID, ArrayList<ServerChangeDirectionEntityPacket>> changedDirections = new HashMap<>();
    public void replicateChangeDirectionEntity(UUID entityUUID, UUID worldUUID, Direction direction) {
        // This is replicated, mark for entities who can view its region
        UUID regionUUID = getRegionAtLocation(getEntity(entityUUID, worldUUID).getLocation()).getRegionUUID();
        if(!changedDirections.containsKey(regionUUID)) {
            changedDirections.put(regionUUID,new ArrayList<>());
        }
        changedDirections.get(regionUUID).add(new ServerChangeDirectionEntityPacket(entityUUID, worldUUID, direction));
    }




    // Replicate to players with visible region, and check to make sure its not a custom visibility block
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

    // Check to make sure the correct "Region" is sent, visibility and all
    public void playerAddRegionToView(UUID playerUUID, Region region) throws IOException {
        if(currentPlayers.containsKey(playerUUID)) {
            Region regionCopy = region.createCopy();
            for (BlockBase blockBase : region.getBlocksArray()) {
                if (blockBase instanceof CustomVisableBlock) {
                    Location relativeLocation = region.getLocation().getLocationDifference(blockBase.getLocation());
                    regionCopy.setBlockRelative(relativeLocation, ((CustomVisableBlock) blockBase).getVisibleBlock(currentPlayers.get(playerUUID)));
                }

            }
            serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerSpawnRegionPacket(regionCopy));
        }
    }

    public void playerRemoveRegionFromView(UUID playerUUID, UUID regionUUID, UUID worldUUID) throws IOException {
        if(currentPlayers.containsKey(playerUUID)) {
            serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerDespawnRegionPacket(regionUUID, worldUUID));
        }
    }


}
