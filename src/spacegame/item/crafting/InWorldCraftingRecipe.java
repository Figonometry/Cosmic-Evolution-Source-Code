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
    private static final String NO_BLOCK_TYPE = "";
    public static final InWorldCraftingRecipe knife = new InWorldCraftingRecipe(recipePath + "stone/knife/", 1, "stoneKnife",
            Item.stoneHandKnifeBlade.ID, 1, Item.block.ID, Block.flintItemStone.getClassType(),false, 0,0);

    public static final InWorldCraftingRecipe axe = new InWorldCraftingRecipe(recipePath + "stone/axe/", 1, "stoneAxe",
            Item.stoneHandAxe.ID, 1, Item.block.ID, Block.flintItemStone.getClassType(),false, 0,1);

    public static final InWorldCraftingRecipe shovel = new InWorldCraftingRecipe(recipePath + "stone/shovel/", 1, "stoneShovel.txt",
            Item.stoneHandShovel.ID, 1, Item.block.ID, Block.flintItemStone.getClassType(),false, 0,2);

    public static final InWorldCraftingRecipe rockFragments = new InWorldCraftingRecipe(recipePath + "stone/rockFragments/", 1, "stoneFragments",
            Item.stoneFragments.ID, 1, Item.block.ID, Block.flintItemStone.getClassType(),false, 0,3);

    public static final InWorldCraftingRecipe rawBrick = new InWorldCraftingRecipe(recipePath + "clay/brick/", 4, "rawRedBrick",
            Item.rawClayAdobeBrick.ID, 2, Item.NULL_ITEM_REFERENCE, NO_BLOCK_TYPE,false, 3,4);

    public static final InWorldCraftingRecipe rawCookingPot = new InWorldCraftingRecipe(recipePath + "clay/cookingPot/", 10, "rawRedCookingPot",
            Item.block.ID,  Block.rawRedClayCookingPot.ID,  1, Item.NULL_ITEM_REFERENCE, NO_BLOCK_TYPE, false, 9,5);

    public static final InWorldCraftingRecipe reedChest = new InWorldCraftingRecipe(recipePath + "reed/chest/", 10, "reedChest",
            Item.block.ID, Block.reedChest.ID, 1, Item.NULL_ITEM_REFERENCE, NO_BLOCK_TYPE,false, 9,6);

    public static final InWorldCraftingRecipe reedBasket = new InWorldCraftingRecipe(recipePath + "reed/basket/", 13, "reedBasket",
            Item.reedBasket.ID, 1, Item.NULL_ITEM_REFERENCE, NO_BLOCK_TYPE,false, 12,7);

    public static final InWorldCraftingRecipe reedTwine = new InWorldCraftingRecipe(recipePath + "reed/twine/", 1, "reedTwine",
            Item.reedTwine.ID, 2, Item.NULL_ITEM_REFERENCE, NO_BLOCK_TYPE,false, 0,8);

    public static final InWorldCraftingRecipe reedCraftingGridTop = new InWorldCraftingRecipe(recipePath + "reed/craftingGrid/", 1, "reedCraftingGridTop",
            Item.reedCraftingGridTop.ID, 1, Item.NULL_ITEM_REFERENCE, NO_BLOCK_TYPE,false, 0,9);

    public static final InWorldCraftingRecipe deerPeltClothing = new InWorldCraftingRecipe(recipePath + "peltClothing/deer/", 1, "deerPeltClothing",
            Item.primitiveDeerPeltClothing.ID, 1, Item.stoneKnife.ID, NO_BLOCK_TYPE,false, 0,10);

    public static final InWorldCraftingRecipe wolfPeltClothing = new InWorldCraftingRecipe(recipePath + "peltClothing/wolf/", 1, "deerPeltClothing",
            Item.primitiveWolfPeltClothing.ID, 1, Item.stoneKnife.ID, NO_BLOCK_TYPE,false, 0,11);

    public static final InWorldCraftingRecipe stoneHoeHead = new InWorldCraftingRecipe(recipePath + "stone/hoeHead/", 1, "stoneHoeHead",
            Item.stoneHoeHead.ID, 1, Item.block.ID, Block.flintItemStone.getClassType(),false, 0,12);

    public static final InWorldCraftingRecipe stoneSpearHead = new InWorldCraftingRecipe(recipePath + "stone/spearHead/", 1, "stoneSpearHead",
            Item.stoneSpearHead.ID, 1, Item.block.ID, Block.flintItemStone.getClassType(), false, 0, 13);

    public static final InWorldCraftingRecipe woodenPanStage1 = new InWorldCraftingRecipe(recipePath + "woodenPan/stage1/", 16, "woodenPanStage1",
            Item.woodenPanStage1.ID, 1, Item.stoneHandAxe.ID, NO_BLOCK_TYPE, true, 15,  14);

    public static final InWorldCraftingRecipe woodenPanFinal = new InWorldCraftingRecipe(recipePath + "woodenPan/final/", 16, "woodenPan",
            Item.woodenPan.ID, 1, Item.stoneHandAxe.ID, NO_BLOCK_TYPE, true, 1,  15);

    public int[][] recipeIndices;
    public String recipeName;
    public short outputItemID;
    public short outputBlockID = Block.NULL_BLOCK_REFERENCE;
    public int maxLayers;
    public int outputCount;
    public short requiredHeldItem = Item.NULL_ITEM_REFERENCE;
    public String heldBlockType = NO_BLOCK_TYPE;
    public boolean isCraftedFromTop;
    public int stoppingLayer;
    /*Outer array is the layer number and the inner is each individual layer's indices in a 16x16 grid, each layer image is loaded into a buffer starting from the bottom layer,
    only pixels that are entirely black will be recognized as valid by the loader and stored in the index array.
     */

    private InWorldCraftingRecipe(String folderPath, int numLayers, String name, short outputItemID, int outputCount, short requiredHeldItem,
                                 String heldBlockType, boolean isCraftedFromTop, int stoppingLayer, int ID){
        if(list[ID] != null){
            throw new IllegalStateException("Crafting recipe already loaded into list for " + this + "at ID " + ID);
        }

        list[ID] = this;

        this.recipeIndices = new int[numLayers][144];
        this.recipeName = name;
        this.outputItemID = outputItemID;
        this.maxLayers = numLayers - 1;
        this.outputCount = outputCount;
        this.requiredHeldItem = requiredHeldItem;
        this.heldBlockType  = heldBlockType;
        this.isCraftedFromTop = isCraftedFromTop;
        this.stoppingLayer = stoppingLayer;

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
                System.out.println(folderPath + imageNumber + ".png");
                e.printStackTrace();
            }
        }
    }

    private InWorldCraftingRecipe(String folderPath, int numLayers, String name, short outputItemID, short outputBlockID, int outputCount, short requiredHeldItem,
                                 String heldBlockType,  boolean isCraftedFromTop, int stoppingLayer, int ID){
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
        this.requiredHeldItem = requiredHeldItem;
        this.heldBlockType = heldBlockType;
        this.isCraftedFromTop = isCraftedFromTop;
        this.stoppingLayer = stoppingLayer;

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
