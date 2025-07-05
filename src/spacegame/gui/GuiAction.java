package spacegame.gui;

import spacegame.core.SpaceGame;

public abstract class GuiAction extends Gui {
    public GuiAction(SpaceGame spaceGame) {
        super(spaceGame);
    }

    public abstract MoveableObject getHoveredObject();
}
