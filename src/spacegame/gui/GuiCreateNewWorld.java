package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.util.LongHasher;
import spacegame.world.SaveSettings;

import java.util.Random;

public final class GuiCreateNewWorld extends Gui {
    private CosmicEvolution ce;
    public int background;
    public int title;
    public int star;
    public int earth;
    public TextField nameWorld;
    public TextField setSeed;
    public Button createWorld;
    public Button back;
    public Button difficultyOptions;
    public SaveSettings saveSettings;
    public int saveSlot;

    public GuiCreateNewWorld(CosmicEvolution cosmicEvolution, int saveSlot, SaveSettings saveSettings) {
        super(cosmicEvolution);
        this.ce = cosmicEvolution;
        this.saveSlot = saveSlot;
        this.createWorld = new Button(EnumButtonEffects.CREATE_NEW_WORLD.name(), 512, 64, -320, -400, this, this.ce);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 320, -400, this, this.ce);
        this.difficultyOptions = new Button(EnumButtonEffects.DIFFICULTY_OPTIONS.name(), 512, 64, 0, -200, this, this.ce);
        this.nameWorld = new TextField(512, 64, 0, 100);
        this.setSeed = new TextField(512, 64, 0, 0);
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

        if(this.nameWorld.text.length() == 0 || this.doesStringContainAllSpaces(this.nameWorld.text)){
            this.createWorld.active = false;
        } else {
            this.createWorld.active = true;
        }

        this.createWorld.renderButton();
        this.back.renderButton();
        this.difficultyOptions.renderButton();
        this.nameWorld.renderTextFieldAndText();
        this.setSeed.renderTextFieldAndText();
        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString("Name World", -384, 75,-15, 16777215, 50, 255);
        fontRenderer.drawCenteredString("Set Seed", -384, -25,-15, 16777215, 50, 255);
    }

    @Override
    public Button getActiveButton() {
        if(this.createWorld.isMouseHoveredOver() && this.createWorld.active){
            return this.createWorld;
        }
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        }
        if(this.difficultyOptions.isMouseHoveredOver() && this.difficultyOptions.active){
            return this.difficultyOptions;
        }
        return null;
    }

    @Override
    public TextField getTextField(){
        if(this.setSeed.isMouseHoveredOver()){
            return this.setSeed;
        }
        if(this.nameWorld.isMouseHoveredOver()){
            return this.nameWorld;
        }
        return null;
    }

    public boolean doesStringContainAllSpaces(String string){
        if(string.length() == 0){
            return true;
        } else {
            for(int i = 0; i < string.length(); i++){
                if(string.charAt(i) != ' '){
                    return false;
                }
            }
        }
        return true;
    }

    public long getSeed() {
        if (this.setSeed.text.length() == 0 || this.doesStringContainAllSpaces(this.setSeed.text)) {
            return new Random().nextLong();
        } else {
            return new LongHasher().hash( 69420, this.setSeed.text);
        }
    }

}
