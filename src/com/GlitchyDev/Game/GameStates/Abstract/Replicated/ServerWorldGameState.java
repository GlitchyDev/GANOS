package com.GlitchyDev.Game.GameStates.Abstract.Replicated;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.GameStates.GameStateType;
import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.General.Authentication.NetworkDisconnectType;
import com.GlitchyDev.Networking.ServerNetworkManager;
import com.GlitchyDev.Utility.GlobalGameData;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Direction;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;
import com.GlitchyDev.World.Region.RegionBase;

import java.util.ArrayList;
import java.util.UUID;

public abstract class ServerWorldGameState extends WorldGameState {
    protected final ServerNetworkManager serverNetworkManager;

    public ServerWorldGameState(GlobalGameData globalGameDataBase, GameStateType gameStateType) {
        super(globalGameDataBase, gameStateType);
        serverNetworkManager = new ServerNetworkManager(this, 5000);
    }

    @Override
    public void logic() {
        for(UUID worldUUID: getWorlds()) {
            getWorld(worldUUID).tick();
        }

        for(UUID playerUUID: serverNetworkManager.getConnectedUsers()) {
            ArrayList<PacketBase> packetsFromUser = serverNetworkManager.getUsersGameSocket(playerUUID).getUnprocessedPackets();
            for(PacketBase packet: packetsFromUser) {
                switch(packet.getPacketType()) {
                    case CLIENT_SEND_INPUT_PACKET:

                        break;
                }
            }
        }

        for(UUID uuid: serverNetworkManager.getConnectedUsers()) {
            for(PacketBase packet: serverNetworkManager.getUsersGameSocket(uuid).getUnprocessedPackets()) {
                processPacket(uuid, packet);
            }
        }


    }

    public abstract void processPacket(UUID uuid, PacketBase packet);

    public abstract void onPlayerLogin(UUID playerUUID);

    public abstract void onPlayerLogout(UUID playerUUID, NetworkDisconnectType reason);


    @Override
    public synchronized void spawnRegion(RegionBase regionBase, UUID worldUUID) {
        super.spawnRegion(regionBase, worldUUID);
    }

    @Override
    public synchronized void despawnRegion(UUID regionUUID, UUID worldUUID) {
        super.despawnRegion(regionUUID, worldUUID);
    }


    @Override
    public synchronized void spawnEntity(EntityBase entity) {
        super.spawnEntity(entity);
    }

    @Override
    public synchronized void despawnEntity(UUID entityUUID, UUID worldUUID) {
        super.despawnEntity(entityUUID, worldUUID);
    }

    @Override
    public synchronized void moveEntity(UUID entityUUID, UUID worldUUID, Location oldLocation, Location newLocation) {
        super.moveEntity(entityUUID, worldUUID, oldLocation, newLocation);
    }

    @Override
    public synchronized void changeDirectionEntity(UUID entityUUID, UUID worldUUID, Direction direction) {
        super.changeDirectionEntity(entityUUID, worldUUID, direction);
    }

    @Override
    public synchronized void setBlock(BlockBase block) {
        super.setBlock(block);
    }
}
