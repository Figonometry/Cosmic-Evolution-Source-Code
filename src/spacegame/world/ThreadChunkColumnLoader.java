package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockContainer;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.util.Logger;
import spacegame.entity.Entity;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityDeer;
import spacegame.entity.EntityItem;
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
        this.loadChunkColumn(this.x, this.z);
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
                                        entityLoaded = new EntityItem(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), entityLoadedTag.getShort("itemType"), (byte) 1, entityLoadedTag.getByte("count"), entityLoadedTag.getShort("durability"));
                                        chunk.addEntityToList(entityLoaded);
                                    }
                                    case "EntityDeer" -> {
                                        entityLoaded = new EntityDeer(entityLoadedTag.getDouble("x"), entityLoadedTag.getDouble("y"), entityLoadedTag.getDouble("z"), false, false);
                                        entityLoaded.despawnTime = entityLoadedTag.getLong("despawnTime");
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
                                Inventory chestInventory = new Inventory(((BlockContainer) (Block.list[chunk.blocks[index]])).inventoryWidth, ((BlockContainer) (Block.list[chunk.blocks[index]])).inventoryHeight);
                                for (int j = 0; j < chestInventory.itemStacks.length; j++) {
                                    item = inventory.getCompoundTag("slot " + j);
                                    if (item != null) {
                                        short id = item.getShort("id");
                                        byte count = item.getByte("count");
                                        short durability = item.getShort("durability");
                                        short metadata = item.getShort("metadata");
                                        chestInventory.loadItemToInventory(id, metadata, count, durability, j);
                                    }
                                }
                                chunk.addChestLocation(index, chestInventory);
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
