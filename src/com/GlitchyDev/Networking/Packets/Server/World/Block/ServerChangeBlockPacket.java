package com.GlitchyDev.Networking.Packets.Server.World.Block;

import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public class ServerChangeBlockPacket extends WorldStateModifyingPackets {
    private final Block changedBlock;

    public ServerChangeBlockPacket(Block changedBlock) {
        super(PacketType.SERVER_CHANGE_BLOCK);
        this.changedBlock = changedBlock;
    }

    public ServerChangeBlockPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_CHANGE_BLOCK, inputBitUtility, worldGameState);
        BlockType blockType = BlockType.values()[inputBitUtility.getNextCorrectIntByte()];
        changedBlock = blockType.getBlockFromInput(worldGameState, inputBitUtility);
        UUID worldUUID = inputBitUtility.getNextUUID();
        Location blockLocation = new Location(inputBitUtility.getNextInteger(),inputBitUtility.getNextInteger(),inputBitUtility.getNextInteger(),worldUUID);
        changedBlock.setLocation(blockLocation);
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        worldGameState.setBlock(changedBlock);
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        changedBlock.writeData(outputBitUtility);
        outputBitUtility.writeNextUUID(changedBlock.getLocation().getWorldUUID());
        outputBitUtility.writeNextInteger(changedBlock.getLocation().getX());
        outputBitUtility.writeNextInteger(changedBlock.getLocation().getY());
        outputBitUtility.writeNextInteger(changedBlock.getLocation().getZ());
    }

    public Block getChangedBlock() {
        return changedBlock;
    }
}
