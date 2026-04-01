package spacegame.item.crafting;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.item.Item;
import spacegame.util.MathUtil;
import spacegame.world.Chunk;

public final class InWorldCraftingItem {
    public int indexInChunk;
    public CraftingBlockRecipes outputRecipe;
    public boolean hasBeenBound;
    public Chunk chunk;
    public boolean[] itemsFilled;
    public boolean remove;


    public InWorldCraftingItem(CraftingBlockRecipes outputRecipe, int indexInChunk, Chunk chunk){
        this.indexInChunk = indexInChunk;
        this.chunk = chunk;
        this.outputRecipe = outputRecipe;

        this.itemsFilled = new boolean[this.outputRecipe.requiredItems.length];
    }


    public boolean canItemBePlaced(double x, double y, double z, short inputItem){
        if(inputItem == Item.reedTwine.ID && this.outputRecipe.requiresBinding){
            return this.bind();
        }

        for(int i = 0; i < this.itemsFilled.length; i++){
            if(this.itemsFilled[i])continue;
            if(this.outputRecipe.requiredItems[i] != inputItem)continue;
            // the 0.25f centers the position to the midle of the grid of voxels
            if(MathUtil.distance2D(x,z, this.outputRecipe.requiredItemPositions[i][0] + 0.25f, this.outputRecipe.requiredItemPositions[i][1] + 0.25f) <= this.outputRecipe.requiredItemRadius[i]){
                this.itemsFilled[i] = true;
                CosmicEvolution.instance.save.thePlayer.removeItemFromInventory();
                return true;
            }
        }

        return false;
    }

    private boolean bind(){
        for(int i = 0; i < this.itemsFilled.length; i++){
            if(!this.itemsFilled[i])return false;
        }

        this.hasBeenBound = true;

        CosmicEvolution.instance.save.thePlayer.removeItemFromInventory();

        return true;
    }

    public void checkOutputComplete(){
        if(!this.hasBeenBound && this.outputRecipe.requiresBinding)return;

        for(int i = 0; i < this.itemsFilled.length; i++) {
            if (!this.itemsFilled[i]) return;
        }

        int x = this.chunk.getBlockXFromIndex(this.indexInChunk);
        int y = this.chunk.getBlockYFromIndex(this.indexInChunk);
        int z = this.chunk.getBlockZFromIndex(this.indexInChunk);

        if(!CosmicEvolution.instance.save.thePlayer.addItemToInventory(this.outputRecipe.itemID,
                this.outputRecipe.isBlock ? this.outputRecipe.blockID : Item.NULL_ITEM_METADATA,
                this.outputRecipe.outputItemCount, this.outputRecipe.isBlock ? Item.NULL_ITEM_DURABILITY :  Item.list[this.outputRecipe.itemID].durability)){
            if(this.outputRecipe.isBlock){
                CosmicEvolution.instance.save.activeWorld.addEntity(new EntityBlock(x + 0.5, y + 0.125, z + 0.5, this.outputRecipe.blockID, this.outputRecipe.outputItemCount));
            } else {
                CosmicEvolution.instance.save.activeWorld.addEntity(new EntityItem(x + 0.5, y + 0.125, z + 0.5, this.outputRecipe.itemID, Item.NULL_ITEM_METADATA, this.outputRecipe.outputItemCount, Item.list[this.outputRecipe.itemID].durability));
            }
        }

        CosmicEvolution.instance.save.activeWorld.setBlockWithNotify(x,y,z, Block.air.ID, false);

        this.remove = true;


        CosmicEvolution.instance.save.activeWorld.delayWhenExitingUI = 60;
    }

    public boolean areAllItemsFilled(){
        for(int i = 0; i < this.itemsFilled.length; i++){
            if(!this.itemsFilled[i])return  false;
        }

        return true;
    }






}
