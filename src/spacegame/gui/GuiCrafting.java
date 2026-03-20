package spacegame.gui;

import spacegame.core.CosmicEvolution;

public abstract class GuiCrafting extends Gui {
    public GuiCrafting(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
    }

    public abstract RecipeSelector getSelectedRecipeSelector();

    public abstract void handleLeftClick();

}
