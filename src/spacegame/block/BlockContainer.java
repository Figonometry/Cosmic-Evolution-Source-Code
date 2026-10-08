package spacegame.block;

import spacegame.entity.EntityPlayer;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.world.Chunk;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;

public abstract class BlockContainer extends Block {
    public int inventoryWidth;
    public int inventoryHeight;
    public BlockContainer(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath);
        this.inventoryWidth = inventoryWidth;
        this.inventoryHeight = inventoryHeight;
    }


    @Override
    public void addBlockStates(int x, int y, int z, short heldBlock, World world, EntityPlayer player, Chunk chunk){
        short heldItem = player.getHeldItem();
        //Class cast exception here when placing item blocks
        ChestLocation chestLocation = new ChestLocation(Chunk.getBlockIndexFromCoordinates(x,y,z), new Inventory(((BlockContainer)(Block.list[heldBlock])).inventoryWidth, ((BlockContainer)(Block.list[heldBlock])).inventoryHeight), chunk);
        if(heldBlock == logPile.ID){
            chestLocation.inventory.itemStacks[0].count = 2;
        }
        if(heldBlock == brickPile.ID ){
            chestLocation.inventory.itemStacks[0].count = 1;
            chestLocation.inventory.itemStacks[0].item = Item.list[heldItem];
        }
        if(heldBlock == itemBlock.ID){
            chestLocation.inventory.itemStacks[0].count = 1;
            chestLocation.inventory.itemStacks[0].item = Item.list[heldItem];
            chestLocation.inventory.itemStacks[0].metadata = player.getHeldMetadata();
            chestLocation.inventory.itemStacks[0].durability = player.getHeldItemDurability();
            chestLocation.inventory.itemStacks[0].decayTime = player.getHeldItemDecayTime();
        }

        chunk.addBlockState(x,y,z, MultiState.CHEST_STATE ,chestLocation);
    }

}
