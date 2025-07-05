package spacegame.block;

import spacegame.core.SpaceGame;

public final class BlockDirt extends Block implements ITickable {
    public BlockDirt(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void tick(int x, int y, int z) {
        if (rand.nextInt(166) == 0) {
            if (SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockLightValue(x, y + 1, z) >= 9 && !Block.list[SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockID(x, y + 1, z)].isSolid) {
                if (this.isNearGrassBlock(x, y, z)) {
                    if(SpaceGame.instance.save.activeWorld.activeWorldFace.chunkFullySurrounded(x >> 5, y >> 5, z >> 5)) {
                        SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, Block.grass.ID);
                    }
                }
            }
        }
    }


    private boolean isNearGrassBlock(int x, int y, int z){
        for(int i = x - 1; i <= x + 1; i++){
            for(int j = y - 1; j <= y + 1; j++){
                for(int k = z - 1; k <= z + 1; k++){
                    if(!(x == i && z == k)){
                        if(this.isValidGrassBlockSpread(i,j,k)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isValidGrassBlockSpread(int x, int y, int z){
        return SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockID(x,y,z) == Block.grass.ID && !Block.list[SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockID(x, y + 1, z)].isSolid;
    }
}
