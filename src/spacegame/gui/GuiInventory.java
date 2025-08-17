package spacegame.gui;

import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.item.Inventory;
import spacegame.item.ItemStack;
import spacegame.render.RenderEngine;

public abstract class GuiInventory extends Gui {
    public Inventory associatedInventory;
    public static int fillableColor;
    public GuiInventory(SpaceGame spaceGame) {
        super(spaceGame);
    }

    public void loadTexture(){
        fillableColor = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/gui/fillableColor.png", RenderEngine.TEXTURE_TYPE_2D, 0);
    }

    public void unloadTexture(){
        SpaceGame.instance.renderEngine.deleteTexture(fillableColor);
    }

    public abstract ItemStack getHoveredItemStack();

}
