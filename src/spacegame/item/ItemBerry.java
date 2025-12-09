package spacegame.item;

import spacegame.block.Block;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public final class ItemBerry extends ItemFood {
    public ItemBerry(short ID, int textureID, String filepath, float saturationIncrease) {
        super(ID, textureID, filepath, saturationIncrease);
    }

    @Override
    public void onItemBlockRightClick(int x, int y, int z, World world, EntityPlayer player) {
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.stoneFragments.ID)return;
        
        world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.berrySeed.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
        world.clearChestLocation(x,y,z);
        world.removeChestLocation(x,y,z);
        world.setBlockWithNotify(x,y,z, Block.air.ID, false);
    }
}
