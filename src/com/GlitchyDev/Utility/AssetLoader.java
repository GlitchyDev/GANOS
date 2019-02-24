package com.GlitchyDev.Utility;

import com.GlitchyDev.Rendering.Assets.InstancedGridTexture;
import com.GlitchyDev.Rendering.Assets.Mesh;
import com.GlitchyDev.Rendering.Assets.Sounds.SoundBuffer;
import com.GlitchyDev.Rendering.Assets.Sounds.SoundManager;
import com.GlitchyDev.Rendering.Assets.Texture;
import com.GlitchyDev.Rendering.Assets.Utils;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An Static AssetLoader to draw Assets from inside the Jar, initialize and load them, and make them available for use
 */
// In the future, allow for the loading of specific assets to be done in order, rather than a load all
public class AssetLoader {

    private static SoundManager soundManager = new SoundManager();
    private static HashMap<String,String> allAssets = new HashMap<>();
    private static HashMap<String,Texture> textureAssets = new HashMap<>();
    private static HashMap<String,InstancedGridTexture> instancedGridTextures = new HashMap<>();
    private static HashMap<String,SoundBuffer> soundAssets = new HashMap<>();
    private static HashMap<String,Mesh> meshAssets = new HashMap<>();
    private static HashMap<String,String> vertexShaderAssets = new HashMap<>();
    private static HashMap<String,String> fragmentShaderAssets = new HashMap<>();
    private static HashMap<String,HashMap<String,String>> configOptionAssets = new HashMap<>();
    private static HashMap<String,ArrayList<String>> configListAssets = new HashMap<>();


    private static boolean isLoadedFromJar;




    public static void loadAssets() throws Exception {
        System.out.println("AssetLoader: Loading Assets");
        soundManager.init();
        long startTime = System.currentTimeMillis();
        detectIfJar();
        if(!isLoadedFromJar) {
            updateAssetRegistry();
        }

        System.out.println("AssetLoader: Initialing Assets");
        InputStream is = AssetLoader.class.getResourceAsStream("/GameAssets/Configs/General/AssetRegistry.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        while(br.ready())
        {
            String path = br.readLine();
            loadAsset(path);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("AssetLoader: Assets Created in " + (endTime - startTime)/1000.0 + "s");



    }

    private static void loadAsset(String fileName) throws Exception {

        Path path = Paths.get(fileName);
        String filePath = fileName.replace("\\","/").replace("GameAssets","");
        String fullName = path.getFileName().toString();
        String fileType = fullName.split("\\.")[1];
        String name = fullName.replace("." + fileType,"");
        allAssets.put(fullName,filePath);
        System.out.println("AssetLoader: Loading Asset " + name + " of type " + fileType);
        switch(fileType)
        {
            case "obj":
                meshAssets.put(name,loadMesh(filePath));
                break;
            case "png":
                textureAssets.put(name,loadTexture(filePath));
                break;
            case "ogg":
                System.out.println(filePath);
                soundAssets.put(name,loadSound(filePath));
                break;
            case "vs":
                vertexShaderAssets.put(name,loadShader(filePath));
                break;
            case "fs":
                fragmentShaderAssets.put(name,loadShader(filePath));
                break;
            case "configOptions":
                configOptionAssets.put(name,loadConfigOptions(filePath));
                break;
            case "configList":
                configListAssets.put(name,loadConfigList(filePath));
                break;
        }


    }


    private static void updateAssetRegistry() throws IOException {
        System.out.println("AssetLoader: Updating Asset Registry");

        File sourceFolder = new File("GameAssets");

        File assetRegistry = new File("GameAssets/Configs/General/AssetRegistry.txt");
        PrintWriter writer = new PrintWriter(assetRegistry);

        processFileToAssetRegistry(writer,sourceFolder);
        writer.close();
    }

    private static void processFileToAssetRegistry(PrintWriter writer, File file) {
        if(file.isDirectory())
        {
            for(File subFiles: file.listFiles())
            {
                processFileToAssetRegistry(writer,subFiles);
            }
        }
        else
        {
            if(!file.getName().equals("AssetRegistry.txt")) {
                System.out.println("AssetLoader: Registered Asset " + file.getName());
                writer.println(file.getPath());
            }
        }
    }

    private static void detectIfJar()
    {
        isLoadedFromJar = AssetLoader.class.getResource("AssetLoader.class").toString().contains("jar");
        if(isLoadedFromJar) {
            System.out.println("AssetLoader: Loading from Jar");
        }
        else
        {
            System.out.println("AssetLoader: Loading Assets from IDE, update AssetRegistry");
        }
    }

    public static InputStream getInputStream(String fileName)
    {
        return AssetLoader.class.getResourceAsStream(allAssets.get(fileName));
    }

    public static Texture getTextureAsset(String name)
    {
        return textureAssets.get(name);
    }


    public static SoundBuffer getSoundAsset(String name) { return soundAssets.get(name);}

    public static Mesh getMeshAsset(String name)
    {
        return meshAssets.get(name).clone();
    }

    public static void registerInstanceGridTexture(Texture texture, String name, int width, int length) {
        instancedGridTextures.put(name,new InstancedGridTexture(texture,name,width,length));
    }

    public static InstancedGridTexture getInstanceGridTexture(String name) {
        return instancedGridTextures.get(name);
    }

    public static String getVertexAsset(String name)
    {
        return vertexShaderAssets.get(name);
    }

    public static String getFragmentAsset(String name)
    {
        return fragmentShaderAssets.get(name);
    }

    public static HashMap<String,String> getConfigOptionAsset(String name)
    {
        return configOptionAssets.get(name);
    }

    public static ArrayList<String> getConfigListAsset(String name)
    {
        return configListAssets.get(name);
    }

    public static Mesh loadMesh(String filePath)
    {
        return OBJLoader.loadMesh(AssetLoader.class.getResourceAsStream(filePath));
    }

    public static SoundBuffer loadSound(String filePath)
    {
        try {
            return new SoundBuffer(AssetLoader.class.getResourceAsStream(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Texture loadTexture(String filePath)
    {
        return new Texture(AssetLoader.class.getResourceAsStream(filePath));
    }

    public static String loadShader(String filePath)
    {
        try {
            return Utils.loadResource(AssetLoader.class.getResourceAsStream(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HashMap<String,String> loadConfigOptions(String filePath) throws IOException {
        BufferedReader configOptionReader = new BufferedReader(new InputStreamReader(AssetLoader.class.getResourceAsStream(filePath)));
        HashMap<String,String> configMap = new HashMap<>();
        while(configOptionReader.ready())
        {
            String line = configOptionReader.readLine();
            String[] configList = line.trim().replace(" ","").split(":");
            configMap.put(configList[0],configList[1]);
        }
        return configMap;
    }

    public static ArrayList<String> loadConfigList(String filePath) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(AssetLoader.class.getResourceAsStream(filePath)));
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        return list;
    }


}
