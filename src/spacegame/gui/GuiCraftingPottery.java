package spacegame.gui;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.MathUtil;
import spacegame.core.SpaceGame;
import spacegame.item.Item;
import spacegame.item.ItemCraftingTemplates;
import spacegame.render.*;

import java.util.Random;

public final class GuiCraftingPottery extends GuiCrafting {
    public Button craft;
    public Button close;
    public int backgroundTexture;
    public int recipeOverlay;
    public int transparentBackground;
    public int fillableColor;
    public short outputBlockID;
    public int outline;
    public CraftingMaterial[] materials;
    public short selectedItemID = -1;
    public short outputItemID;
    public RecipeSelector[] selectableRecipes;
    public RecipeSelector activeRecipe;
    public int x;
    public int y;
    public int z;

    public GuiCraftingPottery(SpaceGame spaceGame, int x, int y, int z) {
        super(spaceGame);
        this.x = x;
        this.y = y;
        this.z = z;
        this.close = new Button(EnumButtonEffects.CLOSE.name(), 50, 50, 349, 190, this, this.sg);
        this.craft = new Button(EnumButtonEffects.CRAFT.name(), 128,50, 0,-175, this, this.sg);
        this.craft.active = false;
        this.selectableRecipes = new RecipeSelector[2];
        this.setSelectableItemIDs();
        this.materials = new CraftingMaterial[64];
        int xMat; //base -370
        int yMat; //base -120
        Random rand = new Random();
        int color = 148 << 16 | 89 << 8 | 80;
        int colorVariance;
        int red;
        int green;
        int blue;
        for(int i = 0 ; i < this.materials.length; i++){
            colorVariance = rand.nextInt(-32, 32);
            xMat = -112 + ((i % 8) * 32);
            yMat = -112 + ((i / 8) * 32);
            this.materials[i] = new CraftingMaterial(32, 32, xMat, yMat, "clay");
            red = (color >> 16) & 255;
            green = (color >> 8) & 255;
            blue = color & 255;
            this.materials[i].setColor((red + colorVariance) << 16 | (green + colorVariance) << 8 | blue + colorVariance);
        }
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

    public void activateRecipeOverlay(){
        switch (this.selectedItemID) {
            case 0 ->
                    this.recipeOverlay = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/recipeTemplates/cookingPot.png", RenderEngine.TEXTURE_TYPE_2D, 0);
            case 15 ->
                    this.recipeOverlay = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/recipeTemplates/brick.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        }
    }

    @Override
    public void loadTextures() {
        this.backgroundTexture = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiCrafting/potteryBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.transparentBackground = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.fillableColor = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.outline = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/outline.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void deleteTextures() {
        SpaceGame.instance.renderEngine.deleteTexture(this.backgroundTexture);
        SpaceGame.instance.renderEngine.deleteTexture(this.transparentBackground);
        SpaceGame.instance.renderEngine.deleteTexture(this.fillableColor);
        SpaceGame.instance.renderEngine.deleteTexture(this.outline);
        if(this.recipeOverlay != 0){
            SpaceGame.instance.renderEngine.deleteTexture(this.recipeOverlay);
        }
    }

    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();
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
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 0);
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
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 3);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 1);
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 2);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.backgroundTexture, Shader.screen2DTexture, SpaceGame.camera);

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


    if(this.selectedItemID == -1) {
        float selectableZ = -90;
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        for (int i = 0; i < this.selectableRecipes.length; i++) {
            tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
            tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
            tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
            tessellator.addVertex2DTexture(this.selectableRecipes[i].isMouseHoveredOver() ? 6001079 : 32526, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
            tessellator.addElements();
        }

        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);

        selectableZ = -88;
        for (int i = 0; i < this.selectableRecipes.length; i++) {
            if (this.selectableRecipes[i].isBlock) continue;
            tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0, this.selectableRecipes[i].itemID, RenderBlocks.WEST_FACE);
            tessellator.addElements();
        }

        tessellator.drawVertexArray(Assets.itemTextureArray, Shader.screenTextureArray, SpaceGame.camera);

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
            tessellator.drawVertexArray(Assets.blockTextureArray, Shader.screenTextureArray, SpaceGame.camera);
        }


        selectableZ = -87;
        for (int i = 0; i < this.selectableRecipes.length; i++) {
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
            tessellator.addElements();
        }


        tessellator.drawTexture2D(this.outline, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);


        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString("Select Recipe", 0, 128, -15, 16777215, 50);
    }
        tessellator.toggleOrtho();

        RecipeSelector hoveredRecipe = this.getSelectedRecipeSelector();
        if (hoveredRecipe != null) {
            tessellator.toggleOrtho();
            String displayedName = hoveredRecipe.displayName;
            float x = MathUtil.getOpenGLMouseX();
            float y = MathUtil.getOpenGLMouseY();
            int font = 50;
            float height = font;
            float width = font * ((displayedName.length() + 2) * 0.34f);

            tessellator.addVertex2DTexture(0, x, y, -10, 3);
            tessellator.addVertex2DTexture(0, x + width, y + height, -10, 1);
            tessellator.addVertex2DTexture(0, x, y + height, -10, 2);
            tessellator.addVertex2DTexture(0, x + width, y, -10, 0);
            tessellator.addElements();
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
            GL46.glDisable(GL46.GL_BLEND);
            tessellator.toggleOrtho();


            FontRenderer fontRenderer = FontRenderer.instance;
            fontRenderer.drawString(displayedName, x, y, -9, 16777215, font);
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
            case 15 -> {
                if (this.doesInputMatchBrickTemplate()) {
                    this.setOutputItem(Item.rawClayAdobeBrick.ID);
                    return true;
                }
            }
            case 0 -> {
                if (this.doesInputMatchCookingPotTemplate()) {
                    this.setOutputItem(Item.block.ID);
                    this.setOutputBlockID(Block.rawRedClayCookingPot.ID);
                    return true;
                }
            }
        }
        return false;
    }

    private void setOutputItem(short itemID){
        this.outputItemID = itemID;
    }

    private void setOutputBlockID(short blockID){
        this.outputBlockID = blockID;
    }

    private boolean doesInputMatchBrickTemplate(){
        short countCorrect = 0;
        boolean squareActive;
        for(int i = 0; i < ItemCraftingTemplates.brick.indices.length; i++) {
            countCorrect = 0;
            for (int j = 0; j < this.materials.length; j++) {
                squareActive = this.materials[j].active;
                if(squareActive){
                    if(this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.brick.indices)){
                        countCorrect++;
                    }
                } else {
                    if(!this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.brick.indices)){
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

    private boolean doesInputMatchCookingPotTemplate(){
        short countCorrect = 0;
        boolean squareActive;
        for(int i = 0; i < ItemCraftingTemplates.cookingPot.indices.length; i++) {
            countCorrect = 0;
            for (int j = 0; j < this.materials.length; j++) {
                squareActive = this.materials[j].active;
                if(squareActive){
                    if(this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.cookingPot.indices)){
                        countCorrect++;
                    }
                } else {
                    if(!this.isIndexSupposedToBeFilled(j, ItemCraftingTemplates.cookingPot.indices)){
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
