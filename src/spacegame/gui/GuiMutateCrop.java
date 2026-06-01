package spacegame.gui;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.item.ItemSeed;
import spacegame.item.itemstate.SeedState;
import spacegame.render.Assets;
import spacegame.render.RenderBlocks;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.render.model.ModelFace;
import spacegame.render.model.ModelLoader;
import spacegame.util.MathUtil;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.Crop;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.InWorld3DCraftingItem;
import spacegame.world.blockstate.TilledSoilState;

import java.util.ArrayList;

public class GuiMutateCrop extends GuiCrafting {
    public int x;
    public int y;
    public int z;
    public World world;
    public EntityPlayer player;
    public int backgroundTexture;
    public int transparentBackground;
    public int fillableColor;
    public int outline;
    public int recipeOverlay;
    public int selectedItemID = -1;
    public RecipeSelector[] selectableRecipes;
    public Crop crop;
    public boolean close;
    public GuiMutateCrop(CosmicEvolution cosmicEvolution, int x, int y, int z, World world, EntityPlayer player, Crop crop) {
        super(cosmicEvolution);
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.player = player;
        this.crop = crop;
        this.selectableRecipes = this.determineSeedRecipes();


        float selectableWidth = 64;
        float selectableHeight = 64;
        float selectableX = -320;
        float selectableY = 64;
        for(int i = 0; i < this.selectableRecipes.length; i++){
            this.selectableRecipes[i].x = selectableX;
            this.selectableRecipes[i].y = selectableY;
            this.selectableRecipes[i].width = selectableWidth;
            this.selectableRecipes[i].height = selectableHeight;
            selectableX += 64;
        }

        if(this.selectableRecipes.length == 0) {
            String cropName = ((ItemSeed)Item.list[player.getHeldItem()]).getCropName();
            world.addCropState(new CropState(Chunk.getBlockIndexFromCoordinates(x,y,z), cropName, false, "no target", 0, 0),x,y,z);
            world.notifyChunk(x,y,z);
            this.close = true;
        }
    }


    private RecipeSelector[] determineSeedRecipes() {
        ArrayList<Short> checkedIDs = new ArrayList<>();
        ArrayList<RecipeSelector> recipeSelectors = new ArrayList<>();
        this.crop.addAllDomesticatableCropsWithOnlyThisAsInput(recipeSelectors);

        for (int i = 0; i < this.player.inventory.itemStacks.length; i++) {
            if (this.player.inventory.itemStacks[i].item instanceof ItemSeed) {
                if (!this.hasItemAlreadyBeenUsed(this.player.inventory.itemStacks[i].item.ID, checkedIDs)) {

                    Crop crop = Crop.getCropFromName(((ItemSeed) this.player.inventory.itemStacks[i].item).getCropName());

                    this.crop.addAllDomesticatableCropsWithTwoInputs(recipeSelectors, crop);

                    checkedIDs.add(this.player.inventory.itemStacks[i].item.ID);
                }
            }
        }


        RecipeSelector[] selectableRecipes = new RecipeSelector[recipeSelectors.size()];

        for (int i = 0; i < selectableRecipes.length; i++) {
            selectableRecipes[i] = recipeSelectors.get(i);
        }

        return selectableRecipes;
    }


    private boolean hasItemAlreadyBeenUsed(short ID, ArrayList<Short> checkedIDs){
        for(int i = 0; i < checkedIDs.size(); i++){
            if(checkedIDs.get(i) == ID){
                return false;
            }
        }
        return true;
    }

    @Override
    public void loadTextures() {
        this.backgroundTexture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiCrafting/seedDomesticationBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
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
            tessellator.addElementsCW();
            tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);

            backgroundZ += 10;
            tessellator.addVertex2DTexture(16777215, outputSlotX - (float) outputWidth / 2, outputSlotY - (float) outputHeight / 2, backgroundZ, 3);
            tessellator.addVertex2DTexture(16777215, outputSlotX + (float) outputWidth / 2, outputSlotY + (float) outputHeight / 2, backgroundZ, 1);
            tessellator.addVertex2DTexture(16777215, outputSlotX - (float) outputWidth / 2, outputSlotY + (float) outputHeight / 2, backgroundZ, 2);
            tessellator.addVertex2DTexture(16777215, outputSlotX + (float) outputWidth / 2, outputSlotY - (float) outputHeight / 2, backgroundZ, 0);
            tessellator.addElementsCW();

            GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.drawTexture2D(this.recipeOverlay, Shader.screen2DTexture, CosmicEvolution.camera);

            GL46.glDisable(GL46.GL_BLEND);
        }


        if(this.selectedItemID == -1) {
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
                model.scaleModel(38f);
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

            for (int i = 0; i < this.selectableRecipes.length; i++) {
                if (!this.selectableRecipes[i].isBlock) continue;
                ModelLoader model = Block.list[this.selectableRecipes[i].blockID].blockModel.copyModel();
                model.translateModel(-0.5f, 0, -0.5f);
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

                for (int face = 0; face < 6; face++) {
                    faces = model.getModelFaceOfType(face);
                    for (int j = 0; j < faces.length; j++) {
                        if (faces[j] == null) continue;
                        textureID = Block.list[this.selectableRecipes[i].blockID].getBlockTexture(this.selectableRecipes[i].blockID, face);

                        vertex1 = new Vector3f(faces[j].vertices[0].x, faces[j].vertices[0].y, faces[j].vertices[0].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                        vertex2 = new Vector3f(faces[j].vertices[1].x, faces[j].vertices[1].y, faces[j].vertices[1].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                        vertex3 = new Vector3f(faces[j].vertices[2].x, faces[j].vertices[2].y, faces[j].vertices[2].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);
                        vertex4 = new Vector3f(faces[j].vertices[3].x, faces[j].vertices[3].y, faces[j].vertices[3].z).mul(38).rotateY((float) (0.25 * Math.PI)).rotateX((float) (0.20 * Math.PI)).add(position);

                        tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), vertex1.x, vertex1.y, vertex1.z, textureID, faces[j].UVs[0][0], faces[j].UVs[0][1]);
                        tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), vertex2.x, vertex2.y, vertex2.z, textureID, faces[j].UVs[1][0], faces[j].UVs[1][1]);
                        tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), vertex3.x, vertex3.y, vertex3.z, textureID, faces[j].UVs[2][0], faces[j].UVs[2][1]);
                        tessellator.addVertexTextureArrayWithUV(((red << 16) | (green << 8) | blue), vertex4.x, vertex4.y, vertex4.z, textureID, faces[j].UVs[3][0], faces[j].UVs[3][1]);
                        tessellator.addElementsCCW();

                        red -= 10;
                        green -= 10;
                        blue -= 10;
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


            FontRenderer fontRenderer = FontRenderer.instance;
            fontRenderer.drawCenteredString("Select Recipe", 0, 128, -15, 16777215, 50, 255);
        }
        tessellator.toggleOrtho();

        RecipeSelector hoveredRecipe = this.getSelectedRecipeSelector();
        if(hoveredRecipe != null) {
            FontRenderer fontRenderer = FontRenderer.instance;
            tessellator.toggleOrtho();
            String displayedName = hoveredRecipe.displayName;
            float x = MathUtil.getOpenGLMouseX();
            float y = MathUtil.getOpenGLMouseY();
            int font = 50;
            float height = font;
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
        }

        FontRenderer fontRenderer = FontRenderer.instance;
        fontRenderer.drawCenteredString("Select which crop to domesticate into", 0, 175, -400, 16777215, 60, 255);
    }

    @Override
    public Button getActiveButton() {
        return null;
    }

    public void handleLeftClick(){
        RecipeSelector recipeSelector = this.getSelectedRecipeSelector();
        if(recipeSelector != null) {
            TilledSoilState tilledSoilState = this.world.getTilledSoilState(this.x, this.y - 1, this.z);
            tilledSoilState.fertilizerID = TilledSoilState.NO_FERTILIZER;

            CropState cropState = new CropState(Chunk.getBlockIndexFromCoordinates(this.x, this.y, this.z),
                   this.crop.name, true, ((ItemSeed)Item.list[recipeSelector.itemID]).getCropName(), 0, 0);
            this.world.addCropState(cropState, this.x, this.y, this.z);
            this.ce.setNewGui(new GuiInGame(this.ce));
            this.world.notifyChunk(this.x, this.y, this.z);
        }
    }


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
