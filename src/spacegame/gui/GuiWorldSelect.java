package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;
import spacegame.world.Save;

public final class GuiWorldSelect extends Gui {
    private SpaceGame sg;
    public TextureLoader background;
    public TextureLoader title;
    public TextureLoader star;
    public TextureLoader earth;
    public Button save1;
    public Button save2;
    public Button save3;
    public Button save4;
    public Button save5;
    public Button delete;
    public Button back;
    public Button worldInfo;
    public boolean deletingFile;

    public GuiWorldSelect(SpaceGame spaceGame) {
        super(spaceGame);
        this.sg = spaceGame;
        this.save1 = new Button(EnumButtonEffects.SAVE_1.name(), 512, 64, 0, 200, this, this.sg);
        this.save2 = new Button(EnumButtonEffects.SAVE_2.name(), 512, 64, 0, 120, this, this.sg);
        this.save3 = new Button(EnumButtonEffects.SAVE_3.name(), 512, 64, 0, 40, this, this.sg);
        this.save4 = new Button(EnumButtonEffects.SAVE_4.name(), 512, 64, 0, -40, this, this.sg);
        this.save5 = new Button(EnumButtonEffects.SAVE_5.name(), 512, 64, 0, -120, this, this.sg);
        this.delete = new Button(EnumButtonEffects.DELETE.name(), 512, 64, 0, -300, this, this.sg);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 0, -380, this, this.sg);
        this.worldInfo = new Button(EnumButtonEffects.WORLD_INFO.name(), 64,64, -320, 200, this, this.sg);
    }

    @Override
    public void loadTextures() {
        this.star = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/star.png", 64,  64);
        this.earth = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", 512,512);
        this.title = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/saveSelect.png", 1052,120);
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

        int titleWidth = 1052;
        int titleHeight = 120;
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

        this.save1.renderButton();
        this.save2.renderButton();
        this.save3.renderButton();
        this.save4.renderButton();
        this.save5.renderButton();
        this.delete.renderButton();
        this.back.renderButton();
        if(this.save1.clicked || this.save2.clicked || this.save3.clicked || this.save4.clicked || this.save5.clicked && !this.deletingFile) {
            if(save1.clicked && Save.doesSaveSlotExist(1) == 2) {
                this.worldInfo.renderButton();
            }
            if(save2.clicked && Save.doesSaveSlotExist(2) == 2) {
                this.worldInfo.renderButton();
            }
            if(save3.clicked && Save.doesSaveSlotExist(3) == 2) {
                this.worldInfo.renderButton();
            }
            if(save4.clicked && Save.doesSaveSlotExist(4) == 2) {
                this.worldInfo.renderButton();
            }
            if(save5.clicked && Save.doesSaveSlotExist(5) == 2) {
                this.worldInfo.renderButton();
            }
        }

    }

    @Override
    public Button getActiveButton() {
        if(this.save1.isMouseHoveredOver() && this.save1.active && !this.subMenu){
            return this.save1;
        }
        if(this.save2.isMouseHoveredOver() && this.save2.active && !this.subMenu){
            return this.save2;
        }
        if(this.save3.isMouseHoveredOver() && this.save3.active && !this.subMenu){
            return this.save3;
        }
        if(this.save4.isMouseHoveredOver() && this.save4.active && !this.subMenu){
            return this.save4;
        }
        if(this.save5.isMouseHoveredOver() && this.save5.active){
            return this.save5;
        }
        if(this.delete.isMouseHoveredOver() && this.delete.active){
            return this.delete;
        }
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        }
        if(this.worldInfo.isMouseHoveredOver() && this.worldInfo.active){
            return this.worldInfo;
        }
        return null;
    }

    public int saveSelected(){
        if(this.save1.clicked){
            return 1;
        }
        if(this.save2.clicked){
            return 2;
        }
        if(this.save3.clicked){
            return 3;
        }
        if(this.save4.clicked){
            return 4;
        }
        if(this.save5.clicked){
            return 5;
        }
        return 1;
    }
}
