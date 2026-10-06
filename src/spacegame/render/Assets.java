package spacegame.render;

import spacegame.core.CosmicEvolution;

public abstract class Assets {
    public static int fontTextureLoader;
    public static TextureAtlas fontTextureAtlas;
    public static int textBox;
    public static TextureAtlas textBoxAtlas;
    public static int mouseIcon;
    public static TextureAtlas mouseIconAtlas;
    public static int blockTextureArray;
    public static int itemTextureArray;

    public static void enableBlockTextureArray(){
        blockTextureArray = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/blocks/", RenderEngine.TEXTURE_TYPE_2D_ARRAY, 182, true); //One higher than the expected amount
    }

    public static void enableItemTextureArray(){
        itemTextureArray = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/item/", RenderEngine.TEXTURE_TYPE_2D_ARRAY, 32, true); //This is one higher than the actual number of item textures
    }

    public static void disableBlockTextureArray(){
        CosmicEvolution.instance.renderEngine.deleteTexture(blockTextureArray);
    }

    public static void disableItemTextureArray(){
        CosmicEvolution.instance.renderEngine.deleteTexture(itemTextureArray);
    }

    public static void enableFontTextureAtlas (){
        fontTextureLoader = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/atlas/font.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        fontTextureAtlas = CosmicEvolution.instance.renderEngine.createTextureAtlas(512, 512, 32, 32, 256, 0);
    }

    public static void disableFontTextureAtlas(){
        CosmicEvolution.instance.renderEngine.deleteTexture(fontTextureLoader);
        fontTextureAtlas = null;
    }

    public static void enableTextBoxAtlas(){
        textBox = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/atlas/keyBoxes.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        textBoxAtlas = CosmicEvolution.instance.renderEngine.createTextureAtlas(64,64,32,32,4,0);
    }

    public static void disableTextBoxAtlas(){
        CosmicEvolution.instance.renderEngine.deleteTexture(textBox);
        textBoxAtlas = null;
    }

    public static void enableMouseIconAtlas(){
        mouseIcon = CosmicEvolution.instance.renderEngine.createTexture("src/spacegame/assets/textures/atlas/mouseIcons.png", RenderEngine.TEXTURE_TYPE_2D, 0, true);
        mouseIconAtlas = CosmicEvolution.instance.renderEngine.createTextureAtlas(64,64,32,32,4,0);
    }

    public static void disableMouseIconAtlas(){
        CosmicEvolution.instance.renderEngine.deleteTexture(mouseIcon);
        mouseIconAtlas = null;
    }
}
