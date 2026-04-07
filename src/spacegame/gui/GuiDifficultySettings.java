package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.SaveSettings;

public final class GuiDifficultySettings extends Gui {
    public Button back;
    public Button saveOptions;
    public Button dropInventoryOnDeath;
    public Button button2;
    public Button button3;
    public Button button4;
    public Button button5;
    public Button button6;
    public Button button7;
    public Button button8;
    public Button testingMode;
    public SaveSettings saveSettings;
    public int background;
    public int title;
    public int star;
    public int earth;
    public boolean fromWorldCreateScreen;
    public int saveSlot;
    public GuiDifficultySettings(CosmicEvolution cosmicEvolution, boolean fromWorldCreateScreen, int currentSaveSlot, SaveSettings saveSettings) {
        super(cosmicEvolution);
        this.fromWorldCreateScreen = fromWorldCreateScreen;
        this.saveOptions = new Button(EnumButtonEffects.SAVE_OPTIONS.name(), 512, 64, -320, -400, this, this.ce);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 320, -400, this, this.ce);
        this.dropInventoryOnDeath = new Button(EnumButtonEffects.DROP_INVENTORY_ON_DEATH.name(), 512, 64, 0, 150, this, this.ce);
        this.testingMode = new Button(EnumButtonEffects.TESTING_MODE.name(), 512, 64, 0, 0, this, this.ce);
        this.saveSlot = currentSaveSlot;
        this.saveSettings = saveSettings;
    }

    @Override
    public void loadTextures() {
        this.star = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/star.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.earth = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.title = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/createNewWorld.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
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


        this.saveOptions.renderButton();
        this.back.renderButton();
        this.dropInventoryOnDeath.renderButton();
        this.testingMode.renderButton();
    }

    @Override
    public Button getActiveButton() {
        if(this.saveOptions.isMouseHoveredOver() && this.saveOptions.active){
            return this.saveOptions;
        }
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        }
        if(this.dropInventoryOnDeath.isMouseHoveredOver() && this.dropInventoryOnDeath.active){
            return this.dropInventoryOnDeath;
        }
        if(this.testingMode.isMouseHoveredOver() && this.testingMode.active){
            return this.testingMode;
        }
        return null;
    }
}
