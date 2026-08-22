package spacegame.gui;

import spacegame.core.CosmicEvolution;

public abstract class Gui {
    public CosmicEvolution ce;
    public boolean subMenu;
    public boolean subMenu2;

    public Gui(CosmicEvolution cosmicEvolution) {
        this.ce = cosmicEvolution;
    }
    public abstract void loadTextures();

    public abstract void deleteTextures();

    public abstract void drawGui();



    public abstract Button getActiveButton();

    public TextField getTextField(){
        return null;
    }
}

