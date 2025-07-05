package spacegame.item;

import org.joml.Vector3f;
import spacegame.block.Block;
import spacegame.block.BlockLog;
import spacegame.core.SpaceGame;
import spacegame.entity.EntityItem;
import spacegame.entity.EntityPlayer;
import spacegame.world.Chunk;
import spacegame.world.WorldFace;

import java.util.Random;

public final class ItemAxe extends Item {
    public ItemAxe(short ID, int textureID, String filepath, Material material) {
        super(ID, textureID, filepath);
        this.durability = (short) (1 * material.durabilityModifier);
        this.hardness = material.hardnessValue;
        this.material = material;
    }

    @Override
    public void onRightClick(int x, int y, int z, WorldFace worldFace, EntityPlayer player){
        if(worldFace.getBlockID(x,y,z) >= Block.oakLogFullSizeNormal.ID && worldFace.getBlockID(x,y,z) <= Block.oakLogSize1EastWest.ID){
            Random rand = new Random();
            worldFace.setBlockWithNotify(x,y,z, Block.air.ID);
            EntityItem[] sticks = new EntityItem[BlockLog.sizeOfLog(worldFace.getBlockID(x,y,z)) / 2];
            Chunk chunk = worldFace.findChunkFromChunkCoordinates(x >> 5, y >> 5, z >> 5);
            for(int i = 0; i < sticks.length; i++) {
                sticks[i] = new EntityItem(x + rand.nextDouble(), y + 0.5, z + rand.nextDouble(), Item.rawStick.ID, (byte) 0, (byte) 1, (short) -1);
                sticks[i].setMovementVector(new Vector3f(rand.nextFloat(-1,1), rand.nextFloat(-1, 1), rand.nextFloat(-1, 1)));
                chunk.addEntityToList(sticks[i]);
            }
            this.onDestroy(player.inventory.itemStacks[EntityPlayer.selectedInventorySlot], EntityPlayer.selectedInventorySlot);
        }
    }

    @Override
    public void onDestroy(ItemStack itemStack, int accessor){
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].item = null;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].count = 0;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].metadata = 0;
        SpaceGame.instance.save.thePlayer.inventory.itemStacks[accessor].durability = 0;
        SpaceGame.instance.save.thePlayer.addItemToInventory(Item.stoneFragments.ID, (byte)1, (byte) 1, Item.stoneFragments.durability);
    }
}
