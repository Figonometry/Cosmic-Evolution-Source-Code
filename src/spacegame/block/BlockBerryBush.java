package spacegame.block;

import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.World;

public final class BlockBerryBush extends Block implements ITickable {
    public BlockBerryBush(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(world.getBlockID(x,y,z) == Block.berryBush.ID){
            world.setBlockWithNotify(x,y,z, Block.berryBushNoBerries.ID);
            EntityItem item = new EntityItem(x + rand.nextFloat(), y + 0.5, z + rand.nextFloat(), Item.berry.ID, (byte) 0, (byte) 1, (short) -1);
            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(item);
        }
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        if(rand.nextInt(10000) == 0) {
            if (world.getBlockID(x, y, z) == Block.berryBushNoBerries.ID) {
                world.setBlockWithNotify(x, y, z, Block.berryBush.ID);
            }
        }
    }
}
