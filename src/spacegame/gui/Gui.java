package spacegame.gui;

import spacegame.core.SpaceGame;
import spacegame.render.*;

public abstract class Gui {
    public SpaceGame sg;
    public boolean subMenu;
    public boolean subMenu2;

    public Gui(SpaceGame spaceGame) {
        this.sg = spaceGame;
    }
    public abstract void loadTextures();

    public abstract void deleteTextures();

    public abstract void drawGui();

    public void renderCursor() {
        int color = 4210752;
        float fontID = 223 / 16F;
        Texture textureID = Assets.fontTextureAtlas.textures.get(223);
        int x = -15;
        int y = -15;
        float z = -1F;

        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        tessellator.addVertex2DTextureWithAtlas(color, x, y, z, 3, textureID, fontID);
        tessellator.addVertex2DTextureWithAtlas(color, x + 30, y + 30, z, 1, textureID, fontID);
        tessellator.addVertex2DTextureWithAtlas(color, x, y + 30, z, 2, textureID, fontID);
        tessellator.addVertex2DTextureWithAtlas(color, x + 30, y, z, 0, textureID, fontID);
        tessellator.addElements();
        tessellator.drawTexture2DWithAtlas(Assets.fontTextureLoader, Shader.screen2DTextureAtlas, SpaceGame.camera);
        tessellator.toggleOrtho();
    }




    public abstract Button getActiveButton();

    public TextField getTextField(){
        return null;
    }
}

