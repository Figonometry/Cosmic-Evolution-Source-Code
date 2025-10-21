package spacegame.gui;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.MathUtil;
import spacegame.core.SpaceGame;
import spacegame.item.Item;
import spacegame.render.*;

public final class GuiCraftingStrawStorage extends GuiCrafting {
    public RecipeSelector[] selectableRecipes;
    public Button close;
    public int inventoryUI;
    public int transparentBackground;
    public int fillableColor;
    public int outline;
    public int x;
    public int y;
    public int z;

    public GuiCraftingStrawStorage(SpaceGame spaceGame, int x, int y, int z) {
        super(spaceGame);
        this.close = new Button(EnumButtonEffects.CLOSE.name(), 50, 50, 349, 190, this, this.sg);
        this.selectableRecipes = new RecipeSelector[2];
        this.setSelectableItemIDs();
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private void setSelectableItemIDs(){
        float selectableWidth = 64;
        float selectableHeight = 64;
        float selectableX = -320;
        float selectableY = 64;

        for(int i = 0; i < this.selectableRecipes.length; i++){
            switch (i) {
                case 0 -> this.selectableRecipes[i] = new RecipeSelector(Item.strawBasket.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.strawBasket.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.straw.ID}, new int[]{8});
                case 1 -> this.selectableRecipes[i] = new RecipeSelector(Item.block.ID, Block.strawChest.ID,selectableX, selectableY, selectableWidth, selectableHeight, Item.block.getDisplayName(Block.strawChest.ID), new short[]{Item.straw.ID}, new int[]{8});
            }
            selectableX += 64;
        }
    }

    @Override
    public void loadTextures() {
        this.inventoryUI = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/clayTexture.png", RenderEngine.TEXTURE_TYPE_2D, 0);
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
    }

    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

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
        tessellator.drawTexture2D(this.inventoryUI, Shader.screen2DTexture, SpaceGame.camera);


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
            if(this.selectableRecipes[i].isBlock)continue;
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

        tessellator.toggleOrtho();


        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString("Select Recipe", 0, 128, -15, 16777215, 50);

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


            fontRenderer.drawString(displayedName, x, y, -9, 16777215, font);
        }
    }

    public void setRecipeSelected(RecipeSelector selectedRecipe){
        if(selectedRecipe.isBlock){
            this.sg.save.activeWorld.setBlockWithNotify(x,y,z, Block.strawChestTier0.ID);
        } else {
            this.sg.save.activeWorld.setBlockWithNotify(x,y,z, Block.strawBasketTier0.ID);
        }
        for(int i = 0; i < selectedRecipe.requiredItemCount.length; i++){
            for(int j = 0; j < selectedRecipe.requiredItemCount[i]; j++){
                this.sg.save.thePlayer.removeSpecificItemFromInventory(selectedRecipe.requiredItems[i]);
            }
        }
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        this.sg.setNewGui(new GuiInGame(this.sg));
    }

    @Override
    public Button getActiveButton() {
        if(this.close.active && this.close.isMouseHoveredOver()){
            return this.close;
        }
        return null;
    }

    @Override
    public CraftingMaterial getHoveredCraftingMaterial() {
        return null;
    }

    @Override
    public RecipeSelector getSelectedRecipeSelector() {
        for(int i = 0; i < this.selectableRecipes.length; i++){
            if(this.selectableRecipes[i].isMouseHoveredOver()){
                return this.selectableRecipes[i];
            }
        }
        return null;
    }
}
