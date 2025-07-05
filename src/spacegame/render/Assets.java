package spacegame.render;

public abstract class Assets {
    public static TextureLoader fontTextureLoader;
    public static TextureAtlas fontTextureAtlas;
    public static TextureArrayLoader blockTextureArray;
    public static TextureArrayLoader itemTextureArray;

    public static void enableBlockTextureArray(){
        blockTextureArray = new TextureArrayLoader("src/spacegame/assets/textures/blocks/", 20);
    }

    public static void enableItemTextureArray(){
        itemTextureArray = new TextureArrayLoader("src/spacegame/assets/textures/item/", 9);
    }

    public static void enableFontTextureAtlas (){
        fontTextureLoader = new TextureLoader("src/spacegame/assets/textures/atlas/font.png", 512, 512);
        fontTextureAtlas = new TextureAtlas(fontTextureLoader, 32, 32, 256, 0);
    }
}
