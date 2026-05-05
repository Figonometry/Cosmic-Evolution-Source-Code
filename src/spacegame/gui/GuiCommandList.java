package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

import java.util.ArrayList;

public final class GuiCommandList extends Gui {
    private static final ArrayList<String> commandFormats = new ArrayList<>();
    public Button back;
    public int transparentBackground;



    public GuiCommandList(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 0, -300, this, cosmicEvolution);
    }

    static {
        commandFormats.add("/teleport,tp <x> <y> <z>");
        commandFormats.add("/giveBlock, giveItem <id> <quantity>");
        commandFormats.add("/time <set,add,sub> <time>");
        commandFormats.add("/freeMove");
        commandFormats.add("/speed <1,2,3 etc>");
        commandFormats.add("/heal");
        commandFormats.add("/kill");
        commandFormats.add("/toggleTime");
        commandFormats.add("/summonEntity <name> <x> <y> <z>");
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
        tessellator.addElementsCW();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        tessellator.toggleOrtho();



        float x = -128;
        float y = CosmicEvolution.height/2f - 60;


        for(int i = 0; i < commandFormats.size(); i++){
            fontRenderer.drawString(commandFormats.get(i), x, y, -90, 255 << 8, 50, 255);
            y -= 30;
        }

        this.back.renderButton();
    }

    @Override
    public Button getActiveButton() {
       return this.back.active && this.back.isMouseHoveredOver() ? this.back : null;
    }
}
