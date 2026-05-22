package spacegame.gui;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.item.crafting.InWorldCraftingRecipe;
import spacegame.render.model.ModelFace;
import spacegame.render.model.ModelLoader;
import spacegame.util.MathUtil;
import spacegame.item.Item;
import spacegame.render.Assets;
import spacegame.render.RenderBlocks;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.Chunk;
import spacegame.item.crafting.InWorld3DCraftingItem;

public final class GuiCraftingStoneTools extends GuiCrafting {
    public Button close;
    public int backgroundTexture;
    public int recipeOverlay;
    public int transparentBackground;
    public int fillableColor;
    public int outline;
    public short selectedItemID = -1;
    public short outputItemID;
    public RecipeSelector[] selectableRecipes;
    public RecipeSelector activeRecipe;
    public int x;
    public int y;
    public int z;
    public GuiCraftingStoneTools(CosmicEvolution cosmicEvolution, int x, int y, int z) {
        super(cosmicEvolution);
        this.x = x;
        this.y = y;
        this.z = z;
        this.close = new Button(EnumButtonEffects.CLOSE.name(), 50, 50, 349, 190, this, this.ce);
        this.selectableRecipes = new RecipeSelector[4];
        this.setSelectableItemIDs();
    }

    private void setSelectableItemIDs(){
        float selectableWidth = 64;
        float selectableHeight = 64;
        float selectableX = -320;
        float selectableY = 64;

        for(int i = 0; i < this.selectableRecipes.length; i++){
            switch (i) {
                case 3 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneFragments.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.stoneFragments.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.block.ID}, new int[]{1}, new short[]{Block.itemStone.ID});
                case 0 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandAxe.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.stoneHandAxe.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.block.ID}, new int[]{1}, new short[]{Block.itemStone.ID});
                case 1 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandKnifeBlade.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.stoneHandKnifeBlade.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.block.ID}, new int[]{1}, new short[]{Block.itemStone.ID});
                case 2 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandShovel.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.stoneHandShovel.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.block.ID}, new int[]{1}, new short[]{Block.itemStone.ID});
            }
            selectableX += 64;
        }
    }


    @Override
    public void loadTextures() {
        this.backgroundTexture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/clayTexture.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
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
        int backgroundZ = -1100;
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 0);
        tessellator.addElementsCW();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        backgroundWidth = 798;
        backgroundHeight = 480;
        backgroundX = 0;
        backgroundY = 0;
        backgroundZ = -1000;
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 3);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 1);
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 2);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 0);
        tessellator.addElementsCW();
        tessellator.drawTexture2D(this.backgroundTexture, Shader.screen2DTexture, CosmicEvolution.camera);


        if(this.selectedItemID == Item.NULL_ITEM_REFERENCE){
            float selectableZ = -900;
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            int transparentColor;
            for(int i = 0; i < this.selectableRecipes.length; i++){
                transparentColor = !this.selectableRecipes[i].meetsCriteriaToMakeRecipe(CosmicEvolution.instance.save.thePlayer) ? 4210752 : this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526;
                tessellator.addVertex2DTexture(transparentColor, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
                tessellator.addVertex2DTexture(transparentColor, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
                tessellator.addVertex2DTexture(transparentColor, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
                tessellator.addVertex2DTexture(transparentColor, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
                tessellator.addElementsCW();
            }

            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);

            selectableZ = -880;
            for (int i = 0; i < this.selectableRecipes.length; i++) {
                if (this.selectableRecipes[i].isBlock) continue;
                ModelLoader model = Item.list[this.selectableRecipes[i].itemID].itemModel.copyModel();
                model.scaleModel(76f);
                model.rotateModel(45, 0, 1, 0);
                model.rotateModel(36, 1, 0, 0);
                Vector3f position = new Vector3f(this.selectableRecipes[i].x, this.selectableRecipes[i].y, -50);
                model.translateModel(position.x, position.y, position.z);
                ModelFace[] faces;
                float textureID;
                int colorRGB = 255;
                int colorTop = 16777215;

                int colorVal = colorRGB;

                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;

                for (int face = 0; face < 6; face++) {
                    faces = model.getModelFaceOfType(face);
                    for (int j = 0; j < faces.length; j++) {
                        if (faces[j] == null) continue;
                        textureID = faces[j].texture;

                        switch (faces[j].faceType){
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

                        tessellator.addVertexTextureArrayWithUV(colorRGB, faces[j].vertices[0].x, faces[j].vertices[0].y, faces[j].vertices[0].z, textureID, faces[j].UVs[0][0], faces[j].UVs[0][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, faces[j].vertices[1].x, faces[j].vertices[1].y, faces[j].vertices[1].z, textureID, faces[j].UVs[1][0], faces[j].UVs[1][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, faces[j].vertices[2].x, faces[j].vertices[2].y, faces[j].vertices[2].z, textureID, faces[j].UVs[2][0], faces[j].UVs[2][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, faces[j].vertices[3].x, faces[j].vertices[3].y, faces[j].vertices[3].z, textureID, faces[j].UVs[3][0], faces[j].UVs[3][1]);
                        tessellator.addElementsCCW();
                    }
                }
                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
            }

            selectableZ = -870;
            for(int i = 0; i < this.selectableRecipes.length; i++){
                tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
                tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
                tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
                tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
                tessellator.addElementsCW();
            }


            tessellator.drawTexture2D(this.outline, Shader.screen2DTexture, CosmicEvolution.camera);
            GL46.glDisable(GL46.GL_BLEND);
        }

        tessellator.toggleOrtho();


        if(this.selectedItemID == Item.NULL_ITEM_REFERENCE){
            FontRenderer fontRenderer = FontRenderer.instance;
            fontRenderer.drawCenteredString("Select Recipe", 0, 128, -15, 16777215, 50, 255);
        }

        RecipeSelector hoveredRecipe = this.getSelectedRecipeSelector();
        if(hoveredRecipe != null) {
            FontRenderer fontRenderer = FontRenderer.instance;
            tessellator.toggleOrtho();
            String displayedName = hoveredRecipe.displayName;
            float x = MathUtil.getOpenGLMouseX();
            float y = MathUtil.getOpenGLMouseY();
            int font = 50;
            float height = font + 64 + (hoveredRecipe.requiredItemCount.length * 64);
            float width = font * ((displayedName.length() + 2) * 0.34f);

            tessellator.addVertex2DTexture(0, x, y, -500, 3);
            tessellator.addVertex2DTexture(0, x + width, y + height, -500, 1);
            tessellator.addVertex2DTexture(0, x, y + height, -500, 2);
            tessellator.addVertex2DTexture(0, x + width, y, -500, 0);
            tessellator.addElementsCW();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
            GL46.glDisable(GL46.GL_BLEND);
            tessellator.toggleOrtho();

            y += height - 50;
            fontRenderer.drawString(displayedName, x, y, -9, 16777215, font, 255);
            y -= 60;
            fontRenderer.drawString("Requires", x, y, -9, 16777215, font, 255);
            y -= 64;
            for (int i = 0; i < hoveredRecipe.requiredItems.length; i++) {
                if (hoveredRecipe.requiredItems[i] == Item.block.ID) continue;
                fontRenderer.drawString(hoveredRecipe.requiredItemCount[i] + "x: ", x, y, -9, 16777215, 50, 255);
                x += 64 + (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);
                y -= 8;
                tessellator.toggleOrtho();
                tessellator.addVertexTextureArrayWithCorner(16777215, x, y, -9, 3, Item.list[hoveredRecipe.requiredItems[i]].getTextureID(hoveredRecipe.requiredItems[i], Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE));
                tessellator.addVertexTextureArrayWithCorner(16777215, x + 64, y + 64, -9, 1, Item.list[hoveredRecipe.requiredItems[i]].getTextureID(hoveredRecipe.requiredItems[i], Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE));
                tessellator.addVertexTextureArrayWithCorner(16777215, x, y + 64, -9, 2, Item.list[hoveredRecipe.requiredItems[i]].getTextureID(hoveredRecipe.requiredItems[i], Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE));
                tessellator.addVertexTextureArrayWithCorner(16777215, x + 64, y, -9, 0, Item.list[hoveredRecipe.requiredItems[i]].getTextureID(hoveredRecipe.requiredItems[i], Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE));
                tessellator.addElementsCW();
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                GL46.glDisable(GL46.GL_BLEND);
                tessellator.toggleOrtho();
                y += 8;
                x -= 64 - (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);
                y -= 64;
            }

            for (int i = 0; i < hoveredRecipe.requiredItems.length; i++) {
                if (hoveredRecipe.requiredItems[i] != Item.block.ID) continue;
                fontRenderer.drawString(hoveredRecipe.requiredItemCount[i] + "x: ", x, y, -9, 16777215, 50, 255);
                x += 64 + (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);
                ModelLoader model = Block.list[hoveredRecipe.requiredItemMetadata[i]].blockModel.copyModel();
                model.translateModel(-0.5f, 0, -0.5f);
                model.scaleModel(38f);
                model.rotateModel(45, 0, 1, 0);
                model.rotateModel(36, 1, 0, 0);
                model.translateModel(0.5f, 0, 0.5f);
                model.translateModel(x + 64, y + 16, -100);
                ModelFace modelFace;
                float textureID;
                int red = 255;
                int green = 255;
                int blue = 255;

                for (int face = 0; face < model.modelFaces.length; face++) {
                    modelFace = model.modelFaces[face];
                    if (modelFace == null) continue;
                    textureID = Block.list[hoveredRecipe.requiredItemMetadata[i]].getBlockTexture(hoveredRecipe.requiredItemMetadata[i], modelFace.faceType);

                    tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), modelFace.vertices[0].x, modelFace.vertices[0].y, modelFace.vertices[0].z, textureID, modelFace.UVs[0][0], modelFace.UVs[0][1]);
                    tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), modelFace.vertices[1].x, modelFace.vertices[1].y, modelFace.vertices[1].z, textureID, modelFace.UVs[1][0], modelFace.UVs[1][1]);
                    tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), modelFace.vertices[2].x, modelFace.vertices[2].y, modelFace.vertices[2].z, textureID, modelFace.UVs[2][0], modelFace.UVs[2][1]);
                    tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), modelFace.vertices[3].x, modelFace.vertices[3].y, modelFace.vertices[3].z, textureID, modelFace.UVs[3][0], modelFace.UVs[3][1]);
                    tessellator.addElementsCCW();

                    red -= 10;
                    green -= 10;
                    blue -= 10;
                }
                tessellator.toggleOrtho();
                tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                tessellator.toggleOrtho();
                y += 8;
                x -= 64 - (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);
                y -= 64;
            }
        }
    }


    @Override
    public Button getActiveButton() {
        if(this.close.isMouseHoveredOver() && this.close.active){
            return this.close;
        }
        return null;
    }



    public RecipeSelector getSelectedRecipeSelector(){
        if(this.selectableRecipes == null)return null;
        for(int i = 0; i < this.selectableRecipes.length; i++){
            if(this.selectableRecipes[i].isMouseHoveredOver()){
                return this.selectableRecipes[i];
            }
        }
        return null;
    }


    public void handleLeftClick(){
        RecipeSelector recipeSelector = this.getSelectedRecipeSelector();
        if(recipeSelector != null){
            CosmicEvolution.instance.save.activeWorld.setBlockWithNotify(this.x, this.y, this.z, Block.crafting3DItem.ID, true);
            InWorld3DCraftingItem craftingBlock = new InWorld3DCraftingItem(Chunk.getBlockIndexFromCoordinates(this.x, this.y, this.z), Block.stone.ID, this.getInWorldCraftingRecipeName(recipeSelector.itemID), CosmicEvolution.instance.save.activeWorld.findChunkFromChunkCoordinates(this.x >> 5, this.y  >> 5, this.z >> 5));
            craftingBlock.activateCraftingLayer(0);
            CosmicEvolution.instance.save.activeWorld.addInWorldCrafting3DItem(this.x, this.y, this.z, craftingBlock);
            GLFW.glfwSetInputMode(CosmicEvolution.instance.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
            CosmicEvolution.instance.setNewGui(new GuiInGame(CosmicEvolution.instance));
        }
    }

    private InWorldCraftingRecipe getInWorldCraftingRecipeName(short itemID){
        switch (itemID){
            case 3 -> {
                return InWorldCraftingRecipe.rockFragments;
            }
            case 4 -> {
                return InWorldCraftingRecipe.axe;
            }
            case 9 -> {
                return InWorldCraftingRecipe.knife;
            }
            case 10 -> {
                return InWorldCraftingRecipe.shovel;
            }
        }

        throw new IllegalArgumentException(itemID + " does not have a valid 3D crafting recipe template, was an invalid item added to the list?");
    }


}
