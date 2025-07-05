package spacegame.item;

import spacegame.core.SpaceGame;
import spacegame.entity.EntityPlayer;
import spacegame.world.WorldFace;

import java.io.*;

public class Item {
    public static final Item[] list = new Item[Short.MAX_VALUE];
    public static final Item block = new ItemBlock((short)0, -1, "src/spacegame/assets/itemFiles/block.txt");
    public static final Item torch = new Item((short)1, 1, "src/spacegame/assets/itemFiles/torch.txt");
    public static final Item stone = new ItemStone((short)2, 2, "src/spacegame/assets/itemFiles/stone.txt", Material.RAW_STONE);
    public static final Item stoneFragments = new ItemStoneFragments((short)3, 3, "src/spacegame/assets/itemFiles/stoneFragments.txt");
    public static final Item stoneHandAxe = new ItemAxe((short)4, 4, "src/spacegame/assets/itemFiles/stoneHandAxe.txt", Material.RAW_STONE);
    public static final Item berry = new ItemBerry((short)5, 5, "src/spacegame/assets/itemFiles/berry.txt");
    public static final Item rawStick = new Item((short)6, 6, "src/spacegame/assets/itemFiles/rawStick.txt");
    public static final Item unlitTorch = new Item((short)7, 7, "src/spacegame/assets/itemFiles/unlitTorch.txt");
    public static final Item woodShards = new Item((short)8, 8, "src/spacegame/assets/itemFiles/woodShards.txt");
    public final short ID;
    public final int textureID;
    public short metadata;
    public short durability = -1; //If this is -1 that means the item has no durability and should never render a durability bar
    public float hardness = 0;
    public boolean renderItemWithBlockModel;
    public boolean canPlaceOnGround;
    public byte stackLimit = 100;
    public String itemName;
    public String toolType = "";
    public Material material;

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

            if (properties[0].equals("renderItemWithBlockModel")) {
                this.renderItemWithBlockModel = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("canPlaceOnGround")) {
                this.canPlaceOnGround = Boolean.parseBoolean(properties[1]);
            }

            if (properties[0].equals("stackLimit")) {
                this.stackLimit = Byte.parseByte(properties[1]);
            }

            if (properties[0].equals("itemName")) {
                this.itemName = properties[1];
            }

            if (properties[0].equals("toolType")) {
                this.itemName = properties[1];
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

    public void onLeftClick(int x, int y, int z, WorldFace worldFace, EntityPlayer player){

    }

    public void onRightClick(int x, int y, int z, WorldFace worldFace, EntityPlayer player){

    }

    public void onDestroy(ItemStack itemStack, int accessor){
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].item = null;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].count = 0;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].metadata = 0;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].durability = 0;
    }

}
