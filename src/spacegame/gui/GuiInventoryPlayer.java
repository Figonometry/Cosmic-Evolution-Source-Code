package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.item.ItemStack;
import spacegame.item.ItemTool;
import spacegame.render.Assets;
import spacegame.render.RenderBlocks;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.render.model.ModelLoader;
import spacegame.render.model.ModelPlayer;
import spacegame.util.MathUtil;

public final class GuiInventoryPlayer extends GuiInventory {
    public int inventoryUI;
    public static int fillableColorWithShadedBottom;
    public static int flllableColor;
    public int playerTexture;

    public GuiInventoryPlayer(CosmicEvolution cosmicEvolution, Inventory associatedInventory) {
        super(cosmicEvolution);
        this.associatedInventory = associatedInventory;
    }

    @Override
    public void loadTextures() {
        this.playerTexture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/entity/player.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.inventoryUI = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/playerInventory.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        fillableColorWithShadedBottom = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColorWithShadedBottom.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        fillableColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.loadTexture();
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.playerTexture);
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
        int backgroundZ = -10000;
        tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 0);
        tessellator.addElementsCW();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int inventoryUIWidth = 1200;
        int inventoryUIHeight = 608;
        int inventoryUIX = 5;
        int inventoryUIY = 0;
        int inventoryUIZ = -9000;
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
        tessellator.addElementsCW();
        tessellator.drawTexture2D(this.inventoryUI, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();


        ModelPlayer modelPlayer = ModelPlayer.getBaseModel();
        modelPlayer.scale(150);
        modelPlayer.animate(CosmicEvolution.instance.save.thePlayer.walkingAnimationTimer, CosmicEvolution.instance.save.thePlayer.animate, CosmicEvolution.instance.save.thePlayer);
        modelPlayer.segments[ModelPlayer.BODY].rotateModelSegmentY(-90f);

        float xCenter = -390;
        float yCenter = 64;

        float x = MathUtil.getOpenGLMouseX();
        float y = MathUtil.getOpenGLMouseY();

        float xDif = x - xCenter;
        float yDif = y - yCenter;

        float angle = 10f;

        float xRatio = xDif / 100f;
        float yRatio = yDif / 100f;

        if(xRatio < -1){
            xRatio = -1.0f;
        }

        if(xRatio > 1){
            xRatio = 1.0f;
        }

        if(yRatio < -1){
            yRatio = -1.0f;
        }

        if(yRatio > 1){
            yRatio = 1.0f;
        }

        modelPlayer.segments[ModelPlayer.BODY].rotateModelSegmentY(angle * xRatio);
        modelPlayer.segments[ModelPlayer.BODY].rotateModelSegmentX(angle * -yRatio);


        modelPlayer.renderModelFromInventory(CosmicEvolution.instance.save.thePlayer, xCenter, yCenter, -180, this.playerTexture);

        this.renderPlayerHeldItemOnModel(xCenter, yCenter, -180);


        this.associatedInventory.renderInventory();
        this.renderHoveredItemStackName(this.getHoveredItemStack());
    }

    private void renderPlayerHeldItemOnModel(float xPos, float yPos, float zPos){
        ModelLoader model = null;

        EntityPlayer thePlayer = CosmicEvolution.instance.save.thePlayer;

        if(thePlayer.isHoldingBlock()){
            model = Block.list[thePlayer.getHeldBlock()].blockModel.copyModel();
            model.rotateModel(45, 0, 1, 0);
            model.rotateModel(-26, 1, 0, 0);
            model.scaleModel(0.25f);
        } else if(thePlayer.getHeldItem() != Item.NULL_ITEM_REFERENCE && thePlayer.getHeldItem() != Item.block.ID){
            model = Item.list[thePlayer.getHeldItem()].itemModel.copyModel(); //Texture is -1 for some reason
        }


        if(model == null)return;

       model.scaleModel(150f);


       if(thePlayer.getHeldItem() != Item.NULL_ITEM_REFERENCE){
           if(Item.list[thePlayer.getHeldItem()] instanceof ItemTool){
               model.rotateModel(180, 0, 1, 0);
               model.rotateModel(90, 1, 0, 0);
           } else {
               model.rotateModel(180, 0, 1, 0);
           }
       }

       if(thePlayer.isHoldingBlock()){
           model.translateModel(0, -10, 0);
       }

        model.rotateModel(-thePlayer.angleRightArm, 1, 0, 0);

        float x = MathUtil.getOpenGLMouseX();
        float y = MathUtil.getOpenGLMouseY();


        xPos -= 56;
        yPos -= 32;
        zPos += 20;
        if(thePlayer.isHoldingBlock()){
            xPos += 24;
            yPos -= 16;
            zPos += 20;
        }


        float xDif = x - xPos;
        float yDif = y - yPos;

        float angle = 10f;

        float xRatio = xDif / 100f;
        float yRatio = yDif / 100f;

        if(xRatio < -1){
            xRatio = -1.0f;
        }

        if(xRatio > 1){
            xRatio = 1.0f;
        }

        if(yRatio < -1){
            yRatio = -1.0f;
        }

        if(yRatio > 1){
            yRatio = 1.0f;
        }

        model.rotateModel(angle * xRatio, 0, 1, 0);
        model.rotateModel(angle * -yRatio, 1, 0, 0);

        model.translateModel(xPos , yPos , zPos);

        int colorVal = 255;

        int colorRGB = 0;

        int colorTop = ((colorVal) << 16) | ((colorVal) << 8) | colorVal;
        int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
        int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
        int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
        int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
        int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;


        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;

        float textureID;


        for(int i = 0; i < model.modelFaces.length; i++) {
            if(model.usesMultipleTextures){
                textureID = model.modelFaces[i].texture;
            } else {
                textureID = Block.list[thePlayer.getHeldBlock()].getBlockTexture(thePlayer.getHeldBlock(), model.modelFaces[i].faceType);
            }
            switch (model.modelFaces[i].faceType){
                case RenderBlocks.TOP_FACE -> {
                    colorRGB = colorTop;
                }
                case RenderBlocks.BOTTOM_FACE -> {
                    colorRGB = colorBottom;
                }
                case RenderBlocks.NORTH_FACE -> {
                    colorRGB = colorNorth;
                }
                case RenderBlocks.SOUTH_FACE -> {
                    colorRGB = colorSouth;
                }
                case RenderBlocks.EAST_FACE -> {
                    colorRGB = colorEast;
                }
                case RenderBlocks.WEST_FACE -> {
                    colorRGB = colorWest;
                }
            }
            for (int j = 0; j < model.modelFaces[i].vertices.length; j++) {


                tessellator.addVertexTextureArrayWithUV(colorRGB, model.modelFaces[i].vertices[j].x,
                        model.modelFaces[i].vertices[j].y,
                        model.modelFaces[i].vertices[j].z, textureID, model.modelFaces[i].UVs[j][0], model.modelFaces[i].UVs[j][1]);
            }
            tessellator.addElementsCCW();
        }

        tessellator.toggleOrtho();
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_BACK);
        tessellator.drawTextureArray(thePlayer.isHoldingBlock() ? Assets.blockTextureArray : Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_CULL_FACE);
        tessellator.toggleOrtho();
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
