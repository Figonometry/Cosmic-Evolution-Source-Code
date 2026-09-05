package spacegame.item.crafting;

import spacegame.block.Block;
import spacegame.item.Item;
import spacegame.item.StoneToolMetadata;

public final class CraftingBlockRecipes {
    public static final int TECH_LEVEL_PRIMITIVE = 1;
    public static final CraftingBlockRecipes[] list = new CraftingBlockRecipes[128];

    public static final CraftingBlockRecipes andesiteStoneAxe = new CraftingBlockRecipes(Item.stoneAxe.ID, StoneToolMetadata.ANDESITE, Item.stoneAxe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.ANDESITE),
            new short[]{Item.block.ID, Item.stoneHandAxe.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 1, new short[]{Block.itemStick.ID, StoneToolMetadata.ANDESITE}, false,
            Item.stoneHandAxe.ID, StoneToolMetadata.ANDESITE);

    public static final CraftingBlockRecipes andesiteShovel = new CraftingBlockRecipes(Item.stoneShovel.ID, StoneToolMetadata.ANDESITE, Item.stoneShovel.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.ANDESITE),
            new short[]{Item.block.ID, Item.stoneHandShovel.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 135}, (byte)1, true, 1, 2, new short[]{Block.itemStick.ID, StoneToolMetadata.ANDESITE}, false,
            Item.stoneHandShovel.ID, StoneToolMetadata.ANDESITE);

    public static final CraftingBlockRecipes andesiteKnife = new CraftingBlockRecipes(Item.stoneKnife.ID,StoneToolMetadata.ANDESITE,  Item.stoneKnife.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.ANDESITE),
            new short[]{Item.block.ID, Item.stoneHandKnifeBlade.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 270}, (byte)1, true, 1, 3, new short[]{Block.itemStick.ID, StoneToolMetadata.ANDESITE}, false,
            Item.stoneHandKnifeBlade.ID, StoneToolMetadata.ANDESITE);

    public static final CraftingBlockRecipes primitiveDoor = new CraftingBlockRecipes(Item.primitiveDoor.ID, Item.primitiveDoor.getDisplayName(Item.NULL_ITEM_REFERENCE, Item.NULL_ITEM_METADATA),
            new short[]{Item.block.ID, Item.block.ID, Item.block.ID, Item.block.ID, Item.block.ID, Item.block.ID, Item.block.ID}, new int[]{1,1,1,1,1,1,1}, TECH_LEVEL_PRIMITIVE,
            new double[][]{{1,0.0625,0.5f}, {0.5f,0.0625,0.5f}, {0,0.0625,0.5f}, {0.75f,0.0625,0.25f}, {0.25f,0.0625,0.25f}, {0.75f,0.0625,0.75f}, {0.25f,0.0625,0.75f}},
            new double[]{90,90,90,0,0,0,0}, (byte)1, true, 6, 4,
            new short[]{Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID,Block.itemStick.ID},
            Item.block.ID, Block.itemStick.ID);

    public static final CraftingBlockRecipes andesiteHoe = new CraftingBlockRecipes(Item.stoneHoe.ID, StoneToolMetadata.ANDESITE, Item.stoneHoe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.ANDESITE),
            new short[]{Item.block.ID, Item.stoneHoeHead.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 7, new short[]{Block.itemStick.ID, StoneToolMetadata.ANDESITE}, false,
            Item.stoneHoeHead.ID, StoneToolMetadata.ANDESITE);

    public static final CraftingBlockRecipes andesiteSpear = new CraftingBlockRecipes(Item.stoneSpear.ID, StoneToolMetadata.ANDESITE, Item.stoneSpear.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.ANDESITE),
            new short[]{Item.block.ID, Item.stoneSpearHead.ID, Item.block.ID}, new int[]{1,1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.2, 0.0625, 0.2},{0.85, 0.0625, 0.85}},
            new double[]{-45, 45,-45}, (byte)1, true, 1, 8, new short[]{Block.itemStick.ID, StoneToolMetadata.ANDESITE, Block.itemStick.ID}, false,
            Item.stoneSpearHead.ID, StoneToolMetadata.ANDESITE);

    public static final CraftingBlockRecipes graniteStoneAxe = new CraftingBlockRecipes(Item.stoneAxe.ID, StoneToolMetadata.GRANITE, Item.stoneAxe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.GRANITE),
            new short[]{Item.block.ID, Item.stoneHandAxe.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 9, new short[]{Block.itemStick.ID, StoneToolMetadata.GRANITE}, false,
            Item.stoneHandAxe.ID, StoneToolMetadata.GRANITE);

    public static final CraftingBlockRecipes graniteShovel = new CraftingBlockRecipes(Item.stoneShovel.ID, StoneToolMetadata.GRANITE, Item.stoneShovel.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.GRANITE),
            new short[]{Item.block.ID, Item.stoneHandShovel.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 135}, (byte)1, true, 1, 10, new short[]{Block.itemStick.ID, StoneToolMetadata.GRANITE}, false,
            Item.stoneHandShovel.ID, StoneToolMetadata.GRANITE);

    public static final CraftingBlockRecipes graniteKnife = new CraftingBlockRecipes(Item.stoneKnife.ID, StoneToolMetadata.GRANITE, Item.stoneKnife.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.GRANITE),
            new short[]{Item.block.ID, Item.stoneHandKnifeBlade.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 270}, (byte)1, true, 1, 11, new short[]{Block.itemStick.ID, StoneToolMetadata.GRANITE}, false,
            Item.stoneHandKnifeBlade.ID, StoneToolMetadata.GRANITE);

    public static final CraftingBlockRecipes graniteHoe = new CraftingBlockRecipes(Item.stoneHoe.ID, StoneToolMetadata.GRANITE, Item.stoneHoe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.GRANITE),
            new short[]{Item.block.ID, Item.stoneHoeHead.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 12, new short[]{Block.itemStick.ID, StoneToolMetadata.GRANITE}, false,
            Item.stoneHoeHead.ID, StoneToolMetadata.GRANITE);

    public static final CraftingBlockRecipes graniteSpear = new CraftingBlockRecipes(Item.stoneSpear.ID, StoneToolMetadata.GRANITE, Item.stoneSpear.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.GRANITE),
            new short[]{Item.block.ID, Item.stoneSpearHead.ID, Item.block.ID}, new int[]{1,1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.2, 0.0625, 0.2},{0.85, 0.0625, 0.85}},
            new double[]{-45, 45,-45}, (byte)1, true, 1, 13, new short[]{Block.itemStick.ID, StoneToolMetadata.GRANITE, Block.itemStick.ID}, false,
            Item.stoneSpearHead.ID, StoneToolMetadata.GRANITE);

    public static final CraftingBlockRecipes basaltStoneAxe = new CraftingBlockRecipes(Item.stoneAxe.ID, StoneToolMetadata.BASALT, Item.stoneAxe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.BASALT),
            new short[]{Item.block.ID, Item.stoneHandAxe.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 14, new short[]{Block.itemStick.ID, StoneToolMetadata.BASALT}, false,
            Item.stoneHandAxe.ID, StoneToolMetadata.BASALT);

    public static final CraftingBlockRecipes basaltShovel = new CraftingBlockRecipes(Item.stoneShovel.ID, StoneToolMetadata.BASALT, Item.stoneShovel.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.BASALT),
            new short[]{Item.block.ID, Item.stoneHandShovel.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 135}, (byte)1, true, 1, 15, new short[]{Block.itemStick.ID, StoneToolMetadata.BASALT}, false,
            Item.stoneHandShovel.ID, StoneToolMetadata.BASALT);

    public static final CraftingBlockRecipes basaltKnife = new CraftingBlockRecipes(Item.stoneKnife.ID, StoneToolMetadata.BASALT, Item.stoneKnife.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.BASALT),
            new short[]{Item.block.ID, Item.stoneHandKnifeBlade.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 270}, (byte)1, true, 1, 16, new short[]{Block.itemStick.ID, StoneToolMetadata.BASALT}, false,
            Item.stoneHandKnifeBlade.ID, StoneToolMetadata.BASALT);

    public static final CraftingBlockRecipes basaltHoe = new CraftingBlockRecipes(Item.stoneHoe.ID, StoneToolMetadata.BASALT, Item.stoneHoe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.BASALT),
            new short[]{Item.block.ID, Item.stoneHoeHead.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 17, new short[]{Block.itemStick.ID, StoneToolMetadata.BASALT}, false,
            Item.stoneHoeHead.ID, StoneToolMetadata.BASALT);

    public static final CraftingBlockRecipes basaltSpear = new CraftingBlockRecipes(Item.stoneSpear.ID, StoneToolMetadata.BASALT, Item.stoneSpear.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.BASALT),
            new short[]{Item.block.ID, Item.stoneSpearHead.ID, Item.block.ID}, new int[]{1,1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.2, 0.0625, 0.2},{0.85, 0.0625, 0.85}},
            new double[]{-45, 45,-45}, (byte)1, true, 1, 18, new short[]{Block.itemStick.ID, StoneToolMetadata.BASALT, Block.itemStick.ID}, false,
            Item.stoneSpearHead.ID, StoneToolMetadata.BASALT);

    public static final CraftingBlockRecipes chertStoneAxe = new CraftingBlockRecipes(Item.stoneAxe.ID, StoneToolMetadata.CHERT, Item.stoneAxe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.CHERT),
            new short[]{Item.block.ID, Item.stoneHandAxe.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 19, new short[]{Block.itemStick.ID, StoneToolMetadata.CHERT}, false,
            Item.stoneHandAxe.ID, StoneToolMetadata.CHERT);

    public static final CraftingBlockRecipes chertShovel = new CraftingBlockRecipes(Item.stoneShovel.ID, StoneToolMetadata.CHERT,  Item.stoneShovel.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.CHERT),
            new short[]{Item.block.ID, Item.stoneHandShovel.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 135}, (byte)1, true, 1, 20, new short[]{Block.itemStick.ID, StoneToolMetadata.CHERT}, false,
            Item.stoneHandShovel.ID, StoneToolMetadata.CHERT);

    public static final CraftingBlockRecipes chertKnife = new CraftingBlockRecipes(Item.stoneKnife.ID, StoneToolMetadata.CHERT, Item.stoneKnife.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.CHERT),
            new short[]{Item.block.ID, Item.stoneHandKnifeBlade.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 270}, (byte)1, true, 1, 21, new short[]{Block.itemStick.ID, StoneToolMetadata.CHERT}, false,
            Item.stoneHandKnifeBlade.ID, StoneToolMetadata.CHERT);

    public static final CraftingBlockRecipes chertHoe = new CraftingBlockRecipes(Item.stoneHoe.ID, StoneToolMetadata.CHERT, Item.stoneHoe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.CHERT),
            new short[]{Item.block.ID, Item.stoneHoeHead.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 22, new short[]{Block.itemStick.ID, StoneToolMetadata.CHERT}, false,
            Item.stoneHoeHead.ID, StoneToolMetadata.CHERT);

    public static final CraftingBlockRecipes chertSpear = new CraftingBlockRecipes(Item.stoneSpear.ID, StoneToolMetadata.CHERT, Item.stoneSpear.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.CHERT),
            new short[]{Item.block.ID, Item.stoneSpearHead.ID, Item.block.ID}, new int[]{1,1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.2, 0.0625, 0.2},{0.85, 0.0625, 0.85}},
            new double[]{-45, 45,-45}, (byte)1, true, 1, 23, new short[]{Block.itemStick.ID, StoneToolMetadata.CHERT, Block.itemStick.ID}, false,
            Item.stoneSpearHead.ID, StoneToolMetadata.CHERT);

    public static final CraftingBlockRecipes obsidianStoneAxe = new CraftingBlockRecipes(Item.stoneAxe.ID, StoneToolMetadata.OBSIDIAN, Item.stoneAxe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.OBSIDIAN),
            new short[]{Item.block.ID, Item.stoneHandAxe.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 24, new short[]{Block.itemStick.ID, StoneToolMetadata.OBSIDIAN}, false,
            Item.stoneHandAxe.ID, StoneToolMetadata.OBSIDIAN);

    public static final CraftingBlockRecipes obsidianShovel = new CraftingBlockRecipes(Item.stoneShovel.ID, StoneToolMetadata.OBSIDIAN, Item.stoneShovel.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.OBSIDIAN),
            new short[]{Item.block.ID, Item.stoneHandShovel.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 135}, (byte)1, true, 1, 25, new short[]{Block.itemStick.ID, StoneToolMetadata.OBSIDIAN}, false,
            Item.stoneHandShovel.ID, StoneToolMetadata.OBSIDIAN);

    public static final CraftingBlockRecipes obsidianKnife = new CraftingBlockRecipes(Item.stoneKnife.ID, StoneToolMetadata.OBSIDIAN, Item.stoneKnife.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.OBSIDIAN),
            new short[]{Item.block.ID, Item.stoneHandKnifeBlade.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 270}, (byte)1, true, 1, 26, new short[]{Block.itemStick.ID, StoneToolMetadata.OBSIDIAN}, false,
            Item.stoneHandKnifeBlade.ID, StoneToolMetadata.OBSIDIAN);

    public static final CraftingBlockRecipes obsidianHoe = new CraftingBlockRecipes(Item.stoneHoe.ID, StoneToolMetadata.OBSIDIAN, Item.stoneHoe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.OBSIDIAN),
            new short[]{Item.block.ID, Item.stoneHoeHead.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 27, new short[]{Block.itemStick.ID, StoneToolMetadata.OBSIDIAN}, false,
            Item.stoneHoeHead.ID, StoneToolMetadata.OBSIDIAN);

    public static final CraftingBlockRecipes obsidianSpear = new CraftingBlockRecipes(Item.stoneSpear.ID, StoneToolMetadata.OBSIDIAN, Item.stoneSpear.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.OBSIDIAN),
            new short[]{Item.block.ID, Item.stoneSpearHead.ID, Item.block.ID}, new int[]{1,1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.2, 0.0625, 0.2},{0.85, 0.0625, 0.85}},
            new double[]{-45, 45,-45}, (byte)1, true, 1, 28, new short[]{Block.itemStick.ID, StoneToolMetadata.OBSIDIAN, Block.itemStick.ID}, false,
            Item.stoneSpearHead.ID, StoneToolMetadata.OBSIDIAN);

    public static final CraftingBlockRecipes flintStoneAxe = new CraftingBlockRecipes(Item.stoneAxe.ID, StoneToolMetadata.FLINT, Item.stoneAxe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.FLINT),
            new short[]{Item.block.ID, Item.stoneHandAxe.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 29, new short[]{Block.itemStick.ID, StoneToolMetadata.FLINT}, false,
            Item.stoneHandAxe.ID, StoneToolMetadata.FLINT);

    public static final CraftingBlockRecipes flintShovel = new CraftingBlockRecipes(Item.stoneShovel.ID, StoneToolMetadata.FLINT, Item.stoneShovel.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.FLINT),
            new short[]{Item.block.ID, Item.stoneHandShovel.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 135}, (byte)1, true, 1, 30, new short[]{Block.itemStick.ID, StoneToolMetadata.FLINT}, false,
            Item.stoneHandShovel.ID, StoneToolMetadata.FLINT);

    public static final CraftingBlockRecipes flintKnife = new CraftingBlockRecipes(Item.stoneKnife.ID,StoneToolMetadata.FLINT,  Item.stoneKnife.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.FLINT),
            new short[]{Item.block.ID, Item.stoneHandKnifeBlade.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.28, 0.0626, 0.28}},
            new double[]{-45, 270}, (byte)1, true, 1, 31, new short[]{Block.itemStick.ID, StoneToolMetadata.FLINT}, false,
            Item.stoneHandKnifeBlade.ID, StoneToolMetadata.FLINT);

    public static final CraftingBlockRecipes flintHoe = new CraftingBlockRecipes(Item.stoneHoe.ID, StoneToolMetadata.FLINT, Item.stoneHoe.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.FLINT),
            new short[]{Item.block.ID, Item.stoneHoeHead.ID}, new int[]{1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.27, 0.0626, 0.44}},
            new double[]{-45, 135}, (byte)1, true, 1, 32, new short[]{Block.itemStick.ID, StoneToolMetadata.FLINT}, false,
            Item.stoneHoeHead.ID, StoneToolMetadata.FLINT);

    public static final CraftingBlockRecipes flintSpear = new CraftingBlockRecipes(Item.stoneSpear.ID, StoneToolMetadata.FLINT, Item.stoneSpear.getDisplayName(Item.NULL_ITEM_REFERENCE, StoneToolMetadata.FLINT),
            new short[]{Item.block.ID, Item.stoneSpearHead.ID, Item.block.ID}, new int[]{1,1,1}, TECH_LEVEL_PRIMITIVE, new double[][]{{0.5, 0.0625, 0.5},{0.2, 0.0625, 0.2},{0.85, 0.0625, 0.85}},
            new double[]{-45, 45,-45}, (byte)1, true, 1, 33, new short[]{Block.itemStick.ID, StoneToolMetadata.FLINT, Block.itemStick.ID}, false,
            Item.stoneSpearHead.ID, StoneToolMetadata.FLINT);

    public short minimumRequiredItemID; //These two fields are meant for displaying in the crafting table a minimum requirement for displaying, otherwise for example every single tool will show up because it requires a stick
    public short minimumRequiredItemMetadata;
    public short itemID;
    public short metadata = Item.NULL_ITEM_METADATA;
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
                                boolean requiresBinding, int bindingCount, int ID, short[] requiredItemMetadata,
                                short minimumRequiredItemID, short minimumRequiredItemMetadata){

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
    public CraftingBlockRecipes(short itemID, short metadata, String displayName, short[] requiredItems, int[] requiredItemCount, int techLevelRequired,
                                double[][] requiredItemPositions, double[] requiredItemAngles, byte outputItemCount,
                                boolean requiresBinding, int bindingCount, int ID, short[] requiredItemMetadata, boolean isBlock,
                                short minimumRequiredItemID, short minimumRequiredItemMetadata){

        if(list[ID] != null){
            throw new RuntimeException("Crafting Recipe ID: " + ID + " ALREADY OCCUPIED WHEN ATTEMPTING TO ADD " + this + " TO THE LIST");
        }

        list[ID] = this;

        this.itemID = itemID;
        this.metadata = metadata;
        this.isBlock = isBlock;
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

    public static CraftingBlockRecipes getRecipeFromOutputItemAndMetadata(short itemID, short metadata){
        for(int i = 0; i < list.length; i++){
            if(list[i] == null)continue;
            if(list[i].itemID == itemID && list[i].metadata == metadata){
                return list[i];
            }
        }
       throw new IllegalStateException("Unable to get recipe for itemID, was an invalid ID passed in?");
    }



}
