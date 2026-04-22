package spacegame.core;

import org.lwjgl.glfw.GLFW;
import spacegame.gui.TextField;

public final class CharListener {


    public static void charCallBack(long window, int codePoint){
        TextField textField = CosmicEvolution.instance.currentlySelectedField;
        if(textField != null){
            if(textField.typing){
                if(textField.text.length() >= textField.lineCharLimit)return;

                char[] chars =  Character.toChars(codePoint);

                if(!textField.canUseSlash && chars[0] == '/')return;

                textField.text = textField.text + chars[0];
            }

        }
    }


}
