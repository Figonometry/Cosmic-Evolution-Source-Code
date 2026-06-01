package spacegame.item;

import spacegame.block.Block;
import spacegame.entity.EntityPlayer;
import spacegame.world.blockstate.InWorld3DCraftingItem;
import spacegame.item.crafting.InWorldCraftingRecipe;
import spacegame.world.Chunk;
import spacegame.world.World;

public final class ItemPelt extends Item {
    public ItemPelt(short ID, String modelFilePath, String filepath) {
        super(ID, modelFilePath, filepath);
    }

    public void onItemBlockRightClick(int x, int y, int z, World world, EntityPlayer player){
        if(!(Item.list[player.getHeldItem()] instanceof ItemKnife))return;

        if(this.ID == Item.deerPelt.ID){

            world.clearChestLocation(x,y,z);
            world.setBlockWithNotify(x,y,z, Block.crafting3DItem.ID, false);

            InWorld3DCraftingItem inWorld3DCraftingItem = new InWorld3DCraftingItem(Chunk.getBlockIndexFromCoordinates(x,y,z),
                    InWorldCraftingRecipe.deerPeltClothing, world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5), (short) 58);
            inWorld3DCraftingItem.activateIndicesFromImage("src/spacegame/assets/craftingRecipes/peltClothing/peltInject.png", 0);
            world.addInWorldCrafting3DItem(x,y,z, inWorld3DCraftingItem);

            world.delayWhenExitingUI = 60;
        } else if(this.ID == Item.wolfPelt.ID){

            world.clearChestLocation(x,y,z);
            world.setBlockWithNotify(x,y,z, Block.crafting3DItem.ID, false);

            InWorld3DCraftingItem inWorld3DCraftingItem = new InWorld3DCraftingItem(Chunk.getBlockIndexFromCoordinates(x,y,z),
                    InWorldCraftingRecipe.wolfPeltClothing, world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5), (short) 60);
            inWorld3DCraftingItem.activateIndicesFromImage("src/spacegame/assets/craftingRecipes/peltClothing/peltInject.png", 0);
            world.addInWorldCrafting3DItem(x,y,z, inWorld3DCraftingItem);

            world.delayWhenExitingUI = 60;
        }

    }
}
