package com.GlitchyDev.Networking.Packets.Server.World.Effect;

import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Networking.Packets.AbstractPackets.WorldStateModifyingPackets;
import com.GlitchyDev.Networking.Packets.Enums.PacketType;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.Utility.OutputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Effects.Abstract.BlockEffect;
import com.GlitchyDev.World.Effects.Abstract.Effect;
import com.GlitchyDev.World.Effects.Enums.EffectType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.UUID;

public class ServerRemoveBlockEffectPacket extends WorldStateModifyingPackets {
    private final BlockEffect blockEffect;
    private Location blockOffsetLocation;
    private final UUID regionUUID;


    public ServerRemoveBlockEffectPacket(BlockEffect blockEffect, Block block) {
        super(PacketType.SERVER_REMOVE_BLOCK_EFFECT );
        this.blockEffect = blockEffect;
        this.blockOffsetLocation = block.getRegionOffsetLocation();
        this.regionUUID = block.getRegionUUID();
    }

    public ServerRemoveBlockEffectPacket(InputBitUtility inputBitUtility, WorldGameState worldGameState) throws IOException {
        super(PacketType.SERVER_ADD_BLOCK_EFFECT, inputBitUtility, worldGameState);
        EffectType effectType = EffectType.values()[inputBitUtility.getNextCorrectIntByte()];
        this.blockEffect = (BlockEffect) effectType.getEffectFromInput(inputBitUtility,worldGameState);
        this.blockOffsetLocation = new Location(inputBitUtility.getNextInteger(),inputBitUtility.getNextInteger(),inputBitUtility.getNextInteger(),inputBitUtility.getNextUUID());
        this.regionUUID = inputBitUtility.getNextUUID();
    }

    @Override
    public void executeModification(WorldGameState worldGameState) {
        Block block = worldGameState.getRegion(regionUUID,blockOffsetLocation.getWorldUUID()).getBlockRelative(blockOffsetLocation);
        blockEffect.setBlock(block);
        BlockEffect foundEffect = null;
        for(Effect effect: block.getCurrentEffects()) {
            if(effect.equals(blockEffect)) {
                foundEffect = (BlockEffect) effect;
            }
        }
        foundEffect.removeBlockEffect();
    }

    @Override
    protected void transmitPacketBody(OutputBitUtility outputBitUtility) throws IOException {
        blockEffect.writeData(outputBitUtility);
        outputBitUtility.writeNextInteger(blockOffsetLocation.getX());
        outputBitUtility.writeNextInteger(blockOffsetLocation.getY());
        outputBitUtility.writeNextInteger(blockOffsetLocation.getZ());
        outputBitUtility.writeNextUUID(blockOffsetLocation.getWorldUUID());
        outputBitUtility.writeNextUUID(regionUUID);
    }
}
