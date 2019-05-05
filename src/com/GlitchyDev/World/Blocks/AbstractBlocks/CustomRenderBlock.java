package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Renderer;

public interface CustomRenderBlock {
    void render(Renderer renderer, Camera camera, Player player);

}
