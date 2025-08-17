package spacegame.block;

import spacegame.core.Sound;
import spacegame.core.SoundPlayer;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.render.ModelLoader;
import spacegame.world.AxisAlignedBB;
import spacegame.world.Chunk;
import spacegame.world.World;

import java.io.*;
import java.util.Random;

import static spacegame.entity.EntityPlayer.selectedInventorySlot;

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
    public static final AxisAlignedBB standardBlock = new AxisAlignedBB(0,0,0,1,1,1);
    public static final AxisAlignedBB itemStoneBoundingBox = new AxisAlignedBB(0,0,0,1,0.125,1);
    public static final Block[] list = new Block[Short.MAX_VALUE];
    public static final Block air = new Block((short) 0, -1, "src/spacegame/assets/blockFiles/air.txt");
    public static final Block grass = new BlockGrass((short) 1, 2, "src/spacegame/assets/blockFiles/grass.txt");
    public static final Block torchStandard = new BlockTorch((short) 2, 3,"src/spacegame/assets/blockFiles/torchStandard.txt");
    public static final Block torchNorth = new BlockTorch((short) 3, 3,"src/spacegame/assets/blockFiles/torchNorth.txt");
    public static final Block torchSouth = new BlockTorch((short) 4, 3,"src/spacegame/assets/blockFiles/torchSouth.txt");
    public static final Block torchEast = new BlockTorch((short) 5, 3,"src/spacegame/assets/blockFiles/torchEast.txt");
    public static final Block torchWest = new BlockTorch((short) 6, 3,"src/spacegame/assets/blockFiles/torchWest.txt");
    public static final Block dirt = new BlockDirt((short) 7, 1,"src/spacegame/assets/blockFiles/dirt.txt");
    public static final Block water = new Block((short) 8, 4, "src/spacegame/assets/blockFiles/water.txt");
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
    public static final Block berryBush = new BlockBerryBush((short) 61, 11, "src/spacegame/assets/blockFiles/berryBush.txt");
    public static final Block berryBushNoBerries = new BlockBerryBush((short) 62, 12, "src/spacegame/assets/blockFiles/berryBush.txt");
    public static final Block campfireUnLit2Sticks = new BlockCampFireUnlit((short) 63, 13, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block campfireUnLit3Sticks = new BlockCampFireUnlit((short) 64, 14, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block campfireUnLit4Sticks = new BlockCampFireUnlit((short) 65, 15, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block campfireUnLit = new BlockCampFireUnlit((short) 66, 16, "src/spacegame/assets/blockFiles/campFireUnlit.txt");
    public static final Block fire = new Block((short)67, 18, "src/spacegame/assets/blockFiles/fire.txt");
    public static final Block campfireLit = new BlockCampFireLit((short) 68, 17, "src/spacegame/assets/blockFiles/campFireLit.txt");
    public static final Block campFireLitLight13 = new BlockCampFireLit((short)69, 17, "src/spacegame/assets/blockFiles/campFireLitLight13.txt"); //Nice
    public static final Block campFireLitLight12 = new BlockCampFireLit((short)70, 17, "src/spacegame/assets/blockFiles/campFireLitLight12.txt");
    public static final Block campFireLitLight11 = new BlockCampFireLit((short)71, 17, "src/spacegame/assets/blockFiles/campFireLitLight11.txt");
    public static final Block campFireLitLight10 = new BlockCampFireLit((short)72, 17, "src/spacegame/assets/blockFiles/campFireLitLight10.txt");
    public static final Block campFireLitLight9 = new BlockCampFireLit((short)73, 17, "src/spacegame/assets/blockFiles/campFireLitLight9.txt");
    public static final Block campFireLitLight8 = new BlockCampFireLit((short)74, 17, "src/spacegame/assets/blockFiles/campFireLitLight8.txt");
    public static final Block campFireLitLight7 = new BlockCampFireLit((short)75, 17, "src/spacegame/assets/blockFiles/campFireLitLight7.txt");
    public static final Block campFireLitLight6 = new BlockCampFireLit((short)76, 17, "src/spacegame/assets/blockFiles/campFireLitLight6.txt");
    public static final Block campFireLitLight5 = new BlockCampFireLit((short)77, 17, "src/spacegame/assets/blockFiles/campFireLitLight5.txt");
    public static final Block campFireLitLight4 = new BlockCampFireLit((short)78, 17, "src/spacegame/assets/blockFiles/campFireLitLight4.txt");
    public static final Block campFireLitLight3 = new BlockCampFireLit((short)79, 17, "src/spacegame/assets/blockFiles/campFireLitLight3.txt");
    public static final Block campFireLitLight2 = new BlockCampFireLit((short)80, 17, "src/spacegame/assets/blockFiles/campFireLitLight2.txt");
    public static final Block campFireLitLight1 = new BlockCampFireLit((short)81, 17, "src/spacegame/assets/blockFiles/campFireLitLight1.txt");
    public static final Block campFireBurnedOut = new Block((short)82, 19, "src/spacegame/assets/blockFiles/campFireBurnedOut.txt");
    public static final Block grassBlockLower = new Block((short)83, 20, "src/spacegame/assets/blockFiles/grassBlockLower.txt");
    public static final Block cactus = new BlockCactus((short)84, 21, "src/spacegame/assets/blockFiles/cactus.txt");
    public static final Block itemStone = new Block((short)85, stone.textureID, "src/spacegame/assets/blockFiles/itemStone.txt");
    public static final Block berryBushFlower = new BlockBerryBush((short)86, 27, "src/spacegame/assets/blockFiles/berryBush.txt");
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
    public ModelLoader blockModel;
    public String blockName;
    public String stepSound = "";
    public int breakTimer = 1;
    public String toolType = "";
    public float hardness;
    public boolean requiresTool;
    public float itemDropChance = 1;
    public AxisAlignedBB standardCollisionBoundingBox = standardBlock;

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

            if (properties[0].equals("canBeBroken")) {
                this.canBeBroken = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("isLightBlock")) {
                this.isLightBlock = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("lightBlockValue")) {
                this.lightBlockValue = Byte.parseByte(properties[1]);
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

    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player) {
        if (!this.canBeBroken) {return;}
        world.setBlockWithNotify(x, y, z, Block.air.ID);
        new SoundPlayer(SpaceGame.instance).playSound(x, y, z, new Sound(this.stepSound, false), new Random().nextFloat(0.6F, 1));
        if (player.inventory.itemStacks[selectedInventorySlot].durability != -1) {
            player.inventory.itemStacks[selectedInventorySlot].durability--;
        }
    }

    public void onRightClick(int x, int y, int z, World world, EntityPlayer player) {
        Chunk chunk = world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
        if (chunk.blocks == null) {chunk.initChunk();}
        if (chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)] != air.ID && chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y, z)] != water.ID) {return;}

        short heldItem = player.getHeldItem();
        if (heldItem == -1 || heldItem == Item.berry.ID) {return;}

        short heldBlock = 0;
        if (player.isHoldingBlock()) {
            heldBlock = player.getHeldBlock();
        } else if (heldItem == Item.torch.ID) {
            heldBlock = switch (facingDirection) {
                case 1 -> Block.torchNorth.ID;
                case 2 -> Block.torchSouth.ID;
                case 3 -> Block.torchEast.ID;
                case 4 -> Block.torchWest.ID;
                default -> Block.torchStandard.ID;
            };
        }

        switch (Item.list[heldItem].itemName){
           case "RAW_STONE":
               heldBlock = itemStone.ID;
               break;
        }



        new SoundPlayer(SpaceGame.instance).playSound(x, y, z, new Sound(list[player.getHeldBlock()].stepSound, false), new Random().nextFloat(0.6F, 1));
        player.removeItemFromInventory();
        world.setBlockWithNotify(x, y, z, heldBlock);
        player.isSwinging = true;
    }


    public static int getRandomTickRate(){
        return 60;
    }

    public int getDynamicBreakTimer(){
        short playerHeldItem = SpaceGame.instance.save.thePlayer.getHeldItem();
        if(playerHeldItem == -1){playerHeldItem = 0;}
        if(!this.requiresTool || !Item.list[playerHeldItem].toolType.equals(this.toolType)){
            return this.breakTimer;
        }
        float percentage = 1 - Item.list[playerHeldItem].hardness;
        if(percentage < 0){percentage = 0.01f;}
        return (int) (percentage * this.breakTimer);
    }

}
