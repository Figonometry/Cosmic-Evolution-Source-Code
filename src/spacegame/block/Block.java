package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.item.ItemKnife;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.render.model.ModelLoader;
import spacegame.util.MathUtil;
import spacegame.world.AxisAlignedBB;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.*;

import java.io.*;
import java.util.Random;

public class Block {
    public static final int NULL_BLOCK_REFERENCE = -1;
    public static final String blockFolderPath = "src/spacegame/assets/blockFiles/";
    public static final Block[] list = new Block[Short.MAX_VALUE];
    public static final Block air = new Block(BlockIDList.AIR, BlockTextureList.NO_TEXTURE, blockFolderPath + "air.txt");
    public static final Block grassBarrenFertilityFull = new BlockGrass(BlockIDList.GRASS_BARREN_FERTILITY_FULL, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassBarrenSoil.txt");
    public static final Block torch = new BlockTorch(BlockIDList.TORCH, BlockTextureList.TORCH_TEXTURE,blockFolderPath + "torch.txt");
    public static final Block grassLowFertilityFull = new BlockGrass(BlockIDList.GRASS_LOW_FERTILITY_FULL, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassLowFertilitySoil.txt");
    public static final Block grassMediumFertilityFull = new BlockGrass(BlockIDList.GRASS_MEDIUM_FERTILITY_FULL, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassMediumFertilitySoil.txt");
    public static final Block grassHighFertilityFull = new BlockGrass(BlockIDList.GRASS_HIGH_FERTILITY_FULL, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassHighFertilitySoil.txt");
    public static final Block grassBarrenFertilityLargePatch = new BlockGrass(BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassBarrenSoil.txt");
    public static final Block barrenSoil = new BlockSoil(BlockIDList.BARREN_SOIL, BlockTextureList.SOIL_BARREN_FERTILITY_TEXTURE,blockFolderPath + "soil.txt");
    public static final Block water = new BlockWater(BlockIDList.WATER, BlockTextureList.WATER_TOP_TEXTURE, blockFolderPath + "water.txt");
    public static final Block asdfoasdfadsfa = null;
    public static final Block snow = new BlockSnow(BlockIDList.SNOW, BlockTextureList.SNOW_TEXTURE, blockFolderPath + "snow.txt");
    public static final Block asdfadfasd = null;
    public static final Block oakLog = new BlockOakLog(BlockIDList.OAK_LOG, BlockTextureList.OAK_LOG_SIDE_TEXTURE, blockFolderPath + "oakLog.txt");
    public static final Block grassLowFertilityLargePatch = new BlockGrass(BlockIDList.GRASS_LOW_FERTILITY_LARGE_PATCH, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassLowFertilitySoil.txt");
    public static final Block grassMediumFertilityLargePatch = new BlockGrass(BlockIDList.GRASS_MEDIUM_FERTILITY_LARGE_PATCH, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassMediumFertilitySoil.txt");
    public static final Block grassHighFertilityLargePatch = new BlockGrass(BlockIDList.GRASS_HIGH_FERTILITY_LARGE_PATCH, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassHighFertilitySoil.txt");
    public static final Block grassBarrenFertilitySmallPatch = new BlockGrass(BlockIDList.GRASS_BARREN_FERTILITY_SMALL_PATCH, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassBarrenSoil.txt");
    public static final Block grassLowFertilitySmallPatch = new BlockGrass(BlockIDList.GRASS_LOW_FERTILITY_SMALL_PATCH, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassLowFertilitySoil.txt");
    public static final Block grassMediumFertilitySmallPatch = new BlockGrass(BlockIDList.GRASS_MEDIUM_FERTILITY_SMALL_PATCH, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassMediumFertilitySoil.txt");
    public static final Block grassHighFertilitySmallPatch = new BlockGrass(BlockIDList.GRASS_HIGH_FERTILITY_SMALL_PATCH, BlockTextureList.GRASS_FULL_SIDE_TEXTURE, blockFolderPath + "grassHighFertilitySoil.txt");
    public static final Block lowFertilitySoil = new BlockSoil(BlockIDList.LOW_FERTILITY_SOIL, BlockTextureList.SOIL_LOW_FERTILITY_TEXTURE,blockFolderPath + "soil.txt");
    public static final Block mediumFertilitySoil = new BlockSoil(BlockIDList.MEDIUM_FERTILITY_SOIL, BlockTextureList.SOIL_MEDIUM_FERTILITY_TEXTURE,blockFolderPath + "soil.txt");
    public static final Block highFertilitySoil = new BlockSoil(BlockIDList.HIGH_FERTILITY_SOIL, BlockTextureList.SOIL_HIGH_FERTILITY_TEXTURE,blockFolderPath + "soil.txt");
    public static final Block clayWithGrassFull = new BlockClayGrass(BlockIDList.CLAY_GRASS_FULL, BlockTextureList.CLAY_TEXTURE, blockFolderPath + "clayWithGrass.txt");
    public static final Block clayWithGrassLargePatches = new BlockClayGrass(BlockIDList.CLAY_GRASS_LARGE_PATCHES, BlockTextureList.CLAY_TEXTURE, blockFolderPath + "clayWithGrass.txt");
    public static final Block clayWithGrassSmallPatches = new BlockClayGrass(BlockIDList.CLAY_GRASS_SMALL_PATCHES, BlockTextureList.CLAY_TEXTURE, blockFolderPath + "clayWithGrass.txt");
    public static final Block andesiteStone = new BlockStone(BlockIDList.ANDESITE_STONE, BlockTextureList.ANDESITE_STONE, blockFolderPath + "andesiteStone.txt");
    public static final Block andesiteGravel = new BlockSand(BlockIDList.ANDESITE_GRAVEL, BlockTextureList.ANDESITE_GRAVEL, blockFolderPath + "andesiteGravel.txt");
    public static final Block andesiteSand = new BlockSand(BlockIDList.ANDESITE_SAND, BlockTextureList.ANDESITE_SAND, blockFolderPath + "andesiteSand.txt");
    public static final Block graniteStone = new BlockStone(BlockIDList.GRANITE_STONE, BlockTextureList.GRANITE_STONE, blockFolderPath + "graniteStone.txt");
    public static final Block graniteGravel = new BlockSand(BlockIDList.GRANITE_GRAVEL, BlockTextureList.GRANITE_GRAVEL, blockFolderPath + "graniteGravel.txt");
    public static final Block graniteSand = new BlockSand(BlockIDList.GRANITE_SAND, BlockTextureList.GRANITE_SAND, blockFolderPath + "graniteSand.txt");
    public static final Block perioditeStone = new BlockStone(BlockIDList.PERIODITE_STONE, BlockTextureList.PERIODITE_STONE, blockFolderPath + "perioditeStone.txt");
    public static final Block perioditeGravel = new BlockSand(BlockIDList.PERIODITE_GRAVEL, BlockTextureList.PERIODITE_GRAVEL, blockFolderPath + "perioditeGravel.txt");
    public static final Block perioditeSand = new BlockSand(BlockIDList.PERIODITE_SAND, BlockTextureList.PERIODITE_SAND, blockFolderPath + "perioditeSand.txt");
    public static final Block obsidianStone = new BlockStone(BlockIDList.OBSIDIAN_STONE, BlockTextureList.OBSIDIAN_STONE, blockFolderPath + "obsidianStone.txt");
    public static final Block obsidianGravel = new BlockSand(BlockIDList.OBSIDIAN_GRAVEL, BlockTextureList.OBSIDIAN_GRAVEL, blockFolderPath + "obsidianGravel.txt");
    public static final Block obsidianSand = new BlockSand(BlockIDList.OBSIDIAN_SAND, BlockTextureList.OBSIDIAN_SAND, blockFolderPath + "obsidianSand.txt");
    public static final Block basaltStone = new BlockStone(BlockIDList.BASALT_STONE, BlockTextureList.BASALT_STONE, blockFolderPath + "basaltStone.txt");
    public static final Block basaltGravel = new BlockSand(BlockIDList.BASALT_GRAVEL, BlockTextureList.BASALT_GRAVEL, blockFolderPath + "basaltGravel.txt");
    public static final Block basaltSand = new BlockSand(BlockIDList.BASALT_SAND, BlockTextureList.BASALT_SAND, blockFolderPath + "basaltSand.txt");
    public static final Block gabbroStone = new BlockStone(BlockIDList.GABBRO_STONE, BlockTextureList.GABBRO_STONE, blockFolderPath + "gabbroStone.txt");
    public static final Block gabbroGravel = new BlockSand(BlockIDList.GABBRO_GRAVEL, BlockTextureList.GABBRO_GRAVEL, blockFolderPath + "gabbroGravel.txt");
    public static final Block gabbroSand = new BlockSand(BlockIDList.GABBRO_SAND, BlockTextureList.GABBRO_SAND, blockFolderPath + "gabbroSand.txt");
    public static final Block chalkStone = new BlockStone(BlockIDList.CHALK_STONE, BlockTextureList.CHALK_STONE, blockFolderPath + "chalkStone.txt");
    public static final Block chalkGravel = new BlockSand(BlockIDList.CHALK_GRAVEL, BlockTextureList.CHALK_GRAVEL, blockFolderPath + "chalkGravel.txt");
    public static final Block chalkSand = new BlockSand(BlockIDList.CHALK_SAND, BlockTextureList.CHALK_SAND, blockFolderPath + "chalkSand.txt");
    public static final Block chertStone = new BlockStone(BlockIDList.CHERT_STONE, BlockTextureList.CHERT_STONE, blockFolderPath + "chertStone.txt");
    public static final Block chertGravel = new BlockSand(BlockIDList.CHERT_GRAVEL, BlockTextureList.CHERT_GRAVEL, blockFolderPath + "chertGravel.txt");
    public static final Block chertSand = new BlockSand(BlockIDList.CHERT_SAND, BlockTextureList.CHERT_SAND, blockFolderPath + "chertSand.txt");
    public static final Block claystoneStone = new BlockStone(BlockIDList.CLAYSTONE_STONE, BlockTextureList.CLAYSTONE_STONE, blockFolderPath + "claystoneStone.txt");
    public static final Block claystoneGravel = new BlockSand(BlockIDList.CLAYSTONE_GRAVEL, BlockTextureList.CLAYSTONE_GRAVEL, blockFolderPath + "claystoneGravel.txt");
    public static final Block claystoneSand = new BlockSand(BlockIDList.CLAYSTONE_SAND, BlockTextureList.CLAYSTONE_SAND, blockFolderPath + "claystoneSand.txt");
    public static final Block conglomerateStone = new BlockStone(BlockIDList.CONGLOMERATE_STONE, BlockTextureList.CONGLOMERATE_STONE, blockFolderPath + "conglomerateStone.txt");
    public static final Block conglomerateGravel = new BlockSand(BlockIDList.CONGLOMERATE_GRAVEL, BlockTextureList.CONGLOMERATE_GRAVEL, blockFolderPath + "conglomerateGravel.txt");
    public static final Block conglomerateSand = new BlockSand(BlockIDList.CONGLOMERATE_SAND, BlockTextureList.CONGLOMERATE_SAND, blockFolderPath + "conglomerateSand.txt");
    public static final Block shaleStone = new BlockStone(BlockIDList.SHALE_STONE, BlockTextureList.SHALE_STONE_SIDE_TEXTURE, blockFolderPath + "shaleStone.txt");
    public static final Block shaleGravel = new BlockSand(BlockIDList.SHALE_GRAVEL, BlockTextureList.SHALE_GRAVEL_TEXTURE, blockFolderPath + "shaleGravel.txt");
    public static final Block shaleSand = new BlockSand(BlockIDList.SHALE_SAND, BlockTextureList.SHALE_SAND_TEXTURE, blockFolderPath + "shaleSand.txt");
    public static final Block limestoneStone = new BlockStone(BlockIDList.LIMESTONE_STONE, BlockTextureList.LIMESTONE_STONE, blockFolderPath + "limestoneStone.txt");
    public static final Block leaf = new BlockLeaf(BlockIDList.LEAF, BlockTextureList.LEAF_OPAQUE_TEXTURE,blockFolderPath + "leaf.txt"); //Leaf Erikson
    public static final Block berryBush = new BlockBerryBush(BlockIDList.BERRY_BUSH, BlockTextureList.BERRY_BUSH_SIDE_TEXTURE, blockFolderPath + "berryBush.txt");
    public static final Block limestoneGravel = new BlockSand(BlockIDList.LIMESTONE_GRAVEL, BlockTextureList.LIMESTONE_GRAVEL, blockFolderPath + "limestoneGravel.txt");
    public static final Block snowLayer = new BlockSnow(BlockIDList.SNOW_LAYER, BlockTextureList.SNOW_TEXTURE, blockFolderPath + "snowLayer.txt");
    public static final Block ice = new BlockIce(BlockIDList.ICE, BlockTextureList.ICE_TEXTURE, blockFolderPath + "ice.txt"); //ice ice baby
    public static final Block limestoneSand = new BlockSand(BlockIDList.LIMESTONE_SAND, BlockTextureList.LIMESTONE_SAND, blockFolderPath + "limestoneStone.txt");
    public static final Block sandstoneStone = new BlockStone(BlockIDList.SANDSTONE_STONE, BlockTextureList.SANDSTONE_STONE, blockFolderPath + "sandstoneStone.txt");
    public static final Block fire = new Block(BlockIDList.FIRE, BlockTextureList.FIRE_TEXTURE, blockFolderPath + "fire.txt");
    public static final Block campfire = new BlockCampFire(BlockIDList.CAMPFIRE, BlockTextureList.CAMPFIRE_BASE_TEXTURE, blockFolderPath + "campFireLit.txt", 3, 1);
    public static final Block sandstoneGravel = new BlockSand(BlockIDList.SANDSTONE_GRAVEL, BlockTextureList.SANDSTONE_GRAVEL, blockFolderPath + "sandstoneGravel.txt");
    public static final Block sandstoneSand = new BlockSand(BlockIDList.SANDSTONE_SAND, BlockTextureList.SANDSTONE_SAND, blockFolderPath + "sandstoneSand.txt");
    public static final Block clay = new BlockClay(BlockIDList.CLAY, BlockTextureList.CLAY_TEXTURE, blockFolderPath + "clay.txt");
    public static final Block itemClay = new BlockItemClay(BlockIDList.ITEM_CLAY, BlockTextureList.CLAY_TEXTURE, blockFolderPath + "itemClay.txt");
    public static final Block rawRedClayCookingPot = new Block(BlockIDList.RAW_RED_CLAY_COOKING_POT, BlockTextureList.CLAY_TEXTURE, blockFolderPath + "rawClayCookingPot.txt");
    public static final Block marbleStone = new BlockStone(BlockIDList.MARBLE_STONE, BlockTextureList.MARBLE_STONE, blockFolderPath + "marbleStone.txt");
    public static final Block marbleGravel = new BlockSand(BlockIDList.MARBLE_GRAVEL, BlockTextureList.MARBLE_GRAVEL, blockFolderPath + "marbleGravel.txt");
    public static final Block marbleSand = new BlockSand(BlockIDList.MARBLE_SAND, BlockTextureList.MARBLE_SAND, blockFolderPath + "marbleSand.txt");
    public static final Block slateStone = new BlockStone(BlockIDList.SLATE_STONE, BlockTextureList.SLATE_STONE, blockFolderPath + "slateStone.txt");
    public static final Block slateGravel = new BlockSand(BlockIDList.SLATE_GRAVEL, BlockTextureList.SLATE_GRAVEL, blockFolderPath + "slateGravel.txt");
    public static final Block slateSand = new BlockSand(BlockIDList.SLATE_SAND, BlockTextureList.SLATE_SAND, blockFolderPath + "slateSand.txt");
    public static final Block phylliteStone = new BlockStone(BlockIDList.PHYLLITE_STONE, BlockTextureList.PHYLLITE_STONE, blockFolderPath + "phylliteStone.txt");
    public static final Block phylliteGravel = new BlockSand(BlockIDList.PHYLLITE_GRAVEL, BlockTextureList.PHYLLITE_GRAVEL, blockFolderPath + "phylliteGravel.txt");
    public static final Block redClayCookingPot = new Block(BlockIDList.RED_CLAY_COOKING_POT, BlockTextureList.FIRED_RED_CLAY_TEXTURE, blockFolderPath + "redClayCookingPot.txt");
    public static final Block phylliteSand = new BlockSand(BlockIDList.PHYLLITE_SAND, BlockTextureList.PHYLLITE_SAND, blockFolderPath + "phylliteSand.txt");
    public static final Block cactus = new BlockCactus(BlockIDList.CACTUS, BlockTextureList.CACTUS_SIDE_TEXTURE, blockFolderPath + "cactus.txt");
    public static final Block asdfadf = null;
    public static final Block serpentiniteStone = new BlockStone(BlockIDList.SERPENTINITE_STONE, BlockTextureList.SERPENTINITE_STONE, blockFolderPath + "serpentiniteStone.txt");
    public static final Block itemStick = new BlockItemStick(BlockIDList.ITEM_STICK, BlockTextureList.ITEM_STICK_TEXTURE, blockFolderPath + "itemStick.txt");
    public static final Block tallGrass = new BlockTallGrass(BlockIDList.TALL_GRASS, BlockTextureList.TALL_GRASS_TEXTURE, blockFolderPath + "tallGrass.txt");
    public static final Block serpentiniteGravel = new BlockSand(BlockIDList.SERPENTINITE_GRAVEL, BlockTextureList.SERPENTINITE_GRAVEL, blockFolderPath + "serpentiniteGravel.txt");
    public static final Block fireWoodBlock = new Block(BlockIDList.FIREWOOD_BLOCK, BlockTextureList.FIREWOOD_TEXTURE, blockFolderPath + "fireWood.txt");
    public static final Block reedChest = new BlockReedChest(BlockIDList.REED_CHEST, BlockTextureList.REED_CHEST_TEXTURE, blockFolderPath + "reedChest.txt",1, 9);
    public static final Block serpentiniteSand = new BlockSand(BlockIDList.SERPENTINITE_SAND, BlockTextureList.SERPENTINITE_SAND, blockFolderPath + "serpentiniteSand.txt");
    public static final Block andesiteItemStone = new BlockItemStone(BlockIDList.ANDESITE_ITEM_STONE, BlockTextureList.ANDESITE_STONE, blockFolderPath + "andesiteItemStone.txt");
    public static final Block graniteItemStone = new BlockItemStone(BlockIDList.GRANITE_ITEM_STONE, BlockTextureList.GRANITE_STONE, blockFolderPath + "graniteItemStone.txt");
    public static final Block perioditeItemStone = new BlockItemStone(BlockIDList.PERIODITE_ITEM_STONE, BlockTextureList.PERIODITE_STONE, blockFolderPath + "perioditeItemStone.txt");
    public static final Block obsidianItemStone = new BlockItemStone(BlockIDList.OBSIDIAN_ITEM_STONE, BlockTextureList.OBSIDIAN_STONE, blockFolderPath + "obsidianItemStone.txt");
    public static final Block basaltItemStone = new BlockItemStone(BlockIDList.BASALT_ITEM_STONE, BlockTextureList.BASALT_STONE, blockFolderPath + "basaltItemStone.txt");
    public static final Block gabbroItemStone = new BlockItemStone(BlockIDList.GABBRO_ITEM_STONE, BlockTextureList.GABBRO_STONE, blockFolderPath + "gabbroItemStone.txt");
    public static final Block pitKiln = new BlockPitKiln(BlockIDList.PIT_KILN, BlockTextureList.STRAW_TEXTURE, blockFolderPath + "pitKilnLit.txt", 1,1);
    public static final Block largeFireWoodBlock = new Block(BlockIDList.LARGE_FIREWOOD_BLOCK, BlockTextureList.FIREWOOD_TEXTURE, blockFolderPath + "fireWood.txt");
    public static final Block logPile = new BlockLogPile(BlockIDList.LOG_PILE, BlockTextureList.FIREWOOD_TEXTURE, blockFolderPath + "logPile.txt", Item.fireWood.ID, 1, 1);
    public static final Block brickPile = new BlockBrickPile(BlockIDList.BRICK_PILE, BlockTextureList.CLAY_TEXTURE, blockFolderPath + "brickPile.txt", Item.rawClayAdobeBrick.ID, 1, 1);
    public static final Block itemBlock = new BlockItem(BlockIDList.ITEM_BLOCK, BlockTextureList.EMPTY_COLOR_TEXTURE, blockFolderPath + "itemBlock.txt", 1, 1);
    public static final Block adobeBrick = new Block(BlockIDList.ADOBE_BRICK, BlockTextureList.FIRED_RED_CLAY_TEXTURE, blockFolderPath + "adobeBrick.txt");
    public static final Block reedLower = new BlockReed(BlockIDList.REED_LOWER, BlockTextureList.NO_TEXTURE, blockFolderPath + "reeds.txt");
    public static final Block reedUpper = new Block(BlockIDList.REED_UPPER, BlockTextureList.NO_TEXTURE, blockFolderPath + "reedsUpper.txt");
    public static final Block chalkItemStone = new BlockItemStone(BlockIDList.CHALK_ITEM_STONE, BlockTextureList.CHALK_STONE, blockFolderPath + "chalkItemStone.txt");
    public static final Block chertItemStone = new BlockItemStone(BlockIDList.CHERT_ITEM_STONE, BlockTextureList.CHERT_STONE, blockFolderPath + "chertItemStone.txt");
    public static final Block claystoneItemStone = new BlockItemStone(BlockIDList.CLAYSTONE_ITEM_STONE, BlockTextureList.CLAYSTONE_STONE, blockFolderPath + "claystoneItemStone.txt");
    public static final Block conglomerateItemStone = new BlockItemStone(BlockIDList.CONGLOMERATE_ITEM_STONE, BlockTextureList.CONGLOMERATE_STONE, blockFolderPath + "conglomerateItemStone.txt");
    public static final Block shaleItemStone = new BlockItemStone(BlockIDList.SHALE_ITEM_STONE, BlockTextureList.SHALE_STONE_TOP_TEXTURE, blockFolderPath + "shaleItemStone.txt");
    public static final Block limestoneItemStone = new BlockItemStone(BlockIDList.LIMESTONE_ITEM_STONE, BlockTextureList.LIMESTONE_STONE, blockFolderPath + "limestoneItemStone.txt");
    public static final Block sandstoneItemStone = new BlockItemStone(BlockIDList.SANDSTONE_ITEM_STONE, BlockTextureList.SANDSTONE_STONE, blockFolderPath + "sandstoneItemStone.txt");
    public static final Block marbleItemStone = new BlockItemStone(BlockIDList.MARBLE_ITEM_STONE, BlockTextureList.MARBLE_STONE, blockFolderPath + "marbleItemStone.txt");
    public static final Block slateItemStone = new BlockItemStone(BlockIDList.SLATE_ITEM_STONE, BlockTextureList.SLATE_STONE, blockFolderPath + "slateItemStone.txt");
    public static final Block phylliteItemStone = new BlockItemStone(BlockIDList.PHYLLITE_ITEM_STONE, BlockTextureList.PHYLLITE_STONE, blockFolderPath + "phylliteItemStone.txt");
    public static final Block serpentiniteItemStone = new BlockItemStone(BlockIDList.SERPENTINITE_ITEM_STONE, BlockTextureList.SERPENTINITE_STONE, blockFolderPath + "serpentiniteItemStone.txt");
    public static final Block unused_field_93 = null;
    public static final Block treeSeed = new BlockSapling(BlockIDList.TREE_SEED, BlockTextureList.NO_TEXTURE, blockFolderPath + "treeSeed.txt");
    public static final Block sapling = new BlockSapling(BlockIDList.SAPLING, BlockTextureList.NO_TEXTURE, blockFolderPath + "sapling.txt");
    public static final Block torchUnlit = new BlockTorch(BlockIDList.TORCH_UNLIT, BlockTextureList.TORCH_UNLIT_TEXTURE, blockFolderPath + "torchUnlit.txt");
    public static final Block unused_field_72 = null;
    public static final Block unused_field_73 = null;
    public static final Block unused_field_74 = null;
    public static final Block unused_field_75 = null;
    public static final Block torchBurnedOut = new BlockTorch(BlockIDList.TORCH_BURNED_OUT, BlockTextureList.TORCH_BURNED_OUT_TEXTURE, blockFolderPath + "torchBurnedOut.txt");
    public static final Block unused_field_76 = null;
    public static final Block unused_field_77 = null;
    public static final Block unused_field_78 = null;
    public static final Block unused_field_79 = null;
    public static final Block crafting3DItem = new BlockCrafting3D(BlockIDList.CRAFTING_3D_ITEM, BlockTextureList.NO_TEXTURE, blockFolderPath + "crafting3DItem.txt");
    public static final Block primitiveCraftingTable = new BlockCraftingTable(BlockIDList.PRIMITIVE_CRAFTING_TABLE, BlockTextureList.PRIMITIVE_CRAFTING_TABLE, blockFolderPath + "primitiveCraftingTable.txt");
    public static final Block craftingItem = new BlockCrafting(BlockIDList.CRAFTING_ITEM, BlockTextureList.NO_TEXTURE, blockFolderPath + "craftingItem.txt");
    public static final Block doorPrimitiveUpper = new BlockDoor(BlockIDList.DOOR_PRIMITIVE_UPPER, BlockTextureList.PRIMITIVE_DOOR_BASE_TEXTURE, blockFolderPath + "doorPrimitive.txt"); //Contains the texture ID
    public static final Block unused_field_94 = null;
    public static final Block unused_field_95 = null;
    public static final Block unused_field_96 = null;
    public static final Block unused_field_97 = null;
    public static final Block unused_field_98 = null;
    public static final Block unused_field_99 = null;
    public static final Block unused_field_100 = null;
    public static final Block unused_field_101 = null;
    public static final Block unused_field_102 = null;
    public static final Block unused_field_103 = null;
    public static final Block unused_field_104 = null;
    public static final Block unused_field_105 = null;
    public static final Block unused_field_106 = null;
    public static final Block unused_field_107 = null;
    public static final Block unused_field_108 = null;
    public static final Block unused_field_109 = null;
    public static final Block doorPrimitiveLower = new BlockDoor(BlockIDList.DOOR_PRIMITIVE_LOWER, BlockTextureList.PRIMITIVE_DOOR_BASE_TEXTURE, blockFolderPath + "doorPrimitive.txt");
    public static final Block flowingWater = new BlockFlowingWater(BlockIDList.FLOWING_WATER, BlockTextureList.WATER_NORTH_FLOW_TEXTURE, blockFolderPath + "waterFlowing.txt");
    public static final Block unused_field_110 = null;
    public static final Block unused_field_111 = null;
    public static final Block unused_field_112 = null;
    public static final Block unused_field_113 = null;
    public static final Block unused_field_114 = null;
    public static final Block unused_field_115 = null;
    public static final Block unused_field_116 = null;
    public static final Block unused_field_117 = null;
    public static final Block unused_field_118 = null;
    public static final Block unused_field_119 = null;
    public static final Block unused_field_120 = null;
    public static final Block unused_field_121 = null;
    public static final Block unused_field_122 = null;
    public static final Block unused_field_123 = null;
    public static final Block unused_field_124 = null;
    public static final Block unused_field_125 = null;
    public static final Block unused_field_126 = null;
    public static final Block unused_field_127 = null;
    public static final Block unused_field_128 = null;
    public static final Block unused_field_129 = null;
    public static final Block unused_field_130 = null;
    public static final Block unused_field_131 = null;
    public static final Block unused_field_132 = null;
    public static final Block unused_field_133 = null;
    public static final Block unused_field_134 = null;
    public static final Block unused_field_135 = null;
    public static final Block unused_field_136 = null;
    public static final Block fullWater = new BlockFlowingWater(BlockIDList.WATER_FULL, BlockTextureList.WATER_TOP_TEXTURE, blockFolderPath + "fullWater.txt");
    public static final Block tilledSoil = new BlockTilledSoil(BlockIDList.TILLED_SOIL, BlockTextureList.SOIL_MEDIUM_FERTILITY_TEXTURE,blockFolderPath + "tilledSoil.txt");
    public static final Block cropGrowth = new BlockCrop(BlockIDList.CROP_GROWTH, BlockTextureList.NO_TEXTURE, blockFolderPath + "cropGrowth.txt");
    public static final Block deadCrop = new Block(BlockIDList.DEAD_CROP, BlockTextureList.DEAD_CROP, blockFolderPath  + "deadCrop.txt");

    public final short ID;
    public final int textureID;
    public static int facingDirection;
    public static final int[] faceOffsetX = {0, 0, -1, 1, 0, 0};
    public static final int[] faceOffsetY = {1, -1, 0, 0, 0, 0};
    public static final int[] faceOffsetZ = {0, 0, 0, 0, -1, 1};
    public static final int FACE_UP    = 0;
    public static final int FACE_DOWN  = 1;
    public static final int FACE_NORTH = 2;
    public static final int FACE_SOUTH = 3;
    public static final int FACE_EAST  = 4;
    public static final int FACE_WEST  = 5;
    public short droppedItemID = Item.block.ID;
    public short itemMetadata;
    public boolean isSolid = true;
    public boolean canBeBroken = true;
    public boolean isLightBlock;
    public byte lightBlockValue;
    public int lightColor;
    public boolean canGreedyMesh = false;
    public boolean canBurnOut;
    public ModelLoader blockModel = BlockModelList.topFaceBlockModel;
    public String blockName;
    public String stepSound = "";
    public int breakTimer = 1;
    public String toolType = "";
    public float hardness;
    public boolean requiresTool;
    public float itemDropChance = 1;
    public AxisAlignedBB standardCollisionBoundingBox = BlockAxisAlignedBBList.standardBlock;
    public String displayName = "Undefined Name";
    public boolean requireSolidBlockBelow;
    public boolean alwaysRenderFace;
    public boolean colorize;
    public boolean waterlogged;
    public ToolTipGroup[][] tooltips; //Outer array is for different states needing to return different tooltips, most will only have one object in the array

    public Block(short ID, int textureID, String filepath) {
        if (list[ID] != null) {
            throw new RuntimeException("Block ID: " + ID + " ALREADY OCCUPIED WHEN ATTEMPTING TO ADD " + this + " TO THE LIST");
        }
        list[ID] = this;
        this.ID = ID;
        this.textureID = textureID;
        this.itemMetadata = this.ID;

        File blockFile = new File(filepath);
        if(!blockFile.exists()){
            throw new RuntimeException("Missing Block File at: " + filepath);
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(blockFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String line = "";
        while(true) {
            try {
                if ((line = reader.readLine()) == null) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String[] properties = line.split(":");

            if (properties[0].equals("droppedItemID")) {
                this.droppedItemID = Short.parseShort(properties[1]);
            }

            if (properties[0].equals("itemMetadata")) {
                this.itemMetadata = Short.parseShort(properties[1]);
            }

            if (properties[0].equals("isSolid")) {
                this.isSolid = Boolean.parseBoolean(properties[1]);
            }

            if(properties[0].equals("requireSolidBlockBelow")){
                this.requireSolidBlockBelow = Boolean.parseBoolean(properties[1]);
            }

            if(properties[0].equals("alwaysRenderFace")){
                this.alwaysRenderFace = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("canBeBroken")) {
                this.canBeBroken = Boolean.parseBoolean(properties[1]);
            }

            if(properties[0].equals("waterlogged")){
                this.waterlogged = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("isLightBlock")) {
                this.isLightBlock = Boolean.parseBoolean(properties[1]);
            }

            if(properties[0].equals("displayName")){
                this.displayName = properties[1];
            }

            if (properties[0].equals("lightBlockValue")) {
                this.lightBlockValue = Byte.parseByte(properties[1]);
            }

            if(properties[0].equals("colorize")){
                this.colorize = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("lightColor")) {
                this.lightColor = Integer.parseInt(properties[1]);
            }

            if (properties[0].equals("canGreedyMesh")) {
                this.canGreedyMesh = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("canBurnOut")) {
                this.canBurnOut = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("boundingBox")) {
                switch (properties[1]){
                    case "snowLayerBB" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.snowLayerBB;
                    case "slab" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.slab;
                    case "quarterBlock" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.quarterBlock;
                    case "threeQuartersBlock" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.threeQuartersBlock;
                    case "fullBlock" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.fullBlock;
                    case "oneVoxelHighBlock" ->  this.standardCollisionBoundingBox = BlockAxisAlignedBBList.oneVoxelHighBlock;
                    case "northDoor" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.northDoor;
                    case "southDoor" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.southDoor;
                    case "eastDoor" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.eastDoor;
                    case "westDoor" -> this.standardCollisionBoundingBox = BlockAxisAlignedBBList.westDoor;
                }
            }

            if (properties[0].equals("blockModel")) {
                switch (properties[1]){
                    case "standardBlockModel" -> this.blockModel = BlockModelList.standardBlockModel;
                    case "torchBlockModel" -> this.blockModel = BlockModelList.torchBlockModel;
                    case "torchNorthBlockModel" -> this.blockModel = BlockModelList.torchNorthBlockModel;
                    case "torchSouthBlockModel" -> this.blockModel = BlockModelList.torchSouthBlockModel;
                    case "torchEastBlockModel" -> this.blockModel = BlockModelList.torchEastBlockModel;
                    case "torchWestBlockModel" -> this.blockModel = BlockModelList.torchWestBlockModel;
                    case "xCrossBlockModel" -> this.blockModel = BlockModelList.xCrossBlockModel;
                    case "topFaceBlockModel" -> this.blockModel = BlockModelList.topFaceBlockModel;
                    case "fireBlockModel" -> this.blockModel = BlockModelList.fireBlockModel;
                    case "itemStoneModel" -> this.blockModel = BlockModelList.itemStoneModel;
                    case "berryBushModel" -> this.blockModel = BlockModelList.berryBushModel;
                    case "itemStickModel" -> this.blockModel = BlockModelList.itemStickModel;
                    case "campFireBase" -> this.blockModel = BlockModelList.campFireBase;
                    case "strawChestModel" -> this.blockModel = BlockModelList.strawChestModel;
                    case "itemClayModel" -> this.blockModel = BlockModelList.itemClayModel;
                    case "clayCookingPotModel" -> this.blockModel = BlockModelList.clayCookingPotModel;
                    case "primitiveDoorUpper" -> this.blockModel = BlockModelList.primitiveDoorUpper;
                    case "reedTop" -> this.blockModel = BlockModelList.reedTop;
                    case "reedBottom" -> this.blockModel = BlockModelList.reedBottom;
                    case "leafModel" -> this.blockModel = BlockModelList.leafModel;
                    case "seedModel" -> this.blockModel = BlockModelList.seedModel;
                    case "saplingModel" -> this.blockModel = BlockModelList.saplingModel;
                    case "primitiveCraftingTable" -> this.blockModel = BlockModelList.primitiveCraftingTableModel;
                    case "tilledSoilModel" -> this.blockModel = BlockModelList.tilledSoilModel;
                    case "snowLayerModel" -> this.blockModel = BlockModelList.snowLayerModel;

                    case "waterDefault" -> this.blockModel = BlockModelList. waterDefault;
                    case "waterFlowNorth1" -> this.blockModel = BlockModelList.waterFlowNorth1;
                    case "waterFlowNorth2" -> this.blockModel = BlockModelList.waterFlowNorth2;
                    case "waterFlowNorth3" -> this.blockModel = BlockModelList.waterFlowNorth3;
                    case "waterFlowNorth4" -> this.blockModel = BlockModelList.waterFlowNorth4;
                    case "waterFlowNorth5" -> this.blockModel = BlockModelList.waterFlowNorth5;
                    case "waterFlowNorth6" -> this.blockModel = BlockModelList.waterFlowNorth6;
                    case "waterFlowNorth7" -> this.blockModel = BlockModelList.waterFlowNorth7;

                    case "waterFlowSouth1" -> this.blockModel = BlockModelList.waterFlowSouth1;
                    case "waterFlowSouth2" -> this.blockModel = BlockModelList.waterFlowSouth2;
                    case "waterFlowSouth3" -> this.blockModel = BlockModelList.waterFlowSouth3;
                    case "waterFlowSouth4" -> this.blockModel = BlockModelList.waterFlowSouth4;
                    case "waterFlowSouth5" -> this.blockModel = BlockModelList.waterFlowSouth5;
                    case "waterFlowSouth6" -> this.blockModel = BlockModelList.waterFlowSouth6;
                    case "waterFlowSouth7" -> this.blockModel = BlockModelList.waterFlowSouth7;

                    case "waterFlowEast1" -> this.blockModel = BlockModelList.waterFlowEast1;
                    case "waterFlowEast2" -> this.blockModel = BlockModelList.waterFlowEast2;
                    case "waterFlowEast3" -> this.blockModel = BlockModelList.waterFlowEast3;
                    case "waterFlowEast4" -> this.blockModel = BlockModelList.waterFlowEast4;
                    case "waterFlowEast5" -> this.blockModel = BlockModelList.waterFlowEast5;
                    case "waterFlowEast6" -> this.blockModel = BlockModelList.waterFlowEast6;
                    case "waterFlowEast7" -> this.blockModel = BlockModelList.waterFlowEast7;

                    case "waterFlowWest1" -> this.blockModel = BlockModelList.waterFlowWest1;
                    case "waterFlowWest2" -> this.blockModel = BlockModelList.waterFlowWest2;
                    case "waterFlowWest3" -> this.blockModel = BlockModelList.waterFlowWest3;
                    case "waterFlowWest4" -> this.blockModel = BlockModelList.waterFlowWest4;
                    case "waterFlowWest5" -> this.blockModel = BlockModelList.waterFlowWest5;
                    case "waterFlowWest6" -> this.blockModel = BlockModelList.waterFlowWest6;
                    case "waterFlowWest7" -> this.blockModel = BlockModelList.waterFlowWest7;

                    case "size2VoxelModel" -> this.blockModel = BlockModelList.size2VoxelModel;
                    case "size15NormalModel" -> this.blockModel = BlockModelList.size15NormalModel;
                    case "size14NormalModel" -> this.blockModel = BlockModelList.size14NormalModel;
                    case "size13NormalModel" -> this.blockModel = BlockModelList.size13NormalModel;
                    case "size12NormalModel" -> this.blockModel = BlockModelList.size12NormalModel;
                    case "size11NormalModel" -> this.blockModel = BlockModelList.size11NormalModel;
                    case "size10NormalModel" -> this.blockModel = BlockModelList.size10NormalModel;
                    case "size9NormalModel" -> this.blockModel = BlockModelList.size9NormalModel;
                    case "size8NormalModel" -> this.blockModel = BlockModelList.size8NormalModel;
                    case "size7NormalModel" -> this.blockModel = BlockModelList.size7NormalModel;
                    case "size6NormalModel" -> this.blockModel = BlockModelList.size6NormalModel;
                    case "size5NormalModel" -> this.blockModel = BlockModelList.size5NormalModel;
                    case "size4NormalModel" -> this.blockModel = BlockModelList.size4NormalModel;
                    case "size3NormalModel" -> this.blockModel = BlockModelList.size3NormalModel;
                    case "size2NormalModel" -> this.blockModel = BlockModelList.size2NormalModel;
                    case "size1NormalModel" -> this.blockModel = BlockModelList.size1NormalModel;
                    case "size15NorthSouthModel" -> this.blockModel = BlockModelList.size15NorthSouthModel;
                    case "size14NorthSouthModel" -> this.blockModel = BlockModelList.size14NorthSouthModel;
                    case "size13NorthSouthModel" -> this.blockModel = BlockModelList.size13NorthSouthModel;
                    case "size12NorthSouthModel" -> this.blockModel = BlockModelList.size12NorthSouthModel;
                    case "size11NorthSouthModel" -> this.blockModel = BlockModelList.size11NorthSouthModel;
                    case "size10NorthSouthModel" -> this.blockModel = BlockModelList.size10NorthSouthModel;
                    case "size9NorthSouthModel" -> this.blockModel = BlockModelList.size9NorthSouthModel;
                    case "size8NorthSouthModel" -> this.blockModel = BlockModelList.size8NorthSouthModel;
                    case "size7NorthSouthModel" -> this.blockModel = BlockModelList.size7NorthSouthModel;
                    case "size6NorthSouthModel" -> this.blockModel = BlockModelList.size6NorthSouthModel;
                    case "size5NorthSouthModel" -> this.blockModel = BlockModelList.size5NorthSouthModel;
                    case "size4NorthSouthModel" -> this.blockModel = BlockModelList.size4NorthSouthModel;
                    case "size3NorthSouthModel" -> this.blockModel = BlockModelList.size3NorthSouthModel;
                    case "size2NorthSouthModel" -> this.blockModel = BlockModelList.size2NorthSouthModel;
                    case "size1NorthSouthModel" -> this.blockModel = BlockModelList.size1NorthSouthModel;
                    case "size15EastWestModel" -> this.blockModel = BlockModelList.size15EastWestModel;
                    case "size14EastWestModel" -> this.blockModel = BlockModelList.size14EastWestModel;
                    case "size13EastWestModel" -> this.blockModel = BlockModelList.size13EastWestModel;
                    case "size12EastWestModel" -> this.blockModel = BlockModelList.size12EastWestModel;
                    case "size11EastWestModel" -> this.blockModel = BlockModelList.size11EastWestModel;
                    case "size10EastWestModel" -> this.blockModel = BlockModelList.size10EastWestModel;
                    case "size9EastWestModel" -> this.blockModel = BlockModelList.size9EastWestModel;
                    case "size8EastWestModel" -> this.blockModel = BlockModelList.size8EastWestModel;
                    case "size7EastWestModel" -> this.blockModel = BlockModelList.size7EastWestModel;
                    case "size6EastWestModel" -> this.blockModel = BlockModelList.size6EastWestModel;
                    case "size5EastWestModel" -> this.blockModel = BlockModelList.size5EastWestModel;
                    case "size4EastWestModel" -> this.blockModel = BlockModelList.size4EastWestModel;
                    case "size3EastWestModel" -> this.blockModel = BlockModelList.size3EastWestModel;
                    case "size2EastWestModel" -> this.blockModel = BlockModelList.size2EastWestModel;
                    case "size1EastWestModel" -> this.blockModel = BlockModelList.size1EastWestModel;
                }
            }

            if (properties[0].equals("blockName")) {
                this.blockName = properties[1];
            }

            if (properties[0].equals("stepSound")) {
                switch (properties[1]) {
                    case "grass" -> this.stepSound = Sound.grass;
                    case "sand" -> this.stepSound = Sound.sand;
                    case "soil" -> this.stepSound = Sound.dirt;
                    case "stone" -> this.stepSound = Sound.stone;
                    case "waterSplash" -> this.stepSound = Sound.waterSplash;
                    case "snow" -> this.stepSound = Sound.snow;
                    case "wood" -> this.stepSound = Sound.wood;
                    case "itemPickup" -> this.stepSound = Sound.itemPickup;
                    case "fallDamage" -> this.stepSound = Sound.fallDamage;
                    case "clay" -> this.stepSound = Sound.clay;
                    case "ice" -> this.stepSound = Sound.ice;
                    case "gravel" -> this.stepSound = Sound.gravel;
                }
            }

            if (properties[0].equals("breakTimer")) {
                this.breakTimer = Integer.parseInt(properties[1]);
            }

            if (properties[0].equals("toolType")) {
                this.toolType = properties[1];
            }

            if (properties[0].equals("hardness")) {
                this.hardness = Float.parseFloat(properties[1]);
            }

            if (properties[0].equals("requiresTool")) {
                this.requiresTool = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("itemDropChance")) {
                this.itemDropChance = Float.parseFloat(properties[1]);
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getBlockTexture(short block, int x, int y, int z, int face) {
        return list[block].getBlockTexture(x,y,z, face);
    }

    public int getBlockTexture(int x, int y, int z, int face) {
        return this.textureID;
    }


    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();

        //This is too generic to put in its own class
        if(world.isBlockAbleToBecomePitKiln(this, x, y, z) && playerHeldItem == Item.straw.ID && MouseListener.rightClickReleased && world.isBlockSuitableForPitKiln(x,y,z)){
            Inventory kilnInventory = new Inventory(1,1);
            kilnInventory.addItemToInventory(world.pitKilnItemType(this.ID, x,y,z), world.getBlockID(x,y,z), world.pitKilnItemCount(this.ID, x,y,z), Item.NULL_ITEM_DURABILITY, 0, null);
            if(this.ID == Block.brickPile.ID){
                world.clearChestLocation(x,y,z);
            }
            world.addBlockState(x,y,z, MultiState.CHEST_STATE, new ChestLocation(Chunk.getBlockIndexFromCoordinates(x,y,z),  kilnInventory, world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5)));
            world.addBlockState(x,y,z, MultiState.PIT_KILN_STATE, new PitKilnState(0,0, false, Chunk.getBlockIndexFromCoordinates(x,y,z)));
            world.setBlockWithNotify(x,y,z, Block.pitKiln.ID, false);
            player.removeItemFromInventory();
            MouseListener.rightClickReleased = false;
        }
    }

    public String getDisplayName(int x, int y, int z){
        return this.displayName;
    }

    protected void handleSpecialLeftClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        short playerHeldItem = player.getHeldItem();
        if(playerHeldItem != Item.NULL_ITEM_REFERENCE) {
            if (this.ID == tallGrass.ID && Item.list[playerHeldItem] instanceof ItemKnife) {
                world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.straw.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0, null));
            }
        }
        if(this.ID == clay.ID){
            int extraClay = CosmicEvolution.globalRand.nextInt(2,4);
            for(int i = 0; i < extraClay; i++){
                world.addEntity(new EntityBlock(x + 0.5, y + 0.5, z + 0.5, Block.itemClay.ID, (byte)1));
            }
        }

        if(world.getBlockID(x,y,z) == Block.reedUpper.ID){
            world.addTimeEvent(x,y - 1,z, CosmicEvolution.instance.save.time + ((ITimeUpdate)reedLower).getUpdateTime(x,y,z,world));
        }
    }

    public void onLeftClickWithNoSpecialFunctions(int x, int y, int z, World world, EntityPlayer player){
        if (!this.canBeBroken) {return;}
        world.setBlockWithNotify(x, y, z, Block.air.ID, true);
        this.clearBlockStates(x,y,z, world);

        if(this instanceof ITickable){
            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeTickableBlockFromArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
        }
    }
    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player) {
        if (!this.canBeBroken) {return;}
        this.handleSpecialLeftClickFunctions(x,y,z,world,player);

        short blockID = world.getBlockID(x,y,z);

        if (list[blockID].droppedItemID != Item.NULL_ITEM_REFERENCE) {
            if (list[blockID].droppedItemID != Item.block.ID) {
                if (list[blockID].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                    world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].droppedItemID, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[list[blockID].droppedItemID].durability, 0, null));
                }
            } else {
                if (list[blockID].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                    world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityBlock(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].itemMetadata, (byte) 1));
                }
            }
        }


        short currentBlockID = world.getBlockID(x,y,z);
        world.setBlockWithNotify(x, y, z, list[currentBlockID].waterlogged || list[currentBlockID] instanceof BlockIce ? water.ID :  air.ID, true);
        this.clearBlockStates(x,y,z, world);


        if(this.isSolid) {
            int blockX = 0;
            int blockY = 0;
            int blockZ = 0;
            if (list[world.getBlockID(x, y + 1, z)].requireSolidBlockBelow) {
                blockID = world.getBlockID(x, y + 1, z);
                blockX = MathUtil.floorDouble(x);
                blockY = MathUtil.floorDouble(y + 1);
                blockZ = MathUtil.floorDouble(z);
                list[blockID].onLeftClickWithNoSpecialFunctions(x, y + 1, z, world, player);
            }

            if (world.getBlockID(x - 1, y, z) == torch.ID) {
                blockID = world.getBlockID(x - 1, y, z);
                blockX = MathUtil.floorDouble(x - 1);
                blockY = MathUtil.floorDouble(y);
                blockZ = MathUtil.floorDouble(z);
                torch.onLeftClickWithNoSpecialFunctions(x - 1, y, z, world, player);
            }

            if (world.getBlockID(x + 1, y, z) == torch.ID) {
                blockID = world.getBlockID(x + 1, y, z);
                blockX = MathUtil.floorDouble(x + 1);
                blockY = MathUtil.floorDouble(y);
                blockZ = MathUtil.floorDouble(z);
                torch.onLeftClickWithNoSpecialFunctions(x + 1, y, z, world, player);
            }

            if (world.getBlockID(x, y, z - 1) == torch.ID) {
                blockID = world.getBlockID(x, y, z - 1);
                blockX = MathUtil.floorDouble(x);
                blockY = MathUtil.floorDouble(y);
                blockZ = MathUtil.floorDouble(z - 1);
                torch.onLeftClickWithNoSpecialFunctions(x, y, z - 1, world, player);
            }

            if (world.getBlockID(x, y, z + 1) == torch.ID) {
                blockID = world.getBlockID(x, y, z + 1);
                blockX = MathUtil.floorDouble(x);
                blockY = MathUtil.floorDouble(y);
                blockZ = MathUtil.floorDouble(z + 1);
                torch.onLeftClickWithNoSpecialFunctions(x, y, z + 1, world, player);
            }

            if (blockID == torch.ID || list[blockID].requireSolidBlockBelow) {
                if (list[blockID].droppedItemID != Item.NULL_ITEM_REFERENCE) {
                    if (list[blockID].droppedItemID != Item.block.ID) {
                        if (list[blockID].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                            world.findChunkFromChunkCoordinates(blockX >> 5, blockY >> 5, blockZ >> 5).addEntityToList(new EntityItem(blockX + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), blockY + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), blockZ + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].droppedItemID, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[list[blockID].droppedItemID].durability, 0, null));
                        }
                    } else {
                        if (list[blockID].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                            world.findChunkFromChunkCoordinates(blockX >> 5, blockY >> 5, blockZ >> 5).addEntityToList(new EntityBlock(blockX + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), blockY + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), blockZ + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].itemMetadata, (byte) 1));
                        }
                    }
                }
            }
        }


        if(this instanceof ITimeUpdate){
            world.removeTimeEvent(x,y,z);
        }

        if(this instanceof ITickable){
            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).removeTickableBlockFromArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
        }

        world.generateParticlesOnBlockBreak(blockID, x, y, z);

        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(this.getStepSound(x,y,z), false, 1f), new Random().nextFloat(0.6F, 1));
        player.reduceHeldItemDurability();
    }

    public void onRightClick(int x, int y, int z, World world, EntityPlayer player) {
        Chunk chunk = world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if (chunk.blocks == null) {chunk.initChunk();}
        if (chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)] != air.ID && chunk.blocks[Chunk.getBlockIndexFromCoordinates(x,y,z)] != snowLayer.ID && !(Block.list[chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)]] instanceof BlockWater)) {return;}
        if (!MouseListener.rightClickReleased)return;

        short heldItem = player.getHeldItem(); //Block all items that cannot be placed on the ground
        if (heldItem == Item.NULL_ITEM_REFERENCE || (!Item.list[heldItem].canPlaceOnGround && !Item.list[heldItem].canPlaceAsItemBlock))return;


        short heldBlock = 0;
        if (player.isHoldingBlock()) {
            heldBlock = player.getHeldBlock();
        }

        if((heldBlock == torch.ID || heldBlock == torchUnlit.ID) && facingDirection == FACE_DOWN)return;

        if(Item.list[heldItem].canPlaceAsItemBlock && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) && list[world.getBlockID(x, y - 1, z)].isSolid){
            heldBlock = itemBlock.ID;
        } else if(Item.list[heldItem].canPlaceAsItemBlock){
            return;
        }

        switch (Item.list[heldItem].itemName) { //Convert held item into an equivalent block id to place, if one exists, otherwise default to the held block
            case "STRAW" -> {
                heldBlock = campfire.ID;
            }
            case "FIRE_WOOD" -> {
                if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && KeyListener.keyReleased[GLFW.GLFW_KEY_LEFT_SHIFT] && world.getBlockID(player.blockLookingAt[0], player.blockLookingAt[1], player.blockLookingAt[2]) != logPile.ID) {;
                    heldBlock = logPile.ID;
                    player.removeItemFromInventory();
                    KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
                } else {
                    return;
                }
            }
            case "RAW_CLAY_ADOBE_BRICK", "CLAY_ADOBE_BRICK" ->{
                if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && KeyListener.keyReleased[GLFW.GLFW_KEY_LEFT_SHIFT] && world.getBlockID(player.blockLookingAt[0], player.blockLookingAt[1], player.blockLookingAt[2]) != logPile.ID) {;
                    heldBlock = brickPile.ID;
                    KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
                } else {
                    return;
                }
            }
            case "BERRY_SEED" -> {
                if(list[world.getBlockID(x, y - 1, z)] instanceof BlockSoil || list[world.getBlockID(x, y - 1, z)] instanceof BlockGrass) {
                    heldBlock = berryBush.ID;
                } else {
                    return;
                };
            }
            case "REED_SEED" -> {
                if(list[world.getBlockID(x, y - 1, z)].isSolid && (list[world.getBlockID(x, y - 1, z)] instanceof BlockSand || list[world.getBlockID(x, y - 1, z)] instanceof BlockSoil || list[world.getBlockID(x, y - 1, z)] instanceof BlockGrass) && world.getBlockID(x,y,z) == water.ID && world.getBlockID(x, y + 1, z) == air.ID){
                    heldBlock = reedLower.ID;
                } else {
                    return;
                }
            }
            case "TREE_SEED" -> {
                if(list[world.getBlockID(x, y - 1, z)] instanceof BlockSoil || list[world.getBlockID(x, y - 1, z)] instanceof BlockGrass) {
                    heldBlock = treeSeed.ID;
                } else {
                    return;
                }
            }
            case "SEED_WILD_GRASS", "SEED_EINKORN_WHEAT", "SEED_EMMER_WHEAT", "SEED_WHEAT" -> {
                if(list[world.getBlockID(x, y - 1, z)] instanceof BlockTilledSoil){
                    heldBlock = cropGrowth.ID;
                } else {
                    return;
                }
            }
            case "DOOR_PRIMITIVE" -> {
                heldBlock = doorPrimitiveLower.ID;
                DoorState newDoorState = null;
                int newDoorStateKey = Chunk.getBlockIndexFromCoordinates(x,y,z);
                switch (player.getPlayerCardinalFaceDirection()){
                    case "North" -> {
                        DoorState eastDoorState = (DoorState) world.getBlockState(x, y, z - 1, MultiState.DOOR_STATE);
                        DoorState westDoorState = (DoorState) world.getBlockState(x, y, z + 1, MultiState.DOOR_STATE);

                        if(eastDoorState != null && eastDoorState.hingeRight && eastDoorState.facingDirection == DoorState.FACE_DIRECTION_SOUTH){
                            eastDoorState.hingeLeft = true;
                            eastDoorState.hingeRight = false;
                            eastDoorState.isOpen = false;
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_SOUTH, false, false, true, newDoorStateKey);
                        } else if(westDoorState != null && westDoorState.hingeRight && westDoorState.facingDirection == DoorState.FACE_DIRECTION_SOUTH){
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_SOUTH, false, true, false, newDoorStateKey);
                        } else {
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_SOUTH, false, false, true, newDoorStateKey);
                        }
                    }
                    case "South" -> {
                        DoorState eastDoorState = (DoorState) world.getBlockState(x, y, z - 1, MultiState.DOOR_STATE);
                        DoorState westDoorState = (DoorState) world.getBlockState(x, y, z + 1, MultiState.DOOR_STATE);

                        if(eastDoorState != null && eastDoorState.hingeRight && eastDoorState.facingDirection == DoorState.FACE_DIRECTION_NORTH){
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_NORTH, false, true, false, newDoorStateKey);
                        } else if(westDoorState != null && westDoorState.hingeRight && westDoorState.facingDirection == DoorState.FACE_DIRECTION_NORTH){
                            westDoorState.hingeLeft = true;
                            westDoorState.hingeRight = false;
                            westDoorState.isOpen = false;
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_NORTH, false, false, true, newDoorStateKey);
                        } else {
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_NORTH, false, false, true, newDoorStateKey);
                        }
                    }
                    case "East" -> {
                        DoorState northDoorState = (DoorState) world.getBlockState(x - 1, y, z, MultiState.DOOR_STATE);
                        DoorState southDoorState = (DoorState) world.getBlockState(x + 1, y, z, MultiState.DOOR_STATE);

                        if(southDoorState != null && southDoorState.hingeRight && southDoorState.facingDirection == DoorState.FACE_DIRECTION_WEST){
                            southDoorState.hingeLeft = true;
                            southDoorState.hingeRight = false;
                            southDoorState.isOpen = false;
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_WEST, false, false, true, newDoorStateKey);
                        } else if(northDoorState != null && northDoorState.hingeRight && northDoorState.facingDirection == DoorState.FACE_DIRECTION_WEST){
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_WEST, false, true, false, newDoorStateKey);
                        } else {
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_WEST, false, false, true, newDoorStateKey);
                        }
                    }
                    case "West" -> {
                        DoorState northDoorState = (DoorState) world.getBlockState(x - 1, y, z, MultiState.DOOR_STATE);
                        DoorState southDoorState = (DoorState) world.getBlockState(x + 1, y, z, MultiState.DOOR_STATE);

                        if(southDoorState != null && southDoorState.hingeRight && southDoorState.facingDirection == DoorState.FACE_DIRECTION_EAST){
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_EAST, false, true, false, newDoorStateKey);
                        } else if(northDoorState != null && northDoorState.hingeRight && northDoorState.facingDirection == DoorState.FACE_DIRECTION_EAST){
                            northDoorState.hingeLeft = true;
                            northDoorState.hingeRight = false;
                            northDoorState.isOpen = false;
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_EAST, false, true, false, newDoorStateKey);
                        } else {
                            newDoorState = new DoorState(DoorState.FACE_DIRECTION_EAST, false, false, true, newDoorStateKey);
                        }
                    }

                }
                world.addBlockState(x,y,z, MultiState.DOOR_STATE, newDoorState);
                world.setBlockWithNotify(x, y + 1, z, Block.doorPrimitiveUpper.ID, true);
            }
        }

        if(list[heldBlock].requireSolidBlockBelow){
            if(!list[world.getBlockID(x,y - 1, z)].isSolid){
                return;
            }
        }


        Block.list[heldBlock].addBlockStates(x,y,z, world, player, chunk);

        if(Block.list[heldBlock] instanceof ITimeUpdate){
            chunk.addTimeUpdateEvent(x,y,z, CosmicEvolution.instance.save.time + ((ITimeUpdate) Block.list[heldBlock]).getUpdateTime(x,y,z, world));
        }


        if(Block.list[heldBlock] instanceof ITickable){
            chunk.addTickableBlockToArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
        }


        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(list[player.getHeldBlock()].stepSound, false, 1f), new Random().nextFloat(0.6F, 1));
        player.removeItemFromInventory();
        world.setBlockWithNotify(x, y, z, heldBlock, true);
        player.isSwinging = true;
    }

    public boolean isColorized(int x, int y, int z, World world){
        return this.colorize;
    }

    public boolean isLightBlock(int x, int y, int z, World world){
        return this.isLightBlock;
    }

    public ModelLoader getBlockModel(int x, int y ,int z, World world){
        return this.blockModel;
    }

    public void addBlockStates(int x, int y, int z, World world, EntityPlayer player, Chunk chunk){

    }

    public void clearBlockStates(int x, int y, int z, World world){
        world.clearAllBlockStates(x,y,z);
    }

    public void adjustBoundingBox(int x, int y, int z, AxisAlignedBB boundingBox){
        boundingBox.minX = x + this.standardCollisionBoundingBox.minX;
        boundingBox.maxX = x + this.standardCollisionBoundingBox.maxX;
        boundingBox.minY = y + this.standardCollisionBoundingBox.minY;
        boundingBox.maxY = y + this.standardCollisionBoundingBox.maxY;
        boundingBox.minZ = z + this.standardCollisionBoundingBox.minZ;
        boundingBox.maxZ = z + this.standardCollisionBoundingBox.maxZ;
    }

    public String getStepSound(int x, int y, int z){
        return this.stepSound;
    }


    //Return a tooltip, subsequent tooltips in the array will be rendered below the main tooltip, alternative tooltips can be set on the main tooltip object and will cycle once
    //every second


    public void registerBlockTooltips(){}


    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player) {
        return null;
    }


    public void onBlockUpdate(int x, int y, int z, World world){}

    public static void registerAllBlockTooltips(){
        for(int i = 0; i < list.length; i++){
            if(list[i] == null)continue;

            list[i].registerBlockTooltips();
        }
    }
    public int getDynamicBreakTimer(){
        short playerHeldItem = CosmicEvolution.instance.save.thePlayer.getHeldItem();
        if(playerHeldItem == -1){playerHeldItem = 0;}
        if(!this.requiresTool || !Item.list[playerHeldItem].toolType.equals(this.toolType)){
            return this.breakTimer;
        }
        float percentage = 1 - Item.list[playerHeldItem].hardness;
        if(percentage < 0){percentage = 0.01f;}
        return (int) (percentage * this.breakTimer);
    }

}
