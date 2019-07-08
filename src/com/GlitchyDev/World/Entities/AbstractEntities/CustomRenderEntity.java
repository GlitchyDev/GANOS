package com.GlitchyDev.World.Entities.AbstractEntities;

import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Renderer;

public interface CustomRenderEntity {
    void renderCustomEntity(Renderer renderer, Camera camera);
}
