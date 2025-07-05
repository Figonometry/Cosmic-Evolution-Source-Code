package spacegame.gui;

import spacegame.core.SpaceGame;

public abstract class GuiCrafting extends Gui {
    public GuiCrafting(SpaceGame spaceGame) {
        super(spaceGame);
    }

    public abstract CraftingMaterial getHoveredCraftingMaterial();
}
