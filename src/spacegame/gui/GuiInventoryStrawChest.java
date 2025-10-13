package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.item.Inventory;
import spacegame.item.ItemStack;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public final class GuiInventoryStrawChest extends GuiInventory {
    public Inventory playerInventory;
    public Inventory chestInventory;
    public int playerInventoryUI;
    public int strawChestInventoryUI;
    public static int fillableColorWithShadedBottom;

    public GuiInventoryStrawChest(SpaceGame spaceGame, Inventory playerInventory, Inventory chestInventory) {
        super(spaceGame);
        this.playerInventory = playerInventory;
        this.chestInventory = chestInventory;

        this.playerInventory.shiftAllItemStacks(-262, 0);
        this.chestInventory.shiftAllItemStacks(480, 0);
    }

    public int getPlayerInventoryShiftX(){
        return -256;
    }

    @Override
    public void loadTextures() {
        this.playerInventoryUI = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/playerInventory.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        transparentBackground = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        fillableColorWithShadedBottom = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColorWithShadedBottom.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.strawChestInventoryUI = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/strawChestInventory.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.loadTexture();
    }

    @Override
    public void deleteTextures() {
        this.playerInventory.resetAllItemStacks();
        this.chestInventory.resetAllItemStacks();
        SpaceGame.instance.renderEngine.deleteTexture(this.playerInventoryUI);
        SpaceGame.instance.renderEngine.deleteTexture(transparentBackground);
        SpaceGame.instance.renderEngine.deleteTexture(fillableColorWithShadedBottom);
        this.unloadTexture();
    }

    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
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
        tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int inventoryUIWidth = 1200;
        int inventoryUIHeight = 608;
        int inventoryUIX = -256;
        int inventoryUIY = 0;
        int inventoryUIZ = -90;
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.playerInventoryUI, Shader.screen2DTexture, SpaceGame.camera);

        inventoryUIWidth = 196;
        inventoryUIHeight = 608;
        inventoryUIX = 512;
        inventoryUIY = 0;
        inventoryUIZ = -90;
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.strawChestInventoryUI, Shader.screen2DTexture, SpaceGame.camera);

        tessellator.toggleOrtho();

        this.playerInventory.renderInventory();
        this.chestInventory.renderInventory();
        this.renderHoveredItemStackName(this.getHoveredItemStack());
    }

    @Override
    public Button getActiveButton() {
        return null;
    }

    @Override
    public ItemStack getHoveredItemStack() {
        ItemStack stack;
        for(int i = 0; i < this.playerInventory.itemStacks.length; i++){
            stack = this.playerInventory.itemStacks[i];
            if(stack.isMouseHoveredOver()){
                return stack;
            }
        }
        for(int i = 0; i < this.chestInventory.itemStacks.length; i++){
            stack = this.chestInventory.itemStacks[i];
            if(stack.isMouseHoveredOver()){
                return stack;
            }
        }
        return null;
    }
}
