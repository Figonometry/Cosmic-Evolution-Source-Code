package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public final class GuiSettingsMainMenu extends Gui {
    private CosmicEvolution ce;
    public Button volumeSounds;
    public Button volumeMusic;
    public Button videoSettings;
    public Button keyBinds;
    public Button returnToMainMenu;
    public int star;
    public int title;
    public int earth;
    public int background;



    public GuiSettingsMainMenu(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
        this.ce = cosmicEvolution;
        this.volumeSounds = new Button(EnumButtonEffects.VOLUME_SOUNDS.name(), 512, 64, -300,150, this, this.ce);
        this.volumeMusic = new Button(EnumButtonEffects.VOLUME_MUSIC.name(), 512, 64, 300, 150, this, this.ce);
        this.videoSettings = new Button(EnumButtonEffects.VIDEO_SETTINGS.name(), 512, 64, 0, -150, this, this.ce);
        this.keyBinds = new Button(EnumButtonEffects.KEYBINDS.name(), 512, 64, 0, -50, this, this.ce);
        this.returnToMainMenu = new Button(EnumButtonEffects.BACK_TO_MAIN_MENU.name(), 512, 64, 0, -400, this, this.ce);
    }

    @Override
    public void loadTextures() {
        this.star = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/star.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.title = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/settings.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.earth = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.background = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.star);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.title);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.earth);
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

        this.volumeSounds.renderButton();
        this.volumeMusic.renderButton();
        this.keyBinds.renderButton();
        this.videoSettings.renderButton();
        this.returnToMainMenu.renderButton();
    }

    @Override
    public Button getActiveButton() {
        if(this.videoSettings.isMouseHoveredOver() && this.videoSettings.active){
            return this.videoSettings;
        }
        if(this.keyBinds.isMouseHoveredOver() && this.keyBinds.active){
            return this.keyBinds;
        }
        if(this.returnToMainMenu.isMouseHoveredOver() && this.returnToMainMenu.active){
            return this.returnToMainMenu;
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
