package spacegame.core;

import org.lwjgl.opengl.GL46;

public final class WindowResizeListener {

    public static void resizeCallback(long window, int screenWidth, int screenHeight) {
        SpaceGame.setWidth(screenWidth);
        SpaceGame.setHeight(screenHeight);


        GL46.glViewport(0, 0, screenWidth, screenHeight);
    }


}
