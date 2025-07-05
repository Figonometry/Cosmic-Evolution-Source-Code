package spacegame.core;

import org.lwjgl.glfw.GLFW;

public final class KeyListener {
    public static KeyListener instance;
    public static boolean capsLockReleased;
    public static boolean tabReleased;
    public static boolean inventoryKeyReleased;
    public static boolean backSpaceReleased;
    public static boolean capsLockEnabled;
    public static boolean dropKeyReleased;
    public static boolean gKeyReleased;
    public boolean keyPressed[] = new boolean[600];


    public static KeyListener getKeyListener() {
        if (KeyListener.instance == null) {
            KeyListener.instance = new KeyListener();
        }

        return KeyListener.instance;
    }


    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key == -1) { //THIS IS NECESSARY TO PREVENT CRASHES FOR UNKOWN KEYS
            key = 599;
        }

        if (action == GLFW.GLFW_RELEASE) {
            getKeyListener().keyPressed[key] = false;
        } else if (action == GLFW.GLFW_PRESS || action == GLFW.GLFW_REPEAT) {
            getKeyListener().keyPressed[key] = true;
        }

    }

    public static boolean isKeyPressed(int keyCode) {
        return getKeyListener().keyPressed[keyCode];
    }
}

