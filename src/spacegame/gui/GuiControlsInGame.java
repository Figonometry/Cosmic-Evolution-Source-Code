package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public final class GuiControlsInGame extends Gui {
    private CosmicEvolution ce;
    private Button forwardKey;
    private Button backwardKey;
    private Button leftKey;
    private Button rightKey;
    private Button jumpKey;
    private Button inventoryKey;
    private Button back;
    private Button dropKey;
    public int title;
    public int background;

    public GuiControlsInGame(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
        this.ce = cosmicEvolution;
        this.forwardKey = new Button(EnumButtonEffects.KEY_FORWARD.name(), 250, 50, -150, 200, this, this.ce);
        this.backwardKey = new Button(EnumButtonEffects.KEY_BACKWARD.name(), 250, 50, 150, 200, this, this.ce);
        this.leftKey = new Button(EnumButtonEffects.KEY_LEFT.name(), 250, 50, -150, 100, this, this.ce);
        this.rightKey = new Button(EnumButtonEffects.KEY_RIGHT.name(), 250, 50, 150, 100, this, this.ce);
        this.jumpKey = new Button(EnumButtonEffects.KEY_JUMP.name(), 250, 50, -150, 0, this, this.ce);
        this.inventoryKey = new Button(EnumButtonEffects.KEY_INVENTORY.name(), 250, 50, 150, 0, this, this.ce);
        this.dropKey = new Button(EnumButtonEffects.KEY_DROP.name(), 550, 50, 0, -100, this, this.ce);
        this.back = new Button(EnumButtonEffects.BACK.name(), 550, 50, 0, -200, this, this.ce);
    }

    @Override
    public void loadTextures() {
        this.title = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/controls.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.background = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.title);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.background);
    }


    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        int titleWidth = 832;
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
        tessellator.drawTexture2D(this.background, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        tessellator.toggleOrtho();

        this.forwardKey.renderButton();
        this.backwardKey.renderButton();
        this.leftKey.renderButton();
        this.rightKey.renderButton();
        this.inventoryKey.renderButton();
        this.jumpKey.renderButton();
        this.dropKey.renderButton();
        this.back.renderButton();
    }

    @Override
    public Button getActiveButton() {
        if(this.forwardKey.isMouseHoveredOver() && this.forwardKey.active){
            return this.forwardKey;
        } else if(this.backwardKey.isMouseHoveredOver() && this.backwardKey.active){
            return this.backwardKey;
        } else if(this.leftKey.isMouseHoveredOver() && this.leftKey.active){
            return this.leftKey;
        } else if(this.rightKey.isMouseHoveredOver() && this.rightKey.active){
            return this.rightKey;
        } else if(this.jumpKey.isMouseHoveredOver() && this.jumpKey.active){
            return this.jumpKey;
        } else if(this.inventoryKey.isMouseHoveredOver() && this.inventoryKey.active){
            return this.inventoryKey;
        } else if(this.dropKey.isMouseHoveredOver() && this.dropKey.active){
            return this.dropKey;
        } else if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        }
        return null;
    }
}
