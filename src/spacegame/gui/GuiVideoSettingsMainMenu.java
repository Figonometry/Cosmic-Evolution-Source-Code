package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;

public final class GuiVideoSettingsMainMenu extends Gui {
    private SpaceGame sg;
    public Button vsync;
    public Button back;
    public Button showFPS;
    public Button fullscreen;
    public Button fov;
    public Button mouseSensitivity;
    public Button volumeSounds;
    public Button volumeMusic;
    public Button chunkViewDistanceVertical;
    public Button chunkViewDistanceHorizontal;
    public TextureLoader star;
    public TextureLoader title;
    public TextureLoader earth;
    public TextureLoader background;

    public GuiVideoSettingsMainMenu(SpaceGame spaceGame){
        super(spaceGame);
        this.sg = spaceGame;
        this.volumeSounds = new Button(EnumButtonEffects.VOLUME_SOUNDS.name(), 512, 64, -587,150, this, this.sg);
        this.volumeMusic = new Button(EnumButtonEffects.VOLUME_MUSIC.name(), 512, 64, 0, 150, this, this.sg);
        this.mouseSensitivity = new Button(EnumButtonEffects.MOUSE_SENSITIVITY.name(), 512, 64, 587, 150, this, this.sg);
        this.showFPS = new Button(EnumButtonEffects.SHOW_FPS.name(), 512, 64, -587, 0, this, this.sg);
        this.fullscreen = new Button(EnumButtonEffects.FULLSCREEN.name(), 512, 64, 0,0, this, this.sg);
        this.fullscreen.active = false;
        this.fov = new Button(EnumButtonEffects.FOV.name(), 512, 64, 587, 0, this, this.sg);
        this.vsync = new Button(EnumButtonEffects.VSYNC.name(), 512, 64, -587, -150,  this, this.sg);
        this.chunkViewDistanceHorizontal = new Button(EnumButtonEffects.CHUNK_VIEW_HORIZONTAL.name(), 512,64, 0, -150, this, this.sg);
        this.chunkViewDistanceVertical = new Button(EnumButtonEffects.CHUNK_VIEW_VERTICAL.name(), 512, 64, 587, -150, this, this.sg);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 0, -400, this, this.sg);
    }

    @Override
    public void loadTextures() {
        this.star = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/star.png", 64,  64);
        this.title = new TextureLoader("src/spacegame/assets/textures/gui/guiMainMenu/videoSettings.png", 832,144);
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

        int titleWidth = 1286;
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

        this.volumeSounds.renderButton();
        this.volumeMusic.renderButton();
        this.mouseSensitivity.renderButton();
        this.fov.renderButton();
        this.vsync.renderButton();
        this.back.renderButton();
        this.showFPS.renderButton();
        this.fullscreen.renderButton();
        this.chunkViewDistanceVertical.renderButton();
        this.chunkViewDistanceHorizontal.renderButton();
    }

    @Override
    public Button getActiveButton() {
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        } else if(this.volumeSounds.isMouseHoveredOver() && this.volumeSounds.active){
            return this.volumeSounds;
        } else if(this.volumeMusic.isMouseHoveredOver() && this.volumeMusic.active){
            return this.volumeMusic;
        } else if(this.fov.isMouseHoveredOver() && this.fov.active){
            return this.fov;
        } else if(this.vsync.isMouseHoveredOver() && this.vsync.active){
            return this.vsync;
        } else if(this.mouseSensitivity.isMouseHoveredOver() && this.mouseSensitivity.active){
            return this.mouseSensitivity;
        } else if(this.showFPS.isMouseHoveredOver() && this.showFPS.active){
            return this.showFPS;
        } else if(this.fullscreen.isMouseHoveredOver() && this.fullscreen.active){
            return this.fullscreen;
        } else if(this.chunkViewDistanceVertical.isMouseHoveredOver() && this.chunkViewDistanceVertical.active){
            return this.chunkViewDistanceVertical;
        } else if(this.chunkViewDistanceHorizontal.isMouseHoveredOver() && this.chunkViewDistanceHorizontal.active){
            return this.chunkViewDistanceHorizontal;
        }
        return null;
    }
}
