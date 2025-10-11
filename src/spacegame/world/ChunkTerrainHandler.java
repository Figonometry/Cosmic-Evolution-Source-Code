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
    public World world;
    public WorldEarth earth;
    public ChunkTerrainHandler(ChunkController controller, World world){
        this.controller = controller;
        this.world = world;
        if(this.world instanceof WorldEarth){
            this.earth = (WorldEarth) this.world;
        }
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
        boolean isDesert;
        for(int i = 0; i < blocks.length; i++) {
            chunk.lightColor[i] =  new Color(this.controller.parentWorld.skyLightColor[0], this.controller.parentWorld.skyLightColor[1], this.controller.parentWorld.skyLightColor[2]).getRGB(); //This is here for efficiency reasons despite not being related to terrain
            x = chunk.getBlockXFromIndex(i);
            y = chunk.getBlockYFromIndex(i);
            z = chunk.getBlockZFromIndex(i);
            noise = this.getTerrainNoise(x,y,z);
            isDesert = this.isDesert(x,y,z);
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
                        blocks[i] = isDesert ? Block.sand.ID : Block.grass.ID;
                        dirtDepth = this.getDirtHeight(x, z);
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                blocks[i - (1024 * j)] = isDesert ? Block.sand.ID : Block.dirt.ID;
                            } else {
                                if (lowerChunk != null) {
                                    if (lowerChunk.blocks == null) {
                                        lowerChunk.initChunk();
                                    }
                                    if (lowerChunk.blocks[i - (1024 * j) + 32767] == Block.stone.ID) {
                                        lowerChunk.blocks[i - (1024 * j) + 32767] = isDesert ? Block.sand.ID : Block.dirt.ID;
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

    public boolean isDesert(int x, int y, int z){
        double rainfall = this.world.getAverageRainfall(x,z);
        return rainfall < 0.3;
    }


    public double getTerrainNoise(int x, int y, int z){
        return  (this.earth.terrainNoise.getNoise(x,y,z,4, this.getContinentalNoise(x,z), this.getYScaleNoise(x,z)) + this.earth.secondaryTerrainNoise.getNoise(x,y,z, this.earth.sampleNoise.getNoise(x >> 5, z >> 5), this.getContinentalNoise(x,z), this.getYScaleNoise(x,z))) / 2;
    }


    public int getDirtHeight(int x, int z){
        return this.earth.dirtNoise.getNoise(x,z);
    }

    public double getContinentalNoise(int x, int z){
        return (this.earth.continentalNoise.getNoiseRaw(x >> 5, z >> 5) + this.earth.secondaryContinentalNoise.getNoiseRaw(x >> 5, z >> 5)) / 2;
    }

    public double getYScaleNoise(int x, int z){
        return (this.earth.scaleNoise.getNoiseRaw(x >> 5,z >> 5) + this.earth.secondaryScaleNoise.getNoiseRaw(x >> 5, z >> 5)) / 2;
    }

    public void populateChunk(Chunk chunk) {
        //retrieve a list of all grass blocks, and maybe other blocks to not have to loop all 32k blocks at once
        Random rand = new Random(SpaceGame.instance.save.seed & (chunk.x + chunk.y * chunk.z));
        WorldGenTree worldGenTree;
        int treeCount = 0;
        int rockCount = 0;
        if(chunk.parentWorld instanceof WorldEarth){
            treeCount = 2 + ((WorldEarth)chunk.parentWorld).treeNoise.getNoise(chunk.x, chunk.z);
            rockCount = 2 + ((WorldEarth)chunk.parentWorld).treeNoise.getNoise(chunk.z, chunk.x);
        }
        int berryClusterCount = rand.nextInt(40) == 0 ? 1 : 0;
        int cactusCount = rand.nextInt(10) == 0 ? 4 : 1;
        int tallGrassCount = rand.nextInt(20, 30);
        short[] grassIndicesRaw = new short[32768];
        short[] surfaceSandIndices = new short[32768];
        int grassIndex = 0;
        int sandIndex = 0;
        int x = 0;
        int y = 0;
        int z = 0;

        for(int i = 0; i < chunk.blocks.length; i++){
            if(chunk.blocks[i] == Block.grass.ID){
                grassIndicesRaw[grassIndex] = (short) i;
                grassIndex++;
            }
            if(chunk.blocks[i] == Block.sand.ID && chunk.parentWorld.getBlockID(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i) + 1, chunk.getBlockZFromIndex(i)) == Block.air.ID && this.isDesert(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i), chunk.getBlockZFromIndex(i))){
                surfaceSandIndices[sandIndex] = (short) i;
                sandIndex++;
            }
        }

        short[] grassIndices = new short[grassIndex + 1];
        for(int i = 0; i < grassIndices.length; i++){
            grassIndices[i] = grassIndicesRaw[i];
        }

        short[] sandIndices = new short[sandIndex + 1];
        for(int i = 0; i < sandIndices.length; i++){
            sandIndices[i] = surfaceSandIndices[i];
        }

        while (cactusCount > 0 && sandIndex > 0){ //Should not generate outside of desert regions
            WorldGenCactus worldGenCactus = new WorldGenCactus(chunk, (WorldEarth)chunk.parentWorld, sandIndices[rand.nextInt(sandIndices.length)]);
            cactusCount--;
        }

        while (treeCount > 0 && grassIndex > 0) {
            worldGenTree = new WorldGenTree(chunk, (WorldEarth) chunk.parentWorld, grassIndices[rand.nextInt(grassIndices.length)]);
            treeCount--;
        }

        while (berryClusterCount > 0 && grassIndex > 0){
            new WorldGenBerryBush(chunk, (WorldEarth)chunk.parentWorld, grassIndices[rand.nextInt(grassIndices.length)]);
            berryClusterCount--;
        }

        int stonePlacementIndex = 0;
        while(rockCount > 0 && grassIndex > 0) {
            stonePlacementIndex = grassIndices[rand.nextInt(grassIndices.length)];
            x = chunk.getBlockXFromIndex(stonePlacementIndex);
            y = chunk.getBlockYFromIndex(stonePlacementIndex) + 1;
            z = chunk.getBlockZFromIndex(stonePlacementIndex);

            if(this.world.getBlockID(x,y,z) == Block.air.ID && this.world.getBlockID(x, y - 1, z) == Block.grass.ID){
                this.world.setBlockWithNotify(x,y,z, Block.itemStone.ID);
            }
            rockCount--;
        }

        int tallGrassPlacementIndex = 0;
        while (tallGrassCount > 0 && grassIndex > 0){
            tallGrassPlacementIndex = grassIndices[rand.nextInt(grassIndices.length)];
            x = chunk.getBlockXFromIndex(tallGrassPlacementIndex);
            y = chunk.getBlockYFromIndex(tallGrassPlacementIndex) + 1;
            z = chunk.getBlockZFromIndex(tallGrassPlacementIndex);

            if(this.world.getBlockID(x,y,z) == Block.air.ID && this.world.getBlockID(x, y - 1, z) == Block.grass.ID){
                this.world.setBlockWithNotify(x,y,z, Block.tallGrass.ID);
            }

            tallGrassCount--;
        }

        chunk.populated = true;
    }
}
