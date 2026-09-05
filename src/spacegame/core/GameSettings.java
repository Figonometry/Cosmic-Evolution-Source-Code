package spacegame.core;

import org.lwjgl.glfw.GLFW;
import spacegame.block.Block;
import spacegame.core.eventlisteners.KeyListener;

import java.io.*;
import java.util.regex.Pattern;

public abstract class GameSettings {
    public static int renderDistance = 8;
    public static int chunkColumnHeight = 9;
    private static File options;
    public static float volume = 1F;
    public static float musicVolume = 1F;
    public static float fov = 0.7F;
    public static float sensitivity = 1F;
    public static boolean fullscreen = false;
    public static boolean showFPS = false;
    public static boolean vsync = false;
    public static boolean invertMouse = false;
    public static boolean shadowMap = true;
    public static boolean viewBob = true;
    public static boolean wavyLeaves = true;
    public static boolean wavyWater = true;
    public static boolean transparentLeaves = false;
    public static boolean blockTooltips = true;
    public static String assetPackPath = "Default:null";
    public static boolean usingDefaultAssets;
    public static boolean dynamicLights = false;
    public static KeyBinding keyBeingModified;
    public static KeyBinding forwardKey = new KeyBinding("Forward", "W", GLFW.GLFW_KEY_W);
    public static KeyBinding backwardKey = new KeyBinding("Backward", "S", GLFW.GLFW_KEY_S);
    public static KeyBinding leftKey = new KeyBinding("Left", "A", GLFW.GLFW_KEY_A);
    public static KeyBinding rightKey = new KeyBinding("Right", "D", GLFW.GLFW_KEY_D);
    public static KeyBinding jumpKey = new KeyBinding("Jump", "Space", GLFW.GLFW_KEY_SPACE);
    public static KeyBinding inventoryKey = new KeyBinding("Inventory", "E", GLFW.GLFW_KEY_E);
    public static KeyBinding dropKey = new KeyBinding("Drop", "Q", GLFW.GLFW_KEY_Q);
    public static KeyBinding shiftKey = new KeyBinding("Crouch", "SHIFT", GLFW.GLFW_KEY_LEFT_SHIFT);
    public static KeyBinding sprintKey = new KeyBinding("Sprint", "CAPS", GLFW.GLFW_KEY_CAPS_LOCK);
    public static KeyBinding sitKey = new KeyBinding("Sit", "G", GLFW.GLFW_KEY_G);

    public static void loadOptionsFromFile(File directory){
        File optionsFile = new File(directory + "/options.txt");
        if(!optionsFile.exists()){
            try {
                optionsFile.createNewFile();
                options = optionsFile;
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(optionsFile));
                String line = "";
                while((line = reader.readLine()) != null){
                    String[] options = line.split(":");
                    if(options[0].equals("fullscreen")){
                        fullscreen = options[1].equals("true");
                    }
                    if(options[0].equals("showFPS")){
                        showFPS = options[1].equals("true");
                    }
                    if(options[0].equals("vsync")){
                        vsync = options[1].equals("true");
                    }
                    if(options[0].equals("invertMouse")){
                        invertMouse = options[1].equals("true");
                    }
                    if(options[0].equals("volume")){
                        volume = parseFloat(options[1]);
                    }
                    if(options[0].equals("musicVolume")){
                        musicVolume = parseFloat(options[1]);
                    }
                    if(options[0].equals("fov")){
                        fov = parseFloat(options[1]);
                        if(fov > 1.3F){
                            fov = 1.3F;
                        }
                        if(fov < 0.3F){
                            fov = 0.3F;
                        }
                    }
                    if(options[0].equals("sensitivity")){
                        sensitivity = parseFloat(options[1]);
                    }
                    if(options[0].equals("renderDistance")){
                        renderDistance = parseInt(options[1]);
                    }
                    if(options[0].equals("chunkColumnHeight")){
                        chunkColumnHeight = parseInt(options[1]);
                    }
                    if(options[0].equals("shadowMap")){
                        shadowMap = options[1].equals("true");
                    }
                    if(options[0].equals("viewBob")){
                        viewBob = options[1].equals("true");
                    }
                    if(options[0].equals("wavyWater")){
                        wavyWater = options[1].equals("true");
                        Block.water.canGreedyMesh = !wavyWater;
                    }
                    if(options[0].equals("wavyLeaves")){
                        wavyLeaves = options[1].equals("true");
                        Block.leaf.canGreedyMesh = !wavyLeaves;
                    }
                    if(options[0].equals("transparentLeaves")){
                        transparentLeaves = options[1].equals("true");
                    }
                    if(options[0].equals("blockTooltips")){
                        blockTooltips = options[1].equals("true");
                    }
                    if(options[0].equals("assetPackPath")){
                        String filepath = CosmicEvolution.instance.osName.equals("win") ? options[2] : options[1];

                        String assetPacksFilepath = CosmicEvolution.instance.launcherFilepath + File.separator + "assetPacks";
                        String[] pathContents = filepath.split(Pattern.quote(File.separator));

                        String packName = pathContents[pathContents.length - 1];

                        assetPackPath = assetPacksFilepath + File.separator + packName;

                        usingDefaultAssets = packName.equals("Default");
                    }
                    if(options[0].equals("dynamicLights")){
                        dynamicLights = options[1].equals("true");
                    }
                    if(options[0].equals("forwardKey")){
                        forwardKey.key = options[1];
                        forwardKey.keyCode = KeyMappings.getKeyCodeFromMap(forwardKey.key, forwardKey.keyCode);
                    }
                    if(options[0].equals("backwardKey")){
                        backwardKey.key = options[1];
                        backwardKey.keyCode = KeyMappings.getKeyCodeFromMap(backwardKey.key, backwardKey.keyCode);
                    }
                    if(options[0].equals("leftKey")){
                        leftKey.key = options[1];
                        leftKey.keyCode = KeyMappings.getKeyCodeFromMap(leftKey.key, leftKey.keyCode);
                    }
                    if(options[0].equals("rightKey")){
                        rightKey.key = options[1];
                        rightKey.keyCode = KeyMappings.getKeyCodeFromMap(rightKey.key, rightKey.keyCode);
                    }
                    if(options[0].equals("jumpKey")){
                        jumpKey.key = options[1];
                        jumpKey.keyCode = KeyMappings.getKeyCodeFromMap(jumpKey.key, jumpKey.keyCode);
                    }
                    if(options[0].equals("inventoryKey")){
                        inventoryKey.key = options[1];
                        inventoryKey.keyCode = KeyMappings.getKeyCodeFromMap(inventoryKey.key, inventoryKey.keyCode);
                    }
                    if(options[0].equals("dropKey")){
                        dropKey.key = options[1];
                        dropKey.keyCode = KeyMappings.getKeyCodeFromMap(dropKey.key, dropKey.keyCode);
                    }
                }
            } catch (FileNotFoundException e) {
                System.out.println("UNABLE TO LOAD OPTIONS FILE");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        options = optionsFile;
    }

    public static void saveOptions(){
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(options));
            writer.println("fullscreen:" + fullscreen);
            writer.println("showFPS:" + showFPS);
            writer.println("vsync:" + vsync);
            writer.println("invertMouse:" + invertMouse);
            writer.println("volume:" + volume);
            writer.println("musicVolume:" + musicVolume);
            writer.println("fov:" + fov);
            writer.println("sensitivity:" + sensitivity);
            writer.println("renderDistance:" + renderDistance);
            writer.println("chunkColumnHeight:" + chunkColumnHeight);
            writer.println("shadowMap:" + shadowMap);
            writer.println("viewBob:" + viewBob);
            writer.println("wavyWater:" + wavyWater);
            writer.println("wavyLeaves:" + wavyLeaves);
            writer.println("transparentLeaves:" + transparentLeaves);
            writer.println("assetPackPath:" + assetPackPath);
            writer.println("dynamicLights:" + dynamicLights);
            writer.println("forwardKey:" + forwardKey.key);
            writer.println("backwardKey:" + backwardKey.key);
            writer.println("leftKey:" + leftKey.key);
            writer.println("rightKey:" + rightKey.key);
            writer.println("jumpKey:" + jumpKey.key);
            writer.println("inventoryKey:" + inventoryKey.key);
            writer.println("dropKey:" + dropKey.key);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static float parseFloat(String value){
        return Float.parseFloat(value);
    }

    private static int parseInt(String value){
        return Integer.parseInt(value);
    }


    public static void setVolumeFromSlider(float volumeValue){
        volume = volumeValue;
    }


    public static void setMusicFromSlider(float musicValue){
        musicVolume = musicValue;
    }

    public static void setFOVFromSlider(float fovValue){
        //Should range from 0 to 1
        fov = 0.3f + fovValue;
        if(fov >= 1.3F){
            fov = 1.3F;
        }

        if(fov <= 0.3F){
            fov = 0.3F;
        }
    }

    public static void changeHorizontalViewDistance(boolean increase){
        renderDistance = increase ? renderDistance + 1 : renderDistance - 1;
        renderDistance = renderDistance > 20 ? 20 : renderDistance;
        renderDistance = renderDistance < 4 ? 4 : renderDistance;
    }

    public static void changeVerticalViewDistance(boolean increase){
        chunkColumnHeight = increase ? chunkColumnHeight + 1 : chunkColumnHeight - 1;
        chunkColumnHeight = chunkColumnHeight > 10 ? 10 : chunkColumnHeight;
        chunkColumnHeight = chunkColumnHeight < 5 ? 5 : chunkColumnHeight;
    }


    public static void setSensitivityFromSlider(float value){
        sensitivity = value;
    }


    public static KeyBinding getKeyBeingModified(){
        if(keyBeingModified == null){
            return null;
        }
        if(keyBeingModified.equals(forwardKey)){
            return forwardKey;
        }
        if(keyBeingModified.equals(backwardKey)){
            return backwardKey;
        }
        if(keyBeingModified.equals(leftKey)){
            return leftKey;
        }
        if(keyBeingModified.equals(rightKey)){
            return rightKey;
        }
        if(keyBeingModified.equals(jumpKey)){
            return jumpKey;
        }
        if(keyBeingModified.equals(inventoryKey)){
            return inventoryKey;
        }
        if(keyBeingModified.equals(dropKey)){
            return dropKey;
        }

        return null;
    }


    public static void setKeyBeingModified(KeyBinding modifiedKey){
        keyBeingModified = modifiedKey;
    }

    public static void switchKeyBinds(){
        KeyBinding key = getKeyBeingModified();
        String result;
        if(key != null){
            result = KeyMappings.getKeyNameFromMap();
            if(result == null){
                return;
            } else {
                key.keyCode = KeyMappings.getKeyCodeFromMap(key.keyCode);
                key.key = result;
                keyBeingModified = null;
                saveOptions();
            }
        }
    }

    public static void setAssetPackPath(String assetPackPath1){
        assetPackPath = assetPackPath1;
        String[] pathContents = assetPackPath.split(Pattern.quote(File.separator));
        String title = pathContents[pathContents.length - 1];

        usingDefaultAssets = title.equals("Default");
    }
}

