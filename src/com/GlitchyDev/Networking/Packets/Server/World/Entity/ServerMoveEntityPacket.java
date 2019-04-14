package com.GlitchyDev.Networking.Packets.Server.World.Entity;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public class ServerMoveEntityPacket extends WorldStateModifyingPackets {
    private final UUID entityUUID;
    private final UUID worldUUID;
    private final Location newLocation;

    public ServerMoveEntityPacket(UUID entityUUID, Location newLocation) {
        super(PacketType.SERVER_MOVE_ENTITY);
        this.entityUUID = entityUUID;
        this.worldUUID = newLocation.getWorldUUID();
        this.newLocation = newLocation;
    }

    public ServerMoveEntityPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_MOVE_ENTITY, inputBitUtility, worldGameState);
        this.entityUUID = inputBitUtility.getNextUUID();
        this.worldUUID = inputBitUtility.getNextUUID();
        this.newLocation = new Location(inputBitUtility.getNextInteger(),inputBitUtility.getNextInteger(),inputBitUtility.getNextInteger(),worldUUID);
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        EntityBase entity = worldGameState.getEntity(entityUUID,worldUUID);
        System.out.println("Moving " + entity + " " + newLocation);
        if(entity == null) {
            System.out.println("ERROR: NONEXISTANT ENTITY MOVED");
        } else {
            entity.setLocation(newLocation);
        }
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(entityUUID);
        outputBitUtility.writeNextUUID(worldUUID);
        outputBitUtility.writeNextInteger(newLocation.getX());
        outputBitUtility.writeNextInteger(newLocation.getY());
        outputBitUtility.writeNextInteger(newLocation.getZ());
    }

    @Override
    public String toString() {
        return super.toString() + "," + entityUUID + "," + worldUUID + "," + newLocation;
    }
}
