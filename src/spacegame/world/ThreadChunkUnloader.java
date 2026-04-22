package spacegame.world;

import spacegame.core.CosmicEvolution;
import spacegame.entity.*;
import spacegame.item.crafting.InWorld3DCraftingItem;
import spacegame.item.crafting.InWorldCraftingItem;
import spacegame.util.Logger;
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
        try {
            this.saveChunk();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            CosmicEvolution.threadJobs.decrementAndGet();
        }
    }


    public void saveChunk(){
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
                NBTTagCompound heatableBlocks = new NBTTagCompound();
                NBTTagCompound crafting3DItems = new NBTTagCompound();
                NBTTagCompound craftingItems = new NBTTagCompound();
                chunkTag.setTag("Chunk", chunkData);
                chunkData.setTag("Entity", entity);
                chunkData.setTag("Chest", chest);
                chunkData.setTag("TimeEvents", timeEvents);
                chunkData.setTag("HeatableBlocks", heatableBlocks);
                chunkData.setTag("Crafting3DItems", crafting3DItems);
                chunkData.setTag("CraftingItems", craftingItems);

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
                            entities[i].setShort("durability", ((EntityItem)savingEntity).itemDurability);
                        } else if (savingEntity instanceof EntityDeer) {
                            entities[i].setString("entityType", "EntityDeer");
                            entities[i].setLong("despawnTime", savingEntity.despawnTime);
                            entities[i].setByte("count", (byte) 1);
                            entities[i].setBoolean("isDead", ((EntityLiving)savingEntity).isDead);
                            entities[i].setBoolean("isAIEnabled", ((EntityLiving)savingEntity).isAIEnabled);
                            entities[i].setLong("timeDied", ((EntityLiving)savingEntity).timeDied);
                        } else if(savingEntity instanceof EntityWolf){
                            entities[i].setString("entityType", "EntityWolf");
                            entities[i].setLong("despawnTime", savingEntity.despawnTime);
                            entities[i].setByte("count", (byte)1);
                            entities[i].setBoolean("isDead", ((EntityLiving)savingEntity).isDead);
                            entities[i].setBoolean("isAIEnabled", ((EntityLiving)savingEntity).isAIEnabled);
                            entities[i].setLong("timeDied", ((EntityLiving)savingEntity).timeDied);
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
                                items[j].setLong("decayTime", stack.decayTime);
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

                if(this.chunks[chunkIndex].heatableBlocks.size() > 0){
                    HeatableBlockLocation heatableBlockLocation;
                    int heatableBlockCount = 0;
                    NBTTagCompound[] heatableBlockTags = new NBTTagCompound[this.chunks[chunkIndex].heatableBlocks.size()];
                    for(int i = 0; i < heatableBlockTags.length; i++){

                        heatableBlockLocation = this.chunks[chunkIndex].heatableBlocks.get(i);
                        heatableBlockTags[i] = new NBTTagCompound();
                        heatableBlockTags[i].setShort("index", heatableBlockLocation.index);
                        heatableBlockTags[i].setFloat("currentTemperature", heatableBlockLocation.currentTemperature);
                        heatableBlockTags[i].setShort("currentFuelBurning", heatableBlockLocation.currentFuelBurning);
                        heatableBlockTags[i].setLong("fuelBurnoutTime", heatableBlockLocation.fuelBurnoutTime);
                        heatableBlockTags[i].setLong("heatingFinishTime", heatableBlockLocation.heatingFinishTime);
                        heatableBlockTags[i].setBoolean("heating", heatableBlockLocation.heating);
                        heatableBlockTags[i].setLong("heatStartTime", heatableBlockLocation.heatStartTime);
                        heatableBlockTags[i].setLong("fuelStartTime", heatableBlockLocation.fuelStartTime);

                        heatableBlocks.setTag("heatableBlock" + heatableBlockCount, heatableBlockTags[i]);
                        heatableBlockCount++;
                    }
                    heatableBlocks.setInteger("heatableBlockCount", heatableBlockCount);
                }

                if(this.chunks[chunkIndex].crafting3DItems.size() > 0){
                    InWorld3DCraftingItem inWorld3DCraftingItem;
                    int inWorldCrafting3DItemCount = 0;
                    NBTTagCompound[] inWorldCrafting3DItemTags = new NBTTagCompound[this.chunks[chunkIndex].crafting3DItems.size()];
                    for(int i = 0; i < inWorldCrafting3DItemTags.length; i++){

                        inWorld3DCraftingItem = this.chunks[chunkIndex].crafting3DItems.get(i);
                        inWorldCrafting3DItemTags[i] = new NBTTagCompound();
                        inWorldCrafting3DItemTags[i].setInteger("index", inWorld3DCraftingItem.indexInChunk);
                        inWorldCrafting3DItemTags[i].setShort("materialBlockID", inWorld3DCraftingItem.materialBlockID);
                        inWorldCrafting3DItemTags[i].setInteger("activeCraftingLayer", inWorld3DCraftingItem.activeCraftingLayer);
                        inWorldCrafting3DItemTags[i].setString("craftingRecipeName", inWorld3DCraftingItem.craftingRecipe.recipeName);

                        for(int j = 0; j < 16; j++){
                            inWorldCrafting3DItemTags[i].setIntArray("craftingLayer" + j, inWorld3DCraftingItem.subVoxelIndices[j]);
                        }

                        crafting3DItems.setTag("inWorld3DCraftingItem" + inWorldCrafting3DItemCount, inWorldCrafting3DItemTags[i]);
                        inWorldCrafting3DItemCount++;
                    }
                    crafting3DItems.setInteger("inWorldCrafting3DItemCount", inWorldCrafting3DItemCount);
                }

                if(this.chunks[chunkIndex].craftingItems.size() > 0){;
                    InWorldCraftingItem inWorldCraftingItem;
                    int inWorldCraftingItemCount = 0;
                    NBTTagCompound[] inWorldCraftingItemTags = new NBTTagCompound[this.chunks[chunkIndex].craftingItems.size()];
                    for(int i = 0; i < inWorldCraftingItemTags.length; i++){

                        inWorldCraftingItem = this.chunks[chunkIndex].craftingItems.get(i);
                        inWorldCraftingItemTags[i] = new NBTTagCompound();
                        inWorldCraftingItemTags[i].setInteger("index", inWorldCraftingItem.indexInChunk);
                        inWorldCraftingItemTags[i].setInteger("outputRecipeID", inWorldCraftingItem.outputRecipe.ID);
                        inWorldCraftingItemTags[i].setBoolean("hasBeenBound", inWorldCraftingItem.hasBeenBound);

                        int filledItemIndex = 0;
                        for(int j = 0; j < inWorldCraftingItem.itemsFilled.length; j++){
                            inWorldCraftingItemTags[i].setBoolean("item" + j + "Filled", inWorldCraftingItem.itemsFilled[j]);
                            filledItemIndex++;
                        }

                        inWorldCraftingItemTags[i].setInteger("filledItemIndex", filledItemIndex);

                        craftingItems.setTag("inWorldCraftingItem" + inWorldCraftingItemCount, inWorldCraftingItemTags[i]);
                        inWorldCraftingItemCount++;
                    }
                    craftingItems.setInteger("inWorldCraftingItemCount", inWorldCraftingItemCount);
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
