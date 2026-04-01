package spacegame.item.crafting;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.entity.EntityItem;
import spacegame.item.Item;
import spacegame.world.Chunk;

import java.util.ArrayList;
import java.util.Random;

public final class InWorld3DCraftingItem {
    public int[][] subVoxelIndices = new int[16][144];
    public int indexInChunk;
    public short materialBlockID; //Used for texture lookup
    public int activeCraftingLayer;
    public InWorldCraftingRecipe craftingRecipe;
    public Chunk chunk;
    public boolean removeObject;

    public InWorld3DCraftingItem(int index, short materialBlockID, InWorldCraftingRecipe craftingRecipe, Chunk chunk){
        this.indexInChunk = index;
        this.materialBlockID = materialBlockID;
        this.craftingRecipe = craftingRecipe;
        this.activeCraftingLayer = 0;
        this.chunk = chunk;
    }

    public void checkCurrentCraftingLayerForCompletion(){
        int[] currentCraftingLayer = this.subVoxelIndices[this.activeCraftingLayer];
        int[] currentCraftingLayerRecipe = this.craftingRecipe.recipeIndices[this.activeCraftingLayer];

        for(int i = 0; i < currentCraftingLayerRecipe.length; i++){
            if(currentCraftingLayer[i] != currentCraftingLayerRecipe[i]){
                return;
            }
        }


        if(this.activeCraftingLayer == this.craftingRecipe.maxLayers){

            if(this.craftingRecipe.outputBlockID != Block.NULL_BLOCK_REFERENCE){
                this.chunk.setBlockWithNotify(this.chunk.getBlockXFromIndex(this.indexInChunk), this.chunk.getBlockYFromIndex(this.indexInChunk), this.chunk.getBlockZFromIndex(this.indexInChunk), this.craftingRecipe.outputBlockID);
            } else {
                if (!CosmicEvolution.instance.save.thePlayer.addItemToInventory(this.craftingRecipe.outputItemID, Item.list[this.craftingRecipe.outputItemID].metadata, (byte) this.craftingRecipe.outputCount, Item.list[this.craftingRecipe.outputItemID].durability)) {
                    CosmicEvolution.instance.save.activeWorld.addEntity(new EntityItem(this.chunk.getBlockXFromIndex(this.indexInChunk) + 0.5, this.chunk.getBlockYFromIndex(this.indexInChunk) + 0.25, this.chunk.getBlockZFromIndex(this.indexInChunk) + 0.5, this.craftingRecipe.outputItemID, Item.list[this.craftingRecipe.outputItemID].metadata, (byte) this.craftingRecipe.outputCount, Item.list[this.craftingRecipe.outputItemID].durability));
                }

                this.chunk.blocks[this.indexInChunk] = Block.air.ID;
            }

            this.removeObject = true;
            return;
        }


        this.activeCraftingLayer++;
    }

    public void removeSubVoxel(int index){
        if(this.materialBlockID == Block.stone.ID){
            if(this.craftingRecipe.recipeIndices[this.activeCraftingLayer][index] != 1){
                this.subVoxelIndices[this.activeCraftingLayer][index] = 0;
                this.removeNonConnectedMaterial();
            }
        } else {
            this.subVoxelIndices[this.activeCraftingLayer][index] = 0;
        }
        CosmicEvolution.instance.soundPlayer.playSound(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, new Sound(Block.list[this.materialBlockID].getStepSound(this.chunk.getBlockXFromIndex(this.indexInChunk), this.chunk.getBlockYFromIndex(this.indexInChunk), this.chunk.getBlockZFromIndex(this.indexInChunk)), false, 1f),new Random().nextFloat(0.6F, 1));
        this.checkCurrentCraftingLayerForCompletion();
        this.chunk.markDirty();
    }

    public void addSubVoxel(int index){
        if(this.subVoxelIndices[this.activeCraftingLayer][index] == 1 || this.materialBlockID == Block.stone.ID)return;


        this.subVoxelIndices[this.activeCraftingLayer][index] = 1;
        this.checkCurrentCraftingLayerForCompletion();
        CosmicEvolution.instance.soundPlayer.playSound(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, new Sound(Block.list[this.materialBlockID].getStepSound(this.chunk.getBlockXFromIndex(this.indexInChunk), this.chunk.getBlockYFromIndex(this.indexInChunk), this.chunk.getBlockZFromIndex(this.indexInChunk)), false, 1f),new Random().nextFloat(0.6F, 1));
        this.chunk.markDirty();
    }


    public void activateCraftingLayer(int layerNumber){
        for(int i = 0; i < 144; i++){
            this.subVoxelIndices[layerNumber][i] = 1;
        }
    }

    //For stone material to carve things out faster
    public void removeNonConnectedMaterial(){
        ArrayList<Integer> listOfIndexes = new ArrayList<>();
        ArrayList<Integer> localCopyOfIndexesChecked = new ArrayList<>();
        ArrayList<Integer> indexesChecked = new ArrayList<>();


        for(int i = 0; i < this.craftingRecipe.recipeIndices[this.activeCraftingLayer].length; i++){
            if(this.craftingRecipe.recipeIndices[this.activeCraftingLayer][i] == 1){
                listOfIndexes.add(i);
                indexesChecked.add(i);
            }
        }

        while(!listOfIndexes.isEmpty()){
            localCopyOfIndexesChecked.addAll(listOfIndexes);
            listOfIndexes.clear();

            for(int i = 0; i < localCopyOfIndexesChecked.size(); i++){
                this.addNeighborIndexesToList(listOfIndexes, indexesChecked, localCopyOfIndexesChecked.get(i));
            }

            localCopyOfIndexesChecked.clear();
        }

        for(int i = 0; i < this.subVoxelIndices[this.activeCraftingLayer].length; i++){
            if(this.subVoxelIndices[this.activeCraftingLayer][i] == 0)continue;
            if(this.isIndexAlreadyInList(indexesChecked, i))continue;

            this.subVoxelIndices[this.activeCraftingLayer][i] = 0;
        }



    }

    private void addNeighborIndexesToList(ArrayList<Integer> listOfIndexes, ArrayList<Integer> indexesChecked, int index){
        if(index % 12 != 0){
            if(!this.isIndexAlreadyInList(indexesChecked, index - 1) && this.subVoxelIndices[this.activeCraftingLayer][index - 1] == 1){
                listOfIndexes.add(index - 1);
                indexesChecked.add(index - 1);
            }
        }

        if(index % 12 != 11){
            if(!this.isIndexAlreadyInList(indexesChecked, index + 1) && this.subVoxelIndices[this.activeCraftingLayer][index + 1] == 1){
                listOfIndexes.add(index + 1);
                indexesChecked.add(index + 1);
            }
        }

        if(index / 12 != 0){
            if(!this.isIndexAlreadyInList(indexesChecked, index - 12) && this.subVoxelIndices[this.activeCraftingLayer][index - 12] == 1){
                listOfIndexes.add(index - 12);
                indexesChecked.add(index - 12);
            }
        }

        if(index / 12 != 11){
            if(!this.isIndexAlreadyInList(indexesChecked, index + 12) && this.subVoxelIndices[this.activeCraftingLayer][index + 12] == 1){
                listOfIndexes.add(index + 12);
                indexesChecked.add(index + 12);
            }
        }
    }

    private boolean isIndexAlreadyInList(ArrayList<Integer> indexesChecked, int indexBeingChecked){
        for(int i = 0; i < indexesChecked.size(); i++){
            if(indexesChecked.get(i) == indexBeingChecked){
                return true;
            }
        }
        return false;
    }
}
