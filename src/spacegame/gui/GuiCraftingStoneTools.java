package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.item.Item;
import spacegame.item.ItemCraftingTemplates;
import spacegame.render.*;

import java.awt.*;
import java.util.Random;

public final class GuiCraftingStoneTools extends GuiCrafting {
    public Button craft;
    public TextureLoader inventoryUI;
    public TextureLoader arrow;
    public TextureLoader transparentBackground;
    public TextureLoader fillableColor;
    public CraftingMaterial[] materials;
    public short outputItemID;
    public int x;
    public int y;
    public int z;
    public GuiCraftingStoneTools(SpaceGame spaceGame, int x, int y, int z) {
        super(spaceGame);
        this.x = x;
        this.y = y;
        this.z = z;
        this.craft = new Button(EnumButtonEffects.CRAFT.name(), 128,50, 0,-150, this, this.sg);
        this.materials = new CraftingMaterial[64];
        int xMat; //base -370
        int yMat; //base -120
        Random rand = new Random();
        float color;
        for(int i = 0 ; i < this.materials.length; i++){
            color = rand.nextFloat(0.4f, 0.6f);
            xMat = -362 + ((i % 8) * 32);
            yMat = -112 + ((i / 8) * 32);
            this.materials[i] = new CraftingMaterial(32, 32, xMat, yMat, "stone");
            this.materials[i].setColor(new Color(color, color, color, 0).getRGB());
        }
    }

    @Override
    public CraftingMaterial getHoveredCraftingMaterial() {
        for(int i = 0; i < this.materials.length; i++){
            if(this.materials[i].isMouseHoveredOver()){
                return this.materials[i];
            }
        }
        return null;
    }

    @Override
    public void loadTextures() {
        this.inventoryUI = new TextureLoader("src/spacegame/assets/textures/gui/guiInventory/playerInventory.png", 32, 32);
        this.arrow = new TextureLoader("src/spacegame/assets/textures/gui/guiInventory/arrow.png", 32, 32);
        this.transparentBackground = new TextureLoader("src/spacegame/assets/textures/gui/transparentBackground.png", 32,32);
        this.fillableColor = new TextureLoader("src/spacegame/assets/textures/gui/fillableColor.png", 32,32);
    }

    @Override
    public void deleteTextures() {
        GL46.glDeleteTextures(this.inventoryUI.texID);
        GL46.glDeleteTextures(this.arrow.texID);
        GL46.glDeleteTextures(this.transparentBackground.texID);
        GL46.glDeleteTextures(this.fillableColor.texID);
        this.inventoryUI = null;
        this.arrow = null;
        this.transparentBackground = null;
        this.fillableColor = null;
    }

    @Override
    public void drawGui() {
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        int backgroundWidth = 1920;
        int backgroundHeight = 1017;
        int backgroundX = 0;
        int backgroundY = 0;
        int backgroundZ = -110;
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.transparentBackground.texID, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);

        backgroundWidth = 798;
        backgroundHeight = 480;
        backgroundX = 0;
        backgroundY = 0;
        backgroundZ = -100;
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 3);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 1);
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 2);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.inventoryUI.texID, Shader.screen2DTexture, SpaceGame.camera);

        backgroundZ += 10;
        int arrowX = 64;
        int arrowY = 0;
        int arrowWidth = 256;
        int arrowHeight = 128;
        tessellator.addVertex2DTexture(0, arrowX - (float)arrowWidth/2, arrowY - (float)arrowHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, arrowX + (float)arrowWidth/2, arrowY + (float)arrowHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, arrowX - (float)arrowWidth/2, arrowY + (float)arrowHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, arrowX + (float)arrowWidth/2, arrowY - (float)arrowHeight/2, backgroundZ, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.arrow.texID, Shader.screen2DTexture, SpaceGame.camera);

        int outputSlotX = 315;
        int outputSlotY = 0;
        int outputWidth = 128;
        int outputHeight = 128;
        tessellator.addVertex2DTexture(0, outputSlotX - (float)outputWidth/2, outputSlotY - (float)outputHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, outputSlotX + (float)outputWidth/2, outputSlotY + (float)outputHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, outputSlotX - (float)outputWidth/2, outputSlotY + (float)outputHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, outputSlotX + (float)outputWidth/2, outputSlotY - (float)outputHeight/2, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.transparentBackground.texID, Shader.screen2DTexture, SpaceGame.camera);

        outputSlotX = -250;
        outputSlotY = 0;
        outputWidth = 256;
        outputHeight = 256;
        tessellator.addVertex2DTexture(0, outputSlotX - (float)outputWidth/2, outputSlotY - (float)outputHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, outputSlotX + (float)outputWidth/2, outputSlotY + (float)outputHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, outputSlotX - (float)outputWidth/2, outputSlotY + (float)outputHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, outputSlotX + (float)outputWidth/2, outputSlotY - (float)outputHeight/2, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.transparentBackground.texID, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();

        this.craft.renderButton();
        this.renderCraftingMaterials();
        this.renderOutput();
    }

    private void renderCraftingMaterials(){
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        int color;
        float x;
        float y;
        float height;
        float width;
        float z = -80;
        for(int i = 0; i < this.materials.length; i++) {
            if (this.materials[i].active) {
                color = this.materials[i].getColor();
                x = this.materials[i].x;
                y = this.materials[i].y;
                height = this.materials[i].height;
                width = this.materials[i].width;
                tessellator.addVertex2DTexture(color, x - width / 2, y - height / 2, z, 3);
                tessellator.addVertex2DTexture(color, x + width / 2, y + height / 2, z, 1);
                tessellator.addVertex2DTexture(color, x - width / 2, y + height / 2, z, 2);
                tessellator.addVertex2DTexture(color, x + width / 2, y - height / 2, z, 0);
                tessellator.addElements();
            }
        }
        tessellator.drawTexture2D(this.fillableColor.texID, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
    }

    private void renderOutput(){
        if(this.doesInputMatchHandAxeTemplate()){
            this.setOutputItem(Item.stoneHandAxe.ID);
        } else {
            this.setOutputItem(Item.stoneFragments.ID);
        }
        this.renderOutputItem();
    }

    private boolean doesInputMatchHandAxeTemplate(){
        short countCorrect = 0;
        boolean squareActive;
        int translateX = -ItemCraftingTemplates.stoneHandAxe.translateLeftLimit;
        int translateY = -ItemCraftingTemplates.stoneHandAxe.translateUpLimit;
        short[] indices;
        for(int i = 0; i < ItemCraftingTemplates.stoneHandAxe.translationCount; i++) {
            countCorrect = 0;
            indices = ItemCraftingTemplates.stoneHandAxe.translate(translateX,translateY,ItemCraftingTemplates.stoneHandAxe.indices);
            for (int j = 0; j < this.materials.length; j++) {
                squareActive = this.materials[j].active;
                if(squareActive){
                    if(this.isIndexSupposedToBeFilled(j, indices)){
                        countCorrect++;
                    }
                } else {
                    if(!this.isIndexSupposedToBeFilled(j, indices)){
                        countCorrect++;
                    }
                }
            }
            translateX++;
            if(translateX == ItemCraftingTemplates.stoneHandAxe.translateRightLimit){
                translateX = -ItemCraftingTemplates.stoneHandAxe.translateLeftLimit;
                if(translateY == ItemCraftingTemplates.stoneHandAxe.translateDownLimit){
                    break;
                }
                translateY++;
            }
            if(countCorrect / 64f >= 0.9f){
                return true;
            }
        }
        return false;
    }


    private boolean isIndexSupposedToBeFilled(int testedIndex, short[] indices){
        for(int i = 0; i < indices.length; i++){
            if(testedIndex == indices[i]){
                return true;
            }
        }
        return false;
    }

    private void renderOutputItem(){
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        float itemID = Item.list[this.outputItemID].getTextureID(this.outputItemID, (byte)0, RenderBlocks.WEST_FACE);
        float x = 315;
        float y = 0;
        float z = -70;
        float width = 128;
        float height = 128;
        tessellator.addVertexTextureArray(16777215, x - width/2, y - height/2, z, 3, itemID, RenderBlocks.WEST_FACE);
        tessellator.addVertexTextureArray(16777215, x + width/2, y + height/2, z, 1, itemID, RenderBlocks.WEST_FACE);
        tessellator.addVertexTextureArray(16777215, x - width/2, y + height/2, z, 2, itemID, RenderBlocks.WEST_FACE);
        tessellator.addVertexTextureArray(16777215, x + width/2, y - height/2, z, 0, itemID, RenderBlocks.WEST_FACE);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawVertexArray(Assets.itemTextureArray.arrayID, Shader.screenTextureArray, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();
    }

    private void setOutputItem(short itemID){
        this.outputItemID = itemID;
    }

    @Override
    public Button getActiveButton() {
        if(this.craft.isMouseHoveredOver() && this.craft.active){
            return this.craft;
        }
        return null;
    }
}
