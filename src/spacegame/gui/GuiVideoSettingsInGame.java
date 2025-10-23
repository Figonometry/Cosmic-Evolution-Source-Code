package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public final class GuiVideoSettingsInGame extends Gui {
    private CosmicEvolution ce;
    public Button vsync;
    public Button back;
    public Button showFPS;
    public Button fullscreen;
    public Button fov;
    public Button mouseSensitivity;
    public Button viewBobbing;
    public Button shadowMap;
    public Button chunkViewDistanceVertical;
    public Button chunkViewDistanceHorizontal;
    public Button pageLeft;
    public Button pageRight;
    public Button wavyWater;
    public Button wavyLeaves;
    public Button transparentLeaves;
    public int title;
    public int background;
    public int page = 1;

    public GuiVideoSettingsInGame(CosmicEvolution cosmicEvolution){
        super(cosmicEvolution);
        this.ce = cosmicEvolution;
        //Page 1
        this.viewBobbing = new Button(EnumButtonEffects.VIEW_BOB.name(), 512, 64, -587,150, this, this.ce);
        this.shadowMap = new Button(EnumButtonEffects.SHADOW_MAP.name(), 512, 64, 0, 150, this, this.ce);
        this.mouseSensitivity = new Button(EnumButtonEffects.MOUSE_SENSITIVITY.name(), 512, 64, 587, 150, this, this.ce);
        this.showFPS = new Button(EnumButtonEffects.SHOW_FPS.name(), 512, 64, -587, 0, this, this.ce);
        this.fullscreen = new Button(EnumButtonEffects.FULLSCREEN.name(), 512, 64, 0,0, this, this.ce);
        this.fov = new Button(EnumButtonEffects.FOV.name(), 512, 64, 587, 0, this, this.ce);
        this.vsync = new Button(EnumButtonEffects.VSYNC.name(), 512, 64, -587, -150,  this, this.ce);
        this.chunkViewDistanceHorizontal = new Button(EnumButtonEffects.CHUNK_VIEW_HORIZONTAL.name(), 512,64, 0, -150, this, this.ce);
        this.chunkViewDistanceVertical = new Button(EnumButtonEffects.CHUNK_VIEW_VERTICAL.name(), 512, 64, 587, -150, this, this.ce);

        //Page 2
        this.wavyWater = new Button(EnumButtonEffects.WAVY_WATER.name(), 512, 64, -587,150, this, this.ce);
        this.wavyLeaves = new Button(EnumButtonEffects.WAVY_LEAVES.name(), 512, 64, 0, 150, this, this.ce);
        this.transparentLeaves = new Button(EnumButtonEffects.TRANSPARENT_LEAVES.name(), 512, 64, 587, 150, this, this.ce);


        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 0, -400, this, this.ce);
        this.pageLeft = new Button(EnumButtonEffects.PAGE_LEFT.name(), 256, 64, -400, -400, this, this.ce);
        this.pageRight = new Button(EnumButtonEffects.PAGE_RIGHT.name(), 256, 64, 400, -400, this, this.ce);
    }

    @Override
    public void loadTextures() {
        this.title = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/videoSettings.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.background = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
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

        switch (this.page) {
            case 1 -> {
                this.viewBobbing.renderButton();
                this.shadowMap.renderButton();
                this.mouseSensitivity.renderButton();
                this.fov.renderButton();
                this.vsync.renderButton();
                this.showFPS.renderButton();
                this.fullscreen.renderButton();
                this.chunkViewDistanceVertical.renderButton();
                this.chunkViewDistanceHorizontal.renderButton();
                this.pageRight.renderButton();
            }
            case 2 -> {
                this.wavyWater.renderButton();
                this.wavyLeaves.renderButton();
                this.transparentLeaves.renderButton();
                this.pageLeft.renderButton();
            }
        }
        this.back.renderButton();
    }

    @Override
    public Button getActiveButton() {
        switch (this.page) {
            case 1 -> {
                if (this.back.isMouseHoveredOver() && this.back.active) {
                    return this.back;
                } else if (this.shadowMap.isMouseHoveredOver() && this.shadowMap.active) {
                    return this.shadowMap;
                } else if (this.viewBobbing.isMouseHoveredOver() && this.viewBobbing.active) {
                    return this.viewBobbing;
                } else if (this.fov.isMouseHoveredOver() && this.fov.active) {
                    return this.fov;
                } else if (this.vsync.isMouseHoveredOver() && this.vsync.active) {
                    return this.vsync;
                } else if (this.mouseSensitivity.isMouseHoveredOver() && this.mouseSensitivity.active) {
                    return this.mouseSensitivity;
                } else if (this.showFPS.isMouseHoveredOver() && this.showFPS.active) {
                    return this.showFPS;
                } else if (this.fullscreen.isMouseHoveredOver() && this.fullscreen.active) {
                    return this.fullscreen;
                } else if (this.chunkViewDistanceVertical.isMouseHoveredOver() && this.chunkViewDistanceVertical.active) {
                    return this.chunkViewDistanceVertical;
                } else if (this.chunkViewDistanceHorizontal.isMouseHoveredOver() && this.chunkViewDistanceHorizontal.active) {
                    return this.chunkViewDistanceHorizontal;
                } else if (this.pageRight.isMouseHoveredOver() && this.pageRight.active) {
                    return this.pageRight;
                }
            }
            case 2 -> {
                if (this.back.isMouseHoveredOver() && this.back.active) {
                    return this.back;
                } else if (this.wavyWater.isMouseHoveredOver() && this.wavyWater.active) {
                    return this.wavyWater;
                } else if (this.wavyLeaves.isMouseHoveredOver() && this.wavyLeaves.active) {
                    return this.wavyLeaves;
                } else if (this.transparentLeaves.isMouseHoveredOver() && this.transparentLeaves.active) {
                    return this.transparentLeaves;
                } else if (this.pageLeft.isMouseHoveredOver() && this.pageLeft.active) {
                    return this.pageLeft;
                }
            }
        }
        return null;
    }
}
