package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;

public final class GuiRenameWorld extends Gui {
    private SpaceGame sg;
    public TextureLoader background;
    public TextureLoader title;
    public TextureLoader star;
    public TextureLoader earth;
    public TextField nameWorld;
    public Button renameWorld;
    public Button back;
    public int saveSlot;
    public GuiRenameWorld(SpaceGame spaceGame, int saveSlot) {
        super(spaceGame);
        this.sg = spaceGame;
        this.saveSlot = saveSlot;
        this.renameWorld = new Button(EnumButtonEffects.RENAME_WORLD.name(), 512, 64, 0, -100, this, this.sg);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 0, -200, this, this.sg);
        this.nameWorld = new TextField(512, 64, 0, 100);
    }

    @Override
    public void loadTextures() {
        this.star = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/star.png", 64,  64);
        this.earth = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", 512,512);
        this.title = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/renameWorld.png", 1256,144);
        this.background = new TextureLoader("src/spacegame/assets/textures/gui/transparentBackground.png", 32,32);
    }

    @Override
    public void deleteTextures() {
        GL46.glDeleteTextures(this.star.texID);
        GL46.glDeleteTextures(this.earth.texID);
        GL46.glDeleteTextures(this.title.texID);
        GL46.glDeleteTextures(this.background.texID);
        this.star = null;
        this.earth = null;
        this.title = null;
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

        int titleWidth = 1256;
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

        if(this.nameWorld.text.length() == 0 || this.doesStringContainAllSpaces(this.nameWorld.text)){
            this.renameWorld.active = false;
        } else {
            this.renameWorld.active = true;
        }

        this.renameWorld.renderButton();
        this.back.renderButton();
        this.nameWorld.renderTextFieldAndText();
        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString("Name World", -384, 75,-15, 16777215, 50);
    }

    @Override
    public Button getActiveButton() {
        if(this.renameWorld.isMouseHoveredOver() && this.renameWorld.active){
            return this.renameWorld;
        }
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
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

    @Override
    public TextField getTextField(){
        if(this.nameWorld.isMouseHoveredOver()){
            return this.nameWorld;
        }
        return null;
    }
}
