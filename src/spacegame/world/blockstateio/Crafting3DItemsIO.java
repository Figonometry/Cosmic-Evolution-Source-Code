package spacegame.world.blockstateio;

import spacegame.item.crafting.InWorldCraftingRecipe;
import spacegame.nbt.NBTTagCompound;
import spacegame.render.RenderEngine;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.InWorld3DCraftingItem;
import spacegame.world.blockstate.MultiState;


public class Crafting3DItemsIO {

    public void saveCrafting3DItems(Chunk chunk, NBTTagCompound nbtTagCompound){
        InWorld3DCraftingItem inWorld3DCraftingItem;
        int totalCount = chunk.getBlockStateCount(MultiState.CRAFTING_3D_ITEM_STATE);
        InWorld3DCraftingItem[] inWorld3DCraftingItems = this.getAll3DCraftingItemsInArray(chunk,totalCount);
        int inWorldCrafting3DItemCount = 0;
        NBTTagCompound[] inWorldCrafting3DItemTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < inWorldCrafting3DItemTags.length; i++){

            inWorld3DCraftingItem = inWorld3DCraftingItems[i];
            inWorldCrafting3DItemTags[i] = new NBTTagCompound();
            inWorldCrafting3DItemTags[i].setInteger("index", inWorld3DCraftingItem.indexInChunk);
            inWorldCrafting3DItemTags[i].setShort("materialBlockID", inWorld3DCraftingItem.materialBlockID);
            inWorldCrafting3DItemTags[i].setShort("itemTextureID", inWorld3DCraftingItem.itemTextureID);
            inWorldCrafting3DItemTags[i].setInteger("activeCraftingLayer", inWorld3DCraftingItem.activeCraftingLayer);
            inWorldCrafting3DItemTags[i].setString("craftingRecipeName", inWorld3DCraftingItem.craftingRecipe.recipeName);

            for(int j = 0; j < 16; j++){
                inWorldCrafting3DItemTags[i].setIntArray("craftingLayer" + j, inWorld3DCraftingItem.subVoxelIndices[j]);
            }

            nbtTagCompound.setTag("inWorldCrafting3DItem" + inWorldCrafting3DItemCount, inWorldCrafting3DItemTags[i]);
            inWorldCrafting3DItemCount++;
        }
        nbtTagCompound.setInteger("inWorldCrafting3DItemCount", inWorldCrafting3DItemCount);
    }

    public void loadCrafting3DItems(Chunk chunk, NBTTagCompound nbtTagCompound) {
        int crafting3DItemCount = nbtTagCompound.getInteger("inWorldCrafting3DItemCount");
        NBTTagCompound inWorldCrafting3DItemLoadedTag;
        InWorld3DCraftingItem inWorld3DCraftingItem = null;
        for (int i = 0; i < crafting3DItemCount; i++) {
            inWorldCrafting3DItemLoadedTag = nbtTagCompound.getCompoundTag("inWorldCrafting3DItem" + i);
            int index = inWorldCrafting3DItemLoadedTag.getInteger("index");
            short materialBlockID = inWorldCrafting3DItemLoadedTag.getShort("materialBlockID");
            short itemTextureID = inWorldCrafting3DItemLoadedTag.getShort("itemTextureID");

            if (materialBlockID != RenderEngine.NULL_TEXTURE) {
                inWorld3DCraftingItem = new InWorld3DCraftingItem(index, materialBlockID,
                        InWorldCraftingRecipe.findInWorldCraftingRecipeFromName(inWorldCrafting3DItemLoadedTag.getString("craftingRecipeName")), chunk);
            } else if (itemTextureID != RenderEngine.NULL_TEXTURE) {
                inWorld3DCraftingItem = new InWorld3DCraftingItem(index,
                        InWorldCraftingRecipe.findInWorldCraftingRecipeFromName(inWorldCrafting3DItemLoadedTag.getString("craftingRecipeName")), chunk, itemTextureID);
            }

            inWorld3DCraftingItem.activeCraftingLayer = inWorldCrafting3DItemLoadedTag.getInteger("activeCraftingLayer");

            for (int j = 0; j < 16; j++) {
                inWorld3DCraftingItem.subVoxelIndices[j] = inWorldCrafting3DItemLoadedTag.getIntArray("craftingLayer" + j);
            }

            chunk.addBlockState(index, MultiState.CRAFTING_3D_ITEM_STATE, inWorld3DCraftingItem);
        }
    }

    public InWorld3DCraftingItem[] getAll3DCraftingItemsInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.CRAFTING_3D_ITEM_STATE, totalCount);
        InWorld3DCraftingItem[] returnArray = new InWorld3DCraftingItem[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (InWorld3DCraftingItem) base[i];
        }


        return returnArray;
    }
}
