package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.*;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.item.ItemKnife;
import spacegame.render.ModelLoader;
import spacegame.util.MathUtil;
import spacegame.world.AxisAlignedBB;
import spacegame.world.Chunk;
import spacegame.world.World;

import java.io.*;
import java.util.Random;

public class Block {
    public static final ModelLoader standardBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/standardBlock.obj");
    public static final ModelLoader torchBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/torch.obj");
    public static final ModelLoader torchNorthBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/torchNorth.obj");
    public static final ModelLoader torchSouthBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/torchSouth.obj");
    public static final ModelLoader torchEastBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/torchEast.obj");
    public static final ModelLoader torchWestBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/torchWest.obj");
    public static final ModelLoader xCrossBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/xCrossBlock.obj");
    public static final ModelLoader topFaceBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/topFaceBlock.obj");
    public static final ModelLoader fireBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/fire.obj");
    public static final ModelLoader itemStoneModel = new ModelLoader("src/spacegame/assets/models/blockModels/itemStone.obj");
    public static final ModelLoader berryBushModel = new ModelLoader("src/spacegame/assets/models/blockModels/berryBush.obj");
    public static final ModelLoader itemStickModel = new ModelLoader("src/spacegame/assets/models/blockModels/itemStick.obj");
    public static final ModelLoader campFireBase = new ModelLoader("src/spacegame/assets/models/blockModels/campFireBase.obj");
    public static final ModelLoader fireWood = new ModelLoader("src/spacegame/assets/models/blockModels/fireWood.obj");
    public static final ModelLoader strawChestBuild0 = new ModelLoader("src/spacegame/assets/models/blockModels/strawChestBuild0.obj");
    public static final ModelLoader strawChestBuild1 = new ModelLoader("src/spacegame/assets/models/blockModels/strawChestBuild1.obj");
    public static final ModelLoader strawChestModel = new ModelLoader("src/spacegame/assets/models/blockModels/strawChest.obj");
    public static final ModelLoader itemClayModel = new ModelLoader("src/spacegame/assets/models/blockModels/itemClay.obj");
    public static final ModelLoader clayCookingPotModel = new ModelLoader("src/spacegame/assets/models/blockModels/clayCookingPot.obj");
    public static final ModelLoader largeFireWood = new ModelLoader("src/spacegame/assets/models/blockModels/largeFireWood.obj");
    public static final ModelLoader brick = new ModelLoader("src/spacegame/assets/models/blockModels/brick.obj");
    public static final ModelLoader quarterBlockModel = new ModelLoader("src/spacegame/assets/models/blockModels/quarterBlock.obj");
    public static final ModelLoader itemVoxelModel = new ModelLoader("src/spacegame/assets/models/blockModels/itemVoxel.obj");
    public static final ModelLoader size15NormalModel = standardBlockModel.alterStandardBlockModel(1,0,1);
    public static final ModelLoader size14NormalModel = standardBlockModel.alterStandardBlockModel(2,0,2);
    public static final ModelLoader size13NormalModel = standardBlockModel.alterStandardBlockModel(3,0,3);
    public static final ModelLoader size12NormalModel = standardBlockModel.alterStandardBlockModel(4,0,4);
    public static final ModelLoader size11NormalModel = standardBlockModel.alterStandardBlockModel(5,0,5);
    public static final ModelLoader size10NormalModel = standardBlockModel.alterStandardBlockModel(6,0,6);
    public static final ModelLoader size9NormalModel = standardBlockModel.alterStandardBlockModel(7,0,7);
    public static final ModelLoader size8NormalModel = standardBlockModel.alterStandardBlockModel(8,0,8);
    public static final ModelLoader size7NormalModel = standardBlockModel.alterStandardBlockModel(9,0,9);
    public static final ModelLoader size6NormalModel = standardBlockModel.alterStandardBlockModel(10,0,10);
    public static final ModelLoader size5NormalModel = standardBlockModel.alterStandardBlockModel(11,0,11);
    public static final ModelLoader size4NormalModel = standardBlockModel.alterStandardBlockModel(12,0,12);
    public static final ModelLoader size3NormalModel = standardBlockModel.alterStandardBlockModel(13,0,13);
    public static final ModelLoader size2NormalModel = standardBlockModel.alterStandardBlockModel(14,0,14);
    public static final ModelLoader size1NormalModel = standardBlockModel.alterStandardBlockModel(15,0,15);
    public static final ModelLoader size15NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,1,1);
    public static final ModelLoader size14NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,2,2);
    public static final ModelLoader size13NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,3,3);
    public static final ModelLoader size12NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,4,4);
    public static final ModelLoader size11NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,5,5);
    public static final ModelLoader size10NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,6,6);
    public static final ModelLoader size9NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,7,7);
    public static final ModelLoader size8NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,8,8);
    public static final ModelLoader size7NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,9,9);
    public static final ModelLoader size6NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,10,10);
    public static final ModelLoader size5NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,11,11);
    public static final ModelLoader size4NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,12,12);
    public static final ModelLoader size3NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,13,13);
    public static final ModelLoader size2NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,14,14);
    public static final ModelLoader size1NorthSouthModel = standardBlockModel.alterStandardBlockModel(0,15,15);
    public static final ModelLoader size15EastWestModel = standardBlockModel.alterStandardBlockModel(1,1,0);
    public static final ModelLoader size14EastWestModel = standardBlockModel.alterStandardBlockModel(2,2,0);
    public static final ModelLoader size13EastWestModel = standardBlockModel.alterStandardBlockModel(3,3,0);
    public static final ModelLoader size12EastWestModel = standardBlockModel.alterStandardBlockModel(4,4,0);
    public static final ModelLoader size11EastWestModel = standardBlockModel.alterStandardBlockModel(5,5,0);
    public static final ModelLoader size10EastWestModel = standardBlockModel.alterStandardBlockModel(6,6,0);
    public static final ModelLoader size9EastWestModel = standardBlockModel.alterStandardBlockModel(7,7,0);
    public static final ModelLoader size8EastWestModel = standardBlockModel.alterStandardBlockModel(8,8,0);
    public static final ModelLoader size7EastWestModel = standardBlockModel.alterStandardBlockModel(9,9,0);
    public static final ModelLoader size6EastWestModel = standardBlockModel.alterStandardBlockModel(10,10,0);
    public static final ModelLoader size5EastWestModel = standardBlockModel.alterStandardBlockModel(11,11,0);
    public static final ModelLoader size4EastWestModel = standardBlockModel.alterStandardBlockModel(12,12,0);
    public static final ModelLoader size3EastWestModel = standardBlockModel.alterStandardBlockModel(13,13,0);
    public static final ModelLoader size2EastWestModel = standardBlockModel.alterStandardBlockModel(14,14,0);
    public static final ModelLoader size1EastWestModel = standardBlockModel.alterStandardBlockModel(15,15,0);
    public static final ModelLoader size2VoxelModel = itemVoxelModel.copyModel().getScaledModel(2).translateModel(0.46875f, 0, 0.46875f);
    public static final AxisAlignedBB standardBlock = new AxisAlignedBB(0,0,0,1,1,1);
    public static final AxisAlignedBB fullBlock = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    public static final AxisAlignedBB itemStoneBoundingBox = new AxisAlignedBB(0,0,0,1,0.125,1);
    public static final AxisAlignedBB quarterBlock = new AxisAlignedBB(0, 0, 0, 1, 0.25f, 1);
    public static final AxisAlignedBB slab = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
    public static final AxisAlignedBB threeQuartersBlock = new AxisAlignedBB(0, 0, 0, 1, 0.75f, 1);
    public static final Block[] list = new Block[Short.MAX_VALUE];
    public static final Block air = new Block((short) 0, -1, "src/spacegame/assets/blockFiles/air.txt");
    public static final Block grass = new BlockGrass((short) 1, 2, "src/spacegame/assets/blockFiles/grass.txt");
    public static final Block torchStandard = new BlockTorch((short) 2, 3,"src/spacegame/assets/blockFiles/torchStandard.txt");
    public static final Block torchNorth = new BlockTorch((short) 3, 3,"src/spacegame/assets/blockFiles/torchNorth.txt");
    public static final Block torchSouth = new BlockTorch((short) 4, 3,"src/spacegame/assets/blockFiles/torchSouth.txt");
    public static final Block torchEast = new BlockTorch((short) 5, 3,"src/spacegame/assets/blockFiles/torchEast.txt");
    public static final Block torchWest = new BlockTorch((short) 6, 3,"src/spacegame/assets/blockFiles/torchWest.txt");
    public static final Block dirt = new BlockDirt((short) 7, 1,"src/spacegame/assets/blockFiles/dirt.txt");
    public static final Block water = new BlockWater((short) 8, 4, "src/spacegame/assets/blockFiles/water.txt");
    public static final Block sand = new Block((short) 9, 5, "src/spacegame/assets/blockFiles/sand.txt"); //I don't like sand
    public static final Block snow = new Block((short) 10, 6, "src/spacegame/assets/blockFiles/snow.txt");
    public static final Block stone = new Block((short) 11, 7, "src/spacegame/assets/blockFiles/stone.txt");
    public static final Block oakLogFullSizeNormal = new BlockLog((short) 12, 8, "src/spacegame/assets/blockFiles/oakLogFullSizeNormal.txt");
    public static final Block oakLogSize15Normal = new BlockLog((short) 13, 8, "src/spacegame/assets/blockFiles/oakLogSize15Normal.txt");
    public static final Block oakLogSize14Normal = new BlockLog((short) 14, 8, "src/spacegame/assets/blockFiles/oakLogSize14Normal.txt");
    public static final Block oakLogSize13Normal = new BlockLog((short) 15, 8,"src/spacegame/assets/blockFiles/oakLogSize13Normal.txt");
    public static final Block oakLogSize12Normal = new BlockLog((short) 16, 8,"src/spacegame/assets/blockFiles/oakLogSize12Normal.txt");
    public static final Block oakLogSize11Normal = new BlockLog((short) 17, 8,"src/spacegame/assets/blockFiles/oakLogSize11Normal.txt");
    public static final Block oakLogSize10Normal = new BlockLog((short) 18, 8, "src/spacegame/assets/blockFiles/oakLogSize10Normal.txt");
    public static final Block oakLogSize9Normal = new BlockLog((short) 19, 8,"src/spacegame/assets/blockFiles/oakLogSize9Normal.txt");
    public static final Block oakLogSize8Normal = new BlockLog((short) 20, 8,"src/spacegame/assets/blockFiles/oakLogSize8Normal.txt");
    public static final Block oakLogSize7Normal = new BlockLog((short) 21, 8,"src/spacegame/assets/blockFiles/oakLogSize7Normal.txt");
    public static final Block oakLogSize6Normal = new BlockLog((short) 22, 8,"src/spacegame/assets/blockFiles/oakLogSize6Normal.txt");
    public static final Block oakLogSize5Normal = new BlockLog((short) 23, 8,"src/spacegame/assets/blockFiles/oakLogSize5Normal.txt");
    public static final Block oakLogSize4Normal = new BlockLog((short) 24, 8,"src/spacegame/assets/blockFiles/oakLogSize4Normal.txt");
    public static final Block oakLogSize3Normal = new BlockLog((short) 25, 8,"src/spacegame/assets/blockFiles/oakLogSize3Normal.txt");
    public static final Block oakLogSize2Normal = new BlockLog((short) 26, 8,"src/spacegame/assets/blockFiles/oakLogSize2Normal.txt");
    public static final Block oakLogSize1Normal = new BlockLog((short) 27, 8,"src/spacegame/assets/blockFiles/oakLogSize1Normal.txt");
    public static final Block oakLogFullSizeNorthSouth = new BlockLog((short) 28, 8,"src/spacegame/assets/blockFiles/oakLogFullSizeNormal.txt");
    public static final Block oakLogSize15NorthSouth = new BlockLog((short) 29, 8, "src/spacegame/assets/blockFiles/oakLogSize15NorthSouth.txt");
    public static final Block oakLogSize14NorthSouth = new BlockLog((short) 30, 8,"src/spacegame/assets/blockFiles/oakLogSize14NorthSouth.txt");
    public static final Block oakLogSize13NorthSouth = new BlockLog((short) 31, 8,"src/spacegame/assets/blockFiles/oakLogSize13NorthSouth.txt");
    public static final Block oakLogSize12NorthSouth = new BlockLog((short) 32, 8,"src/spacegame/assets/blockFiles/oakLogSize12NorthSouth.txt");
    public static final Block oakLogSize11NorthSouth = new BlockLog((short) 33, 8,"src/spacegame/assets/blockFiles/oakLogSize11NorthSouth.txt");
    public static final Block oakLogSize10NorthSouth = new BlockLog((short) 34, 8,"src/spacegame/assets/blockFiles/oakLogSize10NorthSouth.txt");
    public static final Block oakLogSize9NorthSouth = new BlockLog((short) 35, 8,"src/spacegame/assets/blockFiles/oakLogSize9NorthSouth.txt");
    public static final Block oakLogSize8NorthSouth = new BlockLog((short) 36, 8,"src/spacegame/assets/blockFiles/oakLogSize8NorthSouth.txt");
    public static final Block oakLogSize7NorthSouth = new BlockLog((short) 37, 8,"src/spacegame/assets/blockFiles/oakLogSize7NorthSouth.txt");
    public static final Block oakLogSize6NorthSouth = new BlockLog((short) 38, 8,"src/spacegame/assets/blockFiles/oakLogSize6NorthSouth.txt");
    public static final Block oakLogSize5NorthSouth = new BlockLog((short) 39, 8,"src/spacegame/assets/blockFiles/oakLogSize5NorthSouth.txt");
    public static final Block oakLogSize4NorthSouth = new BlockLog((short) 40, 8,"src/spacegame/assets/blockFiles/oakLogSize4NorthSouth.txt");
    public static final Block oakLogSize3NorthSouth = new BlockLog((short) 41, 8,"src/spacegame/assets/blockFiles/oakLogSize3NorthSouth.txt");
    public static final Block oakLogSize2NorthSouth = new BlockLog((short) 42, 8,"src/spacegame/assets/blockFiles/oakLogSize2NorthSouth.txt");
    public static final Block oakLogSize1NorthSouth = new BlockLog((short) 43, 8,"src/spacegame/assets/blockFiles/oakLogSize1NorthSouth.txt");
    public static final Block oakLogFullSizeEastWest = new BlockLog((short) 44, 8,"src/spacegame/assets/blockFiles/oakLogFullSizeNormal.txt");
    public static final Block oakLogSize15EastWest = new BlockLog((short) 45, 8,"src/spacegame/assets/blockFiles/oakLogSize15EastWest.txt");
    public static final Block oakLogSize14EastWest = new BlockLog((short) 46, 8,"src/spacegame/assets/blockFiles/oakLogSize14EastWest.txt");
    public static final Block oakLogSize13EastWest = new BlockLog((short) 47, 8,"src/spacegame/assets/blockFiles/oakLogSize13EastWest.txt");
    public static final Block oakLogSize12EastWest = new BlockLog((short) 48, 8,"src/spacegame/assets/blockFiles/oakLogSize12EastWest.txt");
    public static final Block oakLogSize11EastWest = new BlockLog((short) 49, 8,"src/spacegame/assets/blockFiles/oakLogSize11EastWest.txt");
    public static final Block oakLogSize10EastWest = new BlockLog((short) 50, 8,"src/spacegame/assets/blockFiles/oakLogSize10EastWest.txt");
    public static final Block oakLogSize9EastWest = new BlockLog((short) 51, 8,"src/spacegame/assets/blockFiles/oakLogSize9EastWest.txt");
    public static final Block oakLogSize8EastWest = new BlockLog((short) 52, 8,"src/spacegame/assets/blockFiles/oakLogSize8EastWest.txt");
    public static final Block oakLogSize7EastWest = new BlockLog((short) 53, 8,"src/spacegame/assets/blockFiles/oakLogSize7EastWest.txt");
    public static final Block oakLogSize6EastWest = new BlockLog((short) 54, 8,"src/spacegame/assets/blockFiles/oakLogSize6EastWest.txt");
    public static final Block oakLogSize5EastWest = new BlockLog((short) 55, 8,"src/spacegame/assets/blockFiles/oakLogSize5EastWest.txt");
    public static final Block oakLogSize4EastWest = new BlockLog((short) 56, 8,"src/spacegame/assets/blockFiles/oakLogSize4EastWest.txt");
    public static final Block oakLogSize3EastWest = new BlockLog((short) 57, 8,"src/spacegame/assets/blockFiles/oakLogSize3EastWest.txt");
    public static final Block oakLogSize2EastWest = new BlockLog((short) 58, 8,"src/spacegame/assets/blockFiles/oakLogSize2EastWest.txt");
    public static final Block oakLogSize1EastWest = new BlockLog((short) 59, 8, "src/spacegame/assets/blockFiles/oakLogSize2EastWest.txt");
    public static final Block leaf = new BlockLeaf((short) 60, 10,"src/spacegame/assets/blockFiles/leaf.txt"); //Leaf Erikson
    public static final Block berryBush = new BlockBerryBush((short) 61, 25, "src/spacegame/assets/blockFiles/berryBush.txt");
    public static final Block berryBushNoBerries = new BlockBerryBush((short) 62, 11, "src/spacegame/assets/blockFiles/berryBush.txt");
    public static final Block campFire1FireWood = new BlockCampFireUnlit((short) 63, 16, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block campFire2FireWood = new BlockCampFireUnlit((short) 64, 16, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block campFire3Firewood = new BlockCampFireUnlit((short) 65, 16, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block campFireNoFirewood = new BlockCampFireUnlit((short) 66, 16, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block fire = new Block((short)67, 18, "src/spacegame/assets/blockFiles/fire.txt");
    public static final Block campfireLit = new BlockCampFireLit((short) 68, 16, "src/spacegame/assets/blockFiles/campFireLit.txt");
    public static final Block grassWithClay = new BlockGrassWithClay((short)69, 2, "src/spacegame/assets/blockFiles/grassWithClay.txt"); //Nice
    public static final Block grassBlockWithClayLower = new Block((short)70, 14, "src/spacegame/assets/blockFiles/grassBlockWithClayLower.txt");
    public static final Block clay = new BlockClay((short)71, 13, "src/spacegame/assets/blockFiles/clay.txt");
    public static final Block itemClay = new BlockItemClay((short)72, 13, "src/spacegame/assets/blockFiles/itemClay.txt");
    public static final Block rawRedClayCookingPot = new Block((short)73, 13, "src/spacegame/assets/blockFiles/rawClayCookingPot.txt");
    public static final Block pitKilnUnlit1 = new BlockPitKilnUnlit((short)74, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit2 = new BlockPitKilnUnlit((short)75, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit3 = new BlockPitKilnUnlit((short)76, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit4 = new BlockPitKilnUnlit((short)77, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit5 = new BlockPitKilnUnlit((short)78, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit6 = new BlockPitKilnUnlit((short)79, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit7 = new BlockPitKilnUnlit((short)80, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit8 = new BlockPitKilnUnlit((short)81, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block redClayCookingPot = new Block((short)82, 17, "src/spacegame/assets/blockFiles/redClayCookingPot.txt");
    public static final Block grassBlockLower = new Block((short)83, 20, "src/spacegame/assets/blockFiles/grassBlockLower.txt");
    public static final Block cactus = new BlockCactus((short)84, 21, "src/spacegame/assets/blockFiles/cactus.txt");
    public static final Block itemStone = new BlockItemStone((short)85, stone.textureID, "src/spacegame/assets/blockFiles/itemStone.txt");
    public static final Block berryBushFlower = new BlockBerryBush((short)86, 27, "src/spacegame/assets/blockFiles/berryBush.txt");
    public static final Block itemStick = new BlockItemStick((short)87, 29, "src/spacegame/assets/blockFiles/itemStick.txt");
    public static final Block tallGrass = new Block((short)88, 30, "src/spacegame/assets/blockFiles/tallGrass.txt");
    public static final Block campFire4FireWood = new BlockCampFireUnlit((short) 89, 16, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block fireWoodBlock = new Block((short)90, 31, "src/spacegame/assets/blockFiles/fireWood.txt");
    public static final Block reedChest = new BlockReedChest((short)91, 32, "src/spacegame/assets/blockFiles/reedChest.txt",1, 9);
    public static final Block reedChestTier0 = new BlockReedCrafting((short)92, 32, "src/spacegame/assets/blockFiles/reedChestBuilding0.txt");
    public static final Block reedBasketTier0 = new BlockReedCrafting((short)93, 32, "src/spacegame/assets/blockFiles/reedBasketBuilding.txt");
    public static final Block reedChestTier1 = new BlockReedCrafting((short)94, 32, "src/spacegame/assets/blockFiles/reedChestBuilding1.txt");
    public static final Block pitKilnUnlitLog1 = new BlockPitKilnUnlit((short)95, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlitLog2 = new BlockPitKilnUnlit((short)96, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlitLog3 = new BlockPitKilnUnlit((short)97, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit = new BlockPitKilnUnlit((short)98, 15, "src/spacegame/assets/blockFiles/pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnLit = new BlockPitKilnLit((short)99, 15, "src/spacegame/assets/blockFiles/pitKilnLit.txt", 1,1);
    public static final Block largeFireWoodBlock = new Block((short)100, 31, "src/spacegame/assets/blockFiles/fireWood.txt");
    public static final Block logPile = new BlockLogPile((short)101, 31, "src/spacegame/assets/blockFiles/logPile.txt", Item.fireWood.ID, 1, 1);
    public static final Block brickPile = new BlockBrickPile((short)102, 13, "src/spacegame/assets/blockFiles/brickPile.txt", Item.rawClayAdobeBrick.ID, 1, 1);
    public static final Block itemBlock = new BlockItem((short)103, 19, "src/spacegame/assets/blockFiles/itemBlock.txt", 1, 1);
    public static final Block adobeBrick = new Block((short)104, 17, "src/spacegame/assets/blockFiles/adobeBrick.txt");
    public static final Block reedLower = new BlockReed((short)105, 33, "src/spacegame/assets/blockFiles/reeds.txt");
    public static final Block reedUpper = new Block((short)106, 34, "src/spacegame/assets/blockFiles/reedsUpper.txt");
    public static final Block berrySeed = new BlockBerryBushGrowing((short)107, 35, "src/spacegame/assets/blockFiles/berrySeed.txt");
    public static final Block berryBushGrowth1 = new BlockBerryBushGrowing((short)108, 11, "src/spacegame/assets/blockFiles/berryBushGrowth.txt");
    public static final Block berryBushGrowth2 = new BlockBerryBushGrowing((short)109, 11, "src/spacegame/assets/blockFiles/berryBushGrowth.txt");
    public static final Block berryBushGrowth3 = new BlockBerryBushGrowing((short)110, 11, "src/spacegame/assets/blockFiles/berryBushGrowth.txt");
    public static final Block berryBushGrowth4 = new BlockBerryBushGrowing((short)111, 11, "src/spacegame/assets/blockFiles/berryBushGrowth.txt");
    public static final Block berryBushGrowth5 = new BlockBerryBushGrowing((short)112, 11, "src/spacegame/assets/blockFiles/berryBushGrowth.txt");
    public static final Block reedSeed = new BlockReedGrowing((short)113, 35, "src/spacegame/assets/blockFiles/reedSeed.txt");
    public static final Block reedGrowth1 = new BlockReedGrowing((short)114, 36, "src/spacegame/assets/blockFiles/reedGrowth.txt");
    public static final Block reedGrowth2 = new BlockReedGrowing((short)115, 36, "src/spacegame/assets/blockFiles/reedGrowth.txt");
    public static final Block reedGrowth3 = new BlockReedGrowing((short)116, 36, "src/spacegame/assets/blockFiles/reedGrowth.txt");
    public static final Block reedGrowth4 = new BlockReedGrowing((short)117, 36, "src/spacegame/assets/blockFiles/reedGrowth.txt");
    public static final Block reedGrowth5 = new BlockReedGrowing((short)118, 36, "src/spacegame/assets/blockFiles/reedGrowth.txt");
    public static final Block treeSeed = new BlockSapling((short)119, 35, "src/spacegame/assets/blockFiles/treeSeed.txt");
    public static final Block sapling = new BlockSapling((short)120, 37, "src/spacegame/assets/blockFiles/sapling.txt");
    public final short ID;
    public final int textureID;
    public static int facingDirection;
    public short droppedItemID = Item.block.ID;
    public short itemMetadata;
    public boolean isSolid = true;
    public boolean canBeBroken = true;
    public boolean isLightBlock;
    public byte lightBlockValue;
    public int lightColor;
    public boolean canGreedyMesh = true;
    public boolean canBurnOut;
    public ModelLoader blockModel = topFaceBlockModel;
    public String blockName;
    public String stepSound = "";
    public int breakTimer = 1;
    public String toolType = "";
    public float hardness;
    public boolean requiresTool;
    public float itemDropChance = 1;
    public AxisAlignedBB standardCollisionBoundingBox = standardBlock;
    public String displayName = "Undefined Name";
    public boolean requireSolidBlockBelow;
    public boolean alwaysRenderFace;
    public boolean colorize;
    public boolean waterlogged;

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
                    case "itemStoneBoundingBox" -> this.standardCollisionBoundingBox = itemStoneBoundingBox;
                    case "slab" -> this.standardCollisionBoundingBox = slab;
                    case "quarterBlock" -> this.standardCollisionBoundingBox = quarterBlock;
                    case "threeQuartersBlock" -> this.standardCollisionBoundingBox = threeQuartersBlock;
                    case "fullBlock" -> this.standardCollisionBoundingBox = fullBlock;
                }
            }

            if (properties[0].equals("blockModel")) {
                switch (properties[1]){
                    case "standardBlockModel" -> this.blockModel = standardBlockModel;
                    case "torchBlockModel" -> this.blockModel = torchBlockModel;
                    case "torchNorthBlockModel" -> this.blockModel = torchNorthBlockModel;
                    case "torchSouthBlockModel" -> this.blockModel = torchSouthBlockModel;
                    case "torchEastBlockModel" -> this.blockModel = torchEastBlockModel;
                    case "torchWestBlockModel" -> this.blockModel = torchWestBlockModel;
                    case "xCrossBlockModel" -> this.blockModel = xCrossBlockModel;
                    case "topFaceBlockModel" -> this.blockModel = topFaceBlockModel;
                    case "fireBlockModel" -> this.blockModel = fireBlockModel;
                    case "itemStoneModel" -> this.blockModel = itemStoneModel;
                    case "berryBushModel" -> this.blockModel = berryBushModel;
                    case "itemStickModel" -> this.blockModel = itemStickModel;
                    case "campFireBase" -> this.blockModel = campFireBase;
                    case "strawChestBuild0" -> this.blockModel = strawChestBuild0;
                    case "strawChestBuild1" -> this.blockModel = strawChestBuild1;
                    case "strawChestModel" -> this.blockModel = strawChestModel;
                    case "itemClayModel" -> this.blockModel = itemClayModel;
                    case "clayCookingPotModel" -> this.blockModel = clayCookingPotModel;
                    case "size2VoxelModel" -> this.blockModel = size2VoxelModel;
                    case "size15NormalModel" -> this.blockModel = size15NormalModel;
                    case "size14NormalModel" -> this.blockModel = size14NormalModel;
                    case "size13NormalModel" -> this.blockModel = size13NormalModel;
                    case "size12NormalModel" -> this.blockModel = size12NormalModel;
                    case "size11NormalModel" -> this.blockModel = size11NormalModel;
                    case "size10NormalModel" -> this.blockModel = size10NormalModel;
                    case "size9NormalModel" -> this.blockModel = size9NormalModel;
                    case "size8NormalModel" -> this.blockModel = size8NormalModel;
                    case "size7NormalModel" -> this.blockModel = size7NormalModel;
                    case "size6NormalModel" -> this.blockModel = size6NormalModel;
                    case "size5NormalModel" -> this.blockModel = size5NormalModel;
                    case "size4NormalModel" -> this.blockModel = size4NormalModel;
                    case "size3NormalModel" -> this.blockModel = size3NormalModel;
                    case "size2NormalModel" -> this.blockModel = size2NormalModel;
                    case "size1NormalModel" -> this.blockModel = size1NormalModel;
                    case "size15NorthSouthModel" -> this.blockModel = size15NorthSouthModel;
                    case "size14NorthSouthModel" -> this.blockModel = size14NorthSouthModel;
                    case "size13NorthSouthModel" -> this.blockModel = size13NorthSouthModel;
                    case "size12NorthSouthModel" -> this.blockModel = size12NorthSouthModel;
                    case "size11NorthSouthModel" -> this.blockModel = size11NorthSouthModel;
                    case "size10NorthSouthModel" -> this.blockModel = size10NorthSouthModel;
                    case "size9NorthSouthModel" -> this.blockModel = size9NorthSouthModel;
                    case "size8NorthSouthModel" -> this.blockModel = size8NorthSouthModel;
                    case "size7NorthSouthModel" -> this.blockModel = size7NorthSouthModel;
                    case "size6NorthSouthModel" -> this.blockModel = size6NorthSouthModel;
                    case "size5NorthSouthModel" -> this.blockModel = size5NorthSouthModel;
                    case "size4NorthSouthModel" -> this.blockModel = size4NorthSouthModel;
                    case "size3NorthSouthModel" -> this.blockModel = size3NorthSouthModel;
                    case "size2NorthSouthModel" -> this.blockModel = size2NorthSouthModel;
                    case "size1NorthSouthModel" -> this.blockModel = size1NorthSouthModel;
                    case "size15EastWestModel" -> this.blockModel = size15EastWestModel;
                    case "size14EastWestModel" -> this.blockModel = size14EastWestModel;
                    case "size13EastWestModel" -> this.blockModel = size13EastWestModel;
                    case "size12EastWestModel" -> this.blockModel = size12EastWestModel;
                    case "size11EastWestModel" -> this.blockModel = size11EastWestModel;
                    case "size10EastWestModel" -> this.blockModel = size10EastWestModel;
                    case "size9EastWestModel" -> this.blockModel = size9EastWestModel;
                    case "size8EastWestModel" -> this.blockModel = size8EastWestModel;
                    case "size7EastWestModel" -> this.blockModel = size7EastWestModel;
                    case "size6EastWestModel" -> this.blockModel = size6EastWestModel;
                    case "size5EastWestModel" -> this.blockModel = size5EastWestModel;
                    case "size4EastWestModel" -> this.blockModel = size4EastWestModel;
                    case "size3EastWestModel" -> this.blockModel = size3EastWestModel;
                    case "size2EastWestModel" -> this.blockModel = size2EastWestModel;
                    case "size1EastWestModel" -> this.blockModel = size1EastWestModel;
                }
            }

            if (properties[0].equals("blockName")) {
                this.blockName = properties[1];
            }

            if (properties[0].equals("stepSound")) {
                switch (properties[1]) {
                    case "grass" -> this.stepSound = Sound.grass;
                    case "sand" -> this.stepSound = Sound.sand;
                    case "dirt" -> this.stepSound = Sound.dirt;
                    case "stone" -> this.stepSound = Sound.stone;
                    case "waterSplash" -> this.stepSound = Sound.waterSplash;
                    case "snow" -> this.stepSound = Sound.snow;
                    case "wood" -> this.stepSound = Sound.wood;
                    case "itemPickup" -> this.stepSound = Sound.itemPickup;
                    case "fallDamage" -> this.stepSound = Sound.fallDamage;
                    case "clay" -> this.stepSound = Sound.clay;
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

    public int getBlockTexture(short block, int face) {
        return list[block].getBlockTexture(face);
    }

    public int getBlockTexture(int face) {
        return this.textureID;
    }

    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();

        //This is too generic to put in its own class
        if(world.isBlockAbleToBecomePitKiln(this, x, y, z) && playerHeldItem == Item.straw.ID && MouseListener.rightClickReleased && world.isBlockSuitableForPitKiln(x,y,z)){
            Inventory kilnInventory = new Inventory(1,1);
            kilnInventory.addItemToInventory(world.pitKilnItemType(this.ID, x,y,z), world.getBlockID(x,y,z), world.pitKilnItemCount(this.ID, x,y,z), Item.NULL_ITEM_DURABILITY);
            if(this.ID == Block.brickPile.ID){
                world.clearChestLocation(x,y,z);
            }
            world.addChestLocation(x,y,z, kilnInventory);
            world.setBlockWithNotify(x,y,z, Block.pitKilnUnlit1.ID, false);
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
                world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.straw.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
            }
        }
        if(this.ID == grassWithClay.ID || this.ID == clay.ID){
            int extraClay = CosmicEvolution.globalRand.nextInt(2,4);
            for(int i = 0; i < extraClay; i++){
                world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.clay.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
            }
        }

        if(world.getBlockID(x,y,z) == Block.reedUpper.ID){
            world.addTimeEvent(x,y - 1,z, CosmicEvolution.instance.save.time + ((ITimeUpdate)reedLower).getUpdateTime());
        }
    }

    public void onLeftClickWithNoSpecialFunctions(int x, int y, int z, World world, EntityPlayer player){
        if (!this.canBeBroken) {return;}
        world.setBlockWithNotify(x, y, z, Block.air.ID, true);

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
                    world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].droppedItemID, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[list[blockID].droppedItemID].durability));
                }
            } else {
                if (list[blockID].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                    world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityBlock(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].itemMetadata, (byte) 1));
                }
            }
        }

        world.setBlockWithNotify(x, y, z, list[world.getBlockID(x,y,z)].waterlogged ? water.ID : air.ID, true);


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

            if (world.getBlockID(x - 1, y, z) == torchNorth.ID) {
                blockID = world.getBlockID(x - 1, y, z);
                blockX = MathUtil.floorDouble(x - 1);
                blockY = MathUtil.floorDouble(y);
                blockZ = MathUtil.floorDouble(z);
                torchNorth.onLeftClickWithNoSpecialFunctions(x - 1, y, z, world, player);
            }

            if (world.getBlockID(x + 1, y, z) == torchSouth.ID) {
                blockID = world.getBlockID(x + 1, y, z);
                blockX = MathUtil.floorDouble(x + 1);
                blockY = MathUtil.floorDouble(y);
                blockZ = MathUtil.floorDouble(z);
                torchSouth.onLeftClickWithNoSpecialFunctions(x + 1, y, z, world, player);
            }

            if (world.getBlockID(x, y, z - 1) == torchEast.ID) {
                blockID = world.getBlockID(x, y, z - 1);
                blockX = MathUtil.floorDouble(x);
                blockY = MathUtil.floorDouble(y);
                blockZ = MathUtil.floorDouble(z - 1);
                torchEast.onLeftClickWithNoSpecialFunctions(x, y, z - 1, world, player);
            }

            if (world.getBlockID(x, y, z + 1) == torchWest.ID) {
                blockID = world.getBlockID(x, y, z + 1);
                blockX = MathUtil.floorDouble(x);
                blockY = MathUtil.floorDouble(y);
                blockZ = MathUtil.floorDouble(z + 1);
                torchWest.onLeftClickWithNoSpecialFunctions(x, y, z + 1, world, player);
            }

            if (blockID == torchNorth.ID || blockID == torchSouth.ID || blockID == torchEast.ID || blockID == torchWest.ID || list[blockID].requireSolidBlockBelow) {
                if (list[blockID].droppedItemID != Item.NULL_ITEM_REFERENCE) {
                    if (list[blockID].droppedItemID != Item.block.ID) {
                        if (list[blockID].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                            world.findChunkFromChunkCoordinates(blockX >> 5, blockY >> 5, blockZ >> 5).addEntityToList(new EntityItem(blockX + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), blockY + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), blockZ + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].droppedItemID, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[list[blockID].droppedItemID].durability));
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

        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(this.getStepSound(x,y,z), false, 1f), new Random().nextFloat(0.6F, 1));
        player.reduceHeldItemDurability();
    }

    public void onRightClick(int x, int y, int z, World world, EntityPlayer player) {
        Chunk chunk = world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if (chunk.blocks == null) {chunk.initChunk();}
        if (chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)] != air.ID && chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)] != water.ID) {return;}
        if (!MouseListener.rightClickReleased)return;
        if(world.wouldBlockIntersectPlayer(x,y,z))return;

        short heldItem = player.getHeldItem(); //Block all items that cannot be placed on the ground
        if (heldItem == Item.NULL_ITEM_REFERENCE || (!Item.list[heldItem].canPlaceOnGround && !Item.list[heldItem].canPlaceAsItemBlock))return;

        short heldBlock = 0;
        if (player.isHoldingBlock()) {
            heldBlock = player.getHeldBlock();
        } else if (heldItem == Item.torch.ID) {
            if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))return;
            heldBlock = switch (facingDirection) {
                case 1 -> Block.torchNorth.ID;
                case 2 -> Block.torchSouth.ID;
                case 3 -> Block.torchEast.ID;
                case 4 -> Block.torchWest.ID;
                default -> Block.torchStandard.ID;
            };
        }

        if(Item.list[heldItem].canPlaceAsItemBlock && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))){
            heldBlock = itemBlock.ID;
        } else if(Item.list[heldItem].canPlaceAsItemBlock){
            return;
        }

        switch (Item.list[heldItem].itemName) { //Convert held item into an equivalent block id to place, if one exists, otherwise default to the held block
            case "RAW_STONE" -> heldBlock = itemStone.ID;
            case "RAW_STICK" -> heldBlock = itemStick.ID;
            case "CLAY" -> heldBlock = itemClay.ID;
            case "STRAW" -> heldBlock = campFireNoFirewood.ID;
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
                if(list[world.getBlockID(x, y - 1, z)].ID == grass.ID || list[world.getBlockID(x, y - 1, z)].ID == dirt.ID) {
                    heldBlock = berrySeed.ID;
                } else {
                    return;
                };
            }
            case "REED_SEED" -> {
                if(list[world.getBlockID(x, y - 1, z)].isSolid && (world.getBlockID(x, y - 1, z) == sand.ID || world.getBlockID(x, y - 1, z) == dirt.ID) && world.getBlockID(x,y,z) == water.ID && world.getBlockID(x, y + 1, z) == air.ID){
                    heldBlock = reedSeed.ID;
                } else {
                    return;
                }
            }
            case "TREE_SEED" -> {
                if(world.getBlockID(x, y - 1, z) == grass.ID || world.getBlockID(x, y - 1, z) == dirt.ID) {
                    heldBlock = treeSeed.ID;
                } else {
                    return;
                }
            }
        };

        if(Block.list[heldBlock] instanceof ITimeUpdate){
            chunk.addTimeUpdateEvent(x,y,z, CosmicEvolution.instance.save.time + ((ITimeUpdate) Block.list[heldBlock]).getUpdateTime());
        }


        if(Block.list[heldBlock] instanceof ITickable){
            chunk.addTickableBlockToArray((short) Chunk.getBlockIndexFromCoordinates(x,y,z));
        }

        if(Block.list[heldBlock] instanceof BlockContainer){
            chunk.addChestLocation(x,y,z, new Inventory(((BlockContainer)(Block.list[heldBlock])).inventoryWidth, ((BlockContainer)(Block.list[heldBlock])).inventoryHeight));
            if(heldBlock == logPile.ID){
                chunk.getChestLocation(x,y,z).inventory.itemStacks[0].count = 2;
            }
            if(heldBlock == brickPile.ID ){
                chunk.getChestLocation(x,y,z).inventory.itemStacks[0].count = 1;
                chunk.getChestLocation(x,y,z).inventory.itemStacks[0].item = Item.list[heldItem];
            }
            if(heldBlock == itemBlock.ID){
                chunk.getChestLocation(x,y,z).inventory.itemStacks[0].count = 1;
                chunk.getChestLocation(x,y,z).inventory.itemStacks[0].item = Item.list[heldItem];
                chunk.getChestLocation(x,y,z).inventory.itemStacks[0].metadata = player.getHeldBlock();
                chunk.getChestLocation(x,y,z).inventory.itemStacks[0].durability = player.getHeldItemDurability();
            }
        }

        CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(list[player.getHeldBlock()].stepSound, false, 1f), new Random().nextFloat(0.6F, 1));
        player.removeItemFromInventory();
        world.setBlockWithNotify(x, y, z, heldBlock, true);
        player.isSwinging = true;
    }


    public static int getRandomTickRate(){
        return 60;
    }

    public String getStepSound(int x, int y, int z){
        return this.stepSound;
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
