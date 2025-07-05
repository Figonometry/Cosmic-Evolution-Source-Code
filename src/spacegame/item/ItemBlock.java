package spacegame.item;

import spacegame.block.Block;

public final class ItemBlock extends Item {
    public ItemBlock(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public float getTextureID(short ID, short metadata, int face){
        return Block.list[metadata].getBlockTexture(metadata, face);
    }
}
