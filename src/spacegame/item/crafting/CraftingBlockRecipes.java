package spacegame.item.crafting;

import spacegame.block.Block;
import spacegame.item.Item;

public final class CraftingBlockRecipes {
    public static final int TECH_LEVEL_PRIMITIVE = 1;
    public static final CraftingBlockRecipes[] list = new CraftingBlockRecipes[128];
    public static final CraftingBlockRecipes stoneAxe = new CraftingBlockRecipes(Item.stoneAxe.ID, Item.stoneAxe.getDisplayName(Item.NULL_ITEM_REFERENCE),
            new short[]{Item.block.ID, Item.stoneHandAxe.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 1, new short[]{Block.itemStick.ID, Item.NULL_ITEM_METADATA});

    public static final CraftingBlockRecipes stoneShovel = new CraftingBlockRecipes(Item.stoneShovel.ID, Item.stoneShovel.getDisplayName(Item.NULL_ITEM_REFERENCE),
            new short[]{Item.block.ID, Item.stoneHandShovel.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 135}, (byte)1, true, 1, 2, new short[]{Block.itemStick.ID, Item.NULL_ITEM_METADATA});

    public static final CraftingBlockRecipes stoneKnife = new CraftingBlockRecipes(Item.stoneKnife.ID, Item.stoneKnife.getDisplayName(Item.NULL_ITEM_REFERENCE),
            new short[]{Item.block.ID, Item.stoneHandKnifeBlade.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 270}, (byte)1, true, 1, 3, new short[]{Block.itemStick.ID, Item.NULL_ITEM_METADATA});

    public static final CraftingBlockRecipes primitiveDoor = new CraftingBlockRecipes(Item.primitiveDoor.ID, Item.primitiveDoor.getDisplayName(Item.NULL_ITEM_REFERENCE),
            new short[]{Item.block.ID, Item.block.ID, Item.block.ID, Item.block.ID, Item.block.ID, Item.block.ID, Item.block.ID}, new int[]{1,1,1,1,1,1,1}, TECH_LEVEL_PRIMITIVE,
            new double[][]{{1,0.0625,0.5f}, {0.5f,0.0625,0.5f}, {0,0.0625,0.5f}, {0.75f,0.0625,0.25f}, {0.25f,0.0625,0.25f}, {0.75f,0.0625,0.75f}, {0.25f,0.0625,0.75f}}, new double[]{90,90,90,0,0,0,0}, (byte)1, true, 6, 4,
            new short[]{Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID});

    public static final CraftingBlockRecipes stoneHoe = new CraftingBlockRecipes(Item.stoneHoe.ID, Item.stoneHoe.getDisplayName(Item.NULL_ITEM_REFERENCE),
            new short[]{Item.block.ID, Item.stoneHoeHead.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 5, new short[]{Block.itemStick.ID, Item.NULL_ITEM_METADATA});

    public short itemID;
    public short blockID;
    public String displayName;
    public boolean isBlock;
    public boolean requiresBinding;
    public int bindingCount;
    public byte outputItemCount;
    public short[] requiredItems;
    public short[] requiredItemMetadata;
    public int[] requiredItemCount;
    public double[][] requiredItemPositions;
    public double[] requiredItemAngles;
    public int techLevelRequired; //This determines what type of table can make items, lower tier items can always be made at higher tier tables but not vice versa
    public final int ID;


    //This is to represent crafting recipes that require multiple input items and where 3d shape crafting wouldnt suffice
    public CraftingBlockRecipes(short itemID, String displayName, short[] requiredItems, int[] requiredItemCount, int techLevelRequired,
                                double[][] requiredItemPositions, double[] requiredItemAngles, byte outputItemCount,
                                boolean requiresBinding, int bindingCount, int ID, short[] requiredItemMetadata){

        if(list[ID] != null){
            throw new RuntimeException("Crafting Recipe ID: " + ID + " ALREADY OCCUPIED WHEN ATTEMPTING TO ADD " + this + " TO THE LIST");
        }

        list[ID] = this;


        this.itemID = itemID;
        this.displayName = displayName;
        this.requiredItems = requiredItems;
        this.requiredItemCount = requiredItemCount;
        this.techLevelRequired = techLevelRequired;
        this.requiredItemPositions = requiredItemPositions;
        this.requiredItemAngles = requiredItemAngles;
        this.outputItemCount = outputItemCount;
        this.requiresBinding = requiresBinding;
        this.bindingCount = bindingCount;
        this.requiredItemMetadata = requiredItemMetadata;
        this.ID = ID;
    }
    public CraftingBlockRecipes(short itemID, short blockID, String displayName, short[] requiredItems, int[] requiredItemCount, int techLevelRequired,
                                double[][] requiredItemPositions, double[] requiredItemAngles, byte outputItemCount,
                                boolean requiresBinding, int bindingCount, int ID, short[] requiredItemMetadata){

        if(list[ID] != null){
            throw new RuntimeException("Crafting Recipe ID: " + ID + " ALREADY OCCUPIED WHEN ATTEMPTING TO ADD " + this + " TO THE LIST");
        }

        list[ID] = this;

        this.itemID = itemID;
        this.blockID = blockID;
        this.isBlock = true;
        this.displayName = displayName;
        this.requiredItems = requiredItems;
        this.requiredItemCount = requiredItemCount;
        this.techLevelRequired = techLevelRequired;
        this.requiredItemPositions = requiredItemPositions;
        this.requiredItemAngles = requiredItemAngles;
        this.outputItemCount = outputItemCount;
        this.requiresBinding = requiresBinding;
        this.bindingCount = bindingCount;
        this.requiredItemMetadata = requiredItemMetadata;
        this.ID = ID;
    }

    public static CraftingBlockRecipes getRecipeFromOutputItem(short itemID){
        for(int i = 0; i < list.length; i++){
            if(list[i] == null)continue;
            if(list[i].itemID == itemID){
                return list[i];
            }
        }
       throw new IllegalStateException("Unable to get recipe for itemID, was an invalid ID passed in?");
    }



}
