package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.SpaceGame;
import spacegame.item.Item;
import spacegame.item.ItemCraftingTemplates;
import spacegame.render.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class GuiCraftingStick extends GuiCrafting {
    public Button craft;
    public int inventoryUI;
    public int arrow;
    public int transparentBackground;
    public int fillableColor;
    public CraftingMaterial[] materials;
    public short outputItemID;
    public int x;
    public int y;
    public int z;
    public GuiCraftingStick(SpaceGame spaceGame, int x, int y, int z) {
        super(spaceGame);
        this.craft = new Button(EnumButtonEffects.CRAFT.name(), 128,50, 0,-150, this, this.sg);
        this.materials = new CraftingMaterial[1024];
        this.x = x;
        this.y = y;
        this.z = z;
        int xMat; //base -370
        int yMat; //base -120
        int[] pixels = new int[1024];
        String filepath = "src/spacegame/assets/textures/item/" +
                RenderEngine.getBlockName(Item.rawStick.ID, "src/spacegame/assets/textures/item/") + ".png";
        BufferedImage image = null;

        try {
            image = ImageIO.read(new File(filepath));
            BufferedImage argbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = argbImage.createGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
            image = argbImage;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        image.getRGB(0, 0, 32, 32, pixels, 0, 32);
        Color[] pixelColors = new Color[pixels.length];

        for (int i = 0; i < pixelColors.length; i++) {
            int alpha = (pixels[i] >> 24) & 0xFF;
            int red = (pixels[i] >> 16) & 0xFF;
            int green = (pixels[i] >> 8) & 0xFF;
            int blue = pixels[i] & 0xFF;

            pixelColors[i] = new Color(red, green, blue, alpha);
        }

        Color[] reversedPixelColors = new Color[pixelColors.length];

        for(int i = 0; i < reversedPixelColors.length; i++){
            reversedPixelColors[i] = pixelColors[pixelColors.length - i - 1];
        }

        pixelColors = reversedPixelColors;

        Color[] mirroredPixelColors = new Color[pixelColors.length];

        int index = 0;
        for(int i = 0; i < mirroredPixelColors.length; i += 32){
            for(int j = 31; j >= 0; j--){
                mirroredPixelColors[index] = pixelColors[i + j];
                index++;
            }
        }

        pixelColors = mirroredPixelColors;

        for(int i = 0 ; i < this.materials.length; i++){
            xMat = -362 + ((i % 32) * 8);
            yMat = -112 + ((i / 32) * 8);
            this.materials[i] = new CraftingMaterial(8, 8, xMat, yMat, "wood");
            int red = pixelColors[i].getRed();
            int green = pixelColors[i].getGreen();
            int blue = pixelColors[i].getBlue();
            this.materials[i].setColor(new Color(red, green, blue, 0).getRGB());
            if(pixelColors[i].getAlpha() != 255){
                this.materials[i].active = false;
            }
        }
    }

    @Override
    public void loadTextures() {
        this.inventoryUI = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/blocks/" + RenderEngine.getBlockName(Block.oakLogFullSizeNormal.textureID, "src/spacegame/assets/textures/blocks/") + ".png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.arrow = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/arrow.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.transparentBackground = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.fillableColor = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void deleteTextures() {
        SpaceGame.instance.renderEngine.deleteTexture(this.inventoryUI);
        SpaceGame.instance.renderEngine.deleteTexture(this.arrow);
        SpaceGame.instance.renderEngine.deleteTexture(this.transparentBackground);
        SpaceGame.instance.renderEngine.deleteTexture(this.fillableColor);
    }

    @Override
    public void drawGui() {
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
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
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
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
        tessellator.drawTexture2D(this.inventoryUI, Shader.screen2DTexture, SpaceGame.camera);

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
        tessellator.drawTexture2D(this.arrow, Shader.screen2DTexture, SpaceGame.camera);

        int outputSlotX = 315;
        int outputSlotY = 0;
        int outputWidth = 128;
        int outputHeight = 128;
        tessellator.addVertex2DTexture(0, outputSlotX - (float)outputWidth/2, outputSlotY - (float)outputHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, outputSlotX + (float)outputWidth/2, outputSlotY + (float)outputHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, outputSlotX - (float)outputWidth/2, outputSlotY + (float)outputHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, outputSlotX + (float)outputWidth/2, outputSlotY - (float)outputHeight/2, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);

        outputSlotX = -250;
        outputSlotY = 0;
        outputWidth = 256;
        outputHeight = 256;
        tessellator.addVertex2DTexture(0, outputSlotX - (float)outputWidth/2, outputSlotY - (float)outputHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, outputSlotX + (float)outputWidth/2, outputSlotY + (float)outputHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, outputSlotX - (float)outputWidth/2, outputSlotY + (float)outputHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, outputSlotX + (float)outputWidth/2, outputSlotY - (float)outputHeight/2, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();

        this.craft.renderButton();
        this.renderCraftingMaterials();
        this.renderOutput();
    }

    private void renderOutput(){
        if(this.doesInputMatchTorchTemplate()){
            this.setOutputItem(Item.unlitTorch.ID);
        } else {
            this.setOutputItem(Item.woodShards.ID);
        }
        this.renderOutputItem();
    }

    private boolean doesInputMatchTorchTemplate(){
        short countCorrect = 0;
        boolean squareActive;

        for (int j = 0; j < this.materials.length; j++) {
            squareActive = this.materials[j].active;
            if(squareActive){
                if(this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.unlitTorch.indices)){
                    countCorrect++;
                }
            } else {
                if(!this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.unlitTorch.indices)){
                    countCorrect++;
                }
            }
        }
        return countCorrect / 1024f >= 0.995f;
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
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
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
        tessellator.drawVertexArray(Assets.itemTextureArray, Shader.screenTextureArray, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();
    }

    private void setOutputItem(short itemID){
        this.outputItemID = itemID;
    }

    private void renderCraftingMaterials(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
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
        tessellator.drawTexture2D(this.fillableColor, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
    }

    @Override
    public Button getActiveButton() {
        if(this.craft.isMouseHoveredOver() && this.craft.active){
            return this.craft;
        }
        return null;
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
}
