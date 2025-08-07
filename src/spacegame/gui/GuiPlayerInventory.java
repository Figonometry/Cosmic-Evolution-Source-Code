package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.item.Inventory;
import spacegame.item.ItemStack;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;

public final class GuiPlayerInventory extends GuiInventory {
    public TextureLoader inventoryUI;
    public static TextureLoader transparentBackground;
    public static TextureLoader fillableColorWithShadedBottom;

    public GuiPlayerInventory(SpaceGame spaceGame, Inventory associatedInventory) {
        super(spaceGame);
        this.associatedInventory = associatedInventory;
    }

    @Override
    public void loadTextures() {
        this.inventoryUI = new TextureLoader("src/spacegame/assets/textures/gui/guiInventory/playerInventory.png", 32, 32);
        transparentBackground = new TextureLoader("src/spacegame/assets/textures/gui/transparentBackground.png", 32,32);
        fillableColorWithShadedBottom = new TextureLoader("src/spacegame/assets/textures/gui/fillableColorWithShadedBottom.png", 32,32);
        this.loadTexture();
    }

    @Override
    public void deleteTextures() {
        GL46.glDeleteTextures(this.inventoryUI.texID);
        GL46.glDeleteTextures(transparentBackground.texID);
        GL46.glDeleteTextures(fillableColorWithShadedBottom.texID);
        this.inventoryUI = null;
       transparentBackground = null;
       fillableColorWithShadedBottom = null;
       this.unloadTexture();
    }

    @Override
    public void drawGui() {
        Tessellator tessellator = Tessellator.instance;
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
        tessellator.drawTexture2D(transparentBackground.texID, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int inventoryUIWidth = 656;
        int inventoryUIHeight = 128;
        int inventoryUIX = 5;
        int inventoryUIY = 0;
        int inventoryUIZ = -90;
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.inventoryUI.texID, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();

        this.associatedInventory.renderInventory();

        GuiInGame.renderText();
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
