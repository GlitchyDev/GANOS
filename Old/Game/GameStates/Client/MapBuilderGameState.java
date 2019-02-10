package com.GlitchyDev.Old.Game.GameStates.Client;

import com.GlitchyDev.Old.Game.GameStates.Abstract.EnvironmentGameState;
import com.GlitchyDev.Old.Game.GameStates.GameStateType;
import com.GlitchyDev.Old.GameInput.ControllerDirectionPad;
import com.GlitchyDev.Old.GameInput.GameController;
import com.GlitchyDev.Old.GameInput.GameControllerManager;
import com.GlitchyDev.Old.IO.AssetLoader;
import com.GlitchyDev.Old.Rendering.Assets.Fonts.CustomFontTexture;
import com.GlitchyDev.Old.Rendering.Assets.*;
import com.GlitchyDev.Old.Rendering.Assets.Sounds.SoundSource;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.Camera;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.GameItem;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.SpriteItem;
import com.GlitchyDev.Old.Rendering.Assets.WorldElements.TextItem;
import com.GlitchyDev.Old.Utility.GlobalGameData;
import com.GlitchyDev.Old.World.Blocks.Abstract.BlockBase;
import com.GlitchyDev.Old.World.Blocks.Abstract.PartialCubicBlock;
import com.GlitchyDev.Old.World.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import static org.lwjgl.glfw.GLFW.*;

/**
 * A gamestate designed to be the main form of building Map files in the game, as well as debugging functions
 *
 * Potentially, one should be able to use this gamestate to edit a live game owo
 *
 * Features should include
 * - Creating Map Files
 * - Loading Map Files
 * - Editing Map Files
 * - Saving Map Files
 *
 * Wanted Features:
 * - Make Selection of block faces based on rotation, no more guessing owo
 *
 */
public class MapBuilderGameState extends EnvironmentGameState {
    // Show off the camera switching feature
    private Camera camera;
    private Camera camera2;
    // Used to render Camera 2 to texture for viewing ( Camera screens, mirrors, ect )
    private Texture renderTexture;
    private RenderBuffer renderBuffer;
    // Game Items held
    private ArrayList<GameItem> gameItems = new ArrayList<>();
    private ArrayList<TextItem> hudItems = new ArrayList<>();
    private ArrayList<SpriteItem> centerCursor = new ArrayList<>();
    private ArrayList<SpriteItem> textureEditHud = new ArrayList<>();
    private ArrayList<GameItem> debugItems = new ArrayList<>();
    private PartialCubicBlock cursor;

    // Init'd controller 1
    private GameController controller;
    // Mesh for PartialCubicBlocks
    private PartialCubicInstanceMesh instancedMesh;
    private InstancedGridTexture instancedGridTexture;
    private PartialCubicInstanceMesh cursorInstancedMesh;

    // Loaded World
    private World world;

    // Configs of textures for texture selection menu
    private final int TEXTURE_SCALING = 1;
    private final int XOFFSET = 0;
    private final int YOFFSET = 130;

    // Loaded Texture Selections
    private ArrayList<InstancedGridTexture> availableInstanceTextures = new ArrayList<>();
    private int selectedTexturePackId = 0;
    private int selectedTextureId = 0;
    private int textureCursorX = 0;
    private int textureCursorY = 0;

    private SoundSource backgroundMusic;

    private EditState currentEditState = EditState.MOVE_CURSOR;
    private Location cursorLocation = new Location(0,0,0,null);
    private boolean enableWallMode = true;

    private Thread debugCommandThread;
    private DebugCommandRunnable debugCommandRunnable;

    public MapBuilderGameState(GlobalGameData globalGameDataBase) {
        super(globalGameDataBase, GameStateType.MAPBUILDER);
        init();
    }

    @Override
    public void init() {
        camera = new Camera();
        camera.setPosition(0,10,10);
        camera.setRotation(45,0, 0);

        camera2 = new Camera();
        camera2.setPosition(-5,10,-5);
        camera2.setRotation(30,135, 0);

        renderBuffer = new RenderBuffer(500,500);
        renderTexture = new Texture(renderBuffer);



        Mesh instanceMesh = AssetLoader.getMeshAsset("CubicMesh1");
        instanceMesh.setTexture(renderTexture);

        gameItems.add(new GameItem(instanceMesh));
        gameItems.get(0).setPosition(0,0,0);
        gameItems.get(0).setRotation(0,0,0);
        gameItems.get(0).setScale(1);

        gameItems.add(new GameItem(instanceMesh));
        gameItems.get(1).setPosition(0,0,0);
        gameItems.get(1).setRotation(0,0,0);
        gameItems.get(1).setScale(1.0f);

        gameItems.add(new GameItem(instanceMesh));
        gameItems.get(2).setPosition(0,0,0);
        gameItems.get(2).setRotation(0,0,90);
        gameItems.get(2).setScale(1.0f);

        gameItems.add(new GameItem(instanceMesh));
        gameItems.get(3).setPosition(0,0,0);
        gameItems.get(3).setRotation(0,0,180);
        gameItems.get(3).setScale(1.0f);



        CustomFontTexture customTexture = new CustomFontTexture("DebugFont");

        final int NUM_TEX_ITEMS = 16;
        for(int i = 0; i < NUM_TEX_ITEMS; i++) {
            TextItem item = new TextItem("",customTexture);
            item.setPosition(55*3,i*12,0);
            hudItems.add(item);
        }



        SpriteItem item = new SpriteItem(AssetLoader.getTextureAsset("Text_Cursor"), 2, 2, true,true);
        item.setPosition(-1f,1f,-1f);



        debugItems.add(item);







        boolean[] defaultOrientation = new boolean[]{true,true,true,true,true,true};
        //int[] defaultTextureMapping = new int[]{0,1,2,3,4,5};


        // REGISTER THESE GOD DAMN GRID TEXTURES AS ASSETS
        AssetLoader.registerInstanceGridTexture(AssetLoader.getTextureAsset("EditCursor"),"EditCursor",2,3);
        InstancedGridTexture cursorGridTexture = AssetLoader.getInstanceGridTexture("EditCursor");

        HashMap<String,String> gridTextureRegistry = AssetLoader.getConfigOptionAsset("InstanceTextureRegistry");
        for(String name: gridTextureRegistry.keySet()) {
            AssetLoader.registerInstanceGridTexture(AssetLoader.getTextureAsset(name),name, AssetLoader.getTextureAsset(name).getWidth()/Integer.valueOf(gridTextureRegistry.get(name)),AssetLoader.getTextureAsset(name).getHeight()/Integer.valueOf(gridTextureRegistry.get(name)));
            availableInstanceTextures.add(AssetLoader.getInstanceGridTexture(name));
        }

        instancedGridTexture = AssetLoader.getInstanceGridTexture("UVMapCubeTexture");

        cursorInstancedMesh = new PartialCubicInstanceMesh(AssetLoader.getMeshAsset("CubicMesh1"),3600, cursorGridTexture);
        instancedMesh = new PartialCubicInstanceMesh(AssetLoader.loadMesh("/Mesh/PartialCubicBlock/CubicMesh1.obj"),60*60, instancedGridTexture);




        SpriteItem spriteItem1 = new SpriteItem(availableInstanceTextures.get(0),true);
        spriteItem1.setScale(TEXTURE_SCALING);
        spriteItem1.setPosition(XOFFSET,YOFFSET,0);
        SpriteItem spriteItem2 = new SpriteItem(AssetLoader.getTextureAsset("Cursor"), availableInstanceTextures.get(0).getCubeSideLength() ,availableInstanceTextures.get(0).getCubeSideLength(), true);
        spriteItem2.setScale(TEXTURE_SCALING);
        spriteItem2.setPosition(XOFFSET,YOFFSET,1);

        SpriteItem spriteItem3 = new SpriteItem(AssetLoader.getTextureAsset("Selector"),true);
        spriteItem3.setScale(1);
        spriteItem3.setPosition(globalGameData.getGameWindow().getWidth()/2 - 25, globalGameData.getGameWindow().getHeight()/2 - 25,1);

        textureEditHud.add(spriteItem1);
        textureEditHud.add(spriteItem2);
        textureEditHud.add(spriteItem3);

        int[] zeroTextureMap = new int[]{0,0,0,0,0,0};
        cursor = new PartialCubicBlock(new Location(0,0,0,world), cursorGridTexture,defaultOrientation,zeroTextureMap);
        cursor.setScale(1.4f);

        int width = 60;
        int height = 1;
        int length = 60;

       // world = new World("DebugWorld");

        SpriteItem centerMark = new SpriteItem(AssetLoader.getTextureAsset("Selector"),true);
        centerMark.setPosition(globalGameData.getGameWindow().getWidth()/2 - 25,globalGameData.getGameWindow().getHeight()/2 - 25,0.5f);
        centerCursor.add(centerMark);

        world = new World("TestWorld");
        ArrayList<Integer> textureCode = new ArrayList<>();
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                for(int z = 0; z < length; z++) {
                    Location location = new Location(x,y,z,world);
                    boolean[] faceStates = new boolean[6];
                    textureCode.clear();
                    faceStates[0] = true;
                    for(int i = 0; i < 6; i++) {
                        textureCode.add(i);
                    }
                    int[] a = new int[textureCode.size()];
                    for(int b = 0; b < a.length; b++) {
                        a[b] = textureCode.get(b);
                    }
                    PartialCubicBlock b = new PartialCubicBlock(location, instancedGridTexture,faceStates,a);

                        world.setBlock(location, b);


                }
            }
        }

        GameControllerManager.loadControllers();
        if(GameControllerManager.getLoadedControllerCount() != 0) {
            controller = GameControllerManager.getControllers().get(0);
        }


        backgroundMusic = new SoundSource(true,true);





    }






    private final NumberFormat formatter = new DecimalFormat("#0.00");
    @Override
    public void logic() {
        if(controller != null) {
            controller.tick();
        }
        camera.updateViewMatrix();
        camera2.updateViewMatrix();

        hudItems.get(1).setText("Render: " + formatter.format(getRenderUtilization()) + " Logic: " + formatter.format(getLogicUtilization()));
        hudItems.get(2).setText("FPS:" + getCurrentFPS());
        hudItems.get(4).setText("Pos:" + formatter.format(camera.getPosition().x) + "," + formatter.format(camera.getPosition().y) + "," + formatter.format(camera.getPosition().z)+ " Rot:" + formatter.format(camera.getRotation().x) + "," + formatter.format(camera.getRotation().y) + "," + formatter.format(camera.getRotation().z));
        hudItems.get(5).setText("Editor Mode: " + currentEditState + " EnableWallMode: " + enableWallMode);
        hudItems.get(6).setText("Cursor Location: " + cursorLocation.getX() + " Y:" + cursorLocation.getY() + " Z:" + cursorLocation.getZ());
        hudItems.get(7).setText("Blocks Rendered: " + instancedMesh.getBlocksRendered() + " Blocks Ignored: " + instancedMesh.getBlocksIgnored());
        if(currentEditState == EditState.EDIT_TEXTURE) {
            hudItems.get(8).setText("TexturePackID: " + selectedTexturePackId + " TextureID: " + selectedTextureId + " Cursor " + textureCursorX + "," + textureCursorY);
        }
        else {
            hudItems.get(8).setText("");
        }
        hudItems.get(9).setText("Time: " + gameInputTimings.getActiveMouseButton1Time());



        cameraControlsLogic();
        if(controller.isCurrentlyActive()) {
            editControlsLogic();
        }




        debugItems.get(0).setRotation(debugItems.get(0).getRotation().x,debugItems.get(0).getRotation().y,debugItems.get(0).getRotation().z + 1);

        //logicCamera();



        ArrayList<String> files = gameInput.getDraggedFiles();
        if(files.size() != 0) {
            System.out.println("File Dragged");
            for(String filePath: files) {
                File draggedFile = new File(filePath);
                if(draggedFile.getName().contains("wcnk")) {

                    try {
                        ObjectInputStream in = new ObjectInputStream(new FileInputStream(draggedFile));
                        Region region = new Region(in,cursorLocation.clone() );

                        Direction direction = Direction.NORTH;
                        if(controller.getNorthButton()) {
                            direction = Direction.NORTH;
                        }
                        if(controller.getEastButton()) {
                            direction = Direction.EAST;
                        }
                        if(controller.getSouthButton()) {
                            direction = Direction.SOUTH;
                        }
                        if(controller.getWestButton()) {
                            direction = Direction.WEST;
                        }
                        System.out.println("Adding RegionBase in " + direction);
                        world.addRegion(region,cursorLocation.clone(),direction);






                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        }


        ArrayList<String> commands = debugCommandRunnable.getCommands();
        if(commands.size() != 0) {
            for(String command: commands) {
                String[] parsedCommand = command.split(" ");
                String[] args = new String[parsedCommand.length-1];
                int index = 0;
                for(String arg: parsedCommand) {
                    if(index != 0) {
                        args[index-1] = arg;
                    }
                    index++;
                }
                debugCommands(parsedCommand[0],args);
            }
        }


    }


    public void debugCommands(String command, String[] args) {
        System.out.println("Recieved Command " + command);
        switch(command) {



        }
    }


    // Left Controller Bumper
    public void debugButtonFunction(Location location, BlockBase blockAtLocation) {

        if(controller.getLeftTrigger() >= 0.95) {
            for(Chunk chunk: world.getChunks()) {
                world.addRegion(new Chunk(chunk.getChunkCord()),cursorLocation,Direction.NORTH);
            }
        } else {
            File file = new File(System.getProperty("user.home") + "/Desktop" + "/Hallway.wcnk");
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                world.getChunk(new ChunkCord(0, 0)).writeObject(oos);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void debugDirectionButtonFunction(Location location, BlockBase blockAtLocation, Direction direction) {
        File file = new File(System.getProperty("user.home") + "/Desktop" + "/Hallway.wcnk");
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(direction);
        Region region = new Region(in,cursorLocation);
        world.addRegion(region,location,direction);
    }




    /**
     * Movement for Camera
     *
     * Left Joystick
     * - Button + Vertical Direction: Y axis
     * - Horizontal Direction: Relative Left and Right
     * - Vertical Direction: Relative Forward and Backwards
     *
     * Right JoyStick
     * - Button: Switch Camera
     * - Horizontal Direction: Pan Camera Left/Right
     * - Vertical Direction: Pan Camera Up/Down
     */

    private void cameraControlsLogic() {
        final float CAMERA_MOVEMENT_AMOUNT = 0.3f;
        final float CAMERA_ROTATION_AMOUNT = 3.0f;
        final float JOYSTICK_THRESHOLD = 0.2f;

        if(controller != null && controller.isCurrentlyActive()) {
            if (!controller.getLeftJoyStickButton()) {
                if (controller.getLeftJoyStickY() < -JOYSTICK_THRESHOLD) {
                    camera.moveForward(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickY() > JOYSTICK_THRESHOLD) {
                    camera.moveBackwards(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickX() > JOYSTICK_THRESHOLD) {
                    camera.moveRight(controller.getLeftJoyStickX() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickX() < -JOYSTICK_THRESHOLD) {
                    camera.moveLeft(controller.getLeftJoyStickX() * CAMERA_MOVEMENT_AMOUNT);
                }
            } else {
                if (controller.getLeftJoyStickY() > JOYSTICK_THRESHOLD) {
                    camera.moveDown(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }
                if (controller.getLeftJoyStickY() < -JOYSTICK_THRESHOLD) {
                    camera.moveUp(controller.getLeftJoyStickY() * CAMERA_MOVEMENT_AMOUNT);
                }

            }

            if (controller.getRightJoyStickX() > JOYSTICK_THRESHOLD || controller.getRightJoyStickX() < -JOYSTICK_THRESHOLD) {
                camera.moveRotation(0, controller.getRightJoyStickX() * CAMERA_ROTATION_AMOUNT, 0);
            }
            if (controller.getRightJoyStickY() > JOYSTICK_THRESHOLD || controller.getRightJoyStickY() < -JOYSTICK_THRESHOLD) {
                camera.moveRotation(controller.getRightJoyStickY() * CAMERA_ROTATION_AMOUNT, 0, 0);

            }

            if (controller.getToggleRightJoyStickButton()) {
                Camera temp = camera;
                camera = camera2;
                camera2 = temp;
            }
        } else {
            if (gameInput.getKeyValue(GLFW_KEY_SPACE) >= 1) {
                camera.moveUp(1);
            }
            if (gameInput.getKeyValue(GLFW_KEY_LEFT_SHIFT) >= 1) {
                camera.moveDown(1);
            }

            if (gameInput.getKeyValue(GLFW_KEY_UP) >= 1) {
                camera.moveForward(1);
            }
            if (gameInput.getKeyValue(GLFW_KEY_DOWN) >= 1) {
                camera.moveBackwards(1);
            }
            if (gameInput.getKeyValue(GLFW_KEY_LEFT) >= 1) {
                camera.moveLeft(1);
            }
            if (gameInput.getKeyValue(GLFW_KEY_RIGHT) >= 1) {
                camera.moveRight(1);
            }


            if (gameInput.getKeyValue(GLFW_KEY_W) >= 1) {
                camera.moveRotation(-1,0,0);
            }
            if (gameInput.getKeyValue(GLFW_KEY_S) >= 1) {
                camera.moveRotation(1,0,0);
            }
            if (gameInput.getKeyValue(GLFW_KEY_D) >= 1) {
                camera.moveRotation(0,1,0);
            }
            if (gameInput.getKeyValue(GLFW_KEY_A) >= 1) {
                camera.moveRotation(0,-1,0);
            }





            if (controller.getRightJoyStickX() > JOYSTICK_THRESHOLD || controller.getRightJoyStickX() < -JOYSTICK_THRESHOLD) {
                camera.moveRotation(0, controller.getRightJoyStickX() * CAMERA_ROTATION_AMOUNT, 0);
            }
            if (controller.getRightJoyStickY() > JOYSTICK_THRESHOLD || controller.getRightJoyStickY() < -JOYSTICK_THRESHOLD) {
                camera.moveRotation(controller.getRightJoyStickY() * CAMERA_ROTATION_AMOUNT, 0, 0);

            }

            if (gameInput.getKeyValue(GLFW_KEY_TAB) >= 1) {
                Camera temp = camera;
                camera = camera2;
                camera2 = temp;
            }
        }
    }

    public enum EditState {
        MOVE_CURSOR, // Move Cursor, Delete whole Tiles, Change model
        EDIT_TEXTURE, // Select Texture Sheet, Bind Textures, edit Mesh
        ;
        public EditState toggleState() {
            switch(this) {
                case MOVE_CURSOR:
                    return EDIT_TEXTURE;
                case EDIT_TEXTURE:
                    return MOVE_CURSOR;
            }
            return MOVE_CURSOR;
        }
    }

    private void editControlsLogic()
    {
        if(controller.getToggleRightHomeButton()) {
            currentEditState = currentEditState.toggleState();
            switch(currentEditState) {

                case MOVE_CURSOR:
                    for(Direction direction: Direction.values()) {
                        cursor.setDirectionTexture(direction,0);
                    }
                    break;
                case EDIT_TEXTURE:
                    for(Direction direction: Direction.values()) {
                        cursor.setDirectionTexture(direction,2);
                    }
                    break;
            }

        }
        switch(currentEditState)
        {

            case MOVE_CURSOR:
                moveCursorControlsLogic();
                break;
            case EDIT_TEXTURE:
                editTextureControlsLogic();
                break;
        }
    }




    private void moveCursorControlsLogic() {


        if (!controller.getLeftHomeButton()) {
            if (controller.getToggleNorthButton()) {
                cursorLocation = cursorLocation.getOffsetLocation(0, 0, -1);
            }
            if (controller.getToggleSouthButton()) {
                cursorLocation = cursorLocation.getOffsetLocation(0, 0, 1);
            }
            if (controller.getToggleWestButton()) {
                cursorLocation = cursorLocation.getOffsetLocation(-1, 0, 0);
            }
            if (controller.getToggleEastButton()) {
                cursorLocation = cursorLocation.getOffsetLocation(1, 0, 0);
            }
            if (controller.getToggleRightBumperButton()) {
                cursorLocation = cursorLocation.getOffsetLocation(0, 1, 0);
            }
            if (controller.getToggleRightTrigger()) {
                if (cursorLocation.getY() != 0) {
                    cursorLocation = cursorLocation.getOffsetLocation(0, -1, 0);
                }
            }
            cursor.setLocation(cursorLocation);
        } else {
            ArrayList<BlockBase> blocks = new ArrayList<>();
            for (Chunk chunk : world.getChunks()) {
                for (BlockBase[][] b : chunk.getBlocks()) {
                    for (BlockBase[] bb : b) {
                        for (BlockBase bbb : bb) {
                            if (bbb != null) {
                                blocks.add(bbb);
                            }
                        }
                    }
                }
            }

            BlockBase b = selectBlock3D(blocks, camera);
            if (b != null) {
                cursorLocation = b.getLocation();
                cursor.setLocation(cursorLocation);
            }
        }

        if (controller.getToggleLeftTrigger()) {
            BlockBase blockAtCursorLocation = world.getBlock(cursor.getLocation());
            Location cursorLocationClone = cursorLocation.clone();
            if (blockAtCursorLocation != null) {
                world.setBlock(cursorLocationClone, null);
            } else {
                if(enableWallMode) {
                    PartialCubicBlock partialCubicBlock = new PartialCubicBlock(cursorLocationClone, instancedGridTexture);
                    for(Direction direction: Direction.values()) {
                        partialCubicBlock.setDirectionFaceState(direction,true);
                    }
                    // Adds block to world
                    world.setBlock(cursorLocationClone, partialCubicBlock);
                    for(Direction direction: Direction.values()) {
                        Location offsetLocation = cursorLocationClone.getDirectionLocation(direction);
                        BlockBase wall = world.getBlock(offsetLocation);
                        if(wall instanceof PartialCubicBlock) {
                            if((partialCubicBlock).getDirectionFaceState(direction)) {

                                ((PartialCubicBlock) wall).setDirectionFaceState(direction.reverse(),false);
                                ((PartialCubicBlock)world.getBlock(cursorLocation)).setDirectionFaceState(direction,false);

                                if(wall.isUseless()) {
                                    world.setBlock(offsetLocation,null);
                                }
                            }
                        }
                    }
                }
                else {
                    world.setBlock(cursorLocationClone, new PartialCubicBlock(cursorLocationClone, instancedGridTexture));
                }
            }
        }

        if(world.getBlock(cursorLocation) instanceof PartialCubicBlock) {
            for(Direction direction: Direction.values()) {
                cursor.setDirectionTexture(direction,0);
            }
        }
        else {
            for(Direction direction: Direction.values()) {
                cursor.setDirectionTexture(direction,1);
            }
        }




        if (controller.getToggleLeftBumperButton()) {
            debugButtonFunction(cursorLocation.clone(),world.getBlock(cursorLocation));
        }
        if(controller.getToggleDirectionPad() != ControllerDirectionPad.NONE) {
            debugDirectionButtonFunction(cursorLocation,world.getBlock(cursorLocation),controller.getDirectionPad().getDirection());
        }




        /*
            Use DirectionPad to change models
         */
    }



    private final int FILL_RADIUS = 5;


    private void editTextureControlsLogic() {
        cursor.setAboveTexture(controller.getRightBumperButton() ? 3 : 2);
        cursor.setBelowTexture(controller.getRightTrigger() >= 0.95 ? 3 : 2);
        cursor.setNorthTexture(controller.getNorthButton() ? 3 : 2);
        cursor.setEastTexture(controller.getEastButton() ? 3 : 2);
        cursor.setSouthTexture(controller.getSouthButton() ? 3 : 2);
        cursor.setWestTexture(controller.getWestButton() ? 3 : 2);

        if(controller.getToggleLeftTrigger()) {
            BlockBase block = world.getBlock(cursorLocation);
            int index = 0;
            for(int texture: cursor.getAssignedTextures()) {
                if(texture == 3) {
                    if(block instanceof PartialCubicBlock) {
                        ((PartialCubicBlock) block).getFaceStates()[index] = !((PartialCubicBlock) block).getFaceStates()[index];
                    }
                }
                index++;
            }
            if(block != null && block.isUseless()) {
                world.setBlock(cursorLocation,null);
            }
        }

        if(controller.getToggleDirectionPad() != ControllerDirectionPad.NONE) {
            switch(controller.getDirectionPad()) {
                case NORTH:
                    selectedTextureId = (selectedTextureId == 0) ? (availableInstanceTextures.get(selectedTexturePackId).getTextureGridWidth() * availableInstanceTextures.get(selectedTexturePackId).getTextureGridHeight())-1 : selectedTextureId-1;
                    textureCursorX = selectedTextureId % availableInstanceTextures.get(selectedTexturePackId).getTextureGridWidth();
                    textureCursorY = selectedTextureId / availableInstanceTextures.get(selectedTexturePackId).getTextureGridWidth();
                    setCursorPosition(textureCursorX,textureCursorY);
                    break;
                case EAST:
                    selectedTexturePackId= (selectedTexturePackId == availableInstanceTextures.size()-1) ? 0 : selectedTexturePackId+1;
                    loadTexturePack();
                    selectedTextureId = 0;
                    break;
                case SOUTH:
                    selectedTextureId = (selectedTextureId >= (availableInstanceTextures.get(selectedTexturePackId).getTextureGridWidth() * availableInstanceTextures.get(selectedTexturePackId).getTextureGridHeight())-1) ? 0 : selectedTextureId+1;
                    textureCursorX = selectedTextureId % availableInstanceTextures.get(selectedTexturePackId).getTextureGridWidth();
                    textureCursorY = selectedTextureId / availableInstanceTextures.get(selectedTexturePackId).getTextureGridWidth();
                    setCursorPosition(textureCursorX,textureCursorY);
                    break;
                case WEST:
                    selectedTexturePackId = (selectedTexturePackId == 0) ? availableInstanceTextures.size()-1 : selectedTexturePackId-1;
                    loadTexturePack();
                    selectedTextureId = 0;
                    break;
            }
        }

        if(controller.getToggleLeftBumperButton()) {
            if (world.getBlock(cursorLocation) instanceof PartialCubicBlock) {
                PartialCubicBlock block = (PartialCubicBlock) world.getBlock(cursorLocation);
                InstancedGridTexture blockTexture = block.getInstancedGridTexture();

                if (controller.getNorthButton()) {
                    final Direction assignedDirection = Direction.NORTH;
                    if(controller.getLeftHomeButton()) {
                        BlockBase baseBlock = world.getBlock(cursorLocation);
                        if(baseBlock instanceof PartialCubicBlock && ((PartialCubicBlock) baseBlock).getDirectionFaceState(assignedDirection)) {
                            InstancedGridTexture compareTexture = ((PartialCubicBlock) baseBlock).getInstancedGridTexture();
                            int compareTextureId = ((PartialCubicBlock) baseBlock).getDirectionTexture(assignedDirection);
                            for(int x = -FILL_RADIUS; x <= FILL_RADIUS; x++) {
                                for(int y = -FILL_RADIUS; y <= FILL_RADIUS; y++) {
                                    BlockBase b = world.getBlock(cursorLocation.getOffsetLocation(x,y,0));
                                    if(b instanceof PartialCubicBlock) {
                                        PartialCubicBlock pb2 = (PartialCubicBlock) b;
                                        if(pb2.getInstancedGridTexture() == compareTexture && pb2.getDirectionTexture(assignedDirection) == compareTextureId) {
                                            if(((PartialCubicBlock) b).getDirectionFaceState(assignedDirection)) {
                                                if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                                                    pb2.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                                                    for (Direction direction : Direction.values()) {
                                                        pb2.setDirectionTexture(direction, 0);
                                                    }
                                                }
                                                pb2.setDirectionFaceState(assignedDirection, true);
                                                pb2.setDirectionTexture(assignedDirection, selectedTextureId);

                                            }
                                        }

                                    }
                                }
                            }
                        }
                    } else {
                        if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                            block.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                            for (Direction direction : Direction.values()) {
                                block.setDirectionTexture(direction, 0);
                            }
                        }
                        block.setDirectionFaceState(assignedDirection, true);
                        block.setDirectionTexture(assignedDirection, selectedTextureId);
                    }
                }
                if (controller.getSouthButton()) {
                    final Direction assignedDirection = Direction.SOUTH;
                    if(controller.getLeftHomeButton()) {
                        BlockBase baseBlock = world.getBlock(cursorLocation);
                        if(baseBlock instanceof PartialCubicBlock && ((PartialCubicBlock) baseBlock).getDirectionFaceState(assignedDirection)) {
                            InstancedGridTexture compareTexture = ((PartialCubicBlock) baseBlock).getInstancedGridTexture();
                            int compareTextureId = ((PartialCubicBlock) baseBlock).getDirectionTexture(assignedDirection);
                            for(int x = -FILL_RADIUS; x <= FILL_RADIUS; x++) {
                                for(int y = -FILL_RADIUS; y <= FILL_RADIUS; y++) {
                                    BlockBase b = world.getBlock(cursorLocation.getOffsetLocation(x,y,0));
                                    if(b instanceof PartialCubicBlock) {
                                        PartialCubicBlock pb2 = (PartialCubicBlock) b;
                                        if(pb2.getInstancedGridTexture() == compareTexture && pb2.getDirectionTexture(assignedDirection) == compareTextureId) {
                                            if(((PartialCubicBlock) b).getDirectionFaceState(assignedDirection)) {
                                                if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                                                    pb2.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                                                    for (Direction direction : Direction.values()) {
                                                        pb2.setDirectionTexture(direction, 0);
                                                    }
                                                }
                                                pb2.setDirectionFaceState(assignedDirection, true);
                                                pb2.setDirectionTexture(assignedDirection, selectedTextureId);

                                            }
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                            block.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                            for (Direction direction : Direction.values()) {
                                block.setDirectionTexture(direction, 0);
                            }
                        }
                        block.setDirectionFaceState(assignedDirection, true);
                        block.setDirectionTexture(assignedDirection, selectedTextureId);
                    }
                }
                if (controller.getEastButton()) {
                    final Direction assignedDirection = Direction.EAST;
                    if(controller.getLeftHomeButton()) {
                        BlockBase baseBlock = world.getBlock(cursorLocation);
                        if(baseBlock instanceof PartialCubicBlock && ((PartialCubicBlock) baseBlock).getDirectionFaceState(assignedDirection)) {
                            InstancedGridTexture compareTexture = ((PartialCubicBlock) baseBlock).getInstancedGridTexture();
                            int compareTextureId = ((PartialCubicBlock) baseBlock).getDirectionTexture(assignedDirection);
                            for(int y = -FILL_RADIUS; y <= FILL_RADIUS; y++) {
                                for(int z = -FILL_RADIUS; z <=  FILL_RADIUS; z++) {
                                    BlockBase b = world.getBlock(cursorLocation.getOffsetLocation(0,y,z));
                                    if(b instanceof PartialCubicBlock) {
                                        PartialCubicBlock pb2 = (PartialCubicBlock) b;
                                        if(pb2.getInstancedGridTexture() == compareTexture && pb2.getDirectionTexture(assignedDirection) == compareTextureId) {
                                            if(((PartialCubicBlock) b).getDirectionFaceState(assignedDirection)) {
                                                if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                                                    pb2.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                                                    for (Direction direction : Direction.values()) {
                                                        pb2.setDirectionTexture(direction, 0);
                                                    }
                                                }
                                                pb2.setDirectionFaceState(assignedDirection, true);
                                                pb2.setDirectionTexture(assignedDirection, selectedTextureId);

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                            block.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                            for (Direction direction : Direction.values()) {
                                block.setDirectionTexture(direction, 0);
                            }
                        }
                        block.setDirectionFaceState(assignedDirection, true);
                        block.setDirectionTexture(assignedDirection, selectedTextureId);
                    }
                }
                if (controller.getWestButton()) {
                    final Direction assignedDirection = Direction.WEST;
                    if(controller.getLeftHomeButton()) {
                        BlockBase baseBlock = world.getBlock(cursorLocation);
                        if(baseBlock instanceof PartialCubicBlock && ((PartialCubicBlock) baseBlock).getDirectionFaceState(assignedDirection)) {
                            InstancedGridTexture compareTexture = ((PartialCubicBlock) baseBlock).getInstancedGridTexture();
                            int compareTextureId = ((PartialCubicBlock) baseBlock).getDirectionTexture(assignedDirection);
                            for(int y = -FILL_RADIUS; y <= FILL_RADIUS; y++) {
                                for(int z = -FILL_RADIUS; z <=  FILL_RADIUS; z++) {
                                    BlockBase b = world.getBlock(cursorLocation.getOffsetLocation(0,y,z));
                                    if(b instanceof PartialCubicBlock) {
                                        PartialCubicBlock pb2 = (PartialCubicBlock) b;
                                        if(pb2.getInstancedGridTexture() == compareTexture && pb2.getDirectionTexture(assignedDirection) == compareTextureId) {
                                            if(((PartialCubicBlock) b).getDirectionFaceState(assignedDirection)) {
                                                if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                                                    pb2.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                                                    for (Direction direction : Direction.values()) {
                                                        pb2.setDirectionTexture(direction, 0);
                                                    }
                                                }
                                                pb2.setDirectionFaceState(assignedDirection, true);
                                                pb2.setDirectionTexture(assignedDirection, selectedTextureId);

                                            }
                                        }

                                    }
                                }
                            }
                        }
                    } else {
                        if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                            block.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                            for (Direction direction : Direction.values()) {
                                block.setDirectionTexture(direction, 0);
                            }
                        }
                        block.setDirectionFaceState(assignedDirection, true);
                        block.setDirectionTexture(assignedDirection, selectedTextureId);
                    }
                }
                if (controller.getRightBumperButton()) {
                    final Direction assignedDirection = Direction.ABOVE;
                    if(controller.getLeftHomeButton()) {
                        BlockBase baseBlock = world.getBlock(cursorLocation);
                        if(baseBlock instanceof PartialCubicBlock && ((PartialCubicBlock) baseBlock).getDirectionFaceState(assignedDirection)) {
                            InstancedGridTexture compareTexture = ((PartialCubicBlock) baseBlock).getInstancedGridTexture();
                            int compareTextureId = ((PartialCubicBlock) baseBlock).getDirectionTexture(assignedDirection);
                            for(int x = -FILL_RADIUS; x <= FILL_RADIUS; x++) {
                                for(int z = -FILL_RADIUS; z <=  FILL_RADIUS; z++) {
                                    BlockBase b = world.getBlock(cursorLocation.getOffsetLocation(x,0,z));
                                    if(b instanceof PartialCubicBlock) {
                                        PartialCubicBlock pb2 = (PartialCubicBlock) b;
                                        if(pb2.getInstancedGridTexture() == compareTexture && pb2.getDirectionTexture(assignedDirection) == compareTextureId) {
                                            if(((PartialCubicBlock) b).getDirectionFaceState(assignedDirection)) {
                                                if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                                                    pb2.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                                                    for (Direction direction : Direction.values()) {
                                                        pb2.setDirectionTexture(direction, 0);
                                                    }
                                                }
                                                pb2.setDirectionFaceState(assignedDirection, true);
                                                pb2.setDirectionTexture(assignedDirection, selectedTextureId);

                                            }
                                        }
                                    }
                                }

                            }
                        }
                    } else {
                        if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                            block.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                            for (Direction direction : Direction.values()) {
                                block.setDirectionTexture(direction, 0);
                            }
                        }
                        block.setDirectionFaceState(assignedDirection, true);
                        block.setDirectionTexture(assignedDirection, selectedTextureId);
                    }
                }
                if (controller.getRightTrigger() >= 0.95) {
                    final Direction assignedDirection = Direction.BELOW;
                    if(controller.getLeftHomeButton()) {
                        BlockBase baseBlock = world.getBlock(cursorLocation);
                        if(baseBlock instanceof PartialCubicBlock && ((PartialCubicBlock) baseBlock).getDirectionFaceState(assignedDirection)) {
                            InstancedGridTexture compareTexture = ((PartialCubicBlock) baseBlock).getInstancedGridTexture();
                            int compareTextureId = ((PartialCubicBlock) baseBlock).getDirectionTexture(assignedDirection);
                            for(int x = -FILL_RADIUS; x <= FILL_RADIUS; x++) {
                                for(int z = -FILL_RADIUS; z <=  FILL_RADIUS; z++) {
                                    BlockBase b = world.getBlock(cursorLocation.getOffsetLocation(x,0,z));
                                    if(b instanceof PartialCubicBlock) {
                                        PartialCubicBlock pb2 = (PartialCubicBlock) b;
                                        if(pb2.getInstancedGridTexture() == compareTexture && pb2.getDirectionTexture(assignedDirection) == compareTextureId) {
                                            if(((PartialCubicBlock) b).getDirectionFaceState(assignedDirection)) {
                                                if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                                                    pb2.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                                                    for (Direction direction : Direction.values()) {
                                                        pb2.setDirectionTexture(direction, 0);
                                                    }
                                                }
                                                pb2.setDirectionFaceState(assignedDirection, true);
                                                pb2.setDirectionTexture(assignedDirection, selectedTextureId);

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (availableInstanceTextures.get(selectedTexturePackId) != blockTexture) {
                            block.setInstancedGridTexture(availableInstanceTextures.get(selectedTexturePackId));
                            for (Direction direction : Direction.values()) {
                                block.setDirectionTexture(direction, 0);
                            }
                        }
                        block.setDirectionFaceState(assignedDirection, true);
                        block.setDirectionTexture(assignedDirection, selectedTextureId);
                    }
                }
            }
            else {
                if (world.getBlock(cursorLocation) == null) {
                    PartialCubicBlock block = new PartialCubicBlock(cursorLocation, availableInstanceTextures.get(selectedTexturePackId));
                    world.setBlock(cursorLocation, block);
                    for (Direction direction : Direction.values()) {
                        block.setDirectionFaceState(direction, false);
                    }

                    if (controller.getNorthButton()) {
                        block.setDirectionFaceState(Direction.NORTH, true);
                        block.setDirectionTexture(Direction.NORTH, selectedTextureId);
                    }
                    if (controller.getSouthButton()) {
                        block.setDirectionFaceState(Direction.SOUTH, true);
                        block.setDirectionTexture(Direction.SOUTH, selectedTextureId);
                    }
                    if (controller.getEastButton()) {
                        block.setDirectionFaceState(Direction.EAST, true);
                        block.setDirectionTexture(Direction.EAST, selectedTextureId);
                    }
                    if (controller.getWestButton()) {
                        block.setDirectionFaceState(Direction.WEST, true);
                        block.setDirectionTexture(Direction.WEST, selectedTextureId);
                    }
                    if (controller.getRightBumperButton()) {
                        block.setDirectionFaceState(Direction.ABOVE, true);
                        block.setDirectionTexture(Direction.ABOVE, selectedTextureId);
                    }
                    if (controller.getRightTrigger() >= 0.95) {
                        if (cursorLocation.getY() != 0) {
                            block.setDirectionFaceState(Direction.BELOW, true);
                            block.setDirectionTexture(Direction.BELOW, selectedTextureId);
                        }
                    }

                }
            }
        }
    }



    private void loadTexturePack() {
        textureEditHud.get(0).setSprite(availableInstanceTextures.get(selectedTexturePackId),true);
        textureEditHud.get(1).setSprite(AssetLoader.getTextureAsset("Cursor"), availableInstanceTextures.get(selectedTexturePackId).getCubeSideLength() ,availableInstanceTextures.get(selectedTexturePackId).getCubeSideLength(), true);
        textureCursorX = 0;
        textureCursorY = 0;
        setCursorPosition(0,0);
    }

    private void setCursorPosition(int x, int y) {
        textureEditHud.get(0).setPosition(XOFFSET,YOFFSET,0);
        textureEditHud.get(1).setPosition(XOFFSET + availableInstanceTextures.get(selectedTexturePackId).getCubeSideLength() * x * TEXTURE_SCALING,YOFFSET + availableInstanceTextures.get(selectedTexturePackId).getCubeSideLength() * y * TEXTURE_SCALING,1);
    }
      /*
       int num = ((PartialCubicBlock) block).getAssignedTextures()[i];
       int x = num % instancedGridTexture.getTextureGridWidth();
       int y = num / instancedGridTexture.getTextureGridWidth();
     */



    @Override
    public void render() {
        renderer.prepRender(globalGameData.getGameWindow());

        renderBuffer.bindToRender();
        renderer.clear();
        renderer.updateFrustumCullingFilter(globalGameData.getGameWindow(),camera2,world.getChunks());
        renderer.renderInstancedPartialCubicChunk(globalGameData.getGameWindow(),"Instance3D", camera2, instancedMesh, world.getChunks(), true);
        renderBuffer.unbindToRender(globalGameData.getGameWindow().getWidth(),globalGameData.getGameWindow().getHeight());


        renderer.updateFrustumCullingFilter(globalGameData.getGameWindow(),camera,world.getChunks());
        renderer.render3DElements(globalGameData.getGameWindow(),"FlipDefault3D",camera,gameItems);
        renderer.renderInstancedPartialCubicChunk(globalGameData.getGameWindow(),"Instance3D", camera, instancedMesh, world.getChunks(), true);
        renderer.render3DElements(globalGameData.getGameWindow(),"Glitchy3D",camera, debugItems);
        if(currentEditState == EditState.EDIT_TEXTURE) {
            renderer.renderSprites(globalGameData.getGameWindow(), "Default2D", textureEditHud);
        }
        renderer.renderSprites(globalGameData.getGameWindow(), "Default2D", centerCursor);
        renderer.renderHUD(globalGameData.getGameWindow(),"Default2D",hudItems);

        controller.render(renderer,globalGameData.getGameWindow(),"Default2D");

        ArrayList<PartialCubicBlock> cc = new ArrayList<>();
        cc.add(cursor);
        renderer.renderInstancedPartialCubic(globalGameData.getGameWindow(),"Instance3D", camera, cursorInstancedMesh, cc);



    }


    @Override
    public void enterState(GameStateType previousGameState) {
        globalGameData.getGameWindow().setIcon(AssetLoader.getInputStream("Icon16x16.png"), AssetLoader.getInputStream("Icon24x24.png"));



        File folder = new File(System.getProperty("user.home") + "/Desktop" + "/" + world.getWorldName());

        System.out.println("Loading World " + world.getWorldName());
        folder.mkdir();
        if(folder.exists()) {
            for (File file : folder.listFiles()) {
                String name = file.getName();
                name = name.replace(folder.getName(), "");
                name = name.replace(".wcnk", "");
                String[] cords = name.split("_");

                int x = Integer.parseInt(cords[0]);
                int z = Integer.parseInt(cords[1]);
                ChunkCord cord = new ChunkCord(x, z);

                try {
                    ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                    world.getChunk(cord).readObject(in, new Location(world.getPosNumFromChunkNum(cord.getX()), 0, world.getPosNumFromChunkNum(cord.getZ()), world));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }




        debugCommandRunnable = new DebugCommandRunnable();
        debugCommandThread = new Thread(debugCommandRunnable);
        debugCommandThread.start();


    }

    @Override
    public void exitState(GameStateType nextGameState) {

    }

    @Override
    public void windowClose() {
        for(Chunk chunk: world.getChunks()) {
            File file = new File(System.getProperty("user.home") + "/Desktop" + "/" + world.getWorldName() + "/" + world.getWorldName() + chunk.getChunkCord().getX() + "_" + chunk.getChunkCord().getZ() + ".wcnk");
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
                chunk.writeObject(oos);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        System.exit(0);
    }





    public class DebugCommandRunnable implements Runnable {
        private Scanner scanner = new Scanner(System.in);
        private ArrayList<String> commands = new ArrayList<>();

        @Override
        public void run() {
            while(true) {
                String input = scanner.nextLine();
                synchronized (commands) {
                    commands.add(input);
                }
            }
        }


        public ArrayList<String> getCommands(){
            ArrayList<String> c = null;
            synchronized (commands) {
                c = commands;
                commands = new ArrayList<>();
            }
            return c;
        }
    }




}
