package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.ChestLocation;
import spacegame.world.World;

public final class BlockItem extends BlockContainer {
    public BlockItem(short ID, int textureID, String filepath, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, inventoryWidth, inventoryHeight);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;

        short playerHeldItem = player.getHeldItem();

        ChestLocation chest = world.getChestLocation(x,y,z);

        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && !KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT) && (playerHeldItem == Item.NULL_ITEM_REFERENCE
                || playerHeldItem == chest.inventory.itemStacks[0].item.ID)){
            if(player.addItemToInventory(chest.inventory.itemStacks[0].item.ID, chest.inventory.itemStacks[0].metadata, (byte)1, chest.inventory.itemStacks[0].durability, chest.inventory.itemStacks[0].decayTime)){
                chest.inventory.itemStacks[0].count = 0;
                chest.inventory.itemStacks[0].item = null;
                chest.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
                chest.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                chest.inventory.itemStacks[0].decayTime = 0L;
                world.removeChestLocation(x,y,z);
                world.setBlockWithNotify(x,y,z, Block.air.ID, false);
            }
            return;
        }

        if(playerHeldItem != Item.NULL_ITEM_REFERENCE){
            Item.list[world.getChestLocation(x,y,z).inventory.itemStacks[0].item.ID].onItemBlockRightClick(x,y,z, world, player);
        }
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        ChestLocation chest = CosmicEvolution.instance.save.activeWorld.getChestLocation(x,y,z);
        if(chest == null)return "Error: Chest is null";
        return chest.inventory.itemStacks[0].item.getDisplayName(chest.inventory.itemStacks[0].metadata);
    }


    @Override
    public int getBlockTexture(int face) {
        return switch (face){
            case 2 -> 44;
            case 3 -> 45;
            case 4 -> 46;
            case 5 -> 47;
            case 6 -> 48;
            case 7 -> 49;
            case 8 -> 50;
            case 9 -> 51;
            case 10 -> 52;
            case 11 -> 53;
            case 12 -> 54;
            case 13 -> 55;
            case 14 -> 56;
            case 15 -> 57;
            case 16 -> 58;
            case 17 -> 59;
            case 18 -> 60;
            case 19 -> 61;
            case 20 -> 62;

            default -> this.textureID;
        };

    }
}
