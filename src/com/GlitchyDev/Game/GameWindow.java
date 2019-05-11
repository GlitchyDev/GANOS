package com.GlitchyDev.Game;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GameWindow {
    // Displayed Title of Window
    private String title;
    private long windowHandle;

    private int width;
    private int height;

    private boolean resized;
    private boolean isVSync;

    private final int targetFPS = 60;

    private Matrix4f projectionMatrix;

    private static final float FOV = (float) Math.toRadians(60.0f);

    private static final float Z_NEAR = 0.01f;

    private static final float Z_FAR = 1000.f;

    public GameWindow(String title, int width, int height, boolean isVSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.isVSync = isVSync;
        this.resized = false;
        projectionMatrix = new Matrix4f();
    }

    public void init() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GL_FALSE); // the window will be resizable
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_DOUBLEBUFFER, GL_TRUE);
        //glfwWindowHint(GLFW_AUTO_ICONIFY, GL_FALSE);


        // Get the resolution of the primary monitor


        System.out.println("GameWindow: Create Window of WIDTH " + width + " HEIGHT " + height);

        // Create the window
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        glfwSetWindowSize(windowHandle,width,height);


        // Setup resize callback





        // Center our window
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowHandle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        if (isVSync()) {
            glfwSwapInterval(1);
        }

        GL.createCapabilities();

        // Set the clear color
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        // Use to polygons
        //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );

        // Support for transparencies
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        // Enable Culling of Double sided Triangles since we don't use em
        GL11.glEnable( GL11.GL_CULL_FACE );
        glCullFace(GL_BACK);

        //setIcon(windowHandle, new File("GameAssets/Textures/Icon/Icon16x16.png") , new File("GameAssets/Textures/Icon/Icon24x24.png"));

    }

    public void showWindow()
    {
        glfwShowWindow(windowHandle);
    }

    /*.
       Required at the beginning of each loop
    */
    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
        updateProjectionMatrix();
    }

    public Matrix4f updateProjectionMatrix() {
        float aspectRatio = (float)width / (float)height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Vector2i getWindowPosition() {
        int[] x = new int[1];
        int[] y = new int[1];
        glfwGetWindowPos(windowHandle,x,y);

        return new Vector2i(x[0],y[0]);
    }
    public void setCursor(InputStream stream, int xOffset, int yOffset) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int width = image.getWidth();
        int height = image.getHeight();

        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        // convert image to RGBA format
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int pixel = pixels[y * width + x];

                buffer.put((byte) ((pixel >> 16) & 0xFF));  // red
                buffer.put((byte) ((pixel >> 8) & 0xFF));   // green
                buffer.put((byte) (pixel & 0xFF));          // blue
                buffer.put((byte) ((pixel >> 24) & 0xFF));  // alpha
            }
        }
        buffer.flip(); // this will flip the cursor image vertically

        // create a GLFWImage
        GLFWImage cursorImg = GLFWImage.create();
        cursorImg.width(width);     // setup the images' width
        cursorImg.height(height);   // setup the images' height
        cursorImg.pixels(buffer);   // pass image data

        // create custom cursor and store its ID
        long cursorID = org.lwjgl.glfw.GLFW.glfwCreateCursor(cursorImg, xOffset , yOffset);

        // set current cursor
        glfwSetCursor(getWindowHandle(), cursorID);

        //glfwSetCursor(getWindowHandle(), glfwCreateStandardCursor(GLFW_HAND_CURSOR));
    }

    public void setDefaultCursor(int glfwCursor){
        glfwSetCursor(getWindowHandle(), glfwCreateStandardCursor(glfwCursor));
    }

    public void setCursorPosition(int x, int y) {
        glfwSetCursorPos(windowHandle,x,y);
    }
    public void setIcon(InputStream icon1, InputStream icon2) {

        BufferedImage img = null;
        BufferedImage img2 = null;
        try {
            img = ImageIO.read(icon1);
            img2 = ImageIO.read(icon2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        GLFWImage image = GLFWImage.malloc();
        image.set(img.getWidth(), img.getHeight(), loadImageToByteBuffer(img));

        GLFWImage image2 = GLFWImage.malloc();
        image2.set(img2.getWidth(), img2.getHeight(), loadImageToByteBuffer(img2));


        GLFWImage.Buffer images = GLFWImage.malloc(2);
        images.put(0, image);
        images.put(1, image2);

        glfwSetWindowIcon(windowHandle, images);


        images.free();
        image.free();
        image2.free();
    }

    private ByteBuffer loadImageToByteBuffer(BufferedImage image) {
        final byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
        int counter = 0;
        for (int i = 0; i < image.getHeight(); i++) {
            for (int j = 0; j < image.getWidth(); j++) {
                final int c = image.getRGB(j, i);
                buffer[counter + 0] = (byte) (c << 8 >> 24);
                buffer[counter + 1] = (byte) (c << 16 >> 24);
                buffer[counter + 2] = (byte) (c << 24 >> 24);
                buffer[counter + 3] = (byte) (c >> 24);
                counter += 4;
            }
        }
        ByteBuffer bb = BufferUtils.createByteBuffer(buffer.length);
        bb.put(buffer);
        bb.flip();
        return bb;
    }

    public void centerWindow() {
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(windowHandle, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
    }

    public void setWindowPosition(int x, int y) {
        glfwSetWindowPos(windowHandle,x,y);
    }

    public GLFWVidMode getVideoMode() {
        return glfwGetVideoMode(glfwGetPrimaryMonitor());
    }

    public void setTitle(String title) {
        glfwSetWindowTitle(windowHandle, title);
    }


    // Getter and Setters

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public long getWindowHandle() {
        return windowHandle;
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public boolean getWindowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void makeWindowClose() {
        glfwSetWindowShouldClose(windowHandle,true);
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean hasBeenResized() { return resized; }

    public void setIfResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isVSync() {
        return isVSync;
    }

    public int getTargetFPS() {
        return targetFPS;
    }

    public void setDimensions(int width, int height) {
        glfwSetWindowSize(windowHandle,width,height);
        glViewport(0, 0, width, height);
        this.width = width;
        this.height = height;

    }
}