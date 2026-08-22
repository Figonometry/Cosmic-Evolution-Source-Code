package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.AxisAlignedBB;
import spacegame.world.World;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;

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
            ChestLocation chest = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
            if(chest.inventory.itemStacks[0].count >= 32)return;
            chest.inventory.itemStacks[0].count += 2;
            player.removeItemFromInventory();
            player.removeItemFromInventory();
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));
            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).notifyBlock(x,y,z);
            world.notifyChunk(x,y,z);
            return;
        }

        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && player.addItemToInventory(Item.fireWood.ID, Item.NULL_ITEM_METADATA, (byte)2, Item.NULL_ITEM_DURABILITY, 0, null) && (MouseListener.timeHeldRightClick == 0 || (((CosmicEvolution.instance.save.time - MouseListener.timeHeldRightClick) % 15) == 0))){
            ChestLocation chest = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
            chest.inventory.itemStacks[0].count -= 2;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(Sound.wood, false, 1f), new Random().nextFloat(0.6F, 1));

            if(chest.inventory.itemStacks[0].count <= 0){
                chest.inventory.itemStacks[0].item = null;
                chest.inventory.itemStacks[0].count = 0;
                chest.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                chest.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
                world.removeBlockState(x,y,z, MultiState.CHEST_STATE);
                world.setBlockWithNotify(x,y,z, Block.air.ID, false);
            }

            world.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5).notifyBlock(x,y,z);
            world.notifyChunk(x,y,z);
        }
    }

    @Override
    public void adjustBoundingBox(int x, int y, int z, AxisAlignedBB axisAlignedBB){
        ChestLocation chestLocation = (ChestLocation) CosmicEvolution.instance.save.activeWorld.getBlockState(x, y, z, MultiState.CHEST_STATE);
        int logCount = chestLocation.inventory.itemStacks[0].count / 2;
        if (logCount <= 4) {
            axisAlignedBB.minX = x + BlockAxisAlignedBBList.quarterBlock.minX;
            axisAlignedBB.maxX = x + BlockAxisAlignedBBList.quarterBlock.maxX;
            axisAlignedBB.minY = y + BlockAxisAlignedBBList.quarterBlock.minY;
            axisAlignedBB.maxY = y + BlockAxisAlignedBBList.quarterBlock.maxY;
            axisAlignedBB.minZ = z + BlockAxisAlignedBBList.quarterBlock.minZ;
            axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.quarterBlock.maxZ;
        } else if (logCount <= 8) {
            axisAlignedBB.minX = x + BlockAxisAlignedBBList.slab.minX;
            axisAlignedBB.maxX = x + BlockAxisAlignedBBList.slab.maxX;
            axisAlignedBB.minY = y + BlockAxisAlignedBBList.slab.minY;
            axisAlignedBB.maxY = y + BlockAxisAlignedBBList.slab.maxY;
            axisAlignedBB.minZ = z + BlockAxisAlignedBBList.slab.minZ;
            axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.slab.maxZ;
        } else if (logCount <= 12) {
            axisAlignedBB.minX = x + BlockAxisAlignedBBList.threeQuartersBlock.minX;
            axisAlignedBB.maxX = x + BlockAxisAlignedBBList.threeQuartersBlock.maxX;
            axisAlignedBB.minY = y + BlockAxisAlignedBBList.threeQuartersBlock.minY;
            axisAlignedBB.maxY = y + BlockAxisAlignedBBList.threeQuartersBlock.maxY;
            axisAlignedBB.minZ = z + BlockAxisAlignedBBList.threeQuartersBlock.minZ;
            axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.threeQuartersBlock.maxZ;
        } else {
            axisAlignedBB.minX = x + BlockAxisAlignedBBList.standardBlock.minX;
            axisAlignedBB.maxX = x + BlockAxisAlignedBBList.standardBlock.maxX;
            axisAlignedBB.minY = y + BlockAxisAlignedBBList.standardBlock.minY;
            axisAlignedBB.maxY = y + BlockAxisAlignedBBList.standardBlock.maxY;
            axisAlignedBB.minZ = z + BlockAxisAlignedBBList.standardBlock.minZ;
            axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.standardBlock.maxZ;
        }
    }
}
