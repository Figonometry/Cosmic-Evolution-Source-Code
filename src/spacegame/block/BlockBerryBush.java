package spacegame.block;

import spacegame.core.SpaceGame;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.WorldFace;

public final class BlockBerryBush extends Block implements ITickable {
    public BlockBerryBush(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onRightClick(int x, int y, int z, WorldFace worldFace, EntityPlayer player){
        if(SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockID(x,y,z) == Block.berryBush.ID){
            SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x,y,z, Block.berryBushNoBerries.ID);
            EntityItem item = new EntityItem(x + rand.nextFloat(), y + 0.5, z + rand.nextFloat(), Item.berry.ID, (byte) 0, (byte) 1, (short) -1);
            SpaceGame.instance.save.activeWorld.activeWorldFace.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(item);
        }
    }

    @Override
    public void tick(int x, int y, int z) {
        if(rand.nextInt(10000) == 0) {
            if (SpaceGame.instance.save.activeWorld.activeWorldFace.getBlockID(x, y, z) == Block.berryBushNoBerries.ID) {
                SpaceGame.instance.save.activeWorld.activeWorldFace.setBlockWithNotify(x, y, z, Block.berryBush.ID);
            }
        }
    }
}
