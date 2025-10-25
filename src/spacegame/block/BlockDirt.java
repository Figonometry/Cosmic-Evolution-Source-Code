package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public final class BlockDirt extends Block implements ITickable {
    public BlockDirt(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        if (CosmicEvolution.globalRand.nextInt(166) == 0) {
            if ((world.getBlockLightValue(x, y + 1, z) >= 9 || world.getBlockSkyLightValue(x, y + 1, z) >= 9) && !Block.list[world.getBlockID(x, y + 1, z)].isSolid) {
                if (this.isNearGrassBlock(x, y, z)) {
                    if(world.chunkFullySurrounded(x >> 5, y >> 5, z >> 5)) {
                        world.setBlockWithNotify(x, y, z, Block.grass.ID);
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
        return (CosmicEvolution.instance.save.activeWorld.getBlockID(x,y,z) == Block.grass.ID || CosmicEvolution.instance.save.activeWorld.getBlockID(x,y,z) == Block.grassWithClay.ID) && !Block.list[CosmicEvolution.instance.save.activeWorld.getBlockID(x, y + 1, z)].isSolid;
    }
}
