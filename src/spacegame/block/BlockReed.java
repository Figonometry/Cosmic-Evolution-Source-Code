package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.core.Timer;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.item.ItemKnife;
import spacegame.world.World;

public final class BlockReed extends Block implements ITimeUpdate {
    //Lower block of this type should be assumed to be water logged
    public BlockReed(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){
        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem != Item.NULL_ITEM_REFERENCE && Item.list[playerHeldItem] instanceof ItemKnife && world.getBlockID(x,y,z) == Block.reedLower.ID && world.getBlockID(x, y + 1, z) == Block.reedUpper.ID){
            world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.reeds.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
            world.addTimeEvent(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
            world.setBlockWithNotify(x, y + 1, z, Block.air.ID);
        } else {
            world.removeTimeEvent(x,y,z);
            world.setBlockWithNotify(x, y + 1, z, Block.air.ID);
            world.setBlockWithNotify(x, y, z, Block.air.ID);
        }

    }
    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        if(world.getBlockID(x, y + 1, z) != Block.air.ID)return;

        world.setBlockWithNotify(x, y + 1, z, Block.reedUpper.ID);
        world.removeTimeEvent(x,y,z);
    }

    @Override
    public long getUpdateTime() {
        return 7 * Timer.DAY;
    }

    @Override
    public String getDisplayStringText() {
        return "Will Grow In: ";
    }
}
