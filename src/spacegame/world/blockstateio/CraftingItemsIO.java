package spacegame.world.blockstateio;

import spacegame.item.crafting.CraftingBlockRecipes;
import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.InWorldCraftingItem;
import spacegame.world.blockstatewrapper.InWorldCraftingItemSafe;

import java.util.Iterator;
import java.util.Map;

public class CraftingItemsIO {



    public void saveCraftingItems(Chunk chunk, NBTTagCompound nbtTagCompound){
        InWorldCraftingItem inWorldCraftingItem;
        InWorldCraftingItem[] craftingItems1 = this.getAllCraftingItemsInArray(chunk);
        int inWorldCraftingItemCount = 0;
        NBTTagCompound[] inWorldCraftingItemTags = new NBTTagCompound[chunk.craftingItems.size()];
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


            chunk.addCraftingItem(inWorldCraftingItem);
        }
    }

    public InWorldCraftingItem[] getAllCraftingItemsInArray(Chunk chunk){
        int index = 0;
        InWorldCraftingItem craftingItem;
        InWorldCraftingItem[] inWorldCraftingItems = new InWorldCraftingItem[chunk.craftingItems.size()];
        Iterator<Map.Entry<Integer, InWorldCraftingItemSafe>> iterator = chunk.craftingItems.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, InWorldCraftingItemSafe> entry = iterator.next();
            craftingItem = entry.getValue().value;
            if(craftingItem != null){
                inWorldCraftingItems[index] = craftingItem;
                index++;
            }
        }
        return inWorldCraftingItems;
    }
}
