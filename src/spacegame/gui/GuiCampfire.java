package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.CosmicEvolution;
import spacegame.item.Inventory;
import spacegame.item.ItemStack;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.HeatableBlockLocation;

public final class GuiCampfire extends GuiInventory {
    public Inventory playerInventory;
    public Inventory campfireInventory;
    public int playerInventoryUI;
    public int campfireInventoryUI;
    public int backgroundForProgressBars;
    public int arrowColor;
    public int fireColor;
    public static int fillableColorWithShadedBottom;
    public HeatableBlockLocation heatableBlockLocation;
    public int x;
    public int y;
    public int z;
    public GuiCampfire(CosmicEvolution cosmicEvolution, Inventory playerInventory, Inventory campfireInventory, HeatableBlockLocation heatableBlockLocation, int x, int y, int z) {
        super(cosmicEvolution);

        this.playerInventory = playerInventory;
        this.campfireInventory = campfireInventory;
        this.heatableBlockLocation = heatableBlockLocation;

        this.x = x;
        this.y = y;
        this.z = z;


        this.playerInventory.shiftAllItemStacks(-262, 0);
        this.setPositionsForCampfireInventory();
    }

    private void setPositionsForCampfireInventory(){
        this.campfireInventory.itemStacks[0].x = 512;
        this.campfireInventory.itemStacks[0].y = -65;

        this.campfireInventory.itemStacks[1].x = 512;
        this.campfireInventory.itemStacks[1].y = 65;

        this.campfireInventory.itemStacks[2].x = 512 + 256;
        this.campfireInventory.itemStacks[2].y = 0;
    }


    @Override
    public void loadTextures() {
        this.playerInventoryUI = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/playerInventory.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        transparentBackground = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        fillableColorWithShadedBottom = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColorWithShadedBottom.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.campfireInventoryUI = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/campFireInventory.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.backgroundForProgressBars = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/backgroundForProgressBars.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.arrowColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/arrowColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, false);
        this.fireColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/fireColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.loadTexture();
    }

    @Override
    public void deleteTextures() {
        this.playerInventory.resetAllItemStacks();
        CosmicEvolution.instance.renderEngine.deleteTexture(this.playerInventoryUI);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.campfireInventoryUI);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.backgroundForProgressBars);
        CosmicEvolution.instance.renderEngine.deleteTexture(transparentBackground);
        CosmicEvolution.instance.renderEngine.deleteTexture(fillableColorWithShadedBottom);
        this.unloadTexture();
    }

    @Override
    public void drawGui() {
        if(this.heatableBlockLocation == null){
            CosmicEvolution.instance.save.activeWorld.getHeatableBlock(this.x, this.y, this.z);
        }
        GuiInGame.renderGuiFromOtherGuis();
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        int backgroundWidth = 1920;
        int backgroundHeight = 1017;
        int backgroundX = 0;
        int backgroundY = 0;
        int backgroundZ = -100;
        tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 3);
        tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 1);
        tessellator.addVertex2DTexture(0, backgroundX - backgroundWidth/2, backgroundY + backgroundHeight/2, backgroundZ, 2);
        tessellator.addVertex2DTexture(0, backgroundX + backgroundWidth/2, backgroundY - backgroundHeight/2, backgroundZ, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int inventoryUIWidth = 1200;
        int inventoryUIHeight = 608;
        int inventoryUIX = -256;
        int inventoryUIY = 0;
        int inventoryUIZ = -90;
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.playerInventoryUI, Shader.screen2DTexture, CosmicEvolution.camera);

        inventoryUIWidth = 512;
        inventoryUIHeight = 256;
        inventoryUIX = 512 + 128;
        inventoryUIY = 0;
        inventoryUIZ = -90;
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.campfireInventoryUI, Shader.screen2DTexture, CosmicEvolution.camera);

        inventoryUIZ = -95;
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
        tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
        tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.backgroundForProgressBars, Shader.screen2DTexture, CosmicEvolution.camera);

        if(this.heatableBlockLocation != null) {
            long heatTimeDif = this.heatableBlockLocation.heatingFinishTime - this.heatableBlockLocation.heatStartTime;
            long fuelTimeDif = this.heatableBlockLocation.fuelBurnoutTime - this.heatableBlockLocation.fuelStartTime;
            float ratioForProgressArrow = (float)(CosmicEvolution.instance.save.time - this.heatableBlockLocation.heatStartTime) / heatTimeDif;
            float ratioForFuelIndicator =  1 - ((float)(CosmicEvolution.instance.save.time - this.heatableBlockLocation.fuelStartTime) / fuelTimeDif);

            inventoryUIWidth = 128 + 48;
            inventoryUIHeight = 65;
            inventoryUIX = 512 + 54;
            inventoryUIY = 0;
            inventoryUIZ = -93;

            tessellator.addVertex2DTexture(16777215, inventoryUIX, inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 3);
            tessellator.addVertex2DTexture(16777215, inventoryUIX + (inventoryUIWidth * ratioForProgressArrow), inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 1);
            tessellator.addVertex2DTexture(16777215, inventoryUIX , inventoryUIY + inventoryUIHeight/2, inventoryUIZ, 2);
            tessellator.addVertex2DTexture(16777215, inventoryUIX + (inventoryUIWidth * ratioForProgressArrow), inventoryUIY - inventoryUIHeight/2, inventoryUIZ, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(this.arrowColor, Shader.screen2DTexture, CosmicEvolution.camera);

            inventoryUIWidth = 48;
            inventoryUIHeight = 40;
            inventoryUIX = 512;
            inventoryUIY = -23;
            inventoryUIZ = -93;

            tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY, inventoryUIZ, 3);
            tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY + (inventoryUIHeight * ratioForFuelIndicator), inventoryUIZ, 1);
            tessellator.addVertex2DTexture(16777215, inventoryUIX - inventoryUIWidth/2, inventoryUIY + (inventoryUIHeight * ratioForFuelIndicator), inventoryUIZ, 2);
            tessellator.addVertex2DTexture(16777215, inventoryUIX + inventoryUIWidth/2, inventoryUIY, inventoryUIZ, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(this.fireColor, Shader.screen2DTexture, CosmicEvolution.camera);


        }

        tessellator.toggleOrtho();

        this.playerInventory.renderInventory();
        this.campfireInventory.renderInventory();
        this.renderHoveredItemStackName(this.getHoveredItemStack());
    }

    @Override
    public Button getActiveButton() {
        return null;
    }

    @Override
    public ItemStack getHoveredItemStack() {
        ItemStack stack;
        for(int i = 0; i < this.playerInventory.itemStacks.length; i++){
            stack = this.playerInventory.itemStacks[i];
            if(stack.isMouseHoveredOver()){
                return stack;
            }
        }
        for(int i = 0; i < this.campfireInventory.itemStacks.length; i++){
            stack = this.campfireInventory.itemStacks[i];
            if(stack.isMouseHoveredOver()){
                return stack;
            }
        }
        return null;
    }
}
