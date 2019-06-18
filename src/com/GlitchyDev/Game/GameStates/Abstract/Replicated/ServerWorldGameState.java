package com.GlitchyDev.Game.GameStates.Abstract.Replicated;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Game.GlobalGameData;
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
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomVisableBlock;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.CustomVisibleEntity;
import com.GlitchyDev.World.Entities.AbstractEntities.Entity;
import com.GlitchyDev.World.Entities.Enums.DespawnReason;
import com.GlitchyDev.World.Entities.Enums.SpawnReason;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.Region;
import com.GlitchyDev.World.Transmission.Communication.CommunicationManager;
import com.GlitchyDev.World.Transmission.Network.CommunicationNetworkManager;
import com.GlitchyDev.World.Views.EntityView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public abstract class ServerWorldGameState extends WorldGameState {
    protected final ServerNetworkManager serverNetworkManager;
    protected final HashMap<UUID,Player> currentPlayers;
    private final CommunicationNetworkManager communicationNetworkManager;
    private final CommunicationManager communicationManager;

    public ServerWorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType, int assignedPort) {
        super(globalGameDataBase, gameStateType);
        serverNetworkManager = new ServerNetworkManager(this, assignedPort);
        currentPlayers = new HashMap<>();

        this.communicationNetworkManager = new CommunicationNetworkManager();
        this.communicationManager = new CommunicationManager();
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

        try {
            replicateChanges();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replicate


    }

    public abstract void processPacket(UUID uuid, PacketBase packet);

    public abstract void onPlayerLogin(UUID playerUUID);

    public abstract void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason);


    /**
     * Replicates all changes of perception to all players
     * @throws IOException
     */
    private void replicateChanges() throws IOException {
        synchronized (currentPlayers) {
            for (Player player : currentPlayers.values()) {
                EntityView playerView = player.getEntityView();


                for (Entity entity : despawnedEntities.keySet()) {
                    if(entity != null) {
                        if (player.getEntityView().containsRegion(entity.getCurrentRegionUUID())) {
                            if (player.getEntityView().containsEntity(entity.getUUID())) {
                                player.getEntityView().clearEntity(entity.getUUID());
                                serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(despawnedEntities.get(entity));
                            }
                        }
                    }
                }

                for (Entity entity : spawnedEntities.keySet()) {
                    if (player.getEntityView().containsRegion(entity.getCurrentRegionUUID()) && (!(entity instanceof CustomVisibleEntity) || ((CustomVisibleEntity) entity).doSeeEntity(player.getPlayerEntity()))) {
                        serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(spawnedEntities.get(entity));
                        player.getEntityView().getRegion(entity.getCurrentRegionUUID()).getEntities().add(entity);
                    }
                }

                for (UUID regionUUID : changedBlocks.keySet()) {
                    if (playerView.containsRegion(regionUUID)) {
                        for (PacketBase packet : changedBlocks.get(regionUUID)) {
                            serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(packet);
                            Block block = ((ServerChangeBlockPacket) packet).getChangedBlock();
                            Location relativeLocation = block.getLocation().getLocationDifference(player.getEntityView().getRegion(regionUUID).getLocation());
                            player.getEntityView().getRegion(regionUUID).setBlockRelative(relativeLocation, block);

                        }
                    }
                }


                for (Block block : updatedBlockVisibility) {
                    UUID regionUUID = getRegionAtLocation(block.getLocation()).getRegionUUID();
                    if (playerView.containsRegion(regionUUID)) {
                        if (block instanceof CustomVisableBlock) {
                            Block viewedBlock = ((CustomVisableBlock) block).getVisibleBlock(player);
                            Location regionLocation = player.getEntityView().getRegion(regionUUID).getLocation();
                            Location blockRelative = regionLocation.getLocationDifference(block.getLocation());
                            if (!player.getEntityView().getRegion(regionUUID).getBlockRelative(blockRelative).equals(viewedBlock)) {
                                serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(new ServerChangeBlockPacket(viewedBlock));
                                player.getEntityView().getRegion(regionUUID).setBlockRelative(blockRelative, viewedBlock);
                            }
                        }
                    }
                }

                for (Entity entity : updatedEntityVisibility) {
                    UUID regionUUID = entity.getCurrentRegionUUID();
                    if (playerView.containsRegion(regionUUID)) {
                        if (entity instanceof CustomVisibleEntity) {
                            boolean entityCurrentlyInView = playerView.containsEntity(entity.getUUID());
                            boolean isCurrentlyVisible = ((CustomVisibleEntity) entity).doSeeEntity(player.getPlayerEntity());

                            if (entityCurrentlyInView != isCurrentlyVisible) {
                                if (isCurrentlyVisible) {
                                    serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(new ServerSpawnEntityPacket(entity));
                                    playerView.getRegion(regionUUID).getEntities().add(entity);
                                } else {
                                    serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(new ServerDespawnEntityPacket(entity.getUUID(), entity.getWorldUUID()));
                                    playerView.getRegion(regionUUID).getEntities().remove(entity);
                                }
                            }
                        }
                    }
                }

                for (Entity entity : entityRegionMovement.keySet()) {
                    UUID[] newAndOldRegions = entityRegionMovement.get(entity);
                    boolean containsNew = playerView.containsRegion(newAndOldRegions[0]);
                    boolean containsOld = playerView.containsRegion(newAndOldRegions[1]);

                    if (containsNew) {
                        if (playerView.containsEntity(entity.getUUID())) {
                            if (!(entity instanceof CustomVisibleEntity) || ((CustomVisibleEntity) entity).doSeeEntity(player.getPlayerEntity())) {
                                serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(movedEntitiesBoth.get(entity));
                            }
                        } else {
                            if (!(entity instanceof CustomVisibleEntity) || ((CustomVisibleEntity) entity).doSeeEntity(player.getPlayerEntity())) {
                                serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(movedEntitiesNew.get(entity));
                                playerView.getRegion(newAndOldRegions[0]).getEntities().add(entity);
                            }
                        }
                    } else {
                        if ((playerView.containsEntity(entity.getUUID()))) {
                            serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(movedEntitiesOld.get(entity));
                            playerView.clearEntity(entity.getUUID());
                        }
                    }
                }


                for (Entity entity : changedDirections.keySet()) {
                    if (player.getEntityView().containsRegion(entity.getCurrentRegionUUID()) && player.getEntityView().getRegion(entity.getCurrentRegionUUID()).getEntities().contains(entity)) {
                        serverNetworkManager.getUsersGameSocket(player.getPlayerUUID()).sendPacket(changedDirections.get(entity));
                    }
                }
            }
        }

        spawnedEntities.clear();
        despawnedEntities.clear();
        entityRegionMovement.clear();
        movedEntitiesBoth.clear();
        movedEntitiesOld.clear();
        movedEntitiesNew.clear();
        changedDirections.clear();
        changedBlocks.clear();
        updatedBlockVisibility.clear();
        updatedEntityVisibility.clear();

        // Move entities, spawn respawn and move
        // Check EACH PLAYER for requisite information








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

    private ArrayList<Block> updatedBlockVisibility = new ArrayList<>();
    public void updateBlockVisibility(Block block) {
        updatedBlockVisibility.add(block);
    }

    /**
     * Must be done after movement, or will potentially trigger along the wrong region lines+
     */
    private ArrayList<Entity> updatedEntityVisibility = new ArrayList<>();
    public void updateEntityViability(Entity entity) {
        updatedEntityVisibility.add(entity);
    }


    // Spawning entities triggers an viewcheck on EVERY Player in the world, the Server View should already show an update
    private HashMap<Entity, ServerSpawnEntityPacket> spawnedEntities = new HashMap<>();
    @Override
    public void spawnEntity(Entity entity, SpawnReason spawnReason) {
        // This is replicated, mark for Entities who can view its region
        super.spawnEntity(entity, spawnReason);
        spawnedEntities.put(entity,new ServerSpawnEntityPacket(entity));
    }


    // Despawning entities triggers an viewcheck on EVERY Player in the world, the Server View should already show an update
    private HashMap<Entity, ServerDespawnEntityPacket> despawnedEntities = new HashMap<>();
    @Override
    public void despawnEntity(UUID entityUUID, UUID worldUUID, DespawnReason despawnReason) {
        Entity entity = getEntity(entityUUID,worldUUID);
        despawnedEntities.put(entity,new ServerDespawnEntityPacket(entityUUID, worldUUID));
        super.despawnEntity(entityUUID, worldUUID, despawnReason);
    }



    // Entities who move between regions
    private HashMap<Entity,UUID[]> entityRegionMovement = new HashMap<>();
    private HashMap<Entity, ServerMoveEntityPacket> movedEntitiesBoth = new HashMap<>();
    private HashMap<Entity, ServerDespawnEntityPacket> movedEntitiesOld = new HashMap<>();
    private HashMap<Entity, ServerSpawnEntityPacket> movedEntitiesNew = new HashMap<>();
    // Every Player in the world will get checked if they personally have access to t
    public void replicateMoveEntity(UUID entityUUID, Location oldLocation, Location newLocation) {
        // This is replicated, mark for entities who can view its region
        // Also mark if it enters and or exits Regions, update viewing

        Entity entity = getEntity(entityUUID, newLocation.getWorldUUID());
        UUID previousRegion = getRegionAtLocation(oldLocation).getRegionUUID();
        UUID newRegion = getRegionAtLocation(newLocation).getRegionUUID();



        entityRegionMovement.put(entity,new UUID[]{newRegion,previousRegion});
        movedEntitiesBoth.put(entity,new ServerMoveEntityPacket(entityUUID, newLocation));
        movedEntitiesOld.put(entity,new ServerDespawnEntityPacket(entityUUID, oldLocation.getWorldUUID()));
        movedEntitiesNew.put(entity,new ServerSpawnEntityPacket(entity));
    }


    // Replicate to players with this in its view
    private HashMap<Entity, ServerChangeDirectionEntityPacket> changedDirections = new HashMap<>();
    public void replicateChangeDirectionEntity(UUID entityUUID, UUID worldUUID, Direction direction) {
        // This is replicated, mark for entities who can view its region
        Entity entity = getEntity(entityUUID, worldUUID);
        changedDirections.put(entity,new ServerChangeDirectionEntityPacket(entityUUID, worldUUID, direction));
    }




    // Replicate to players with visible region, and check to make sure its not a custom visibility block
    private HashMap<UUID, ArrayList<ServerChangeBlockPacket>> changedBlocks = new HashMap<>();
    @Override
    public void setBlock(Block block) {
        super.setBlock(block);
        // This is replicated, mark for entities who can view it
        UUID regionUUID = getRegionAtLocation(block.getLocation()).getRegionUUID();
        if(!changedBlocks.containsKey(regionUUID)) {
            changedBlocks.put(regionUUID, new ArrayList<>());
        }
        changedBlocks.get(regionUUID).add(new ServerChangeBlockPacket(block));
    }

    @Override
    public void setBlock(Block block, UUID regionUUID) {
        super.setBlock(block,regionUUID);
        // This is replicated, mark for entities who can view it
        if(!changedBlocks.containsKey(regionUUID)) {
            changedBlocks.put(regionUUID, new ArrayList<>());
        }
        changedBlocks.get(regionUUID).add(new ServerChangeBlockPacket(block));
    }

    // Check to make sure the correct "Region" is sent, visibility and all
    public void playerAddRegionToView(UUID playerUUID, Region region, EntityView entityView) throws IOException {
        if(currentPlayers.containsKey(playerUUID)) {
            Region regionCopy = region.createCopy();
            for (Block block : region.getBlocksArray()) {
                if (block instanceof CustomVisableBlock) {
                    Location relativeLocation = region.getLocation().getLocationDifference(block.getLocation());
                    regionCopy.setBlockRelative(relativeLocation, ((CustomVisableBlock) block).getVisibleBlock(currentPlayers.get(playerUUID)));
                }
            }

            for(Entity entity: region.getEntities()) {
                if(entity instanceof CustomVisibleEntity) {
                    if(!((CustomVisibleEntity) entity).doSeeEntity(currentPlayers.get(playerUUID).getPlayerEntity())) {
                        regionCopy.getEntities().remove(entity);
                    }
                }
            }
            serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerSpawnRegionPacket(regionCopy));
            if(serverNetworkManager.getUsersGameSocket(playerUUID) != null) {
                entityView.getViewableRegions().remove(region);
                entityView.getViewableRegions().add(regionCopy);
            }
        }
    }

    public void playerRemoveRegionFromView(UUID playerUUID, UUID regionUUID, UUID worldUUID) throws IOException {
        if(currentPlayers.containsKey(playerUUID)) {
            serverNetworkManager.getUsersGameSocket(playerUUID).sendPacket(new ServerDespawnRegionPacket(regionUUID, worldUUID));
        }
    }


    public CommunicationNetworkManager getCommunicationNetworkManager() {
        return communicationNetworkManager;
    }

    public CommunicationManager getCommunicationManager() {
        return communicationManager;
    }

}
