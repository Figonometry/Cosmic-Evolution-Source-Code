package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
public final class GuiCommandEntry extends Gui {
    public int transparentBackground;
    public TextField commandField;
    public GuiCommandEntry(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
        this.commandField = new TextField(1800, 64, 0, -350, 100, true);
        this.commandField.typing = true;
        cosmicEvolution.currentlySelectedField = this.commandField;
    }

    @Override
    public void loadTextures() {
        this.transparentBackground = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.transparentBackground);
    }

    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();

        FontRenderer fontRenderer = FontRenderer.instance;


        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);


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
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        tessellator.toggleOrtho();

        fontRenderer.drawCenteredString("This is a developer command console, this will disappear later on in Alpha or Beta", 0, -300, -90, 16777215, 60, 255);

        this.commandField.renderTextFieldAndText();
    }

    @Override
    public Button getActiveButton() {
        return null;
    }

    @Override
    public TextField getTextField(){
        return this.commandField.isMouseHoveredOver() ? this.commandField : null;
    }
}
