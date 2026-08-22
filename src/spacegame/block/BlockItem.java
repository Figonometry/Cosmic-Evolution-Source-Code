package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.gui.ToolTip;
import spacegame.gui.ToolTipGroup;
import spacegame.item.Item;
import spacegame.item.ItemIDList;
import spacegame.render.texturelists.BlockTextureList;
import spacegame.render.texturelists.ItemTextureList;
import spacegame.render.texturelists.MouseAndKeyIconTextureList;
import spacegame.world.World;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;

public final class BlockItem extends BlockContainer {
    public BlockItem(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;

        short playerHeldItem = player.getHeldItem();

        ChestLocation chest = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);

        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && !KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT) && (playerHeldItem == Item.NULL_ITEM_REFERENCE
                || playerHeldItem == chest.inventory.itemStacks[0].item.ID)){
            if(player.addItemToInventory(chest.inventory.itemStacks[0].item.ID, chest.inventory.itemStacks[0].metadata, (byte)1, chest.inventory.itemStacks[0].durability, chest.inventory.itemStacks[0].decayTime, null)){
                chest.inventory.itemStacks[0].count = 0;
                chest.inventory.itemStacks[0].item = null;
                chest.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
                chest.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                chest.inventory.itemStacks[0].decayTime = 0L;
                world.removeBlockState(x,y,z, MultiState.CHEST_STATE);
                world.setBlockWithNotify(x,y,z, Block.air.ID, false);
            }
            return;
        }

        if(playerHeldItem != Item.NULL_ITEM_REFERENCE){
            ChestLocation chestLocation = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
            Item.list[chestLocation.inventory.itemStacks[0].item.ID].onItemBlockRightClick(x,y,z, world, player);
        }
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        ChestLocation chest = (ChestLocation) CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CHEST_STATE);
        if(chest == null)return "Error: Chest is null";
        return chest.inventory.itemStacks[0].item.getDisplayName(chest.inventory.itemStacks[0].metadata);
    }



    //This method converts the texture from the item model to the equivalent value in the block texture array
    @Override
    public int getBlockTexture(int x, int y, int z, int texture) {
        return switch (texture){
            case ItemTextureList.STONE_TEXTURE  -> BlockTextureList.ITEM_STONE_TEXTURE;
            case ItemTextureList.BERRY_TEXTURE -> BlockTextureList.ITEM_BERRY_TEXTURE;
            case ItemTextureList.LEAF_TEXTURE -> BlockTextureList.ITEM_LEAF_TEXTURE;
            case ItemTextureList.FIREWOOD_TEXTURE -> BlockTextureList.ITEM_FIREWOOD_TEXTURE;
            case ItemTextureList.RAW_GAME_MEAT_TEXTURE -> BlockTextureList.ITEM_RAW_GAME_MEAT_TEXTURE;
            case ItemTextureList.COOKED_GAME_MEAT_TEXTURE -> BlockTextureList.ITEM_COOKED_GAME_MEAT_TEXTURE;
            case ItemTextureList.STRAW_TEXTURE -> BlockTextureList.ITEM_STRAW_TEXTURE;
            case ItemTextureList.TWINE_TEXTURE -> BlockTextureList.ITEM_TWINE_TEXTURE;
            case ItemTextureList.REED_TOP_TEXTURE -> BlockTextureList.ITEM_REED_TOP_TEXTURE;
            case ItemTextureList.CLAY_TEXTURE -> BlockTextureList.ITEM_CLAY_TEXTURE;
            case ItemTextureList.FIRED_RED_CLAY_TEXTURE -> BlockTextureList.ITEM_FIRED_RED_CLAY_TEXTURE;
            case ItemTextureList.MUD_TEXTURE -> BlockTextureList.ITEM_MUD_TEXTURE;
            case ItemTextureList.REED_STALK_TEXTURE -> BlockTextureList.ITEM_REED_STALK_TEXTURE;
            case ItemTextureList.LOG_TEXTURE -> BlockTextureList.ITEM_LOG_TEXTURE;
            case ItemTextureList.DEER_PELT_TOP_TEXTURE -> BlockTextureList.ITEM_DEER_PELT_TOP_TEXTURE;
            case ItemTextureList.ANIMAL_PELT_UNDER_TEXTURE -> BlockTextureList.ITEM_ANIMAL_PELT_UNDER_TEXTURE;
            case ItemTextureList.WOLF_PELT_TOP_TEXTURE -> BlockTextureList.ITEM_WOLF_PELT_TOP_TEXTURE;
            case ItemTextureList.ROT_TEXTURE -> BlockTextureList.ITEM_ROT_TEXTURE;
            case ItemTextureList.PRIMITIVE_DOOR_BASE_TEXTURE -> BlockTextureList.ITEM_PRIMITIVE_DOOR_BASE_TEXTURE;
            case ItemTextureList.SEED_WHEAT_FAMILY_TEXTURE -> BlockTextureList.ITEM_SEED_WHEAT_FAMILY_TEXTURE;
            case ItemTextureList.BONE_TEXTURE -> BlockTextureList.ITEM_BONE_TEXTURE;
            case ItemTextureList.BONEMEAL_TEXTURE -> BlockTextureList.ITEM_BONEMEAL_TEXTURE;
            case ItemTextureList.EINKORN_WHEAT_TEXTURE -> BlockTextureList.ITEM_EINKORN_WHEAT;
            case ItemTextureList.WHEAT_TEXTURE -> BlockTextureList.ITEM_WHEAT_TEXTURE;

            default -> this.textureID;
        };

    }


    @Override
    public void registerBlockTooltips(){
        this.tooltips = new ToolTipGroup[4][1];

        for(int i = 0; i < this.tooltips.length; i++){
            for(int k = 0; k < this.tooltips[i].length; k++){
                this.tooltips[i][k] = new ToolTipGroup();
            };
        }

        ToolTip berryRightClick = new ToolTip();
        berryRightClick.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        berryRightClick.addText("with");
        berryRightClick.addItemID(ItemIDList.STONE_FRAGMENTS);
        berryRightClick.addText("to create seeds");

        this.tooltips[0][0].addToolTip(berryRightClick);



        ToolTip boneRightClick = new ToolTip();
        boneRightClick.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        boneRightClick.addText("with");
        boneRightClick.addItemID(ItemIDList.STONE_FRAGMENTS);
        boneRightClick.addText("to create");
        boneRightClick.addItemID(ItemIDList.BONEMEAL);

        this.tooltips[1][0].addToolTip(boneRightClick);


        ToolTip peltRightClick = new ToolTip();
        peltRightClick.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        peltRightClick.addText("with");
        peltRightClick.addItemID(ItemIDList.STONE_KNIFE);
        peltRightClick.addText("to craft primitive clothing");


        this.tooltips[2][0].addToolTip(peltRightClick);


        ToolTip reedRightClick = new ToolTip();
        reedRightClick.addKeyWithBoxOutline("SHIFT");
        reedRightClick.addText("+");
        reedRightClick.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        reedRightClick.addText("with");
        reedRightClick.addItemID(ItemIDList.STONE_KNIFE);
        reedRightClick.addText("to craft");


        ToolTip reedRightClick2 = new ToolTip();
        reedRightClick.addKeyWithBoxOutline("SHIFT");
        reedRightClick.addText("+");
        reedRightClick.addMouseIcon(MouseAndKeyIconTextureList.RIGHT_CLICK);
        reedRightClick.addText("with");
        reedRightClick.addItemID(ItemIDList.STONE_FRAGMENTS);
        reedRightClick.addText("to create seeds");


        this.tooltips[3][0].addToolTip(reedRightClick);
        this.tooltips[3][0].addToolTip(reedRightClick2);
    }

    @Override
    public ToolTipGroup[] getBlockToolTips(int x, int y, int z, World world, EntityPlayer player){
        ChestLocation chestLocation = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
        if(chestLocation == null)return null;

        switch (chestLocation.inventory.itemStacks[0].item.ID){
            case ItemIDList.BERRY -> {
                return this.tooltips[0];
            }
            case ItemIDList.BONE -> {
                return this.tooltips[1];
            }
            case ItemIDList.DEER_PELT, ItemIDList.WOLF_PELT ->{
                return this.tooltips[2];
            }
            case ItemIDList.REEDS -> {
                return this.tooltips[3];
            }
        }


        return null;
    }

}
