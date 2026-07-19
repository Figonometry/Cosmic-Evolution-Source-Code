package spacegame.item;

import spacegame.block.Block;
import spacegame.entity.EntityPlayer;
import spacegame.render.model.ModelLoader;
import spacegame.world.World;

import java.io.*;

public class Item {
    public static String modelFolderPath = "src/spacegame/assets/models/itemModels/";
    public static final Item[] list = new Item[Short.MAX_VALUE];
    public static final Item block = new Item((short)0, null, "src/spacegame/assets/itemFiles/block.txt");
    public static final Item stoneHoeHead = new ItemTool((short) 1, modelFolderPath + "stoneHoeHead.obj", "src/spacegame/assets/itemFiles/stoneHoeHead.txt");
    public static final Item stoneHoe = new ItemHoe((short) 2, modelFolderPath + "stoneHoe.obj", "src/spacegame/assets/itemFiles/stoneHoe.txt", Material.STONE);
    public static final Item stoneFragments = new Item((short)3,  modelFolderPath + "stoneFragments.obj", "src/spacegame/assets/itemFiles/stoneFragments.txt");
    public static final Item stoneHandAxe = new ItemAxe((short)4, modelFolderPath + "stoneHandAxe.obj", "src/spacegame/assets/itemFiles/stoneHandAxe.txt", Material.RAW_STONE);
    public static final Item berry = new ItemBerry((short)5, modelFolderPath + "berry.obj", "src/spacegame/assets/itemFiles/berry.txt", 50f);
    public static final Item seedWildGrass = new ItemSeed((short)6, modelFolderPath + "seedWildGrass.obj", "src/spacegame/assets/itemFiles/seedWildGrass.txt");
    public static final Item seedEinkornWheat = new ItemSeed((short)7, modelFolderPath + "seedEinkornWheat.obj", "src/spacegame/assets/itemFiles/seedEinkornWheat.txt");
    public static final Item fireWood = new ItemFirewood((short)8, modelFolderPath + "firewood.obj", "src/spacegame/assets/itemFiles/fireWood.txt");
    public static final Item stoneHandKnifeBlade = new ItemKnife((short)9, modelFolderPath + "stoneKnifeBlade.obj", "src/spacegame/assets/itemFiles/stoneHandKnifeBlade.txt", Material.RAW_STONE);
    public static final Item stoneHandShovel = new ItemShovel((short)10, modelFolderPath + "stoneHandShovel.obj", "src/spacegame/assets/itemFiles/stoneHandShovel.txt", Material.RAW_STONE);
    public static final Item rawGameMeat = new ItemRawGameMeat((short)11, modelFolderPath + "rawGameMeat.obj", "src/spacegame/assets/itemFiles/rawGameMeat.txt", 5f);
    public static final Item straw = new Item((short)12, modelFolderPath + "straw.obj", "src/spacegame/assets/itemFiles/straw.txt");
    public static final Item reedBasket = new Item((short)13, modelFolderPath + "reedBasket.obj", "src/spacegame/assets/itemFiles/reedBasket.txt");
    public static final Item einkornWheat = new ItemFood((short)14, modelFolderPath + "einkornWheat.obj", "src/spacegame/assets/itemFiles/einkornWheat.txt", 5f);
    public static final Item rawClayAdobeBrick = new Item((short)15, modelFolderPath + "rawRedClayBrick.obj", "src/spacegame/assets/itemFiles/rawClayAdobeBrick.txt");
    public static final Item firedRedClayAdobeBrick = new Item((short)16, modelFolderPath + "redClayBrick.obj", "src/spacegame/assets/itemFiles/firedRedClayAdobeBrick.txt");
    public static final Item mud = new Item((short)17, modelFolderPath + "mud.obj", "src/spacegame/assets/itemFiles/mud.txt");
    public static final Item reeds = new ItemReed((short)18, modelFolderPath + "reeds.obj", "src/spacegame/assets/itemFiles/reeds.txt");
    public static final Item bone = new ItemBone((short)19, modelFolderPath + "bone.obj", "src/spacegame/assets/itemFiles/bone.txt");
    public static final Item boneMeal = new Item((short)20, modelFolderPath + "boneMeal.obj", "src/spacegame/assets/itemFiles/boneMeal.txt"); //20
    public static final Item seedEmmerWheat = new ItemSeed((short)21, modelFolderPath + "seedEmmerWheat.obj", "src/spacegame/assets/itemFiles/emmerWheat.txt");
    public static final Item cookedGameMeat = new ItemCookedGameMeat((short)22, modelFolderPath + "cookedGameMeat.obj", "src/spacegame/assets/itemFiles/cookedGameMeat.txt", 300f);
    public static final Item reedTwine = new Item((short)23, modelFolderPath + "twine.obj", "src/spacegame/assets/itemFiles/reedTwine.txt");
    public static final Item reedCraftingGridTop = new Item((short)24, modelFolderPath + "craftingGridTop.obj", "src/spacegame/assets/itemFiles/reedCraftingGridTop.txt");
    public static final Item stoneAxe = new ItemAxe((short)25, modelFolderPath + "stoneAxe.obj", "src/spacegame/assets/itemFiles/stoneAxe.txt", Material.STONE);
    public static final Item stoneShovel = new ItemShovel((short)26, modelFolderPath + "stoneShovel.obj", "src/spacegame/assets/itemFiles/stoneShovel.txt", Material.STONE);
    public static final Item stoneKnife = new ItemKnife((short)27, modelFolderPath + "stoneKnife.obj", "src/spacegame/assets/itemFiles/stoneKnife.txt", Material.STONE);
    public static final Item deerPelt = new ItemPelt((short)28, modelFolderPath + "deerPelt.obj", "src/spacegame/assets/itemFiles/deerPelt.txt");
    public static final Item rot = new Item((short)29, modelFolderPath + "rot.obj", "src/spacegame/assets/itemFiles/rot.txt");
    public static final Item wolfPelt = new ItemPelt((short)30, modelFolderPath + "wolfPelt.obj", "src/spacegame/assets/itemFiles/wolfPelt.txt");
    public static final Item primitiveDoor = new Item((short)31, modelFolderPath + "primitiveDoor.obj", "src/spacegame/assets/itemFiles/primitiveDoor.txt");
    public static final Item primitiveDeerPeltClothing = new ItemClothing((short)32, modelFolderPath + "primitiveDeerPeltClothing.obj", "src/spacegame/assets/itemFiles/primitiveDeerPeltClothing.txt");
    public static final Item primitiveWolfPeltClothing = new ItemClothing((short)33, modelFolderPath + "primitiveWolfPeltClothing.obj", "src/spacegame/assets/itemFiles/primitiveWolfPeltClothing.txt");
    public static final Item emmerWheat = new ItemFood((short)34, modelFolderPath + "einkornWheat.obj", "src/spacegame/assets/itemFiles/emmerWheat.txt", 10f);
    public static final Item seedStandardWheat = new ItemSeed((short)35, modelFolderPath + "seedStandardWheat.obj", "src/spacegame/assets/itemFiles/seedStandardWheat.txt");
    public static final Item wheat = new ItemFood((short)36, modelFolderPath + "wheat.obj", "src/spacegame/assets/itemFiles/wheat.txt", 20f);
    public static final Item speltWheat = new ItemFood((short)37, modelFolderPath + "wheat.obj", "src/spacegame/assets/itemFiles/speltWheat.txt", 30f);
    public static final Item seedSpeltWheat = new ItemSeed((short)38, modelFolderPath + "seedSpeltWheat.obj", "src/spacegame/assets/itemFiles/seedSpeltWheat.txt");
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
    private String displayName = "Undefined Name";
    public String itemType;
    public int storageLevel;
    public ModelLoader itemModel;
    public static final short NULL_ITEM_REFERENCE = -1;
    public static final short NULL_ITEM_DURABILITY = -1;
    public static final short NULL_ITEM_METADATA = 0;
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
    public short durability = NULL_ITEM_DURABILITY;   //If this is -1 that means the item has no durability and should never render a durability bar
    public short metadata = NULL_ITEM_METADATA;

    public Item(short ID, String modelFilePath, String filepath){
        if (list[ID] != null) {
            throw new RuntimeException("Block ID: " + ID + " ALREADY OCCUPIED WHEN ATTEMPTING TO ADD " + this + " TO THE LIST");
        }
        list[ID] = this;
        this.ID = ID;

        File itemFile = new File(filepath);
        if(!itemFile.exists()){
            throw new RuntimeException("Missing item file at " + filepath);
        }

        if(modelFilePath == null || !new File(modelFilePath).exists())modelFilePath = modelFolderPath + "defaultItem.obj"; //If the passed in model doesnt yet exist assign the default item model
        this.itemModel = new ModelLoader(modelFilePath, true);


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
        return ID == block.ID ?  Block.list[metadata].getBlockTexture(metadata, face) : 0;
    }

    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){

    }

    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){

    }
    public void onFinishRightClickAnimation(int x, int y, int z, World world, EntityPlayer player){

    }

    public String getDisplayName(short blockID){
        return this.ID == block.ID ? Block.list[blockID].getDisplayName(0,0,0) : this.displayName;
    }



    public void onDestroy(ItemStack itemStack){
        itemStack.item = null;
        itemStack.count = 0;
        itemStack.metadata = 0;
        itemStack.durability = 0;
        itemStack.decayTime = 0L;
        itemStack.itemState = null;
    }

}
