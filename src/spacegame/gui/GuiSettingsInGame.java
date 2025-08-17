package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public final class GuiSettingsInGame extends Gui {
    private SpaceGame sg;
    public Button volumeSounds;
    public Button volumeMusic;
    public Button videoSettings;
    public Button keyBinds;
    public Button back;
    public int title;
    public int background;

    public GuiSettingsInGame(SpaceGame spaceGame) {
        super(spaceGame);
        this.sg = spaceGame;
        this.volumeSounds = new Button(EnumButtonEffects.VOLUME_SOUNDS.name(), 512, 64, -300,150, this, this.sg);
        this.volumeMusic = new Button(EnumButtonEffects.VOLUME_MUSIC.name(), 512, 64, 300, 150, this, this.sg);
        this.videoSettings = new Button(EnumButtonEffects.VIDEO_SETTINGS.name(), 512, 64, 0, -150, this, this.sg);
        this.keyBinds = new Button(EnumButtonEffects.KEYBINDS.name(), 512, 64, 0, -50, this, this.sg);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 0, -400, this, this.sg);
    }

    @Override
    public void loadTextures() {
        this.title = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/settings.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.background = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void deleteTextures() {
        SpaceGame.instance.renderEngine.deleteTexture(this.title);
        SpaceGame.instance.renderEngine.deleteTexture(this.background);
    }

    @Override
    public void drawGui() {
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        int titleWidth = 782;
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

        this.volumeSounds.renderButton();
        this.volumeMusic.renderButton();
        this.keyBinds.renderButton();
        this.videoSettings.renderButton();
        this.back.renderButton();

        GuiInGame.renderText();
    }

    @Override
    public Button getActiveButton() {
        if(this.videoSettings.isMouseHoveredOver() && this.videoSettings.active){
            return this.videoSettings;
        }
        if(this.keyBinds.isMouseHoveredOver() && this.keyBinds.active){
            return this.keyBinds;
        }
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        }
        if(this.volumeSounds.isMouseHoveredOver() && this.volumeSounds.active){
            return this.volumeSounds;
        }
        if(this.volumeMusic.isMouseHoveredOver() && this.volumeMusic.active){
            return this.volumeMusic;
        }
        return null;
    }
}
