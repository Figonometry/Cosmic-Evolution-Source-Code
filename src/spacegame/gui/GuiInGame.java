package spacegame.gui;


import org.joml.*;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.*;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.core.Timer;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.*;
import spacegame.entity.animations.PlayerAnimationThrustingSpear;
import spacegame.entity.animations.PlayerAnimationThrustingSpearHold;
import spacegame.entity.animations.PlayerAnimationTillingSoil;
import spacegame.item.Item;
import spacegame.item.ItemHoe;
import spacegame.item.ItemSeed;
import spacegame.item.ItemSpear;
import spacegame.render.*;
import spacegame.render.model.ModelFace;
import spacegame.render.model.ModelLoader;
import spacegame.render.model.ModelPlayer;
import spacegame.render.model.ModelSegment;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.util.MathUtil;
import spacegame.world.AxisAlignedBB;
import spacegame.world.Chunk;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.*;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Random;

public final class GuiInGame extends Gui {
    public static int vignette;
    public static int water;
    public static int hotbar;
    public static int outline;
    public static int subVoxelOutline;
    public static int blockBreaking;
    public static TextureAtlas blockBreakingAtlas;
    public static int crossHairIndictaor;
    public static TextureAtlas crossHairIndicatorAtlas;
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
            crossHairIndictaor = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/crosshairOutlineAtlas.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
            crossHairIndicatorAtlas = CosmicEvolution.instance.renderEngine.createTextureAtlas(64, 64, 32, 32, 4, 0);
        }
    }

    @Override
    public void deleteTextures() {

    }

    @Override
    public void handleInput(){
        if(CosmicEvolution.instance.save.saveSettings.testingMode){
            if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_BACKSLASH) && KeyListener.keyReleased[GLFW.GLFW_KEY_BACKSLASH]){
                CosmicEvolution.instance.setNewGui(CosmicEvolution.instance.currentGui instanceof GuiInGame ? new GuiCommandEntry(CosmicEvolution.instance) : new GuiInGame(CosmicEvolution.instance));
                CosmicEvolution.instance.save.activeWorld.toggleWorldPause();
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_BACKSLASH);
            }
        }


        if (MouseListener.getScrollY() == -1) {
            EntityPlayer.selectedInventorySlot++;
            if (EntityPlayer.selectedInventorySlot > 8) {
                EntityPlayer.selectedInventorySlot = 0;
            }
        } else if (MouseListener.getScrollY() == 1) {
            EntityPlayer.selectedInventorySlot--;
            if (EntityPlayer.selectedInventorySlot < 0) {
                EntityPlayer.selectedInventorySlot = 8;
            }
        }


        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_1)) {
            EntityPlayer.selectedInventorySlot = 0;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_2)) {
            EntityPlayer.selectedInventorySlot = 1;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_3)) {
            EntityPlayer.selectedInventorySlot = 2;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_4)) {
            EntityPlayer.selectedInventorySlot = 3;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_5)) {
            EntityPlayer.selectedInventorySlot = 4;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_6)) {
            EntityPlayer.selectedInventorySlot = 5;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_7)) {
            EntityPlayer.selectedInventorySlot = 6;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_8)) {
            EntityPlayer.selectedInventorySlot = 7;
        }

        if (KeyListener.isKeyPressed(GLFW.GLFW_KEY_9)) {
            EntityPlayer.selectedInventorySlot = 8;
        }


        if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_TAB) && KeyListener.keyReleased[GLFW.GLFW_KEY_TAB]) {
            this.ce.setNewGui(new GuiUniverseMap(this.ce));
            CosmicEvolution.setGLClearColor(0, 0, 0, 0);
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_TAB);
        }

        if(KeyListener.isKeyPressed(GameSettings.inventoryKey.keyCode) && KeyListener.keyReleased[GameSettings.inventoryKey.keyCode]) {
            this.ce.setNewGui(new GuiInventoryPlayer(this.ce, this.ce.save.thePlayer.inventory));
            KeyListener.setKeyReleased(GameSettings.inventoryKey.keyCode);
        }

    }

    public static void renderDebugText(){
        int leftSide = -970;
        FontRenderer fontRenderer = FontRenderer.instance;
        if(GameSettings.showFPS) {
            fontRenderer.drawString(CosmicEvolution.instance.title + " (" + CosmicEvolution.instance.fps + " FPS)", leftSide, 460,-15, 16777215, 50, 255);
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
            fontRenderer.drawString("Block Light Color: " + CosmicEvolution.instance.save.activeWorld.getBlockLightColor(playerX, playerY, playerZ), leftSide, 100,-15, 16777215, 50, 255);
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
        renderDebugText();
        renderCrosshair();
        renderBlockAndEntityToolTip();
        renderVignette();
        renderHeldItem();
        renderBlockLookingAtName();
        renderMessageText();
        renderHotbar();
        renderHealthAndHungerBar();
    }

    public static void renderGuiFromOtherGuis(){
        renderDebugText();
        renderVignette();
        renderHeldItem();
        renderBlockLookingAtName();
        renderMessageText();
        renderHotbar();
        renderHealthAndHungerBar();
    }


   private static void renderCrosshair(){
       int color = 4210752;
       float fontID = 223 / 16F;
       Texture textureID = Assets.fontTextureAtlas.textures.get(223);
       int x = -15;
       int y = -15;
       float z = -10F;

       RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
       tessellator.toggleOrtho();
       tessellator.addVertex2DTextureWithAtlas(color, x, y, z, 3, textureID, fontID, 255);
       tessellator.addVertex2DTextureWithAtlas(color, x + 30, y + 30, z, 1, textureID, fontID, 255);
       tessellator.addVertex2DTextureWithAtlas(color, x, y + 30, z, 2, textureID, fontID, 255);
       tessellator.addVertex2DTextureWithAtlas(color, x + 30, y, z, 0, textureID, fontID, 255);
       tessellator.addElementsCW();
       tessellator.drawTexture2DWithAtlas(Assets.fontTextureLoader, Shader.screen2DTextureAtlas, CosmicEvolution.camera);

       if(CosmicEvolution.instance.save.thePlayer.drawingBack) {
           color = 0;

           z += 5;


           float outerMovement = 15 - 10 * (CosmicEvolution.instance.save.thePlayer.drawbackTimer / 180f);

           if(outerMovement < 5f){
               outerMovement = 5f;
           }

           float sideLength = outerMovement * 2;

           x -= outerMovement;
           y += outerMovement;

           tessellator.addVertex2DTextureWithAtlas(color, x, y, z, 3, crossHairIndicatorAtlas.getTexture(2), 2 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x + 30, y + 30, z, 1, crossHairIndicatorAtlas.getTexture(2), 2 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x, y + 30, z, 2, crossHairIndicatorAtlas.getTexture(2), 2 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x + 30, y, z, 0, crossHairIndicatorAtlas.getTexture(2), 2 / 16f, 255);
           tessellator.addElementsCW();

           x += sideLength;
           tessellator.addVertex2DTextureWithAtlas(color, x, y, z, 3, crossHairIndicatorAtlas.getTexture(3), 3 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x + 30, y + 30, z, 1, crossHairIndicatorAtlas.getTexture(3), 3 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x, y + 30, z, 2, crossHairIndicatorAtlas.getTexture(3), 3 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x + 30, y, z, 0, crossHairIndicatorAtlas.getTexture(3), 3 / 16f, 255);
           tessellator.addElementsCW();

           y -= sideLength;
           tessellator.addVertex2DTextureWithAtlas(color, x, y, z, 3, crossHairIndicatorAtlas.getTexture(1), 1 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x + 30, y + 30, z, 1, crossHairIndicatorAtlas.getTexture(1), 1 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x, y + 30, z, 2, crossHairIndicatorAtlas.getTexture(1), 1 / 16f, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x + 30, y, z, 0, crossHairIndicatorAtlas.getTexture(1), 1 / 16f, 255);
           tessellator.addElementsCW();

           x -= sideLength;
           tessellator.addVertex2DTextureWithAtlas(color, x, y, z, 3, crossHairIndicatorAtlas.getTexture(0), 0, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x + 30, y + 30, z, 1, crossHairIndicatorAtlas.getTexture(0), 0, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x, y + 30, z, 2, crossHairIndicatorAtlas.getTexture(0), 0, 255);
           tessellator.addVertex2DTextureWithAtlas(color, x + 30, y, z, 0, crossHairIndicatorAtlas.getTexture(0), 0, 255);
           tessellator.addElementsCW();

           GL46.glEnable(GL46.GL_BLEND);
           GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
           tessellator.drawTexture2DWithAtlas(crossHairIndictaor, Shader.screen2DTextureAtlas, CosmicEvolution.camera);
           GL46.glDisable(GL46.GL_BLEND);
       }

       tessellator.toggleOrtho();
   }

    public static void renderVignette(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;

        if(!CosmicEvolution.instance.save.thePlayer.freeMove) {
            short blockPlayerHeadIsIn = CosmicEvolution.instance.save.activeWorld.getBlockID(MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.x), MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height / 2), MathUtil.floorDouble(CosmicEvolution.instance.save.thePlayer.z));
            if (Block.list[blockPlayerHeadIsIn] instanceof BlockWater) {
                GL46.glEnable(GL46.GL_BLEND);
                GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                tessellator.toggleOrtho();
                tessellator.addVertex2DTexture(8355711, (float) -CosmicEvolution.width / 2, (float) -CosmicEvolution.height / 2, -900, 3);
                tessellator.addVertex2DTexture(8355711, (float) CosmicEvolution.width / 2, (float) CosmicEvolution.height / 2, -900, 1);
                tessellator.addVertex2DTexture(8355711, (float) -CosmicEvolution.width / 2, (float) CosmicEvolution.height / 2, -900, 2);
                tessellator.addVertex2DTexture(8355711, (float) CosmicEvolution.width / 2, (float) -CosmicEvolution.height / 2, -900, 0);
                tessellator.addElementsCW();
                tessellator.drawTexture2D(water, Shader.screen2DTexture, CosmicEvolution.camera);
                tessellator.toggleOrtho();
                GL46.glDisable(GL46.GL_BLEND);
                renderAirBar();
            } else if (blockPlayerHeadIsIn != Block.air.ID && Block.list[blockPlayerHeadIsIn].isSolid && blockPlayerHeadIsIn != Block.leaf.ID && !(Block.list[blockPlayerHeadIsIn] instanceof BlockDoor)) {
                int textureID = Block.list[blockPlayerHeadIsIn].textureID;
                tessellator.toggleOrtho();
                tessellator.addVertexTextureArrayWithCorner(4144959, (float) -CosmicEvolution.width / 2, (float) -CosmicEvolution.height / 2, -900, 3, textureID);
                tessellator.addVertexTextureArrayWithCorner(4144959, (float) CosmicEvolution.width / 2, (float) CosmicEvolution.height / 2, -900, 1, textureID);
                tessellator.addVertexTextureArrayWithCorner(4144959, (float) -CosmicEvolution.width / 2, (float) CosmicEvolution.height / 2, -900, 2, textureID);
                tessellator.addVertexTextureArrayWithCorner(4144959, (float) CosmicEvolution.width / 2, (float) -CosmicEvolution.height / 2, -900, 0, textureID);
                tessellator.addElementsCW();
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
        tessellator.addElementsCW();
        GL46.glDepthMask(false);
        tessellator.drawTexture2D(vignette, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDepthMask(true);
        tessellator.toggleOrtho();
        GL46.glDisable(GL46.GL_BLEND);
    }

    public static void renderBlockLookingAtName(){
        short blockID = CosmicEvolution.instance.save.thePlayer.getPlayerLookingAtBlockID();
        if(blockID == Block.air.ID || Block.list[blockID] instanceof BlockWater)return;
        if(blockID == Block.craftingItem.ID) {
            int[] coords = CosmicEvolution.instance.save.thePlayer.getPlayerLookingAtBlockCoords();
            renderCraftingItemInfoOverlay(coords[0], coords[1], coords[2]);
        }
        if(blockID >= 135 && blockID <= 150){ //Door facing directions
            int[] coords = CosmicEvolution.instance.save.thePlayer.getPlayerLookingAtBlockCoords();
            blockID = CosmicEvolution.instance.save.activeWorld.getBlockID(coords[0], coords[1] + 1, coords[2]);
        }
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        FontRenderer fontRenderer = FontRenderer.instance;

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        int[] blockCoordinates = CosmicEvolution.instance.save.thePlayer.getPlayerLookingAtBlockCoords();
        if(Block.list[blockID] instanceof ITimeUpdate && !(Block.list[blockID] instanceof BlockTorch) && !(Block.list[blockID] instanceof BlockTilledSoil && !(Block.list[blockID] instanceof BlockCrop))) {
            StringBuilder stringBuilder = new StringBuilder();
            TimeUpdateEvent updateEvent = CosmicEvolution.instance.save.activeWorld.getTimeEvent(blockCoordinates[0], blockCoordinates[1], blockCoordinates[2]);
            if(updateEvent != null) {
                long timeUntil = updateEvent.updateTime - CosmicEvolution.instance.save.time;
                long daysUntil = timeUntil / Timer.GAME_DAY;
                long hoursUntil = (timeUntil % Timer.GAME_DAY) / Timer.GAME_HOUR;
                long minutesUntil = (timeUntil % Timer.GAME_HOUR) / Timer.GAME_MINUTE;

                stringBuilder.append(((ITimeUpdate) Block.list[blockID]).getDisplayStringText(blockCoordinates[0], blockCoordinates[1], blockCoordinates[2], CosmicEvolution.instance.save.activeWorld));
                if (daysUntil != 0) {
                    stringBuilder.append(daysUntil).append(daysUntil != 1 ? " Days " : " Day ");
                }
                if (hoursUntil != 0 || (minutesUntil != 0 && daysUntil != 0)) {
                    stringBuilder.append(hoursUntil).append(hoursUntil != 1 ? " Hours " : " Hour ");
                }
                if (minutesUntil > 0) {
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
            tessellator.addElementsCW();
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
            tessellator.addElementsCW();
            tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
            tessellator.toggleOrtho();
            fontRenderer.drawCenteredString(Block.list[blockID].getDisplayName(blockCoordinates[0], blockCoordinates[1], blockCoordinates[2]), 0, 425, -14, 16777215, 50, 255);
        }
        GL46.glDisable(GL46.GL_BLEND);
    }

    private static void renderCraftingItemInfoOverlay(int bx, int by, int bz) {
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        FontRenderer fontRenderer = FontRenderer.instance;

        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);

        InWorldCraftingItem craftingItem = (InWorldCraftingItem) CosmicEvolution.instance.save.activeWorld.getBlockState(bx, by, bz, MultiState.CRAFTING_ITEM_STATE);
        if (craftingItem == null) return;

        tessellator.toggleOrtho();
        int x = 640;
        int y = 0;
        float width = 512;
        float height = 512;

        tessellator.addVertex2DTexture(0, x - width / 2f, y - height / 2f, -900, 3);
        tessellator.addVertex2DTexture(0, x + width / 2f, y + height / 2f, -900, 1);
        tessellator.addVertex2DTexture(0, x - width / 2f, y + height / 2f, -900, 2);
        tessellator.addVertex2DTexture(0, x + width / 2f, y - height / 2f, -900, 0);
        tessellator.addElementsCW();
        tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();
        y = 200;
        fontRenderer.drawCenteredString(Item.list[craftingItem.outputRecipe.itemID].getDisplayName(craftingItem.outputRecipe.itemID, craftingItem.outputRecipe.metadata), x, y, -14, 16777215, 50, 255);
        y -= 120;
        for (int i = 0; i < craftingItem.itemsFilled.length; i++) {
            fontRenderer.drawCenteredString((craftingItem.itemsFilled[i] ? "COMPLETED: " : "MISSING: ") +
                            Item.list[craftingItem.outputRecipe.requiredItems[i]].getDisplayName(craftingItem.outputRecipe.requiredItems[i], craftingItem.outputRecipe.requiredItemMetadata[i]), x, y,
                    -14, craftingItem.itemsFilled[i] ? 255 << 8 : 255 << 16, 50, 255);

            if (!craftingItem.itemsFilled[i] && craftingItem.outputRecipe.requiredItems[i] != Item.block.ID) {
                y -= 30;

                ModelLoader model = Item.list[craftingItem.outputRecipe.requiredItems[i]].getItemModel(craftingItem.outputRecipe.requiredItemMetadata[i]).copyModel();
                model.scaleModel(76f);
                model.rotateModel(45, 0, 1, 0);
                model.rotateModel(36, 1, 0, 0);
                model.translateModel(x, y, -200);

                ModelFace face;
                float textureID;
                int colorVal = 255;

                int colorRGB = 0;

                int colorTop = ((colorVal) << 16) | ((colorVal) << 8) | colorVal;
                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;

                for (int faceIndex = 0; faceIndex < model.modelFaces.length; faceIndex++) {
                    face = model.modelFaces[faceIndex];

                    switch (face.faceType) {
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


                    textureID = face.texture;

                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[0].x, face.vertices[0].y, face.vertices[0].z, textureID, face.UVs[0][0], face.UVs[0][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[1].x, face.vertices[1].y, face.vertices[1].z, textureID, face.UVs[1][0], face.UVs[1][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[2].x, face.vertices[2].y, face.vertices[2].z, textureID, face.UVs[2][0], face.UVs[2][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[3].x, face.vertices[3].y, face.vertices[3].z, textureID, face.UVs[3][0], face.UVs[3][1]);
                    tessellator.addElementsCCW();
                }

                tessellator.toggleOrtho();
                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                tessellator.toggleOrtho();
            }

            if (!craftingItem.itemsFilled[i] && craftingItem.outputRecipe.requiredItems[i] == Item.block.ID) {
                y -= 30;

                ModelLoader model = Block.list[craftingItem.outputRecipe.requiredItemMetadata[i]].getBlockModel(0, 0, 0, CosmicEvolution.instance.save.activeWorld).copyModel();
                model.translateModel(-0.5f, 0, -0.5f);
                model.scaleModel(76f);
                model.rotateModel(45, 0, 1, 0);
                model.rotateModel(36, 1, 0, 0);
                model.translateModel(0.5f, 0, 0.5f);
                model.translateModel(x, y, -200);

                ModelFace face;
                float textureID;
                int colorVal = 255;

                int colorRGB = 0;

                int colorTop = ((colorVal) << 16) | ((colorVal) << 8) | colorVal;
                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;
                for (int faceIndex = 0; faceIndex < model.modelFaces.length; faceIndex++) {
                    face = model.modelFaces[faceIndex];
                    textureID = Block.list[craftingItem.outputRecipe.requiredItemMetadata[i]].getBlockTexture(craftingItem.outputRecipe.requiredItemMetadata[i], 0, 0, 0, face.faceType);

                    switch (face.faceType) {
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


                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[0].x, face.vertices[0].y, face.vertices[0].z, textureID, face.UVs[0][0], face.UVs[0][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[1].x, face.vertices[1].y, face.vertices[1].z, textureID, face.UVs[1][0], face.UVs[1][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[2].x, face.vertices[2].y, face.vertices[2].z, textureID, face.UVs[2][0], face.UVs[2][1]);
                    tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[3].x, face.vertices[3].y, face.vertices[3].z, textureID, face.UVs[3][0], face.UVs[3][1]);
                    tessellator.addElementsCCW();
                }

                tessellator.toggleOrtho();
                tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                tessellator.toggleOrtho();
            }
            y -= !craftingItem.itemsFilled[i] ? 75 : 30;
        }

        if (craftingItem.outputRecipe.requiresBinding && !craftingItem.hasBeenBound && craftingItem.areAllItemsFilled()) {
            y -= 30;
            fontRenderer.drawCenteredString("MISSING: " + Item.reedTwine.getDisplayName(Item.NULL_ITEM_REFERENCE, Item.NULL_ITEM_METADATA), x, y, -14, 255 << 16, 50, 255);
            tessellator.toggleOrtho();

            ModelLoader model = Item.reedTwine.getItemModel(Item.NULL_ITEM_METADATA).copyModel();
            model.translateModel(-0.5f, 0, -0.5f);
            model.scaleModel(76f);
            model.rotateModel(45, 0, 1, 0);
            model.rotateModel(36, 1, 0, 0);
            model.translateModel(0.5f, 0, 0.5f);
            model.translateModel(x, y, -200);

            ModelFace face;
            float textureID;
            int colorVal = 255;

            int colorRGB = 0;

            int colorTop = ((colorVal) << 16) | ((colorVal) << 8) | colorVal;
            int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
            int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
            int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
            int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
            int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;
            for (int faceIndex = 0; faceIndex < model.modelFaces.length; faceIndex++) {
                face = model.modelFaces[faceIndex];
                textureID = face.texture;

                switch (face.faceType) {
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


                tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[0].x, face.vertices[0].y, face.vertices[0].z, textureID, face.UVs[0][0], face.UVs[0][1]);
                tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[1].x, face.vertices[1].y, face.vertices[1].z, textureID, face.UVs[1][0], face.UVs[1][1]);
                tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[2].x, face.vertices[2].y, face.vertices[2].z, textureID, face.UVs[2][0], face.UVs[2][1]);
                tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[3].x, face.vertices[3].y, face.vertices[3].z, textureID, face.UVs[3][0], face.UVs[3][1]);
                tessellator.addElementsCCW();


                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                tessellator.toggleOrtho();
            }

            GL46.glDisable(GL46.GL_BLEND);
        }
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
        tessellator.addVertex2DTexture(0, x, y, -190, 3);
        tessellator.addVertex2DTexture(0, x + 864, y + 96, -190, 1);
        tessellator.addVertex2DTexture(0, x, y + 96, -190, 2);
        tessellator.addVertex2DTexture(0, x + 864, y, -190, 0);
        tessellator.addElementsCW();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int size = 96;
        if(EntityPlayer.selectedInventorySlot == 0){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 0 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 0 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 0 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 0 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 1){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 1 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 1 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 1 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 1 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 2){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 2 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 2 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 2 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 2 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 3){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 3 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 3 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 3 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 3 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 4){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 4 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 4 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 4 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 4 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 5){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 5 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 5 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 5 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 5 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 6){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 6 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 6 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 6 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 6 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 7){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 7 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 7 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 7 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 7 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 8){
            size = 102;
        }
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 8 ? 0 : 16777215, x, y, -180, 3);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 8 ? 0 : 16777215, x + size, y + size, -180, 1);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 8 ? 0 : 16777215, x, y + size, -180, 2);
        tessellator.addVertex2DTexture(EntityPlayer.selectedInventorySlot == 8 ? 0 : 16777215, x + size, y, -180, 0);
        tessellator.addElementsCW();
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

        if(CosmicEvolution.instance.currentGui instanceof GuiInventoryStrawChest){
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
        tessellator.addElementsCW();

        float progress = (int) (368 * (CosmicEvolution.instance.save.thePlayer.health / CosmicEvolution.instance.save.thePlayer.maxHealth));
        tessellator.addVertex2DTexture(11016473, x, y, -89, 3);
        tessellator.addVertex2DTexture(11016473, x + progress, y + 16, -89, 1);
        tessellator.addVertex2DTexture(11016473, x, y + 16, -89, 2);
        tessellator.addVertex2DTexture(11016473, x + progress, y, -89, 0);
        tessellator.addElementsCW();

        x += 496;
        tessellator.addVertex2DTexture(5522201, x, y, -90, 3);
        tessellator.addVertex2DTexture(5522201, x + 368, y + 16, -90, 1);
        tessellator.addVertex2DTexture(5522201, x, y + 16, -90, 2);
        tessellator.addVertex2DTexture(5522201, x + 368, y, -90, 0);
        tessellator.addElementsCW();

        progress = (int) (368 * (CosmicEvolution.instance.save.thePlayer.saturation / CosmicEvolution.instance.save.thePlayer.maxSaturation));
        tessellator.addVertex2DTexture(16764748, x, y, -89, 3);
        tessellator.addVertex2DTexture(16764748, x + progress, y + 16, -89, 1);
        tessellator.addVertex2DTexture(16764748, x, y + 16, -89, 2);
        tessellator.addVertex2DTexture(16764748, x + progress, y, -89, 0);
        tessellator.addElementsCW();



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
        tessellator.addElementsCW();

        float progress = (int) (864 * ((300 - CosmicEvolution.instance.save.thePlayer.drowningTimer) / 300f));
        tessellator.addVertex2DTexture(38143, x, y, -89, 3);
        tessellator.addVertex2DTexture(38143, x + progress, y + 16, -89, 1);
        tessellator.addVertex2DTexture(38143, x, y + 16, -89, 2);
        tessellator.addVertex2DTexture(38143, x + progress, y, -89, 0);
        tessellator.addElementsCW();
        tessellator.drawTexture2D(fillableColorWithShadedBottom, Shader.screen2DTexture, CosmicEvolution.camera);
        tessellator.toggleOrtho();
    }


    public static void renderHeldItem() {
        EntityPlayer player = CosmicEvolution.instance.save.thePlayer;
        final short heldBlock = player.getHeldBlock();
        final short heldMetadata = player.getHeldMetadata();
        if(player.isHoldingBlock()) {
            if (heldBlock != Block.air.ID) {
                float x = 3f;
                float y = -2.5f;
                float z = -3f;
                if(Block.list[heldBlock] instanceof BlockItemStone || heldBlock == Block.itemClay.ID || heldBlock == Block.treeSeed.ID || heldBlock == Block.berryBush.ID || heldBlock == Block.reedLower.ID){
                    y += 0.5f;
                }
                if(heldBlock == Block.itemStick.ID){
                    x += 1f;
                    y += 0.5f;
                }
                if(GameSettings.viewBob) {
                    x -= 0.5f * ((MathUtil.sin((float) (((player.viewBobTimer /(player.sprinting ? 30f : 60f)) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    y -= 0.25f * ((MathUtil.sin((float) (((player.viewBobTimer / (player.sprinting ? 30f : 60f)) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
                }
                z -= 1f * ((MathUtil.sin((float) ((((float)player.swingTimer / (float)player.maxSwingTimer) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                Vector3f position = new Vector3f(x,y,z);
                Matrix3f rotationMatrix = new Matrix3f();
                rotationMatrix.rotateY((float) (0.25 * Math.PI));
                double sine = (MathUtil.sin((float) ((((double) player.swingTimer / player.maxSwingTimer) * Math.PI * 2) - (0.5 * Math.PI))) * 0.5) + 0.5f;
                rotationMatrix.rotateLocalX((float) ((float) -(0.25 * Math.PI) * sine));
                Quaternionf rotation = rotationMatrix.getUnnormalizedRotation(new Quaternionf());
                int playerX = MathUtil.floorDouble(player.x);
                int playerY = MathUtil.floorDouble(player.y);
                int playerZ = MathUtil.floorDouble(player.z);
                float blockLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ));
                float lightLevelFloat = CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight > blockLight ? CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight : blockLight;
                lightLevelFloat -=  0.1 * (MathUtil.sin(player.yaw / 45) + 1);
                lightLevelFloat -=  0.1 * (MathUtil.sin(player.pitch / 45) + 1);
                if(lightLevelFloat < 0.1){
                    lightLevelFloat = 0.1f;
                }
                int channelVal = MathUtil.floatToIntRGBA(lightLevelFloat);
                if(heldBlock == Block.torch.ID){
                    channelVal = 255;
                }
                int colorRGB = channelVal << 16 | channelVal << 8 | channelVal;

                RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
                ModelLoader model = Block.list[heldBlock].getBlockModel(0,0,0, CosmicEvolution.instance.save.activeWorld).copyModel();
                model.translateModel(-0.5f, 0, -0.5f);
                if(Block.list[heldBlock] instanceof BlockItemStone ){
                    model.translateModel(0.5f, 0, 0.5f);
                    model.scaleModel(2f);
                }

                int colorVal;

                int colorTop = colorRGB;

                colorVal = colorRGB & 255;

                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;

                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                float textureID;
                for(int face = 0; face < 6; face++){
                    ModelFace[] faces = model.getModelFaceOfType(face);
                    for(int i = 0; i < faces.length; i++){
                        if(faces[i] == null)continue;

                        textureID = RenderBlocks.getBlockTextureID(heldBlock, face, MathUtil.floorDouble(player.x), MathUtil.floorDouble(player.y), MathUtil.floorDouble(player.z));
                        vertex1 = new Vector3f(faces[i].vertices[0].x, faces[i].vertices[0].y, faces[i].vertices[0].z).rotate(rotation).add(position);
                        vertex2 = new Vector3f(faces[i].vertices[1].x, faces[i].vertices[1].y, faces[i].vertices[1].z).rotate(rotation).add(position);
                        vertex3 = new Vector3f(faces[i].vertices[2].x, faces[i].vertices[2].y, faces[i].vertices[2].z).rotate(rotation).add(position);
                        vertex4 = new Vector3f(faces[i].vertices[3].x, faces[i].vertices[3].y, faces[i].vertices[3].z).rotate(rotation).add(position);


                        switch (faces[i].faceType){
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


                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex1.x, vertex1.y, vertex1.z, textureID, faces[i].UVs[0][0], faces[i].UVs[0][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex2.x, vertex2.y, vertex2.z, textureID, faces[i].UVs[1][0], faces[i].UVs[1][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex3.x, vertex3.y, vertex3.z, textureID, faces[i].UVs[2][0], faces[i].UVs[2][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex4.x, vertex4.y, vertex4.z, textureID, faces[i].UVs[3][0], faces[i].UVs[3][1]);
                        tessellator.addElementsCCW();
                        colorVal = colorRGB & 255;
                        colorVal -= 10;
                        colorRGB = (colorVal << 16) | (colorVal << 8) | colorVal;
                    }
                }

                Matrix4d preservedViewMatrix = CosmicEvolution.camera.viewMatrix.get(new Matrix4d());
                CosmicEvolution.camera.viewMatrix = new Matrix4d();
                GL46.glEnable(GL46.GL_CULL_FACE);
                GL46.glCullFace(GL46.GL_BACK);
                tessellator.drawTextureArray(Assets.blockTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
                GL46.glDisable(GL46.GL_CULL_FACE);
                CosmicEvolution.camera.viewMatrix = preservedViewMatrix;
            }
        } else {
            short itemID = player.getHeldItem();
            if(itemID == Item.NULL_ITEM_REFERENCE) {
                RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
                ModelSegment arm = ModelPlayer.getBaseModel().segments[ModelPlayer.LEFT_ARM];
                arm.scale(1.5f);

                float translateX = 1.5f;
                float translateY = -1.25f;
                float translateZ = -2f;
                if (GameSettings.viewBob) {
                    translateX -= 0.125f * ((MathUtil.sin((float) (((player.viewBobTimer / (player.sprinting ? 30f : 60f)) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    translateY -= 0.0625f * ((MathUtil.sin((float) (((player.viewBobTimer / (player.sprinting ? 30f : 60f)) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
                }
                translateZ -= 1f * ((MathUtil.sin((float) ((((float)player.swingTimer / (float)player.maxSwingTimer) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);

                Vector3f translation = new Vector3f(translateX, translateY, translateZ);
                Matrix3f rotationMatrix = new Matrix3f();
                rotationMatrix.rotateY((float) -(0.35 * Math.PI));
                double sine = (MathUtil.sin((float) ((((double) player.swingTimer / (float)player.maxSwingTimer) * Math.PI * 2) - (0.5 * Math.PI))) * 0.5) + 0.5f;
                rotationMatrix.rotateLocalX((float) ((float) -(0.25 * Math.PI) * sine));

                int playerX = MathUtil.floorDouble(player.x);
                int playerY = MathUtil.floorDouble(player.y);
                int playerZ = MathUtil.floorDouble(player.z);
                float blockLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ));
                float lightLevelFloat = CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight > blockLight ? CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight : blockLight;
                lightLevelFloat -= 0.1 * (MathUtil.sin(player.yaw / 45) + 1);
                lightLevelFloat -= 0.1 * (MathUtil.sin(player.pitch / 45) + 1);
                if (lightLevelFloat < 0.1f) {
                    lightLevelFloat = 0.1f;
                }
                int channelVal = MathUtil.floatToIntRGBA(lightLevelFloat);
                int colorRGB = channelVal << 16 | channelVal << 8 | channelVal;

                arm.rotateModelSegmentX(-45);
                arm.rotateModelSegmentX((float) (-45 * sine));
                arm.rotateModelSegmentY((float) -(0.35 * Math.PI));
                arm.translateModelSegment(translation.x, translation.y, translation.z);


                int colorVal;

                int colorTop = colorRGB;

                colorVal = colorRGB & 255;

                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;
                float[] UVSamples;
                Vector3f[] topFace = arm.topFace;
                Vector3f[] bottomFace = arm.bottomFace;
                Vector3f[] northFace = arm.northFace;
                Vector3f[] southFace = arm.southFace;
                Vector3f[] eastFace = arm.eastFace;
                Vector3f[] westFace = arm.westFace;


                UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 24, 40, 24, 32, 16, 40, 16);
                tessellator.addVertex2DTextureWithSampling(colorTop, topFace[0].x, topFace[0].y, topFace[0].z, 2, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorTop, topFace[1].x, topFace[1].y, topFace[1].z, 0, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorTop, topFace[2].x, topFace[2].y, topFace[2].z, 1, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorTop, topFace[3].x, topFace[3].y, topFace[3].z, 3, UVSamples[6], UVSamples[7]);
                tessellator.addElementsCW();

                UVSamples = MathUtil.mapUVCoordinatesTopBottom(88, 96, 32, 16, 40, 16, 32, 8, 40, 8);
                tessellator.addVertex2DTextureWithSampling(colorBottom, bottomFace[0].x, bottomFace[0].y, bottomFace[0].z, 2, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorBottom, bottomFace[1].x, bottomFace[1].y, bottomFace[1].z, 0, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorBottom, bottomFace[2].x, bottomFace[2].y, bottomFace[2].z, 1, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorBottom, bottomFace[3].x, bottomFace[3].y, bottomFace[3].z, 3, UVSamples[6], UVSamples[7]);
                tessellator.addElementsCW();

                UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 0, 24, 8, 24, 0, 0, 8, 0);
                tessellator.addVertex2DTextureWithSampling(colorNorth, northFace[0].x, northFace[0].y, northFace[0].z, 3, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorNorth, northFace[1].x, northFace[1].y, northFace[1].z, 1, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorNorth, northFace[2].x, northFace[2].y, northFace[2].z, 2, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorNorth, northFace[3].x, northFace[3].y, northFace[3].z, 0, UVSamples[6], UVSamples[7]);
                tessellator.addElementsCW();

                UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 8, 24, 16, 24, 8, 0, 16, 0);
                tessellator.addVertex2DTextureWithSampling(colorSouth, southFace[0].x, southFace[0].y, southFace[0].z, 3, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorSouth, southFace[1].x, southFace[1].y, southFace[1].z, 1, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorSouth, southFace[2].x, southFace[2].y, southFace[2].z, 2, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorSouth, southFace[3].x, southFace[3].y, southFace[3].z, 0, UVSamples[6], UVSamples[7]);
                tessellator.addElementsCW();

                UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 16, 24, 24, 24, 16, 0, 24, 0);
                tessellator.addVertex2DTextureWithSampling(colorEast, eastFace[0].x, eastFace[0].y, eastFace[0].z, 3, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorEast, eastFace[1].x, eastFace[1].y, eastFace[1].z, 1, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorEast, eastFace[2].x, eastFace[2].y, eastFace[2].z, 2, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorEast, eastFace[3].x, eastFace[3].y, eastFace[3].z, 0, UVSamples[6], UVSamples[7]);
                tessellator.addElementsCW();

                UVSamples = MathUtil.mapUVCoordinatesNSEW(88, 96, 24, 24, 32, 24, 24, 0, 32, 0);
                tessellator.addVertex2DTextureWithSampling(colorWest, westFace[0].x, westFace[0].y, westFace[0].z, 3, UVSamples[0], UVSamples[1]);
                tessellator.addVertex2DTextureWithSampling(colorWest, westFace[1].x, westFace[1].y, westFace[1].z, 1, UVSamples[2], UVSamples[3]);
                tessellator.addVertex2DTextureWithSampling(colorWest, westFace[2].x, westFace[2].y, westFace[2].z, 2, UVSamples[4], UVSamples[5]);
                tessellator.addVertex2DTextureWithSampling(colorWest, westFace[3].x, westFace[3].y, westFace[3].z, 0, UVSamples[6], UVSamples[7]);
                tessellator.addElementsCW();

                Matrix4d preservedViewMatrix = CosmicEvolution.camera.viewMatrix.get(new Matrix4d());
                CosmicEvolution.camera.viewMatrix = new Matrix4d();
                GL46.glEnable(GL46.GL_CULL_FACE);
                GL46.glCullFace(GL46.GL_FRONT);
                tessellator.drawTexture2D(player.getTexture(), Shader.screen2DTexture, CosmicEvolution.camera);
                GL46.glDisable(GL46.GL_CULL_FACE);
                CosmicEvolution.camera.viewMatrix = preservedViewMatrix;


            } else if(itemID != Item.block.ID){
                RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;

                float x = 3f;
                float y = -2f;
                float z = -3f;
                if(GameSettings.viewBob) {
                    x -= 0.125f * ((MathUtil.sin((float) (((player.viewBobTimer / (player.sprinting ? 30f : 60f)) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    y -= 0.0625f * ((MathUtil.sin((float) (((player.viewBobTimer / (player.sprinting ? 30f : 60f)) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
                }
                if(player.playerAnimation == null) {
                    z -= 1f * ((MathUtil.sin((float) ((((float) player.swingTimer / (float) player.maxSwingTimer) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                }

                Vector3f position = new Vector3f(x,y,z);
                Matrix3f rotationMatrix = new Matrix3f();
                if(player.playerAnimation == null) {
                    double sine = (MathUtil.sin((float) ((((double) player.swingTimer / player.maxSwingTimer) * Math.PI * 2) - (0.5 * Math.PI))) * 0.5) + 0.5f;
                    rotationMatrix.rotateLocalX((float) ((float) -(0.25 * Math.PI) * sine));
                }

                if(Item.list[itemID] instanceof ItemSpear){
                    rotationMatrix = new Matrix3f();
                    z = -3f;


                    if(player.playerAnimation instanceof PlayerAnimationThrustingSpearHold) {
                        position.z += 1f * 1 - (player.playerAnimation.timer / 60f);
                    }

                    if(player.playerAnimation instanceof PlayerAnimationThrustingSpear){
                        position.z += 1f;

                        position.z -= 4f * ((MathUtil.sin((float) ((((float) player.playerAnimation.timer / 30f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    }

                    if(player.drawingBack){
                        float ratio = player.drawbackTimer / 60f;
                        if(ratio > 1)ratio = 1;
                        position.z += ratio;
                    }

                }


                if(Item.list[player.getHeldItem()] instanceof ItemHoe){
                    float ratio;

                    if(player.playerAnimation instanceof PlayerAnimationTillingSoil){
                        if(player.playerAnimation.timer > 45){
                            ratio = ((player.playerAnimation.timer - 60) * -1) / 15f;
                            rotationMatrix.rotateLocalX((float) (0.25 * Math.PI) * ratio);
                        } else if(player.playerAnimation.timer > 15){
                            rotationMatrix.rotateLocalX((float) (0.25 * Math.PI));
                        } else if (player.playerAnimation.timer > 0) {
                            ratio = (15 - player.playerAnimation.timer) / 15f;
                            rotationMatrix.rotateLocalX((float) (0.25 * Math.PI));
                            rotationMatrix.rotateLocalX((float) (-0.5 * Math.PI) * ratio);
                        } else if(player.playerAnimation.timer > -15) {
                            ratio = (player.playerAnimation.timer * -1) / 15f;
                            rotationMatrix.rotateLocalX((float) (-0.25 * Math.PI));
                            rotationMatrix.rotateLocalX((float) (0.25 * Math.PI) * ratio);
                        }
                    }
                }

                Quaternionf rotation = rotationMatrix.getUnnormalizedRotation(new Quaternionf());

                int playerX = MathUtil.floorDouble(player.x);
                int playerY = MathUtil.floorDouble(player.y);
                int playerZ = MathUtil.floorDouble(player.z);

                float blockLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ));
                float lightLevelFloat = Math.max(CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight, blockLight);
                lightLevelFloat -=  0.1 * (MathUtil.sin(player.yaw / 45) + 1);
                lightLevelFloat -=  0.1 * (MathUtil.sin(player.pitch / 45) + 1);
                if(lightLevelFloat < 0.1f){
                    lightLevelFloat = 0.1f;
                }
                int channelVal = MathUtil.floatToIntRGBA(lightLevelFloat);
                int colorRGB = channelVal << 16 | channelVal << 8 | channelVal;


                ModelLoader model = Item.list[itemID].getItemModel(heldMetadata).copyModel();
                model.scaleModel(2);

                Vector3f vertex1;
                Vector3f vertex2;
                Vector3f vertex3;
                Vector3f vertex4;
                float textureID;
                int colorVal;

                int colorTop = colorRGB;

                colorVal = colorRGB & 255;

                int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
                int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
                int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
                int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
                int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;
                for(int face = 0; face < 6; face++){
                    ModelFace[] faces = model.getModelFaceOfType(face);
                    for(int i = 0; i < faces.length; i++){
                        if(faces[i] == null)continue;

                        textureID = faces[i].texture;
                        vertex1 = new Vector3f(faces[i].vertices[0].x, faces[i].vertices[0].y, faces[i].vertices[0].z).rotate(rotation).add(position);
                        vertex2 = new Vector3f(faces[i].vertices[1].x, faces[i].vertices[1].y, faces[i].vertices[1].z).rotate(rotation).add(position);
                        vertex3 = new Vector3f(faces[i].vertices[2].x, faces[i].vertices[2].y, faces[i].vertices[2].z).rotate(rotation).add(position);
                        vertex4 = new Vector3f(faces[i].vertices[3].x, faces[i].vertices[3].y, faces[i].vertices[3].z).rotate(rotation).add(position);

                        switch (faces[i].faceType){
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

                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex1.x, vertex1.y, vertex1.z, textureID, faces[i].UVs[0][0], faces[i].UVs[0][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex2.x, vertex2.y, vertex2.z, textureID, faces[i].UVs[1][0], faces[i].UVs[1][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex3.x, vertex3.y, vertex3.z, textureID, faces[i].UVs[2][0], faces[i].UVs[2][1]);
                        tessellator.addVertexTextureArrayWithUV(colorRGB, vertex4.x, vertex4.y, vertex4.z, textureID, faces[i].UVs[3][0], faces[i].UVs[3][1]);
                        tessellator.addElementsCCW();
                    }
                }




                Matrix4d preservedViewMatrix = CosmicEvolution.camera.viewMatrix.get(new Matrix4d());
                CosmicEvolution.camera.viewMatrix = new Matrix4d();
                GL46.glEnable(GL46.GL_CULL_FACE);
                GL46.glCullFace(GL46.GL_BACK);
                tessellator.drawTextureArray(Assets.itemTextureArray, Shader.screenTextureArray, CosmicEvolution.camera);
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
                final double multiplier = 0.005;
                final double xDif = (rayCast[0] - CosmicEvolution.instance.save.thePlayer.x);
                final double yDif = (rayCast[1] - (CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height/2));
                final double zDif = (rayCast[2] - CosmicEvolution.instance.save.thePlayer.z);

                int blockX = 0;
                int blockY = 0;
                int blockZ = 0;
                for (int loopPass = 0; loopPass < 300; loopPass++) {

                    double cx = CosmicEvolution.instance.save.thePlayer.x + xDif * multiplier * loopPass;
                    double cy = CosmicEvolution.instance.save.thePlayer.y  + CosmicEvolution.instance.save.thePlayer.height/2 + yDif * multiplier * loopPass;
                    double cz = CosmicEvolution.instance.save.thePlayer.z + zDif * multiplier * loopPass;

                    blockX = MathUtil.floorDouble(cx);
                    blockY = MathUtil.floorDouble(cy);
                    blockZ = MathUtil.floorDouble(cz);

                    Block checkedBlock = Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ)];
                    if(checkedBlock.ID == Block.crafting3DItem.ID)return;
                    if(checkedBlock.ID == Block.craftingItem.ID)return;

                    if (isBlockVisible(blockX, blockY, blockZ) && intersectsBlockBoundingBox(checkedBlock, cx, cy, cz)) {
                        if (checkedBlock.ID != Block.air.ID && !(checkedBlock instanceof BlockWater)) {
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
                        modelLoader = Block.list[block].getBlockModel(locationX,locationY,locationZ, chunk.parentWorld);
                        Random rand = new Random(Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ));
                        float rotation = rand.nextFloat((float) 0, (float) (2 * Math.PI));
                        float stickScale = 0;
                        if(block == Block.itemStick.ID){
                           stickScale = rand.nextFloat(0.5f, 0.9f);
                        }
                        float translateX = rand.nextFloat(0.25f, 0.75f);
                        float translateZ = rand.nextFloat(0.25f, 0.75f);
                        Vector3f offset = new Vector3f(translateX, 0f, translateZ);

                        if(block == Block.doorPrimitiveUpper.ID || block == Block.doorPrimitiveLower.ID){
                            DoorState doorState = (DoorState) CosmicEvolution.instance.save.activeWorld.getBlockState(locationX,  block == Block.doorPrimitiveUpper.ID ? locationY - 1 : locationY, locationZ, MultiState.DOOR_STATE);
                            ModelLoader baseModel = block == Block.doorPrimitiveLower.ID ? BlockModelList.primitiveDoorLower : modelLoader;
                            handleDoorModel(modelLoader, doorState, baseModel);
                        }


                        for (int i = 0; i < modelLoader.modelFaces.length; i++) {

                            ModelFace modelFace = modelLoader.modelFaces[i];

                            if(Block.list[block] instanceof BlockItemStone){
                                modelFace = Block.list[block].getBlockModel(locationX,locationY,locationZ, chunk.parentWorld).copyModel().modelFaces[i];
                                for(int j = 0; j < modelFace.vertices.length; j++){
                                    modelFace.vertices[j].rotateY(rotation);
                                }
                                for(int j = 0; j < modelFace.vertices.length; j++){
                                    modelFace.vertices[j].add(offset);
                                }
                            }

                            if(Block.list[block].ID == Block.itemStick.ID){
                                ModelLoader model =  Block.list[block].getBlockModel(locationX,locationY,locationZ, chunk.parentWorld).copyModel();
                                model.scaleModel(stickScale);
                                modelFace = model.modelFaces[i];
                                modelFace.normal.rotateY(rotation);
                                for(int j = 0; j < modelFace.vertices.length; j++){
                                    modelFace.vertices[j].rotateY(rotation);
                                }
                                for(int j = 0; j < modelFace.vertices.length; j++){
                                    modelFace.vertices[j].add(offset);
                                }
                            }


                            renderSpecialFace(tessellator, 16777215, Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ),textureID, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, chunk, blockBreakingAtlas.getTexture(textureID), 0, 0, 0, 0);
                            tessellator.addElementsCCW();
                        }

                        GL46.glEnable(GL46.GL_CULL_FACE);
                        GL46.glCullFace(GL46.GL_BACK);
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

    private static void handleDoorModel(ModelLoader modelLoader, DoorState doorState, ModelLoader baseModel){
        switch (doorState.facingDirection){
            case DoorState.FACE_DIRECTION_NORTH -> {
                if(doorState.isOpen && doorState.hingeLeft){
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(270, 0, 1, 0);
                    modelLoader.translateModel(0.5f, 0, 0.9375f);
                } else if(doorState.isOpen && doorState.hingeRight){
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(90, 0, 1, 0);
                    modelLoader.translateModel(0.5f, 0, 0.0625f);
                } else {
                    modelLoader = baseModel.copyModel();
                    modelLoader.translateModel(0.0625f, 0, 0.5f);
                }
            }
            case DoorState.FACE_DIRECTION_SOUTH -> {
                if(doorState.isOpen && doorState.hingeLeft){
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(90, 0, 1, 0);
                    modelLoader.translateModel(0.5f, 0, 0.0625f);
                } else if(doorState.isOpen && doorState.hingeRight){
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(270, 0, 1, 0);
                    modelLoader.translateModel(0.5f, 0, 0.9375f);
                } else {
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(180, 0, 1, 0);
                    modelLoader.translateModel(0.9375f, 0, 0.5f);
                }
            }
            case DoorState.FACE_DIRECTION_EAST -> {
                if(doorState.isOpen && doorState.hingeLeft){
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(180, 0, 1, 0);
                    modelLoader.translateModel(0.0625f, 0, 0.5f);
                } else if(doorState.isOpen && doorState.hingeRight){
                    modelLoader = baseModel.copyModel();
                    modelLoader.translateModel(0.9375f, 0, 0.5f);
                } else {
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(270, 0, 1, 0);
                    modelLoader.translateModel(0.5f, 0, 0.0625f);
                }
            }
            case DoorState.FACE_DIRECTION_WEST -> {
                if(doorState.isOpen && doorState.hingeLeft){
                    modelLoader = baseModel.copyModel();
                    modelLoader.translateModel(0.9375f, 0, 0.5f);
                } else if(doorState.isOpen && doorState.hingeRight){
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(180, 0, 1, 0);
                    modelLoader.translateModel(0.0625f, 0, 0.5f);
                } else {
                    modelLoader = baseModel.copyModel();
                    modelLoader.rotateModel(90, 0, 1, 0);
                    modelLoader.translateModel(0.5f, 0, 0.9375f);
                }
            }
            default -> {
                throw new IllegalStateException("Unknown Face Direction on door");
            }
        }
    }

    private static boolean intersectsBlockBoundingBox(Block block, double x, double y, double z) {
        double localX = x - MathUtil.floorDouble(x);
        double localY = y - MathUtil.floorDouble(y);
        double localZ = z - MathUtil.floorDouble(z);

        return block.standardCollisionBoundingBox.pointInsideBoundingBox(localX, localY, localZ);
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
            final double multiplier = 0.005;
            final double xDif = (rayCast[0] - CosmicEvolution.instance.save.thePlayer.x);
            final double yDif = (rayCast[1] - (CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height/2));
            final double zDif = (rayCast[2] - CosmicEvolution.instance.save.thePlayer.z);

            int blockX = 0;
            int blockY = 0;
            int blockZ = 0;
            for (int loopPass = 0; loopPass < 300; loopPass++) {

                double cx = CosmicEvolution.instance.save.thePlayer.x + xDif * multiplier * loopPass;
                double cy = CosmicEvolution.instance.save.thePlayer.y  + CosmicEvolution.instance.save.thePlayer.height/2 + yDif * multiplier * loopPass;
                double cz = CosmicEvolution.instance.save.thePlayer.z + zDif * multiplier * loopPass;


                blockX = MathUtil.floorDouble(cx);
                blockY = MathUtil.floorDouble(cy);
                blockZ = MathUtil.floorDouble(cz);

                Block checkedBlock = Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ)];

                if(checkedBlock.ID == Block.craftingItem.ID){
                    renderCraftingItemOutlines(blockX,blockY,blockZ);
                    return;
                }

                if (isBlockVisible(blockX, blockY, blockZ) && intersectsBlockBoundingBox(checkedBlock, cx, cy, cz)) {
                    if(checkedBlock.ID != Block.air.ID && !(checkedBlock instanceof BlockWater)){
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
                    modelLoader = Block.list[block].getBlockModel(locationX,locationY,locationZ, chunk.parentWorld);
                    Random rand = new Random(Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ));
                    float rotation = rand.nextFloat((float) 0, (float) (2 * Math.PI));
                    float stickScale = 0;
                    if(block == Block.itemStick.ID){
                       stickScale = rand.nextFloat(0.5f, 0.9f);
                    }
                    float translateX = rand.nextFloat(0.25f, 0.75f);
                    float translateZ = rand.nextFloat(0.25f, 0.75f);
                    Vector3f offset = new Vector3f(translateX, 0f, translateZ);

                    if(block == Block.doorPrimitiveUpper.ID || block == Block.doorPrimitiveLower.ID){
                        DoorState doorState = (DoorState) CosmicEvolution.instance.save.activeWorld.getBlockState(locationX,  block == Block.doorPrimitiveUpper.ID ? locationY - 1 : locationY, locationZ, MultiState.DOOR_STATE);
                        ModelLoader baseModel = block == Block.doorPrimitiveLower.ID ? BlockModelList.primitiveDoorLower : modelLoader;
                        handleDoorModel(modelLoader, doorState, baseModel);
                    }


                    for(int i = 0; i < modelLoader.modelFaces.length; i++){
                        ModelFace modelFace = modelLoader.modelFaces[i];

                        if(Block.list[block] instanceof BlockItemStone){
                            modelFace = Block.list[block].getBlockModel(locationX,locationY,locationZ,chunk.parentWorld).copyModel().modelFaces[i];
                            for(int j = 0; j < modelFace.vertices.length; j++){
                                modelFace.vertices[j].rotateY(rotation);
                            }
                            for(int j = 0; j < modelFace.vertices.length; j++){
                                modelFace.vertices[j].add(offset);
                            }
                        }

                        if(Block.list[block].ID == Block.itemStick.ID){
                            ModelLoader model = Block.list[block].getBlockModel(locationX,locationY,locationZ,chunk.parentWorld).copyModel();
                            model.scaleModel(stickScale);
                            modelFace = model.modelFaces[i];
                            modelFace.normal.rotateY(rotation);
                            for(int j = 0; j < modelFace.vertices.length; j++){
                                modelFace.vertices[j].rotateY(rotation);
                            }
                            for(int j = 0; j < modelFace.vertices.length; j++){
                                modelFace.vertices[j].add(offset);
                            }
                        }

                        boolean renderFace = CosmicEvolution.instance.save.activeWorld.shouldFaceRender(locationX, locationY, locationZ, modelFace.faceType);
                        if(renderFace) {
                            renderSpecialFace(tessellator, 16777215, Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ), 0, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, chunk, null, modelFace.normal.x, modelFace.normal.y, modelFace.normal.z, chunk.getSkyLightValue(locationX, locationY, locationZ));
                            if (Block.list[block].ID != Block.crafting3DItem.ID) {
                                tessellator.addElementsCCW();
                            }
                        }
                    }

                    GL46.glEnable(GL46.GL_CULL_FACE);
                    GL46.glCullFace(GL46.GL_BACK);
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


    private static Entity hasHitEntity(double x, double y, double z){
        World world = CosmicEvolution.instance.save.activeWorld;

        int chunkX = MathUtil.floorDouble(x) >> 5;
        int chunkY = MathUtil.floorDouble(y) >> 5;
        int chunkZ = MathUtil.floorDouble(z) >> 5;

        ArrayList<Entity> entities = world.getEntitiesInChunks(world.getSurroundingChunksAndCurrentChunk(chunkX, chunkY, chunkZ));

        for(int i = 0; i < entities.size(); i++){
            if(entities.get(i) instanceof EntityLiving){
                if(((EntityLiving) entities.get(i)).isDead){
                    if(entities.get(i).boundingBox.pointInsideBoundingBox(x,y,z)) { //This determines if you hit an entity
                        return entities.get(i);
                    }
                }
            }
        }
        return null;
    }
    private static void renderBlockAndEntityToolTip(){
        int locationX = Integer.MIN_VALUE;
        int locationY = Integer.MIN_VALUE;
        int locationZ = Integer.MIN_VALUE;
        short block = Short.MIN_VALUE;
        if (!CosmicEvolution.instance.save.activeWorld.paused) {
            double[] rayCast = CosmicEvolution.camera.rayCast(3);
            final double multiplier = 0.01;
            final double xDif = (rayCast[0] - CosmicEvolution.instance.save.thePlayer.x);
            final double yDif = (rayCast[1] - (CosmicEvolution.instance.save.thePlayer.y + CosmicEvolution.instance.save.thePlayer.height/2));
            final double zDif = (rayCast[2] - CosmicEvolution.instance.save.thePlayer.z);

            int blockX = 0;
            int blockY = 0;
            int blockZ = 0;
            for (int loopPass = 0; loopPass < 300; loopPass++) {

                double cx = CosmicEvolution.instance.save.thePlayer.x + xDif * multiplier * loopPass;
                double cy = CosmicEvolution.instance.save.thePlayer.y  + CosmicEvolution.instance.save.thePlayer.height/2 + yDif * multiplier * loopPass;
                double cz = CosmicEvolution.instance.save.thePlayer.z + zDif * multiplier * loopPass;

                Entity entity = hasHitEntity(cx, cy, cz);
                if(entity instanceof EntityLiving){
                    renderEntityTooltip((EntityLiving) entity);
                }

                blockX = MathUtil.floorDouble(cx);
                blockY = MathUtil.floorDouble(cy);
                blockZ = MathUtil.floorDouble(cz);

                Block checkedBlock = Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ)];


                if (isBlockVisible(blockX, blockY, blockZ) && intersectsBlockBoundingBox(checkedBlock, cx, cy, cz)) {
                    if(checkedBlock.ID != Block.air.ID && !(checkedBlock instanceof BlockWater)){
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
                    renderBlockTooltip(locationX, locationY, locationZ, chunk);
                }
            }

        }
    }

    private static void renderCraftingItemOutlines(int x, int y, int z){
        InWorldCraftingItem craftingItem = (InWorldCraftingItem) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CRAFTING_ITEM_STATE);

        if(craftingItem == null)return;

        RenderEngine.WorldTessellator tessellator = RenderEngine.WorldTessellator.instance;

        long time = CosmicEvolution.instance.save.time % 120;
        if (time > 60) {
            time = 120 - time;
        }
        int r;
        int g;
        int b;
        boolean sameItem;

        //Model can only be so small before issues arise
        for(int i = 0; i < craftingItem.itemsFilled.length; i++){
            if(craftingItem.itemsFilled[i])continue;

            ModelLoader model;

            if(craftingItem.outputRecipe.requiredItems[i] == Item.block.ID){
                model = Block.list[craftingItem.outputRecipe.requiredItemMetadata[i]].getBlockModel(0,0,0, CosmicEvolution.instance.save.activeWorld).copyModel();
            } else {
                model = Item.list[craftingItem.outputRecipe.requiredItems[i]].getItemModel(craftingItem.outputRecipe.requiredItemMetadata[i]).copyModel();
            }

            ModelFace face;

            model.scaleModel(0.5f);
            if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandAxe.ID ||
                    craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandShovel.ID ||
                    craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandKnifeBlade.ID){
                model.scaleModel(0.75f);
                model.rotateModel(90, 0, 0, 1);
                if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandShovel.ID){
                    model.rotateModel(-90, 0, 1, 0);
                }
                if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneHandKnifeBlade.ID){
                    model.rotateModel(135f, 0, 1, 0);
                    model.scaleModel(2.1f);
                }
            }

            if(craftingItem.outputRecipe.requiredItems[i] == Item.stoneHoeHead.ID){
                model.rotateModel(90f, 0, 0, 1);
            }

            model.rotateModel((float) craftingItem.outputRecipe.requiredItemAngles[i], 0, 1, 0);
            model.translateModel(x & 31,y & 31, z & 31);
            model.translateModel((float) craftingItem.outputRecipe.requiredItemPositions[i][0], (float) craftingItem.outputRecipe.requiredItemPositions[i][1], (float) craftingItem.outputRecipe.requiredItemPositions[i][2]);

            sameItem = craftingItem.outputRecipe.requiredItems[i] == CosmicEvolution.instance.save.thePlayer.getHeldItem();
            float blockLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockLightValue(x, y, z));
            float skyLight = getLightValueFromMap(CosmicEvolution.instance.save.activeWorld.getBlockSkyLightValue(x,y,z));
            float lightLevelFloat = CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight > blockLight ? CosmicEvolution.instance.save.activeWorld.chunkController.renderWorldScene.baseLight : blockLight;
            lightLevelFloat -=  0.1 * (MathUtil.sin(CosmicEvolution.instance.save.thePlayer.yaw / 45) + 1);
            lightLevelFloat -=  0.1 * (MathUtil.sin(CosmicEvolution.instance.save.thePlayer.pitch / 45) + 1);
            if(lightLevelFloat < 0.1){
                lightLevelFloat = 0.1f;
            }
            int channelVal = MathUtil.floatToIntRGBA(lightLevelFloat);
            int colorRGB;

            if(!sameItem){
                colorRGB = 0;
            } else {
                r = channelVal;
                g = channelVal;
                b = channelVal;

                r -= channelVal * (time / 60f);
                g -= channelVal * (time / 60f);
                b -= channelVal * (time / 60f);

                colorRGB = r << 16 | g << 8 | b;
            }

            for(int j = 0; j < model.modelFaces.length; j++){
                face = model.modelFaces[j];
                if(face == null)continue;

                float textureID = craftingItem.outputRecipe.requiredItems[i] == Item.block.ID ?
                        Block.list[craftingItem.outputRecipe.requiredItemMetadata[i]].getBlockTexture(0, 0, 0, face.faceType) : Block.itemBlock.getBlockTexture(0, 0, 0, face.texture);

                tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[0].x, face.vertices[0].y, face.vertices[0].z, textureID, face.normal.x, face.normal.y, face.normal.z, skyLight,face.UVs[0][0], face.UVs[0][1]);
                tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[1].x, face.vertices[1].y, face.vertices[1].z, textureID, face.normal.x, face.normal.y, face.normal.z, skyLight,face.UVs[1][0], face.UVs[1][1]);
                tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[2].x, face.vertices[2].y, face.vertices[2].z, textureID, face.normal.x, face.normal.y, face.normal.z, skyLight,face.UVs[2][0], face.UVs[2][1]);
                tessellator.addVertexTextureArrayWithUV(colorRGB, face.vertices[3].x, face.vertices[3].y, face.vertices[3].z, textureID, face.normal.x, face.normal.y, face.normal.z, skyLight,face.UVs[3][0], face.UVs[3][1]);
                tessellator.addElementsCCW();
            }


            GL46.glEnable(GL46.GL_CULL_FACE);
            GL46.glCullFace(GL46.GL_BACK);
            Shader.worldShaderTextureArray.uploadBoolean("performNormals", false);
            tessellator.drawTextureArray(Assets.blockTextureArray, Shader.worldShaderTextureArray, CosmicEvolution.camera);
            Shader.worldShaderTextureArray.uploadBoolean("performNormals", true);
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
        InWorld3DCraftingItem craftingBlock = (InWorld3DCraftingItem) chunk.getBlockState(index, MultiState.CRAFTING_3D_ITEM_STATE);
        if(craftingBlock == null)return;
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
            blockModel = BlockModelList.crafting3DItemVoxelModel.copyModel();
            blockModel.translateModel(translationVector.x, translationVector.y, translationVector.z);
            for(int face = 0; face < 6; face++) {
                modelFace = blockModel.getModelFace(face);
                renderSpecialFace(tessellator, isPlayerLookingAtIndex ? blue : craftingBlock.craftingRecipe.recipeIndices[craftingBlock.activeCraftingLayer][i] == 1 && craftingBlock.subVoxelIndices[craftingBlock.activeCraftingLayer][i] == 0 ? red : green, index, 0, modelFace, 0,0,0,0,0,0,0,0, 3,1,2,0, chunk,null, modelFace.normal.x, modelFace.normal.y, modelFace.normal.z, chunk.getSkyLightValue(x,y,z));
                tessellator.addElementsCCW();
            }

        }
        GL46.glEnable(GL46.GL_CULL_FACE);
        GL46.glCullFace(GL46.GL_BACK);
        GL46.glEnable(GL46.GL_POLYGON_OFFSET_FILL);
        GL46.glPolygonOffset(-1f, -1f);
        Shader.worldShader2DTexture.uploadBoolean("performNormals", false);
        tessellator.drawTexture2D(subVoxelOutline, Shader.worldShader2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_POLYGON_OFFSET_FILL);
        GL46.glDisable(GL46.GL_CULL_FACE);
    }

    private static void renderEntityTooltip(EntityLiving entityLiving){
        if(!GameSettings.blockTooltips)return;
        //Attempt to grab ToolTip array, if null exit function

        ToolTipGroup[] toolTips = entityLiving.getToolTip();
        if(toolTips == null)return;

        //Transform world coordinates to chunk local coordinate range of 0-31

        //Get the center point of the block, utilize the AABB instead of the center of a full block
        double xPos = MathUtil.positiveMod(entityLiving.x, 32);
        double yPos = MathUtil.positiveMod(entityLiving.y, 32);
        double zPos = MathUtil.positiveMod(entityLiving.z, 32);

        Chunk chunk = CosmicEvolution.instance.save.activeWorld.findChunkFromChunkCoordinates(MathUtil.floorDouble(entityLiving.x) >> 5, MathUtil.floorDouble(entityLiving.y) >> 5, MathUtil.floorDouble(entityLiving.z) >> 5);

        Vector3d centerPoint = new Vector3d(xPos, yPos, zPos);

        //Transform the centerpoint into screenspace coordinates utilizing the same pipeline the GPU uses
        //This would normally be done with floats however the camera's matrices are doubles to handle precision issues when translating

        //Calculate the chunk offset to transform into appropriate world coordinates relative to the camera
        EntityPlayer player = CosmicEvolution.instance.save.thePlayer;
        Camera camera = CosmicEvolution.camera;
        int offsetX = (chunk.x - player.chunkX) << 5;
        int offsetY = (chunk.y - player.chunkY) << 5;
        int offsetZ = (chunk.z - player.chunkZ) << 5;
        Vector3d chunkOffset = new Vector3d(offsetX, offsetY, offsetZ);


        Vector3d worldPos = new Vector3d(centerPoint).add(chunkOffset);

        //Transform world pos by the view matrix
        Vector4d viewPos = new Vector4d(worldPos, 1.0f);
        viewPos.mul(camera.viewMatrix);

        //Transform viewPos by the projection matrix
        Vector4d clipPos = new Vector4d(viewPos).mul(camera.projectionMatrix);

        //Convert clip coordinates to normalized device coordinates
        float ndcX = (float) (clipPos.x / clipPos.w);
        float ndcY = (float) (clipPos.y / clipPos.w);
        float ndcZ = (float) (clipPos.z / clipPos.w);

        //Convert NDC to screen coordinates
        //These variables are the screen width/height as they are what's assigned to the viewport, which translates to the framebuffers
        float screenX = (ndcX * 0.5f + 0.5f) * CosmicEvolution.width;
        float screenY = (ndcY * 0.5f + 0.5f) * CosmicEvolution.height;

        screenX -= (CosmicEvolution.width/2f);
        screenY -= (CosmicEvolution.height/2f);

        //Assemble vertex data for all tooltips
        //The top version will be rendered normally with a white color and second version will be rendered just behind it with a gray color, block and item models will not render twice
        //This makes it easier on the eyes
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        ToolTip toolTip;
        float width;
        float height = 50;
        float x;
        float y;
        float z = -100;
        int currentIndex;
        float size;
        int color = 16777215;
        for(int i = 0; i < toolTips.length * 2; i++){
            int previousImage = -1; //Start at an invalid value
            currentIndex = i / 2;
            toolTip = toolTips[currentIndex].getCurrentTooltip();
            width = calculateToolTipLength(toolTip);
            x = screenX - (width/2f);
            y = screenY;


            if((i & 1) == 1){ //Shift for secondary draw before moving to the next tooltip
                x += 3;
                y -= 3;
                z -= 1;
                color = 4210752; //Gray
            }


            final float noSpacing = 0f;
            final float normalSpacing = 0.34f * 50;
            final float doubleSpacing = normalSpacing * 4;
            float spacing = normalSpacing;
            for(int k = 0; k < toolTip.tooltip.size(); k++){
                if(k == 0){
                    previousImage = toolTip.tooltip.get(k);
                    continue; //Skip the first index, there's nothing before it, assign variable
                }


                if((k & 1) == 0){ //Even index, therefore it's the image integer
                    if(previousImage != toolTip.tooltip.get(k) && toolTip.tooltip.get(k) != ToolTip.TEXT_BOX_ATLAS){ //If the previousImage does not match the current one use double spacing, otherwise use normal spacing
                        spacing = doubleSpacing;
                        if(previousImage == ToolTip.TEXT_BOX_ATLAS){
                            spacing = normalSpacing;
                        }

                        if(previousImage == ToolTip.TEXT_BOX_ATLAS && (toolTip.tooltip.get(k - 1) == MouseAndKeyIconTextureList.FULL_BOUND_BOX || toolTip.tooltip.get(k - 1) == MouseAndKeyIconTextureList.BOUND_BOX_RIGHT)){
                            spacing = doubleSpacing;
                        }

                    } else if(toolTip.tooltip.get(k) != ToolTip.TEXT_BOX_ATLAS) { //use normal spacing unless it's using the textbox atlas, this will not advance the value since it draws over chars
                        spacing = normalSpacing;
                    } else { //Executes if the image is the text box atlas
                        spacing = noSpacing;
                    }
                    previousImage = toolTip.tooltip.get(k); //Assign variable
                } else {
                    //Add vertex data and continue so there's no double assignment to the x value
                    switch (previousImage){
                        case ToolTip.FONT_ATLAS -> {
                            size = 50f;
                            int indexInTexture = toolTip.tooltip.get(k);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y - size/2f, z, 3, Assets.fontTextureAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y + size/2f, z, 1, Assets.fontTextureAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y + size/2f, z, 2, Assets.fontTextureAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y - size/2f, z, 0, Assets.fontTextureAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addElementsCW();
                        }
                        case ToolTip.TEXT_BOX_ATLAS -> {
                            x -= spacing;
                            size = 75f;
                            int indexInTexture = toolTip.tooltip.get(k);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y - size/2f, z, 3, Assets.textBoxAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y + size/2f, z, 1, Assets.textBoxAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y + size/2f, z, 2, Assets.textBoxAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y - size/2f, z, 0, Assets.textBoxAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addElementsCW();
                            x += spacing;
                        }

                        case ToolTip.BLOCK_ARRAY -> {
                            if((i & 1) == 1)break; //Do not render the models twice
                            int indexInTexture = toolTip.tooltip.get(k);
                            renderBlockModelForTooltip(tessellator, x,y,z, indexInTexture, previousImage);
                        }

                        case ToolTip.ITEM_ARRAY -> {
                            if((i & 1) == 1)break;
                            int indexInTexture = toolTip.tooltip.get(k);
                            renderItemModelForTooltip(tessellator,x,y,z, indexInTexture, previousImage);
                        }

                        case ToolTip.MOUSE_ICON_ATLAS -> {
                            size = 75f;
                            int indexInTexture = toolTip.tooltip.get(k);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y - size/2f, z, 3, Assets.mouseIconAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y + size/2f, z, 1, Assets.mouseIconAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y + size/2f, z, 2, Assets.mouseIconAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y - size/2f, z, 0, Assets.mouseIconAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addElementsCW();
                        }
                    }



                    continue; //The advancement of the value only needs to be calculated once
                }


                x += spacing;
            }
        }

        //draw
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawToolTip();
        GL46.glDisable(GL46.GL_BLEND);
    }


    private static void renderBlockTooltip(int bx, int by, int bz, Chunk chunk){
        if(!GameSettings.blockTooltips)return;
        //Attempt to grab ToolTip array, if null exit function
        short blockID = chunk.getBlockID(bx,by,bz);

        ToolTipGroup[] toolTips = Block.list[blockID].getBlockToolTips(bx, by, bz, chunk.parentWorld, CosmicEvolution.instance.save.thePlayer);
        if(toolTips == null)return;

        //Transform world coordinates to chunk local coordinate range of 0-31
        int localX = bx & 31;
        int localY = by & 31;
        int localZ = bz & 31;

        //Get the center point of the block, utilize the AABB instead of the center of a full block
        AxisAlignedBB blockBoundingBox = Block.list[blockID].standardCollisionBoundingBox;
        double xPos = (float) (localX + ((blockBoundingBox.maxX - blockBoundingBox.minX) / 2.0));
        double yPos = (float) (localY + ((blockBoundingBox.maxY - blockBoundingBox.minY) / 2.0));
        double zPos = (float) (localZ + ((blockBoundingBox.maxZ - blockBoundingBox.minZ) / 2.0));

        Vector3d centerPoint = new Vector3d(xPos, yPos, zPos);

        //Transform the centerpoint into screenspace coordinates utilizing the same pipeline the GPU uses
        //This would normally be done with floats however the camera's matrices are doubles to handle precision issues when translating

        //Calculate the chunk offset to transform into appropriate world coordinates relative to the camera
        EntityPlayer player = CosmicEvolution.instance.save.thePlayer;
        Camera camera = CosmicEvolution.camera;
        int offsetX = (chunk.x - player.chunkX) << 5;
        int offsetY = (chunk.y - player.chunkY) << 5;
        int offsetZ = (chunk.z - player.chunkZ) << 5;
        Vector3d chunkOffset = new Vector3d(offsetX, offsetY, offsetZ);


        Vector3d worldPos = new Vector3d(centerPoint).add(chunkOffset);

        //Transform world pos by the view matrix
        Vector4d viewPos = new Vector4d(worldPos, 1.0f);
        viewPos.mul(camera.viewMatrix);

        //Transform viewPos by the projection matrix
        Vector4d clipPos = new Vector4d(viewPos).mul(camera.projectionMatrix);

        //Convert clip coordinates to normalized device coordinates
        float ndcX = (float) (clipPos.x / clipPos.w);
        float ndcY = (float) (clipPos.y / clipPos.w);
        float ndcZ = (float) (clipPos.z / clipPos.w);

        //Convert NDC to screen coordinates
        //These variables are the screen width/height as they are what's assigned to the viewport, which translates to the framebuffers
        float screenX = (ndcX * 0.5f + 0.5f) * CosmicEvolution.width;
        float screenY = (ndcY * 0.5f + 0.5f) * CosmicEvolution.height;

        screenX -= (CosmicEvolution.width/2f);
        screenY -= (CosmicEvolution.height/2f);

        //Assemble vertex data for all tooltips
        //The top version will be rendered normally with a white color and second version will be rendered just behind it with a gray color, block and item models will not render twice
        //This makes it easier on the eyes
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        ToolTip toolTip;
        float width;
        float height = 50;
        float x;
        float y;
        float z = -100;
        int currentIndex;
        float size;
        int color = 16777215;
        for(int i = 0; i < toolTips.length * 2; i++){
            int previousImage = -1; //Start at an invalid value
            currentIndex = i / 2;
            toolTip = toolTips[currentIndex].getCurrentTooltip();
            width = calculateToolTipLength(toolTip);
            x = screenX - (width/2f);
            y = screenY;


            if((i & 1) == 1){ //Shift for secondary draw before moving to the next tooltip
                x += 3;
                y -= 3;
                z -= 1;
                color = 4210752; //Gray
            }


            final float noSpacing = 0f;
            final float normalSpacing = 0.34f * 50;
            final float doubleSpacing = normalSpacing * 4;
            float spacing = normalSpacing;
            for(int k = 0; k < toolTip.tooltip.size(); k++){
                if(k == 0){
                    previousImage = toolTip.tooltip.get(k);
                    continue; //Skip the first index, there's nothing before it, assign variable
                }


                if((k & 1) == 0){ //Even index, therefore it's the image integer
                    if(previousImage != toolTip.tooltip.get(k) && toolTip.tooltip.get(k) != ToolTip.TEXT_BOX_ATLAS){ //If the previousImage does not match the current one use double spacing, otherwise use normal spacing
                        spacing = doubleSpacing;
                        if(previousImage == ToolTip.TEXT_BOX_ATLAS){
                            spacing = normalSpacing;
                        }

                        if(previousImage == ToolTip.TEXT_BOX_ATLAS && (toolTip.tooltip.get(k - 1) == MouseAndKeyIconTextureList.FULL_BOUND_BOX || toolTip.tooltip.get(k - 1) == MouseAndKeyIconTextureList.BOUND_BOX_RIGHT)){
                            spacing = doubleSpacing;
                        }

                    } else if(toolTip.tooltip.get(k) != ToolTip.TEXT_BOX_ATLAS) { //use normal spacing unless it's using the textbox atlas, this will not advance the value since it draws over chars
                        spacing = normalSpacing;
                    } else { //Executes if the image is the text box atlas
                        spacing = noSpacing;
                    }
                    previousImage = toolTip.tooltip.get(k); //Assign variable
                } else {
                    //Add vertex data and continue so there's no double assignment to the x value
                    switch (previousImage){
                        case ToolTip.FONT_ATLAS -> {
                            size = 50f;
                            int indexInTexture = toolTip.tooltip.get(k);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y - size/2f, z, 3, Assets.fontTextureAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y + size/2f, z, 1, Assets.fontTextureAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y + size/2f, z, 2, Assets.fontTextureAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y - size/2f, z, 0, Assets.fontTextureAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addElementsCW();
                        }
                        case ToolTip.TEXT_BOX_ATLAS -> {
                            x -= spacing;
                            size = 75f;
                            int indexInTexture = toolTip.tooltip.get(k);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y - size/2f, z, 3, Assets.textBoxAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y + size/2f, z, 1, Assets.textBoxAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y + size/2f, z, 2, Assets.textBoxAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y - size/2f, z, 0, Assets.textBoxAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addElementsCW();
                            x += spacing;
                        }

                        case ToolTip.BLOCK_ARRAY -> {
                            if((i & 1) == 1)break; //Do not render the models twice
                            int indexInTexture = toolTip.tooltip.get(k);
                            renderBlockModelForTooltip(tessellator, x,y,z, indexInTexture, previousImage);
                        }

                        case ToolTip.ITEM_ARRAY -> {
                            if((i & 1) == 1)break;
                            int indexInTexture = toolTip.tooltip.get(k);
                            renderItemModelForTooltip(tessellator,x,y,z, indexInTexture, previousImage);
                        }

                        case ToolTip.MOUSE_ICON_ATLAS -> {
                            size = 75f;
                            int indexInTexture = toolTip.tooltip.get(k);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y - size/2f, z, 3, Assets.mouseIconAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y + size/2f, z, 1, Assets.mouseIconAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x + size/2f, y + size/2f, z, 2, Assets.mouseIconAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addVertexTooltipAtlas(color, x - size/2f, y - size/2f, z, 0, Assets.mouseIconAtlas.textures.get(indexInTexture), 0, previousImage);
                            tessellator.addElementsCW();
                        }
                    }



                    continue; //The advancement of the value only needs to be calculated once
                }


                x += spacing;
            }
        }

        //draw
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawToolTip();
        GL46.glDisable(GL46.GL_BLEND);
    }


    //In general if the current atlas being drawn from is different from the previous atlas advance the value by 25, this is the same spacing as a space in the font renderer
    //If the current and previous atlas are the same advance the value by 12.5, this ensures that text is right next to each other
    //Do not advance the value if the current atlas is the text box atlas
    private static float calculateToolTipLength(ToolTip toolTip){
        float value = 0;
        final float noSpacing = 0f;
        final float normalSpacing = 0.34f * 50;
        final float doubleSpacing = normalSpacing * 4;
        float spacing;

        int previousImage = -1; //Initialized to an invalid value
        for(int i = 0; i < toolTip.tooltip.size(); i++){
            if(i == 0){
                previousImage = toolTip.tooltip.get(i);
                continue; //Skip the first index, there's nothing before it, assign variable
            }


            if((i & 1) == 0){ //Even index, therefore it's the image integer
                if(previousImage != toolTip.tooltip.get(i) && toolTip.tooltip.get(i) != ToolTip.TEXT_BOX_ATLAS){ //If the previousImage does not match the current one use double spacing, otherwise use normal spacing
                    spacing = doubleSpacing;
                    if(previousImage == ToolTip.TEXT_BOX_ATLAS){
                        spacing = normalSpacing;
                    }

                    if(previousImage == ToolTip.TEXT_BOX_ATLAS && (toolTip.tooltip.get(i - 1) == MouseAndKeyIconTextureList.FULL_BOUND_BOX || toolTip.tooltip.get(i - 1) == MouseAndKeyIconTextureList.BOUND_BOX_RIGHT)){
                        spacing = doubleSpacing;
                    }

                } else if(toolTip.tooltip.get(i) != ToolTip.TEXT_BOX_ATLAS) { //use normal spacing unless it's using the textbox atlas, this will not advance the value since it draws over chars
                    spacing = normalSpacing;
                } else { //Executes if the image is the text box atlas
                    spacing = noSpacing;
                }
                previousImage = toolTip.tooltip.get(i); //Assign variable
            } else {
                continue; //The advancement of the value only needs to be calculated once
            }


            value += spacing;
        }

        return value;
    }

    private static void renderBlockModelForTooltip(RenderEngine.Tessellator tessellator, float x, float y, float z, int blockID, int imageID){
        ModelLoader model = Block.list[blockID].getBlockModel(0,0,0, CosmicEvolution.instance.save.activeWorld).copyModel();
        model.translateModel(-0.5f, 0, -0.5f);
        if(Block.list[blockID] instanceof BlockItemStone  || blockID == Block.itemStick.ID){
            model.translateModel(0.5f, 0, 0.5f);
            model.scaleModel(2f);
        }
        if(blockID == Block.itemClay.ID){
            model.scaleModel(2f);
            model.translateModel(0, 0.25f, 0);
        }
        ModelFace[] faces;
        float textureID;
        Vector3f vertex1;
        Vector3f vertex2;
        Vector3f vertex3;
        Vector3f vertex4;
        Vector3f position = new Vector3f(x, y - 16,-70);
        int red = 255;
        int green = 255;
        int blue = 255;
        for(int face = 0; face < 6; face++){
            faces = model.getModelFaceOfType(face);
            for(int i = 0; i < faces.length; i++){
                if(faces[i] == null)continue;
                textureID = Block.list[blockID].getBlockTexture((short) blockID, 0, 0, 0, face);
                vertex1 = new Vector3f(faces[i].vertices[0].x, faces[i].vertices[0].y, faces[i].vertices[0].z).mul(38).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                vertex2 = new Vector3f(faces[i].vertices[1].x, faces[i].vertices[1].y, faces[i].vertices[1].z).mul(38).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                vertex3 = new Vector3f(faces[i].vertices[2].x, faces[i].vertices[2].y, faces[i].vertices[2].z).mul(38).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);
                vertex4 = new Vector3f(faces[i].vertices[3].x, faces[i].vertices[3].y, faces[i].vertices[3].z).mul(38).rotateY((float)(0.25 * Math.PI)).rotateX((float)(0.20 * Math.PI)).add(position);

                tessellator.addVertexTooltipArray(((red << 16) | (green << 8) | blue), vertex1.x, vertex1.y, vertex1.z, faces[i].UVs[0][0], faces[i].UVs[0][1], textureID, imageID);
                tessellator.addVertexTooltipArray(((red << 16) | (green << 8) | blue), vertex2.x, vertex2.y, vertex2.z, faces[i].UVs[1][0], faces[i].UVs[1][1], textureID, imageID);
                tessellator.addVertexTooltipArray(((red << 16) | (green << 8) | blue), vertex3.x, vertex3.y, vertex3.z, faces[i].UVs[2][0], faces[i].UVs[2][1], textureID, imageID);
                tessellator.addVertexTooltipArray(((red << 16) | (green << 8) | blue), vertex4.x, vertex4.y, vertex4.z, faces[i].UVs[3][0], faces[i].UVs[3][1], textureID, imageID);
                tessellator.addElementsCCW();

                red -= 10;
                green -= 10;
                blue -= 10;
            }
        }
    }


    private static void renderItemModelForTooltip(RenderEngine.Tessellator tessellator, float x, float y, float z, int itemID, int imageID){
        Item item = Item.list[itemID];
        ModelLoader model = item.getItemModel(Item.NULL_ITEM_METADATA).copyModel();
        model.scaleModel(78f);
        model.rotateModel(45, 0, 1, 0);
        model.rotateModel(36, 1, 0, 0);

        if(item instanceof ItemSeed){
            model.scaleModel(2f);
        }

        if(item instanceof ItemSpear){
            model.scaleModel(0.75f);
        }

        Vector3f position = new Vector3f(x, y, -70);
        model.translateModel(position.x, position.y, position.z);
        ModelFace face;
        float textureID;

        int colorVal = 255;

        int colorRGB = 0;

        int colorTop = ((colorVal) << 16) | ((colorVal) << 8) | colorVal;
        int colorBottom = ((colorVal - 10) << 16) | ((colorVal - 10) << 8) | colorVal - 10;
        int colorNorth = ((colorVal - 20) << 16) | ((colorVal - 20) << 8) | colorVal - 20;
        int colorSouth = ((colorVal - 30) << 16) | ((colorVal - 30) << 8) | colorVal - 30;
        int colorEast = ((colorVal - 40) << 16) | ((colorVal - 40) << 8) | colorVal - 40;
        int colorWest = ((colorVal - 50) << 16) | ((colorVal - 50) << 8) | colorVal - 50;

        for (int i = 0; i < model.modelFaces.length; i++) {
            face = model.modelFaces[i];
            if (face == null) continue;
            textureID = face.texture;

            switch (face.faceType){
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

            tessellator.addVertexTooltipArray(colorRGB, face.vertices[0].x, face.vertices[0].y, face.vertices[0].z,face.UVs[0][0], face.UVs[0][1],textureID, imageID);
            tessellator.addVertexTooltipArray(colorRGB, face.vertices[1].x, face.vertices[1].y, face.vertices[1].z,face.UVs[1][0], face.UVs[1][1],textureID, imageID);
            tessellator.addVertexTooltipArray(colorRGB, face.vertices[2].x, face.vertices[2].y, face.vertices[2].z,face.UVs[2][0], face.UVs[2][1],textureID, imageID);
            tessellator.addVertexTooltipArray(colorRGB, face.vertices[3].x, face.vertices[3].y, face.vertices[3].z,face.UVs[3][0], face.UVs[3][1],textureID, imageID);
            tessellator.addElementsCCW();
        }
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
            tessellator.vertexBuffer.put(blockFace.UVs[0][0]);
            tessellator.vertexBuffer.put(blockFace.UVs[0][1]);
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
            tessellator.vertexBuffer.put(blockFace.UVs[1][0]);
            tessellator.vertexBuffer.put(blockFace.UVs[1][1]);
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
            tessellator.vertexBuffer.put(blockFace.UVs[2][0]);
            tessellator.vertexBuffer.put(blockFace.UVs[2][1]);
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
            tessellator.vertexBuffer.put(blockFace.UVs[3][0]);
            tessellator.vertexBuffer.put(blockFace.UVs[3][1]);
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



}
