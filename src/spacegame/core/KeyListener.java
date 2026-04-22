package spacegame.core;

import org.lwjgl.glfw.GLFW;
import spacegame.gui.CommandParser;
import spacegame.gui.GuiCommandEntry;
import spacegame.gui.TextField;

import java.util.Arrays;

public final class KeyListener {
    public static boolean capsLockEnabled; //This special boolean is for typing strings out in text boxes
    public static boolean[] keyPressed = new boolean[600];
    public static boolean[] keyReleased = new boolean[600];
    private static long timePressedBackSpace = 0;

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

       TextField textField = CosmicEvolution.instance.currentlySelectedField;

        if(textField != null){
            if(textField.typing){
                if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE) && textField.text.length() > 0 && (timePressedBackSpace == 0 || System.currentTimeMillis() >= timePressedBackSpace + 500L)) {
                    char[] newTextCharacters = new char[textField.text.length() - 1];
                    for (int i = 0; i < textField.text.length() - 1; i++) {
                        newTextCharacters[i] = textField.text.charAt(i);
                    }
                    textField.text = new String(newTextCharacters);
                    if(keyReleased[GLFW.GLFW_KEY_BACKSPACE] || timePressedBackSpace == 0){
                        timePressedBackSpace = System.currentTimeMillis();
                    }
                    setKeyReleased(GLFW.GLFW_KEY_BACKSPACE);
                }

                if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_ENTER) && CosmicEvolution.instance.currentGui instanceof GuiCommandEntry && KeyListener.keyReleased[GLFW.GLFW_KEY_ENTER]){
                    new CommandParser().parseCommand(textField.text);
                    textField.text = "";
                    setKeyReleased(GLFW.GLFW_KEY_ENTER);
                }
            }
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

