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

public final class ThreadChunkSave implements Runnable {
    public Chunk chunk;
    public ThreadChunkSave(Chunk chunk){
        this.chunk = chunk;
    }

    @Override
    public void run(){
        try {
            this.saveChunk();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            CosmicEvolution.threadJobs.decrementAndGet();
        }
    }

    public void saveChunk() {
        File file = new File(CosmicEvolution.instance.save.activeWorld.worldFolder + "/Chunk." + chunk.x + "." + chunk.y + "." + chunk.z + ".dat");
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

            chunkData.setInteger("x", chunk.x);
            chunkData.setInteger("y", chunk.y);
            chunkData.setInteger("z", chunk.z);
            chunkData.setBoolean("populated", chunk.populated);
            chunkData.setBoolean("containsWater", chunk.containsWater);
            chunkData.setBoolean("containsAir", chunk.containsAir);
            chunkData.setBoolean("empty", chunk.empty);
            if(!chunk.empty) {
                chunkData.setShortArray("blocks", chunk.blocks);
                chunkData.setShortArray("decayableLeaves", chunk.decayableLeaves);
                chunkData.setIntArray("topFaceBitMask", chunk.topFaceBitMask);
                chunkData.setIntArray("bottomFaceBitMask", chunk.bottomFaceBitMask);
                chunkData.setIntArray("northFaceBitMask", chunk.northFaceBitMask);
                chunkData.setIntArray("southFaceBitMask", chunk.southFaceBitMask);
                chunkData.setIntArray("eastFaceBitMask", chunk.eastFaceBitMask);
                chunkData.setIntArray("westFaceBitMask", chunk.westFaceBitMask);
            }


            if(this.chunk.entities.size() > 0){
                new ChunkEntitiesIO().saveEntities(this.chunk, entity);
            }

            if(this.chunk.chestLocations.size() > 0){
                new ChestLocationIO().saveChestLocations(this.chunk, chest);
            }

            if(this.chunk.updateEvents.size() > 0){
                new TimeUpdateIO().saveTimeEvents(this.chunk, timeEvents);
            }

            if(this.chunk.heatableBlocks.size() > 0){
                new HeatableBlockIO().saveHeatableBlocks(this.chunk, heatableBlocks);
            }

            if(this.chunk.crafting3DItems.size() > 0){
                new Crafting3DItemsIO().saveCrafting3DItems(this.chunk, crafting3DItems);
            }

            if(this.chunk.craftingItems.size() > 0){
                new CraftingItemsIO().saveCraftingItems(this.chunk, craftingItems);
            }

            if(this.chunk.cropStates.size() > 0){
                new CropStateIO().saveCropStates(this.chunk, cropStates);
            }

            if(this.chunk.tilledSoilStates.size() > 0){
                new TilledSoilStateIO().saveTilledSoilStates(this.chunk, tilledSoilStates);
            }

            NBTIO.writeCompressed(chunkTag, outputStream);
            outputStream.close();
        } catch (IOException e){
            new Logger(e);
        }

    }
}