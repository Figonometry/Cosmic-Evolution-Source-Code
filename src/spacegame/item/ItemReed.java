package spacegame.item;

import org.lwjgl.glfw.GLFW;
import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.gui.GuiCraftingReedStorage;
import spacegame.world.World;

public final class ItemReed extends Item {
    public ItemReed(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void onItemBlockRightClick(int x, int y, int z, World world, EntityPlayer player) {
        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && !KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))return;


        short playerHeldItem = player.getHeldItem();
        if (playerHeldItem != Item.NULL_ITEM_REFERENCE) {
            if (Item.list[playerHeldItem].toolType.equals(Item.ITEM_TOOL_TYPE_KNIFE)) {
                CosmicEvolution.instance.setNewGui(new GuiCraftingReedStorage(CosmicEvolution.instance, x, y, z));
                MouseListener.rightClickReleased = false;
            }
        }

        if(playerHeldItem == Item.stoneFragments.ID){
            world.addEntity(new EntityItem(x + 0.5, y + 0.5, z + 0.5, Item.reedSeed.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY));
            world.clearChestLocation(x,y,z);
            world.removeChestLocation(x,y,z);
            world.setBlockWithNotify(x,y,z, Block.air.ID, false);
            MouseListener.rightClickReleased = false;
        }
    }
}
