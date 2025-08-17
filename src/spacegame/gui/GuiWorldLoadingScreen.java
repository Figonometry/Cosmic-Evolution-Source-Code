package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.GameSettings;
import spacegame.core.SpaceGame;
import spacegame.core.Timer;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.World;

public final class GuiWorldLoadingScreen extends Gui {
    private SpaceGame sg;
    public int transparentBackground;
    public int fillableColorWithShadedBottom;
    public int star;

    public GuiWorldLoadingScreen(SpaceGame spaceGame) {
        super(spaceGame);
        this.sg = spaceGame;
    }

    @Override
    public void loadTextures() {
        this.transparentBackground = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.fillableColorWithShadedBottom = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColorWithShadedBottom.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.star = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/star.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void deleteTextures() {
        SpaceGame.instance.renderEngine.deleteTexture(this.transparentBackground);
        SpaceGame.instance.renderEngine.deleteTexture(this.fillableColorWithShadedBottom);
        SpaceGame.instance.renderEngine.deleteTexture(this.star);
    }

    @Override
    public void drawGui() {
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        for(int i = 0; i < GuiMainMenu.starCount; i++){
            GuiMainMenu.renderStar(i, tessellator);
        }
        tessellator.drawTexture2D(this.star, Shader.screen2DTexture, SpaceGame.camera);

        int backgroundWidth = SpaceGame.width;
        int backgroundHeight = SpaceGame.height;
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
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int loadingBarX = -512;
        int loadingBarY = 0;
        int loadingBarZ = -50;
        int loadingBarHeight = 16;
        double progress = this.getProgressBarCompletion();
        if(progress > 1){
            progress = 1;
        }
        int loadingBarWidth = (int) (1024 * progress);
        tessellator.addVertex2DTexture(65313, loadingBarX, loadingBarY - loadingBarHeight/2, loadingBarZ, 3);
        tessellator.addVertex2DTexture(65313, loadingBarX + loadingBarWidth, loadingBarY + loadingBarHeight/2, loadingBarZ, 1);
        tessellator.addVertex2DTexture(65313, loadingBarX, loadingBarY + loadingBarHeight/2, loadingBarZ, 2);
        tessellator.addVertex2DTexture(65313, loadingBarX + loadingBarWidth, loadingBarY - loadingBarHeight/2, loadingBarZ, 0);
        tessellator.addElements();

        loadingBarWidth = 1024;
        tessellator.addVertex2DTexture(1925120, loadingBarX, loadingBarY - loadingBarHeight/2, loadingBarZ, 3);
        tessellator.addVertex2DTexture(1925120, loadingBarX + loadingBarWidth, loadingBarY + loadingBarHeight/2, loadingBarZ, 1);
        tessellator.addVertex2DTexture(1925120, loadingBarX, loadingBarY + loadingBarHeight/2, loadingBarZ, 2);
        tessellator.addVertex2DTexture(1925120, loadingBarX + loadingBarWidth, loadingBarY - loadingBarHeight/2, loadingBarZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.fillableColorWithShadedBottom, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
        this.renderLoadingText();
    }

    public double getProgressBarCompletion(){
        switch (World.worldLoadPhase){
            case 0 -> {
                return (double) World.noiseMapsCompleted / (double)World.totalMaps;
            }
            case 1 -> {
                return ((double)(this.sg.save.activeWorld.chunkController.numberOfLoadedChunks / (double)((GameSettings.renderDistance * 2 + 1) * (GameSettings.renderDistance * 2 + 1) * (GameSettings.chunkColumnHeight * 2))));
            }
            case 2 -> {
                return (double) 1 / this.sg.save.activeWorld.chunkController.threadQueue.size();
            }
        }
        return 0;
    }

    public void renderLoadingText(){
        FontRenderer fontRenderer = FontRenderer.instance;
        long second = Timer.elapsedTime / 60;
        second %= 4;
        switch (World.worldLoadPhase){
            case 0 -> {
                switch ((int) second) {
                    case 0 -> fontRenderer.drawCenteredString("Generating NoiseMaps " + World.noiseMapsCompleted + "/" + World.totalMaps, 0, 128,-15, 16777215, 50);
                    case 1 -> fontRenderer.drawCenteredString("Generating NoiseMaps. " + World.noiseMapsCompleted + "/" + World.totalMaps, 0, 128,-15, 16777215, 50);
                    case 2 -> fontRenderer.drawCenteredString("Generating NoiseMaps.. " + World.noiseMapsCompleted + "/" + World.totalMaps, 0, 128,-15, 16777215, 50);
                    case 3 -> fontRenderer.drawCenteredString("Generating NoiseMaps... " + World.noiseMapsCompleted + "/" + World.totalMaps, 0, 128,-15, 16777215, 50);
                }
            }
            case 1 -> {
                switch ((int) second) {
                    case 0 -> fontRenderer.drawCenteredString("Generating World", 0, 128, -15,16777215, 50);
                    case 1 -> fontRenderer.drawCenteredString("Generating World.", 0, 128, -15,16777215, 50);
                    case 2 -> fontRenderer.drawCenteredString("Generating World..", 0, 128,-15, 16777215, 50);
                    case 3 -> fontRenderer.drawCenteredString("Generating World...", 0, 128, -15,16777215, 50);
                }
            }
            case 2 -> {
                switch ((int) second) {
                    case 0 -> fontRenderer.drawCenteredString("Finalizing Chunk Threads: " + this.sg.save.activeWorld.chunkController.threadQueue.size() + " Threads Remaining", 0, 128,-15, 16777215, 50);
                    case 1 -> fontRenderer.drawCenteredString("Finalizing Chunk Threads: " + this.sg.save.activeWorld.chunkController.threadQueue.size() + " Threads Remaining.", 0, 128, -15,16777215, 50);
                    case 2 -> fontRenderer.drawCenteredString("Finalizing Chunk Threads: " + this.sg.save.activeWorld.chunkController.threadQueue.size() + " Threads Remaining..", 0, 128,-15, 16777215, 50);
                    case 3 -> fontRenderer.drawCenteredString("Finalizing Chunk Threads: " + this.sg.save.activeWorld.chunkController.threadQueue.size() + " Threads Remaining...", 0, 128, -15,16777215, 50);
                }
            }
        }
    }

    @Override
    public Button getActiveButton() {
        return null;
    }
}
