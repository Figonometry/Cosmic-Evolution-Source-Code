package spacegame.world;

import spacegame.core.CrashLogger;
import spacegame.core.SpaceGame;
import spacegame.entity.Entity;
import spacegame.entity.EntityBlock;
import spacegame.entity.EntityItem;
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
            chunkTag.setTag("Chunk", chunkData);
            chunkData.setTag("Entity", entity);

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
                if(chunk.renderableItemID != null) {
                    int[] renderableItemID = new int[chunk.renderableItemID.length];
                    for (int i = 0; i < renderableItemID.length; i++) {
                        renderableItemID[i] = chunk.renderableItemID[i];
                    }
                    chunkData.setIntArray("renderableItemID", renderableItemID);
                }
                if(chunk.renderableItemIndexes != null) {
                    int[] renderableItemIndexes = new int[chunk.renderableItemIndexes.length];
                    for (int i = 0; i < renderableItemIndexes.length; i++) {
                        renderableItemIndexes[i] = chunk.renderableItemIndexes[i];
                    }
                    chunkData.setIntArray("renderableItemIndexes", renderableItemIndexes);
                }
                if(chunk.renderableItemDurability != null) {
                    int[] renderableItemDurability = new int[chunk.renderableItemDurability.length];
                    for (int i = 0; i < renderableItemDurability.length; i++) {
                        renderableItemDurability[i] = chunk.renderableItemDurability[i];
                    }
                    chunkData.setIntArray("renderableItemDurability", renderableItemDurability);
                }
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
                    }
                    entity.setTag("entity" + entityCount, entities[i]);
                    entityCount++;
                }
                entity.setInteger("entityCount", entityCount);
            }

            NBTIO.writeCompressed(chunkTag, outputStream);
            outputStream.close();
        } catch (IOException e){
            new CrashLogger(e);
        }
        synchronized (SpaceGame.instance.save.activeWorld.activeWorldFace.chunkController.removeChunks) {
            SpaceGame.instance.save.activeWorld.activeWorldFace.chunkController.removeChunks.add(this.chunk);
        }
    }
}
