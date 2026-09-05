package spacegame.block;

import spacegame.entity.EntityFallingBlock;
import spacegame.world.worldtypes.World;

public final class BlockSand extends Block{
    public BlockSand(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void onBlockUpdate(int x, int y, int z, World world){
        if(this.canBlockFall(x,y,z, world)) {
            EntityFallingBlock entityFallingBlock = new EntityFallingBlock(x + 0.5, y, z + 0.5, this.ID, this.getBlockModel(x, y, z, world));
            world.addEntity(entityFallingBlock);
            world.setBlockAndNotify(x, y, z, Block.air.ID, false);
        }
    }

    private boolean canBlockFall(int x, int y, int z, World world){
        return world.getBlockID(x,y - 1,z) == Block.air.ID;
    }
}
