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
    public Button close;
    public int inventoryUI;
    public int recipeOverlay;
    public int transparentBackground;
    public int fillableColor;
    public int outline;
    public CraftingMaterial[] materials;
    public short selectedItemID = -1;
    public short outputItemID;
    public RecipeSelector[] selectableRecipes;
    public int x;
    public int y;
    public int z;
    public GuiCraftingStoneTools(SpaceGame spaceGame, int x, int y, int z) {
        super(spaceGame);
        this.x = x;
        this.y = y;
        this.z = z;
        this.close = new Button(EnumButtonEffects.CLOSE.name(), 50, 50, 349, 190, this, this.sg);
        this.craft = new Button(EnumButtonEffects.CRAFT.name(), 128,50, 0,-175, this, this.sg);
        this.craft.active = false;
        this.selectableRecipes = new RecipeSelector[4];
        this.setSelectableItemIDs();
        this.materials = new CraftingMaterial[64];
        int xMat; //base -370
        int yMat; //base -120
        Random rand = new Random();
        float color;
        for(int i = 0 ; i < this.materials.length; i++){
            color = rand.nextFloat(0.4f, 0.7f);
            xMat = -112 + ((i % 8) * 32);
            yMat = -112 + ((i / 8) * 32);
            this.materials[i] = new CraftingMaterial(32, 32, xMat, yMat, "stone");
            this.materials[i].setColor(new Color(color, color, color, 0).getRGB());
        }
    }

    private void setSelectableItemIDs(){
        float selectableWidth = 64;
        float selectableHeight = 64;
        float selectableX = -320;
        float selectableY = 64;

        for(int i = 0; i < this.selectableRecipes.length; i++){
            switch (i) {
                case 3 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneFragments.ID, selectableX, selectableY, selectableWidth, selectableHeight);
                case 0 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandAxe.ID, selectableX, selectableY, selectableWidth, selectableHeight);
                case 1 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandKnifeBlade.ID, selectableX, selectableY, selectableWidth, selectableHeight);
                case 2 -> this.selectableRecipes[i] = new RecipeSelector(Item.stoneHandShovel.ID, selectableX, selectableY, selectableWidth, selectableHeight);
            }
            selectableX += 64;
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
        this.inventoryUI = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/playerInventory.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.transparentBackground = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.fillableColor = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.outline = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/outline.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void deleteTextures() {
        SpaceGame.instance.renderEngine.deleteTexture(this.inventoryUI);
        SpaceGame.instance.renderEngine.deleteTexture(this.transparentBackground);
        SpaceGame.instance.renderEngine.deleteTexture(this.fillableColor);
        SpaceGame.instance.renderEngine.deleteTexture(this.outline);
        if(this.recipeOverlay != 0){
            SpaceGame.instance.renderEngine.deleteTexture(this.recipeOverlay);
        }
    }

    @Override
    public void drawGui() {
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        this.close.renderButton();
        if(this.selectedItemID != -1) {
            this.renderCraftingMaterials();
            this.craft.active = this.isOutputComplete();
            this.craft.renderButton();
        }

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
            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);

            backgroundZ += 10;
            tessellator.addVertex2DTexture(16777215, outputSlotX - (float) outputWidth / 2, outputSlotY - (float) outputHeight / 2, backgroundZ, 3);
            tessellator.addVertex2DTexture(16777215, outputSlotX + (float) outputWidth / 2, outputSlotY + (float) outputHeight / 2, backgroundZ, 1);
            tessellator.addVertex2DTexture(16777215, outputSlotX - (float) outputWidth / 2, outputSlotY + (float) outputHeight / 2, backgroundZ, 2);
            tessellator.addVertex2DTexture(16777215, outputSlotX + (float) outputWidth / 2, outputSlotY - (float) outputHeight / 2, backgroundZ, 0);
            tessellator.addElements();

            GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(this.recipeOverlay, Shader.screen2DTexture, SpaceGame.camera);

            GL46.glDisable(GL46.GL_BLEND);
        }

        if(this.selectedItemID == -1){
            float selectableZ = -90;

            for(int i = 0; i < this.selectableRecipes.length; i++){
                tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
                tessellator.addElements();
            }


            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawVertexArray(Assets.itemTextureArray, Shader.screenTextureArray, SpaceGame.camera);

            selectableZ = -89;
            for(int i = 0; i < this.selectableRecipes.length; i++){
                tessellator.addVertex2DTexture(0, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
                tessellator.addVertex2DTexture(0, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
                tessellator.addVertex2DTexture(0, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
                tessellator.addVertex2DTexture(0, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
                tessellator.addElements();
            }


            tessellator.drawTexture2D(this.outline, Shader.screen2DTexture, SpaceGame.camera);

            selectableZ = -88;
            for(int i = 0; i < this.selectableRecipes.length; i++){
                tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 0, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
                tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 0, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
                tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 0, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
                tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 0, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
                tessellator.addElements();
            }

            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
            GL46.glDisable(GL46.GL_BLEND);
        }

        tessellator.toggleOrtho();


        if(this.selectedItemID == -1){
            FontRenderer fontRenderer = FontRenderer.instance;
            fontRenderer.drawCenteredString("Select Recipe", 0, 128, -15, 16777215, 50);
        }
    }

    public void activateRecipeOverlay(){
        switch (this.selectedItemID) {
            case 3 ->
                    this.recipeOverlay = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/recipeTemplates/rockFragmentTemplate.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            case 4 ->
                    this.recipeOverlay = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/recipeTemplates/handAxeTemplate.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            case 9 ->
                    this.recipeOverlay = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/recipeTemplates/knifeTemplate.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            case 10 ->
                    this.recipeOverlay = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/recipeTemplates/shovelHeadTemplate.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        }
    }

    private void renderCraftingMaterials(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        int color;
        float x;
        float y;
        float height;
        float width;
        float z = -95;
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

    private boolean isOutputComplete(){
        switch (this.selectedItemID) {
            case 4 -> {
                if (this.doesInputMatchHandAxeTemplate()) {
                    this.setOutputItem(Item.stoneHandAxe.ID);
                    return true;
                }
            }
            case 3 -> {
                if (this.doesInputMatchStoneFragmentsTemplate()) {
                    this.setOutputItem(Item.stoneFragments.ID);
                    return true;
                }
            }
            case 9 -> {
                if (this.doesInputMatchKnifeTemplate()) {
                    this.setOutputItem(Item.stoneHandKnifeBlade.ID);
                    return true;
                }
            }
            case 10 -> {
                if (this.doesInputMatchHandShovelTemplate()) {
                    this.setOutputItem(Item.stoneHandShovel.ID);
                    return true;
                }
            }
        }


        return false;
    }

    private boolean doesInputMatchStoneFragmentsTemplate(){
        short countCorrect = 0;
        boolean squareActive;
        for(int i = 0; i < ItemCraftingTemplates.stoneFragments.indices.length; i++) {
            countCorrect = 0;
            for (int j = 0; j < this.materials.length; j++) {
                squareActive = this.materials[j].active;
                if(squareActive){
                    if(this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.stoneFragments.indices)){
                        countCorrect++;
                    }
                } else {
                    if(!this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.stoneFragments.indices)){
                        countCorrect++;
                    }
                }
            }
            if(countCorrect / 64f == 1f){
                return true;
            }
        }
        return false;
    }

    private boolean doesInputMatchHandShovelTemplate(){
        short countCorrect = 0;
        boolean squareActive;
        for(int i = 0; i < ItemCraftingTemplates.stoneHandShovel.indices.length; i++) {
            countCorrect = 0;
            for (int j = 0; j < this.materials.length; j++) {
                squareActive = this.materials[j].active;
                if(squareActive){
                    if(this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.stoneHandShovel.indices)){
                        countCorrect++;
                    }
                } else {
                    if(!this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.stoneHandShovel.indices)){
                        countCorrect++;
                    }
                }
            }
            if(countCorrect / 64f == 1f){
                return true;
            }
        }
        return false;
    }

    private boolean doesInputMatchKnifeTemplate(){
        short countCorrect = 0;
        boolean squareActive;
        for(int i = 0; i < ItemCraftingTemplates.knifeBlade.indices.length; i++) {
            countCorrect = 0;
            for (int j = 0; j < this.materials.length; j++) {
                squareActive = this.materials[j].active;
                if(squareActive){
                    if(this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.knifeBlade.indices)){
                        countCorrect++;
                    }
                } else {
                    if(!this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.knifeBlade.indices)){
                        countCorrect++;
                    }
                }
            }
            if(countCorrect / 64f == 1f){
                return true;
            }
        }
        return false;
    }

    private boolean doesInputMatchHandAxeTemplate(){
        short countCorrect = 0;
        boolean squareActive;
        for(int i = 0; i < ItemCraftingTemplates.stoneHandAxe.indices.length; i++) {
            countCorrect = 0;
            for (int j = 0; j < this.materials.length; j++) {
                squareActive = this.materials[j].active;
                if(squareActive){
                    if(this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.stoneHandAxe.indices)){
                        countCorrect++;
                    }
                } else {
                    if(!this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.stoneHandAxe.indices)){
                        countCorrect++;
                    }
                }
            }
            if(countCorrect / 64f == 1f){
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


    private void setOutputItem(short itemID){
        this.outputItemID = itemID;
    }

    @Override
    public Button getActiveButton() {
        if(this.craft.isMouseHoveredOver() && this.craft.active){
            return this.craft;
        }
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


}
