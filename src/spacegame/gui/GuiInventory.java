package spacegame.gui;

import org.lwjgl.opengl.GL46;
import spacegame.core.SpaceGame;
import spacegame.item.Inventory;
import spacegame.item.ItemStack;
import spacegame.render.TextureLoader;

public abstract class GuiInventory extends Gui {
    public Inventory associatedInventory;
    public static TextureLoader fillableColor;
    public GuiInventory(SpaceGame spaceGame) {
        super(spaceGame);
    }

    public void loadTexture(){
        fillableColor = new TextureLoader("src/spacegame/assets/textures/gui/fillableColor.png", 32, 32);
    }

    public void unloadTexture(){
        GL46.glDeleteTextures(fillableColor.texID);
        fillableColor = null;
    }

    public abstract ItemStack getHoveredItemStack();

}
