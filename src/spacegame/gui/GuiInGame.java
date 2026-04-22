package spacegame.gui;


import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.block.BlockTorch;
import spacegame.block.ITimeUpdate;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.entity.Entity;
import spacegame.item.crafting.InWorldCraftingItem;
import spacegame.render.model.ModelFace;
import spacegame.render.model.ModelLoader;
import spacegame.render.model.ModelPlayer;
import spacegame.render.model.ModelSegment;
import spacegame.util.GeneralUtil;
import spacegame.util.MathUtil;
import spacegame.core.Timer;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.*;
import spacegame.world.AxisAlignedBB;
import spacegame.world.Chunk;
import spacegame.item.crafting.InWorld3DCraftingItem;
import spacegame.world.TimeUpdateEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.Random;

public final class GuiInGame extends Gui {
    public static int vignette;
    public static int water;
    public static int hotbar;
    public static int outline;
    public static int subVoxelOutline;
    public static int blockBreaking;
    public static TextureAtlas blockBreakingAtlas;
    public static int transparentBackground;
    public static int fillableColorWithShadedBottom;
    public static int fillableColor;
    public static String messageText = "dummy";
    public static int messageTextAlpha;
    public static int messageTextColor;
    public static long timeMessageStarted;
    public static float red;
    public static float green;
    public static float blue;
    public static float skyLightValue;

    public GuiInGame(CosmicEvolution ce) {
        super(ce);
        this.ce = ce;
    }

    @Override
    public void loadTextures() {
        if (vignette == 0) {
            vignette = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/vignette.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            water = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/waterOverlay.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            outline = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/outline.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            subVoxelOutline = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/outline.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            blockBreaking = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/blockBreaking.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            blockBreakingAtlas = CosmicEvolution.instance.renderEngine.createTextureAtlas(96, 96, 32, 32, 9, 0);
            hotbar = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/hotbarSlot.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            transparentBackground = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            fillableColorWithShadedBottom = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColorWithShadedBottom.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            fillableColor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        }
    }

    @Override
    public void deleteTextures() {

    }

    public static void renderText(){
        int leftSide = -970;
        FontRenderer fontRenderer = FontRenderer.instance;
        if(GameSettings.showFPS) {
            fontRenderer.drawString(CosmicEvolution.instance.title + " (" + CosmicEvolution.instance.fps * -1 + " FPS)", leftSide, 460,-15, 16777215, 50, 255);
        } else {
            fontRenderer.drawString(CosmicEvolution.instance.title, leftSide, 460,-15, 16777215, 50, 255);
        }
        int playerX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x);
        int playerY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y);
        int playerZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z);

        if (CosmicEvolution.instance.save.saveSettings.testingMode) {
            fontRenderer.drawString("X: " + CosmicEvolution.instance.save.thePlayer.x, leftSide, 430,-15, 16777215, 50, 255);
            fontRenderer.drawString("Y: " + (CosmicEvolution.instance.save.thePlayer.y), leftSide, 400,-15, 16777215, 50, 255);
            fontRenderer.drawString("Z: " + CosmicEvolution.instance.save.thePlayer.z, leftSide, 370,-15, 16777215, 50, 255);
            fontRenderer.drawString("Pitch: " + CosmicEvolution.instance.save.thePlayer.pitch, leftSide, 340,-15, 16777215, 50, 255);
            fontRenderer.drawString("Yaw: " + CosmicEvolution.instance.save.thePlayer.yaw, leftSide, 310,-15, 16777215, 50, 255);
            fontRenderer.drawString("Chunks Loaded " + CosmicEvolution.instance.save.activeWorld.chunkController.numberOfLoadedChunks(), leftSide, 280,-15, 16777215, 50, 255);
            fontRenderer.drawString("Block Light Level: " + CosmicEvolution.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ), leftSide, 250,-15, 16777215, 50, 255);
            fontRenderer.drawString("Regions Loaded: " + CosmicEvolution.instance.save.activeWorld.chunkController.numberOfLoadedRegions(), leftSide, 220,-15, 16777215, 50, 255);
            fontRenderer.drawString("Draw Calls: " +  CosmicEvolution.instance.save.activeWorld.chunkController.drawCalls, leftSide, 190,-15, 16777215, 50, 255);
            fontRenderer.drawString("Thread Count: " + Thread.activeCount(), leftSide, 160,-15, 16777215, 50, 255);
            fontRenderer.drawString("Thread Queue Size: " + CosmicEvolution.threadJobs.get(), leftSide, 130,-15, 16777215, 50, 255);
            fontRenderer.drawString("Sky Light Level: " + CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(playerX, playerY, playerZ), leftSide, 100,-15, 16777215, 50, 255);
            fontRenderer.drawString("Temperature: " + CosmicEvolution.instance.save.activeWorld.getDisplayTemperature(playerX, playerY, playerZ) + "F", leftSide, 70,-15, 16777215, 50, 255);
            fontRenderer.drawString("Rainfall: " + CosmicEvolution.instance.save.activeWorld.getRainfall(playerX, playerZ), leftSide, 40,-15, 16777215, 50, 255);
            fontRenderer.drawString("Time: " + CosmicEvolution.instance.save.time, leftSide, 10, -15, 16777215, 50, 255);
            fontRenderer.drawString("Entities: " + CosmicEvolution.instance.save.activeWorld.chunkController.numLoadedEntities + " / " + CosmicEvolution.instance.save.activeWorld.chunkController.entityCap, leftSide,-20, -15, 16777215, 50, 255);
        } else {
            fontRenderer.drawString("Temperature: " + CosmicEvolution.instance.save.activeWorld.getDisplayTemperature(playerX, playerY, playerZ) + "F", leftSide, 400,-15, 16777215, 50, 255);
            fontRenderer.drawString("Rainfall: " + CosmicEvolution.instance.save.activeWorld.getRainfall(playerX, playerZ), leftSide, 370,-15, 16777215, 50, 255);
        }
    }


    @Override
    public void drawGui() {
        GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
        renderText();
        this.renderCursor();
        renderVignette();
        renderBlockLookingAtName();
        renderMessageText();
        renderHeldItem();
        renderHotbar();
        renderHealthAndHungerBar();
    }

    public static void renderGuiFromOtherGuis(){
        renderText();
        renderVignette();
        renderBlockLookingAtName();
        renderMessageText();
        renderHeldItem();
        renderHotbar();
        renderHealthAndHungerBar();
    }

    public static void renderVignette(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;

        if(!CosmicEvolution.instance.save.thePlayer.freeMove) {
            short blockPlayerHeadIsIn = CosmicEvolution.instance.save.activeWorld.getBlockID(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x), MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height / 2), MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z));
            if (blockPlayerHeadIsIn == Block.water.ID) {
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                tessellator.toggleOrtho();
                tessellator.addVertex2DTexture(8355711, (float) -CosmicEvolution.width / 2, (float) -CosmicEvolution.height / 2, -900, 3);
                tessellator.addVertex2DTexture(8355711, (float) CosmicEvolution.width / 2, (float) CosmicEvolution.height / 2, -900, 1);
                tessellator.addVertex2DTexture(8355711, (float) -CosmicEvolution.width / 2, (float) CosmicEvolution.height / 2, -900, 2);
                tessellator.addVertex2DTexture(8355711, (float) CosmicEvolution.width / 2, (float) -CosmicEvolution.height / 2, -900, 0);
                tessellator.addElements();
                tessellator.drawTexture2D(water, Shader.screen2DTexture, CosmicEvolution.camera);
                tessellator.toggleOrtho();
                GL46.glDisable(GL46.GL_BLEND);
                renderAirBar();
            } else if (blockPlayerHeadIsIn != Block.air.ID && Block.list[blockPlayerHeadIsIn].isSolid && blockPlayerHeadIsIn != Block.leaf.ID) {
                int textureID = Block.list[blockPlayerHeadIsIn].textureID;
                tessellator.toggleOrtho();
                tessellator.addVertexTextureArray(4144959, (float) -CosmicEvolution.width / 2, (float) -CosmicEvolution.height / 2, -900, 3, textureID, 2);
                tessellator.addVertexTextureArray(4144959, (float) CosmicEvolution.width / 2, (float) CosmicEvolution.height / 2, -900, 1, textureID, 2);
                tessellator.addVertexTextureArray(4144959, (float) -CosmicEvolution.width / 2, (float) CosmicEvolution.height / 2, -900, 2, textureID, 2);
                tessellator.addVertexTextureArray(4144959, (float) CosmicEvolution.width / 2, (float) -CosmicEvolution.height / 2, -900, 0, textureID, 2);
                tessellator.addElements();
                tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                tessellator.toggleOrtho();
            }
        }

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ZERO, GL46.GL_ONE_MINUS_SRC_COLOR);
        tessellator.toggleOrtho();
        tessellator.addVertex2DTexture(16777215, (float) -970, (float) -970, -100, 3);
        tessellator.addVertex2DTexture(16777215, (float) 970, (float) 970, -100, 1);
        tessellator.addVertex2DTexture(16777215, (float) -970, (float) 970, -100, 2);
        tessellator.addVertex2DTexture(16777215, (float) 970, (float) -970, -100, 0);
        tessellator.addElements();
        GL46.glDepthMask(false);
        tessellator.drawTexture2D(vignette, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDepthMask(true);
        tessellator.toggleOrtho();
        GL46.glDisable(GL46.GL_BLEND);
    }

    public static void renderBlockLookingAtName(){
        short blockID = CosmicEvolution.instance.save.thePlayer.getPlayerLookingAtBlockID();
        if(blockID == Block.air.ID || blockID == Block.water.ID)return;
        if(blockID == Block.craftingItem.ID) {
            int[] coords = CosmicEvolution.instance.save.thePlayer.getPlayerLookingAtBlockCoords();
            renderCraftingItemInfoOverlay(coords[0], coords[1], coords[2]);
        }
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        FontRenderer fontRenderer = FontRenderer.instance;

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        int[] blockCoordinates = CosmicEvolution.instance.save.thePlayer.getPlayerLookingAtBlockCoords();
        if(Block.list[blockID] instanceof ITimeUpdate && !(Block.list[blockID] instanceof BlockTorch)) {
            StringBuilder stringBuilder = new StringBuilder();
            TimeUpdateEvent updateEvent = CosmicEvolution.instance.save.activeWorld.getTimeEvent(blockCoordinates[0], blockCoordinates[1], blockCoordinates[2]);
            if(updateEvent != null) {
                long timeUntil = updateEvent.updateTime - CosmicEvolution.instance.save.time;
                long daysUntil = timeUntil / Timer.GAME_DAY;
                long hoursUntil = (timeUntil % Timer.GAME_DAY) / Timer.GAME_HOUR;
                long minutesUntil = (timeUntil % Timer.GAME_HOUR) / Timer.GAME_MINUTE;

                stringBuilder.append(((ITimeUpdate) Block.list[blockID]).getDisplayStringText());
                if (daysUntil != 0) {
                    stringBuilder.append(daysUntil).append(daysUntil != 1 ? " Days " : " Day ");
                }
                if (hoursUntil != 0 || (minutesUntil != 0 && daysUntil != 0)) {
                    stringBuilder.append(hoursUntil).append(hoursUntil != 1 ? " Hours " : " Hour ");
                }
                if (minutesUntil != 0) {
                    stringBuilder.append(minutesUntil).append(minutesUntil != 1 ? " Minutes" : " Minute");
                }
            }

            tessellator.toggleOrtho();
            int x = 0;
            int y = 450;
            float width = Math.max(Block.list[blockID].getDisplayName(blockCoordinates[0], blockCoordinates[1], blockCoordinates[2]).length() * 25, stringBuilder.toString().length() * 25);
            float height = 100;

            tessellator.addVertex2DTexture(0, x - width / 2f, y - height / 2f, -90, 3);
            tessellator.addVertex2DTexture(0, x + width / 2f, y + height / 2f, -90, 1);
            tessellator.addVertex2DTexture(0, x - width / 2f, y + height / 2f, -90, 2);
            tessellator.addVertex2DTexture(0, x + width / 2f, y - height / 2f, -90, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();
            fontRenderer.drawCenteredString(Block.list[blockID].getDisplayName(blockCoordinates[0], blockCoordinates[1], blockCoordinates[2]), 0, 450, -14, 16777215, 50, 255);

            if(updateEvent != null) {
                fontRenderer.drawCenteredString(stringBuilder.toString(), 0, 400, -14, 16777215, 50, 255);
            }
        } else {
            tessellator.toggleOrtho();
            int x = 0;
            int y = 450;
            float width = Block.list[blockID].getDisplayName(blockCoordinates[0], blockCoordinates[1], blockCoordinates[2]).length() * 25;
            float height = 50;
            tessellator.addVertex2DTexture(0, x - width / 2f, y - height / 2f, -90, 3);
            tessellator.addVertex2DTexture(0, x + width / 2f, y + height / 2f, -90, 1);
            tessellator.addVertex2DTexture(0, x - width / 2f, y + height / 2f, -90, 2);
            tessellator.addVertex2DTexture(0, x + width / 2f, y - height / 2f, -90, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();
            fontRenderer.drawCenteredString(Block.list[blockID].getDisplayName(blockCoordinates[0], blockCoordinates[1], blockCoordinates[2]), 0, 425, -14, 16777215, 50, 255);
        }
        GL46.glDisable(GL46.GL_BLEND);
    }

    private static void renderCraftingItemInfoOverlay(int bx, int by, int bz){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        FontRenderer fontRenderer = FontRenderer.instance;

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);

        InWorldCraftingItem craftingItem = CosmicEvolution.instance.save.activeWorld.getInWorldCraftingItem(bx, by, bz);
        if(craftingItem == null)return;

        tessellator.toggleOrtho();
        int x = 640;
        int y = 0;
        float width = 512;
        float height = 512;

        tessellator.addVertex2DTexture(0, x - width / 2f, y - height / 2f, -90, 3);
        tessellator.addVertex2DTexture(0, x + width / 2f, y + height / 2f, -90, 1);
        tessellator.addVertex2DTexture(0, x - width / 2f, y + height / 2f, -90, 2);
        tessellator.addVertex2DTexture(0, x + width / 2f, y - height / 2f, -90, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();
        y = 200;
        fontRenderer.drawCenteredString(Item.list[craftingItem.outputRecipe.itemID].getDisplayName(craftingItem.outputRecipe.blockID), x, y, -14, 16777215, 50, 255);
        y -= 120;
        for(int i = 0; i < craftingItem.itemsFilled.length; i++){
            fontRenderer.drawCenteredString((craftingItem.itemsFilled[i] ? "COMPLETED: " : "MISSING: ")  + Item.list[craftingItem.outputRecipe.requiredItems[i]].getDisplayName(craftingItem.outputRecipe.requiredItems[i]), x, y, -14, craftingItem.itemsFilled[i] ? 255 << 8 : 255 << 16, 50, 255);

            if(!craftingItem.itemsFilled[i]) {
                y -= 30;
                tessellator.toggleOrtho();
                tessellator.addVertexTextureArray(16777215, x - 30, y - 30, -85, 3, craftingItem.outputRecipe.requiredItems[i], RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x + 30, y + 30, -85, 1, craftingItem.outputRecipe.requiredItems[i], RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x - 30, y + 30, -85, 2, craftingItem.outputRecipe.requiredItems[i], RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(16777215, x + 30, y - 30, -85, 0, craftingItem.outputRecipe.requiredItems[i], RenderBlocks.WEST_FACE);
                tessellator.addElements();
                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                tessellator.toggleOrtho();
            }
            y -= !craftingItem.itemsFilled[i] ? 75 : 30;
        }

        if(craftingItem.outputRecipe.requiresBinding && !craftingItem.hasBeenBound && craftingItem.areAllItemsFilled()) {
            y -= 30;
            fontRenderer.drawCenteredString("MISSING: " + Item.reedTwine.getDisplayName(Item.NULL_ITEM_REFERENCE), x, y, -14, 255 << 16, 50, 255);
            tessellator.toggleOrtho();
            tessellator.addVertexTextureArray(16777215, x - 30, y - 30, -85, 3, Item.reedTwine.getTextureID(Item.reedTwine.ID, Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, x + 30, y + 30, -85, 1, Item.reedTwine.getTextureID(Item.reedTwine.ID, Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, x - 30, y + 30, -85, 2, Item.reedTwine.getTextureID(Item.reedTwine.ID, Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
            tessellator.addVertexTextureArray(16777215, x + 30, y - 30, -85, 0, Item.reedTwine.getTextureID(Item.reedTwine.ID, Item.NULL_ITEM_METADATA, RenderBlocks.WEST_FACE), RenderBlocks.WEST_FACE);
            tessellator.addElements();
            tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
            tessellator.toggleOrtho();
        }

        GL46.glDisable(GL46.GL_BLEND);
    }

    public static void renderMessageText() {
        if (messageText.equals("dummy")) return;

        FontRenderer fontRenderer = FontRenderer.instance;
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
        fontRenderer.drawCenteredString(messageText, 0, -250, -15, messageTextColor, 50, messageTextAlpha);
        GL46.glDisable(GL46.GL_BLEND);
    }

    public static void fadeMessageText(){
        if(messageText.equals("dummy"))return;

        if (CosmicEvolution.instance.save.time >= timeMessageStarted + 120 && ((CosmicEvolution.instance.save.time & 1) == 0)) { //after 2 seconds fade the string
            messageTextAlpha -= 2;
            if (messageTextAlpha <= 0) {
                messageText = "dummy";
            }
        }
    }

    public static void setMessageText(String messageText1, int color){
        messageText = messageText1;
        messageTextAlpha = 255;
        messageTextColor = color;
        timeMessageStarted = CosmicEvolution.instance.save.time;
    }

    public static void renderHotbar(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        int x = -428;
        int y = -500;
        tessellator.addVertex2DTexture(0, x, y, -90, 3);
        tessellator.addVertex2DTexture(0, x + 864, y + 96, -90, 1);
        tessellator.addVertex2DTexture(0, x, y + 96, -90, 2);
        tessellator.addVertex2DTexture(0, x + 864, y, -90, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int size = 96;
        if(EntityPlayer.selectedInventorySlot == 0){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 0 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 0 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 0 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 0 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 1){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 1 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 1 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 1 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 1 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 2){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 2 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 2 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 2 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 2 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 3){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 3 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 3 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 3 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 3 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 4){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 4 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 4 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 4 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 4 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 5){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 5 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 5 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 5 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 5 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 6){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 6 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 6 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 6 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 6 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 7){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 7 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 7 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 7 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 7 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 8){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 8 ? 0 : 16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 8 ? 0 : 16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 8 ? 0 : 16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 8 ? 0 : 16777215, x + size, y, -80, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(hotbar, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();

        x = -428;
        size = 96;
        for(int i = 0; i < 9; i++) {
            if (i == EntityPlayer.selectedInventorySlot) {
                CosmicEvolution.instance.save.thePlayer.inventory.itemStacks[i].adjustStackPosition(x + 46, y + 46);
                CosmicEvolution.instance.save.thePlayer.inventory.itemStacks[i].renderItemStackOnHotbar();
                CosmicEvolution.instance.save.thePlayer.inventory.itemStacks[i].resetStackPosition();
            } else {
                CosmicEvolution.instance.save.thePlayer.inventory.itemStacks[i].adjustStackPosition(x + 43, y + 43);
                CosmicEvolution.instance.save.thePlayer.inventory.itemStacks[i].renderItemStackOnHotbar();
                CosmicEvolution.instance.save.thePlayer.inventory.itemStacks[i].resetStackPosition();
            }

            x += size;
        }

        if(CosmicEvolution.instance.currentGui instanceof GuiInventoryStrawChest || CosmicEvolution.instance.currentGui instanceof GuiCampfire){
            CosmicEvolution.instance.save.thePlayer.inventory.shiftPlayerHotbar(-256, 0);
        }
    }

    private static void renderHealthAndHungerBar(){ //Full red 16711680, half red 8323072
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        int x = -428;
        int y = -395;

        tessellator.addVertex2DTexture(4128768, x, y, -90, 3);
        tessellator.addVertex2DTexture(4128768, x + 368, y + 16, -90, 1);
        tessellator.addVertex2DTexture(4128768, x, y + 16, -90, 2);
        tessellator.addVertex2DTexture(4128768, x + 368, y, -90, 0);
        tessellator.addElements();

        float progress = (int) (368 * (CosmicEvolution.instance.save.thePlayer.health / CosmicEvolution.instance.save.thePlayer.maxHealth));
        tessellator.addVertex2DTexture(11016473, x, y, -89, 3);
        tessellator.addVertex2DTexture(11016473, x + progress, y + 16, -89, 1);
        tessellator.addVertex2DTexture(11016473, x, y + 16, -89, 2);
        tessellator.addVertex2DTexture(11016473, x + progress, y, -89, 0);
        tessellator.addElements();

        x += 496;
        tessellator.addVertex2DTexture(5522201, x, y, -90, 3);
        tessellator.addVertex2DTexture(5522201, x + 368, y + 16, -90, 1);
        tessellator.addVertex2DTexture(5522201, x, y + 16, -90, 2);
        tessellator.addVertex2DTexture(5522201, x + 368, y, -90, 0);
        tessellator.addElements();

        progress = (int) (368 * (CosmicEvolution.instance.save.thePlayer.saturation / CosmicEvolution.instance.save.thePlayer.maxSaturation));
        tessellator.addVertex2DTexture(16764748, x, y, -89, 3);
        tessellator.addVertex2DTexture(16764748, x + progress, y + 16, -89, 1);
        tessellator.addVertex2DTexture(16764748, x, y + 16, -89, 2);
        tessellator.addVertex2DTexture(16764748, x + progress, y, -89, 0);
        tessellator.addElements();



        tessellator.drawTexture2D(fillableColorWithShadedBottom, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();
    }

    public static void renderAirBar(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        int x = -428;
        int y = -379;
        tessellator.addVertex2DTexture(9535, x, y, -90, 3);
        tessellator.addVertex2DTexture(9535, x + 864, y + 16, -90, 1);
        tessellator.addVertex2DTexture(9535, x, y + 16, -90, 2);
        tessellator.addVertex2DTexture(9535, x + 864, y, -90, 0);
        tessellator.addElements();

        float progress = (int) (864 * ((300 - CosmicEvolution.instance.save.thePlayer.drowningTimer) / 300f));
        tessellator.addVertex2DTexture(38143, x, y, -89, 3);
        tessellator.addVertex2DTexture(38143, x + progress, y + 16, -89, 1);
        tessellator.addVertex2DTexture(38143, x, y + 16, -89, 2);
        tessellator.addVertex2DTexture(38143, x + progress, y, -89, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(fillableColorWithShadedBottom, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();
    }


    public static void renderHeldItem() {
        final short heldBlock = CosmicEvolution.instance.save.thePlayer.getHeldBlock();
        if(CosmicEvolution.instance.save.thePlayer.isHoldingBlock()) {
            if (heldBlock != Block.air.ID) {
                float blockID;
                float x = 2f;
                float y = -2.5f;
                float z = -3f;
                if(GameSettings.viewBob) {
                    x -= 0.5f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.viewBobTimer / 60f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    y -= 0.25f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.viewBobTimer / 60f) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
                }
                z -= 1f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.swingTimer / CosmicEvolution.instance.save.thePlayer.maxSwingTimer) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                Vector3f position = new Vector3f(x,y,z);
                Matrix3f rotationMatrix = new Matrix3f();
                rotationMatrix.rotateY((float) (0.25 * Math.PI));
                double sine = (MathUtil.sin((float) ((((double) CosmicEvolution.instance.save.thePlayer.swingTimer / CosmicEvolution.instance.save.thePlayer.maxSwingTimer) * Math.PI * 2) - (0.5 * Math.PI))) * 0.5) + 0.5f;
                rotationMatrix.rotateLocalX((float) ((float) -(0.25 * Math.PI) * sine));
                Quaternionf rotation = rotationMatrix.getUnnormalizedRotation(new Quaternionf());
                int playerX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x);
                int playerY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y);
                int playerZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z);
                float blockLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ));
                float lightLevelFloat = CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight > blockLight ? CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight : blockLight;
                lightLevelFloat -=  0.1 * (MathUtil.sin(CosmicEvolution.instance.save.thePlayer.yaw / 45) + 1);
                lightLevelFloat -=  0.1 * (MathUtil.sin(CosmicEvolution.instance.save.thePlayer.pitch / 45) + 1);
                if(lightLevelFloat < 0.1){
                    lightLevelFloat = 0.1f;
                }
                int channelVal = MathUtil.floatToIntRGBA(lightLevelFloat);
                int colorRGB = channelVal << 16 | channelVal << 8 | channelVal;

                RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
                ModelLoader model = Block.list[heldBlock].blockModel.copyModel();
                model.translateModel(-0.5f, 0, -0.5f);
                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                float textureID;
                int colorVal;
                float[] UVSamples;
                for(int face = 0; face < 6; face++){
                    ModelFace[] faces = model.getModelFaceOfType(face);
                    for(int i = 0; i < faces.length; i++){
                        if(faces[i] == null)continue;
                        UVSamples = face == RenderBlocks.TOP_FACE || face == RenderBlocks.BOTTOM_FACE ? RenderBlocks.autoUVTopBottom(RenderBlocks.getFaceWidth(faces[i]), RenderBlocks.getFaceHeight(faces[i])) : RenderBlocks.autoUVNSEW(RenderBlocks.getFaceWidth(faces[i]), RenderBlocks.getFaceHeight(faces[i]));
                        textureID = RenderBlocks.getBlockTextureID(heldBlock, face);
                        vertex1 = new Vector3f(faces[i].vertices[0].x, faces[i].vertices[0].y, faces[i].vertices[0].z).rotate(rotation).add(position);
                        vertex2 = new Vector3f(faces[i].vertices[1].x, faces[i].vertices[1].y, faces[i].vertices[1].z).rotate(rotation).add(position);
                        vertex3 = new Vector3f(faces[i].vertices[2].x, faces[i].vertices[2].y, faces[i].vertices[2].z).rotate(rotation).add(position);
                        vertex4 = new Vector3f(faces[i].vertices[3].x, faces[i].vertices[3].y, faces[i].vertices[3].z).rotate(rotation).add(position);


                        tessellator.addVertexTextureArrayWithSampling(colorRGB, vertex1.x, vertex1.y, vertex1.z, 3, textureID, UVSamples[0], UVSamples[1]);
                        tessellator.addVertexTextureArrayWithSampling(colorRGB, vertex2.x, vertex2.y, vertex2.z, 1, textureID, UVSamples[2], UVSamples[3]);
                        tessellator.addVertexTextureArrayWithSampling(colorRGB, vertex3.x, vertex3.y, vertex3.z, 2, textureID, UVSamples[4], UVSamples[5]);
                        tessellator.addVertexTextureArrayWithSampling(colorRGB, vertex4.x, vertex4.y, vertex4.z, 0, textureID, UVSamples[6], UVSamples[7]);
                        tessellator.addElements();
                        colorVal = colorRGB & 255;
                        colorVal -= 10;
                        colorRGB = (colorVal << 16) | (colorVal << 8) | colorVal;
                    }
                }

                Matrix4d preservedViewMatrix = CosmicEvolution.camera.viewMatrix.get(new Matrix4d());
                CosmicEvolution.camera.viewMatrix = new Matrix4d();
                GL46.glEnable(GL46.GL_CULL_FACE);
                GL46.glCullFace(GL46.GL_FRONT);
                tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                GL46.glDisable(GL46.GL_CULL_FACE);
                CosmicEvolution.camera.viewMatrix = preservedViewMatrix;
            }
        } else {
            short itemID = CosmicEvolution.instance.save.thePlayer.getHeldItem();
            if(itemID == Item.NULL_ITEM_REFERENCE) {
                RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
                ModelSegment arm = ModelPlayer.getBaseModel().segments[ModelPlayer.LEFT_ARM];
                arm.scale(1.5f);
                float translateX = 1.5f;
                float translateY = -1.25f;
                float translateZ = -2f;
                if (GameSettings.viewBob) {
                    translateX -= 0.125f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.viewBobTimer / 60f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    translateY -= 0.0625f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.viewBobTimer / 60f) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
                }
                translateZ -= 0.5f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.swingTimer / 15f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);

                Vector3f translation = new Vector3f(translateX, translateY, translateZ);
                Matrix3f rotationMatrix = new Matrix3f();
                rotationMatrix.rotateY((float) -(0.35 * Math.PI));
                double sine = (MathUtil.sin((float) ((((double) CosmicEvolution.instance.save.thePlayer.swingTimer / 15f) * Math.PI * 2) - (0.5 * Math.PI))) * 0.5) + 0.5f;
                rotationMatrix.rotateLocalX((float) ((float) -(0.25 * Math.PI) * sine));
                int playerX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x);
                int playerY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y);
                int playerZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z);
                float blockLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ));
                float lightLevelFloat = CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight > blockLight ? CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight : blockLight;
                lightLevelFloat -= 0.1 * (MathUtil.sin(CosmicEvolution.instance.save.thePlayer.yaw / 45) + 1);
                lightLevelFloat -= 0.1 * (MathUtil.sin(CosmicEvolution.instance.save.thePlayer.pitch / 45) + 1);
                if (lightLevelFloat < 0.1f) {
                    lightLevelFloat = 0.1f;
                }
                int channelVal = MathUtil.floatToIntRGBA(lightLevelFloat);
                int colorRGB = channelVal << 16 | channelVal << 8 | channelVal;

                arm.rotateModelSegmentX(-45);
                arm.rotateModelSegmentY((float) -(0.35 * Math.PI));
                arm.translateModelSegment(translation.x, translation.y, translation.z);


                int colorVal;
                float[] UVSamples;
                Vector3f[] topFace = arm.topFace;
                Vector3f[] bottomFace = arm.bottomFace;
                Vector3f[] northFace = arm.northFace;
                Vector3f[] southFace = arm.southFace;
                Vector3f[] eastFace = arm.eastFace;
                Vector3f[] westFace = arm.westFace;

                colorRGB = Math.max(colorRGB, 0);

                UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 24, 40, 24, 32, 16, 40, 16);
                tessellator.addVertex2DTextureWithSampling(colorRGB, topFace[0].x, topFace[0].y, topFace[0].z, 2, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, topFace[1].x, topFace[1].y, topFace[1].z, 0, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, topFace[2].x, topFace[2].y, topFace[2].z, 1, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, topFace[3].x, topFace[3].y, topFace[3].z, 3, UVSamples[6], UVSamples[7]);
                tessellator.addElements();

                colorVal = colorRGB & 255;
                colorVal -= 10;
                colorRGB = (colorVal << 16) | (colorVal << 8) | colorVal;
                colorRGB = Math.max(colorRGB, 0);
                UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 16, 40, 16, 32, 8, 40, 8);
                tessellator.addVertex2DTextureWithSampling(colorRGB, bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3, UVSamples[6], UVSamples[7]);
                tessellator.addElements();

                colorVal = colorRGB & 255;
                colorVal -= 10;
                colorRGB = (colorVal << 16) | (colorVal << 8) | colorVal;
                colorRGB = Math.max(colorRGB, 0);
                UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 24, 8, 24, 0, 0, 8, 0);
                tessellator.addVertex2DTextureWithSampling(colorRGB, northFace[0].x, northFace[0].y, northFace[0].z, 3, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, northFace[1].x, northFace[1].y, northFace[1].z, 1, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, northFace[2].x, northFace[2].y, northFace[2].z, 2, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, northFace[3].x, northFace[3].y, northFace[3].z, 0, UVSamples[6], UVSamples[7]);
                tessellator.addElements();

                colorVal = colorRGB & 255;
                colorVal -= 10;
                colorRGB = (colorVal << 16) | (colorVal << 8) | colorVal;
                colorRGB = Math.max(colorRGB, 0);
                UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 24, 16, 24, 8, 0, 16, 0);
                tessellator.addVertex2DTextureWithSampling(colorRGB, southFace[0].x, southFace[0].y, southFace[0].z, 3, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, southFace[1].x, southFace[1].y, southFace[1].z, 1, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, southFace[2].x, southFace[2].y, southFace[2].z, 2, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, southFace[3].x, southFace[3].y, southFace[3].z, 0, UVSamples[6], UVSamples[7]);
                tessellator.addElements();

                colorVal = colorRGB & 255;
                colorVal -= 10;
                colorRGB = (colorVal << 16) | (colorVal << 8) | colorVal;
                colorRGB = Math.max(colorRGB, 0);
                UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 24, 24, 24, 16, 0, 24, 0);
                tessellator.addVertex2DTextureWithSampling(colorRGB, eastFace[0].x, eastFace[0].y, eastFace[0].z, 3, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, eastFace[1].x, eastFace[1].y, eastFace[1].z, 1, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, eastFace[2].x, eastFace[2].y, eastFace[2].z, 2, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, eastFace[3].x, eastFace[3].y, eastFace[3].z, 0, UVSamples[6], UVSamples[7]);
                tessellator.addElements();

                colorVal = colorRGB & 255;
                colorVal -= 10;
                colorRGB = (colorVal << 16) | (colorVal << 8) | colorVal;
                colorRGB = Math.max(colorRGB, 0);
                UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 24, 32, 24, 24, 0, 32, 0);
                tessellator.addVertex2DTextureWithSampling(colorRGB, westFace[0].x, westFace[0].y, westFace[0].z, 3, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, westFace[1].x, westFace[1].y, westFace[1].z, 1, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, westFace[2].x, westFace[2].y, westFace[2].z, 2, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorRGB, westFace[3].x, westFace[3].y, westFace[3].z, 0, UVSamples[6], UVSamples[7]);
                tessellator.addElements();

                Matrix4d preservedViewMatrix = CosmicEvolution.camera.viewMatrix.get(new Matrix4d());
                CosmicEvolution.camera.viewMatrix = new Matrix4d();
                GL46.glEnable(GL46.GL_CULL_FACE);
                GL46.glCullFace(GL46.GL_FRONT);
                tessellator.drawTexture2D(CosmicEvolution.instance.save.thePlayer.getTexture(), Shader.screen2DTexture, CosmicEvolution.camera);
                GL46.glDisable(GL46.GL_CULL_FACE);
                CosmicEvolution.camera.viewMatrix = preservedViewMatrix;


            } else if(itemID != Item.block.ID){
                RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
                float x = 2f;
                float y = -1f;
                float z = -3f;
                if(GameSettings.viewBob) {
                    x -= 0.125f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.viewBobTimer / 60f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    y -= 0.0625f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.viewBobTimer / 60f) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
                }
                z -= 1f * ((MathUtil.sin((float) (((CosmicEvolution.instance.save.thePlayer.swingTimer / CosmicEvolution.instance.save.thePlayer.maxSwingTimer) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                Vector3f position = new Vector3f(x,y,z);
                Matrix3f rotationMatrix = new Matrix3f();
                rotationMatrix.rotateY((float) -(0.35 * Math.PI));
                double sine = (MathUtil.sin((float) ((((double) CosmicEvolution.instance.save.thePlayer.swingTimer / CosmicEvolution.instance.save.thePlayer.maxSwingTimer) * Math.PI * 2) - (0.5 * Math.PI))) * 0.5) + 0.5f;
                rotationMatrix.rotateLocalX((float) ((float) -(0.25 * Math.PI) * sine));
                Quaternionf rotation = rotationMatrix.getUnnormalizedRotation(new Quaternionf());
                int playerX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x);
                int playerY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y);
                int playerZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z);
                float blockLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ));
                float lightLevelFloat = CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight > blockLight ? CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight : blockLight;
                lightLevelFloat -=  0.1 * (MathUtil.sin(CosmicEvolution.instance.save.thePlayer.yaw / 45) + 1);
                lightLevelFloat -=  0.1 * (MathUtil.sin(CosmicEvolution.instance.save.thePlayer.pitch / 45) + 1);
                if(lightLevelFloat < 0.1f){
                    lightLevelFloat = 0.1f;
                }
                int[] pixels = new int[1024];
                String filepath = "src/spacegame/assets/textures/item/" +
                        RenderEngine.getBlockName(Item.list[itemID].textureID, "src/spacegame/assets/textures/item/") + ".png";
                BufferedImage image = null;

                try {
                    image = ImageIO.read(new File(filepath));
                    BufferedImage argbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = argbImage.createGraphics();
                    g.drawImage(image, 0, 0, null);
                    g.dispose();
                    image = argbImage;

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                image.getRGB(0, 0, 32, 32, pixels, 0, 32);
                Color[] pixelColors = new Color[pixels.length];

                for (int i = 0; i < pixelColors.length; i++) {
                    int alpha = (pixels[i] >> 24) & 0xFF;
                    int red = (pixels[i] >> 16) & 0xFF;
                    int green = (pixels[i] >> 8) & 0xFF;
                    int blue = pixels[i] & 0xFF;

                    pixelColors[i] = new Color(red, green, blue, alpha);
                }

                Color testingColor;
                Color neighborColor;
                z = 0.5f;
                final float pixelWidth = 1 / 32f;
                float red;
                float green;
                float blue;
                float alpha;
                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                for(int i = 0; i < pixelColors.length; i++){
                    testingColor = pixelColors[i];
                    if(testingColor.getAlpha() == 255) {
                        x = (float) (i % 32) / 32;
                        y = -(float) (i / 32) / 32;
                        red = testingColor.getRed() / 255f;
                        green = testingColor.getGreen() / 255f;
                        blue = testingColor.getBlue() / 255f;
                        red *= lightLevelFloat;
                        green *= lightLevelFloat;
                        blue *= lightLevelFloat;
                        int colorValue = new Color(red, green, blue, 0).getRGB();
                        vertex1 = new Vector3f(x, y, z + pixelWidth).rotate(rotation).add(position);
                        vertex2 = new Vector3f(x + pixelWidth, y + pixelWidth, z + pixelWidth).rotate(rotation).add(position);
                        vertex3 = new Vector3f(x, y + pixelWidth, z + pixelWidth).rotate(rotation).add(position);
                        vertex4 = new Vector3f(x + pixelWidth, y, z + pixelWidth).rotate(rotation).add(position);
                        tessellator.addVertex2DTexture(colorValue, vertex1.x, vertex1.y, vertex1.z, 3);
                        tessellator.addVertex2DTexture(colorValue, vertex2.x, vertex2.y, vertex2.z, 1);
                        tessellator.addVertex2DTexture(colorValue, vertex3.x, vertex3.y, vertex3.z, 2);
                        tessellator.addVertex2DTexture(colorValue, vertex4.x, vertex4.y, vertex4.z, 0);
                        tessellator.addElements();
                        if (x == 0) {
                            //Render north face
                        }
                        if (x + pixelWidth == 1) {
                            //Render south face
                        }
                        if (y == 0) {
                            //Render top face
                        }
                        if (y - pixelWidth == -1) {
                            //Render bottom face
                        }

                        //north
                        if(i < pixelColors.length && i != 1023) {
                            neighborColor = pixelColors[i + 1];
                            if (neighborColor.getAlpha() != 255) {
                                x += pixelWidth;
                                red -= 0.05f;
                                green -= 0.05f;
                                blue -= 0.05f;
                                if (red < 0) {
                                    red = 0;
                                }
                                if (green < 0) {
                                    green = 0;
                                }
                                if (blue < 0) {
                                    blue = 0;
                                }
                                colorValue = new Color(red, green, blue, 0).getRGB();
                                vertex1 = new Vector3f(x, y, z + pixelWidth).rotate(rotation).add(position);
                                vertex2 = new Vector3f(x, y + pixelWidth, z).rotate(rotation).add(position);
                                vertex3 = new Vector3f(x, y + pixelWidth, z + pixelWidth).rotate(rotation).add(position);
                                vertex4 = new Vector3f(x, y, z).rotate(rotation).add(position);
                                tessellator.addVertex2DTexture(colorValue, vertex1.x, vertex1.y, vertex1.z, 3);
                                tessellator.addVertex2DTexture(colorValue, vertex2.x, vertex2.y, vertex2.z, 1);
                                tessellator.addVertex2DTexture(colorValue, vertex3.x, vertex3.y, vertex3.z, 2);
                                tessellator.addVertex2DTexture(colorValue, vertex4.x, vertex4.y, vertex4.z, 0);
                                tessellator.addElements();
                                x -= pixelWidth;
                            }
                        }

                        //south
                   //     if(i > 0) {
                   //         neighborColor = pixelColors[i - 1];
                   //         if (neighborColor.getAlpha() != 255) {
                   //             x -= pixelWidth;
                   //             red -= 0.17f;
                   //             green -= 0.17f;
                   //             blue -= 0.17f;
                   //             if (red < 0) {
                   //                 red = 0;
                   //             }
                   //             if (green < 0) {
                   //                 green = 0;
                   //             }
                   //             if (blue < 0) {
                   //                 blue = 0;
                   //             }
                   //             colorValue = new Color(red, green, blue, 0).getRGB();
                   //             vertex1 = new Vector3f(x, y, z).rotate(rotation).add(position);
                   //             vertex2 = new Vector3f(x, y + pixelWidth, z).rotate(rotation).add(position);
                   //             vertex3 = new Vector3f(x, y + pixelWidth, z + pixelWidth).rotate(rotation).add(position);
                   //             vertex4 = new Vector3f(x, y, z).rotate(rotation).add(position);
                   //             tessellator.addVertex2DTexture(colorValue, vertex1.x, vertex1.y, vertex1.z, 3);
                   //             tessellator.addVertex2DTexture(colorValue, vertex2.x, vertex2.y, vertex2.z, 1);
                   //             tessellator.addVertex2DTexture(colorValue, vertex3.x, vertex3.y, vertex3.z, 2);
                   //             tessellator.addVertex2DTexture(colorValue, vertex4.x, vertex4.y, vertex4.z, 0);
                   //             tessellator.addElements();
                   //             x += pixelWidth;
                   //         }
                   //     }

                        //top, this is reversed from normal
                        if (i - 32 > 0) {
                            neighborColor = pixelColors[i - 32];
                            if (neighborColor.getAlpha() != 255) {
                                y += pixelWidth;
                                red -= 0.05f;
                                green -= 0.05f;
                                blue -= 0.05f;
                                if (red < 0) {
                                    red = 0;
                                }
                                if (green < 0) {
                                    green = 0;
                                }
                                if (blue < 0) {
                                    blue = 0;
                                }
                                colorValue = new Color(red, green, blue, 0).getRGB();
                                vertex1 = new Vector3f(x + pixelWidth, y, z).rotate(rotation).add(position);
                                vertex2 = new Vector3f(x, y, z + pixelWidth).rotate(rotation).add(position);
                                vertex3 = new Vector3f(x + pixelWidth, y, z + pixelWidth).rotate(rotation).add(position);
                                vertex4 = new Vector3f(x, y, z).rotate(rotation).add(position);
                                tessellator.addVertex2DTexture(colorValue, vertex1.x, vertex1.y, vertex1.z, 3);
                                tessellator.addVertex2DTexture(colorValue, vertex2.x, vertex2.y, vertex2.z, 1);
                                tessellator.addVertex2DTexture(colorValue, vertex3.x, vertex3.y, vertex3.z, 2);
                                tessellator.addVertex2DTexture(colorValue, vertex4.x, vertex4.y, vertex4.z, 0);
                                tessellator.addElements();
                                y -= pixelWidth;
                            }
                        }

                        //bottom
                        if (i + 32 < pixelColors.length - 1) {
                            neighborColor = pixelColors[i + 32];
                            if (neighborColor.getAlpha() != 255) {
                                y -= pixelWidth;
                                red -= 0.05f;
                                green -= 0.05f;
                                blue -= 0.05f;
                                if (red < 0) {
                                    red = 0;
                                }
                                if (green < 0) {
                                    green = 0;
                                }
                                if (blue < 0) {
                                    blue = 0;
                                }
                                colorValue = new Color(red, green, blue, 0).getRGB();
                                vertex1 = new Vector3f(x, y, z).rotate(rotation).add(position);
                                vertex2 = new Vector3f(x + pixelWidth, y, z + pixelWidth).rotate(rotation).add(position);
                                vertex3 = new Vector3f(x, y, z + pixelWidth).rotate(rotation).add(position);
                                vertex4 = new Vector3f(x + pixelWidth, y, z).rotate(rotation).add(position);
                                tessellator.addVertex2DTexture(colorValue, vertex1.x, vertex1.y, vertex1.z, 3);
                                tessellator.addVertex2DTexture(colorValue, vertex2.x, vertex2.y, vertex2.z, 1);
                                tessellator.addVertex2DTexture(colorValue, vertex3.x, vertex3.y, vertex3.z, 2);
                                tessellator.addVertex2DTexture(colorValue, vertex4.x, vertex4.y, vertex4.z, 0);
                                tessellator.addElements();
                                y += pixelWidth;
                            }
                        }
                    }
                }


                Matrix4d preservedViewMatrix = CosmicEvolution.camera.viewMatrix.get(new Matrix4d());
                CosmicEvolution.camera.viewMatrix = new Matrix4d();
                GL46.glEnable(GL46.GL_CULL_FACE);
                GL46.glCullFace(GL46.GL_FRONT);
                tessellator.drawTexture2D(fillableColor, Shader.screen2DTexture, CosmicEvolution.camera);
                GL46.glDisable(GL46.GL_CULL_FACE);
                CosmicEvolution.camera.viewMatrix = preservedViewMatrix;
            }
        }
    }

    public static float getLightValueFromMap(byte lightValue) {
        return switch (lightValue) {
            case 0, 1 -> 0.1F;
            case 2 -> 0.11F;
            case 3 -> 0.13F;
            case 4 -> 0.16F;
            case 5 -> 0.2F;
            case 6 -> 0.24F;
            case 7 -> 0.29F;
            case 8 -> 0.35F;
            case 9 -> 0.42F;
            case 10 -> 0.5F;
            case 11 -> 0.58F;
            case 12 -> 0.67F;
            case 13 -> 0.77F;
            case 14 -> 0.88F;
            case 15 -> 1.0F;
            default -> 0.1F;
        };
    }

    public static void renderBlockBreakingOutline() {
        if (CosmicEvolution.instance.save.thePlayer.breakTimer != 0) {
            RenderEngine.WorldTessellator tessellator = RenderEngine.WorldTessellator.instance;
            int locationX = Integer.MIN_VALUE;
            int locationY = Integer.MIN_VALUE;
            int locationZ = Integer.MIN_VALUE;
            short block = Short.MIN_VALUE;
            ModelLoader modelLoader;
            if (!CosmicEvolution.instance.save.activeWorld.paused) {
                double[] rayCast = CosmicEvolution.camera.rayCast(3);
                final double multiplier = 0.05D;
                final double xDif = (rayCast[0] - CosmicEvolution.instance.save.thePlayer.x);
                final double yDif = (rayCast[1] - (CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height/2));
                final double zDif = (rayCast[2] - CosmicEvolution.instance.save.thePlayer.z);

                int blockX = 0;
                int blockY = 0;
                int blockZ = 0;
                for (int loopPass = 0; loopPass < 30; loopPass++) {
                    blockX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x + xDif * multiplier * loopPass);
                    blockY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y  + CosmicEvolution.instance.save.thePlayer.height/2 + yDif * multiplier * loopPass);
                    blockZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z + zDif * multiplier * loopPass);

                    Block checkedBlock = Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ)];
                    if(checkedBlock.ID == Block.crafting3DItem.ID)return;
                    if(checkedBlock.ID == Block.craftingItem.ID)return;

                    if (isBlockVisible(blockX, blockY, blockZ)) {
                        if (checkedBlock.ID != Block.air.ID && checkedBlock.ID != Block.water.ID) {
                            locationX = blockX;
                            locationY = blockY;
                            locationZ = blockZ;
                            block = CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ);
                            break;
                        }
                    }
                }

                if (locationX != Integer.MIN_VALUE && locationY != Integer.MIN_VALUE && locationZ != Integer.MIN_VALUE && block != Short.MIN_VALUE) {
                    Chunk chunk = CosmicEvolution.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(locationX >> 5, locationY >> 5, locationZ >> 5);
                    if (chunk != null) {
                        int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
                        int playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
                        int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;
                        int xOffset = (chunk.x - playerChunkX) << 5;
                        int yOffset = (chunk.y - playerChunkY) << 5;
                        int zOffset = (chunk.z - playerChunkZ) << 5;
                        Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
                        Shader.worldShader2DTextureWithAtlas.uploadVec3f("chunkOffset", chunkOffset);
                        Shader.worldShader2DTextureWithAtlas.uploadBoolean("useFog", true);
                        int textureID = (int) (((double) CosmicEvolution.instance.save.thePlayer.breakTimer/(double)Block.list[block].getDynamicBreakTimer()) * 6);
                        if(textureID > 6){
                            textureID = 6;
                        }
                        modelLoader = Block.list[block].blockModel;
                        Random rand = new Random(Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ));
                        float rotation = rand.nextFloat((float) 0, (float) (2 * Math.PI));
                        float stickScale = 0;
                        if(block == Block.itemStick.ID){
                           stickScale = rand.nextFloat(0.5f, 0.9f);
                        }
                        float translateX = rand.nextFloat(0.25f, 0.75f);
                        float translateZ = rand.nextFloat(0.25f, 0.75f);
                        Vector3f offset = new Vector3f(translateX, 0f, translateZ);
                        for (int i = 0; i < modelLoader.modelFaces.length; i++) {

                            ModelFace modelFace = modelLoader.modelFaces[i];

                            if(Block.list[block].ID == Block.itemStone.ID){
                                modelFace = Block.list[block].blockModel.copyModel().modelFaces[i];
                                for(int j = 0; j < modelFace.vertices.length; j++){
                                    modelFace.vertices[j].rotateY(rotation);
                                }
                                for(int j = 0; j < modelFace.vertices.length; j++){
                                    modelFace.vertices[j].add(offset);
                                }
                            }

                            if(Block.list[block].ID == Block.itemStick.ID){
                                modelFace = Block.list[block].blockModel.copyModel().getScaledModel(stickScale).modelFaces[i];
                                modelFace.normal.rotateY(rotation);
                                for(int j = 0; j < modelFace.vertices.length; j++){
                                    modelFace.vertices[j].rotateY(rotation);
                                }
                                for(int j = 0; j < modelFace.vertices.length; j++){
                                    modelFace.vertices[j].add(offset);
                                }
                            }


                            renderSpecialFace(tessellator, 16777215, Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ),textureID, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, chunk, blockBreakingAtlas.getTexture(textureID), 0, 0, 0, 0);
                            tessellator.addElements();
                        }
                        GL46.glEnable(GL46.GL_CULL_FACE);
                        GL46.glCullFace(GL46.GL_FRONT);
                        GL46.glEnable(GL46.GL_BLEND);
                        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                        GL46.glEnable(GL46.GL_ALPHA_TEST);
                        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);
                        GL46.glEnable(GL46.GL_POLYGON_OFFSET_FILL);
                        GL46.glPolygonOffset(-1f, -1f);
                        tessellator.drawTexture2DWithAtlas(blockBreaking, Shader.worldShader2DTextureWithAtlas, CosmicEvolution.camera);
                        GL46.glDisable(GL46.GL_POLYGON_OFFSET_FILL);
                        GL46.glDisable(GL46.GL_BLEND);
                        GL46.glDisable(GL46.GL_ALPHA_TEST);
                        GL46.glDisable(GL46.GL_CULL_FACE);
                    }
                }
            }
        }
    }

    public static void renderBlockOutline(){
        RenderEngine.WorldTessellator tessellator = RenderEngine.WorldTessellator.instance;
        int locationX = Integer.MIN_VALUE;
        int locationY = Integer.MIN_VALUE;
        int locationZ = Integer.MIN_VALUE;
        short block = Short.MIN_VALUE;
        ModelLoader modelLoader;
        if (!CosmicEvolution.instance.save.activeWorld.paused) {
            double[] rayCast = CosmicEvolution.camera.rayCast(3);
            final double multiplier = 0.05F;
            final double xDif = (rayCast[0] - CosmicEvolution.instance.save.thePlayer.x);
            final double yDif = (rayCast[1] - (CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height/2));
            final double zDif = (rayCast[2] - CosmicEvolution.instance.save.thePlayer.z);

            int blockX = 0;
            int blockY = 0;
            int blockZ = 0;
            for (int loopPass = 0; loopPass < 30; loopPass++) {
                blockX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x + xDif * multiplier * loopPass);
                blockY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y  + CosmicEvolution.instance.save.thePlayer.height/2 + yDif * multiplier * loopPass);
                blockZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z + zDif * multiplier * loopPass);

                Block checkedBlock = Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ)];

                if(checkedBlock.ID == Block.craftingItem.ID){
                    renderCraftingItemOutlines(blockX,blockY,blockZ);
                    return;
                }

                if (isBlockVisible(blockX, blockY, blockZ)) {
                    if(checkedBlock.ID != Block.air.ID && checkedBlock.ID != Block.water.ID){
                            locationX = blockX;
                            locationY = blockY;
                            locationZ = blockZ;
                            block = CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ);
                            break;
                    }
                }
            }

            if(locationX != Integer.MIN_VALUE && locationY != Integer.MIN_VALUE && locationZ != Integer.MIN_VALUE && block != Short.MIN_VALUE){
                Chunk chunk = CosmicEvolution.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(locationX >> 5, locationY >> 5, locationZ >> 5);
                if(chunk != null) {
                    int playerChunkX = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x) >> 5;
                    int playerChunkY = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y) >> 5;
                    int playerChunkZ = MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z) >> 5;
                    int xOffset = (chunk.x - playerChunkX) << 5;
                    int yOffset = (chunk.y - playerChunkY) << 5;
                    int zOffset = (chunk.z - playerChunkZ) << 5;
                    Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
                    Shader.worldShader2DTexture.uploadVec3f("chunkOffset", chunkOffset);
                    Shader.worldShader2DTexture.uploadBoolean("compressTest", true);
                    modelLoader = Block.list[block].blockModel;
                    Random rand = new Random(Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ));
                    float rotation = rand.nextFloat((float) 0, (float) (2 * Math.PI));
                    float stickScale = 0;
                    if(block == Block.itemStick.ID){
                       stickScale = rand.nextFloat(0.5f, 0.9f);
                    }
                    float translateX = rand.nextFloat(0.25f, 0.75f);
                    float translateZ = rand.nextFloat(0.25f, 0.75f);
                    Vector3f offset = new Vector3f(translateX, 0f, translateZ);
                    for(int i = 0; i < modelLoader.modelFaces.length; i++){
                        ModelFace modelFace = modelLoader.modelFaces[i];

                        if(Block.list[block].ID == Block.itemStone.ID){
                            modelFace = Block.list[block].blockModel.copyModel().modelFaces[i];
                            for(int j = 0; j < modelFace.vertices.length; j++){
                                modelFace.vertices[j].rotateY(rotation);
                            }
                            for(int j = 0; j < modelFace.vertices.length; j++){
                                modelFace.vertices[j].add(offset);
                            }
                        }

                        if(Block.list[block].ID == Block.itemStick.ID){
                            modelFace = Block.list[block].blockModel.copyModel().getScaledModel(stickScale).modelFaces[i];
                            modelFace.normal.rotateY(rotation);
                            for(int j = 0; j < modelFace.vertices.length; j++){
                                modelFace.vertices[j].rotateY(rotation);
                            }
                            for(int j = 0; j < modelFace.vertices.length; j++){
                                modelFace.vertices[j].add(offset);
                            }
                        }



                        renderSpecialFace(tessellator, 16777215, Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ), 0, modelFace, 0,0,0,0,0,0,0,0, 3,1,2,0, chunk,null, modelFace.normal.x, modelFace.normal.y, modelFace.normal.z, chunk.getSkyLightValue(locationX, locationY, locationZ));
                        if(Block.list[block].ID != Block.crafting3DItem.ID) {
                            tessellator.addElements();
                        }
                    }

                    GL46.glEnable(GL46.GL_CULL_FACE);
                    GL46.glCullFace(GL46.GL_FRONT);
                    GL46.glEnable(GL46.GL_POLYGON_OFFSET_FILL);
                    GL46.glPolygonOffset(-1f, -1f);
                    tessellator.drawTexture2D(outline, Shader.worldShader2DTexture, CosmicEvolution.camera);
                    GL46.glDisable(GL46.GL_POLYGON_OFFSET_FILL);
                    GL46.glDisable(GL46.GL_CULL_FACE);

                    Shader.worldShader2DTexture.uploadBoolean("compressTest", false);

                    if(Block.list[block].ID == Block.crafting3DItem.ID){
                        renderCrafting3DItemGrid(locationX, locationY, locationZ, chunk);
                    }
                }
            }

        }
    }

    private static void renderCraftingItemOutlines(int x, int y, int z){
        InWorldCraftingItem craftingItem = CosmicEvolution.instance.save.activeWorld.getInWorldCraftingItem(x,y,z);

        if(craftingItem == null)return;

        RenderEngine.WorldTessellator tessellator = RenderEngine.WorldTessellator.instance;

        int index = Chunk.getBlockIndexFromCoordinates(x,y,z);
        Chunk chunk = CosmicEvolution.instance.save.activeWorld.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        double rotation;
        double[] position; //local coordinates
        double translateX;
        double translateY;
        double translateZ;
        double voxelShift = 0.015625; // 1/32 then scaled by 1/2, any smaller is not viable due to float to half conversion
        long time = CosmicEvolution.instance.save.time % 120;
        if (time > 60) {
            time = 120 - time;
        }
        int r;
        int g;
        int b;
        int rDif;
        int gDif;
        int bDif;
        boolean sameItem;
        int color;
        ModelLoader baseModel;
        ModelFace modelFace;

        //Model can only be so small before issues arise
        for(int i = 0; i < craftingItem.itemsFilled.length; i++){
            if(craftingItem.itemsFilled[i])continue;

            rotation = craftingItem.outputRecipe.requiredItemAngles[i];
            position = craftingItem.outputRecipe.requiredItemPositions[i];

            int[] itemPixels = GeneralUtil.getFilePixelsARGB("src/spacegame/assets/textures/item/" +
                    RenderEngine.getBlockName(Item.list[craftingItem.outputRecipe.requiredItems[i]].textureID, "src/spacegame/assets/textures/item/") + ".png", 32, 32);

            if(itemPixels == null){
                System.out.println("Failed to get item pixels");
                continue;
            }

            itemPixels = GeneralUtil.rotateImagePixels(itemPixels, rotation, 32, 32);

            sameItem = craftingItem.outputRecipe.requiredItems[i] == CosmicEvolution.instance.save.thePlayer.getHeldItem();

            for(int j = 0; j < itemPixels.length; j++){
                if(((itemPixels[j] >> 24) & 255) != 255)continue;

                baseModel = Block.centeredVoxel.copyModel();

                translateX = (voxelShift * (j % 32)) + position[0];
                translateY = position[1];
                translateZ = (voxelShift * (j >> 5)) + position[2];

                baseModel = baseModel.translateModel((float) translateX, (float)translateY, (float) translateZ);

                r = (itemPixels[j] >> 16) & 255;
                g = (itemPixels[j] >> 8) & 255;
                b = itemPixels[j] & 255;

                if(sameItem){
                    rDif = 255 - r;
                    gDif = 255 - g;
                    bDif = 255 - b;
                    r += rDif * (time / 60f);
                    g += gDif * (time / 60f);
                    b += bDif * (time / 60f);

                    color = r << 16 | g << 8 | b;
                } else {
                    color = 0;
                }


                for(int face = 0; face < 6; face++){
                    if(face == 1)continue;
                    if(face == 2 || face == 3 || face == 4 || face == 5){
                        if(!shouldCraftingItemGridFaceRender(face, j, itemPixels)){
                            continue;
                        }
                    }


                    modelFace = baseModel.getModelFace(face);
                    renderSpecialFace(tessellator, color, index, 0, modelFace, 0,0,0,0,0,0,0,0, 3,1,2,0, chunk, null, modelFace.normal.x, modelFace.normal.y, modelFace.normal.z, 15);
                    tessellator.addElements();
                }
            }

            GL46.glEnable(GL46.GL_CULL_FACE);
            GL46.glCullFace(GL46.GL_FRONT);
            GL46.glEnable(GL46.GL_POLYGON_OFFSET_FILL);
            GL46.glPolygonOffset(-1f, -1f);
            Shader.worldShader2DTexture.uploadBoolean("performNormals", false);
            tessellator.drawTexture2D(fillableColor, Shader.worldShader2DTexture, CosmicEvolution.camera);
            Shader.worldShader2DTexture.uploadBoolean("performNormals", true);
            GL46.glDisable(GL46.GL_POLYGON_OFFSET_FILL);
            GL46.glDisable(GL46.GL_CULL_FACE);
        }
    }

    private static boolean shouldCraftingItemGridFaceRender(int face, int index, int[] pixels){
        int x = index % 32;
        int y = 31 - (index / 32);
        switch (face){
            case 2 -> { //North
                return x - 1 >= 0 && (pixels[index - 1] >> 24 & 255) != 255;
            }
            case 3 -> { //South
                return x + 1 <= 31 && (pixels[index + 1] >> 24 & 255) != 255;
            }
            case 4 -> { //East
                return y - 1 >= 0 && (pixels[index - 32] >> 24 & 255) != 255;
            }

            case 5 -> { //West
                return y + 1 <= 31 && (pixels[index + 32] >> 24 & 255) != 255;
            }

            default -> {
                return  true;
            }
        }

    }

    private static int getCraftingGridIndexPlayerIsLookingAt(int bx, int by, int bz, int layer){
        double px = CosmicEvolution.instance.save.thePlayer.x;
        double py = (CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height / 2) - (CosmicEvolution.instance.save.thePlayer.isShifting ? EntityPlayer.SHIFT_DISTANCE : 0);
        double pz = CosmicEvolution.instance.save.thePlayer.z;

        double[] ray = CosmicEvolution.camera.rayCast(3);
        Vector3d dir = new Vector3d(
                (ray[0] - CosmicEvolution.instance.save.thePlayer.x),
                (ray[1] - (CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height / 2)
                 - (CosmicEvolution.instance.save.thePlayer.isShifting ? EntityPlayer.SHIFT_DISTANCE : 0)),
                (ray[2] - CosmicEvolution.instance.save.thePlayer.z)
        );

        dir.normalize();

        double[] hit = AxisAlignedBB.intersectRayWithBlockAABB(px, py, pz, dir, bx, by, bz);

        if(hit != null) {
            return handleIntersectForInWorldCraftingBlock(hit[0], hit[1], hit[2], dir, bx, by, bz, layer);
        } else {
            return 256;
        }

    }

    private static int handleIntersectForInWorldCraftingBlock(double worldX, double worldY, double worldZ, Vector3d dir, int bx, int by, int bz, int layer) {

        // --- 2. Local coords ---
        double lx = worldX - bx;
        double ly = worldY - by;
        double lz = worldZ - bz;

        // --- 4. Ray stepping parameters ---
        double step = 0.001;     // high precision
        double maxDist = 2.5;    // enough to reach far side at shallow angles


        double layerMinY = layer / 16.0;
        double layerMaxY = (layer + 1) / 16.0;

        double tol = 0.0;

        int highlightedIndex = -1;
        int highlightedX = -1;
        int highlightedZ = -1;


        // --- 5. Step along ray and find FIRST valid voxel ---
        for (double t = 0.0; t <= maxDist; t += step) {

            double x = lx + dir.x * t;
            double y = ly + dir.y * t;
            double z = lz + dir.z * t;

            // Only break if we leave the block vertically
            if (y < 0 || y > 1)
                break;


            // Ignore X/Z out of bounds — DO NOT break
            if (x < 0 || x > 1 || z < 0 || z > 1)
                continue;


            // Must be within the active layer (with tolerance)
            if (y < layerMinY - tol|| y > layerMaxY + tol)
                continue;

            // Must be inside crafting footprint

            if (x < 0.125 || x > 0.875) continue;
            if (z < 0.125 || z > 0.875) continue;

            int xIndex = (int)((x - 0.125) / 0.0625);
            int zIndex = (int)((z - 0.125) / 0.0625);

            // Clamp to avoid out-of-bounds
            xIndex = Math.max(0, Math.min(11, xIndex));
            zIndex = Math.max(0, Math.min(11, zIndex));

            int index = xIndex + zIndex * 12;

            // FIRST valid voxel wins — stop immediately
            highlightedIndex = index;
            highlightedX = xIndex;
            highlightedZ = zIndex;

            // No voxel under cursor
            if (highlightedIndex == -1) {
                return 256;
            } else {
                return index;
            }
        }
        return 256;
    }

    private static void renderCrafting3DItemGrid(int x, int y, int z, Chunk chunk) {
        int red = 12529455;
        int green = 5947183;
        int blue = 52735;
        int index = Chunk.getBlockIndexFromCoordinates(x,y,z);
        InWorld3DCraftingItem craftingBlock = chunk.getInWorldCrafting3DItem(index);
        RenderEngine.WorldTessellator tessellator = RenderEngine.WorldTessellator.instance;

        Vector3f translationVector = new Vector3f();
        translationVector.y = craftingBlock.activeCraftingLayer / 16f;

        ModelLoader blockModel;
        ModelFace modelFace;

        int indexPlayerIsLookingAt = getCraftingGridIndexPlayerIsLookingAt(x,y,z, craftingBlock.activeCraftingLayer);
        boolean isPlayerLookingAtIndex = false;

        for (int i = 0; i < 144; i++) {
            isPlayerLookingAtIndex = i == indexPlayerIsLookingAt;

            if(craftingBlock.craftingRecipe.recipeIndices[craftingBlock.activeCraftingLayer][i] == craftingBlock.subVoxelIndices[craftingBlock.activeCraftingLayer][i] && !isPlayerLookingAtIndex)continue;

            translationVector.x = ((i % 12) * 0.0625f) + 0.125f;
            translationVector.z = ((i / 12) * 0.0625f) + 0.125f;
            blockModel = Block.crafting3DItemVoxelModel.copyModel().translateModel(translationVector.x, translationVector.y, translationVector.z);
            for(int face = 0; face < 6; face++) {
                modelFace = blockModel.getModelFace(face);
                renderSpecialFace(tessellator, isPlayerLookingAtIndex ? blue : craftingBlock.craftingRecipe.recipeIndices[craftingBlock.activeCraftingLayer][i] == 1 && craftingBlock.subVoxelIndices[craftingBlock.activeCraftingLayer][i] == 0 ? red : green, index, 0, modelFace, 0,0,0,0,0,0,0,0, 3,1,2,0, chunk,null, modelFace.normal.x, modelFace.normal.y, modelFace.normal.z, chunk.getSkyLightValue(x,y,z));
                tessellator.addElements();
            }

        }
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_FRONT);
        GL46.glEnable(GL46.GL_POLYGON_OFFSET_FILL);
        GL46.glPolygonOffset(-1f, -1f);
        Shader.worldShader2DTexture.uploadBoolean("performNormals", false);
        tessellator.drawTexture2D(subVoxelOutline, Shader.worldShader2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_POLYGON_OFFSET_FILL);
        GL46.glDisable(GL46.GL_CULL_FACE);
    }

    public static boolean isBlockVisible(int blockX, int blockY, int blockZ) {
        boolean exposed = false;
//This does not work if any of the exposed faces are air, this should be AND
        exposed |= !Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX + 1, blockY, blockZ)].isSolid;
        exposed |= !Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX - 1, blockY, blockZ)].isSolid;
        exposed |= !Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY + 1, blockZ)].isSolid;
        exposed |= !Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY - 1, blockZ)].isSolid;
        exposed |= !Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ + 1)].isSolid;
        exposed |= !Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ - 1)].isSolid;

        return exposed;
    }

    private static void renderSpecialFace(RenderEngine.WorldTessellator tessellator, int colorValue, int index, int textureID, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4, Chunk chunk, Texture texture, float normalX, float normalY, float normalZ, float skylightValue) {
        int x = (index % 32);
        int y = (index >> 10);
        int z = ((index % 1024) >> 5);

        textureID /= 16F;

        int bitMap = 0;
        switch (blockFace.faceType){
            case 0 -> {
              bitMap = chunk.topFaceBitMask[Chunk.calculateBitMaskIndex(x,z)];
                if(chunk.checkBitValue(bitMap, chunk.createMask(y)) == 0){
                    return;
                }
            }
            case 1 -> {
                bitMap = chunk.bottomFaceBitMask[Chunk.calculateBitMaskIndex(x,z)];
                if(chunk.checkBitValue(bitMap, chunk.createMask(y)) == 0){
                    return;
                }
            }
            case 2 -> {
                bitMap = chunk.northFaceBitMask[Chunk.calculateBitMaskIndex(z, y)];
                if(chunk.checkBitValue(bitMap, chunk.createMask(x)) == 0){
                    return;
                }
            }
            case 3 -> {
                bitMap = chunk.southFaceBitMask[Chunk.calculateBitMaskIndex(z, y)];
                if(chunk.checkBitValue(bitMap, chunk.createMask(x)) == 0){
                    return;
                }
            }
            case 4 -> {
                bitMap = chunk.eastFaceBitMask[Chunk.calculateBitMaskIndex(x, y)];
                if(chunk.checkBitValue(bitMap, chunk.createMask(z)) == 0){
                    return;
                }
            }
            case 5 -> {
                bitMap = chunk.westFaceBitMask[Chunk.calculateBitMaskIndex(x, y)];
                if(chunk.checkBitValue(bitMap, chunk.createMask(z)) == 0){
                    return;
                }
            }
        }


        float red = ((colorValue >> 16) & 255) / 255f;
        float green = ((colorValue >> 8) & 255) / 255f;
        float blue = (colorValue & 255) / 255f;
        float alpha = 1F;

        Vector3f blockPosition = new Vector3f(x, y, z);
        Vector3f vertex1 = new Vector3f(blockFace.vertices[0].x, blockFace.vertices[0].y, blockFace.vertices[0].z).add(blockPosition);
        Vector3f vertex2 = new Vector3f(blockFace.vertices[1].x, blockFace.vertices[1].y, blockFace.vertices[1].z).add(blockPosition);
        Vector3f vertex3 = new Vector3f(blockFace.vertices[2].x, blockFace.vertices[2].y, blockFace.vertices[2].z).add(blockPosition);
        Vector3f vertex4 = new Vector3f(blockFace.vertices[3].x, blockFace.vertices[3].y, blockFace.vertices[3].z).add(blockPosition);


        if(texture != null) {
            tessellator.vertexBuffer.put(vertex1.x);
            tessellator.vertexBuffer.put(vertex1.y);
            tessellator.vertexBuffer.put(vertex1.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(texture.texCoords[corner1].x + xSample1);
            tessellator.vertexBuffer.put(texture.texCoords[corner1].y + ySample1);
            tessellator.vertexBuffer.put((float) textureID);
            tessellator.vertexBuffer.put(normalX);
            tessellator.vertexBuffer.put(normalY);
            tessellator.vertexBuffer.put(normalZ);
            tessellator.vertexBuffer.put(skylightValue);

            tessellator.vertexBuffer.put(vertex2.x);
            tessellator.vertexBuffer.put(vertex2.y);
            tessellator.vertexBuffer.put(vertex2.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(texture.texCoords[corner2].x + xSample2);
            tessellator.vertexBuffer.put(texture.texCoords[corner2].y + ySample2);
            tessellator.vertexBuffer.put((float) textureID);
            tessellator.vertexBuffer.put(normalX);
            tessellator.vertexBuffer.put(normalY);
            tessellator.vertexBuffer.put(normalZ);
            tessellator.vertexBuffer.put(skylightValue);

            tessellator.vertexBuffer.put(vertex3.x);
            tessellator.vertexBuffer.put(vertex3.y);
            tessellator.vertexBuffer.put(vertex3.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(texture.texCoords[corner3].x + xSample3);
            tessellator.vertexBuffer.put(texture.texCoords[corner3].y + ySample3);
            tessellator.vertexBuffer.put((float) textureID);
            tessellator.vertexBuffer.put(normalX);
            tessellator.vertexBuffer.put(normalY);
            tessellator.vertexBuffer.put(normalZ);
            tessellator.vertexBuffer.put(skylightValue);

            tessellator.vertexBuffer.put(vertex4.x);
            tessellator.vertexBuffer.put(vertex4.y);
            tessellator.vertexBuffer.put(vertex4.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(texture.texCoords[corner4].x + xSample4);
            tessellator.vertexBuffer.put(texture.texCoords[corner4].y + ySample4);
            tessellator.vertexBuffer.put((float) textureID);
            tessellator.vertexBuffer.put(normalX);
            tessellator.vertexBuffer.put(normalY);
            tessellator.vertexBuffer.put(normalZ);
            tessellator.vertexBuffer.put(skylightValue);
        } else {
            tessellator.vertexBuffer.put(vertex1.x);
            tessellator.vertexBuffer.put(vertex1.y);
            tessellator.vertexBuffer.put(vertex1.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(0);
            tessellator.vertexBuffer.put(1);
            tessellator.vertexBuffer.put(normalX);
            tessellator.vertexBuffer.put(normalY);
            tessellator.vertexBuffer.put(normalZ);
            tessellator.vertexBuffer.put(skylightValue);

            tessellator.vertexBuffer.put(vertex2.x);
            tessellator.vertexBuffer.put(vertex2.y);
            tessellator.vertexBuffer.put(vertex2.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(1);
            tessellator.vertexBuffer.put(0);
            tessellator.vertexBuffer.put(normalX);
            tessellator.vertexBuffer.put(normalY);
            tessellator.vertexBuffer.put(normalZ);
            tessellator.vertexBuffer.put(skylightValue);

            tessellator.vertexBuffer.put(vertex3.x);
            tessellator.vertexBuffer.put(vertex3.y);
            tessellator.vertexBuffer.put(vertex3.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(1);
            tessellator.vertexBuffer.put(1);
            tessellator.vertexBuffer.put(normalX);
            tessellator.vertexBuffer.put(normalY);
            tessellator.vertexBuffer.put(normalZ);
            tessellator.vertexBuffer.put(skylightValue);

            tessellator.vertexBuffer.put(vertex4.x);
            tessellator.vertexBuffer.put(vertex4.y);
            tessellator.vertexBuffer.put(vertex4.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(0);
            tessellator.vertexBuffer.put(0);
            tessellator.vertexBuffer.put(normalX);
            tessellator.vertexBuffer.put(normalY);
            tessellator.vertexBuffer.put(normalZ);
            tessellator.vertexBuffer.put(skylightValue);
        }
    }


    @Override
    public Button getActiveButton() {
        return null;
    }

    public static void resetLight() {
        red = 1F;
        green = 1F;
        blue = 1F;
    }

    public static void setVertexLight1Arg(byte light, float x, float y, float z, float[] lightColor) {
        float finalLight = getLightValueFromMap(light);

        red = lightColor[0];
        green = lightColor[1];
        blue = lightColor[2];

        red *= finalLight;
        green *= finalLight;
        blue *= finalLight;
    }

    protected static int calculateVertexLightColor(Vector3f vertex, Entity associatedEntity){
        resetLight();
        float x = (float) (Math.abs(MathUtil.positiveMod(associatedEntity.x,32f) - vertex.x) + associatedEntity.x);
        float y = (float) (Math.abs(MathUtil.positiveMod(associatedEntity.y,32f) - vertex.y) + associatedEntity.y);
        float z = (float) (Math.abs(MathUtil.positiveMod(associatedEntity.z,32f) - vertex.z) + associatedEntity.z);
        int xInt = MathUtil.floorDouble(x);
        int yInt = MathUtil.floorDouble(y);
        int zInt = MathUtil.floorDouble(z);
        float[] lightColor =  !associatedEntity.canDamage ? new float[]{1,0.65f,0.65f}  : CosmicEvolution.instance.save.activeWorld.getBlockLightColor(xInt, yInt, zInt);

        byte lightVal = CosmicEvolution.instance.save.activeWorld.getBlockLightValue(xInt, yInt, zInt);
        byte skyLightVal = CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(xInt, yInt, zInt);

        setVertexLight1Arg(lightVal > skyLightVal ? lightVal : skyLightVal, x, y, z, lightColor);
        skyLightValue = GuiInGame.getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(xInt, yInt, zInt));

        return MathUtil.floatToIntRGBA(red) << 16 | MathUtil.floatToIntRGBA(green) << 8 | MathUtil.floatToIntRGBA(blue);
    }
}
