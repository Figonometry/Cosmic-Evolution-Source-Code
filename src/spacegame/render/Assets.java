package spacegame.render;

import spacegame.core.CosmicEvolution;

public abstract class Assets {
    public static int fontTextureLoader;
    public static TextureAtlas fontTextureAtlas;
    public static int blockTextureArray;
    public static int itemTextureArray;

    public static void enableBlockTextureArray(){
        blockTextureArray = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/blocks/", RenderEngine.TEXTURE_TYPE_2D_ARRAY, 33); //One higher than the expected amount
    }

    public static void enableItemTextureArray(){
        itemTextureArray = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/", RenderEngine.TEXTURE_TYPE_2D_ARRAY, 17); //This is one higher than the actual number of item textures
    }

    public static void disableBlockTextureArray(){
        CosmicEvolution.instance.renderEngine.deleteTexture(blockTextureArray);
    }

    public static void disableItemTextureArray(){
        CosmicEvolution.instance.renderEngine.deleteTexture(itemTextureArray);
    }

    public static void enableFontTextureAtlas (){
        fontTextureLoader = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/atlas/font.png", RenderEngine.TEXTURE_TYPE_2D, 0);
        fontTextureAtlas = CosmicEvolution.instance.renderEngine.createTextureAtlas(512, 512, 32, 32, 256, 0);
    }
}
