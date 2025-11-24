package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.world.Chunk;

public final class GuiLightFire extends GuiAction {
    public int campFire;
    public int stone1Texture;
    public int stone2Texture;
    public int transparentBackground;
    public MoveableObject stone1;
    public MoveableObject stone2;
    public int x;
    public int y;
    public int z;

    public GuiLightFire(CosmicEvolution cosmicEvolution, int x, int y, int z) {
        super(cosmicEvolution);
        this.stone1 = new MoveableObject(-128, -200, 96, 96);
        this.stone2 = new MoveableObject(128, -200, 96, 96);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public MoveableObject getHoveredObject() {
       if(this.stone1.isMouseHoveredOver()){
           return this.stone1;
       } else if(this.stone2.isMouseHoveredOver()){
           return this.stone2;
       }
       return null;
    }

    @Override
    public void loadTextures() {
        this.campFire = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInGame/campFireLighting.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.stone1Texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/stone1.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.stone2Texture = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/guiInventory/stone2.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        this.transparentBackground = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
    }

    @Override
    public void deleteTextures() {
        CosmicEvolution.instance.renderEngine.deleteTexture(this.campFire);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.stone1Texture);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.stone2Texture);
        CosmicEvolution.instance.renderEngine.deleteTexture(this.transparentBackground);
    }

    @Override
    public void drawGui() {
        GuiInGame.renderGuiFromOtherGuis();
        GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        float x = 0;
        float y = 0;
        float z = -100;
        float backgroundWidth = 1920;
        float backgroundHeight = 1017;
        tessellator.addVertex2DTexture(0, x - backgroundWidth/2, y - backgroundHeight/2, z, 3);
        tessellator.addVertex2DTexture(0, x + backgroundWidth/2, y + backgroundHeight/2, z, 1);
        tessellator.addVertex2DTexture(0, x - backgroundWidth/2, y + backgroundHeight/2, z, 2);
        tessellator.addVertex2DTexture(0, x + backgroundWidth/2, y - backgroundHeight/2, z, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.transparentBackground, Shader.screen2DTexture, CosmicEvolution.camera);

        backgroundWidth = 512;
        backgroundHeight = 512;
        z += 10;
        tessellator.addVertex2DTexture(16777215, x - backgroundWidth/2, y - backgroundHeight/2, z, 3);
        tessellator.addVertex2DTexture(16777215, x + backgroundWidth/2, y + backgroundHeight/2, z, 1);
        tessellator.addVertex2DTexture(16777215, x - backgroundWidth/2, y + backgroundHeight/2, z, 2);
        tessellator.addVertex2DTexture(16777215, x + backgroundWidth/2, y - backgroundHeight/2, z, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.campFire, Shader.screen2DTexture, CosmicEvolution.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();

        this.stone1.render(this.stone1Texture);
        this.stone2.render(this.stone2Texture);
        this.checkFireLighting();
    }

    private void checkFireLighting() {
        float x = (float) (MouseListener.instance.xPos - CosmicEvolution.width / 2D);
        float y = (float) ((MouseListener.instance.yPos - CosmicEvolution.height / 2D) * -1);
        if (this.ce.currentlySelectedMoveableObject != null) {
            if (this.ce.currentlySelectedMoveableObject.equals(this.stone1)) {
                if (this.stone2.isMouseHoveredOver() && this.ce.currentlySelectedMoveableObject.pickedUp && Math.abs(x) <= 256 && Math.abs(y) <= 256) {
                   this.ce.save.activeWorld.setBlockWithNotify(this.x, this.y, this.z, Block.campfireLit.ID, false);
                   this.ce.save.activeWorld.findChunkFromChunkCoordinates(this.x >> 5, this.y >> 5, this.z >> 5).addTickableBlockToArray((short) Chunk.getBlockIndexFromCoordinates(this.x,this.y,this.z));
                   GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                   this.ce.setNewGui(new GuiInGame(this.ce));
                }
            } else if (this.ce.currentlySelectedMoveableObject.equals(this.stone2)) {
                if (this.stone1.isMouseHoveredOver() && this.ce.currentlySelectedMoveableObject.pickedUp && Math.abs(x) <= 256 && Math.abs(y) <= 256) {
                    this.ce.save.activeWorld.setBlockWithNotify(this.x, this.y, this.z, Block.campfireLit.ID, false);
                    this.ce.save.activeWorld.findChunkFromChunkCoordinates(this.x >> 5, this.y >> 5, this.z >> 5).addTickableBlockToArray((short) Chunk.getBlockIndexFromCoordinates(this.x,this.y,this.z));
                    GLFW.glfwSetInputMode(this.ce.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                    this.ce.setNewGui(new GuiInGame(this.ce));
                }
            }
        }
    }

    @Override
    public Button getActiveButton() {
        return null;
    }
}
