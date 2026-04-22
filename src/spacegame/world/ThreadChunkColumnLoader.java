package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockContainer;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.entity.*;
import spacegame.item.crafting.CraftingBlockRecipes;
import spacegame.item.crafting.InWorld3DCraftingItem;
import spacegame.item.crafting.InWorldCraftingItem;
import spacegame.item.crafting.InWorldCraftingRecipe;
import spacegame.util.Logger;
import spacegame.item.Inventory;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public final class ThreadChunkColumnLoader implements Runnable {
    public final int x;
    public final int z;
    public ChunkController controller;

    public ThreadChunkColumnLoader(int x, int z, ChunkController controller) {
        this.x = x;
        this.z = z;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            this.loadChunkColumn(this.x, this.z);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            CosmicEvolution.threadJobs.decrementAndGet();
        }
    }

    public void loadChunkColumn(int x, int z) {
        this.controller.parentWorld.generateWeatherSystems(x,z);
        for (int y = this.controller.playerChunkY - GameSettings.chunkColumnHeight; y < this.controller.playerChunkY + GameSettings.chunkColumnHeight; y++) {
            if (!this.controller.doesChunkExitAtPos(x, y, z)) {
                Chunk chunk = null;
                File file = new File(CosmicEvolution.instance.save.activeWorld.worldFolder + "/Chunk." + x + "." + y + "." + z + ".dat");
                try {
                    if(file.exists()) {
                        FileInputStream inputStream = new FileInputStream(file);
                        NBTTagCompound chunkTag = NBTIO.readCompressed(inputStream);
                        NBTTagCompound chunkData = chunkTag.getCompoundTag("Chunk");
                        NBTTagCompound entity = chunkData.getCompoundTag("Entity");
                        NBTTagCompound chest = chunkData.getCompoundTag("Chest");
                        NBTTagCompound timeEvents = chunkData.getCompoundTag("TimeEvents");
                        NBTTagCompound heatableBlocks = chunkData.getCompoundTag("HeatableBlocks");
                        NBTTagCompound crafting3DItems = chunkData.getCompoundTag("Crafting3DItems");
                        NBTTagCompound craftingItems = chunkData.getCompoundTag("CraftingItems");
                        chunk = new Chunk(x, y, z, CosmicEvolution.instance.save.activeWorld);

                        chunk.containsWater = chunkData.getBoolean("containsWater");
                        chunk.containsAir = chunkData.getBoolean("containsAir");
                        chunk.populated = chunkData.getBoolean("populated");
                        chunk.empty = chunkData.getBoolean("empty");
                        if (!chunk.empty) {
                            chunk.blocks = chunkData.getShortArray("blocks");
                            chunk.decayableLeaves = chunkData.getShortArray("decayableLeaves");
                            chunk.topFaceBitMask = chunkData.getIntArray("topFaceBitMask");
                            chunk.bottomFaceBitMask = chunkData.getIntArray("bottomFaceBitMask");
                            chunk.northFaceBitMask = chunkData.getIntArray("northFaceBitMask");
                            chunk.southFaceBitMask = chunkData.getIntArray("southFaceBitMask");
                            chunk.eastFaceBitMask = chunkData.getIntArray("eastFaceBitMask");
                            chunk.westFaceBitMask = chunkData.getIntArray("westFaceBitMask");
                        }

                        if(entity != null) {
                            int entityCount = entity.getInteger("entityCount");
                            NBTTagCompound entityLoadedTag;
                            Entity entityLoaded;
                            for (int i = 0; i < entityCount; i++) {
                                entityLoadedTag = entity.getCompoundTag("entity" + i);
                                switch (entityLoadedTag.getString("entityType")) {
                                    case "EntityBlock" -> {
                                        entityLoaded = new EntityBlock(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("blockType"), entityLoadedTag.getByte("count"));
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                    case "EntityItem" -> {
                                        entityLoaded = new EntityItem(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("itemType"), (byte) 1, entityLoadedTag.getByte("count"), entityLoadedTag.getShort("durability"), 0);
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                    case "EntityDeer" -> {
                                        entityLoaded = new EntityDeer(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                                        entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
                                        ((EntityLiving)entityLoaded).isDead = entityLoadedTag.getBoolean("isDead");
                                        ((EntityLiving)entityLoaded).isAIEnabled = entityLoadedTag.getBoolean("isAIEnabled");
                                        ((EntityLiving)entityLoaded).timeDied = entityLoadedTag.getLong("timeDied");
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                    case "EntityWolf" -> {
                                        entityLoaded = new EntityWolf(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                                        entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
                                        ((EntityLiving)entityLoaded).isDead = entityLoadedTag.getBoolean("isDead");
                                        ((EntityLiving)entityLoaded).isAIEnabled = entityLoadedTag.getBoolean("isAIEnabled");
                                        ((EntityLiving)entityLoaded).timeDied = entityLoadedTag.getLong("timeDied");
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                }
                            }
                        }

                        if(chest != null) {
                            int chestCount = chest.getInteger("chestCount");
                            NBTTagCompound chestLoadedTag;
                            for (int i = 0; i < chestCount; i++) {
                                chestLoadedTag = chest.getCompoundTag("chest" + i);
                                NBTTagCompound inventory = chestLoadedTag.getCompoundTag("Inventory");
                                short index = chestLoadedTag.getShort("index");
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
                                            chestInventory.loadItemToInventory(id, metadata, count, durability, j, decayTime);
                                        }
                                    }
                                    chunk.addChestLocation(index, chestInventory);
                                }
                            }
                        }

                        if(timeEvents != null) {
                            int eventCount = timeEvents.getInteger("eventCount");
                            NBTTagCompound eventLoadedTag;
                            for (int i = 0; i < eventCount; i++) {
                                eventLoadedTag = timeEvents.getCompoundTag("event" + i);
                                short index = eventLoadedTag.getShort("index");
                                long updateTime = eventLoadedTag.getLong("updateTime");
                                chunk.addTimeUpdateEvent(index, updateTime);
                            }
                        }

                        if(heatableBlocks != null){
                            int heatableBlockCount = heatableBlocks.getInteger("heatableBlockCount");
                            NBTTagCompound heatableBlockLoadedTag;
                            HeatableBlockLocation heatableBlockLocation;
                            for(int i = 0; i < heatableBlockCount; i++){
                                heatableBlockLoadedTag = heatableBlocks.getCompoundTag("heatableBlock" + i);
                                short index = heatableBlockLoadedTag.getShort("index");
                                float currentTemperature = heatableBlockLoadedTag.getFloat("currentTemperature");
                                short currentFuelBurning = heatableBlockLoadedTag.getShort("currentFuelBurning");
                                long fuelBurnoutTime = heatableBlockLoadedTag.getLong("fuelBurnoutTime");
                                long heatingFinishTime = heatableBlockLoadedTag.getLong("heatingFinishTime");
                                long fuelStartTime = heatableBlockLoadedTag.getLong("fuelStartTime");
                                long heatStartTime = heatableBlockLoadedTag.getLong("heatStartTime");
                                boolean heating = heatableBlockLoadedTag.getBoolean("heating");
                                heatableBlockLocation = new HeatableBlockLocation(index);
                                heatableBlockLocation.currentTemperature = currentTemperature;
                                heatableBlockLocation.currentFuelBurning = currentFuelBurning;
                                heatableBlockLocation.fuelBurnoutTime = fuelBurnoutTime;
                                heatableBlockLocation.heatingFinishTime = heatingFinishTime;
                                heatableBlockLocation.heating = heating;
                                heatableBlockLocation.fuelStartTime = fuelStartTime;
                                heatableBlockLocation.heatStartTime = heatStartTime;
                                chunk.addHeatableBlock(heatableBlockLocation);
                            }
                        }



                        if(crafting3DItems != null){
                            int crafting3DItemCount = crafting3DItems.getInteger("inWorldCrafting3DItemCount");
                            NBTTagCompound inWorldCrafting3DItemLoadedTag;
                            InWorld3DCraftingItem inWorld3DCraftingItem;
                            for(int i = 0; i < crafting3DItemCount; i++){
                                inWorldCrafting3DItemLoadedTag = crafting3DItems.getCompoundTag("inWorld3DCraftingItem" + i);
                                int index = inWorldCrafting3DItemLoadedTag.getInteger("index");
                                short materialBlockID = inWorldCrafting3DItemLoadedTag.getShort("materialBlockID");

                                inWorld3DCraftingItem = new InWorld3DCraftingItem(index, materialBlockID, InWorldCraftingRecipe.findInWorldCraftingRecipeFromName(inWorldCrafting3DItemLoadedTag.getString("craftingRecipeName")), chunk);
                                inWorld3DCraftingItem.activeCraftingLayer = inWorldCrafting3DItemLoadedTag.getInteger("activeCraftingLayer");

                                for(int j = 0; j < 16; j++){
                                    inWorld3DCraftingItem.subVoxelIndices[j] = inWorldCrafting3DItemLoadedTag.getIntArray("craftingLayer" + j);
                                }

                                chunk.crafting3DItems.add(inWorld3DCraftingItem);

                            }
                        }

                        if(craftingItems != null){
                            int craftingItemCount = craftingItems.getInteger("inWorldCraftingItemCount");
                            NBTTagCompound inWorldCraftingItemLoadedTag;
                            InWorldCraftingItem inWorldCraftingItem;
                            for(int i = 0; i < craftingItemCount; i++){
                                inWorldCraftingItemLoadedTag = craftingItems.getCompoundTag("inWorldCraftingItem" + i);

                                int index =  inWorldCraftingItemLoadedTag.getInteger("index");
                                int recipeID = inWorldCraftingItemLoadedTag.getInteger("outputRecipeID");
                                boolean hasBeenBound = inWorldCraftingItemLoadedTag.getBoolean("hasBeenBound");

                                inWorldCraftingItem = new InWorldCraftingItem(CraftingBlockRecipes.list[recipeID], index, chunk);
                                inWorldCraftingItem.hasBeenBound = hasBeenBound;

                                int filledItemIndex = inWorldCraftingItemLoadedTag.getInteger("filledItemIndex");

                                for(int j = 0; j < filledItemIndex; j++){
                                    inWorldCraftingItem.itemsFilled[j] = inWorldCraftingItemLoadedTag.getBoolean("item" + j + "Filled");
                                }


                                chunk.craftingItems.add(inWorldCraftingItem);

                            }
                        }

                        inputStream.close();
                    }
                } catch (IOException e){
                    new Logger(e);
                }
                if(chunk != null){
                    this.controller.addChunkFromFile(chunk);
                } else {
                    this.controller.addChunk(new Chunk(x, y, z, this.controller.parentWorld));
                }
            }
        }
    }
}
