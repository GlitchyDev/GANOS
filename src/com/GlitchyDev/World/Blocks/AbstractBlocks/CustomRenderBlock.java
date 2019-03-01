package com.GlitchyDev.World.Blocks.AbstractBlocks;

import com.GlitchyDev.Game.Player.Player;
import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Renderer;
import com.GlitchyDev.Utility.GameWindow;

public interface CustomRenderBlock {
    void render(Renderer renderer, GameWindow gameWindow, Camera camera, Player player);

}
