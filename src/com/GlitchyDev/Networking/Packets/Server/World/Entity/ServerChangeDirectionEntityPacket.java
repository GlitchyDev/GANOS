package com.GlitchyDev.Networking.Packets.Server.World.Entity;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Direction;

import java.io.IOException;
import java.util.UUID;

public class ServerChangeDirectionEntityPacket extends WorldStateModifyingPackets {
    private final UUID entityUUID;
    private final UUID worldUUID;
    private final Direction newDirection;

    public ServerChangeDirectionEntityPacket(UUID entityUUID, UUID worldUUID, Direction newDirection) {
        super(PacketType.SERVER_MOVE_ENTITY);
        this.entityUUID = entityUUID;
        this.worldUUID = worldUUID;
        this.newDirection = newDirection;
    }

    public ServerChangeDirectionEntityPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_MOVE_ENTITY, inputBitUtility, worldGameState);
        this.entityUUID = inputBitUtility.getNextUUID();
        this.worldUUID = inputBitUtility.getNextUUID();
        this.newDirection = Direction.getCompleteCardinal()[inputBitUtility.getNextCorrectedIntBit(3)];
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.getEntity(entityUUID,worldUUID).setDirection(newDirection);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        outputBitUtility.writeNextUUID(entityUUID);
        outputBitUtility.writeNextUUID(worldUUID);
        outputBitUtility.writeNextCorrectedBitsInt(newDirection.ordinal(),3);
    }

    @Override
    public String toString() {
        return super.toString() + "," + entityUUID + "," + worldUUID + "," + newDirection;
    }
}
