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


        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && !KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT) && playerHeldItem == Item.NULL_ITEM_REFERENCE){
            ChestLocation chest = world.getChestLocation(x,y,z);
            if(player.addItemToInventory(chest.inventory.itemStacks[0].item.ID, chest.inventory.itemStacks[0].metadata, (byte)1, chest.inventory.itemStacks[0].durability)){
                chest.inventory.itemStacks[0].count = 0;
                chest.inventory.itemStacks[0].item = null;
                chest.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
                chest.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                world.removeChestLocation(x,y,z);
                world.setBlockWithNotify(x,y,z, Block.air.ID);
            }
        }
    }

    @Override
    public String getDisplayName(int x, int y, int z){
        ChestLocation chest = CosmicEvolution.instance.save.activeWorld.getChestLocation(x,y,z);
        return chest.inventory.itemStacks[0].item.getDisplayName(chest.inventory.itemStacks[0].metadata);
    }


}
