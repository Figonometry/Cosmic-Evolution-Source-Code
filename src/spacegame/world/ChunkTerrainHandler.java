package spacegame.world;

import spacegame.block.Block;
import spacegame.block.ITickable;
import spacegame.core.SpaceGame;
import spacegame.item.Item;

import java.awt.*;
import java.util.Random;

public final class ChunkTerrainHandler {
    public ChunkController controller;
    public double solidNoiseThreshold = 0D;
    public ChunkTerrainHandler(ChunkController controller){
        this.controller = controller;
    }

    public void setTerrain(short[] blocks, Chunk chunk) {
        ChunkColumnSkylightMap lightMap = this.controller.findChunkSkyLightMap(chunk.x, chunk.z);
        Chunk lowerChunk = this.controller.findChunkFromChunkCoordinates(chunk.x, chunk.y - 1, chunk.z);

        double noise;
        int x = 0;
        int y = 0;
        int z = 0;
        int dirtDepth = 0;
        int tickableIndex = 0;
        chunk.tickableBlockIndex = new short[32768];
        for(int i = 0; i < blocks.length; i++) {
            chunk.lightColor[i] =  new Color(this.controller.parentWorldFace.parentWorld.skyLightColor[0], this.controller.parentWorldFace.parentWorld.skyLightColor[1], this.controller.parentWorldFace.parentWorld.skyLightColor[2]).getRGB(); //This is here for efficiency reasons despite not being related to terrain
            x = chunk.getBlockXFromIndex(i);
            y = chunk.getBlockYFromIndex(i);
            z = chunk.getBlockZFromIndex(i);
            noise = this.getTerrainNoise(x,y,z);
            if (noise >= this.solidNoiseThreshold) {
                blocks[i] = Block.stone.ID;
                chunk.empty = false;
                if(lightMap.isHeightGreater(x,y,z)){
                    lightMap.updateLightMap(x,y,z);
                    chunk.lighting[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
                    chunk.skyLight[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
                    chunk.updateSkylight = true;
                }
            } else if(y <= 0){
                chunk.updateSkylight = true;
                blocks[i] = Block.water.ID;
                chunk.containsWater = true;
                chunk.empty = false;
            } else {
                chunk.containsAir = true;
                chunk.skyLight[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 15;
            }

            if(y > 3 && y < 128) {
                if (this.getTerrainNoise(x, y, z) >= this.solidNoiseThreshold) {
                    if (this.getTerrainNoise(x, y + 1, z) < this.solidNoiseThreshold) {
                        blocks[i] = Block.grass.ID;
                        dirtDepth = this.getDirtHeight(x, z);
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                blocks[i - (1024 * j)] = Block.dirt.ID;
                            } else {
                                if (lowerChunk != null) {
                                    if (lowerChunk.blocks == null) {
                                        lowerChunk.initChunk();
                                    }
                                    if (lowerChunk.blocks[i - (1024 * j) + 32767] == Block.stone.ID) {
                                        lowerChunk.blocks[i - (1024 * j) + 32767] = Block.dirt.ID;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (y >= 128){
                if (this.getTerrainNoise(x, y, z) >= this.solidNoiseThreshold) {
                    if (this.getTerrainNoise(x, y + 1, z) < this.solidNoiseThreshold) {
                        blocks[i] = Block.snow.ID;
                        dirtDepth = this.getDirtHeight(x, z);
                        dirtDepth *= 2;
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                blocks[i - (1024 * j)] = Block.snow.ID;
                            } else {
                                if (lowerChunk != null) {
                                    if(lowerChunk.blocks == null){
                                        lowerChunk.initChunk();
                                    }
                                    if (lowerChunk.blocks[i - (1024 * j) + 32767] == Block.stone.ID) {
                                        lowerChunk.blocks[i - (1024 * j) + 32767] = Block.snow.ID;
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (this.getTerrainNoise(x, y, z) >= this.solidNoiseThreshold) {
                    if (this.getTerrainNoise(x, y + 1, z) < this.solidNoiseThreshold) {
                        blocks[i] = Block.sand.ID;
                        dirtDepth = this.getDirtHeight(x, z);
                        dirtDepth *= 2;
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                blocks[i - (1024 * j)] = Block.sand.ID;
                            } else {
                                if (lowerChunk != null) {
                                    if(lowerChunk.blocks == null){
                                        lowerChunk.initChunk();
                                    }
                                    if (lowerChunk.blocks[i - (1024 * j) + 32767] == Block.stone.ID) {
                                        lowerChunk.blocks[i - (1024 * j) + 32767] = Block.sand.ID;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if(Block.list[chunk.blocks[i]] instanceof ITickable){
                chunk.tickableBlockIndex[tickableIndex] = (short) i;
                tickableIndex++;
            }
        }
        boolean empty = tickableIndex == 0;
        chunk.truncateTickableIndexArray(tickableIndex + 1, empty);

    }


    public double getTerrainNoise(int x, int y, int z){
        return  (WorldEarth.terrainNoise.getNoise(x,y,z,4, this.getContinentalNoise(x,z), this.getYScaleNoise(x,z)) + WorldEarth.secondaryTerrainNoise.getNoise(x,y,z, WorldEarth.sampleNoise.getNoise(x >> 5, z >> 5), this.getContinentalNoise(x,z), this.getYScaleNoise(x,z))) / 2;
    }


    public int getDirtHeight(int x, int z){
        return WorldEarth.dirtNoise.getNoise(x,z);
    }

    public double getContinentalNoise(int x, int z){
        return (WorldEarth.continentalNoise.getNoiseRaw(x >> 5, z >> 5) + WorldEarth.secondaryContinentalNoise.getNoiseRaw(x >> 5, z >> 5)) / 2;
    }

    public double getYScaleNoise(int x, int z){
        return (WorldEarth.scaleNoise.getNoiseRaw(x >> 5,z >> 5) + WorldEarth.secondaryScaleNoise.getNoiseRaw(x >> 5, z >> 5)) / 2;
    }

    public void populateChunk(Chunk chunk) {
        int safetyThreshold = 0;
        Random rand = new Random(SpaceGame.instance.save.seed & (chunk.x + chunk.y * chunk.z));
        WorldGenTree worldGenTree;
        boolean containsGrass = false;
        int treeCount = 2 + WorldEarth.treeNoise.getNoise(chunk.x, chunk.z);
        int rockCount = 2 + WorldEarth.treeNoise.getNoise(chunk.z, chunk.x);
        int berryClusterCount = rand.nextInt(100) == 0 ? 1 : 0;
        populate:
        while ((treeCount > 0 || rockCount > 0 || berryClusterCount > 0) && safetyThreshold < 10) {
            if(chunk.blocks == null){
                chunk.initChunk();
            }
            for (int i = 0; i < chunk.blocks.length; i++) {
                if (Block.list[chunk.blocks[i]].ID == Block.grass.ID) {
                    containsGrass = true;
                    if(rand.nextInt(100) == 0 && treeCount > 0) {
                        worldGenTree = new WorldGenTree(chunk, this.controller.parentWorldFace, i);
                        if (worldGenTree.willGenerate) {
                            treeCount--;
                            if(worldGenTree.decayIntoDirt){
                                chunk.blocks[i] = Block.dirt.ID;
                            }
                        }
                    }
                    if(rand.nextInt(100) == 0 && berryClusterCount > 0){
                        new WorldGenBerryBush(chunk, this.controller.parentWorldFace, i);
                        berryClusterCount--;
                    }

                    if(rand.nextInt(100) == 0 && rockCount > 0) {
                        chunk.addRenderableItem(chunk.getBlockXFromIndex(i),chunk.getBlockYFromIndex(i),chunk.getBlockZFromIndex(i), Item.stone.ID, Item.stone.durability);
                        rockCount--;
                    }
                    if(rockCount <= 0 && treeCount <= 0 && berryClusterCount <= 0){
                        break populate;
                    }
                }
            }
            if(!containsGrass){
                break;
            }
            safetyThreshold++;
        }
        chunk.populated = true;
    }
}
