package spacegame.item;

import spacegame.block.Block;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public final class ItemBone extends Item {
    public ItemBone(short ID, String modelFilePath, String filepath) {
        super(ID, modelFilePath, filepath);
    }

    @Override
    public void onItemBlockRightClick(int x, int y, int z, World world, EntityPlayer player) {
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.stoneFragments.ID)return;

        world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.boneMeal.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0, null));
        world.clearChestLocation(x,y,z);
        world.setBlockWithNotify(x,y,z, Block.air.ID, false);
    }
}
