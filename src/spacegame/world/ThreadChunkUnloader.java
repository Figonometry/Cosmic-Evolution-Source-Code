package spacegame.world;

import spacegame.core.Logger;
import spacegame.core.SpaceGame;
import spacegame.entity.Entity;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityDeer;
import spacegame.entity.EntityItem;
import spacegame.item.ItemStack;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public final class ThreadChunkUnloader implements Runnable {
    public Chunk chunk;
    public ChunkRegion region;
    public ThreadChunkUnloader(Chunk chunk, ChunkRegion region){
        this.chunk = chunk;
        this.region = region;
    }

    @Override
    public void run() {
        File file = new File(SpaceGame.instance.save.activeWorld.worldFolder + "/Chunk." + chunk.x + "." + chunk.y + "." + chunk.z + ".dat");
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            NBTTagCompound chunkTag = new NBTTagCompound();
            NBTTagCompound chunkData = new NBTTagCompound();
            NBTTagCompound entity = new NBTTagCompound();
            NBTTagCompound chest = new NBTTagCompound();
            chunkTag.setTag("Chunk", chunkData);
            chunkData.setTag("Entity", entity);
            chunkData.setTag("Chest", chest);

            chunkData.setInteger("x", chunk.x);
            chunkData.setInteger("y", chunk.y);
            chunkData.setInteger("z", chunk.z);
            chunkData.setBoolean("populated", chunk.populated);
            chunkData.setBoolean("containsWater", chunk.containsWater);
            chunkData.setBoolean("containsAir", chunk.containsAir);
            chunkData.setBoolean("empty", chunk.empty);
            if(!chunk.empty) {
                chunkData.setShortArray("blocks", chunk.blocks);
                chunkData.setIntArray("topFaceBitMask", chunk.topFaceBitMask);
                chunkData.setIntArray("bottomFaceBitMask", chunk.bottomFaceBitMask);
                chunkData.setIntArray("northFaceBitMask", chunk.northFaceBitMask);
                chunkData.setIntArray("southFaceBitMask", chunk.southFaceBitMask);
                chunkData.setIntArray("eastFaceBitMask", chunk.eastFaceBitMask);
                chunkData.setIntArray("westFaceBitMask", chunk.westFaceBitMask);
            }


            if(this.chunk.entities.size() > 0){
                Entity savingEntity;
                int entityCount = 0;
                NBTTagCompound[] entities = new NBTTagCompound[this.chunk.entities.size()];
                for(int i = 0; i < entities.length; i++){
                    savingEntity = this.chunk.entities.get(i);
                    entities[i] = new NBTTagCompound();
                    entities[i].setDouble("x", savingEntity.x);
                    entities[i].setDouble("y", savingEntity.y);
                    entities[i].setDouble("z", savingEntity.z);
                    if(savingEntity instanceof EntityBlock){
                        entities[i].setString("entityType", "EntityBlock");
                        entities[i].setShort("blockType", ((EntityBlock) savingEntity).block);
                        entities[i].setByte("count", ((EntityBlock)savingEntity).count);
                    } else if(savingEntity instanceof EntityItem){
                        entities[i].setString("entityType", "EntityItem");
                        entities[i].setShort("itemType", ((EntityItem) savingEntity).item);
                        entities[i].setByte("count", ((EntityItem)savingEntity).count);
                    } else if(savingEntity instanceof EntityDeer){
                        entities[i].setString("entityType", "EntityDeer");
                        entities[i].setLong("despawnTime", savingEntity.despawnTime);
                        entities[i].setByte("count", (byte)1);
                    }
                    entity.setTag("entity" + entityCount, entities[i]);
                    entityCount++;
                }
                entity.setInteger("entityCount", entityCount);
            }

            if(this.chunk.chestLocations.size() > 0){
                ChestLocation chestLocation;
                int chestCount = 0;
                NBTTagCompound[] chests = new NBTTagCompound[this.chunk.chestLocations.size()];
                for(int i = 0; i < chests.length; i++){
                    chestLocation = this.chunk.chestLocations.get(i);
                    chests[i] = new NBTTagCompound();
                    chests[i].setShort("index", chestLocation.index);
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
                            inventory.setTag("slot " + slotNumber, items[j]);
                        }
                        slotNumber++;
                    }
                    chests[i].setTag("Inventory", inventory);
                    chest.setTag("chest" + chestCount, chests[i]);
                    chestCount++;
                }
                chest.setInteger("chestCount", chestCount);
            }

            NBTIO.writeCompressed(chunkTag, outputStream);
            outputStream.close();
        } catch (IOException e){
            new Logger(e);
        }
        synchronized (SpaceGame.instance.save.activeWorld.chunkController.removeChunks) {
            SpaceGame.instance.save.activeWorld.chunkController.removeChunks.add(this.chunk);
        }
    }
}
