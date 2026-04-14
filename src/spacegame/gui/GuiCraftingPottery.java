package spacegame.gui;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.item.crafting.InWorldCraftingRecipe;
import spacegame.util.MathUtil;
import spacegame.item.Item;
import spacegame.render.*;
import spacegame.world.Chunk;
import spacegame.item.crafting.InWorld3DCraftingItem;

public final class GuiCraftingPottery extends GuiCrafting {
    public Button close;
    public int backgroundTexture;
    public int recipeOverlay;
    public int transparentBackground;
    public int fillableColor;
    public short outputBlockID;
    public int outline;
    public short selectedItemID = -1;
    public short outputItemID;
    public RecipeSelector[] selectableRecipes;
    public int x;
    public int y;
    public int z;

    public GuiCraftingPottery(CosmicEvolution cosmicEvolution, int x, int y, int z) {
        super(cosmicEvolution);
        this.x = x;
        this.y = y;
        this.z = z;
        this.close = new Button(EnumButtonEffects.CLOSE.name(), 50, 50, 349, 190, this, this.ce);
        this.selectableRecipes = new RecipeSelector[2];
        this.setSelectableItemIDs();
    }

    private void setSelectableItemIDs(){
        float selectableWidth = 64;
        float selectableHeight = 64;
        float selectableX = -320;
        float selectableY = 64;

        for(int i = 0; i < this.selectableRecipes.length; i++){
            switch (i) {
                case 0 -> this.selectableRecipes[i] = new RecipeSelector(Item.rawClayAdobeBrick.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.rawClayAdobeBrick.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.straw.ID, Item.clay.ID}, new int[]{1,1});
                case 1 -> this.selectableRecipes[i] = new RecipeSelector(Item.block.ID, Block.rawRedClayCookingPot.ID,selectableX, selectableY, selectableWidth, selectableHeight, Item.block.getDisplayName(Block.rawRedClayCookingPot.ID), new short[]{Item.clay.ID}, new int[]{16});
            }
            selectableX += 64;
        }
    }


    @Override
    public void loadTextures() {
        this.backgroundTexture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiCrafting/potteryBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.transparentBackground = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.fillableColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.outline = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/outline.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.backgroundTexture);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.transparentBackground);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.fillableColor);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.outline);
        if(this.recipeOverlay != 0){
            CosmicEvolution.instance.renderEngine.deleteTexture(this.recipeOverlay);
        }
    }

    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();
        GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        this.close.renderButton();

        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        int backgroundWidth = 1920;
        int backgroundHeight = 1017;
        int backgroundX = 0;
        int backgroundY = 0;
        int backgroundZ = -110;
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        backgroundWidth = 798;
        backgroundHeight = 480;
        backgroundX = 0;
        backgroundY = 0;
        backgroundZ = -100;
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 3);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 1);
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 2);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.backgroundTexture, Shader.screen2DTexture, CosmicEvolution.camera);

        if(this.selectedItemID != -1) {
            backgroundZ += 10;
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);

            int outputSlotX = 0;
            int outputSlotY = 0;
            int outputWidth = 256;
            int outputHeight = 256;

            tessellator.addVertex2DTexture(0, outputSlotX - (float) outputWidth / 2, outputSlotY - (float) outputHeight / 2, backgroundZ, 3);
            tessellator.addVertex2DTexture(0, outputSlotX + (float) outputWidth / 2, outputSlotY + (float) outputHeight / 2, backgroundZ, 1);
            tessellator.addVertex2DTexture(0, outputSlotX - (float) outputWidth / 2, outputSlotY + (float) outputHeight / 2, backgroundZ, 2);
            tessellator.addVertex2DTexture(0, outputSlotX + (float) outputWidth / 2, outputSlotY - (float) outputHeight / 2, backgroundZ, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);

            backgroundZ += 10;
            tessellator.addVertex2DTexture(16777215, outputSlotX - (float) outputWidth / 2, outputSlotY - (float) outputHeight / 2, backgroundZ, 3);
            tessellator.addVertex2DTexture(16777215, outputSlotX + (float) outputWidth / 2, outputSlotY + (float) outputHeight / 2, backgroundZ, 1);
            tessellator.addVertex2DTexture(16777215, outputSlotX - (float) outputWidth / 2, outputSlotY + (float) outputHeight / 2, backgroundZ, 2);
            tessellator.addVertex2DTexture(16777215, outputSlotX + (float) outputWidth / 2, outputSlotY - (float) outputHeight / 2, backgroundZ, 0);
            tessellator.addElements();

            GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(this.recipeOverlay, Shader.screen2DTexture, CosmicEvolution.camera);

            GL46.glDisable(GL46.GL_BLEND);
        }


    if(this.selectedItemID == -1) {
        float selectableZ = -90;
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        int transparentColor;
        for (int i = 0; i < this.selectableRecipes.length; i++) {
            transparentColor = !this.selectableRecipes[i].meetsCriteriaToMakeRecipe(CosmicEvolution.instance.save.thePlayer) ? 4210752 : this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526;
            tessellator.addVertex2DTexture(transparentColor, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
            tessellator.addVertex2DTexture(transparentColor, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
            tessellator.addVertex2DTexture(transparentColor, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
            tessellator.addVertex2DTexture(transparentColor, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
            tessellator.addElements();
        }

        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);

        selectableZ = -88;
        for (int i = 0; i < this.selectableRecipes.length; i++) {
            if (this.selectableRecipes[i].isBlock) continue;
            tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3, Item.list[this.selectableRecipes[i].itemID].getTextureID( this.selectableRecipes[i].itemID, Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1, Item.list[this.selectableRecipes[i].itemID].getTextureID( this.selectableRecipes[i].itemID, Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2, Item.list[this.selectableRecipes[i].itemID].getTextureID( this.selectableRecipes[i].itemID, Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0, Item.list[this.selectableRecipes[i].itemID].getTextureID( this.selectableRecipes[i].itemID, Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
            tessellator.addElements();
        }

        tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);

        for (int i = 0; i < this.selectableRecipes.length; i++) {
            if (!this.selectableRecipes[i].isBlock) continue;
            ModelLoader model = Block.list[this.selectableRecipes[i].blockID].blockModel.copyModel().translateModel(-0.5f, 0, -0.5f);
            ModelFace[] faces;
            float textureID;
            Vector3f vertex1;
            Vector3f vertex2;
            Vector3f vertex3;
            Vector3f vertex4;
            Vector3f position = new Vector3f(this.selectableRecipes[i].x, this.selectableRecipes[i].y - 16, -50);
            int red = 255;
            int green = 255;
            int blue = 255;
            float[] UVSamples;
            for (int face = 0; face < 6; face++) {
                faces = model.getModelFaceOfType(face);
                for (int j = 0; j < faces.length; j++) {
                    if (faces[j] == null) continue;
                    textureID = Block.list[this.selectableRecipes[i].blockID].getBlockTexture(this.selectableRecipes[i].blockID, face);
                    UVSamples = face == RenderBlocks.TOP_FACE || face == RenderBlocks.BOTTOM_FACE ? RenderBlocks.autoUVTopBottom(RenderBlocks.getFaceWidth(faces[j]), RenderBlocks.getFaceHeight(faces[j])) : RenderBlocks.autoUVNSEW(RenderBlocks.getFaceWidth(faces[j]), RenderBlocks.getFaceHeight(faces[j]));
                    vertex1 = new Vector3f(faces[j].vertices[0].x, faces[j].vertices[0].y, faces[j].vertices[0].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                    vertex2 = new Vector3f(faces[j].vertices[1].x, faces[j].vertices[1].y, faces[j].vertices[1].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                    vertex3 = new Vector3f(faces[j].vertices[2].x, faces[j].vertices[2].y, faces[j].vertices[2].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                    vertex4 = new Vector3f(faces[j].vertices[3].x, faces[j].vertices[3].y, faces[j].vertices[3].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);

                    tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex1.x, vertex1.y, vertex1.z, 3, textureID, UVSamples[0], UVSamples[1]);
                    tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex2.x, vertex2.y, vertex2.z, 1, textureID, UVSamples[2], UVSamples[3]);
                    tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex3.x, vertex3.y, vertex3.z, 2, textureID, UVSamples[4], UVSamples[5]);
                    tessellator.addVertexTextureArrayWithSampling(((red << 16) | (green << 8) | blue), vertex4.x, vertex4.y, vertex4.z, 0, textureID, UVSamples[6], UVSamples[7]);
                    tessellator.addElements();

                    red -= 10;
                    green -= 10;
                    blue -= 10;
                }
            }
            tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
        }


        selectableZ = -87;
        for (int i = 0; i < this.selectableRecipes.length; i++) {
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
            tessellator.addElements();
        }


        tessellator.drawTexture2D(this.outline, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);


        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString("Select Recipe", 0, 128, -15, 16777215, 50, 255);
    }
        tessellator.toggleOrtho();

        RecipeSelector hoveredRecipe = this.getSelectedRecipeSelector();
        if(hoveredRecipe != null){
            FontRenderer fontRenderer = FontRenderer.instance;
            tessellator.toggleOrtho();
            String displayedName = hoveredRecipe.displayName;
            float x = MathUtil.getOpenGLMouseX();
            float y = MathUtil.getOpenGLMouseY();
            int font = 50;
            float height = font + 64 + (hoveredRecipe.requiredItemCount.length * 64);
            float width = font * ((displayedName.length() + 2) * 0.34f);

            tessellator.addVertex2DTexture(0, x, y, -10, 3);
            tessellator.addVertex2DTexture(0, x + width, y + height, -10, 1);
            tessellator.addVertex2DTexture(0, x, y + height, -10, 2);
            tessellator.addVertex2DTexture(0, x + width, y, -10, 0);
            tessellator.addElements();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
            GL46.glDisable(GL46.GL_BLEND);
            tessellator.toggleOrtho();

            y += height - 50;
            fontRenderer.drawString(displayedName, x, y , -9, 16777215, font, 255);
            y -= 60;
            fontRenderer.drawString("Requires", x, y, -9, 16777215, font, 255);
            y -= 64;
            for(int i = 0; i < hoveredRecipe.requiredItems.length; i++){
                fontRenderer.drawString(hoveredRecipe.requiredItemCount[i] + "x: ", x, y, -9, 16777215, 50, 255);
                x += 64 + (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);
                y -= 8;
                tessellator.toggleOrtho();
                tessellator.addVertexTextureArray(16777215, x, y, -9, 3, Item.list[hoveredRecipe.requiredItems[i]].getTextureID(hoveredRecipe.requiredItems[i], Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x + 64, y + 64, -9, 1, Item.list[hoveredRecipe.requiredItems[i]].getTextureID(hoveredRecipe.requiredItems[i], Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x, y + 64, -9, 2, Item.list[hoveredRecipe.requiredItems[i]].getTextureID(hoveredRecipe.requiredItems[i], Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x + 64, y, -9, 0, Item.list[hoveredRecipe.requiredItems[i]].getTextureID(hoveredRecipe.requiredItems[i], Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
                tessellator.addElements();
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                GL46.glDisable(GL46.GL_BLEND);
                tessellator.toggleOrtho();
                y += 8;
                x -= 64 - (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);;
                y -= 64;
            }
        }
    }

    public void handleLeftClick(){
        RecipeSelector recipeSelector = this.getSelectedRecipeSelector();


        if(recipeSelector != null) {
            if (recipeSelector.meetsCriteriaToMakeRecipe(CosmicEvolution.instance.save.thePlayer)) {
                CosmicEvolution.instance.save.activeWorld.setBlockWithNotify(this.x, this.y, this.z, Block.crafting3DItem.ID, true);
                InWorld3DCraftingItem craftingBlock = new InWorld3DCraftingItem(Chunk.getBlockIndexFromCoordinates(x, y, z), Block.clay.ID, this.getInWorldCraftingRecipeName(recipeSelector.itemID), CosmicEvolution.instance.save.activeWorld.findChunkFromChunkCoordinates(this.x >> 5, this.y >> 5, this.z >> 5));
                if (recipeSelector.itemID != Item.block.ID) {
                    craftingBlock.activateCraftingLayer(0);
                } else if (recipeSelector.blockID == Block.rawRedClayCookingPot.ID) {
                    craftingBlock.activateCraftingLayer(0);
                    craftingBlock.activeCraftingLayer++;
                }
                CosmicEvolution.instance.save.activeWorld.addInWorldCrafting3DItem(this.x, this.y, this.z, craftingBlock);
                GLFW.glfwSetInputMode(CosmicEvolution.instance.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                CosmicEvolution.instance.setNewGui(new GuiInGame(CosmicEvolution.instance));
                recipeSelector.removeRequiredItemsFromInventory(CosmicEvolution.instance.save.thePlayer);
            }
        }
    }

    private InWorldCraftingRecipe getInWorldCraftingRecipeName(short itemID){
        switch (itemID){
            case 0 -> {
                switch (this.getSelectedRecipeSelector().blockID){
                    case 73 -> {
                        return InWorldCraftingRecipe.rawCookingPot;
                    }
                }
            }

            case 15 -> {
                return InWorldCraftingRecipe.rawBrick;
            }
        }
        throw new RuntimeException(itemID + " does not have a valid 3D crafting recipe template, was an invalid item added to the list?");
    }





    @Override
    public Button getActiveButton() {
        if(this.close.isMouseHoveredOver() && this.close.active){
            return this.close;
        }
        return null;
    }




    @Override
    public RecipeSelector getSelectedRecipeSelector() {
        if(this.selectableRecipes == null)return null;
        for(int i = 0; i < this.selectableRecipes.length; i++){
            if(this.selectableRecipes[i].isMouseHoveredOver()){
                return this.selectableRecipes[i];
            }
        }
        return null;
    }
}
