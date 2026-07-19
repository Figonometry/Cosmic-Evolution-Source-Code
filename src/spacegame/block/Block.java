package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiMutateCrop;
import spacegame.item.Inventory;
import spacegame.item.Item;
import spacegame.item.ItemKnife;
import spacegame.item.ItemSeed;
import spacegame.item.itemstate.SeedState;
import spacegame.render.model.ModelLoader;
import spacegame.util.MathUtil;
import spacegame.world.AxisAlignedBB;
import spacegame.world.Chunk;
import spacegame.world.World;
import spacegame.world.blockstate.CampfireState;
import spacegame.world.blockstate.Crop;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.TilledSoilState;

import java.io.*;
import java.util.Random;

public class Block {
    public static final int NULL_BLOCK_REFERENCE = -1;
    public static final String modelFolderPath = "src/spacegame/assets/models/blockModels/";
    public static final String blockFolderPath = "src/spacegame/assets/blockFiles/";
    public static final ModelLoader standardBlockModel = new ModelLoader(modelFolderPath + "standardBlock.obj", false);
    public static final ModelLoader torchBlockModel = new ModelLoader(modelFolderPath + "torch.obj", false);
    public static final ModelLoader torchNorthBlockModel = new ModelLoader(modelFolderPath + "torchNorth.obj", false);
    public static final ModelLoader torchSouthBlockModel = new ModelLoader(modelFolderPath + "torchSouth.obj", false);
    public static final ModelLoader torchEastBlockModel = new ModelLoader(modelFolderPath + "torchEast.obj", false);
    public static final ModelLoader torchWestBlockModel = new ModelLoader(modelFolderPath + "torchWest.obj", false);
    public static final ModelLoader xCrossBlockModel = new ModelLoader(modelFolderPath + "xCrossBlock.obj", false);
    public static final ModelLoader topFaceBlockModel = new ModelLoader(modelFolderPath + "topFaceBlock.obj", false);
    public static final ModelLoader fireBlockModel = new ModelLoader(modelFolderPath + "fire.obj", false);
    public static final ModelLoader itemStoneModel = new ModelLoader(modelFolderPath + "itemStone.obj", false);
    public static final ModelLoader berryBushModel = new ModelLoader(modelFolderPath + "berryBush.obj", false);
    public static final ModelLoader itemStickModel = new ModelLoader(modelFolderPath + "itemStick.obj", false);
    public static final ModelLoader campFireBase = new ModelLoader(modelFolderPath + "campFireBase.obj", false);
    public static final ModelLoader fireWood = new ModelLoader(modelFolderPath + "fireWood.obj", false);
    public static final ModelLoader campFireStick1 = new ModelLoader(modelFolderPath + "campFireStick1.obj", true);
    public static final ModelLoader campFireStick2 = new ModelLoader(modelFolderPath + "campFireStick2.obj", true);
    public static final ModelLoader campFireStick3 = new ModelLoader(modelFolderPath + "campFireStick3.obj", true);
    public static final ModelLoader campFireStick4 = new ModelLoader(modelFolderPath + "campFireStick4.obj", true);
    public static final ModelLoader campFireStickFull = new ModelLoader(modelFolderPath + "campFireStickFull.obj", true);
    public static final ModelLoader strawChestModel = new ModelLoader(modelFolderPath + "strawChest.obj", false);
    public static final ModelLoader itemClayModel = new ModelLoader(modelFolderPath + "itemClay.obj", false);
    public static final ModelLoader clayCookingPotModel = new ModelLoader(modelFolderPath + "clayCookingPot.obj", false);
    public static final ModelLoader largeFireWood = new ModelLoader(modelFolderPath + "largeFireWood.obj", false);
    public static final ModelLoader brick = new ModelLoader(modelFolderPath + "brick.obj", false);
    public static final ModelLoader quarterBlockModel = new ModelLoader(modelFolderPath + "quarterBlock.obj", false);
    public static final ModelLoader itemVoxelModel = new ModelLoader(modelFolderPath + "itemVoxel.obj", false);
    public static final ModelLoader crafting3DItemVoxelModel = new ModelLoader(modelFolderPath + "crafting3DVoxel.obj", false);
    public static final ModelLoader centeredVoxel = new ModelLoader(modelFolderPath + "centeredVoxel.obj", false).scaleModel(0.5f);
    public static final ModelLoader primitiveDoorUpper = new ModelLoader(modelFolderPath + "primitiveDoorUpper.obj", true);
    public static final ModelLoader primitiveDoorLower = new ModelLoader(modelFolderPath + "primitiveDoorLower.obj", true);
    public static final ModelLoader reedTop = new ModelLoader(modelFolderPath + "reedTop.obj", true);
    public static final ModelLoader reedBottom = new ModelLoader(modelFolderPath + "reedLower.obj", true);
    public static final ModelLoader leafModel = new ModelLoader(modelFolderPath + "leafModel.obj", false);
    public static final ModelLoader seedModel = new ModelLoader(modelFolderPath + "seed.obj", true);
    public static final ModelLoader saplingModel = new ModelLoader(modelFolderPath + "sapling.obj", true);
    public static final ModelLoader primitiveCraftingTableModel = new ModelLoader(modelFolderPath + "primitiveCraftingTable.obj", true);
    public static final ModelLoader waterDefault= new ModelLoader(modelFolderPath + "waterDefault.obj", false);
    public static final ModelLoader topOfFullWater = new ModelLoader(modelFolderPath + "topOfFullWater.obj", false);
    public static final ModelLoader waterFlowNorth1 = new ModelLoader(modelFolderPath + "waterFlowNorth1.obj", false);
    public static final ModelLoader waterFlowNorth2 = new ModelLoader(modelFolderPath + "waterFlowNorth2.obj", false);
    public static final ModelLoader waterFlowNorth3 = new ModelLoader(modelFolderPath + "waterFlowNorth3.obj", false);
    public static final ModelLoader waterFlowNorth4 = new ModelLoader(modelFolderPath + "waterFlowNorth4.obj", false);
    public static final ModelLoader waterFlowNorth5 = new ModelLoader(modelFolderPath + "waterFlowNorth5.obj", false);
    public static final ModelLoader waterFlowNorth6 = new ModelLoader(modelFolderPath + "waterFlowNorth6.obj", false);
    public static final ModelLoader waterFlowNorth7 = new ModelLoader(modelFolderPath + "waterFlowNorth7.obj", false);
    public static final ModelLoader waterFlowSouth1 = new ModelLoader(modelFolderPath + "waterFlowSouth1.obj", false);
    public static final ModelLoader waterFlowSouth2 = new ModelLoader(modelFolderPath + "waterFlowSouth2.obj", false);
    public static final ModelLoader waterFlowSouth3 = new ModelLoader(modelFolderPath + "waterFlowSouth3.obj", false);
    public static final ModelLoader waterFlowSouth4 = new ModelLoader(modelFolderPath + "waterFlowSouth4.obj", false);
    public static final ModelLoader waterFlowSouth5 = new ModelLoader(modelFolderPath + "waterFlowSouth5.obj", false);
    public static final ModelLoader waterFlowSouth6 = new ModelLoader(modelFolderPath + "waterFlowSouth6.obj", false);
    public static final ModelLoader waterFlowSouth7 = new ModelLoader(modelFolderPath + "waterFlowSouth7.obj", false);
    public static final ModelLoader waterFlowEast1 = new ModelLoader(modelFolderPath + "waterFlowEast1.obj", false);
    public static final ModelLoader waterFlowEast2 = new ModelLoader(modelFolderPath + "waterFlowEast2.obj", false);
    public static final ModelLoader waterFlowEast3 = new ModelLoader(modelFolderPath + "waterFlowEast3.obj", false);
    public static final ModelLoader waterFlowEast4 = new ModelLoader(modelFolderPath + "waterFlowEast4.obj", false);
    public static final ModelLoader waterFlowEast5 = new ModelLoader(modelFolderPath + "waterFlowEast5.obj", false);
    public static final ModelLoader waterFlowEast6 = new ModelLoader(modelFolderPath + "waterFlowEast6.obj", false);
    public static final ModelLoader waterFlowEast7 = new ModelLoader(modelFolderPath + "waterFlowEast7.obj", false);
    public static final ModelLoader waterFlowWest1 = new ModelLoader(modelFolderPath + "waterFlowWest1.obj", false);
    public static final ModelLoader waterFlowWest2 = new ModelLoader(modelFolderPath + "waterFlowWest2.obj", false);
    public static final ModelLoader waterFlowWest3 = new ModelLoader(modelFolderPath + "waterFlowWest3.obj", false);
    public static final ModelLoader waterFlowWest4 = new ModelLoader(modelFolderPath + "waterFlowWest4.obj", false);
    public static final ModelLoader waterFlowWest5 = new ModelLoader(modelFolderPath + "waterFlowWest5.obj", false);
    public static final ModelLoader waterFlowWest6 = new ModelLoader(modelFolderPath + "waterFlowWest6.obj", false);
    public static final ModelLoader waterFlowWest7 = new ModelLoader(modelFolderPath + "waterFlowWest7.obj", false);
    public static final ModelLoader tilledSoilModel = new ModelLoader(modelFolderPath + "tilledSoilModel.obj", false);
    public static final ModelLoader cropGrowth1Model = new ModelLoader(modelFolderPath + "cropGrowth1.obj", false);
    public static final ModelLoader cropGrowth2Model = new ModelLoader(modelFolderPath + "cropGrowth2.obj", false);
    public static final ModelLoader cropGrowth3Model = new ModelLoader(modelFolderPath + "cropGrowth3.obj", false);
    public static final ModelLoader cropGrowth4Model = new ModelLoader(modelFolderPath + "cropGrowth4.obj", false);
    public static final ModelLoader cropGrowth5Model = new ModelLoader(modelFolderPath + "cropGrowth5.obj", false);
    public static final ModelLoader cropGrowth6Model = new ModelLoader(modelFolderPath + "cropGrowth6.obj", false);
    public static final ModelLoader cropGrowth7Model = new ModelLoader(modelFolderPath + "cropGrowth7.obj", false);
    public static final ModelLoader cropGrowth8Model = new ModelLoader(modelFolderPath + "cropGrowth8.obj", false);

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
    public static final ModelLoader size2VoxelModel = itemVoxelModel.copyModel().scaleModel(2).translateModel(0.46875f, 0, 0.46875f);

    public static final AxisAlignedBB standardBlock = new AxisAlignedBB(0,0,0,1,1,1);
    public static final AxisAlignedBB fullBlock = new AxisAlignedBB(0, 0, 0, 1, 1, 1);
    public static final AxisAlignedBB quarterBlock = new AxisAlignedBB(0, 0, 0, 1, 0.25f, 1);
    public static final AxisAlignedBB slab = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
    public static final AxisAlignedBB threeQuartersBlock = new AxisAlignedBB(0, 0, 0, 1, 0.75f, 1);
    public static final AxisAlignedBB oneVoxelHighBlock = new AxisAlignedBB(0, 0, 0, 1, 0.03125f, 1);
    public static final AxisAlignedBB northDoor = new AxisAlignedBB(0, 0, 0, 0.125, 1, 1);
    public static final AxisAlignedBB southDoor = new AxisAlignedBB(0.875,0,0, 1, 1, 1);
    public static final AxisAlignedBB eastDoor = new AxisAlignedBB(0,0,0,1,1,0.125);
    public static final AxisAlignedBB westDoor = new AxisAlignedBB(0, 0, 0.875, 1, 1, 1);

    public static final Block[] list = new Block[Short.MAX_VALUE];
    public static final Block air = new Block((short) 0, -1, blockFolderPath + "air.txt");
    public static final Block grass = new BlockGrass((short) 1, 2, blockFolderPath + "grass.txt");
    public static final Block torchStandard = new BlockTorch((short) 2, 3,blockFolderPath + "torchStandard.txt");
    public static final Block torchNorth = new BlockTorch((short) 3, 3,blockFolderPath + "torchNorth.txt");
    public static final Block torchSouth = new BlockTorch((short) 4, 3,blockFolderPath + "torchSouth.txt");
    public static final Block torchEast = new BlockTorch((short) 5, 3,blockFolderPath + "torchEast.txt");
    public static final Block torchWest = new BlockTorch((short) 6, 3,blockFolderPath + "torchWest.txt");
    public static final Block dirt = new BlockDirt((short) 7, 1,blockFolderPath + "dirt.txt");
    public static final Block water = new BlockWater((short) 8, 4, blockFolderPath + "water.txt");
    public static final Block sand = new Block((short) 9, 5, blockFolderPath + "sand.txt"); //I don't like sand
    public static final Block snow = new Block((short) 10, 6, blockFolderPath + "snow.txt");
    public static final Block stone = new Block((short) 11, 7, blockFolderPath + "stone.txt");
    public static final Block oakLogFullSizeNormal = new BlockLog((short) 12, 8, blockFolderPath + "oakLogFullSizeNormal.txt");
    public static final Block oakLogSize15Normal = new BlockLog((short) 13, 8, blockFolderPath + "oakLogSize15Normal.txt");
    public static final Block oakLogSize14Normal = new BlockLog((short) 14, 8, blockFolderPath + "oakLogSize14Normal.txt");
    public static final Block oakLogSize13Normal = new BlockLog((short) 15, 8,blockFolderPath + "oakLogSize13Normal.txt");
    public static final Block oakLogSize12Normal = new BlockLog((short) 16, 8,blockFolderPath + "oakLogSize12Normal.txt");
    public static final Block oakLogSize11Normal = new BlockLog((short) 17, 8,blockFolderPath + "oakLogSize11Normal.txt");
    public static final Block oakLogSize10Normal = new BlockLog((short) 18, 8, blockFolderPath + "oakLogSize10Normal.txt");
    public static final Block oakLogSize9Normal = new BlockLog((short) 19, 8,blockFolderPath + "oakLogSize9Normal.txt");
    public static final Block oakLogSize8Normal = new BlockLog((short) 20, 8,blockFolderPath + "oakLogSize8Normal.txt");
    public static final Block oakLogSize7Normal = new BlockLog((short) 21, 8,blockFolderPath + "oakLogSize7Normal.txt");
    public static final Block oakLogSize6Normal = new BlockLog((short) 22, 8,blockFolderPath + "oakLogSize6Normal.txt");
    public static final Block oakLogSize5Normal = new BlockLog((short) 23, 8,blockFolderPath + "oakLogSize5Normal.txt");
    public static final Block oakLogSize4Normal = new BlockLog((short) 24, 8,blockFolderPath + "oakLogSize4Normal.txt");
    public static final Block oakLogSize3Normal = new BlockLog((short) 25, 8,blockFolderPath + "oakLogSize3Normal.txt");
    public static final Block oakLogSize2Normal = new BlockLog((short) 26, 8,blockFolderPath + "oakLogSize2Normal.txt");
    public static final Block oakLogSize1Normal = new BlockLog((short) 27, 8,blockFolderPath + "oakLogSize1Normal.txt");
    public static final Block oakLogFullSizeNorthSouth = new BlockLog((short) 28, 8,blockFolderPath + "oakLogFullSizeNormal.txt");
    public static final Block oakLogSize15NorthSouth = new BlockLog((short) 29, 8, blockFolderPath + "oakLogSize15NorthSouth.txt");
    public static final Block oakLogSize14NorthSouth = new BlockLog((short) 30, 8,blockFolderPath + "oakLogSize14NorthSouth.txt");
    public static final Block oakLogSize13NorthSouth = new BlockLog((short) 31, 8,blockFolderPath + "oakLogSize13NorthSouth.txt");
    public static final Block oakLogSize12NorthSouth = new BlockLog((short) 32, 8,blockFolderPath + "oakLogSize12NorthSouth.txt");
    public static final Block oakLogSize11NorthSouth = new BlockLog((short) 33, 8,blockFolderPath + "oakLogSize11NorthSouth.txt");
    public static final Block oakLogSize10NorthSouth = new BlockLog((short) 34, 8,blockFolderPath + "oakLogSize10NorthSouth.txt");
    public static final Block oakLogSize9NorthSouth = new BlockLog((short) 35, 8,blockFolderPath + "oakLogSize9NorthSouth.txt");
    public static final Block oakLogSize8NorthSouth = new BlockLog((short) 36, 8,blockFolderPath + "oakLogSize8NorthSouth.txt");
    public static final Block oakLogSize7NorthSouth = new BlockLog((short) 37, 8,blockFolderPath + "oakLogSize7NorthSouth.txt");
    public static final Block oakLogSize6NorthSouth = new BlockLog((short) 38, 8,blockFolderPath + "oakLogSize6NorthSouth.txt");
    public static final Block oakLogSize5NorthSouth = new BlockLog((short) 39, 8,blockFolderPath + "oakLogSize5NorthSouth.txt");
    public static final Block oakLogSize4NorthSouth = new BlockLog((short) 40, 8,blockFolderPath + "oakLogSize4NorthSouth.txt");
    public static final Block oakLogSize3NorthSouth = new BlockLog((short) 41, 8,blockFolderPath + "oakLogSize3NorthSouth.txt");
    public static final Block oakLogSize2NorthSouth = new BlockLog((short) 42, 8,blockFolderPath + "oakLogSize2NorthSouth.txt");
    public static final Block oakLogSize1NorthSouth = new BlockLog((short) 43, 8,blockFolderPath + "oakLogSize1NorthSouth.txt");
    public static final Block oakLogFullSizeEastWest = new BlockLog((short) 44, 8,blockFolderPath + "oakLogFullSizeNormal.txt");
    public static final Block oakLogSize15EastWest = new BlockLog((short) 45, 8,blockFolderPath + "oakLogSize15EastWest.txt");
    public static final Block oakLogSize14EastWest = new BlockLog((short) 46, 8,blockFolderPath + "oakLogSize14EastWest.txt");
    public static final Block oakLogSize13EastWest = new BlockLog((short) 47, 8,blockFolderPath + "oakLogSize13EastWest.txt");
    public static final Block oakLogSize12EastWest = new BlockLog((short) 48, 8,blockFolderPath + "oakLogSize12EastWest.txt");
    public static final Block oakLogSize11EastWest = new BlockLog((short) 49, 8,blockFolderPath + "oakLogSize11EastWest.txt");
    public static final Block oakLogSize10EastWest = new BlockLog((short) 50, 8,blockFolderPath + "oakLogSize10EastWest.txt");
    public static final Block oakLogSize9EastWest = new BlockLog((short) 51, 8,blockFolderPath + "oakLogSize9EastWest.txt");
    public static final Block oakLogSize8EastWest = new BlockLog((short) 52, 8,blockFolderPath + "oakLogSize8EastWest.txt");
    public static final Block oakLogSize7EastWest = new BlockLog((short) 53, 8,blockFolderPath + "oakLogSize7EastWest.txt");
    public static final Block oakLogSize6EastWest = new BlockLog((short) 54, 8,blockFolderPath + "oakLogSize6EastWest.txt");
    public static final Block oakLogSize5EastWest = new BlockLog((short) 55, 8,blockFolderPath + "oakLogSize5EastWest.txt");
    public static final Block oakLogSize4EastWest = new BlockLog((short) 56, 8,blockFolderPath + "oakLogSize4EastWest.txt");
    public static final Block oakLogSize3EastWest = new BlockLog((short) 57, 8,blockFolderPath + "oakLogSize3EastWest.txt");
    public static final Block oakLogSize2EastWest = new BlockLog((short) 58, 8,blockFolderPath + "oakLogSize2EastWest.txt");
    public static final Block oakLogSize1EastWest = new BlockLog((short) 59, 8, blockFolderPath + "oakLogSize2EastWest.txt");
    public static final Block leaf = new BlockLeaf((short) 60, 10,blockFolderPath + "leaf.txt"); //Leaf Erikson
    public static final Block berryBush = new BlockBerryBush((short) 61, 25, blockFolderPath + "berryBush.txt");
    public static final Block berryBushNoBerries = new BlockBerryBush((short) 62, 11, blockFolderPath + "berryBush.txt");
    public static final Block unused_field_1 = null; //Unused 63
    public static final Block unused_field_2 = null; //64
    public static final Block unused_field_3 = null; //65
    public static final Block unused_field_4 = null; //66
    public static final Block fire = new Block((short)67, 18, blockFolderPath + "fire.txt");
    public static final Block campfire = new BlockCampFire((short) 68, 16, blockFolderPath + "campFireLit.txt", 3, 1);
    public static final Block grassWithClay = new BlockGrassWithClay((short)69, 2, blockFolderPath + "grassWithClay.txt"); //Nice
    public static final Block grassBlockWithClayLower = new Block((short)70, 14, blockFolderPath + "grassBlockWithClayLower.txt");
    public static final Block clay = new BlockClay((short)71, 13, blockFolderPath + "clay.txt");
    public static final Block itemClay = new BlockItemClay((short)72, 13, blockFolderPath + "itemClay.txt");
    public static final Block rawRedClayCookingPot = new Block((short)73, 13, blockFolderPath + "rawClayCookingPot.txt");
    public static final Block pitKilnUnlit1 = new BlockPitKilnUnlit((short)74, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit2 = new BlockPitKilnUnlit((short)75, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit3 = new BlockPitKilnUnlit((short)76, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit4 = new BlockPitKilnUnlit((short)77, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit5 = new BlockPitKilnUnlit((short)78, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit6 = new BlockPitKilnUnlit((short)79, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit7 = new BlockPitKilnUnlit((short)80, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit8 = new BlockPitKilnUnlit((short)81, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block redClayCookingPot = new Block((short)82, 17, blockFolderPath + "redClayCookingPot.txt");
    public static final Block grassBlockLower = new Block((short)83, 20, blockFolderPath + "grassBlockLower.txt");
    public static final Block cactus = new BlockCactus((short)84, 21, blockFolderPath + "cactus.txt");
    public static final Block itemStone = new BlockItemStone((short)85, stone.textureID, blockFolderPath + "itemStone.txt");
    public static final Block berryBushFlower = new BlockBerryBush((short)86, 27, blockFolderPath + "berryBush.txt");
    public static final Block itemStick = new BlockItemStick((short)87, 29, blockFolderPath + "itemStick.txt");
    public static final Block tallGrass = new BlockTallGrass((short)88, 30, blockFolderPath + "tallGrass.txt");
    public static final Block unused_field_5 = null; //Unused 89
    public static final Block fireWoodBlock = new Block((short)90, 31, blockFolderPath + "fireWood.txt");
    public static final Block reedChest = new BlockReedChest((short)91, 32, blockFolderPath + "reedChest.txt",1, 9);
    public static final Block reedChestTier0 = new BlockReedCrafting((short)92, 32, blockFolderPath + "reedChestBuilding0.txt");
    public static final Block reedBasketTier0 = new BlockReedCrafting((short)93, 32, blockFolderPath + "reedBasketBuilding.txt");
    public static final Block reedChestTier1 = new BlockReedCrafting((short)94, 32, blockFolderPath + "reedChestBuilding1.txt");
    public static final Block pitKilnUnlitLog1 = new BlockPitKilnUnlit((short)95, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlitLog2 = new BlockPitKilnUnlit((short)96, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlitLog3 = new BlockPitKilnUnlit((short)97, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnUnlit = new BlockPitKilnUnlit((short)98, 15, blockFolderPath + "pitKilnUnlit.txt", 1,1);
    public static final Block pitKilnLit = new BlockPitKilnLit((short)99, 15, blockFolderPath + "pitKilnLit.txt", 1,1);
    public static final Block largeFireWoodBlock = new Block((short)100, 31, blockFolderPath + "fireWood.txt");
    public static final Block logPile = new BlockLogPile((short)101, 31, blockFolderPath + "logPile.txt", Item.fireWood.ID, 1, 1);
    public static final Block brickPile = new BlockBrickPile((short)102, 13, blockFolderPath + "brickPile.txt", Item.rawClayAdobeBrick.ID, 1, 1);
    public static final Block itemBlock = new BlockItem((short)103, 19, blockFolderPath + "itemBlock.txt", 1, 1);
    public static final Block adobeBrick = new Block((short)104, 17, blockFolderPath + "adobeBrick.txt");
    public static final Block reedLower = new BlockReed((short)105, 33, blockFolderPath + "reeds.txt");
    public static final Block reedUpper = new Block((short)106, 34, blockFolderPath + "reedsUpper.txt");
    public static final Block berrySeed = new BlockBerryBushGrowing((short)107, 35, blockFolderPath + "berrySeed.txt");
    public static final Block berryBushGrowth1 = new BlockBerryBushGrowing((short)108, 11, blockFolderPath + "berryBushGrowth.txt");
    public static final Block berryBushGrowth2 = new BlockBerryBushGrowing((short)109, 11, blockFolderPath + "berryBushGrowth.txt");
    public static final Block berryBushGrowth3 = new BlockBerryBushGrowing((short)110, 11, blockFolderPath + "berryBushGrowth.txt");
    public static final Block berryBushGrowth4 = new BlockBerryBushGrowing((short)111, 11, blockFolderPath + "berryBushGrowth.txt");
    public static final Block berryBushGrowth5 = new BlockBerryBushGrowing((short)112, 11, blockFolderPath + "berryBushGrowth.txt");
    public static final Block reedSeed = new BlockReedGrowing((short)113, 35, blockFolderPath + "reedSeed.txt");
    public static final Block reedGrowth1 = new BlockReedGrowing((short)114, 36, blockFolderPath + "reedGrowth.txt");
    public static final Block reedGrowth2 = new BlockReedGrowing((short)115, 36, blockFolderPath + "reedGrowth.txt");
    public static final Block reedGrowth3 = new BlockReedGrowing((short)116, 36, blockFolderPath + "reedGrowth.txt");
    public static final Block reedGrowth4 = new BlockReedGrowing((short)117, 36, blockFolderPath + "reedGrowth.txt");
    public static final Block reedGrowth5 = new BlockReedGrowing((short)118, 36, blockFolderPath + "reedGrowth.txt");
    public static final Block treeSeed = new BlockSapling((short)119, 35, blockFolderPath + "treeSeed.txt");
    public static final Block sapling = new BlockSapling((short)120, 37, blockFolderPath + "sapling.txt");
    public static final Block torchStandardUnlit = new BlockTorchUnlit((short)121, 38, blockFolderPath + "torchStandardUnlit.txt");
    public static final Block torchNorthUnlit = new BlockTorchUnlit((short)122, 38, blockFolderPath + "torchNorthUnlit.txt");
    public static final Block torchSouthUnlit = new BlockTorchUnlit((short)123, 38, blockFolderPath + "torchSouthUnlit.txt");
    public static final Block torchEastUnlit = new BlockTorchUnlit((short)124, 38, blockFolderPath + "torchEastUnlit.txt");
    public static final Block torchWestUnlit = new BlockTorchUnlit((short)125, 38, blockFolderPath + "torchWestUnlit.txt");
    public static final Block torchStandardBurnedOut = new Block((short)126, 39, blockFolderPath + "torchStandardBurnedOut.txt");
    public static final Block torchNorthBurnedOut = new Block((short)127, 39, blockFolderPath + "torchNorthBurnedOut.txt");
    public static final Block torchSouthBurnedOut = new Block((short)128, 39, blockFolderPath + "torchSouthBurnedOut.txt");
    public static final Block torchEastBurnedOut = new Block((short)129, 39, blockFolderPath + "torchEastBurnedOut.txt");
    public static final Block torchWestBurnedOut = new Block((short)130, 39, blockFolderPath + "torchWestBurnedOut.txt");
    public static final Block crafting3DItem = new BlockCrafting3D((short)131, -1, blockFolderPath + "crafting3DItem.txt");
    public static final Block primitiveCraftingTable = new BlockCraftingTable((short)132, 40, blockFolderPath + "primitiveCraftingTable.txt");
    public static final Block craftingItem = new BlockCrafting((short)133, -1, blockFolderPath + "craftingItem.txt");
    public static final Block doorPrimitiveUpper = new BlockDoor((short)134, 42, blockFolderPath + "doorPrimitive.txt"); //Contains the texture ID
    public static final Block doorNorthDoorHingeLeftClosed = new BlockDoor((short)135, -1, blockFolderPath + "northDoorClosedHingeLeft.txt");
    public static final Block doorNorthDoorHingeRightClosed = new BlockDoor((short)136, -1, blockFolderPath + "northDoorClosedHingeRight.txt");
    public static final Block doorNorthDoorHingeLeftOpen = new BlockDoor((short)137, -1, blockFolderPath + "northDoorOpenHingeLeft.txt");
    public static final Block doorNorthDoorHingeRightOpen = new BlockDoor((short)138, -1, blockFolderPath + "northDoorOpenHingeRight.txt");
    public static final Block doorSouthDoorHingeLeftClosed = new BlockDoor((short)139, -1, blockFolderPath + "southDoorClosedHingeLeft.txt");
    public static final Block doorSouthDoorHingeRightClosed = new BlockDoor((short)140, -1, blockFolderPath + "southDoorClosedHingeRight.txt");
    public static final Block doorSouthDoorHingeLeftOpen = new BlockDoor((short)141, -1, blockFolderPath + "southDoorOpenHingeLeft.txt");
    public static final Block doorSouthDoorHingeRightOpen = new BlockDoor((short)142, -1, blockFolderPath + "southDoorOpenHingeRight.txt");
    public static final Block doorEastDoorHingeLeftClosed = new BlockDoor((short)143, -1, blockFolderPath + "eastDoorClosedHingeLeft.txt");
    public static final Block doorEastDoorHingeRightClosed = new BlockDoor((short)144, -1, blockFolderPath + "eastDoorClosedHingeRight.txt");
    public static final Block doorEastDoorHingeLeftOpen = new BlockDoor((short)145, -1, blockFolderPath + "eastDoorOpenHingeLeft.txt");
    public static final Block doorEastDoorHingeRightOpen = new BlockDoor((short)146, -1, blockFolderPath + "eastDoorOpenHingeRight.txt");
    public static final Block doorWestDoorHingeLeftClosed = new BlockDoor((short)147, -1, blockFolderPath + "westDoorClosedHingeLeft.txt");
    public static final Block doorWestDoorHingeRightClosed = new BlockDoor((short)148, -1, blockFolderPath + "westDoorClosedHingeRight.txt");
    public static final Block doorWestDoorHingeLeftOpen = new BlockDoor((short)149, -1, blockFolderPath + "westDoorOpenHingeLeft.txt");
    public static final Block doorWestDoorHingeRightOpen = new BlockDoor((short)150, -1, blockFolderPath + "westDoorOpenHingeRight.txt");
    public static final Block doorPrimitiveLower = new BlockDoor((short)151, 42, blockFolderPath + "doorPrimitive.txt");
    public static final Block waterFlowNorth1Block = new BlockWater((short)152, 65, blockFolderPath + "waterFlowNorth1.txt");
    public static final Block waterFlowNorth2Block = new BlockWater((short)153, 65, blockFolderPath + "waterFlowNorth2.txt");
    public static final Block waterFlowNorth3Block = new BlockWater((short)154, 65, blockFolderPath + "waterFlowNorth3.txt");
    public static final Block waterFlowNorth4Block = new BlockWater((short)155, 65, blockFolderPath + "waterFlowNorth4.txt");
    public static final Block waterFlowNorth5Block = new BlockWater((short)156, 65, blockFolderPath + "waterFlowNorth5.txt");
    public static final Block waterFlowNorth6Block = new BlockWater((short)157, 65, blockFolderPath + "waterFlowNorth6.txt");
    public static final Block waterFlowNorth7Block = new BlockWater((short)158, 65, blockFolderPath + "waterFlowNorth7.txt");
    public static final Block waterFlowSouth1Block = new BlockWater((short)159, 66, blockFolderPath + "waterFlowSouth1.txt");
    public static final Block waterFlowSouth2Block = new BlockWater((short)160, 66, blockFolderPath + "waterFlowSouth2.txt");
    public static final Block waterFlowSouth3Block = new BlockWater((short)161, 66, blockFolderPath + "waterFlowSouth3.txt");
    public static final Block waterFlowSouth4Block = new BlockWater((short)162, 66, blockFolderPath + "waterFlowSouth4.txt");
    public static final Block waterFlowSouth5Block = new BlockWater((short)163, 66, blockFolderPath + "waterFlowSouth5.txt");
    public static final Block waterFlowSouth6Block = new BlockWater((short)164, 66, blockFolderPath + "waterFlowSouth6.txt");
    public static final Block waterFlowSouth7Block = new BlockWater((short)165, 66, blockFolderPath + "waterFlowSouth7.txt");
    public static final Block waterFlowEast1Block = new BlockWater((short)166, 67, blockFolderPath + "waterFlowEast1.txt");
    public static final Block waterFlowEast2Block = new BlockWater((short)167, 67, blockFolderPath + "waterFlowEast2.txt");
    public static final Block waterFlowEast3Block = new BlockWater((short)168, 67, blockFolderPath + "waterFlowEast3.txt");
    public static final Block waterFlowEast4Block = new BlockWater((short)169, 67, blockFolderPath + "waterFlowEast4.txt");
    public static final Block waterFlowEast5Block = new BlockWater((short)170, 67, blockFolderPath + "waterFlowEast5.txt");
    public static final Block waterFlowEast6Block = new BlockWater((short)171, 67, blockFolderPath + "waterFlowEast6.txt");
    public static final Block waterFlowEast7Block = new BlockWater((short)172, 67, blockFolderPath + "waterFlowEast7.txt");
    public static final Block waterFlowWest1Block = new BlockWater((short)173, 68, blockFolderPath + "waterFlowWest1.txt");
    public static final Block waterFlowWest2Block = new BlockWater((short)174, 68, blockFolderPath + "waterFlowWest2.txt");
    public static final Block waterFlowWest3Block = new BlockWater((short)175, 68, blockFolderPath + "waterFlowWest3.txt");
    public static final Block waterFlowWest4Block = new BlockWater((short)176, 68, blockFolderPath + "waterFlowWest4.txt");
    public static final Block waterFlowWest5Block = new BlockWater((short)177, 68, blockFolderPath + "waterFlowWest5.txt");
    public static final Block waterFlowWest6Block = new BlockWater((short)178, 68, blockFolderPath + "waterFlowWest6.txt");
    public static final Block waterFlowWest7Block = new BlockWater((short)179, 68, blockFolderPath + "waterFlowWest7.txt");
    public static final Block fullWater = new BlockWater((short)180, 4, blockFolderPath + "fullWater.txt");
    public static final Block tilledSoil = new BlockSoil((short)181, 1,blockFolderPath + "tilledSoil.txt");
    public static final Block cropGrowth = new BlockCrop((short)182, -1, blockFolderPath + "cropGrowth.txt");
    public static final Block deadCrop = new Block((short)183, 95, blockFolderPath  + "deadCrop.txt");

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
    public String faceDirection;
    public boolean isDoorOpen;

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
                    case "slab" -> this.standardCollisionBoundingBox = slab;
                    case "quarterBlock" -> this.standardCollisionBoundingBox = quarterBlock;
                    case "threeQuartersBlock" -> this.standardCollisionBoundingBox = threeQuartersBlock;
                    case "fullBlock" -> this.standardCollisionBoundingBox = fullBlock;
                    case "oneVoxelHighBlock" ->  this.standardCollisionBoundingBox = oneVoxelHighBlock;
                    case "northDoor" -> this.standardCollisionBoundingBox = northDoor;
                    case "southDoor" -> this.standardCollisionBoundingBox = southDoor;
                    case "eastDoor" -> this.standardCollisionBoundingBox = eastDoor;
                    case "westDoor" -> this.standardCollisionBoundingBox = westDoor;
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
                    case "strawChestModel" -> this.blockModel = strawChestModel;
                    case "itemClayModel" -> this.blockModel = itemClayModel;
                    case "clayCookingPotModel" -> this.blockModel = clayCookingPotModel;
                    case "primitiveDoorUpper" -> this.blockModel = primitiveDoorUpper;
                    case "reedTop" -> this.blockModel = reedTop;
                    case "reedBottom" -> this.blockModel = reedBottom;
                    case "leafModel" -> this.blockModel = leafModel;
                    case "seedModel" -> this.blockModel = seedModel;
                    case "saplingModel" -> this.blockModel = saplingModel;
                    case "primitiveCraftingTable" -> this.blockModel = primitiveCraftingTableModel;
                    case "tilledSoilModel" -> this.blockModel = tilledSoilModel;

                    case "waterDefault" -> this.blockModel = waterDefault;
                    case "waterFlowNorth1" -> this.blockModel = waterFlowNorth1;
                    case "waterFlowNorth2" -> this.blockModel = waterFlowNorth2;
                    case "waterFlowNorth3" -> this.blockModel = waterFlowNorth3;
                    case "waterFlowNorth4" -> this.blockModel = waterFlowNorth4;
                    case "waterFlowNorth5" -> this.blockModel = waterFlowNorth5;
                    case "waterFlowNorth6" -> this.blockModel = waterFlowNorth6;
                    case "waterFlowNorth7" -> this.blockModel = waterFlowNorth7;

                    case "waterFlowSouth1" -> this.blockModel = waterFlowSouth1;
                    case "waterFlowSouth2" -> this.blockModel = waterFlowSouth2;
                    case "waterFlowSouth3" -> this.blockModel = waterFlowSouth3;
                    case "waterFlowSouth4" -> this.blockModel = waterFlowSouth4;
                    case "waterFlowSouth5" -> this.blockModel = waterFlowSouth5;
                    case "waterFlowSouth6" -> this.blockModel = waterFlowSouth6;
                    case "waterFlowSouth7" -> this.blockModel = waterFlowSouth7;

                    case "waterFlowEast1" -> this.blockModel = waterFlowEast1;
                    case "waterFlowEast2" -> this.blockModel = waterFlowEast2;
                    case "waterFlowEast3" -> this.blockModel = waterFlowEast3;
                    case "waterFlowEast4" -> this.blockModel = waterFlowEast4;
                    case "waterFlowEast5" -> this.blockModel = waterFlowEast5;
                    case "waterFlowEast6" -> this.blockModel = waterFlowEast6;
                    case "waterFlowEast7" -> this.blockModel = waterFlowEast7;

                    case "waterFlowWest1" -> this.blockModel = waterFlowWest1;
                    case "waterFlowWest2" -> this.blockModel = waterFlowWest2;
                    case "waterFlowWest3" -> this.blockModel = waterFlowWest3;
                    case "waterFlowWest4" -> this.blockModel = waterFlowWest4;
                    case "waterFlowWest5" -> this.blockModel = waterFlowWest5;
                    case "waterFlowWest6" -> this.blockModel = waterFlowWest6;
                    case "waterFlowWest7" -> this.blockModel = waterFlowWest7;

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

            if(properties[0].equals("faceDirection")){
                this.faceDirection = properties[1];
            }

            if(properties[0].equals("isDoorOpen")){
                this.isDoorOpen = Boolean.parseBoolean(properties[1]);
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
            kilnInventory.addItemToInventory(world.pitKilnItemType(this.ID, x,y,z), world.getBlockID(x,y,z), world.pitKilnItemCount(this.ID, x,y,z), Item.NULL_ITEM_DURABILITY, 0, null);
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
                world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.straw.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0, null));
            }
        }
        if(this.ID == grassWithClay.ID || this.ID == clay.ID){
            int extraClay = CosmicEvolution.globalRand.nextInt(2,4);
            for(int i = 0; i < extraClay; i++){
                world.addEntity(new EntityBlock(x + 0.5, y + 0.5, z + 0.5, Block.itemClay.ID, (byte)1));
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
                    world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityItem(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].droppedItemID, Item.NULL_ITEM_METADATA, (byte) 1, Item.list[list[blockID].droppedItemID].durability, 0, null));
                }
            } else {
                if (list[blockID].itemDropChance > CosmicEvolution.globalRand.nextFloat()) {
                    world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).addEntityToList(new EntityBlock(x + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), y + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), z + 0.5 + CosmicEvolution.globalRand.nextDouble(-0.3, 0.3), list[blockID].itemMetadata, (byte) 1));
                }
            }
        }

        if(this instanceof BlockHeating){
            world.removeHeatableBlock(x,y,z);
        }

        if(this instanceof BlockContainer){
            world.removeChestLocation(x,y,z);
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

        if(this instanceof BlockCrop){
            world.removeCropState(x,y,z);
        }

        if(this instanceof BlockSoil){
            world.removeTilledSoilState(x,y,z);
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
        if (chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)] != air.ID && !(Block.list[chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)]] instanceof BlockWater)) {return;}
        if (!MouseListener.rightClickReleased)return;

        short heldItem = player.getHeldItem(); //Block all items that cannot be placed on the ground
        if (heldItem == Item.NULL_ITEM_REFERENCE || (!Item.list[heldItem].canPlaceOnGround && !Item.list[heldItem].canPlaceAsItemBlock))return;


        short heldBlock = 0;
        if (player.isHoldingBlock()) {
            heldBlock = player.getHeldBlock();
        }

        if (heldBlock == torchStandard.ID) {
            if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))return;
            heldBlock = switch (facingDirection) {
                case FACE_NORTH -> torchNorth.ID;
                case FACE_SOUTH -> torchSouth.ID;
                case FACE_EAST -> torchEast.ID;
                case FACE_WEST -> torchWest.ID;
                case FACE_DOWN -> air.ID;
                default -> torchStandard.ID;
            };
        } else if(heldBlock == torchStandardUnlit.ID){
            if(KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))return;
            heldBlock = switch (facingDirection) {
                case FACE_NORTH -> torchNorthUnlit.ID;
                case FACE_SOUTH -> torchSouthUnlit.ID;
                case FACE_EAST -> torchEastUnlit.ID;
                case FACE_WEST -> torchWestUnlit.ID;
                case FACE_DOWN -> air.ID;
                default -> torchStandardUnlit.ID;
            };
        }

        if(Item.list[heldItem].canPlaceAsItemBlock && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT)) && list[world.getBlockID(x, y - 1, z)].isSolid){
            heldBlock = itemBlock.ID;
        } else if(Item.list[heldItem].canPlaceAsItemBlock){
            return;
        }

        switch (Item.list[heldItem].itemName) { //Convert held item into an equivalent block id to place, if one exists, otherwise default to the held block
            case "STRAW" -> {
                heldBlock = campfire.ID;
                world.addChestLocation(x,y,z, new Inventory(1, 2));
                world.addCampfireState(new CampfireState(Chunk.getBlockIndexFromCoordinates(x,y,z), false, 0,0), x,y,z);
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
            case "SEED_WILD_GRASS", "SEED_EINKORN_WHEAT", "SEED_EMMER_WHEAT", "SEED_WHEAT" -> {
                if(list[world.getBlockID(x, y - 1, z)] instanceof BlockSoil){
                    heldBlock = cropGrowth.ID;
                } else {
                    return;
                }
            }
            case "DOOR_PRIMITIVE" -> {
                switch (player.getPlayerCardinalFaceDirection()){
                    case "North" -> {
                        if(world.getBlockID(x, y, z - 1) == Block.doorSouthDoorHingeRightClosed.ID){
                            world.setBlock(x, y, z - 1, Block.doorSouthDoorHingeLeftClosed.ID);
                            heldBlock =  Block.doorSouthDoorHingeRightClosed.ID;
                        } else if(world.getBlockID(x, y, z + 1) == Block.doorSouthDoorHingeRightClosed.ID){
                            heldBlock =  Block.doorSouthDoorHingeLeftClosed.ID;
                        } else {
                            heldBlock =  Block.doorSouthDoorHingeRightClosed.ID;
                        }
                    }
                    case "South" -> {
                        if(world.getBlockID(x, y, z - 1) == Block.doorNorthDoorHingeRightClosed.ID){
                            heldBlock =  Block.doorNorthDoorHingeLeftClosed.ID;
                        } else if(world.getBlockID(x, y, z + 1) == Block.doorNorthDoorHingeRightClosed.ID){
                            world.setBlock(x, y, z + 1, Block.doorNorthDoorHingeLeftClosed.ID);
                            heldBlock =  Block.doorNorthDoorHingeRightClosed.ID;
                        } else {
                            heldBlock =  Block.doorNorthDoorHingeRightClosed.ID;
                        }
                    }
                    case "East" -> {
                        if(world.getBlockID(x + 1, y, z) == Block.doorWestDoorHingeRightClosed.ID){
                            world.setBlock(x + 1, y, z, Block.doorWestDoorHingeLeftClosed.ID);
                            heldBlock =  Block.doorWestDoorHingeRightClosed.ID;
                        } else if(world.getBlockID(x - 1, y, z) == Block.doorWestDoorHingeRightClosed.ID){
                            heldBlock =  Block.doorWestDoorHingeLeftClosed.ID;
                        } else {
                            heldBlock =  Block.doorWestDoorHingeRightClosed.ID;
                        }
                    }
                    case "West" -> {
                        if(world.getBlockID(x + 1, y, z) == Block.doorEastDoorHingeRightClosed.ID){
                            heldBlock =  Block.doorEastDoorHingeLeftClosed.ID;
                        } else if(world.getBlockID(x - 1, y, z) == Block.doorEastDoorHingeRightClosed.ID){
                            world.setBlock(x - 1, y, z, Block.doorEastDoorHingeLeftClosed.ID);
                            heldBlock =  Block.doorEastDoorHingeRightClosed.ID;
                        } else {
                            heldBlock =  Block.doorEastDoorHingeRightClosed.ID;
                        }
                    }

                }
                world.setBlock(x, y + 1, z, Block.doorPrimitiveUpper.ID);
            }
        }

        if(list[heldBlock].requireSolidBlockBelow){
            if(!list[world.getBlockID(x,y - 1, z)].isSolid){
                return;
            }
        }

        if(Block.list[heldBlock] instanceof ITimeUpdate){
            chunk.addTimeUpdateEvent(x,y,z, CosmicEvolution.instance.save.time + ((ITimeUpdate) Block.list[heldBlock]).getUpdateTime());
        }

        if(Block.list[heldBlock] instanceof BlockCrop){
            TilledSoilState tilledSoilState = world.getTilledSoilState(x, y - 1, z);

            SeedState seedState = (SeedState)player.getHeldItemState();

            if(seedState == null)throw new IllegalStateException("Itemstate in the player's hand is null");

            String cropName = ((ItemSeed)Item.list[heldItem]).getCropName();

            boolean canMutate = tilledSoilState.fertilizerID == TilledSoilState.BONEMEAL;
            if(canMutate && !seedState.canMutate){
                GuiMutateCrop guiMutateCrop = new GuiMutateCrop(world.ce, x, y, z, world, player, Crop.getCropFromName(cropName));
                if(!guiMutateCrop.close) {
                    world.ce.setNewGui(guiMutateCrop);
                } else {
                    tilledSoilState.fertilizerID = TilledSoilState.NO_FERTILIZER;
                }
            } else {
                chunk.addCropState(new CropState(Chunk.getBlockIndexFromCoordinates(x,y,z), cropName, canMutate, seedState.targetCrop, 0, seedState.percentToTargetCrop),x,y,z);
            }
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
                chunk.getChestLocation(x,y,z).inventory.itemStacks[0].decayTime = player.getHeldItemDecayTime();
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

    public boolean isLightBlock(int x, int y, int z, World world){
        return this.isLightBlock;
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
