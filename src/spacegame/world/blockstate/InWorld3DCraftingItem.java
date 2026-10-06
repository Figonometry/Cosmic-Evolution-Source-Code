package spacegame.world.blockstate;

import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.block.BlockIDList;
import spacegame.block.BlockItemStone;
import spacegame.core.CosmicEvolution;
import spacegame.core.Sound;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityParticle;
import spacegame.gui.GuiInGame;
import spacegame.item.Item;
import spacegame.item.StoneToolMetadata;
import spacegame.item.crafting.InWorldCraftingRecipe;
import spacegame.render.RenderEngine;
import spacegame.world.Chunk;
import spacegame.render.texturelists.BlockTextureList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public final class InWorld3DCraftingItem extends BlockState{
    public int[][] subVoxelIndices = new int[16][144];
    public int indexInChunk;
    public short materialBlockID = RenderEngine.NULL_TEXTURE; //Used for texture lookup on blocks
    public short itemTextureID = RenderEngine.NULL_TEXTURE;
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

    public InWorld3DCraftingItem(int index, InWorldCraftingRecipe craftingRecipe, Chunk chunk, short itemTextureID){
        this.indexInChunk = index;
        this.itemTextureID = itemTextureID;
        this.craftingRecipe = craftingRecipe;
        this.activeCraftingLayer = 0;
        this.chunk = chunk;
    }

    @Override
    public void onTick(Chunk callingChunk){
        if(this.removeObject){
          callingChunk.removeBlockState(this.indexInChunk, MultiState.CRAFTING_3D_ITEM_STATE);
        }
    }

    public short calculateOutPutMetadata(){
        switch (this.materialBlockID){
            case BlockIDList.ANDESITE_ITEM_STONE -> {
                return StoneToolMetadata.ANDESITE;
            }
            case BlockIDList.GRANITE_ITEM_STONE -> {
                return StoneToolMetadata.GRANITE;
            }
            case BlockIDList.BASALT_ITEM_STONE -> {
                return StoneToolMetadata.BASALT;
            }
            case BlockIDList.CHERT_ITEM_STONE -> {
                return StoneToolMetadata.CHERT;
            }
            case BlockIDList.OBSIDIAN_ITEM_STONE -> {
                return StoneToolMetadata.OBSIDIAN;
            }
            case BlockIDList.FLINT_ITEM_STONE -> {
                return StoneToolMetadata.FLINT;
            }
        }

        return Item.list[this.craftingRecipe.outputItemID].metadata;
    }
    //This needs to be changed to go down along with up
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
                this.chunk.setBlockAndNotify(this.chunk.getBlockXFromIndex(this.indexInChunk), this.chunk.getBlockYFromIndex(this.indexInChunk), this.chunk.getBlockZFromIndex(this.indexInChunk), this.craftingRecipe.outputBlockID);
            } else {
                if (!CosmicEvolution.instance.save.thePlayer.addItemToInventory(this.craftingRecipe.outputItemID, this.calculateOutPutMetadata(), (byte) this.craftingRecipe.outputCount, Item.list[this.craftingRecipe.outputItemID].getDurability(this.calculateOutPutMetadata()), 0, null)) {
                    CosmicEvolution.instance.save.activeWorld.addEntity(new EntityItem(this.chunk.getBlockXFromIndex(this.indexInChunk) + 0.5, this.chunk.getBlockYFromIndex(this.indexInChunk) + 0.25, this.chunk.getBlockZFromIndex(this.indexInChunk) + 0.5, this.craftingRecipe.outputItemID, this.calculateOutPutMetadata(), (byte) this.craftingRecipe.outputCount, Item.list[this.craftingRecipe.outputItemID].getDurability(this.calculateOutPutMetadata()), 0, null));
                }

                this.chunk.setBlock(this.indexInChunk, Block.air.ID);
            }

            this.removeObject = true;
            return;
        }


        this.activeCraftingLayer++;
    }

    public void removeSubVoxel(int index, double worldX, double worldY, double worldZ){
        //Return if the required held item doesnt match, not all will require a held item
        if(this.craftingRecipe.requiredHeldItem != Item.NULL_ITEM_REFERENCE) {
            if (this.craftingRecipe.requiredHeldItem != CosmicEvolution.instance.save.thePlayer.getHeldItem() ||
                    !this.craftingRecipe.heldBlockType.equals(Block.list[CosmicEvolution.instance.save.thePlayer.getHeldBlock()].getClassType())) {
                GuiInGame.setMessageText("Hold " + Item.list[this.craftingRecipe.requiredHeldItem].getDisplayName(Item.NULL_ITEM_REFERENCE, Item.NULL_ITEM_METADATA), 16777215);
                return;
            }
        }


        if(Block.list[this.materialBlockID] instanceof BlockItemStone){
            if(this.craftingRecipe.recipeIndices[this.activeCraftingLayer][index] != 1){
                this.subVoxelIndices[this.activeCraftingLayer][index] = 0;
                this.generateParticlesOnStoneSubVoxelBreak(worldX, worldY, worldZ);
                this.removeNonConnectedMaterial();
            }
        } else {
            this.subVoxelIndices[this.activeCraftingLayer][index] = 0;
        }
        CosmicEvolution.instance.soundPlayer.playSound(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, new Sound(Block.list[this.materialBlockID != RenderEngine.NULL_TEXTURE ? this.materialBlockID : this.itemTextureID].getStepSound(this.chunk.getBlockXFromIndex(this.indexInChunk), this.chunk.getBlockYFromIndex(this.indexInChunk), this.chunk.getBlockZFromIndex(this.indexInChunk)), false, 1f),new Random().nextFloat(0.6F, 1));
        this.checkCurrentCraftingLayerForCompletion();
        this.chunk.markDirty();
    }

    public void addSubVoxel(int index){
        if(this.subVoxelIndices[this.activeCraftingLayer][index] == 1 || Block.list[this.materialBlockID] instanceof BlockItemStone)return;


        this.subVoxelIndices[this.activeCraftingLayer][index] = 1;
        this.checkCurrentCraftingLayerForCompletion();
        CosmicEvolution.instance.soundPlayer.playSound(CosmicEvolution.instance.save.thePlayer.x, CosmicEvolution.instance.save.thePlayer.y, CosmicEvolution.instance.save.thePlayer.z, new Sound(Block.list[this.materialBlockID != RenderEngine.NULL_TEXTURE ? this.materialBlockID : this.itemTextureID].getStepSound(this.chunk.getBlockXFromIndex(this.indexInChunk), this.chunk.getBlockYFromIndex(this.indexInChunk), this.chunk.getBlockZFromIndex(this.indexInChunk)), false, 1f),new Random().nextFloat(0.6F, 1));
        this.chunk.markDirty();
    }


    public void activateCraftingLayer(int layerNumber){
        for(int i = 0; i < 144; i++){
            this.subVoxelIndices[layerNumber][i] = 1;
        }
    }

    public void activateIndicesFromImage(String filepath, int layerNumber){
        File file = new File(filepath);

        if(!file.exists())return;

        int[] pixels = new int[144];
        BufferedImage image;
        try {
            image = ImageIO.read(file);
            image.getRGB(0, 0, 12, 12, pixels, 0,12);

            for(int i = 0; i < pixels.length; i++){
                this.subVoxelIndices[layerNumber][i] = ((pixels[i] >> 16) & 255) == 0 && ((pixels[i] >> 8) & 255) == 0 && (pixels[i] & 255) == 0 ? 1 : 0;
            }

        } catch (Exception e){
            e.printStackTrace();
            return;
        }
    }

    public void generateParticlesOnStoneSubVoxelBreak(double x, double y, double z){
        EntityParticle[] particles = new EntityParticle[16];


        for(int i = 0; i < particles.length; i++){
            float xMove = CosmicEvolution.globalRand.nextFloat(0.5f, 1);
            xMove = CosmicEvolution.globalRand.nextBoolean() ? xMove : -xMove;
            float zMove = CosmicEvolution.globalRand.nextFloat(0.5f, 1);
            zMove = CosmicEvolution.globalRand.nextBoolean() ? zMove : -zMove;

            particles[i] = new EntityParticle(x, y, z, true, 240, this.materialBlockID, true, false, true, true, CosmicEvolution.globalRand.nextInt(31), CosmicEvolution.globalRand.nextInt(31));

            particles[i].setMovementVector(new Vector3f(xMove, 0, zMove));

            particles[i].size = 0.03125f;

            this.chunk.parentWorld.addEntity(particles[i]);
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
