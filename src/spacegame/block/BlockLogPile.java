package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.KeyListener;
import spacegame.core.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.ChestLocation;
import spacegame.world.World;

import java.util.Random;

public final class BlockLogPile extends BlockPile {
    public BlockLogPile(short ID, int textureID, String filepath, short itemInPile, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, itemInPile, inventoryWidth, inventoryHeight);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();

        if(playerHeldItem == Item.fireWood.ID && player.getHeldItemCount() >= 2 && KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && (MouseListener.timeHeldRightClick == 0 || (((CosmicEvolution.instance.save.time - MouseListener.timeHeldRightClick) % 15) == 0))){
            ChestLocation chest = world.getChestLocation(x,y,z);
            if(chest.inventory.itemStacks[0].count >= 32)return;
            chest.inventory.itemStacks[0].count += 2;
            player.removeItemFromInventory();
            player.removeItemFromInventory();
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));
            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).notifyBlock(x,y,z);
            return;
        }

        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && player.addItemToInventory(Item.fireWood.ID, Item.NULL_ITEM_METADATA, (byte)2, Item.NULL_ITEM_DURABILITY) && (MouseListener.timeHeldRightClick == 0 || (((CosmicEvolution.instance.save.time - MouseListener.timeHeldRightClick) % 15) == 0))){
            ChestLocation chest = world.getChestLocation(x,y,z);
            chest.inventory.itemStacks[0].count -= 2;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));

            if(chest.inventory.itemStacks[0].count <= 0){
                chest.inventory.itemStacks[0].item = null;
                chest.inventory.itemStacks[0].count = 0;
                chest.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                chest.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
                world.removeChestLocation(x,y,z);
                world.setBlockWithNotify(x,y,z, Block.air.ID, false);
            }

            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).notifyBlock(x,y,z);
        }
    }
}
