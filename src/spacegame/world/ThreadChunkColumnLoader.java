package spacegame.world;

import spacegame.core.GameSettings;
import spacegame.core.Logger;
import spacegame.core.SpaceGame;
import spacegame.entity.Entity;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
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
        for (int y = this.controller.playerChunkY - GameSettings.chunkColumnHeight; y < this.controller.playerChunkY + GameSettings.chunkColumnHeight; y++) {
            if (!this.controller.doesChunkExitAtPos(x, y, z)) {
                Chunk chunk = null;
                File file = new File(SpaceGame.instance.save.activeWorld.worldFolder + "/Chunk." + x + "." + y + "." + z + ".dat");
                try {
                    if(file.exists()) {
                        FileInputStream inputStream = new FileInputStream(file);
                        NBTTagCompound chunkTag = NBTIO.readCompressed(inputStream);
                        NBTTagCompound chunkData = chunkTag.getCompoundTag("Chunk");
                        NBTTagCompound entity = chunkData.getCompoundTag("Entity");
                        chunk = new Chunk(x, y, z, SpaceGame.instance.save.activeWorld);

                        chunk.containsWater = chunkData.getBoolean("containsWater");
                        chunk.containsAir = chunkData.getBoolean("containsAir");
                        chunk.populated = chunkData.getBoolean("populated");
                        chunk.empty = chunkData.getBoolean("empty");
                        if (!chunk.empty) {
                            chunk.blocks = chunkData.getShortArray("blocks");
                            chunk.topFaceBitMask = chunkData.getIntArray("topFaceBitMask");
                            chunk.bottomFaceBitMask = chunkData.getIntArray("bottomFaceBitMask");
                            chunk.northFaceBitMask = chunkData.getIntArray("northFaceBitMask");
                            chunk.southFaceBitMask = chunkData.getIntArray("southFaceBitMask");
                            chunk.eastFaceBitMask = chunkData.getIntArray("eastFaceBitMask");
                            chunk.westFaceBitMask = chunkData.getIntArray("westFaceBitMask");
                            int[] renderableItemID = chunkData.getIntArray("renderableItemID");
                            int[] renderableItemIndexes = chunkData.getIntArray("renderableItemIndexes");
                            int[] renderableItemDurability = chunkData.getIntArray("renderableItemDurability");
                            chunk.renderableItemID = new short[renderableItemID.length];
                            chunk.renderableItemIndexes = new short[renderableItemIndexes.length];
                            chunk.renderableItemDurability = new short[renderableItemDurability.length];
                            for(int i = 0; i < renderableItemID.length; i++){
                                chunk.renderableItemID[i] = (short) renderableItemID[i];
                                chunk.containsItems = true;
                            }
                            for(int i = 0; i < renderableItemIndexes.length; i++){
                                chunk.renderableItemIndexes[i] = (short) renderableItemIndexes[i];
                            }
                            for(int i = 0; i < renderableItemDurability.length; i++){
                                chunk.renderableItemDurability[i] = (short) renderableItemDurability[i];
                            }
                        }

                        int entityCount = entity.getInteger("entityCount");
                        NBTTagCompound entityLoadedTag;
                        Entity entityLoaded;
                        for(int i = 0; i < entityCount; i++){
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
