package spacegame.block;

import spacegame.core.CosmicEvolution;
import spacegame.core.MouseListener;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.world.World;

public final class BlockStrawCrafting extends Block {
    public BlockStrawCrafting(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();


        if(this.ID == Block.strawChestTier0.ID && playerHeldItem == Item.straw.ID ){
            if(CosmicEvolution.instance.save.thePlayer.getHeldItemCount() >= 8){
                world.setBlockWithNotify(x,y,z, Block.strawChestTier1.ID);
                for(int i = 0; i < 8; i++){
                    CosmicEvolution.instance.save.thePlayer.removeItemFromInventory();
                }
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(this.ID == Block.strawChestTier1.ID && playerHeldItem == Item.straw.ID){
            if(CosmicEvolution.instance.save.thePlayer.getHeldItemCount() >= 8){
                world.setBlockWithNotify(x,y,z, Block.strawChest.ID);
                world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addChestLocation(x, y, z,  new Inventory(((BlockContainer)(Block.strawChest)).inventoryWidth, ((BlockContainer)(Block.strawChest)).inventoryHeight));
                for(int i = 0; i < 8; i++){
                    CosmicEvolution.instance.save.thePlayer.removeItemFromInventory();
                }
                MouseListener.rightClickReleased = false;
                return;
            }
        }

        if(this.ID == Block.strawBasketTier0.ID && playerHeldItem == Item.straw.ID){
            if(CosmicEvolution.instance.save.thePlayer.getHeldItemCount() >= 4){
                world.setBlockWithNotify(x,y,z, Block.air.ID);
                world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.strawBasket.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
                for(int i = 0; i < 4; i++){
                    CosmicEvolution.instance.save.thePlayer.removeItemFromInventory();
                }
                MouseListener.rightClickReleased = false;
            }
        }
    }
}
