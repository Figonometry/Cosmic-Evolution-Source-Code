package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.core.Timer;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.RenderBlocks;
import spacegame.world.Tech;
import spacegame.world.World;

public final class BlockBerryBush extends Block implements ITimeUpdate {
    public BlockBerryBush(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem != Item.block.ID && playerHeldItem != Item.torch.ID){
            if(world.getBlockID(x,y,z) == Block.berryBush.ID){
                world.setBlockWithNotify(x,y,z, Block.berryBushNoBerries.ID);
                EntityItem item = new EntityItem(x + CosmicEvolution.globalRand.nextFloat(), y + 0.5, z + CosmicEvolution.globalRand.nextFloat(), Item.berry.ID, (byte) 0, (byte) 1, (short) -1);
                world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(item);
                world.addTimeEvent(x,y,z,CosmicEvolution.instance.save.time + this.getUpdateTime());
                Tech.techUpdateEvent(Tech.UPDATE_EVENT_BERRY_COLLECTION);
            }
            MouseListener.rightClickReleased = false;
        }
    }


    @Override
    public int getBlockTexture(int face){
        return face == RenderBlocks.TOP_FACE ? this.textureID + 1 : this.textureID;
    }

    @Override
    public void onTimeUpdate(int x, int y, int z, World world) {
        if(this.ID == Block.berryBushNoBerries.ID){
            world.setBlockWithNotify(x,y,z, Block.berryBushFlower.ID);
            world.updateTimeEventTime(x,y,z, CosmicEvolution.instance.save.time + this.getUpdateTime());
        } else if(this.ID == Block.berryBushFlower.ID){
            world.setBlockWithNotify(x,y,z, Block.berryBush.ID);
            world.removeTimeEvent(x,y,z);
        }
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        return this.ID == Block.berryBush.ID ? "Berry Bush (Ripe)" : this.ID == Block.berryBushFlower.ID ? "Berry Bush (Flowering)" : this.displayName;
    }

    @Override
    public long getUpdateTime() {
        return 7 * Timer.DAY;
    }

    @Override
    public String getDisplayStringText() {
        return this.ID == Block.berryBushNoBerries.ID ? "Will Flower In: " : "Will Ripen In: ";
    }
}
