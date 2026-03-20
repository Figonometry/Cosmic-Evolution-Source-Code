package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.item.InWorldCraftingRecipe;
import spacegame.util.MathUtil;
import spacegame.item.Item;
import spacegame.item.ItemCraftingTemplates;
import spacegame.render.Assets;
import spacegame.render.RenderBlocks;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.Chunk;
import spacegame.world.InWorldCraftingBlock;

import java.util.Random;

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
                case 3 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneFragments.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.stoneFragments.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.stone.ID}, new int[]{1}, ItemCraftingTemplates.stoneFragments.indices);
                case 0 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandAxe.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.stoneHandAxe.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.stone.ID}, new int[]{1}, ItemCraftingTemplates.stoneHandAxe.indices);
                case 1 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandKnifeBlade.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.stoneHandKnifeBlade.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.stone.ID}, new int[]{1}, ItemCraftingTemplates.knifeBlade.indices);
                case 2 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandShovel.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.stoneHandShovel.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.stone.ID}, new int[]{1}, ItemCraftingTemplates.stoneHandShovel.indices);
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
        int backgroundZ = -110;
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 0);
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
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 3);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 1);
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth /2, backgroundY + (float) backgroundHeight /2, backgroundZ, 2);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth /2, backgroundY - (float) backgroundHeight /2, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.backgroundTexture, Shader.screen2DTexture, CosmicEvolution.camera);


        if(this.selectedItemID == -1){
            float selectableZ = -90;
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            for(int i = 0; i < this.selectableRecipes.length; i++){
                tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
                tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
                tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
                tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
                tessellator.addElements();
            }

            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);

            selectableZ = -88;
            for(int i = 0; i < this.selectableRecipes.length; i++){
                tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
                tessellator.addElements();
            }


            tessellator.drawVertexArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);

            selectableZ = -87;
            for(int i = 0; i < this.selectableRecipes.length; i++){
                tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
                tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
                tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
                tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
                tessellator.addElements();
            }


            tessellator.drawTexture2D(this.outline, Shader.screen2DTexture, CosmicEvolution.camera);
            GL46.glDisable(GL46.GL_BLEND);
        }

        tessellator.toggleOrtho();


        if(this.selectedItemID == -1){
            FontRenderer fontRenderer = FontRenderer.instance;
            fontRenderer.drawCenteredString("Select Recipe", 0, 128, -15, 16777215, 50, 255);
        }

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
                tessellator.addVertexTextureArray(16777215, x, y, -9, 3, hoveredRecipe.requiredItems[i], RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x + 64, y + 64, -9, 1, hoveredRecipe.requiredItems[i], RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x, y + 64, -9, 2, hoveredRecipe.requiredItems[i], RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x + 64, y, -9, 0, hoveredRecipe.requiredItems[i], RenderBlocks.WEST_FACE);
                tessellator.addElements();
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                tessellator.drawVertexArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                GL46.glDisable(GL46.GL_BLEND);
                tessellator.toggleOrtho();
                y += 8;
                x -= 64 - (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);;
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
            InWorldCraftingBlock craftingBlock = new InWorldCraftingBlock(Chunk.getBlockIndexFromCoordinates(this.x, this.y, this.z), Block.stone.ID, this.getInWorldCraftingRecipeName(recipeSelector.itemID), CosmicEvolution.instance.save.activeWorld.findChunkFromChunkCoordinates(this.x >> 5, this.y  >> 5, this.z >> 5));
            craftingBlock.activateCraftingLayer(0);
            CosmicEvolution.instance.save.activeWorld.addInWorldCraftingBlock(this.x, this.y, this.z, craftingBlock);
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
