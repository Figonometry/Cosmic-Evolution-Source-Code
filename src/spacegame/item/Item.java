package spacegame.item;

import spacegame.block.Block;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityPlayer;
import spacegame.world.World;

import java.io.*;

public class Item {
    public static final Item[] list = new Item[Short.MAX_VALUE];
    public static final Item block = new ItemBlock((short)0, -1, "src/spacegame/assets/itemFiles/block.txt");
    public static final Item torch = new Item((short)1, 1, "src/spacegame/assets/itemFiles/torch.txt");
    public static final Item stone = new Item((short)2, 2, "src/spacegame/assets/itemFiles/stone.txt");
    public static final Item stoneFragments = new ItemStoneFragments((short)3, 3, "src/spacegame/assets/itemFiles/stoneFragments.txt");
    public static final Item stoneHandAxe = new ItemAxe((short)4, 4, "src/spacegame/assets/itemFiles/stoneHandAxe.txt", Material.RAW_STONE);
    public static final Item berry = new ItemFood((short)5, 5, "src/spacegame/assets/itemFiles/berry.txt", 5f);
    public static final Item rawStick = new Item((short)6, 6, "src/spacegame/assets/itemFiles/rawStick.txt");
    public static final Item unlitTorch = new Item((short)7, 7, "src/spacegame/assets/itemFiles/unlitTorch.txt");
    public static final Item fireWood = new Item((short)8, 8, "src/spacegame/assets/itemFiles/fireWood.txt");
    public static final Item stoneHandKnifeBlade = new ItemKnife((short)9, 9, "src/spacegame/assets/itemFiles/stoneHandKnifeBlade.txt", Material.RAW_STONE);
    public static final Item stoneHandShovel = new ItemShovel((short)10, 10, "src/spacegame/assets/itemFiles/stoneHandShovel.txt", Material.RAW_STONE);
    public static final Item rawVenison = new ItemFood((short)11, 11, "src/spacegame/assets/itemFiles/rawVenison.txt", 5f);
    public static final Item straw = new Item((short)12, 12, "src/spacegame/assets/itemFiles/straw.txt");
    public static final Item strawBasket = new Item((short)13, 13, "src/spacegame/assets/itemFiles/strawBasket.txt");
    public static final Item clay = new Item((short)14, 14, "src/spacegame/assets/itemFiles/clay.txt");
    public static final Item clayAdobeBrick = new Item((short)15, 15, "src/spacegame/assets/itemFiles/clayAdobeBrick.txt");
    public final short ID;
    public final int textureID;
    public float hardness = 0;
    public boolean renderItemWithBlockModel;
    public byte stackLimit = 100;
    public float attackDamage;
    public boolean canPlaceOnGround = false;
    public String itemName;
    public String toolType = "";
    public Material material;
    private String displayName = "Undefined Name";
    public String itemType;
    public int storageLevel;
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

    public Item(short ID, int textureID, String filepath){
        if (list[ID] != null) {
            throw new RuntimeException("Block ID: " + ID + " ALREADY OCCUPIED WHEN ATTEMPTING TO ADD " + this + " TO THE LIST");
        }
        list[ID] = this;
        this.ID = ID;
        this.textureID = textureID;

        File itemFile = new File(filepath);
        if(!itemFile.exists()){
            throw new RuntimeException("Missing item file at " + filepath);
        }
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

    public float getTextureID(short ID, short metadata, int face){
        return list[ID].textureID;
    }

    public void onLeftClick(int x, int y, int z, World world, EntityPlayer player){

    }

    public void onRightClick(int x, int y, int z, World world, EntityPlayer player){

    }

    public String getDisplayName(short blockID){
        return this.ID == block.ID ? Block.list[blockID].displayName : this.displayName;
    }



    public void onDestroy(ItemStack itemStack, int accessor){
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].item = null;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].count = 0;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].metadata = 0;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].durability = 0;
    }

}
