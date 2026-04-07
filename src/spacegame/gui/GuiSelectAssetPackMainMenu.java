package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.render.AssetPack;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.util.MathUtil;

import java.io.File;

public final class GuiSelectAssetPackMainMenu extends Gui {
    public Button back;
    public Scrollbar scrollbar;
    public int star;
    public int title;
    public int earth;
    public int background;
    public int fillableColor;
    public int outline;
    public AssetPack[] assetPacks;
    public File assetPackFolder;
    public int folderCount;
    public GuiSelectAssetPackMainMenu(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
        this.back = new Button(EnumButtonEffects.BACK.name(), 512, 64, 0, -450, this, cosmicEvolution);

        File assetPackFolder = new File(cosmicEvolution.launcherDirectory + "/assetPacks");
        if(!assetPackFolder.exists()){
            assetPackFolder.mkdirs();
        }
        File defaultAssetPack = new File(assetPackFolder + "/Default");
        defaultAssetPack.mkdirs();
        this.assetPackFolder = assetPackFolder;

        File[] folderContents = assetPackFolder.listFiles();

        if(folderContents != null) {
            int folderCount = 0;
            for (int i = 0; i < folderContents.length; i++) {
                if (folderContents[i].isDirectory()) {
                    folderCount++;
                }
            }

            this.folderCount = folderCount;

            this.assetPacks = new AssetPack[folderCount];
            int nextAvailIndex = 0;

            for (int i = 0; i < folderContents.length; i++) {
                if(folderContents[i].isDirectory()){
                    this.assetPacks[nextAvailIndex] = new AssetPack(folderContents[i].getPath());
                    nextAvailIndex++;
                }
            }
        }


        if(this.assetPacks.length > 3) {
            float scrollbarHeight = (float) (Math.pow(1.03, (-this.assetPacks.length + 3)) * 660);
            int maxScrolls = (this.assetPacks.length - 3) * 2;
            this.scrollbar = new Scrollbar(512 + 128, 0, 32, scrollbarHeight, 330, -330, maxScrolls); //660 at 3 and decreasing from there
            this.scrollbar.y = this.scrollbar.upperBound - this.scrollbar.height/2;
            this.scrollbar.calculatePixelStep();
        }

    }

    public void rebuildAssetPackArray(File[] folderContents){
        if(folderContents == null){
            GameSettings.assetPackPath = null;
            return;
        }

        int nextAvailIndex = 0;

        this.assetPacks = new AssetPack[this.folderCount];

        for (int i = 0; i < folderContents.length; i++) {
            if(folderContents[i].isDirectory()){
                this.assetPacks[nextAvailIndex] = new AssetPack(folderContents[i].getPath());
                nextAvailIndex++;
            }
        }

        if(this.assetPacks.length > 3) {
            float scrollbarHeight = (float) (Math.pow(1.03, (-this.assetPacks.length + 3)) * 660);
            int maxScrolls = (this.assetPacks.length - 3) * 2;
            this.scrollbar = new Scrollbar(512 + 128, 0, 32, scrollbarHeight, 330, -330, maxScrolls);
            this.scrollbar.calculatePixelStep();
            this.scrollbar.y = -this.scrollbar.pixelStep * this.scrollbar.scrolls;
        } else {
            this.scrollbar = null;
        }
    }

    @Override
    public void loadTextures() {
        this.outline = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/assetPackOutline.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.fillableColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.star = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/star.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.title = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/assetPackTitle.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.earth = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiMainMenu/earth.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.background = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.star);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.title);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.earth);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.background);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.fillableColor);
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

        int titleWidth = 1087;
        int titleHeight = 132;
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

        int contentAreaWidth = CosmicEvolution.width - CosmicEvolution.width / 4 - 100;
        int contentAreaHeight = CosmicEvolution.height - CosmicEvolution.height / 4 - 100;
        int contentAreaX = 0;
        int contentAreaY = 0;
        int contentAreaZ = -90;
        tessellator.addVertex2DTexture(0, contentAreaX - contentAreaWidth/2, contentAreaY - contentAreaHeight/2, contentAreaZ, 3);
        tessellator.addVertex2DTexture(0, contentAreaX + contentAreaWidth/2, contentAreaY + contentAreaHeight/2, contentAreaZ, 1);
        tessellator.addVertex2DTexture(0, contentAreaX - contentAreaWidth/2, contentAreaY + contentAreaHeight/2, contentAreaZ, 2);
        tessellator.addVertex2DTexture(0, contentAreaX + contentAreaWidth/2, contentAreaY - contentAreaHeight/2, contentAreaZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.fillableColor, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();

        float left = (CosmicEvolution.width / 2.0f) + (contentAreaX - contentAreaWidth / 2.0f);
        float right = (CosmicEvolution.width / 2.0f) + (contentAreaX + contentAreaWidth / 2.0f);
        float bottom = (CosmicEvolution.height / 2.0f) + (contentAreaY - contentAreaHeight / 2.0f);
        float top = (CosmicEvolution.height / 2.0f) + (contentAreaY + contentAreaHeight / 2.0f);

        int sx = (int)left;
        int sy = (int)bottom;
        int sw = (int)(right - left);
        int sh = (int)(top - bottom);


        GL46.glEnable(GL46.GL_SCISSOR_TEST);
        GL46.glScissor(sx, sy, sw, sh);

        contentAreaZ = -80;

        float additionalY = this.scrollbar == null ? 0 : this.scrollbar.additionalY;
        float x = 96;
        float y = 200 + additionalY;
        float width = 1024;
        float height = 196;
        FontRenderer fontRenderer = FontRenderer.instance;
        for(int i = 0; i < this.assetPacks.length; i++) {
            if (this.assetPacks[i] == null) continue;
            if (!this.isAssetPackInViewport(y, height)) {
                if (this.assetPacks[i].icon != -1) {
                    this.assetPacks[i].deleteIcon();
                }
                continue;
            }

            if (this.assetPacks[i].icon == -1) {
                this.assetPacks[i].loadIcon();
            }

            int outlineColor = this.assetPacks[i].filepath.equals(GameSettings.assetPackPath) ? 255 << 8 : 16777215;
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(outlineColor, x - width / 2, y - height / 2, contentAreaZ, 3);
            tessellator.addVertex2DTexture(outlineColor, x + width / 2, y + height / 2, contentAreaZ, 1);
            tessellator.addVertex2DTexture(outlineColor, x - width / 2, y + height / 2, contentAreaZ, 2);
            tessellator.addVertex2DTexture(outlineColor, x + width / 2, y - height / 2, contentAreaZ, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(this.outline, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();

            fontRenderer.drawString(this.assetPacks[i].title, x - 513, y + 40, -70, 16777215, 60, 255);
            fontRenderer.drawString(this.assetPacks[i].description, x - 512, y - 40, -70, 16777215, 50, 255);

            x = -544;
            width = 196;
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(16777215, x - width / 2, y - height / 2, contentAreaZ, 3);
            tessellator.addVertex2DTexture(16777215, x + width / 2, y + height / 2, contentAreaZ, 1);
            tessellator.addVertex2DTexture(16777215, x - width / 2, y + height / 2, contentAreaZ, 2);
            tessellator.addVertex2DTexture(16777215, x + width / 2, y - height / 2, contentAreaZ, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(this.assetPacks[i].icon, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();

            width = 1024;
            x = 96;
            y -= 200;
        }



        GL46.glDisable(GL46.GL_SCISSOR_TEST);


        if(this.scrollbar != null) {
            this.scrollbar.render(this.fillableColor);
        }
        this.back.renderButton();
    }

    private boolean isAssetPackInViewport(float y, float height){
        if(y < (float) CosmicEvolution.height / 2 || y > (float) -CosmicEvolution.height / 2){
            return true;
        }

        float upperBound = y + height/2;
        float lowerBound = y - height/2;

        if(upperBound < (float) CosmicEvolution.height / 2 || upperBound > (float) -CosmicEvolution.height / 2){
            return true;
        }

        if(lowerBound < (float) CosmicEvolution.height / 2 || lowerBound > (float) -CosmicEvolution.height / 2){
            return true;
        }

        return false;
    }

    @Override
    public Button getActiveButton() {
        if(this.back.isMouseHoveredOver() && this.back.active){
            return this.back;
        }
        return null;
    }

    public AssetPack getHoveredAssetPack(){
        float additionalY = this.scrollbar == null ? 0 : this.scrollbar.additionalY;
        float x = 96;
        float y = 200 + additionalY;
        float width = 1024;
        float height = 196;
        float mouseX = MathUtil.getOpenGLMouseX();
        float mouseY = MathUtil.getOpenGLMouseY();
        for(int i = 0; i < this.assetPacks.length; i++){
            if(this.assetPacks[i] == null)continue;
            if(!this.isAssetPackInViewport(y, height))continue;

            if(MathUtil.pointInsideBox(mouseX, mouseY, x, y, width, height)){
                return this.assetPacks[i];
            }

            x = -544;
            width = 196;


            if(MathUtil.pointInsideBox(mouseX, mouseY, x, y, width, height)){
                return this.assetPacks[i];
            }


            width = 1024;
            x = 96;
            y -= 200;
        }

        return null;
    }


}
