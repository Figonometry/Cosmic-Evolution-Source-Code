package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public final class GuiPauseInGame extends Gui {
    public SpaceGame sg;
    public Button back;
    public Button settings;
    public Button quit;
    public int title;
    public int background;

    public GuiPauseInGame(SpaceGame spaceGame) {
        super(spaceGame);
        this.sg = spaceGame;
        this.back = new Button(EnumButtonEffects.BACK_TO_GAME.name(), 512, 64, 0, 150, this, this.sg);
        this.settings = new Button(EnumButtonEffects.SETTINGS.name(), 512, 64, 0, 0, this, this.sg);
        this.quit = new Button(EnumButtonEffects.QUIT_TO_MAIN_MENU.name(), 512, 64, 0, -150, this, this.sg);
    }

    @Override
    public void loadTextures() {
        this.title = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/pause.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.background = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void deleteTextures() {
        SpaceGame.instance.renderEngine.deleteTexture(this.title);
        SpaceGame.instance.renderEngine.deleteTexture(this.background);
    }

    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        int titleWidth = 546;
        int titleHeight = 144;
        int titleX = 0;
        int titleY = 400;
        int titleZ = -50;
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth/2, titleY - titleHeight/2, titleZ, 3);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth/2, titleY + titleHeight/2, titleZ, 1);
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth/2, titleY + titleHeight/2, titleZ, 2);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth/2, titleY - titleHeight/2, titleZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.title, Shader.screen2DTexture, SpaceGame.camera);

        int backgroundWidth = 1920;
        int backgroundHeight = 1017;
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
        tessellator.drawTexture2D(this.background, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();

        this.back.renderButton();
        this.quit.renderButton();
        this.settings.renderButton();
    }

    @Override
    public Button getActiveButton() {
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        } else if(this.quit.isMouseHoveredOver() && this.quit.active){
            return this.quit;
        } else if(this.settings.isMouseHoveredOver() && this.settings.active){
            return this.settings;
        } else
        return null;
    }
}
