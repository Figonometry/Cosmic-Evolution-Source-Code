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
import spacegame.render.*;
import spacegame.world.Chunk;
import spacegame.item.crafting.InWorld3DCraftingItem;

public final class GuiCraftingReeds extends GuiCrafting {
    public RecipeSelector[] selectableRecipes;
    public Button close;
    public int inventoryUI;
    public int transparentBackground;
    public int fillableColor;
    public int outline;
    public int x;
    public int y;
    public int z;

    public GuiCraftingReeds(CosmicEvolution cosmicEvolution, int x, int y, int z) {
        super(cosmicEvolution);
        this.close = new Button(EnumButtonEffects.CLOSE.name(), 50, 50, 349, 190, this, this.ce);
        this.selectableRecipes = new RecipeSelector[4];
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
                case 0 -> this.selectableRecipes[i] = new RecipeSelector(Item.reedBasket.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.reedBasket.getDisplayName(Item.NULL_ITEM_REFERENCE), new short[]{Item.reeds.ID}, new int[]{8}, new short[]{Item.NULL_ITEM_METADATA});
                case 1 -> this.selectableRecipes[i] = new RecipeSelector(Item.block.ID, Block.reedChest.ID,selectableX, selectableY, selectableWidth, selectableHeight, Item.block.getDisplayName(Block.reedChest.ID), new short[]{Item.reeds.ID}, new int[]{8}, new short[]{Item.NULL_ITEM_METADATA});
                case 2 -> this.selectableRecipes[i] = new RecipeSelector(Item.reedTwine.ID, selectableX, selectableY,selectableWidth, selectableHeight, Item.reedTwine.getDisplayName(Item.NULL_ITEM_REFERENCE), null, null, new short[]{Item.NULL_ITEM_METADATA});
                case 3 -> this.selectableRecipes[i] = new RecipeSelector(Item.reedCraftingGridTop.ID, selectableX, selectableY, selectableWidth, selectableHeight, Item.reedCraftingGridTop.getDisplayName(Item.NULL_ITEM_REFERENCE), null, null, new short[]{Item.NULL_ITEM_METADATA});
            }
            selectableX += 64;
        }
    }

    @Override
    public void loadTextures() {
        this.inventoryUI = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiTechTree/clayTexture.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.transparentBackground = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.fillableColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.outline = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/outline.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.inventoryUI);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.transparentBackground);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.fillableColor);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.outline);
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
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 0);
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
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 3);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 1);
        tessellator.addVertex2DTexture(16777215, backgroundX - (float) backgroundWidth / 2, backgroundY + (float) backgroundHeight / 2, backgroundZ, 2);
        tessellator.addVertex2DTexture(16777215, backgroundX + (float) backgroundWidth / 2, backgroundY - (float) backgroundHeight / 2, backgroundZ, 0);
        tessellator.addElementsCW();
        tessellator.drawTexture2D(this.inventoryUI, Shader.screen2DTexture, CosmicEvolution.camera);


        float selectableZ = -900;
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        int transparentColor;
        for (int i = 0; i < this.selectableRecipes.length; i++) {
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
            model = model.getScaledModel(50f);
            model = model.rotateModel(45, 0, 1, 0);
            model = model.rotateModel(36, 1, 0, 0);
            Vector3f position = new Vector3f(this.selectableRecipes[i].x, this.selectableRecipes[i].y, -50);
            model = model.translateModel(position.x, position.y, position.z);
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
                    textureID = Block.list[this.selectableRecipes[i].blockID].getBlockTexture(this.selectableRecipes[i].blockID, face);

                    vertex1 = new Vector3f(faces[j].vertices[0].x, faces[j].vertices[0].y, faces[j].vertices[0].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                    vertex2 = new Vector3f(faces[j].vertices[1].x, faces[j].vertices[1].y, faces[j].vertices[1].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                    vertex3 = new Vector3f(faces[j].vertices[2].x, faces[j].vertices[2].y, faces[j].vertices[2].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                    vertex4 = new Vector3f(faces[j].vertices[3].x, faces[j].vertices[3].y, faces[j].vertices[3].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);


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

                    tessellator.addVertexTextureArrayWithUV(colorRGB, vertex1.x, vertex1.y, vertex1.z, textureID, faces[j].UVs[0][0], faces[j].UVs[0][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, vertex2.x, vertex2.y, vertex2.z, textureID, faces[j].UVs[1][0], faces[j].UVs[1][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, vertex3.x, vertex3.y, vertex3.z, textureID, faces[j].UVs[2][0], faces[j].UVs[2][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, vertex4.x, vertex4.y, vertex4.z, textureID, faces[j].UVs[3][0], faces[j].UVs[3][1]);
                    tessellator.addElementsCCW();
                }
            }
            tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
        }

        selectableZ = -870;
        for (int i = 0; i < this.selectableRecipes.length; i++) {
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 3);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 1);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x - (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y + (this.selectableRecipes[i].height / 2), selectableZ, 2);
            tessellator.addVertex2DTexture(16777215, this.selectableRecipes[i].x + (this.selectableRecipes[i].width / 2), this.selectableRecipes[i].y - (this.selectableRecipes[i].height / 2), selectableZ, 0);
            tessellator.addElementsCW();
        }


        tessellator.drawTexture2D(this.outline, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        tessellator.toggleOrtho();


        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString("Select Recipe", 0, 128, -15, 16777215, 50, 255);

        RecipeSelector hoveredRecipe = this.getSelectedRecipeSelector();
        if(hoveredRecipe != null) {
            if (hoveredRecipe.requiredItems != null && hoveredRecipe.requiredItemCount != null) {
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
                    if(hoveredRecipe.requiredItems[i] == Item.block.ID)continue;
                    fontRenderer.drawString(hoveredRecipe.requiredItemCount[i] + "x: ", x, y, -9, 16777215, 50, 255);
                    x += 64 + (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);
                    y -= 8;
                    ModelLoader model = Item.list[hoveredRecipe.requiredItems[i]].itemModel.copyModel();
                    model = model.getScaledModel(76f);
                    model = model.rotateModel(45, 0, 1, 0);
                    model = model.rotateModel(36, 1, 0, 0);
                    Vector3f position = new Vector3f(x + 32, y + 32, -200);
                    model = model.translateModel(position.x, position.y, position.z);
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
                    tessellator.toggleOrtho();
                    tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                    tessellator.toggleOrtho();
                    y += 8;
                    x -= 64 - (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);;
                    y -= 64;
                }

                for (int i = 0; i < hoveredRecipe.requiredItems.length; i++) {
                    if (hoveredRecipe.requiredItems[i] != Item.block.ID) continue;
                    fontRenderer.drawString(hoveredRecipe.requiredItemCount[i] + "x: ", x, y, -9, 16777215, 50, 255);
                    x += 64 + (hoveredRecipe.requiredItemCount[i] >= 100 ? 2 * 17 : hoveredRecipe.requiredItemCount[i] >= 10 ? 17 : 0);
                    ModelLoader model = Block.list[hoveredRecipe.requiredItemMetadata[i]].blockModel.copyModel().translateModel(-0.5f, 0, -0.5f);
                    model = model.getScaledModel(76f);
                    model = model.rotateModel(45, 0, 1, 0);
                    model = model.rotateModel(36, 1, 0, 0);
                    model = model.translateModel(0.5f, 0, 0.5f);
                    model = model.translateModel(x + 64, y + 16, -200);
                    ModelFace modelFace;
                    float textureID;
                    int colorRGB = 255;
                    int colorTop = 16777215;

                    int colorVal = colorRGB;

                    int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                    int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                    int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                    int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                    int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;

                    for (int face = 0; face < model.modelFaces.length; face++) {
                        modelFace = model.modelFaces[face];
                        if (modelFace == null) continue;
                        textureID = model.usesMultipleTextures ? modelFace.texture : Block.list[hoveredRecipe.requiredItemMetadata[i]].getBlockTexture(hoveredRecipe.requiredItemMetadata[i], modelFace.faceType);

                        switch (modelFace.faceType){
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

                        tessellator.addVertexTextureArrayWithUV(colorRGB, modelFace.vertices[0].x, modelFace.vertices[0].y, modelFace.vertices[0].z, textureID, modelFace.UVs[0][0], modelFace.UVs[0][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, modelFace.vertices[1].x, modelFace.vertices[1].y, modelFace.vertices[1].z, textureID, modelFace.UVs[1][0], modelFace.UVs[1][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, modelFace.vertices[2].x, modelFace.vertices[2].y, modelFace.vertices[2].z, textureID, modelFace.UVs[2][0], modelFace.UVs[2][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, modelFace.vertices[3].x, modelFace.vertices[3].y, modelFace.vertices[3].z, textureID, modelFace.UVs[3][0], modelFace.UVs[3][1]);
                        tessellator.addElementsCCW();
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
    }


    @Override
    public Button getActiveButton() {
        if(this.close.active && this.close.isMouseHoveredOver()){
            return this.close;
        }
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

    @Override
    public void handleLeftClick(){
        RecipeSelector recipeSelector = this.getSelectedRecipeSelector();

        if(recipeSelector != null) {
            if (recipeSelector.meetsCriteriaToMakeRecipe(CosmicEvolution.instance.save.thePlayer)) {
                CosmicEvolution.instance.save.activeWorld.setBlockWithNotify(this.x, this.y, this.z, Block.crafting3DItem.ID, true);
                InWorld3DCraftingItem craftingBlock = new InWorld3DCraftingItem(Chunk.getBlockIndexFromCoordinates(x, y, z), Block.reedChest.ID, this.getInWorldCraftingRecipeName(recipeSelector.itemID), CosmicEvolution.instance.save.activeWorld.findChunkFromChunkCoordinates(this.x >> 5, this.y >> 5, this.z >> 5));
                if(recipeSelector.itemID != Item.reedTwine.ID && recipeSelector.itemID != Item.reedCraftingGridTop.ID) {
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
                    case 91 -> {
                        return InWorldCraftingRecipe.reedChest;
                    }
                }
            }

            case 13 -> {
                return InWorldCraftingRecipe.reedBasket;
            }
            case 23 -> {
                return InWorldCraftingRecipe.reedTwine;
            }
            case 24 -> {
                return InWorldCraftingRecipe.reedCraftingGridTop;
            }
        }
        throw new RuntimeException(itemID + " does not have a valid 3D crafting recipe template, was an invalid item added to the list?");
    }
}
