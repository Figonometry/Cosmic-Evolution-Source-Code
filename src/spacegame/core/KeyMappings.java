package spacegame.core;

import org.lwjgl.glfw.GLFW;
import spacegame.gui.TextField;

public abstract class KeyMappings {

    public static String getKeyNameFromMap(){

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
            return "Space";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_APOSTROPHE)){
            return "'";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_COMMA)){
            return ",";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_MINUS)){
            return "-";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_PERIOD)){
            return ".";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SLASH)){
            return "/";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_0)){
            return "0";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_1)){
            return "1";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_2)){
            return "2";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_3)){
            return "3";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_4)){
            return "4";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_5)){
            return "5";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_6)){
            return "6";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_7)){
            return "7";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_8)){
            return "8";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_9)){
            return "9";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SEMICOLON)){
            return ";";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_EQUAL)){
            return "=";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)){
            return "A";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_B)){
            return "B";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_C)){
            return "C";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)){
            return "D";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_E)){
            return "E";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F)){
            return "F";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_G)){
            return "G";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_H)){
            return "H";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_I)){
            return "I";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_J)){
            return "J";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_K)){
            return "K";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_L)){
            return "L";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_M)){
            return "M";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_N)){
            return "N";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_O)){
            return "O";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_P)){
            return "P";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_Q)){
            return "Q";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_R)){
            return "R";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)){
            return "S";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_T)){
            return "T";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_U)){
            return "U";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_V)){
            return "V";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)){
            return "W";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_X)){
            return "X";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_Y)){
            return "Y";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_Z)){
            return "Z";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_BRACKET)){
            return "[";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_BRACKET)){
            return "]";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_GRAVE_ACCENT)){
            return "`";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_ENTER)){
            return "Enter";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_TAB)){
            return "Tab";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_BACKSPACE)){
            return "Backspace";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_INSERT)){
            return "Insert";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_DELETE)){
            return "Delete";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT)){
            return "Right";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT)){
            return "Left";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_DOWN)){
            return "Down";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_UP)){
            return "Up";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_PAGE_UP)){
            return "Page Up";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_PAGE_DOWN)){
            return "Page Down";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_HOME)){
            return "Home";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_END)){
            return "End";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_CAPS_LOCK)){
            return "Caps Lock";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SCROLL_LOCK)){
            return "Scroll Lock";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_NUM_LOCK)){
            return "Num Lock";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_PRINT_SCREEN)){
            return "Print Screen";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_PAUSE)){
            return "Pause";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F1)){
            return "F1";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F2)){
            return "F2";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F3)){
            return "F3";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F4)){
            return "F4";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F5)){
            return "F5";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F6)){
            return "F6";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F7)){
            return "F7";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F8)){
            return "F8";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F9)){
            return "F9";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F10)){
            return "F10";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F11)){
            return "F11";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F12)){
            return "F12";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_0)){
            return "Numpad 0";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_1)){
            return "Numpad 1";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_2)){
            return "Numpad 2";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_3)){
            return "Numpad 3";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_4)){
            return "Numpad 4";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_5)){
            return "Numpad 5";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_6)){
            return "Numpad 6";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_7)){
            return "Numpad 7";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_8)){
            return "Numpad 8";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_9)){
            return "Numpad 9";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_DECIMAL)){
            return "Numpad .";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_DIVIDE)){
            return "Numpad /";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_MULTIPLY)){
            return "Numpad *";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_SUBTRACT)){
            return "Numpad -";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_ADD)){
            return "Numpad +";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_ENTER)){
            return "Numpad Enter";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_EQUAL)){
            return "Numpad Equal";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
            return "Left Shift";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)){
            return "Left Control";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_ALT)){
            return "Left Alt";
        }
        return null;
    }

    public static int getKeyCodeFromMap(int currentKeyCode){

        for(int i = 0; i < GLFW.GLFW_KEY_LAST; i++){
            if(KeyListener.isKeyPressed(i)){
                return i;
            }
        }


        return currentKeyCode;
    }

    public static String getKeyNameFromMapForTextFields(TextField textField){


        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SPACE)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_SPACE;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_SPACE);
            return " ";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_0)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_0;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_0);
            return "0";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_1)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_1;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_1);
            return "1";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_2)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_2;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_2);
            return "2";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_3)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_3;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_3);
            return "3";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_4)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_4;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_4);
            return "4";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_5)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_5;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_5);
            return "5";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_6)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_6;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_6);
            return "6";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_7)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_7;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_7);
            return "7";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_8)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_8;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_8);
            return "8";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_9)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_9;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_9);
            return "9";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_A)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_A;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_A);
            if(KeyListener.capsLockEnabled) {
                return "A";
            } else {
                return "a";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_B)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_B;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_B);
            if(KeyListener.capsLockEnabled) {
                return "B";
            } else {
                return "b";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_C)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_C;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_C);
            if(KeyListener.capsLockEnabled) {
                return "C";
            } else {
                return "c";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_D)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_D;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_D);
            if(KeyListener.capsLockEnabled) {
                return "D";
            } else {
                return "d";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_E)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_E;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_E);
            if(KeyListener.capsLockEnabled) {
                return "E";
            } else {
                return "e";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_F)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_F;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_F);
            if(KeyListener.capsLockEnabled) {
                return "F";
            } else {
                return "f";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_G)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_G;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_G);
            if(KeyListener.capsLockEnabled) {
                return "G";
            } else {
                return "g";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_H)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_H;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_H);
            if(KeyListener.capsLockEnabled) {
                return "H";
            } else {
                return "h";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_I)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_I;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_I);
            if(KeyListener.capsLockEnabled) {
                return "I";
            } else {
                return "i";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_J)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_J;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_J);
            if(KeyListener.capsLockEnabled) {
                return "J";
            } else {
                return "j";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_K)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_K;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_K);
            if(KeyListener.capsLockEnabled) {
                return "K";
            } else {
                return "k";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_L)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_L;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_L);
            if(KeyListener.capsLockEnabled) {
                return "L";
            } else {
                return "l";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_M)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_M;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_M);
            if(KeyListener.capsLockEnabled) {
                return "M";
            } else {
                return "m";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_N)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_N;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_N);
            if(KeyListener.capsLockEnabled) {
                return "N";
            } else {
                return "n";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_O)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_O;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_O);
            if(KeyListener.capsLockEnabled) {
                return "O";
            } else {
                return "o";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_P)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_P;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_P);
            if(KeyListener.capsLockEnabled) {
                return "P";
            } else {
                return "p";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_Q)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_Q;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_Q);
            if(KeyListener.capsLockEnabled) {
                return "Q";
            } else {
                return "q";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_R)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_R;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_R);
            if(KeyListener.capsLockEnabled) {
                return "R";
            } else {
                return "r";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_S)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_S;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_S);
            if(KeyListener.capsLockEnabled) {
                return "S";
            } else {
                return "s";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_T)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_T;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_T);
            if(KeyListener.capsLockEnabled) {
                return "T";
            } else {
                return "t";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_U)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_U;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_U);
            if(KeyListener.capsLockEnabled) {
                return "U";
            } else {
                return "u";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_V)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_V;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_V);
            if(KeyListener.capsLockEnabled) {
                return "V";
            } else {
                return "v";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_W)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_W;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_W);
            if(KeyListener.capsLockEnabled) {
                return "W";
            } else {
                return "w";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_X)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_X;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_X);
            if(KeyListener.capsLockEnabled) {
                return "X";
            } else {
                return "x";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_Y)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_Y;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_Y);
            if(KeyListener.capsLockEnabled) {
                return "Y";
            } else {
                return "y";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_Z)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_Z;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_Z);
            if(KeyListener.capsLockEnabled) {
                return "Z";
            } else {
                return "z";
            }
        }


        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_0)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_0;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_0);
            return "0";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_1)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_1;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_1);
            return "1";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_2)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_2;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_2);
            return "2";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_3)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_3;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_3);
            return "3";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_4)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_4;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_4);
            return "4";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_5)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_5;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_5);
            return "5";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_6)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_6;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_6);
            return "6";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_7)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_7;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_7);
            return "7";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_8)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_8;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_8);
            return "8";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_KP_9)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_KP_9;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_KP_9);
            return "9";
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_SLASH)){
            if(textField.canUseSlash) {
                textField.keyBeingPressed = GLFW.GLFW_KEY_SLASH;
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_SLASH);
                return "/";
            }
        }

        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_MINUS)){
            textField.keyBeingPressed = GLFW.GLFW_KEY_MINUS;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_MINUS);
            return "-";
        }

        return null;
    }

    public static int getKeyCodeFromMap(String keyName, int defaultKeyCode){
        if(keyName != null) {
            if (keyName.equals("Space") || keyName.equals(" ")) {
                return GLFW.GLFW_KEY_SPACE;
            }

            if (keyName.equals("'")) {
                return GLFW.GLFW_KEY_APOSTROPHE;
            }

            if (keyName.equals(",")) {
                return GLFW.GLFW_KEY_COMMA;
            }

            if (keyName.equals("-")) {
                return GLFW.GLFW_KEY_MINUS;
            }

            if (keyName.equals(".")) {
                return GLFW.GLFW_KEY_PERIOD;
            }

            if (keyName.equals("/")) {
                return GLFW.GLFW_KEY_SLASH;
            }

            if (keyName.equals("0")) {
                return GLFW.GLFW_KEY_0;
            }

            if (keyName.equals("1")) {
                return GLFW.GLFW_KEY_1;
            }

            if (keyName.equals("2")) {
                return GLFW.GLFW_KEY_2;
            }

            if (keyName.equals("3")) {
                return GLFW.GLFW_KEY_3;
            }

            if (keyName.equals("4")) {
                return GLFW.GLFW_KEY_4;
            }

            if (keyName.equals("5")) {
                return GLFW.GLFW_KEY_5;
            }

            if (keyName.equals("6")) {
                return GLFW.GLFW_KEY_6;
            }

            if (keyName.equals("7")) {
                return GLFW.GLFW_KEY_7;
            }

            if (keyName.equals("8")) {
                return GLFW.GLFW_KEY_8;
            }

            if (keyName.equals("9")) {
                return GLFW.GLFW_KEY_9;
            }

            if (keyName.equals(";")) {
                return GLFW.GLFW_KEY_SEMICOLON;
            }

            if (keyName.equals("=")) {
                return GLFW.GLFW_KEY_EQUAL;
            }

            if (keyName.equals("A") || keyName.equals("a")) {
                return GLFW.GLFW_KEY_A;
            }

            if (keyName.equals("B") || keyName.equals("b")) {
                return GLFW.GLFW_KEY_B;
            }

            if (keyName.equals("C") || keyName.equals("c")) {
                return GLFW.GLFW_KEY_C;
            }

            if (keyName.equals("D") || keyName.equals("d")) {
                return GLFW.GLFW_KEY_D;
            }

            if (keyName.equals("E") || keyName.equals("e")) {
                return GLFW.GLFW_KEY_E;
            }

            if (keyName.equals("F") || keyName.equals("f")) {
                return GLFW.GLFW_KEY_F;
            }

            if (keyName.equals("G") || keyName.equals("g")) {
                return GLFW.GLFW_KEY_G;
            }

            if (keyName.equals("H") || keyName.equals("h")) {
                return GLFW.GLFW_KEY_H;
            }

            if (keyName.equals("I") || keyName.equals("i")) {
                return GLFW.GLFW_KEY_I;
            }

            if (keyName.equals("J") || keyName.equals("j")) {
                return GLFW.GLFW_KEY_J;
            }

            if (keyName.equals("K") || keyName.equals("k")) {
                return GLFW.GLFW_KEY_K;
            }

            if (keyName.equals("L") || keyName.equals("l")) {
                return GLFW.GLFW_KEY_L;
            }

            if (keyName.equals("M") || keyName.equals("m")) {
                return GLFW.GLFW_KEY_M;
            }

            if (keyName.equals("N") || keyName.equals("n")) {
                return GLFW.GLFW_KEY_N;
            }

            if (keyName.equals("O") || keyName.equals("o")) {
                return GLFW.GLFW_KEY_O;
            }

            if (keyName.equals("P") || keyName.equals("p")) {
                return GLFW.GLFW_KEY_P;
            }

            if (keyName.equals("Q") || keyName.equals("q")) {
                return GLFW.GLFW_KEY_Q;
            }

            if (keyName.equals("R") || keyName.equals("r")) {
                return GLFW.GLFW_KEY_R;
            }

            if (keyName.equals("S") || keyName.equals("s")) {
                return GLFW.GLFW_KEY_S;
            }

            if (keyName.equals("T") || keyName.equals("t")) {
                return GLFW.GLFW_KEY_T;
            }

            if (keyName.equals("U") || keyName.equals("u")) {
                return GLFW.GLFW_KEY_U;
            }

            if (keyName.equals("V") || keyName.equals("v")) {
                return GLFW.GLFW_KEY_V;
            }

            if (keyName.equals("W") || keyName.equals("w")) {
                return GLFW.GLFW_KEY_W;
            }

            if (keyName.equals("X") || keyName.equals("x")) {
                return GLFW.GLFW_KEY_X;
            }

            if (keyName.equals("Y") || keyName.equals("y")) {
                return GLFW.GLFW_KEY_Y;
            }

            if (keyName.equals("Z") || keyName.equals("z")) {
                return GLFW.GLFW_KEY_Z;
            }

            if (keyName.equals("[")) {
                return GLFW.GLFW_KEY_LEFT_BRACKET;
            }

            if (keyName.equals("]")) {
                return GLFW.GLFW_KEY_RIGHT_BRACKET;
            }

            if (keyName.equals("`")) {
                return GLFW.GLFW_KEY_GRAVE_ACCENT;
            }

            if (keyName.equals("Enter")) {
                return GLFW.GLFW_KEY_ENTER;
            }

            if (keyName.equals("Tab")) {
                return GLFW.GLFW_KEY_TAB;
            }

            if (keyName.equals("Backspace")) {
                return GLFW.GLFW_KEY_BACKSPACE;
            }

            if (keyName.equals("Insert")) {
                return GLFW.GLFW_KEY_INSERT;
            }

            if (keyName.equals("Delete")) {
                return GLFW.GLFW_KEY_DELETE;
            }

            if (keyName.equals("Right")) {
                return GLFW.GLFW_KEY_RIGHT;
            }

            if (keyName.equals("Left")) {
                return GLFW.GLFW_KEY_LEFT;
            }

            if (keyName.equals("Down")) {
                return GLFW.GLFW_KEY_DOWN;
            }

            if (keyName.equals("Up")) {
                return GLFW.GLFW_KEY_UP;
            }

            if (keyName.equals("Page Up")) {
                return GLFW.GLFW_KEY_PAGE_UP;
            }

            if (keyName.equals("Page Down")) {
                return GLFW.GLFW_KEY_PAGE_DOWN;
            }

            if (keyName.equals("Home")) {
                return GLFW.GLFW_KEY_HOME;
            }

            if (keyName.equals("End")) {
                return GLFW.GLFW_KEY_END;
            }

            if (keyName.equals("Caps Lock")) {
                return GLFW.GLFW_KEY_CAPS_LOCK;
            }

            if (keyName.equals("Scroll Lock")) {
                return GLFW.GLFW_KEY_SCROLL_LOCK;
            }

            if (keyName.equals("Num Lock")) {
                return GLFW.GLFW_KEY_NUM_LOCK;
            }

            if (keyName.equals("Print Screen")) {
                return GLFW.GLFW_KEY_PRINT_SCREEN;
            }

            if (keyName.equals("Pause")) {
                return GLFW.GLFW_KEY_PAUSE;
            }

            if (keyName.equals("F1")) {
                return GLFW.GLFW_KEY_F1;
            }

            if (keyName.equals("F2")) {
                return GLFW.GLFW_KEY_F2;
            }

            if (keyName.equals("F3")) {
                return GLFW.GLFW_KEY_F3;
            }

            if (keyName.equals("F4")) {
                return GLFW.GLFW_KEY_F4;
            }

            if (keyName.equals("F5")) {
                return GLFW.GLFW_KEY_F5;
            }

            if (keyName.equals("F6")) {
                return GLFW.GLFW_KEY_F6;
            }

            if (keyName.equals("F7")) {
                return GLFW.GLFW_KEY_F7;
            }

            if (keyName.equals("F8")) {
                return GLFW.GLFW_KEY_F8;
            }

            if (keyName.equals("F9")) {
                return GLFW.GLFW_KEY_F9;
            }

            if (keyName.equals("F10")) {
                return GLFW.GLFW_KEY_F10;
            }

            if (keyName.equals("F11")) {
                return GLFW.GLFW_KEY_F11;
            }

            if (keyName.equals("F12")) {
                return GLFW.GLFW_KEY_F12;
            }

            if (keyName.equals("Numpad 0")) {
                return GLFW.GLFW_KEY_KP_0;
            }

            if (keyName.equals("Numpad 1")) {
                return GLFW.GLFW_KEY_KP_1;
            }

            if (keyName.equals("Numpad 2")) {
                return GLFW.GLFW_KEY_KP_2;
            }

            if (keyName.equals("Numpad 3")) {
                return GLFW.GLFW_KEY_KP_3;
            }

            if (keyName.equals("Numpad 4")) {
                return GLFW.GLFW_KEY_KP_4;
            }

            if (keyName.equals("Numpad 5")) {
                return GLFW.GLFW_KEY_KP_5;
            }

            if (keyName.equals("Numpad 6")) {
                return GLFW.GLFW_KEY_KP_6;
            }

            if (keyName.equals("Numpad 7")) {
                return GLFW.GLFW_KEY_KP_7;
            }

            if (keyName.equals("Numpad 8")) {
                return GLFW.GLFW_KEY_KP_8;
            }

            if (keyName.equals("Numpad 9")) {
                return GLFW.GLFW_KEY_KP_9;
            }

            if (keyName.equals("Numpad .")) {
                return GLFW.GLFW_KEY_KP_DECIMAL;
            }

            if (keyName.equals("Numpad /")) {
                return GLFW.GLFW_KEY_KP_DIVIDE;
            }

            if (keyName.equals("Numpad *")) {
                return GLFW.GLFW_KEY_KP_MULTIPLY;
            }

            if (keyName.equals("Numpad -")) {
                return GLFW.GLFW_KEY_KP_SUBTRACT;
            }

            if (keyName.equals("Numpad +")) {
                return GLFW.GLFW_KEY_KP_ADD;
            }

            if (keyName.equals("Numpad Enter")) {
                return GLFW.GLFW_KEY_KP_ENTER;
            }

            if (keyName.equals("Numpad =")) {
                return GLFW.GLFW_KEY_KP_EQUAL;
            }

            if (keyName.equals("Left Shift")) {
                return GLFW.GLFW_KEY_LEFT_SHIFT;
            }

            if (keyName.equals("Left Control")) {
                return GLFW.GLFW_KEY_LEFT_CONTROL;
            }

            if (keyName.equals("Left Alt")) {
                return GLFW.GLFW_KEY_LEFT_ALT;
            }
        }
        return defaultKeyCode;
    }
}
