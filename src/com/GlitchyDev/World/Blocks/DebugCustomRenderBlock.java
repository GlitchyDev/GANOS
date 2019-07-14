package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.Game.Player;
import com.GlitchyDev.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.Block;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomRenderBlock;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomVisibleBlock;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class DebugCustomRenderBlock extends Block implements CustomRenderBlock, CustomVisibleBlock, TickableBlock {
    private final GameItem mesh;
    private final AirBlock replacementBlock;

    public DebugCustomRenderBlock(WorldGameState worldGameState, Location location, UUID regionUUID) {
        super(BlockType.DEBUG_CUSTOM_RENDER, worldGameState, location, regionUUID);
        mesh = new GameItem(AssetLoader.getMeshAsset("CubicMesh1").clone());
        mesh.getMesh().setTexture(AssetLoader.getTextureAsset("grassblock"));
        replacementBlock = new AirBlock(worldGameState,location, regionUUID);
    }

    public DebugCustomRenderBlock(WorldGameState worldGameState, InputBitUtility inputBitUtility, UUID regionUUID) throws IOException {
        super(BlockType.DEBUG_CUSTOM_RENDER, worldGameState, inputBitUtility, regionUUID);
        mesh = new GameItem(AssetLoader.getMeshAsset("CubicMesh1").clone());
        mesh.getMesh().setTexture(AssetLoader.getTextureAsset("grassblock"));
        replacementBlock = new AirBlock(worldGameState, location, regionUUID);
    }

    @Override
    public Block getCopy() {
        return new DebugCustomRenderBlock(worldGameState, getLocation().clone(), regionUUID);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof DebugCustomRenderBlock;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBlockType());
    }

    @Override
    public void renderCustomBlock(Renderer renderer, Camera camera) {
        renderer.render3DElement(camera, mesh, "Default3D");
    }

    @Override
    public Block getVisibleBlock(Player player) {
        return isVisible ? this : replacementBlock;
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        replacementBlock.setLocation(location);
    }

    @Override
    public void setRegionUUID(UUID regionUUID) {
        super.setRegionUUID(regionUUID);
        replacementBlock.setRegionUUID(regionUUID);
    }

    private int tickCount = 0;
    private boolean isVisible = true;
    @Override
    public void tick() {
        if(tickCount % 120 == 0) {
            ((ServerWorldGameState)worldGameState).updateBlockVisibility(this);
            isVisible = !isVisible;
        }
        tickCount++;
    }
}
