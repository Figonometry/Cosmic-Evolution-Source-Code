package spacegame.gui;

import spacegame.core.CosmicEvolution;

public abstract class GuiCrafting extends Gui {
    public GuiCrafting(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
    }

    public abstract CraftingMaterial getHoveredCraftingMaterial();

    public abstract RecipeSelector getSelectedRecipeSelector();
}
