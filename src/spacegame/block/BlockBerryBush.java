package spacegame.block;

import spacegame.core.SpaceGame;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.gui.Tech;
import spacegame.world.World;

public final class BlockBerryBush extends Block implements ITickable {
    public BlockBerryBush(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(world.getBlockID(x,y,z) == Block.berryBush.ID){
            world.setBlockWithNotify(x,y,z, Block.berryBushNoBerries.ID);
            EntityItem item = new EntityItem(x + SpaceGame.globalRand.nextFloat(), y + 0.5, z + SpaceGame.globalRand.nextFloat(), Item.berry.ID, (byte) 0, (byte) 1, (short) -1);
            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(item);
            Tech.techUpdateEvent(Tech.UPDATE_EVENT_BERRY_COLLECTION);
        }
    }

    @Override
    public void tick(int x, int y, int z, World world) {
        if(SpaceGame.globalRand.nextInt(10000) == 0) {
            if (world.getBlockID(x, y, z) == Block.berryBushNoBerries.ID) {
                world.setBlockWithNotify(x, y, z, Block.berryBushFlower.ID);
            }
            if (world.getBlockID(x, y, z) == Block.berryBushFlower.ID) {
                world.setBlockWithNotify(x, y, z, Block.berryBush.ID);
            }
        }
    }


    @Override
    public int getBlockTexture(int face){
        switch (this.ID) {
            case 62 -> { //No berries?
                return switch (face) {
                    case 0 -> this.textureID + 13;
                    default -> this.textureID;
                };
            }
            case 61 -> { //Berries
                return switch (face){
                    case 0 -> this.textureID + 15;
                    default -> this.textureID;
                };
            }
            case 86 -> { //Berries with flowers
                return switch (face){
                    case 0 -> this.textureID + 1;
                    default -> this.textureID;
                };
            }
        }
        return this.textureID;
    }
}
