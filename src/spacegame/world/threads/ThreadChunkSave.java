package spacegame.world.threads;

import spacegame.core.CosmicEvolution;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.util.Logger;
import spacegame.world.Chunk;
import spacegame.world.blockstate.MultiState;
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
            NBTTagCompound campfireStates = new NBTTagCompound();
            NBTTagCompound torchStates = new NBTTagCompound();
            NBTTagCompound logStates = new NBTTagCompound();
            NBTTagCompound berryBushStates = new NBTTagCompound();
            NBTTagCompound pitKilnStates = new NBTTagCompound();
            NBTTagCompound reedGrowthStates = new NBTTagCompound();
            NBTTagCompound doorStates = new NBTTagCompound();
            NBTTagCompound flowingWaterStates = new NBTTagCompound();
            NBTTagCompound soilStates = new NBTTagCompound();
            NBTTagCompound clayStates = new NBTTagCompound();

            chunkTag.setTag("Chunk", chunkData);
            chunkData.setTag("Entity", entity);
            chunkData.setTag("Chest", chest);
            chunkData.setTag("TimeEvents", timeEvents);
            chunkData.setTag("HeatableBlocks", heatableBlocks);
            chunkData.setTag("Crafting3DItems", crafting3DItems);
            chunkData.setTag("CraftingItems", craftingItems);
            chunkData.setTag("CropStates", cropStates);
            chunkData.setTag("TilledSoilStates", tilledSoilStates);
            chunkData.setTag("CampfireStates", campfireStates);
            chunkData.setTag("TorchStates", torchStates);
            chunkData.setTag("LogStates", logStates);
            chunkData.setTag("BerryBushStates", berryBushStates);
            chunkData.setTag("PitKilnStates", pitKilnStates);
            chunkData.setTag("ReedGrowthStates", reedGrowthStates);
            chunkData.setTag("DoorStates", doorStates);
            chunkData.setTag("FlowingWaterStates", flowingWaterStates);

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
            }


            if(this.chunk.entities.size() > 0){
                new ChunkEntitiesIO().saveEntities(this.chunk, entity);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.CHEST_STATE)){
                new ChestLocationIO().saveChestLocations(this.chunk, chest);
            }

            if(this.chunk.updateEvents.size() > 0){
                new TimeUpdateIO().saveTimeEvents(this.chunk, timeEvents);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.HEATABLE_BLOCK_STATE)){
                new HeatableBlockIO().saveHeatableBlocks(this.chunk, heatableBlocks);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.CRAFTING_3D_ITEM_STATE)){
                new Crafting3DItemsIO().saveCrafting3DItems(this.chunk, crafting3DItems);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.CRAFTING_ITEM_STATE)){
                new CraftingItemsIO().saveCraftingItems(this.chunk, craftingItems);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.CROP_STATE)){
                new CropStateIO().saveCropStates(this.chunk, cropStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.TILLED_SOIL_STATE)){
                new TilledSoilStateIO().saveTilledSoilStates(this.chunk, tilledSoilStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.CAMPFIRE_STATE)){
                new CampfireStateIO().saveCampfireStates(this.chunk, campfireStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.TORCH_STATE)){
                new TorchStateIO().saveTorchStates(this.chunk, torchStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.LOG_STATE)){
                new LogStateIO().saveLogStates(this.chunk, logStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.BERRY_BUSH_STATE)){
                new BerryBushStateIO().saveBerryBushStates(this.chunk, berryBushStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.PIT_KILN_STATE)){
                new PitKilnStateIO().savePitKilnStates(this.chunk, pitKilnStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.REED_GROWTH_STATE)){
                new ReedGrowthStateIO().saveReedGrowthStates(this.chunk, reedGrowthStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.DOOR_STATE)){
                new DoorStateIO().saveDoorStates(this.chunk, doorStates);
            }

            if(this.chunk.doesChunkContainStateOfType(MultiState.FLOWING_WATER_STATE)){
                new FlowingWaterStateIO().saveFlowingWaterStates(this.chunk, flowingWaterStates);
            }

            NBTIO.writeCompressed(chunkTag, outputStream);
            outputStream.close();
        } catch (IOException e){
            new Logger(e);
        }

    }
}