package spacegame.core;

import org.lwjgl.glfw.GLFW;

import java.util.Arrays;

public final class KeyListener {
    public static boolean capsLockEnabled; //This special boolean is for typing strings out in text boxes
    public static boolean[] keyPressed = new boolean[600];
    public static boolean[] keyReleased = new boolean[600];

    static {
        Arrays.fill(keyReleased, true);
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        key = key == -1 ? 599 : key; //This is needed to prevent crashes from unmapped keys in GLFW

        if (action == GLFW.GLFW_RELEASE) {
            keyPressed[key] = false;
        } else if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
            keyPressed[key] = true;
        }
    }

    public static void setKeyReleased(int keyCode){ //Marks a key as not released, this prevents executions of a code block over multiple tick cycles when it should only run once during that tick
        keyReleased[keyCode] = false;
    }

    public static void checkIfKeysArePressed(){ //Checks if keys are not pressed and sets their state to released
        for(int i = 0; i < keyReleased.length; i++){
            if(!isKeyPressed(i)){
                keyReleased[i] = true;
            }
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return keyPressed[keyCode];
    }
}

