package spacegame.item;

import spacegame.block.Block;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.render.model.ModelLoader;
import spacegame.world.worldtypes.World;

import java.io.*;

public class Item {
    public static String modelFolderPath = "src/spacegame/assets/models/itemModels/";
    public static final Item[] list = new Item[Short.MAX_VALUE];
    public static final Item block = new Item(ItemIDList.BLOCK, "Block Model not needed", "src/spacegame/assets/itemFiles/block.txt");
    public static final Item stoneHoeHead = new ItemTool(ItemIDList.STONE_HOE_HEAD, modelFolderPath + "stoneHoeHead.obj", "src/spacegame/assets/itemFiles/stoneHoeHead.txt");
    public static final Item stoneHoe = new ItemHoe(ItemIDList.STONE_HOE, modelFolderPath + "stoneHoe.obj", "src/spacegame/assets/itemFiles/stoneHoe.txt", Material.STONE);
    public static final Item stoneFragments = new ItemTool(ItemIDList.STONE_FRAGMENTS,  modelFolderPath + "stoneFragments.obj", "src/spacegame/assets/itemFiles/stoneFragments.txt");
    public static final Item stoneHandAxe = new ItemAxe(ItemIDList.STONE_HAND_AXE, modelFolderPath + "stoneHandAxe.obj", "src/spacegame/assets/itemFiles/stoneHandAxe.txt", Material.RAW_STONE);
    public static final Item berry = new ItemBerry(ItemIDList.BERRY, modelFolderPath + "berry.obj", "src/spacegame/assets/itemFiles/berry.txt", 50f);
    public static final Item seedWildGrass = new ItemSeed(ItemIDList.SEED_WILD_GRASS, modelFolderPath + "seedWildGrass.obj", "src/spacegame/assets/itemFiles/seedWildGrass.txt");
    public static final Item seedEinkornWheat = new ItemSeed(ItemIDList.SEED_EINKORN_WHEAT, modelFolderPath + "seedEinkornWheat.obj", "src/spacegame/assets/itemFiles/seedEinkornWheat.txt");
    public static final Item fireWood = new ItemFirewood(ItemIDList.FIREWOOD, modelFolderPath + "firewood.obj", "src/spacegame/assets/itemFiles/fireWood.txt");
    public static final Item stoneHandKnifeBlade = new ItemKnife(ItemIDList.STONE_HAND_KNIFE_BLADE, modelFolderPath + "stoneKnifeBlade.obj", "src/spacegame/assets/itemFiles/stoneHandKnifeBlade.txt", Material.RAW_STONE);
    public static final Item stoneHandShovel = new ItemShovel(ItemIDList.STONE_HAND_SHOVEL, modelFolderPath + "stoneHandShovel.obj", "src/spacegame/assets/itemFiles/stoneHandShovel.txt", Material.RAW_STONE);
    public static final Item rawGameMeat = new ItemRawGameMeat(ItemIDList.RAW_GAME_MEAT, modelFolderPath + "rawGameMeat.obj", "src/spacegame/assets/itemFiles/rawGameMeat.txt", 5f);
    public static final Item straw = new Item(ItemIDList.STRAW, modelFolderPath + "straw.obj", "src/spacegame/assets/itemFiles/straw.txt");
    public static final Item reedBasket = new Item(ItemIDList.REED_BASKET, modelFolderPath + "reedBasket.obj", "src/spacegame/assets/itemFiles/reedBasket.txt");
    public static final Item einkornWheat = new ItemFood(ItemIDList.EINKORN_WHEAT, modelFolderPath + "einkornWheat.obj", "src/spacegame/assets/itemFiles/einkornWheat.txt", 5f);
    public static final Item rawClayAdobeBrick = new Item(ItemIDList.RAW_CLAY_ADOBE_BRICK, modelFolderPath + "rawRedClayBrick.obj", "src/spacegame/assets/itemFiles/rawClayAdobeBrick.txt");
    public static final Item firedRedClayAdobeBrick = new Item(ItemIDList.FIRED_RED_CLAY_ADOBE_BRICK, modelFolderPath + "redClayBrick.obj", "src/spacegame/assets/itemFiles/firedRedClayAdobeBrick.txt");
    public static final Item mud = new Item(ItemIDList.MUD, modelFolderPath + "mud.obj", "src/spacegame/assets/itemFiles/mud.txt");
    public static final Item reeds = new ItemReed(ItemIDList.REEDS, modelFolderPath + "reeds.obj", "src/spacegame/assets/itemFiles/reeds.txt");
    public static final Item bone = new ItemBone(ItemIDList.BONE, modelFolderPath + "bone.obj", "src/spacegame/assets/itemFiles/bone.txt");
    public static final Item boneMeal = new Item(ItemIDList.BONEMEAL, modelFolderPath + "boneMeal.obj", "src/spacegame/assets/itemFiles/boneMeal.txt"); //20
    public static final Item seedEmmerWheat = new ItemSeed(ItemIDList.SEED_EMMER_WHEAT, modelFolderPath + "seedEmmerWheat.obj", "src/spacegame/assets/itemFiles/emmerWheat.txt");
    public static final Item cookedGameMeat = new ItemCookedGameMeat(ItemIDList.COOKED_GAME_MEAT, modelFolderPath + "cookedGameMeat.obj", "src/spacegame/assets/itemFiles/cookedGameMeat.txt", 300f);
    public static final Item reedTwine = new Item(ItemIDList.REED_TWINE, modelFolderPath + "twine.obj", "src/spacegame/assets/itemFiles/reedTwine.txt");
    public static final Item reedCraftingGridTop = new Item(ItemIDList.REED_CRAFTING_GRID_TOP, modelFolderPath + "craftingGridTop.obj", "src/spacegame/assets/itemFiles/reedCraftingGridTop.txt");
    public static final Item stoneAxe = new ItemAxe(ItemIDList.STONE_AXE, modelFolderPath + "stoneAxe.obj", "src/spacegame/assets/itemFiles/stoneAxe.txt", Material.STONE);
    public static final Item stoneShovel = new ItemShovel(ItemIDList.STONE_SHOVEL, modelFolderPath + "stoneShovel.obj", "src/spacegame/assets/itemFiles/stoneShovel.txt", Material.STONE);
    public static final Item stoneKnife = new ItemKnife(ItemIDList.STONE_KNIFE, modelFolderPath + "stoneKnife.obj", "src/spacegame/assets/itemFiles/stoneKnife.txt", Material.STONE);
    public static final Item deerPelt = new ItemPelt(ItemIDList.DEER_PELT, modelFolderPath + "deerPelt.obj", "src/spacegame/assets/itemFiles/deerPelt.txt");
    public static final Item rot = new Item(ItemIDList.ROT, modelFolderPath + "rot.obj", "src/spacegame/assets/itemFiles/rot.txt");
    public static final Item wolfPelt = new ItemPelt(ItemIDList.WOLF_PELT, modelFolderPath + "wolfPelt.obj", "src/spacegame/assets/itemFiles/wolfPelt.txt");
    public static final Item primitiveDoor = new Item(ItemIDList.PRIMITIVE_DOOR, modelFolderPath + "primitiveDoor.obj", "src/spacegame/assets/itemFiles/primitiveDoor.txt");
    public static final Item primitiveDeerPeltClothing = new ItemClothing(ItemIDList.PRIMITIVE_DEER_PELT_CLOTHING, modelFolderPath + "primitiveDeerPeltClothing.obj", "src/spacegame/assets/itemFiles/primitiveDeerPeltClothing.txt");
    public static final Item primitiveWolfPeltClothing = new ItemClothing(ItemIDList.PRIMITIVE_WOLF_PELT_CLOTHING, modelFolderPath + "primitiveWolfPeltClothing.obj", "src/spacegame/assets/itemFiles/primitiveWolfPeltClothing.txt");
    public static final Item emmerWheat = new ItemFood(ItemIDList.EMMER_WHEAT, modelFolderPath + "einkornWheat.obj", "src/spacegame/assets/itemFiles/emmerWheat.txt", 10f);
    public static final Item seedStandardWheat = new ItemSeed(ItemIDList.SEED_STANDARD_WHEAT, modelFolderPath + "seedStandardWheat.obj", "src/spacegame/assets/itemFiles/seedStandardWheat.txt");
    public static final Item wheat = new ItemFood(ItemIDList.WHEAT, modelFolderPath + "wheat.obj", "src/spacegame/assets/itemFiles/wheat.txt", 20f);
    public static final Item speltWheat = new ItemFood(ItemIDList.SPELT_WHEAT, modelFolderPath + "wheat.obj", "src/spacegame/assets/itemFiles/speltWheat.txt", 30f);
    public static final Item seedSpeltWheat = new ItemSeed(ItemIDList.SEED_SPELT_WHEAT, modelFolderPath + "seedSpeltWheat.obj", "src/spacegame/assets/itemFiles/seedSpeltWheat.txt");
    public static final Item stoneSpearHead = new ItemTool(ItemIDList.STONE_SPEAR_HEAD, modelFolderPath + "stoneSpearHead.obj", "src/spacegame/assets/itemFiles/stoneSpearHead.txt");
    public static final Item stoneSpear = new ItemSpear(ItemIDList.STONE_SPEAR, modelFolderPath + "stoneSpear.obj", "src/spacegame/assets/itemFiles/stoneSpear.txt", Material.STONE);
    public final short ID;
    public float hardness = 0;
    public boolean canPlaceAsItemBlock;
    public boolean renderItemWithBlockModel;
    public byte stackLimit = 64;
    public float attackDamage;
    public boolean canPlaceOnGround = false;
    public String itemName;
    public String toolType = "";
    public Material material;
    protected String displayName = "Undefined Name";
    public String itemType;
    public int storageLevel;
    public ModelLoader itemModel;
    public int hitDistance = 3;
    public boolean canDrawBack = false;
    public static final short NULL_ITEM_REFERENCE = -1;
    public static final short NULL_ITEM_DURABILITY = -1;
    public static final short NULL_ITEM_METADATA = -1;
    public static final String ITEM_TYPE_PLAYER_STORAGE = "playerStorage";
    public static final String ITEM_TYPE_ARMOR_HEAD = "armorHead";
    public static final String ITEM_TYPE_ARMOR_TORSO = "armorTorso";
    public static final String ITEM_TYPE_ARMOR_LEGS = "armorLegs";
    public static final String ITEM_TYPE_ARMOR_FEET = "armorFeet";
    public static final String ITEM_TYPE_CLOTHING_HEAD = "clothingHead";
    public static final String ITEM_TYPE_CLOTHING_TORSO = "clothingTorso";
    public static final String ITEM_TYPE_CLOTHING_LEGS = "clothingLegs";
    public static final String ITEM_TYPE_CLOTHING_FEET = "clothingFeet";
    public static final String ITEM_TYPE_OFFHAND = "offhand";
    public static final String ITEM_TOOL_TYPE_KNIFE = "knife";
    public static final String ITEM_TOOL_TYPE_SHOVEL = "shovel";
    public static final String ITEM_TOOL_TYPE_AXE = "axe";
    public static final String ITEM_TOOL_TYPE_PICKAXE = "pickaxe";
    public short durability = NULL_ITEM_DURABILITY;   //If this is -1 that means the item has no durability and should never render a durability bar
    public short metadata = NULL_ITEM_METADATA;


    public Item(short ID, String modelFilePath, String filepath){
        this(ID, new ModelLoader(modelFilePath, true), filepath);
    }

    public Item(short ID, ModelLoader itemModel, String filepath){
        if (list[ID] != null) {
            throw new RuntimeException("Block ID: " + ID + " ALREADY OCCUPIED WHEN ATTEMPTING TO ADD " + this + " TO THE LIST");
        }
        list[ID] = this;
        this.ID = ID;

        File itemFile = new File(filepath);
        if(!itemFile.exists()){
            throw new RuntimeException("Missing item file at " + filepath);
        }

        this.itemModel = itemModel;


        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(itemFile));
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

            if (properties[0].equals("durability")) {
                this.durability = Short.parseShort(properties[1]);
            }

            if (properties[0].equals("hardness")) {
                this.hardness = Float.parseFloat(properties[1]);
            }

            if (properties[0].equals("attackDamage")) {
                this.attackDamage = Float.parseFloat(properties[1]);
            }

            if (properties[0].equals("itemType")) {
                this.itemType = properties[1];
            }

            if (properties[0].equals("renderItemWithBlockModel")) {
                this.renderItemWithBlockModel = Boolean.parseBoolean(properties[1]);
            }

            if(properties[0].equals("storageLevel")){
                this.storageLevel = Integer.parseInt(properties[1]);
            }

            if(properties[0].equals("canPlaceAsItemBlock")){
                this.canPlaceAsItemBlock = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("displayName")) {
                this.displayName = properties[1];
            }

            if(properties[0].equals("canDrawBack")){
                this.canDrawBack = Boolean.parseBoolean(properties[1]);
            }

            if(properties[0].equals("canPlaceOnGround")){
                this.canPlaceOnGround = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("stackLimit")) {
                this.stackLimit = Byte.parseByte(properties[1]);
            }

            if (properties[0].equals("itemName")) {
                this.itemName = properties[1];
            }

            if (properties[0].equals("toolType")) {
                this.toolType = properties[1];
            }

            if(properties[0].equals("hitDistance")){
                this.hitDistance = Integer.parseInt(properties[1]);
            }
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void onItemBlockRightClick(int x, int y, int z, World world, EntityPlayer player){

    }

    public float getTextureID(short ID, short metadata, int face){
        return ID == block.ID ?  Block.list[metadata].getBlockTexture(metadata, 0, 0, 0, face) : 0;
    }

    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){

    }

    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){

    }
    public void onFinishRightClickAnimation(int x, int y, int z, World world, EntityPlayer player){

    }

    public void onDrawBackRelease(EntityPlayer entityPlayer, World world){

    }

    public short getDurability(short metadata){
        return this.durability;
    }

    public ModelLoader getItemModel(short itemMetadata){
        return this.itemModel;
    }

    public String getDisplayName(short blockID, short metadata){
        return this.ID == block.ID ? Block.list[blockID].getDisplayName(0,0,0) : this.displayName;
    }

    public Sound getEntityHitSound(){
        return new Sound(Sound.stabEntity, false, 0f);
    }



    public void onDestroy(ItemStack itemStack){
        itemStack.clearDataFromStack();
    }

}
