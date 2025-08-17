package spacegame.block;

import spacegame.core.GameSettings;

public final class BlockLeaf extends Block {
    public BlockLeaf(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public int getBlockTexture(int face){
        return GameSettings.transparentLeaves ? this.textureID + 14 : this.textureID;
    }
}
