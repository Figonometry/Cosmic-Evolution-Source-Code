package spacegame.gui;

import org.lwjgl.opengl.GL46;
import spacegame.core.MathUtil;
import spacegame.core.SpaceGame;
import spacegame.item.Inventory;
import spacegame.item.ItemStack;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;

public abstract class GuiInventory extends Gui {
    public Inventory associatedInventory;
    public static int fillableColor;
    public static int transparentBackground;
    public GuiInventory(SpaceGame spaceGame) {
        super(spaceGame);
    }

    public void loadTexture(){
        fillableColor = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        transparentBackground = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/transparentBackground.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    public void unloadTexture() {
        SpaceGame.instance.renderEngine.deleteTexture(fillableColor);
        SpaceGame.instance.renderEngine.deleteTexture(transparentBackground);
    }

    public abstract ItemStack getHoveredItemStack();


    public void renderHoveredItemStackName(ItemStack stack){
        if(stack == null)return;
        if(stack.item == null)return;

        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        FontRenderer fontRenderer = FontRenderer.instance;
        tessellator.toggleOrtho();
        String displayedName = stack.item.getDisplayName(stack.metadata);
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
