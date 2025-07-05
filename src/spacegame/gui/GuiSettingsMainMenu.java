package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;

public final class GuiSettingsMainMenu extends Gui {
    private SpaceGame sg;
    public Button videoSettings;
    public Button keyBinds;
    public Button returnToMainMenu;
    public TextureLoader star;
    public TextureLoader title;
    public TextureLoader earth;
    public TextureLoader background;



    public GuiSettingsMainMenu(SpaceGame spaceGame) {
        super(spaceGame);
        this.sg = spaceGame;
        this.videoSettings = new Button(EnumButtonEffects.VIDEO_SETTINGS.name(), 512, 64, 0, -50, this, this.sg);
        this.keyBinds = new Button(EnumButtonEffects.KEYBINDS.name(), 512, 64, 0, 50, this, this.sg);
        this.returnToMainMenu = new Button(EnumButtonEffects.BACK_TO_MAIN_MENU.name(), 512, 64, 0, -400, this, this.sg);
    }

    @Override
    public void loadTextures() {
        this.star = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/star.png", 64,  64);
        this.title = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/settings.png", 832,144);
        this.earth = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", 512,512);
        this.background = new TextureLoader("src/spacegame/assets/textures/gui/transparentBackground.png", 32,32);
    }

    @Override
    public void deleteTextures() {
        GL46.glDeleteTextures(this.star.texID);
        GL46.glDeleteTextures(this.title.texID);
        GL46.glDeleteTextures(this.earth.texID);
        GL46.glDeleteTextures(this.background.texID);
        this.star = null;
        this.title = null;
        this.earth = null;
        this.background = null;
    }

    @Override
    public void drawGui() {
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        for(int i = 0; i < GuiMainMenu.starCount; i++){
            GuiMainMenu.renderStar(i, tessellator);
        }
        tessellator.drawTexture2D(this.star.texID, Shader.screen2DTexture, SpaceGame.camera);

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
        tessellator.drawTexture2D(this.title.texID, Shader.screen2DTexture, SpaceGame.camera);

        int earthSize = 256;
        int earthX = 0;
        int earthY = 0;
        int earthZ = -900;
        tessellator.addVertex2DTexture(16777215, earthX - earthSize, earthY - earthSize, earthZ, 3);
        tessellator.addVertex2DTexture(16777215, earthX + earthSize, earthY + earthSize, earthZ, 1);
        tessellator.addVertex2DTexture(16777215, earthX - earthSize, earthY + earthSize, earthZ, 2);
        tessellator.addVertex2DTexture(16777215, earthX + earthSize, earthY - earthSize, earthZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.earth.texID, Shader.screen2DTexture, SpaceGame.camera);

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
        tessellator.drawTexture2D(this.background.texID, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();

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
       return null;
    }
}
