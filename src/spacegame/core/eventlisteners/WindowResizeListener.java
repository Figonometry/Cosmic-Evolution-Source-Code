package spacegame.core.eventlisteners;

import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.Shader;

public final class WindowResizeListener {

    public static void resizeCallback(long window, int screenWidth, int screenHeight) {
        CosmicEvolution.setWidth(screenWidth);
        CosmicEvolution.setHeight(screenHeight);


        GL46.glViewport(0, 0, screenWidth, screenHeight);

        //CosmicEvolution.camera.readjustProjectionMatrices();

        //Shader.toolTipShader.uploadInt("width", screenWidth);
        //Shader.toolTipShader.uploadInt("height", screenHeight);
    }


}
