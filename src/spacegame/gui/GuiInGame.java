package spacegame.gui;


import org.joml.Matrix3f;
import org.joml.Matrix4d;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.GameSettings;
import spacegame.core.MathUtil;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.*;
import spacegame.world.Chunk;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public final class GuiInGame extends Gui {
    public int vignette;
    public int water;
    public int hotbar;
    public static int outline;
    public static int blockBreaking;
    public static TextureAtlas blockBreakingAtlas;
    public int transparentBackground;
    public int fillableColorWithShadedBottom;
    public static int fillableColor;

    public GuiInGame(SpaceGame sg) {
        super(sg);
        this.sg = sg;
    }

    @Override
    public void loadTextures() {
        this.vignette = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/vignette.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.water = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/waterOverlay.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        outline = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/outline.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        blockBreaking = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/blockBreaking.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        blockBreakingAtlas = SpaceGame.instance.renderEngine.createTextureAtlas(96,96, 32, 32, 9, 0);
        this.hotbar = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/hotbarSlot.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.transparentBackground = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        this.fillableColorWithShadedBottom = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColorWithShadedBottom.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        fillableColor = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    @Override
    public void deleteTextures() {
        SpaceGame.instance.renderEngine.deleteTexture(this.vignette);
        SpaceGame.instance.renderEngine.deleteTexture(this.water);
        SpaceGame.instance.renderEngine.deleteTexture(outline);
        SpaceGame.instance.renderEngine.deleteTexture(blockBreaking);
        SpaceGame.instance.renderEngine.deleteTexture(this.hotbar);
        SpaceGame.instance.renderEngine.deleteTexture(this.transparentBackground);
        SpaceGame.instance.renderEngine.deleteTexture(this.fillableColorWithShadedBottom);
        blockBreakingAtlas = null;
    }

    public static void renderText(){
        int leftSide = -970;
        FontRenderer fontRenderer = FontRenderer.instance;
        if(GameSettings.showFPS) {
            fontRenderer.drawString(SpaceGame.instance.title + " (" + SpaceGame.instance.fps * -1 + " FPS)", leftSide, 460,-15, 16777215, 50);
        } else {
            fontRenderer.drawString(SpaceGame.instance.title, leftSide, 460,-15, 16777215, 50);
        }
        int playerX = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x);
        int playerY = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y);
        int playerZ = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z);

        if (SpaceGame.DEBUG_MODE) {
            fontRenderer.drawString("X: " + SpaceGame.instance.save.thePlayer.x, leftSide, 430,-15, 16777215, 50);
            fontRenderer.drawString("Y: " + (SpaceGame.instance.save.thePlayer.y), leftSide, 400,-15, 16777215, 50);
            fontRenderer.drawString("Z: " + SpaceGame.instance.save.thePlayer.z, leftSide, 370,-15, 16777215, 50);
            fontRenderer.drawString("Pitch: " + SpaceGame.instance.save.thePlayer.pitch, leftSide, 340,-15, 16777215, 50);
            fontRenderer.drawString("Yaw: " + SpaceGame.instance.save.thePlayer.yaw, leftSide, 310,-15, 16777215, 50);
            fontRenderer.drawString("Chunks Loaded " + SpaceGame.instance.save.activeWorld.chunkController.numberOfLoadedChunks(), leftSide, 280,-15, 16777215, 50);
            fontRenderer.drawString("Block Light Level: " + SpaceGame.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ), leftSide, 250,-15, 16777215, 50);
            fontRenderer.drawString("Regions Loaded: " + SpaceGame.instance.save.activeWorld.chunkController.numberOfLoadedRegions(), leftSide, 220,-15, 16777215, 50);
            fontRenderer.drawString("Draw Calls: " +  SpaceGame.instance.save.activeWorld.chunkController.drawCalls, leftSide, 190,-15, 16777215, 50);
            fontRenderer.drawString("Thread Count: " + Thread.activeCount(), leftSide, 160,-15, 16777215, 50);
            fontRenderer.drawString("Thread Queue Size: " + SpaceGame.instance.save.activeWorld.chunkController.threadQueue.size(), leftSide, 130,-15, 16777215, 50);
            fontRenderer.drawString("Sky Light Level: " + SpaceGame.instance.save.activeWorld.getBlockSkyLightValue(playerX, playerY, playerZ), leftSide, 100,-15, 16777215, 50);
            fontRenderer.drawString("Temperature: " + SpaceGame.instance.save.activeWorld.getDisplayTemperature(playerX, playerY, playerZ) + "F", leftSide, 70,-15, 16777215, 50);
            fontRenderer.drawString("Rainfall: " + SpaceGame.instance.save.activeWorld.getRainfall(playerX, playerZ), leftSide, 40,-15, 16777215, 50);
            fontRenderer.drawString("Time: " + SpaceGame.instance.save.time, leftSide, 10, -15, 16777215, 50);
            fontRenderer.drawString("Entities: " + SpaceGame.instance.save.activeWorld.chunkController.numLoadedEntities + " / " + SpaceGame.instance.save.activeWorld.chunkController.entityCap, leftSide,-20, -15, 16777215, 50);
        } else {
            fontRenderer.drawString("Temperature: " + SpaceGame.instance.save.activeWorld.getDisplayTemperature(playerX, playerY, playerZ) + "F", leftSide, 400,-15, 16777215, 50);
            fontRenderer.drawString("Rainfall: " + SpaceGame.instance.save.activeWorld.getRainfall(playerX, playerZ), leftSide, 370,-15, 16777215, 50);
        }
    }


    @Override
    public void drawGui() {
        renderText();
        this.renderCursor();

        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;

        short blockPlayerHeadIsIn = this.sg.save.activeWorld.getBlockID(MathUtil.floorDouble(this.sg.save.thePlayer.x), MathUtil.floorDouble(this.sg.save.thePlayer.y+  this.sg.save.thePlayer.height/2), MathUtil.floorDouble(this.sg.save.thePlayer.z));
        if(blockPlayerHeadIsIn == Block.water.ID){
            GL46.glEnable(GL46.GL_BLEND);
            GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
            tessellator.toggleOrtho();
            tessellator.addVertex2DTexture(8355711, (float) -SpaceGame.width /2, (float) -SpaceGame.height /2, -900, 3);
            tessellator.addVertex2DTexture(8355711, (float) SpaceGame.width /2, (float) SpaceGame.height /2, -900, 1);
            tessellator.addVertex2DTexture(8355711, (float) -SpaceGame.width /2, (float) SpaceGame.height /2, -900, 2);
            tessellator.addVertex2DTexture(8355711, (float) SpaceGame.width /2, (float) -SpaceGame.height /2, -900, 0);
            tessellator.addElements();
            tessellator.drawTexture2D(this.water, Shader.screen2DTexture, SpaceGame.camera);
            tessellator.toggleOrtho();
            GL46.glDisable(GL46.GL_BLEND);
            this.renderAirBar();
        } else if (blockPlayerHeadIsIn != Block.air.ID && blockPlayerHeadIsIn != Block.torchStandard.ID && blockPlayerHeadIsIn != Block.torchNorth.ID && blockPlayerHeadIsIn != Block.torchSouth.ID && blockPlayerHeadIsIn != Block.torchEast.ID && blockPlayerHeadIsIn != Block.torchWest.ID) {
            int textureID = Block.list[blockPlayerHeadIsIn].textureID;
            tessellator.toggleOrtho();
            tessellator.addVertexTextureArray(4144959, (float) -SpaceGame.width /2, (float) -SpaceGame.height /2, -900, 3, textureID, 2);
            tessellator.addVertexTextureArray(4144959, (float) SpaceGame.width /2, (float) SpaceGame.height /2, -900, 1, textureID, 2);
            tessellator.addVertexTextureArray(4144959, (float) -SpaceGame.width /2, (float) SpaceGame.height /2, -900, 2,textureID, 2);
            tessellator.addVertexTextureArray(4144959, (float) SpaceGame.width /2, (float) -SpaceGame.height /2, -900, 0,textureID, 2);
            tessellator.addElements();
            tessellator.drawVertexArray(Assets.blockTextureArray, Shader.screenTextureArray, SpaceGame.camera);
            tessellator.toggleOrtho();
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
        tessellator.drawTexture2D(this.vignette, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDepthMask(true);
        tessellator.toggleOrtho();
        GL46.glDisable(GL46.GL_BLEND);

        this.renderHeldItem();
        this.renderHotbar();
        this.renderHealthbar();
    }

    public void renderHotbar(){
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
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int size = 96;
        if(EntityPlayer.selectedInventorySlot == 0){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 1){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 2){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 3){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 4){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 5){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 6){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 7){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        size = 96;
        x += size;
        if(EntityPlayer.selectedInventorySlot == 8){
            size = 102;
        }
        tessellator.addVertex2DTexture(16777215, x, y, -80, 3);
        tessellator.addVertex2DTexture(16777215, x + size, y + size, -80, 1);
        tessellator.addVertex2DTexture(16777215, x, y + size, -80, 2);
        tessellator.addVertex2DTexture(16777215, x + size, y, -80, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.hotbar, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();

        x = -428;
        size = 96;
        for(int i = 0; i < 9; i++) {
            if (i == EntityPlayer.selectedInventorySlot) {
                SpaceGame.instance.save.thePlayer.inventory.itemStacks[i].adjustStackPosition(x + 46, y + 46);
                SpaceGame.instance.save.thePlayer.inventory.itemStacks[i].renderItemStackOnHotbar();
                SpaceGame.instance.save.thePlayer.inventory.itemStacks[i].resetStackPosition();
            } else {
                SpaceGame.instance.save.thePlayer.inventory.itemStacks[i].adjustStackPosition(x + 43, y + 43);
                SpaceGame.instance.save.thePlayer.inventory.itemStacks[i].renderItemStackOnHotbar();
                SpaceGame.instance.save.thePlayer.inventory.itemStacks[i].resetStackPosition();
            }

            x += size;
        }
    }

    private void renderHealthbar(){ //Full red 16711680, half red 8323072
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        int x = -428;
        int y = -395;
        tessellator.addVertex2DTexture(4128768, x, y, -90, 3);
        tessellator.addVertex2DTexture(4128768, x + 864, y + 16, -90, 1);
        tessellator.addVertex2DTexture(4128768, x, y + 16, -90, 2);
        tessellator.addVertex2DTexture(4128768, x + 864, y, -90, 0);
        tessellator.addElements();

        float progress = (int) (864 * (SpaceGame.instance.save.thePlayer.health / SpaceGame.instance.save.thePlayer.maxHealth));
        tessellator.addVertex2DTexture(16711680, x, y, -89, 3);
        tessellator.addVertex2DTexture(16711680, x + progress, y + 16, -89, 1);
        tessellator.addVertex2DTexture(16711680, x, y + 16, -89, 2);
        tessellator.addVertex2DTexture(16711680, x + progress, y, -89, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.fillableColorWithShadedBottom, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
    }

    private void renderAirBar(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        int x = -428;
        int y = -379;
        tessellator.addVertex2DTexture(9535, x, y, -90, 3);
        tessellator.addVertex2DTexture(9535, x + 864, y + 16, -90, 1);
        tessellator.addVertex2DTexture(9535, x, y + 16, -90, 2);
        tessellator.addVertex2DTexture(9535, x + 864, y, -90, 0);
        tessellator.addElements();

        float progress = (int) (864 * ((300 - SpaceGame.instance.save.thePlayer.drowningTimer) / 300f));
        tessellator.addVertex2DTexture(38143, x, y, -89, 3);
        tessellator.addVertex2DTexture(38143, x + progress, y + 16, -89, 1);
        tessellator.addVertex2DTexture(38143, x, y + 16, -89, 2);
        tessellator.addVertex2DTexture(38143, x + progress, y, -89, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.fillableColorWithShadedBottom, Shader.screen2DTexture, SpaceGame.camera);
        tessellator.toggleOrtho();
    }


    public void renderHeldItem() {
        final short heldBlock = SpaceGame.instance.save.thePlayer.getHeldBlock();
        if(SpaceGame.instance.save.thePlayer.isHoldingBlock()) {
            if (heldBlock != Block.air.ID) {
                float blockID;
                float x = 2f;
                float y = -2.5f;
                float z = -3f;
                if(GameSettings.viewBob) {
                    x -= 0.5f * ((MathUtil.sin((float) (((this.sg.save.thePlayer.viewBobTimer / 60f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    y -= 0.25f * ((MathUtil.sin((float) (((this.sg.save.thePlayer.viewBobTimer / 60f) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
                }
                z -= 1f * ((MathUtil.sin((float) (((SpaceGame.instance.save.thePlayer.swingTimer / 15f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                Vector3f position = new Vector3f(x,y,z);
                Matrix3f rotationMatrix = new Matrix3f();
                rotationMatrix.rotateY((float) (0.25 * Math.PI));
                double sine = (MathUtil.sin((float) ((((double) SpaceGame.instance.save.thePlayer.swingTimer / 15) * Math.PI * 2) - (0.5 * Math.PI))) * 0.5) + 0.5f;
                rotationMatrix.rotateLocalX((float) ((float) -(0.25 * Math.PI) * sine));
                Quaternionf rotation = rotationMatrix.getUnnormalizedRotation(new Quaternionf());
                int playerX = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x);
                int playerY = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y);
                int playerZ = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z);
                float blockLight = getLightValueFromMap(SpaceGame.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ));
                float lightLevelFloat = SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.baseLight > blockLight ? SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.baseLight : blockLight;
                lightLevelFloat -=  0.1 * (MathUtil.sin(SpaceGame.instance.save.thePlayer.yaw / 45) + 1);
                lightLevelFloat -=  0.1 * (MathUtil.sin(SpaceGame.instance.save.thePlayer.pitch / 45) + 1);
                if(lightLevelFloat < 0.1){
                    lightLevelFloat = 0.1f;
                }
                Color color = new Color(lightLevelFloat, lightLevelFloat, lightLevelFloat, 0);
                int colorRGB = color.getRGB();

                RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
                Vector3f vertex1 = new Vector3f(1, 1, 0).rotate(rotation).add(position);
                Vector3f vertex2 = new Vector3f(0, 1, 1).rotate(rotation).add(position);
                Vector3f vertex3 = new Vector3f(1, 1, 1).rotate(rotation).add(position);
                Vector3f vertex4 = new Vector3f(0, 1, 0).rotate(rotation).add(position);
                blockID = RenderBlocks.getBlockTextureID(heldBlock, RenderBlocks.TOP_FACE);
                tessellator.addVertexTextureArray(colorRGB, vertex1.x, vertex1.y, vertex1.z, 3, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex2.x, vertex2.y, vertex2.z, 1, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex3.x, vertex3.y, vertex3.z, 2, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex4.x, vertex4.y, vertex4.z, 0, blockID, 0);
                tessellator.addElements();

                color = color.darker();
                colorRGB = color.getRGB();
                vertex1 = new Vector3f(0, 0, 0).rotate(rotation).add(position);
                vertex2 = new Vector3f(1, 0, 1).rotate(rotation).add(position);
                vertex3 = new Vector3f(0, 0, 1).rotate(rotation).add(position);
                vertex4 = new Vector3f(1, 0, 0).rotate(rotation).add(position);
                blockID = RenderBlocks.getBlockTextureID(heldBlock, RenderBlocks.BOTTOM_FACE);
                tessellator.addVertexTextureArray(colorRGB, vertex1.x, vertex1.y, vertex1.z, 3, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex2.x, vertex2.y, vertex2.z, 1, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex3.x, vertex3.y, vertex3.z, 2, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex4.x, vertex4.y, vertex4.z, 0, blockID, 0);
                tessellator.addElements();

                color = color.darker();
                colorRGB = color.getRGB();
                vertex1 = new Vector3f(0, 0, 0).rotate(rotation).add(position);
                vertex2 = new Vector3f(0, 1, 1).rotate(rotation).add(position);
                vertex3 = new Vector3f(0, 1, 0).rotate(rotation).add(position);
                vertex4 = new Vector3f(0, 0, 1).rotate(rotation).add(position);
                blockID = RenderBlocks.getBlockTextureID(heldBlock, RenderBlocks.NORTH_FACE);
                tessellator.addVertexTextureArray(colorRGB, vertex1.x, vertex1.y, vertex1.z, 3, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex2.x, vertex2.y, vertex2.z, 1, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex3.x, vertex3.y, vertex3.z, 2, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex4.x, vertex4.y, vertex4.z, 0, blockID, 0);
                tessellator.addElements();

                color = color.darker();
                colorRGB = color.getRGB();
                vertex1 = new Vector3f(1, 0, 1).rotate(rotation).add(position);
                vertex2 = new Vector3f(1, 1, 0).rotate(rotation).add(position);
                vertex3 = new Vector3f(1, 1, 1).rotate(rotation).add(position);
                vertex4 = new Vector3f(1, 0, 0).rotate(rotation).add(position);
                blockID = RenderBlocks.getBlockTextureID(heldBlock, RenderBlocks.SOUTH_FACE);
                tessellator.addVertexTextureArray(colorRGB, vertex1.x, vertex1.y, vertex1.z, 3, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex2.x, vertex2.y, vertex2.z, 1, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex3.x, vertex3.y, vertex3.z, 2, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex4.x, vertex4.y, vertex4.z, 0, blockID, 0);
                tessellator.addElements();

                color = color.darker();
                colorRGB = color.getRGB();
                vertex1 = new Vector3f(1, 0, 0).rotate(rotation).add(position);
                vertex2 = new Vector3f(0, 1, 0).rotate(rotation).add(position);
                vertex3 = new Vector3f(1, 1, 0).rotate(rotation).add(position);
                vertex4 = new Vector3f(0, 0, 0).rotate(rotation).add(position);
                blockID = RenderBlocks.getBlockTextureID(heldBlock, RenderBlocks.EAST_FACE);
                tessellator.addVertexTextureArray(colorRGB, vertex1.x, vertex1.y, vertex1.z, 3, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex2.x, vertex2.y, vertex2.z, 1, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex3.x, vertex3.y, vertex3.z, 2, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex4.x, vertex4.y, vertex4.z, 0, blockID, 0);
                tessellator.addElements();

                color = color.darker();
                colorRGB = color.getRGB();
                vertex1 = new Vector3f(0, 0, 1).rotate(rotation).add(position);
                vertex2 = new Vector3f(1, 1, 1).rotate(rotation).add(position);
                vertex3 = new Vector3f(0, 1, 1).rotate(rotation).add(position);
                vertex4 = new Vector3f(1, 0, 1).rotate(rotation).add(position);
                blockID = RenderBlocks.getBlockTextureID(heldBlock, RenderBlocks.WEST_FACE);
                tessellator.addVertexTextureArray(colorRGB, vertex1.x, vertex1.y, vertex1.z, 3, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex2.x, vertex2.y, vertex2.z, 1, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex3.x, vertex3.y, vertex3.z, 2, blockID, 0);
                tessellator.addVertexTextureArray(colorRGB, vertex4.x, vertex4.y, vertex4.z, 0, blockID, 0);
                tessellator.addElements();

                Matrix4d preservedViewMatrix = SpaceGame.camera.viewMatrix.get(new Matrix4d());
                SpaceGame.camera.viewMatrix = new Matrix4d();
                GL46.glEnable(GL46.GL_CULL_FACE);
                GL46.glCullFace(GL46.GL_FRONT);
                tessellator.drawVertexArray(Assets.blockTextureArray, Shader.screenTextureArray, SpaceGame.camera);
                GL46.glDisable(GL46.GL_CULL_FACE);
                SpaceGame.camera.viewMatrix = preservedViewMatrix;
            }
        } else {
            short itemID = SpaceGame.instance.save.thePlayer.getHeldItem();
            if(itemID == -1){return;}
            if(itemID != Item.block.ID){
                RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
                float x = 2f;
                float y = -1f;
                float z = -3f;
                if(GameSettings.viewBob) {
                    x -= 0.125f * ((MathUtil.sin((float) (((this.sg.save.thePlayer.viewBobTimer / 60f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                    y -= 0.0625f * ((MathUtil.sin((float) (((this.sg.save.thePlayer.viewBobTimer / 60f) - 0.125f) * (Math.PI * 4f))) * 0.5) + 0.5f);
                }
                z -= 1f * ((MathUtil.sin((float) (((SpaceGame.instance.save.thePlayer.swingTimer / 15f) + 0.75f) * (Math.PI * 2f))) * 0.5) + 0.5f);
                Vector3f position = new Vector3f(x,y,z);
                Matrix3f rotationMatrix = new Matrix3f();
                rotationMatrix.rotateY((float) -(0.35 * Math.PI));
                double sine = (MathUtil.sin((float) ((((double) SpaceGame.instance.save.thePlayer.swingTimer / 15) * Math.PI * 2) - (0.5 * Math.PI))) * 0.5) + 0.5f;
                rotationMatrix.rotateLocalX((float) ((float) -(0.25 * Math.PI) * sine));
                Quaternionf rotation = rotationMatrix.getUnnormalizedRotation(new Quaternionf());
                int playerX = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x);
                int playerY = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y);
                int playerZ = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z);
                float blockLight = getLightValueFromMap(SpaceGame.instance.save.activeWorld.getBlockLightValue(playerX, playerY, playerZ));
                float lightLevelFloat = SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.baseLight > blockLight ? SpaceGame.instance.save.activeWorld.chunkController.renderWorldScene.baseLight : blockLight;
                lightLevelFloat -=  0.1 * (MathUtil.sin(SpaceGame.instance.save.thePlayer.yaw / 45) + 1);
                lightLevelFloat -=  0.1 * (MathUtil.sin(SpaceGame.instance.save.thePlayer.pitch / 45) + 1);
                if(lightLevelFloat < 0.1f){
                    lightLevelFloat = 0.1f;
                }
                int[] pixels = new int[1024];
                String filepath = "src/spacegame/assets/textures/item/" +
                        RenderEngine.getBlockName(itemID, "src/spacegame/assets/textures/item/") + ".png";
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
                        if(i < pixelColors.length) {
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


                Matrix4d preservedViewMatrix = SpaceGame.camera.viewMatrix.get(new Matrix4d());
                SpaceGame.camera.viewMatrix = new Matrix4d();
                GL46.glEnable(GL46.GL_CULL_FACE);
                GL46.glCullFace(GL46.GL_FRONT);
                tessellator.drawTexture2D(fillableColor, Shader.screen2DTexture, SpaceGame.camera);
                GL46.glDisable(GL46.GL_CULL_FACE);
                SpaceGame.camera.viewMatrix = preservedViewMatrix;
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
        if (SpaceGame.instance.save.thePlayer.breakTimer != 0) {
            RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
            int locationX = Integer.MIN_VALUE;
            int locationY = Integer.MIN_VALUE;
            int locationZ = Integer.MIN_VALUE;
            short block = Short.MIN_VALUE;
            ModelLoader modelLoader;
            if (!SpaceGame.instance.save.activeWorld.paused) {
                double[] rayCast = SpaceGame.camera.rayCast(3);
                final double multiplier = 0.05D;
                final double xDif = (rayCast[0] - SpaceGame.instance.save.thePlayer.x);
                final double yDif = (rayCast[1] - (SpaceGame.instance.save.thePlayer.y + SpaceGame.instance.save.thePlayer.height/2));
                final double zDif = (rayCast[2] - SpaceGame.instance.save.thePlayer.z);

                int blockX = 0;
                int blockY = 0;
                int blockZ = 0;
                for (int loopPass = 0; loopPass < 30; loopPass++) {
                    blockX = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x + xDif * multiplier * loopPass);
                    blockY = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y  + SpaceGame.instance.save.thePlayer.height/2 + yDif * multiplier * loopPass);
                    blockZ = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z + zDif * multiplier * loopPass);

                    Block checkedBlock = Block.list[SpaceGame.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ)];
                    if (isBlockVisible(blockX, blockY, blockZ)) {
                        if (checkedBlock.ID != Block.air.ID && checkedBlock.ID != Block.water.ID) {
                            locationX = blockX;
                            locationY = blockY;
                            locationZ = blockZ;
                            block = SpaceGame.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ);
                            break;
                        }
                    }
                }

                if (locationX != Integer.MIN_VALUE && locationY != Integer.MIN_VALUE && locationZ != Integer.MIN_VALUE && block != Short.MIN_VALUE) {
                    Chunk chunk = SpaceGame.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(locationX >> 5, locationY >> 5, locationZ >> 5);
                    if (chunk != null) {
                        int playerChunkX = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x) >> 5;
                        int playerChunkY = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y) >> 5;
                        int playerChunkZ = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z) >> 5;
                        int xOffset = (chunk.x - playerChunkX) << 5;
                        int yOffset = (chunk.y - playerChunkY) << 5;
                        int zOffset = (chunk.z - playerChunkZ) << 5;
                        Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
                        Shader.worldShader2DTextureWithAtlas.uploadVec3f("chunkOffset", chunkOffset);
                        Shader.worldShader2DTextureWithAtlas.uploadBoolean("useFog", true);
                        int textureID = (int) (((double)SpaceGame.instance.save.thePlayer.breakTimer/(double)Block.list[block].getDynamicBreakTimer()) * 10);
                        if(textureID > 8){
                            textureID = 8;
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


                            renderSpecialFace(tessellator, 16777215, Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ),textureID, modelFace, 0, 0, 0, 0, 0, 0, 0, 0, 3, 1, 2, 0, chunk, blockBreakingAtlas.getTexture(textureID));
                            tessellator.addElements();
                        }
                        GL46.glEnable(GL46.GL_CULL_FACE);
                        GL46.glCullFace(GL46.GL_FRONT);
                        GL46.glEnable(GL46.GL_BLEND);
                        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
                        GL46.glEnable(GL46.GL_ALPHA_TEST);
                        GL46.glAlphaFunc(GL46.GL_GREATER, 0.1F);
                        tessellator.drawTexture2DWithAtlas(blockBreaking, Shader.worldShader2DTextureWithAtlas, SpaceGame.camera);
                        GL46.glDisable(GL46.GL_BLEND);
                        GL46.glDisable(GL46.GL_ALPHA_TEST);
                        GL46.glDisable(GL46.GL_CULL_FACE);
                    }
                }
            }
        }
    }

    public static void renderBlockOutline(){
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        int locationX = Integer.MIN_VALUE;
        int locationY = Integer.MIN_VALUE;
        int locationZ = Integer.MIN_VALUE;
        short block = Short.MIN_VALUE;
        ModelLoader modelLoader;
        if (!SpaceGame.instance.save.activeWorld.paused) {
            double[] rayCast = SpaceGame.camera.rayCast(3);
            final double multiplier = 0.05F;
            final double xDif = (rayCast[0] - SpaceGame.instance.save.thePlayer.x);
            final double yDif = (rayCast[1] - (SpaceGame.instance.save.thePlayer.y + SpaceGame.instance.save.thePlayer.height/2));
            final double zDif = (rayCast[2] - SpaceGame.instance.save.thePlayer.z);

            int blockX = 0;
            int blockY = 0;
            int blockZ = 0;
            for (int loopPass = 0; loopPass < 30; loopPass++) {
                blockX = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x + xDif * multiplier * loopPass);
                blockY = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y  + SpaceGame.instance.save.thePlayer.height/2 + yDif * multiplier * loopPass);
                blockZ = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z + zDif * multiplier * loopPass);

                Block checkedBlock = Block.list[SpaceGame.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ)];
                if (isBlockVisible(blockX, blockY, blockZ)) {
                    if(checkedBlock.ID != Block.air.ID && checkedBlock.ID != Block.water.ID){
                            locationX = blockX;
                            locationY = blockY;
                            locationZ = blockZ;
                            block = SpaceGame.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ);
                            break;
                    }
                }
            }

            if(locationX != Integer.MIN_VALUE && locationY != Integer.MIN_VALUE && locationZ != Integer.MIN_VALUE && block != Short.MIN_VALUE){
                Chunk chunk = SpaceGame.instance.save.activeWorld.chunkController.findChunkFromChunkCoordinates(locationX >> 5, locationY >> 5, locationZ >> 5);
                if(chunk != null) {
                    int playerChunkX = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.x) >> 5;
                    int playerChunkY = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.y) >> 5;
                    int playerChunkZ = MathUtil.floorDouble(SpaceGame.instance.save.thePlayer.z) >> 5;
                    int xOffset = (chunk.x - playerChunkX) << 5;
                    int yOffset = (chunk.y - playerChunkY) << 5;
                    int zOffset = (chunk.z - playerChunkZ) << 5;
                    Vector3f chunkOffset = new Vector3f(xOffset, yOffset, zOffset);
                    Shader.worldShader2DTexture.uploadVec3f("chunkOffset", chunkOffset);
                    Shader.worldShader2DTexture.uploadBoolean("useFog", true);
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



                        renderSpecialFace(tessellator, 16777215, Chunk.getBlockIndexFromCoordinates(locationX, locationY, locationZ), 0, modelFace, 0,0,0,0,0,0,0,0, 3,1,2,0, chunk,null);
                        tessellator.addElements();
                    }

                    GL46.glEnable(GL46.GL_CULL_FACE);
                    GL46.glCullFace(GL46.GL_FRONT);
                    GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_LINE);
                    tessellator.drawTexture2D(outline, Shader.worldShader2DTexture, SpaceGame.camera);
                    GL46.glPolygonMode(GL46.GL_FRONT_AND_BACK, GL46.GL_FILL);
                    GL46.glDisable(GL46.GL_CULL_FACE);

                    Shader.worldShader2DTexture.uploadBoolean("compressTest", false);
                }
            }

        }

    }

    public static boolean isBlockVisible(int blockX, int blockY, int blockZ) {
        boolean exposed = false;
//This does not work if any of the exposed faces are air, this should be AND
        exposed |= !Block.list[SpaceGame.instance.save.activeWorld.getBlockID(blockX + 1, blockY, blockZ)].isSolid;
        exposed |= !Block.list[SpaceGame.instance.save.activeWorld.getBlockID(blockX - 1, blockY, blockZ)].isSolid;
        exposed |= !Block.list[SpaceGame.instance.save.activeWorld.getBlockID(blockX, blockY + 1, blockZ)].isSolid;
        exposed |= !Block.list[SpaceGame.instance.save.activeWorld.getBlockID(blockX, blockY - 1, blockZ)].isSolid;
        exposed |= !Block.list[SpaceGame.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ + 1)].isSolid;
        exposed |= !Block.list[SpaceGame.instance.save.activeWorld.getBlockID(blockX, blockY, blockZ - 1)].isSolid;

        return exposed;
    }

    private static void renderSpecialFace(RenderEngine.Tessellator tessellator, int colorValue, int index, int textureID, ModelFace blockFace, float xSample1, float ySample1, float xSample2, float ySample2, float xSample3, float ySample3, float xSample4, float ySample4, int corner1, int corner2, int corner3, int corner4, Chunk chunk, Texture texture) {
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

            tessellator.vertexBuffer.put(vertex2.x);
            tessellator.vertexBuffer.put(vertex2.y);
            tessellator.vertexBuffer.put(vertex2.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(1);
            tessellator.vertexBuffer.put(0);

            tessellator.vertexBuffer.put(vertex3.x);
            tessellator.vertexBuffer.put(vertex3.y);
            tessellator.vertexBuffer.put(vertex3.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(1);
            tessellator.vertexBuffer.put(1);

            tessellator.vertexBuffer.put(vertex4.x);
            tessellator.vertexBuffer.put(vertex4.y);
            tessellator.vertexBuffer.put(vertex4.z);
            tessellator.vertexBuffer.put(red);
            tessellator.vertexBuffer.put(green);
            tessellator.vertexBuffer.put(blue);
            tessellator.vertexBuffer.put(alpha);
            tessellator.vertexBuffer.put(0);
            tessellator.vertexBuffer.put(0);
        }
    }


    @Override
    public Button getActiveButton() {
        return null;
    }
}
