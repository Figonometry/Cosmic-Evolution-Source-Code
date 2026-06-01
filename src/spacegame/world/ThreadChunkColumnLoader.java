package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockContainer;
import spacegame.core.CosmicEvolution;
import spacegame.core.GameSettings;
import spacegame.entity.*;
import spacegame.item.crafting.CraftingBlockRecipes;
import spacegame.item.itemstate.ItemState;
import spacegame.world.blockstate.InWorld3DCraftingItem;
import spacegame.world.blockstate.InWorldCraftingItem;
import spacegame.item.crafting.InWorldCraftingRecipe;
import spacegame.render.RenderEngine;
import spacegame.util.Logger;
import spacegame.item.Inventory;
import spacegame.nbt.NBTIO;
import spacegame.nbt.NBTTagCompound;
import spacegame.world.blockstate.HeatableBlockLocation;
import spacegame.world.blockstateio.*;

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
                        NBTTagCompound cropStates = chunkData.getCompoundTag("CropStates");
                        NBTTagCompound tilledSoilStates = chunkData.getCompoundTag("TilledSoilStates");

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
                            new ChunkEntitiesIO().loadEntities(chunk, entity);
                        }

                        if(chest != null) {
                            new ChestLocationIO().loadChestLocations(chunk, chest);
                        }

                        if(timeEvents != null) {
                            new TimeUpdateIO().loadTimeEvents(chunk, timeEvents);
                        }

                        if(heatableBlocks != null){
                            new HeatableBlockIO().loadHeatableBlocks(chunk, heatableBlocks);
                        }

                        if(crafting3DItems != null){
                            new Crafting3DItemsIO().loadCrafting3DItems(chunk, crafting3DItems);
                        }

                        if(craftingItems != null){
                            new CraftingItemsIO().loadCraftingItems(chunk, craftingItems);
                        }

                        if(cropStates != null){
                            new CropStateIO().loadCropStates(chunk, cropStates);
                        }

                        if(tilledSoilStates != null){
                            new TilledSoilStateIO().loadTilledSoilStates(chunk, tilledSoilStates);
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
