package spacegame.core;

import org.lwjgl.glfw.GLFW;
import spacegame.block.Block;

import java.io.*;

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
    public static boolean transparentLeaves;
    public static KeyBinding keyBeingModified;
    public static KeyBinding forwardKey = new KeyBinding("Forward", "W", GLFW.GLFW_KEY_W);
    public static KeyBinding backwardKey = new KeyBinding("Backward", "S", GLFW.GLFW_KEY_S);
    public static KeyBinding leftKey = new KeyBinding("Left", "A", GLFW.GLFW_KEY_A);
    public static KeyBinding rightKey = new KeyBinding("Right", "D", GLFW.GLFW_KEY_D);
    public static KeyBinding jumpKey = new KeyBinding("Jump", "Space", GLFW.GLFW_KEY_SPACE);
    public static KeyBinding inventoryKey = new KeyBinding("Inventory", "E", GLFW.GLFW_KEY_E);
    public static KeyBinding dropKey = new KeyBinding("Drop", "Q", GLFW.GLFW_KEY_Q);

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


    public static void increaseVolume(){
        volume += 0.01F;
        if(volume >= 1){
            volume = 1;
        }
    }

    public static void decreaseVolume(){
        volume -= 0.01F;
        if(volume <= 0){
            volume = 0;
        }
    }

    public static void increaseMusicVolume(){
        musicVolume += 0.01F;
        if(musicVolume >= 1){
            musicVolume = 1;
        }
    }

    public static void decreaseMusicVolume(){
        musicVolume -= 0.01F;
        if(musicVolume <= 0){
            musicVolume = 0;
        }
    }

    public static void increaseFOV(){
        fov += 0.01F;
        if(fov >= 1.3F){
            fov = 1.3F;
        }
    }

    public static void decreaseFOV(){
        fov -= 0.01F;
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


    public static void increaseSensitivity(){
        sensitivity += 0.01F;
        if(sensitivity >= 2){
            sensitivity = 2;
        }
    }

    public static void decreaseSensitivity(){
        sensitivity -= 0.01F;
        if(sensitivity <= 0){
            sensitivity = 0;
        }
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
}

