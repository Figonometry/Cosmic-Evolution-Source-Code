package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.world.World;

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
    public void tick(int x, int y, int z, World world) {
        if (CosmicEvolution.globalRand.nextInt(166) == 0) {
            if (world.getBlockLightValue(x, y + 1, z) <= 4 && this.canBlockDecayGrass(x, y + 1, z, world)) {
                if(world.chunkFullySurrounded(x >> 5, y >> 5, z >> 5)) {
                    world.setBlockWithNotify(x, y, z, Block.dirt.ID);
                }
            }
        }
        if(CosmicEvolution.globalRand.nextInt(100000) == 0){
            if((world.getBlockLightValue(x, y + 1, z) >= 9 || world.getBlockSkyLightValue(x, y + 1, z) >= 9) && world.getBlockID(x,y + 1, z) == Block.air.ID){
                if(world.chunkFullySurrounded(x >> 5, (y + 1) >> 5, z >> 5)){
                    world.setBlockWithNotify(x, y + 1, z, Block.tallGrass.ID);
                }
            }
        }
    }

    private boolean canBlockDecayGrass(int x, int y, int z, World world){
        short blockID = world.getBlockID(x, y, z);
        if(blockID >= Block.oakLogFullSizeNormal.ID && blockID <= Block.oakLogSize1EastWest.ID && (blockID != Block.oakLogFullSizeNormal.ID && blockID != Block.oakLogFullSizeNorthSouth.ID && blockID != Block.oakLogFullSizeEastWest.ID)){
            return false;
        } else {
            return Block.list[blockID].isSolid;
        }
    }
}
