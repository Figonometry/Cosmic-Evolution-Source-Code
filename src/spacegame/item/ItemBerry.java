package spacegame.item;

import spacegame.block.Block;
import spacegame.core.Timer;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

public final class ItemBerry extends ItemFood implements IDecayItem {
    public ItemBerry(short ID, String modelFilePath, String filepath, float saturationIncrease) {
        super(ID, modelFilePath, filepath, saturationIncrease);
    }

    @Override
    public void onItemBlockRightClick(int x, int y, int z, World world, EntityPlayer player) {
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.stoneFragments.ID)return;
        
        world.addEntity(new EntityBlock(x + 0.5, y + 0.5, z + 0.5, Block.berrySeed.ID, (byte)1));
        world.clearChestLocation(x,y,z);
        world.removeChestLocation(x,y,z);
        world.setBlockWithNotify(x,y,z, Block.air.ID, false);
    }

    @Override
    public void decayItem(ItemStack itemStack) {
        itemStack.item = Item.rot;
        itemStack.decayTime = 0L;
    }

    @Override
    public long getDecayTime() {
        return Timer.GAME_DAY;
    }
}
