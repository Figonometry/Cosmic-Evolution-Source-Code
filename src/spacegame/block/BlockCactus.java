package spacegame.block;

import spacegame.render.texturelists.BlockTextureList;

public final class BlockCactus extends Block {
    public BlockCactus(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public int getBlockTexture(int x, int y, int z, int face){
        return switch (face) {
            case Block.FACE_UP -> BlockTextureList.CACTUS_TOP_TEXTURE;
            case Block.FACE_DOWN -> BlockTextureList.CACTUS_BOTTOM_TEXTURE;
            default -> this.textureID;
        };
    }
}
