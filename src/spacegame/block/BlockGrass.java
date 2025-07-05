package spacegame.block;

import spacegame.core.SpaceGame;

public final class BlockGrass extends Block implements ITickable {
    public BlockGrass(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public int getBlockTexture(int face) {
        return switch (face) {
            case 0 -> this.textureID - 2;
            case 1 -> this.textureID - 1;
            default -> this.textureID;
        };
    }

    @Override
    public void tick(int x, int y, int z) {
        if (rand.nextInt(166) == 0) {
            if (SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockLightValue(x, y + 1, z) <= 4 && this.canBlockDecayGrass(x, y + 1, z)) {
                if(SpaceGame.instance.save.activeWorld.activeWorldFace.chunkFullySurrounded(x >> 5, y >> 5, z >> 5)) {
                    SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, Block.dirt.ID);
                }
            }
        }
    }

    private boolean canBlockDecayGrass(int x, int y, int z){
        short blockID = SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockID(x, y, z);
        if(blockID >= Block.oakLogFullSizeNormal.ID && blockID <= Block.oakLogSize1EastWest.ID && (blockID != Block.oakLogFullSizeNormal.ID && blockID != Block.oakLogFullSizeNorthSouth.ID && blockID != Block.oakLogFullSizeEastWest.ID)){
            return false;
        } else {
            return Block.list[blockID].isSolid;
        }
    }
}
