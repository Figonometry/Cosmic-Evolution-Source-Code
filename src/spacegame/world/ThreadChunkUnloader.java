package spacegame.world;

import spacegame.core.CosmicEvolution;
import spacegame.util.Logger;
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
    public Chunk[] chunks;
    public ThreadChunkUnloader(Chunk[] chunks){
        this.chunks = chunks;
    }

    @Override
    public void run() {
        for (int chunkIndex = 0; chunkIndex < this.chunks.length; chunkIndex++) {
            if(this.chunks[chunkIndex] == null)continue;
            File file = new File(CosmicEvolution.instance.save.activeWorld.worldFolder + "/Chunk." + this.chunks[chunkIndex].x + "." + this.chunks[chunkIndex].y + "." + this.chunks[chunkIndex].z + ".dat");
            try {
                FileOutputStream outputStream = new FileOutputStream(file);
                NBTTagCompound chunkTag = new NBTTagCompound();
                NBTTagCompound chunkData = new NBTTagCompound();
                NBTTagCompound entity = new NBTTagCompound();
                NBTTagCompound chest = new NBTTagCompound();
                NBTTagCompound timeEvents = new NBTTagCompound();
                chunkTag.setTag("Chunk", chunkData);
                chunkData.setTag("Entity", entity);
                chunkData.setTag("Chest", chest);
                chunkData.setTag("TimeEvents", timeEvents);

                chunkData.setInteger("x", this.chunks[chunkIndex].x);
                chunkData.setInteger("y", this.chunks[chunkIndex].y);
                chunkData.setInteger("z", this.chunks[chunkIndex].z);
                chunkData.setBoolean("populated", this.chunks[chunkIndex].populated);
                chunkData.setBoolean("containsWater", this.chunks[chunkIndex].containsWater);
                chunkData.setBoolean("containsAir", this.chunks[chunkIndex].containsAir);
                chunkData.setBoolean("empty", this.chunks[chunkIndex].empty);
                if (!this.chunks[chunkIndex].empty) {
                    chunkData.setShortArray("blocks", this.chunks[chunkIndex].blocks);
                    chunkData.setShortArray("decayableLeaves", this.chunks[chunkIndex].decayableLeaves);
                    chunkData.setIntArray("topFaceBitMask", this.chunks[chunkIndex].topFaceBitMask);
                    chunkData.setIntArray("bottomFaceBitMask", this.chunks[chunkIndex].bottomFaceBitMask);
                    chunkData.setIntArray("northFaceBitMask", this.chunks[chunkIndex].northFaceBitMask);
                    chunkData.setIntArray("southFaceBitMask", this.chunks[chunkIndex].southFaceBitMask);
                    chunkData.setIntArray("eastFaceBitMask", this.chunks[chunkIndex].eastFaceBitMask);
                    chunkData.setIntArray("westFaceBitMask", this.chunks[chunkIndex].westFaceBitMask);
                }


                if (this.chunks[chunkIndex].entities.size() > 0) {
                    Entity savingEntity;
                    int entityCount = 0;
                    NBTTagCompound[] entities = new NBTTagCompound[this.chunks[chunkIndex].entities.size()];
                    for (int i = 0; i < entities.length; i++) {
                        savingEntity = this.chunks[chunkIndex].entities.get(i);
                        entities[i] = new NBTTagCompound();
                        entities[i].setDouble("x", savingEntity.x);
                        entities[i].setDouble("y", savingEntity.y);
                        entities[i].setDouble("z", savingEntity.z);
                        if (savingEntity instanceof EntityBlock) {
                            entities[i].setString("entityType", "EntityBlock");
                            entities[i].setShort("blockType", ((EntityBlock) savingEntity).block);
                            entities[i].setByte("count", ((EntityBlock) savingEntity).count);
                        } else if (savingEntity instanceof EntityItem) {
                            entities[i].setString("entityType", "EntityItem");
                            entities[i].setShort("itemType", ((EntityItem) savingEntity).item);
                            entities[i].setByte("count", ((EntityItem) savingEntity).count);
                        } else if (savingEntity instanceof EntityDeer) {
                            entities[i].setString("entityType", "EntityDeer");
                            entities[i].setLong("despawnTime", savingEntity.despawnTime);
                            entities[i].setByte("count", (byte) 1);
                        }
                        entity.setTag("entity" + entityCount, entities[i]);
                        entityCount++;
                    }
                    entity.setInteger("entityCount", entityCount);
                }

                if (this.chunks[chunkIndex].chestLocations.size() > 0) {
                    ChestLocation chestLocation;
                    int chestCount = 0;
                    NBTTagCompound[] chests = new NBTTagCompound[this.chunks[chunkIndex].chestLocations.size()];
                    for (int i = 0; i < chests.length; i++) {
                        chestLocation = this.chunks[chunkIndex].chestLocations.get(i);
                        chests[i] = new NBTTagCompound();
                        chests[i].setShort("index", chestLocation.index);
                        ItemStack stack;
                        NBTTagCompound[] items = new NBTTagCompound[chestLocation.inventory.itemStacks.length];
                        NBTTagCompound inventory = new NBTTagCompound();
                        int slotNumber = 0;
                        for (int j = 0; j < chestLocation.inventory.itemStacks.length; j++) {
                            stack = chestLocation.inventory.itemStacks[j];
                            if (stack.item != null) {
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

                if (this.chunks[chunkIndex].updateEvents.size() > 0) {
                    TimeUpdateEvent timeUpdateEvent;
                    int eventCount = 0;
                    NBTTagCompound[] events = new NBTTagCompound[this.chunks[chunkIndex].updateEvents.size()];
                    for (int i = 0; i < events.length; i++) {
                        timeUpdateEvent = this.chunks[chunkIndex].updateEvents.get(i);
                        events[i] = new NBTTagCompound();
                        events[i].setShort("index", timeUpdateEvent.index);
                        events[i].setLong("updateTime", timeUpdateEvent.updateTime);
                        timeEvents.setTag("event" + eventCount, events[i]);
                        eventCount++;
                    }
                    timeEvents.setInteger("eventCount", eventCount);
                }

                NBTIO.writeCompressed(chunkTag, outputStream);
                outputStream.close();
            } catch (IOException e) {
                new Logger(e);
            }
            synchronized (CosmicEvolution.instance.save.activeWorld.chunkController.removeChunks) {
                CosmicEvolution.instance.save.activeWorld.chunkController.removeChunks.add(this.chunks[chunkIndex]);
            }
        }
    }
}
