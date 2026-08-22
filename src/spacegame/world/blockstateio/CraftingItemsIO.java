package spacegame.world.blockstateio;

import spacegame.item.crafting.CraftingBlockRecipes;
import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.InWorld3DCraftingItem;
import spacegame.world.blockstate.InWorldCraftingItem;
import spacegame.world.blockstate.MultiState;


public class CraftingItemsIO {



    public void saveCraftingItems(Chunk chunk, NBTTagCompound nbtTagCompound){
        InWorldCraftingItem inWorldCraftingItem;
        int totalCount = chunk.getBlockStateCount(MultiState.CRAFTING_ITEM_STATE);
        InWorldCraftingItem[] craftingItems1 = this.getAllCraftingItemsInArray(chunk, totalCount);
        int inWorldCraftingItemCount = 0;
        NBTTagCompound[] inWorldCraftingItemTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < inWorldCraftingItemTags.length; i++){

            inWorldCraftingItem = craftingItems1[i];
            inWorldCraftingItemTags[i] = new NBTTagCompound();
            inWorldCraftingItemTags[i].setInteger("index", inWorldCraftingItem.indexInChunk);
            inWorldCraftingItemTags[i].setInteger("outputRecipeID", inWorldCraftingItem.outputRecipe.ID);
            inWorldCraftingItemTags[i].setBoolean("hasBeenBound", inWorldCraftingItem.hasBeenBound);

            int filledItemIndex = 0;
            for(int j = 0; j < inWorldCraftingItem.itemsFilled.length; j++){
                inWorldCraftingItemTags[i].setBoolean("item" + j + "Filled", inWorldCraftingItem.itemsFilled[j]);
                filledItemIndex++;
            }

            inWorldCraftingItemTags[i].setInteger("filledItemIndex", filledItemIndex);

            nbtTagCompound.setTag("inWorldCraftingItem" + inWorldCraftingItemCount, inWorldCraftingItemTags[i]);
            inWorldCraftingItemCount++;
        }
        nbtTagCompound.setInteger("inWorldCraftingItemCount", inWorldCraftingItemCount);
    }

    public void loadCraftingItems(Chunk chunk, NBTTagCompound nbtTagCompound){
        int craftingItemCount = nbtTagCompound.getInteger("inWorldCraftingItemCount");
        NBTTagCompound inWorldCraftingItemLoadedTag;
        InWorldCraftingItem inWorldCraftingItem;
        for(int i = 0; i < craftingItemCount; i++){
            inWorldCraftingItemLoadedTag = nbtTagCompound.getCompoundTag("inWorldCraftingItem" + i);

            int index =  inWorldCraftingItemLoadedTag.getInteger("index");
            int recipeID = inWorldCraftingItemLoadedTag.getInteger("outputRecipeID");
            boolean hasBeenBound = inWorldCraftingItemLoadedTag.getBoolean("hasBeenBound");

            inWorldCraftingItem = new InWorldCraftingItem(CraftingBlockRecipes.list[recipeID], index, chunk);
            inWorldCraftingItem.hasBeenBound = hasBeenBound;

            int filledItemIndex = inWorldCraftingItemLoadedTag.getInteger("filledItemIndex");

            for(int j = 0; j < filledItemIndex; j++){
                inWorldCraftingItem.itemsFilled[j] = inWorldCraftingItemLoadedTag.getBoolean("item" + j + "Filled");
            }


            chunk.addBlockState(index, MultiState.CRAFTING_ITEM_STATE, inWorldCraftingItem);
        }
    }

    public InWorldCraftingItem[] getAllCraftingItemsInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.CRAFTING_ITEM_STATE, totalCount);
        InWorldCraftingItem[] returnArray = new InWorldCraftingItem[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (InWorldCraftingItem) base[i];
        }


        return returnArray;
    }
}
