package spacegame.gui;

import spacegame.core.CosmicEvolution;

public abstract class GuiAction extends Gui {
    public GuiAction(CosmicEvolution cosmicEvolution) {
        super(cosmicEvolution);
    }

    public abstract MoveableObject getHoveredObject();
}
