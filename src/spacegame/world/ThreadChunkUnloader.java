package spacegame.world;

import spacegame.core.CosmicEvolution;
import spacegame.entity.*;
import spacegame.world.blockstate.InWorld3DCraftingItem;
import spacegame.world.blockstate.InWorldCraftingItem;
import spacegame.util.Logger;
import spacegame.item.ItemStack;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.world.blockstate.HeatableBlockLocation;
import spacegame.world.blockstate.TimeUpdateEvent;
import spacegame.world.blockstateio.*;

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
                NBTTagCompound cropStates = new NBTTagCompound();
                NBTTagCompound tilledSoilStates = new NBTTagCompound();

                chunkTag.setTag("Chunk", chunkData);
                chunkData.setTag("Entity", entity);
                chunkData.setTag("Chest", chest);
                chunkData.setTag("TimeEvents", timeEvents);
                chunkData.setTag("HeatableBlocks", heatableBlocks);
                chunkData.setTag("Crafting3DItems", crafting3DItems);
                chunkData.setTag("CraftingItems", craftingItems);
                chunkData.setTag("CropStates", cropStates);
                chunkData.setTag("TilledSoilStates", tilledSoilStates);

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
                    new ChunkEntitiesIO().saveEntities(this.chunks[chunkIndex], entity);
                }

                if(this.chunks[chunkIndex].chestLocations.size() > 0){
                    new ChestLocationIO().saveChestLocations(this.chunks[chunkIndex], chest);
                }

                if(this.chunks[chunkIndex].updateEvents.size() > 0){
                    new TimeUpdateIO().saveTimeEvents(this.chunks[chunkIndex], timeEvents);
                }

                if(this.chunks[chunkIndex].heatableBlocks.size() > 0){
                    new HeatableBlockIO().saveHeatableBlocks(this.chunks[chunkIndex], heatableBlocks);
                }

                if(this.chunks[chunkIndex].crafting3DItems.size() > 0){
                    new Crafting3DItemsIO().saveCrafting3DItems(this.chunks[chunkIndex], crafting3DItems);
                }

                if(this.chunks[chunkIndex].craftingItems.size() > 0){
                    new CraftingItemsIO().saveCraftingItems(this.chunks[chunkIndex], craftingItems);
                }

                if(this.chunks[chunkIndex].cropStates.size() > 0){
                    new CropStateIO().saveCropStates(this.chunks[chunkIndex], cropStates);
                }

                if(this.chunks[chunkIndex].tilledSoilStates.size() > 0){
                    new TilledSoilStateIO().saveTilledSoilStates(this.chunks[chunkIndex], tilledSoilStates);
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
