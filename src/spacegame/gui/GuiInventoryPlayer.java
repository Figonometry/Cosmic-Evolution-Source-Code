package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.item.Inventory;
import spacegame.item.ItemStack;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public final class GuiInventoryPlayer extends GuiInventory {
    public int inventoryUI;
    public static int fillableColorWithShadedBottom;
    public static int flllableColor;

    public GuiInventoryPlayer(CosmicEvolution cosmicEvolution, Inventory associatedInventory) {
        super(cosmicEvolution);
        this.associatedInventory = associatedInventory;
    }

    @Override
    public void loadTextures() {
        this.inventoryUI = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/playerInventory.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        fillableColorWithShadedBottom = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColorWithShadedBottom.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        fillableColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.loadTexture();
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.inventoryUI);
        CosmicEvolution.instance.renderEngine.deleteTexture(fillableColorWithShadedBottom);
        CosmicEvolution.instance.renderEngine.deleteTexture(fillableColor);
        this.unloadTexture();
    }

    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();
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
        tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int inventoryUIWidth = 1200;
        int inventoryUIHeight = 608;
        int inventoryUIX = 5;
        int inventoryUIY = 0;
        int inventoryUIZ = -90;
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.inventoryUI, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();

        this.associatedInventory.renderInventory();
        this.renderHoveredItemStackName(this.getHoveredItemStack());
    }

    @Override
    public Button getActiveButton() {
        return null;
    }

    @Override
    public ItemStack getHoveredItemStack() {
       ItemStack stack;
       for(int i = 0; i < this.associatedInventory.itemStacks.length; i++){
           stack = this.associatedInventory.itemStacks[i];
           if(stack.isMouseHoveredOver()){
               return stack;
           }
       }
       return null;
    }
}
