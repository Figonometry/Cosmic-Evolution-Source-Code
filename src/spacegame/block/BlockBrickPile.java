package spacegame.block;

import org.lwjgl.glfw.GLFW;
import spacegame.core.CosmicEvolution;
import spacegame.core.eventlisteners.KeyListener;
import spacegame.core.eventlisteners.MouseListener;
import spacegame.core.Sound;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityPlayer;
import spacegame.item.Item;
import spacegame.world.AxisAlignedBB;
import spacegame.world.worldtypes.World;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.blockstate.MultiState;

import java.util.Random;

public final class BlockBrickPile extends BlockPile {
    public BlockBrickPile(short ID, int textureID, String filepath, short itemInPile, int inventoryWidth, int inventoryHeight) {
        super(ID, textureID, filepath, itemInPile, inventoryWidth, inventoryHeight);
    }

    @Override
    public void handleSpecialRightClickFunctions(int x, int y, int z, World world, EntityPlayer player){
        if(!MouseListener.rightClickReleased)return;
        short playerHeldItem = player.getHeldItem();


        if((playerHeldItem == Item.rawClayAdobeBrick.ID || playerHeldItem == Item.firedRedClayAdobeBrick.ID) && player.getHeldItemCount() >= 1 && KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && (MouseListener.timeHeldRightClick == 0 || (((CosmicEvolution.instance.save.time - MouseListener.timeHeldRightClick) % 15) == 0))){
            ChestLocation chest = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
            if(chest.inventory.itemStacks[0].count >= 48)return;
            chest.inventory.itemStacks[0].count++;
            player.removeItemFromInventory();
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound(playerHeldItem == Item.rawClayAdobeBrick.ID ? Sound.clay : Sound.stone, false, 1f), new Random().nextFloat(0.6F, 1));
            world.notifyChunk(x,y,z);
            return;
        }
        ChestLocation chest = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);


        if(!KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) && CosmicEvolution.instance.save.thePlayer.addItemToInventory(chest.inventory.itemStacks[0].item.ID, Item.NULL_ITEM_METADATA, (byte)1, Item.NULL_ITEM_DURABILITY, 0, null) && (MouseListener.timeHeldRightClick == 0 || (((CosmicEvolution.instance.save.time - MouseListener.timeHeldRightClick) % 15) == 0))){
            chest.inventory.itemStacks[0].count--;
            KeyListener.setKeyReleased(GLFW.GLFW_KEY_LEFT_SHIFT);
            CosmicEvolution.instance.soundPlayer.playSound(x, y, z, new Sound( chest.inventory.itemStacks[0].item.ID == Item.rawClayAdobeBrick.ID ? Sound.clay : Sound.stone, false, 1f), new Random().nextFloat(0.6F, 1));

            if(chest.inventory.itemStacks[0].count <= 0){
                chest.inventory.itemStacks[0].item = null;
                chest.inventory.itemStacks[0].count = 0;
                chest.inventory.itemStacks[0].metadata = Item.NULL_ITEM_METADATA;
                chest.inventory.itemStacks[0].durability = Item.NULL_ITEM_DURABILITY;
                world.removeBlockState(x,y,z, MultiState.CHEST_STATE);
                world.setBlockAndNotify(x,y,z, Block.air.ID, false);
            }

            world.notifyChunk(x,y,z);
            return;
        }


        if(playerHeldItem == Item.mud.ID && (KeyListener.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT) || KeyListener.isKeyPressed(GLFW.GLFW_KEY_RIGHT_SHIFT))){
            ChestLocation chestLocation = (ChestLocation) world.getBlockState(x,y,z, MultiState.CHEST_STATE);
            if(chestLocation.inventory.itemStacks[0].item.ID == Item.firedRedClayAdobeBrick.ID && chestLocation.inventory.itemStacks[0].count == 8){
                world.clearChestLocation(x,y,z);
                world.setBlockAndNotify(x,y,z, Block.air.ID, false);
                world.addEntity(new EntityBlock(x + 0.5, y + 0.5, z + 0.5, Block.adobeBrick.ID, (byte)2));
                player.removeItemFromInventory();
                return;
            }
        }
    }


    @Override
    public void adjustBoundingBox(int x, int y, int z, AxisAlignedBB axisAlignedBB){
        ChestLocation chestLocation = (ChestLocation)CosmicEvolution.instance.save.activeWorld.getBlockState(x,y,z, MultiState.CHEST_STATE);
        if(chestLocation == null)return;

        int brickCount = chestLocation.inventory.itemStacks[0].count;
        if (brickCount <= 12) {
            axisAlignedBB.minX = x + BlockAxisAlignedBBList.quarterBlock.minX;
            axisAlignedBB.maxX = x + BlockAxisAlignedBBList.quarterBlock.maxX;
            axisAlignedBB.minY = y + BlockAxisAlignedBBList.quarterBlock.minY;
            axisAlignedBB.maxY = y + BlockAxisAlignedBBList.quarterBlock.maxY;
            axisAlignedBB.minZ = z + BlockAxisAlignedBBList.quarterBlock.minZ;
            axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.quarterBlock.maxZ;
        } else if (brickCount <= 24) {
            axisAlignedBB.minX = x + BlockAxisAlignedBBList.slab.minX;
            axisAlignedBB.maxX = x + BlockAxisAlignedBBList.slab.maxX;
            axisAlignedBB.minY = y + BlockAxisAlignedBBList.slab.minY;
            axisAlignedBB.maxY = y + BlockAxisAlignedBBList.slab.maxY;
            axisAlignedBB.minZ = z + BlockAxisAlignedBBList.slab.minZ;
            axisAlignedBB.maxZ = z + BlockAxisAlignedBBList.slab.maxZ;
        } else if (brickCount <= 36) {
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
