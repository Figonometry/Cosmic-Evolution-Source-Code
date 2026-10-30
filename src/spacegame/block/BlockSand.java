package spacegame.block;
import spacegame.entity.EntityFallingBlock;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.item.ItemWoodenPan;
import spacegame.world.Chunk;
import spacegame.world.blockstate.MultiState;
import spacegame.world.blockstate.PartialBlock;
import spacegame.world.worldtypes.World;

public final class BlockSand extends Block {
    public BlockSand(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }


    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.woodenPan.ID)return;
        if(player.getHeldMetadata() != ItemWoodenPan.EMPTY_PAN)return;
        //Set the metadata in the slot equal to this blockID for rendering purposes on the pan and for the loot table to pull
        player.inventory.itemStacks[EntityPlayer.selectedInventorySlot].metadata = this.ID;

        //Since this is sand/gravel replace this block with a partial block
        world.setBlockAndNotify(x,y,z, Block.partialBlock.ID, false);
        world.addBlockState(x,y,z, MultiState.PARTIAL_BLOCK_STATE, new PartialBlock(this.ID, PartialBlock.MAX_LAYERS - 1, Chunk.getBlockIndexFromCoordinates(x,y,z)));
    }


    @Override
    public void onBlockUpdate(int x, int y, int z, World world){
        if (world.getBlockID(x, y, z) != this.ID) {
            return;
        }

        if(this.canBlockFall(x,y,z, world)) {
            EntityFallingBlock entityFallingBlock = new EntityFallingBlock(x + 0.5, y, z + 0.5, this.ID, this.getBlockModel(x, y, z, world));
            world.addEntity(entityFallingBlock);
            world.setBlockAndNotify(x, y, z, Block.air.ID, false);
        }
    }

    private boolean canBlockFall(int x, int y, int z, World world){
        return world.getBlockID(x,y - 1,z) == Block.air.ID;
    }

    //Implement a static method that takes in x,y,z, world, player, and block for dropping from loot tables
}
