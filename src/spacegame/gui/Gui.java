package spacegame.gui;

import spacegame.core.CosmicEvolution;
import spacegame.render.Assets;
import spacegame.render.RenderEngine;
import spacegame.render.Shader;
import spacegame.render.Texture;

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

    public void renderCursor() {
        int color = 4210752;
        float fontID = 223 / 16F;
        Texture textureID = Assets.fontTextureAtlas.textures.get(223);
        int x = -15;
        int y = -15;
        float z = -1F;

        RenderEngine.Tessellator tessellator = RenderEngine.Tessellator.instance;
        tessellator.toggleOrtho();
        tessellator.addVertex2DTextureWithAtlas(color, x, y, z, 3, textureID, fontID, 255);
        tessellator.addVertex2DTextureWithAtlas(color, x + 30, y + 30, z, 1, textureID, fontID, 255);
        tessellator.addVertex2DTextureWithAtlas(color, x, y + 30, z, 2, textureID, fontID, 255);
        tessellator.addVertex2DTextureWithAtlas(color, x + 30, y, z, 0, textureID, fontID, 255);
        tessellator.addElements();
        tessellator.drawTexture2DWithAtlas(Assets.fontTextureLoader, Shader.screen2DTextureAtlas, CosmicEvolution.camera);
        tessellator.toggleOrtho();
    }




    public abstract Button getActiveButton();

    public TextField getTextField(){
        return null;
    }
}

