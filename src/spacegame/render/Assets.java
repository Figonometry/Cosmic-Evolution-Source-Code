package spacegame.render;

import spacegame.core.SpaceGame;

public abstract class Assets {
    public static int fontTextureLoader;
    public static TextureAtlas fontTextureAtlas;
    public static int blockTextureArray;
    public static int itemTextureArray;

    public static void enableBlockTextureArray(){
        blockTextureArray = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/blocks/", RenderEngine.TEXTURE_TYPE_2D_ARRAY, 29); //One higher than the expected amount
    }

    public static void enableItemTextureArray(){
        itemTextureArray = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/", RenderEngine.TEXTURE_TYPE_2D_ARRAY, 11); //This is one higher than the actual number of item textures
    }

    public static void enableFontTextureAtlas (){
        fontTextureLoader = SpaceGame.instance.renderEngine.createTexture("src/spacegame/assets/textures/atlas/font.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        fontTextureAtlas = new TextureAtlas(512, 512, 32, 32, 256, 0);
    }
}
