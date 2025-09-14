package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.block.Block;
import spacegame.core.MouseListener;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;

public final class GuiLightFire extends GuiAction {
    public TextureLoader campFire;
    public TextureLoader stone1Texture;
    public TextureLoader stone2Texture;
    public TextureLoader transparentBackground;
    public MoveableObject stone1;
    public MoveableObject stone2;
    public int x;
    public int y;
    public int z;

    public GuiLightFire(SpaceGame spaceGame, int x, int y, int z) {
        super(spaceGame);
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
        this.campFire = new TextureLoader("src/spacegame/assets/textures/blocks/campFireUnlit.png", 32, 32);
        this.stone1Texture = new TextureLoader("src/spacegame/assets/textures/gui/guiInventory/stone1.png", 32, 32);
        this.stone2Texture = new TextureLoader("src/spacegame/assets/textures/gui/guiInventory/stone2.png", 32, 32);
        this.transparentBackground = new TextureLoader("src/spacegame/assets/textures/gui/transparentBackground.png", 32,32);
    }

    @Override
    public void deleteTextures() {
        GL46.glDeleteTextures(this.campFire.texID);
        GL46.glDeleteTextures(this.stone1Texture.texID);
        GL46.glDeleteTextures(this.stone2Texture.texID);
        GL46.glDeleteTextures(this.transparentBackground.texID);
        this.campFire = null;
        this.stone1Texture = null;
        this.stone2Texture = null;
        this.transparentBackground = null;
    }

    @Override
    public void drawGui() {
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
        Tessellator tessellator = Tessellator.instance;
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
        tessellator.drawTexture2D(this.transparentBackground.texID, Shader.screen2DTexture, SpaceGame.camera);

        backgroundWidth = 512;
        backgroundHeight = 512;
        z += 10;
        tessellator.addVertex2DTexture(16777215, x - backgroundWidth/2, y - backgroundHeight/2, z, 3);
        tessellator.addVertex2DTexture(16777215, x + backgroundWidth/2, y + backgroundHeight/2, z, 1);
        tessellator.addVertex2DTexture(16777215, x - backgroundWidth/2, y + backgroundHeight/2, z, 2);
        tessellator.addVertex2DTexture(16777215, x + backgroundWidth/2, y - backgroundHeight/2, z, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.campFire.texID, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);
        tessellator.toggleOrtho();

        this.stone1.render(this.stone1Texture.texID);
        this.stone2.render(this.stone2Texture.texID);
        this.checkFireLighting();
    }

    private void checkFireLighting() {
        float x = (float) (MouseListener.instance.xPos - SpaceGame.width / 2D);
        float y = (float) ((MouseListener.instance.yPos - SpaceGame.height / 2D) * -1);
        if (this.sg.currentlySelectedMoveableObject != null) {
            if (this.sg.currentlySelectedMoveableObject.equals(this.stone1)) {
                if (this.stone2.isMouseHoveredOver() && this.sg.currentlySelectedMoveableObject.pickedUp && Math.abs(x) <= 256 && Math.abs(y) <= 256) {
                   this.sg.save.activeWorld.setBlockWithNotify(this.x, this.y, this.z, Block.campfireLit.ID);
                   GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                   this.sg.setNewGui(new GuiInGame(this.sg));
                }
            } else if (this.sg.currentlySelectedMoveableObject.equals(this.stone2)) {
                if (this.stone1.isMouseHoveredOver() && this.sg.currentlySelectedMoveableObject.pickedUp && Math.abs(x) <= 256 && Math.abs(y) <= 256) {
                    this.sg.save.activeWorld.setBlockWithNotify(this.x, this.y, this.z, Block.campfireLit.ID);
                    GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
                    this.sg.setNewGui(new GuiInGame(this.sg));
                }
            }
        }
    }

    @Override
    public Button getActiveButton() {
        return null;
    }
}
