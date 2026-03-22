package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.core.Timer;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.ThreadDeleteWorld;

public final class GuiDeletingWorld extends Gui {
    public int background;
    public int title;
    public int star;
    public int earth;
    public ThreadDeleteWorld associatedThread;
    public GuiDeletingWorld(CosmicEvolution cosmicEvolution, ThreadDeleteWorld associatedThread) {
        super(cosmicEvolution);
        this.associatedThread = associatedThread;
    }

    @Override
    public void loadTextures() {
        this.star = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/star.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.earth = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.title = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/deleteWorld.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.background = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.star);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.earth);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.title);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.background);
    }

    @Override
    public void drawGui() {
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        for(int i = 0; i < GuiMainMenu.starCount; i++){
            GuiMainMenu.renderStar(i, tessellator);
        }
        tessellator.drawTexture2D(this.star, Shader.screen2DTexture, CosmicEvolution.camera);

        int titleWidth = 1204;
        int titleHeight = 144;
        int titleX = 0;
        int titleY = 400;
        int titleZ = -50;
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth/2, titleY - titleHeight/2, titleZ, 3);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth/2, titleY + titleHeight/2, titleZ, 1);
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth/2, titleY + titleHeight/2, titleZ, 2);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth/2, titleY - titleHeight/2, titleZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.title, Shader.screen2DTexture, CosmicEvolution.camera);

        int earthSize = 256;
        int earthX = 0;
        int earthY = 0;
        int earthZ = -900;
        tessellator.addVertex2DTexture(16777215, earthX - earthSize, earthY - earthSize, earthZ, 3);
        tessellator.addVertex2DTexture(16777215, earthX + earthSize, earthY + earthSize, earthZ, 1);
        tessellator.addVertex2DTexture(16777215, earthX - earthSize, earthY + earthSize, earthZ, 2);
        tessellator.addVertex2DTexture(16777215, earthX + earthSize, earthY - earthSize, earthZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.earth, Shader.screen2DTexture, CosmicEvolution.camera);

        int backgroundWidth = CosmicEvolution.width;
        int backgroundHeight = CosmicEvolution.height;
        int backgroundX = 0;
        int backgroundY = 0;
        int backgroundZ = -100;
        tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.background, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        tessellator.toggleOrtho();

        FontRenderer fontRenderer = FontRenderer.instance;


        long second = Timer.elapsedTime / 60;
        second %= 4;

        String text = "";
        switch ((int) second){
            case 0 -> {
                text = "Deleting World";
            }
            case 1 -> {
                text = "Deleting World.";
            }
            case 2 -> {
                text = "Deleting World..";
            }
            case 3 -> {
                text = "Deleting World...";
            }
        }
        fontRenderer.drawCenteredString(text, 0, 0,-15, 16777215, 50, 255);
    }

    @Override
    public Button getActiveButton() {
        return null;
    }
}
