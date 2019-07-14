package com.GlitchyDev.World.General;

import com.GlitchyDev.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Rendering.Renderer;
import org.joml.Vector3f;

public interface CustomTransparentRenderable {
    void renderTransparency(Renderer renderer, Camera camera);
    double getDistance(Vector3f position);
}
