package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.World;

public final class BlockWater extends BlockFluid {
    public BlockWater(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;

        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem == Item.block.ID && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))){
            if(player.getHeldBlock() == Block.dirt.ID && player.addItemToInventory(Item.mud.ID, Item.NULL_ITEM_METADATA, (byte)8, Item.NULL_ITEM_DURABILITY)){
                player.removeItemFromInventory();
                MouseListener.rightClickReleased = false;
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_RIGHT_SHIFT);
                KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            }
        }
    }
}
