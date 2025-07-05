package spacegame.gui;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.render.Shader;
import spacegame.render.Tessellator;
import spacegame.render.TextureLoader;

public final class GuiDeathScreen extends Gui {
    public TextureLoader background;
    public TextureLoader title;
    public Button respawn;

    public GuiDeathScreen(SpaceGame spaceGame) {
        super(spaceGame);
        this.respawn = new Button(EnumButtonEffects.RESPAWN.name(), 512, 64, 0, 0, this, this.sg);
    }

    @Override
    public void loadTextures() {
        this.title = new TextureLoader("src/spacegame/assets/textures/gui/guiInGame/deathScreen.png", 770, 144);
        this.background = new TextureLoader("src/spacegame/assets/textures/gui/transparentBackground.png", 32, 32);
    }

    @Override
    public void deleteTextures() {
        GL46.glDeleteTextures(this.title.texID);
        GL46.glDeleteTextures(this.background.texID);
        this.title = null;
        this.background = null;
    }

    @Override
    public void drawGui() {
        Tessellator tessellator = Tessellator.instance;
        tessellator.toggleOrtho();
        GLFW.glfwSetInputMode(this.sg.window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);

        int backgroundWidth = SpaceGame.width;
        int backgroundHeight = SpaceGame.height;
        int backgroundX = 0;
        int backgroundY = 0;
        int backgroundZ = -100;
        tessellator.addVertex2DTexture(4128768, backgroundX - backgroundWidth / 2, backgroundY - backgroundHeight / 2, backgroundZ, 3);
        tessellator.addVertex2DTexture(4128768, backgroundX + backgroundWidth / 2, backgroundY + backgroundHeight / 2, backgroundZ, 1);
        tessellator.addVertex2DTexture(4128768, backgroundX - backgroundWidth / 2, backgroundY + backgroundHeight / 2, backgroundZ, 2);
        tessellator.addVertex2DTexture(4128768, backgroundX + backgroundWidth / 2, backgroundY - backgroundHeight / 2, backgroundZ, 0);
        tessellator.addElements();
        GL46.glEnable(GL46.GL_BLEND);
        GL46.glBlendFunc(GL46.GL_ONE, GL46.GL_ONE_MINUS_SRC_ALPHA);
        tessellator.drawTexture2D(this.background.texID, Shader.screen2DTexture, SpaceGame.camera);
        GL46.glDisable(GL46.GL_BLEND);

        int titleWidth = 770;
        int titleHeight = 144;
        int titleX = 0;
        int titleY = 400;
        int titleZ = -50;
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth / 2, titleY - titleHeight / 2, titleZ, 3);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth / 2, titleY + titleHeight / 2, titleZ, 1);
        tessellator.addVertex2DTexture(16777215, titleX - titleWidth / 2, titleY + titleHeight / 2, titleZ, 2);
        tessellator.addVertex2DTexture(16777215, titleX + titleWidth / 2, titleY - titleHeight / 2, titleZ, 0);
        tessellator.addElements();
        tessellator.drawTexture2D(this.title.texID, Shader.screen2DTexture, SpaceGame.camera);

        tessellator.toggleOrtho();

        this.respawn.renderButton();

        GuiInGame.renderText();
    }

    @Override
    public Button getActiveButton() {
        if (this.respawn.isMouseHoveredOver() && this.respawn.active) {
            return this.respawn;
        }
        return null;
    }

}
