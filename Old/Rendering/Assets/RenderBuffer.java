package com.GlitchyDev.Old.Rendering.Assets;

import org.lwjgl.opengl.GL14;

import static org.lwjgl.opengl.EXTFramebufferObject.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Works to store the texture outputted via a rendering process
 */
public class RenderBuffer {
    private final int framebufferID;
    private final int colorTextureID;
    private final int depthRenderBufferID;

    private final int width;
    private final int height;

    public RenderBuffer(int width, int height)
    {
        this.width = width;
        this.height = height;
        framebufferID = glGenFramebuffersEXT();                                         // create a new framebuffer
        colorTextureID = glGenTextures();                                               // and a new texture used as a color buffer
        depthRenderBufferID = glGenRenderbuffersEXT();                                  // And finally a new depthbuffer

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);                        // switch to the new framebuffer

        // initialize color texture
        glBindTexture(GL_TEXTURE_2D, colorTextureID);                                   // Bind the colorbuffer texture
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);               // make it linear filterd
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0,GL_RGBA, GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        glFramebufferTexture2DEXT(GL_FRAMEBUFFER_EXT,GL_COLOR_ATTACHMENT0_EXT,GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer


        // initialize depth renderbuffer
        glBindRenderbufferEXT(GL_RENDERBUFFER_EXT, depthRenderBufferID);                // bind the depth renderbuffer
        glRenderbufferStorageEXT(GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height); // get the data space for it
        glFramebufferRenderbufferEXT(GL_FRAMEBUFFER_EXT,GL_DEPTH_ATTACHMENT_EXT,GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer

        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public void bindToRender()
    {

        glViewport (0, 0, width, height);                                    // set The Current Viewport to the fbo size
        glBindTexture(GL_TEXTURE_2D, 0);                                // unlink textures because if we dont it all is gonna fail
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, framebufferID);        // switch to rendering on our FBO


    }

    public void unbindToRender(int width, int height)
    {
        glViewport (0, 0, width, height);
        glEnable(GL_TEXTURE_2D);
        glBindFramebufferEXT(GL_FRAMEBUFFER_EXT, 0);
    }

    public int getTextureID()
    {
        return colorTextureID;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
