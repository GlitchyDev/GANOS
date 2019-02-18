package com.GlitchyDev.Networking.Packets.Server.World.Entity;

import com.GlitchyDev.Networking.Packets.AbstractPackets.PacketBase;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Entities.AbstractEntities.EntityBase;
import com.GlitchyDev.World.Entities.Enums.EntityType;

import java.io.IOException;

public class ServerSpawnEntityPacket extends PacketBase {
    private final EntityBase entityBase;

    public ServerSpawnEntityPacket(PacketType packetType, EntityBase entityBase) {
        super(packetType);
        this.entityBase = entityBase;
    }

    public ServerSpawnEntityPacket(InputBitUtility inputBitUtility) throws IOException {
        super(inputBitUtility);
        EntityType entityType = EntityType.values()[inputBitUtility.getNextCorrectIntByte()];
        this.entityBase = entityType.getEntityFromInput(inputBitUtility);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        entityBase.writeData(outputBitUtility);
    }
}
