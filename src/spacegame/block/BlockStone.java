package spacegame.block;

import spacegame.render.texturelists.BlockTextureList;

public final class BlockStone extends Block {
    public BlockStone(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }
    public int getBlockTexture(int x, int y, int z, int face) {
        if(this.ID == BlockIDList.SHALE_STONE) {
            if (face == Block.FACE_UP ) {
                return BlockTextureList.SHALE_STONE_TOP_TEXTURE;
            }
            if(face == Block.FACE_DOWN){
                return BlockTextureList.SHALE_STONE_BOTTOM_TEXTURE;
            }
        }

        return this.textureID;
    }
}
