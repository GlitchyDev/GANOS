package com.GlitchyDev.World.Blocks;

import com.GlitchyDev.Game.GameStates.Abstract.Replicated.ServerWorldGameState;
import com.GlitchyDev.Game.GameStates.Abstract.WorldGameState;
import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.AssetLoader;
import com.GlitchyDev.Utility.InputBitUtility;
import com.GlitchyDev.World.Blocks.AbstractBlocks.BlockBase;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomRenderBlock;
import com.GlitchyDev.World.Blocks.AbstractBlocks.CustomVisableBlock;
import com.GlitchyDev.World.Blocks.AbstractBlocks.TickableBlock;
import com.GlitchyDev.World.Blocks.Enums.BlockType;
import com.GlitchyDev.World.Location;

import java.io.IOException;
import java.util.Objects;

public class DebugCustomRenderBlock extends BlockBase implements CustomRenderBlock, CustomVisableBlock, TickableBlock {
    private final GameItem mesh;
    private final AirBlock replacementBlock;

    public DebugCustomRenderBlock(WorldGameState worldGameState, Location location) {
        super(worldGameState, BlockType.DEBUG_CUSTOM_RENDER, location);
        mesh = new GameItem(AssetLoader.getMeshAsset("CubicMesh1").clone());
        mesh.getMesh().setTexture(AssetLoader.getTextureAsset("grassblock"));
        replacementBlock = new AirBlock(worldGameState,location);
    }

    public DebugCustomRenderBlock(WorldGameState worldGameState, InputBitUtility inputBitUtility) throws IOException {
        super(worldGameState, BlockType.DEBUG_CUSTOM_RENDER, inputBitUtility);
        mesh = new GameItem(AssetLoader.getMeshAsset("CubicMesh1").clone());
        mesh.getMesh().setTexture(AssetLoader.getTextureAsset("grassblock"));
        replacementBlock = new AirBlock(worldGameState, (Location) null);
    }

    @Override
    public BlockBase getCopy() {
        return new DebugCustomRenderBlock(worldGameState, getLocation().clone());
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
    public void render(Renderer renderer, Camera camera, Player player) {
        renderer.render3DElement(camera, mesh, "Default3D");
    }

    @Override
    public BlockBase getVisibleBlock(Player player) {
        return Math.random() > 0.5 ? this : replacementBlock;
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        replacementBlock.setLocation(location);
    }

    int tickCount = 0;
    @Override
    public void tick() {
        if(tickCount % 120 == 0) {
            ((ServerWorldGameState)worldGameState).updateBlockVisibility(this);
        }
        tickCount++;
    }
}
