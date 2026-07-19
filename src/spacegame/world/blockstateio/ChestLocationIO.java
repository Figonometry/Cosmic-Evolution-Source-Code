package spacegame.world.blockstateio;

import spacegame.block.Block;
import spacegame.block.BlockContainer;
import spacegame.item.Inventory;
import spacegame.item.ItemStack;
import spacegame.item.itemstate.ItemState;
import spacegame.nbt.NBTTagCompound;
import spacegame.world.blockstate.ChestLocation;
import spacegame.world.Chunk;
import spacegame.world.blockstatewrapper.ChestLocationSafe;

import java.util.Iterator;
import java.util.Map;

public class ChestLocationIO { //This must be instance and not static since it will be executed from different threads and synchronizing would cause performance overhead

    public void saveChestLocations(Chunk chunk, NBTTagCompound nbtTagCompound){
        ChestLocation chestLocation;
        ChestLocation[] chestLocations = this.getAllChestLocationsInArray(chunk);
        int chestCount = 0;
        NBTTagCompound[] chests = new NBTTagCompound[chunk.chestLocations.size()];
        for(int i = 0; i < chests.length; i++){
            chestLocation = chestLocations[i];
            chests[i] = new NBTTagCompound();
            chests[i].setInteger("index", chestLocation.index);
            ItemStack stack;
            NBTTagCompound[] items = new NBTTagCompound[chestLocation.inventory.itemStacks.length];
            NBTTagCompound inventory = new NBTTagCompound();
            int slotNumber = 0;
            for(int j = 0; j < chestLocation.inventory.itemStacks.length; j++){
                stack = chestLocation.inventory.itemStacks[j];
                if(stack.item != null) {
                    items[j] = new NBTTagCompound();
                    items[j].setShort("id", stack.item.ID);
                    items[j].setByte("count", stack.count);
                    items[j].setShort("metadata", stack.metadata);
                    items[j].setShort("durability", stack.durability);
                    items[j].setLong("decayTime", stack.decayTime);
                    if(stack.itemState != null) {
                        items[j].setCompoundTag("itemState", stack.itemState.getCompoundTag());
                    }
                    inventory.setTag("slot " + slotNumber, items[j]);
                }
                slotNumber++;
            }
            chests[i].setTag("Inventory", inventory);
            nbtTagCompound.setTag("chest" + chestCount, chests[i]);
            chestCount++;
        }
        nbtTagCompound.setInteger("chestCount", chestCount);
    }

    public void loadChestLocations(Chunk chunk, NBTTagCompound nbtTagCompound){
        int chestCount = nbtTagCompound.getInteger("chestCount");
        NBTTagCompound chestLoadedTag;
        for (int i = 0; i < chestCount; i++) {
            chestLoadedTag = nbtTagCompound.getCompoundTag("chest" + i);
            NBTTagCompound inventory = chestLoadedTag.getCompoundTag("Inventory");
            int index = chestLoadedTag.getInteger("index");
            NBTTagCompound item;
            if (Block.list[chunk.blocks[index]] instanceof BlockContainer) {
                Inventory chestInventory = new Inventory(((BlockContainer) (Block.list[chunk.blocks[index]])).inventoryWidth, ((BlockContainer) (Block.list[chunk.blocks[index]])).inventoryHeight);
                for (int j = 0; j < chestInventory.itemStacks.length; j++) {
                    item = inventory.getCompoundTag("slot " + j);
                    if (item != null) {
                        short id = item.getShort("id");
                        byte count = item.getByte("count");
                        short durability = item.getShort("durability");
                        short metadata = item.getShort("metadata");
                        long decayTime = item.getLong("decayTime");
                        chestInventory.loadItemToInventory(id, metadata, count, durability, j, decayTime, ItemState.loadFromCompoundTag(item.getCompoundTag("itemState")));
                    }
                }
                chunk.addChestLocation(index, chestInventory);
            }
        }
    }

    private ChestLocation[] getAllChestLocationsInArray(Chunk chunk){
        int index = 0;
        ChestLocation chestLocation;
        ChestLocation[] chestLocations1 = new ChestLocation[chunk.chestLocations.size()];
        Iterator<Map.Entry<Integer, ChestLocationSafe>> iterator = chunk.chestLocations.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, ChestLocationSafe> entry = iterator.next();
            chestLocation = entry.getValue().value;
            if(chestLocation != null){
                chestLocations1[index] = chestLocation;
                index++;
            }
        }
        return chestLocations1;
    }

}
