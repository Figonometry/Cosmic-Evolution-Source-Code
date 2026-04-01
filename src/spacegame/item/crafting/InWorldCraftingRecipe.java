package spacegame.item.crafting;

import spacegame.block.Block;
import spacegame.item.Item;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class InWorldCraftingRecipe {
    public static final String recipePath = "src/spacegame/assets/craftingRecipes/";
    public static final InWorldCraftingRecipe[] list = new InWorldCraftingRecipe[128];
    public static final InWorldCraftingRecipe knife = new InWorldCraftingRecipe(recipePath + "stone/knife/", 1, "stoneKnife", Item.stoneHandKnifeBlade.ID, 1, 0);
    public static final InWorldCraftingRecipe axe = new InWorldCraftingRecipe(recipePath + "stone/axe/", 1, "stoneAxe", Item.stoneHandAxe.ID, 1, 1);
    public static final InWorldCraftingRecipe shovel = new InWorldCraftingRecipe(recipePath + "stone/shovel/", 1, "stoneShovel.txt", Item.stoneHandShovel.ID, 1, 2);
    public static final InWorldCraftingRecipe rockFragments = new InWorldCraftingRecipe(recipePath + "stone/rockFragments/", 1, "stoneFragments", Item.stoneFragments.ID, 1, 3);
    public static final InWorldCraftingRecipe rawBrick = new InWorldCraftingRecipe(recipePath + "clay/brick/", 4, "rawRedBrick", Item.rawClayAdobeBrick.ID, 2, 4);
    public static final InWorldCraftingRecipe rawCookingPot = new InWorldCraftingRecipe(recipePath + "clay/cookingPot/", 10, "rawRedCookingPot", Item.block.ID,  Block.rawRedClayCookingPot.ID,  1, 5);
    public static final InWorldCraftingRecipe reedChest = new InWorldCraftingRecipe(recipePath + "/reed/chest/", 10, "reedChest", Item.block.ID, Block.reedChest.ID, 1, 6);
    public static final InWorldCraftingRecipe reedBasket = new InWorldCraftingRecipe(recipePath + "/reed/basket/", 13, "reedBasket", Item.reedBasket.ID, 1,7);
    public static final InWorldCraftingRecipe reedTwine = new InWorldCraftingRecipe(recipePath + "/reed/twine/", 1, "reedTwine", Item.reedTwine.ID, 2, 8);
    public static final InWorldCraftingRecipe reedCraftingGridTop = new InWorldCraftingRecipe(recipePath + "/reed/craftingGrid/", 1, "reedCraftingGridTop", Item.reedCraftingGridTop.ID, 1, 9);
    public int[][] recipeIndices;
    public String recipeName;
    public short outputItemID;
    public short outputBlockID = Block.NULL_BLOCK_REFERENCE;
    public int maxLayers;
    public int outputCount;

    /*Outer array is the layer number and the inner is each individual layer's indices in a 16x16 grid, each layer image is loaded into a buffer starting from the bottom layer,
    only pixels that are entirely black will be recognized as valid by the loader and stored in the index array.
     */
    public InWorldCraftingRecipe(String folderPath, int numLayers, String name, short outputItemID, int outputCount, int ID){
        if(list[ID] != null){
            throw new IllegalStateException("Crafting recipe already loaded into list for " + this + "at ID " + ID);
        }

        list[ID] = this;

        this.recipeIndices = new int[numLayers][144];
        this.recipeName = name;
        this.outputItemID = outputItemID;
        this.maxLayers = numLayers - 1;
        this.outputCount = outputCount;

        BufferedImage image;
        int[] tempBuffer = new int[144];
        for(int imageNumber = 0; imageNumber < numLayers; imageNumber++){
            try {
                image = ImageIO.read(new File(folderPath + imageNumber + ".png"));
                image.getRGB(0, 0, 12, 12, tempBuffer, 0, 12);

                //Check each pixel color, if the pixel is entirely black then set the value to 1 meaning it is required otherwise leave it as 0 meaning it must be empty
                for(int i = 0; i < tempBuffer.length; i++){
                    this.recipeIndices[imageNumber][i] = ((tempBuffer[i] >> 16) & 255) == 0 && ((tempBuffer[i] >> 8) & 255) == 0 && (tempBuffer[i] & 255) == 0 ? 1 : 0;
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public InWorldCraftingRecipe(String folderPath, int numLayers, String name, short outputItemID, short outputBlockID, int outputCount, int ID){
        if(list[ID] != null){
            throw new IllegalStateException("Crafting recipe already loaded into list for " + this + "at ID " + ID);
        }

        list[ID] = this;

        this.recipeIndices = new int[numLayers][144];
        this.recipeName = name;
        this.outputItemID = outputItemID;
        this.outputBlockID = outputBlockID;
        this.outputCount = outputCount;
        this.maxLayers = numLayers - 1;

        BufferedImage image;
        int[] tempBuffer = new int[144];
        for(int imageNumber = 0; imageNumber < numLayers; imageNumber++){
            try {
                image = ImageIO.read(new File(folderPath + imageNumber + ".png"));
                image.getRGB(0, 0, 12, 12, tempBuffer, 0, 12);

                //Check each pixel color, if the pixel is entirely black then set the value to 1 meaning it is required otherwise leave it as 0 meaning it must be empty
                for(int i = 0; i < tempBuffer.length; i++){
                    this.recipeIndices[imageNumber][i] = ((tempBuffer[i] >> 16) & 255) == 0 && ((tempBuffer[i] >> 8) & 255) == 0 && (tempBuffer[i] & 255) == 0 ? 1 : 0;
                }

            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static InWorldCraftingRecipe findInWorldCraftingRecipeFromName(String name){
        for(int i = 0; i < list.length; i++){
            if(list[i].recipeName.equals(name)){
                return list[i];
            }
        }
        throw new IllegalArgumentException("Unable to locate recipe name with the provided name " + name);
    }

    public static InWorldCraftingRecipe findInWorldCraftingRecipeFromOutputItem(short itemID){
        for(int i = 0; i < list.length; i++){
            if(list[i].outputItemID == itemID){
                return list[i];
            }
        }
        throw new IllegalArgumentException("Unable to locate recipe name with the provided output item ID " + itemID);
    }


}
