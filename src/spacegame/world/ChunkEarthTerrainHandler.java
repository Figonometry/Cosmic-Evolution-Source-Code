package spacegame.world;

import spacegame.block.*;
import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;

import java.awt.*;
import java.util.Random;

public final class ChunkEarthTerrainHandler {
    public ChunkController controller;
    public double solidNoiseThreshold = 0D;
    public World world;
    public WorldEarth earth;
    public GeologicRegistry geologicRegistry;
    public GeologicProvince[] geologicProvinces = new GeologicProvince[8];
    public ChunkEarthTerrainHandler(ChunkController controller, World world){
        this.controller = controller;
        this.world = world;
        if(this.world instanceof WorldEarth){
            this.earth = (WorldEarth) this.world;
        }
        this.geologicRegistry = new GeologicRegistry();
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
                blocks[i] = this.getStoneType(x,y,z);
                chunk.empty = false;
                if(Block.list[chunk.blocks[i]].isSolid)
                    if(lightMap.isHeightGreater(x,y,z)){
                        lightMap.updateLightMap(x,y,z);
                        chunk.light[Chunk.getBlockIndexFromCoordinates(x, y, z)] = 0;
                        chunk.updateSkylight = true;
                    }
            } else if(y <= 0){
                chunk.updateSkylight = true;
                blocks[i] = Block.water.ID;
                chunk.containsWater = true;
                chunk.empty = false;
            } else {
                chunk.containsAir = true;
                chunk.light[Chunk.getBlockIndexFromCoordinates(x, y, z)] = (byte) (15 << 4);
            }

            if(y > 3 && y < 128) {
                if (this.getTerrainNoise(x, y, z) >= this.solidNoiseThreshold) {
                    if (this.getTerrainNoise(x, y + 1, z) < this.solidNoiseThreshold) {
                        blocks[i] = isDesert ? this.getSandType(x,y,z) : this.getGrassType(x,y,z);
                        dirtDepth = this.getDirtHeight(x, z);
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                blocks[i - (1024 * j)] = isDesert ? this.getGravelType(chunk.getBlockXFromIndex(i - (1024 * j)), chunk.getBlockYFromIndex(i - (1024 * j)), chunk.getBlockZFromIndex(i - (1024 * j))) : this.getReducedSoilFertility(this.getSoilType(chunk.getBlockXFromIndex(i - (1024 * j)), chunk.getBlockYFromIndex(i - (1024 * j)), chunk.getBlockZFromIndex(i - (1024 * j))));
                            } else {
                                if (lowerChunk != null) {
                                    if (lowerChunk.blocks == null) {
                                        lowerChunk.initChunk();
                                    }
                                    if (lowerChunk.blocks[i - (1024 * j) + 32767] == this.getStoneType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767))) {
                                        lowerChunk.blocks[i - (1024 * j) + 32767] = isDesert ? this.getGravelType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767)) : this.getReducedSoilFertility(this.getSoilType(chunk.getBlockXFromIndex(i - (1024 * j) + 32767), chunk.getBlockYFromIndex(i - (1024 * j) + 32767), chunk.getBlockZFromIndex(i - (1024 * j) + 32767)));
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
                                    if (lowerChunk.blocks[i - (1024 * j) + 32767] == this.getStoneType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767))) {
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
                        blocks[i] = this.getSandType(x,y,z);
                        dirtDepth = this.getDirtHeight(x, z);
                        dirtDepth *= 2;
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                blocks[i - (1024 * j)] = this.getSandType(chunk.getBlockXFromIndex(i - (1024 * j)), chunk.getBlockYFromIndex(i - (1024 * j)), chunk.getBlockZFromIndex(i - (1024 * j)));
                            } else {
                                if (lowerChunk != null) {
                                    if(lowerChunk.blocks == null){
                                        lowerChunk.initChunk();
                                    }
                                    if (lowerChunk.blocks[i - (1024 * j) + 32767] == this.getStoneType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767))) {
                                        lowerChunk.blocks[i - (1024 * j) + 32767] = this.getSandType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767));
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
        return rainfall < 0.3 && !this.isFrozenBiome(x,y,z);
    }

    public boolean isFrozenBiome(int x, int y, int z){
        double temperature = this.world.getAverageTemperature(x,y,z);
        return temperature < 0.3;
    }


    public double getTerrainNoise(int x, int y, int z){
        return  (this.earth.terrainNoise.getNoise(x,y,z,4, this.getContinentalNoise(x,z), this.getYScaleNoise(x,z)) + this.earth.secondaryTerrainNoise.getNoise(x,y,z, this.earth.sampleNoise.getNoiseIntCasted(x >> 5, z >> 5), this.getContinentalNoise(x,z), this.getYScaleNoise(x,z))) / 2;
    }


    public int getDirtHeight(int x, int z){
        return this.earth.dirtNoise.getNoiseIntCasted(x,z);
    }

    public double getContinentalNoise(int x, int z){ //Elevation
        return (this.earth.globalElevationMap.getNoiseRaw(this.earth.convertBlockZToGlobalMap(z), this.earth.convertBlockXToGlobalMap(x)) + this.earth.continentalNoise.getNoiseRaw(x >> 5, z >> 5) / 2d);
    }

    public double getYScaleNoise(int x, int z){
        return (this.earth.scaleNoise.getNoiseRaw(x >> 5,z >> 5) + this.earth.secondaryScaleNoise.getNoiseRaw(x >> 5, z >> 5)) / 2;
    }

    public double getTreeNoise(int x, int z){
        return (this.earth.treeNoise.getNoiseRaw(x, z) + this.earth.secondaryTreeNoise.getNoiseRaw(x, z)) * 0.5;
    }

    public int getChunkTreeDensity(int x, int z){
        return (int) (this.earth.treeDensityNoise1.getNoiseIntCasted(x,z) + this.earth.treeDensityNoise2.getNoiseIntCasted(x,z) * 0.5f);
    }

    public void populateChunk(Chunk chunk) {
        //retrieve a list of all grass blocks, and maybe other blocks to not have to loop all 32k blocks at once
        Random rand = new Random(CosmicEvolution.instance.save.seed & new LongHasher().hash(CosmicEvolution.instance.save.seed, String.valueOf(chunk.x & chunk.y * chunk.z)));
        WorldGenTree worldGenTree;
        int rockCount = 0;
        if(chunk.parentWorld instanceof WorldEarth){
            rockCount = 2 + ((WorldEarth)chunk.parentWorld).treeNoise.getNoiseIntCasted(chunk.z, chunk.x);
        }
        int berryClusterCount = rand.nextInt(40) == 0 ? 1 : 0;
        int cactusCount = rand.nextInt(10) == 0 ? 4 : 1;
        int tallGrassCount = rand.nextInt(20, 30);
        boolean generateClayBlob = rand.nextInt(5) == 0;
        short[] grassIndicesRaw = new short[32768];
        short[] surfaceSandIndices = new short[32768];
        short[] surfaceWaterIndices = new short[32768];
        int grassIndex = 0;
        int sandIndex = 0;
        int waterIndex = 0;
        int x = 0;
        int y = 0;
        int z = 0;

        for(int i = 0; i < chunk.blocks.length; i++){
            if(Block.list[chunk.blocks[i]] instanceof BlockGrass){
                grassIndicesRaw[grassIndex] = (short) i;
                grassIndex++;
            }
            if(Block.list[chunk.blocks[i]] instanceof BlockSand && chunk.parentWorld.getBlockID(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i) + 1, chunk.getBlockZFromIndex(i)) == Block.air.ID && this.isDesert(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i), chunk.getBlockZFromIndex(i))){
                surfaceSandIndices[sandIndex] = (short) i;
                sandIndex++;
            }
            if(chunk.blocks[i] == Block.water.ID && chunk.parentWorld.getBlockID(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i) + 1, chunk.getBlockZFromIndex(i)) == Block.air.ID && Block.list[chunk.parentWorld.getBlockID(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i) - 1, chunk.getBlockZFromIndex(i))].isSolid){
                surfaceWaterIndices[waterIndex] = (short) i;
                waterIndex++;
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

        short[] waterIndices = new short[waterIndex + 1];
        for(int i = 0; i < waterIndices.length; i++){
            waterIndices[i] = surfaceWaterIndices[i];
        }

        if(generateClayBlob && grassIndex > 0){
            new WorldGenClayBlob(chunk, (WorldEarth)chunk.parentWorld, grassIndices[rand.nextInt(grassIndices.length)]);
        }

        while (cactusCount > 0 && sandIndex > 0){ //Should not generate outside of desert regionMap
            WorldGenCactus worldGenCactus = new WorldGenCactus(chunk, (WorldEarth)chunk.parentWorld, sandIndices[rand.nextInt(sandIndices.length)]);
            cactusCount--;
        }

        int treeX;
        int treeZ;
        for(int i = 0; i < grassIndices.length; i += 96 - this.getChunkTreeDensity(chunk.x, chunk.z)){ //interval for i directly controls tree density
            treeX = chunk.getBlockXFromIndex(grassIndices[i]);
            treeZ = chunk.getBlockZFromIndex(grassIndices[i]);
            short groundBlockID = chunk.parentWorld.getBlockID(treeX, chunk.getBlockYFromIndex(grassIndices[i]) ,treeZ);
            if(this.getTreeNoise(treeX, treeZ) < 0 || !(Block.list[groundBlockID] instanceof BlockGrass)  && !(Block.list[groundBlockID] instanceof BlockClayGrass))continue;

            new WorldGenTree(chunk, (WorldEarth) chunk.parentWorld, grassIndices[rand.nextInt(grassIndices.length)], true);
        }


        if(waterIndex > 0 && rand.nextBoolean()) {
            new WorldGenReeds(chunk, (WorldEarth)chunk.parentWorld, waterIndices[rand.nextInt(waterIndices.length)]);
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

            if(this.world.getBlockID(x,y,z) == Block.air.ID && Block.list[this.world.getBlockID(x, y - 1, z)] instanceof BlockGrass){
                this.world.setBlockAndNotify(x,y,z, this.getItemStoneType(x,y,z), false);
            }
            rockCount--;
        }

        int tallGrassPlacementIndex = 0;
        while (tallGrassCount > 0 && grassIndex > 0){
            tallGrassPlacementIndex = grassIndices[rand.nextInt(grassIndices.length)];
            x = chunk.getBlockXFromIndex(tallGrassPlacementIndex);
            y = chunk.getBlockYFromIndex(tallGrassPlacementIndex) + 1;
            z = chunk.getBlockZFromIndex(tallGrassPlacementIndex);

            if(this.world.getBlockID(x,y,z) == Block.air.ID && Block.list[this.world.getBlockID(x, y - 1, z)] instanceof BlockGrass){
                this.world.setBlockAndNotify(x,y,z, Block.tallGrass.ID, false);
                chunk.tallGrassCount++;
            }

            tallGrassCount--;
        }


        for(int i = 0; i < chunk.blocks.length; i++) {
            x = chunk.getBlockXFromIndex(i);
            y = chunk.getBlockYFromIndex(i);
            z = chunk.getBlockZFromIndex(i);
            if (this.earth.doesBlockHaveSkyAccess(x, y + 1, z) && this.isFrozenBiome(x, y, z) && this.earth.getBlockID(x, y + 1 , z) == Block.air.ID && (Block.list[chunk.blocks[i]].isSolid || Block.list[chunk.blocks[i]] instanceof BlockWater)) { //place snow and ice
                if (Block.list[chunk.blocks[i]] instanceof BlockWater) {
                    chunk.blocks[i] = Block.ice.ID;
                } else {
                    if (chunk.isBlockInCallingChunkExcludeEdge(x, y + 1, z)) {
                        chunk.blocks[i + 1024] = Block.snowLayer.ID;
                    } else {
                        this.earth.setBlock(x, y + 1, z, Block.snowLayer.ID);
                        chunk.firstRender = true;
                    }
                }
            }
        }

        chunk.populated = true;
    }

    private GeologicProvince getGeologicProvince(int x, int y, int z){
        GeologicProvince geologicProvince = null;

        // --- Normal range check ---
        for(int i = 0; i < this.geologicProvinces.length; i++) {

            int height = this.geologicProvinces[i].heightMap.getNoiseIntCasted(x, z);

            int lowerBoundNormal = height - (this.geologicProvinces[7].floor - this.geologicProvinces[0].floor);

            if (y <= height && y >= lowerBoundNormal) {
                geologicProvince = this.geologicProvinces[i];
                break;
            }

            if (y >= this.geologicProvinces[7].floor + 64 ||
                    y >= this.geologicProvinces[7].heightMap.getNoiseIntCasted(x, z)) {

                geologicProvince = this.geologicProvinces[7];
                break;
            }
        }

        // --- Determine shift direction ---
        boolean yBelowBottom = y < this.geologicProvinces[0].floor;
        boolean yAboveTop    = y > this.geologicProvinces[7].floor;

        int floorDifference = this.geologicProvinces[7].floor - this.geologicProvinces[0].floor;

        // --- Shifted comparison if not found ---
        int timesLooped = 1;
        int maxLoops = 64;

        while (geologicProvince == null && timesLooped < maxLoops) {

            for(int i = 0; i < this.geologicProvinces.length; i++) {

                int baseHeight = this.geologicProvinces[i].heightMap.getNoiseIntCasted(x, z);

                int heightShifted;
                int lowerBoundShifted;

                if (yBelowBottom) {
                    // --- Shift provinces UPWARD ---
                    heightShifted = baseHeight + (floorDifference * timesLooped);
                    lowerBoundShifted = heightShifted - floorDifference;

                    if (y >= lowerBoundShifted && y <= heightShifted) {
                        geologicProvince = this.geologicProvinces[i];
                        break;
                    }

                } else {
                    // --- Shift provinces DOWNWARD ---
                    heightShifted = baseHeight - (floorDifference * timesLooped);
                    lowerBoundShifted = heightShifted - floorDifference;

                    if (y <= heightShifted && y >= lowerBoundShifted) {
                        geologicProvince = this.geologicProvinces[i];
                        break;
                    }
                }
            }

            timesLooped++;
        }

        if (geologicProvince == null) {
            throw new IllegalStateException(
                    "Could not resolve geologic province for y=" + y +
                            " after " + (timesLooped - 1) + " shifts."
            );
        }

        return geologicProvince;
    }






    public short getStoneType(int x, int y, int z){
        return this.geologicRegistry.getStoneTypeID(this.getGeologicProvince(x,y,z).getRockType(x,z));
    }

    public short getSandType(int x, int y, int z){
        return this.geologicRegistry.getSandTypeID(this.getGeologicProvince(x,y,z).getRockType(x,z));
    }

    public short getGravelType(int x, int y, int z){
        return this.geologicRegistry.getGravelTypeID(this.getGeologicProvince(x,y,z).getRockType(x,z));
    }

    public short getItemStoneType(int x, int y, int z){
        switch (this.geologicRegistry.getStoneTypeID(this.getGeologicProvince(x,y,z).getRockType(x,z))){
            case BlockIDList.ANDESITE_STONE -> {
                return Block.andesiteItemStone.ID;
            }
            case BlockIDList.GRANITE_STONE -> {
                return Block.graniteItemStone.ID;
            }
            case BlockIDList.PERIODITE_STONE -> {
                return Block.perioditeItemStone.ID;
            }
            case BlockIDList.OBSIDIAN_STONE -> {
                return Block.obsidianItemStone.ID;
            }
            case BlockIDList.BASALT_STONE -> {
                return Block.basaltItemStone.ID;
            }
            case BlockIDList.GABBRO_STONE -> {
                return Block.gabbroItemStone.ID;
            }
            case BlockIDList.CHALK_STONE -> {
                return Block.chalkItemStone.ID;
            }
            case BlockIDList.CHERT_STONE -> {
                return Block.chertItemStone.ID;
            }
            case BlockIDList.CLAYSTONE_STONE -> {
                return Block.claystoneItemStone.ID;
            }
            case BlockIDList.CONGLOMERATE_STONE -> {
                return Block.conglomerateItemStone.ID;
            }
            case BlockIDList.SHALE_STONE -> {
                return Block.shaleItemStone.ID;
            }
            case BlockIDList.LIMESTONE_STONE -> {
                return Block.limestoneItemStone.ID;
            }
            case BlockIDList.SANDSTONE_STONE -> {
                return Block.sandstoneItemStone.ID;
            }
            case BlockIDList.MARBLE_STONE -> {
                return Block.marbleItemStone.ID;
            }
            case BlockIDList.SLATE_STONE -> {
                return Block.slateItemStone.ID;
            }
            case BlockIDList.PHYLLITE_STONE -> {
                return Block.phylliteItemStone.ID;
            }
            case BlockIDList.SERPENTINITE_STONE -> {
                return Block.serpentiniteItemStone.ID;
            }
            default -> {
                return BlockIDList.AIR;
            }
        }
    }


    public short getGrassType(int x, int y, int z){
        switch (this.getSoilType(x,y,z)){
            case BlockIDList.BARREN_SOIL -> {
                return BlockIDList.GRASS_BARREN_FERTILITY_LARGE_PATCH;
            }

            case BlockIDList.LOW_FERTILITY_SOIL -> {
                return BlockIDList.GRASS_LOW_FERTILITY_FULL;
            }

            case BlockIDList.MEDIUM_FERTILITY_SOIL -> {
                return BlockIDList.GRASS_MEDIUM_FERTILITY_FULL;
            }

            case BlockIDList.HIGH_FERTILITY_SOIL -> {
                return BlockIDList.GRASS_HIGH_FERTILITY_FULL;
            }

            default -> {
                throw new IllegalStateException();
            }
        }
    }

    public short getSoilType(int x, int y, int z) {
        double rainfall = this.world.getAverageRainfall(x, z);
        double temperature = this.world.getAverageTemperature(x, y, z);

        if(rainfall > 0.7 && temperature > 0.8){
            return BlockIDList.HIGH_FERTILITY_SOIL;
        }

        if(rainfall < 0.45){
            return BlockIDList.BARREN_SOIL;
        }

        if(rainfall >= 0.45 && rainfall <= 0.6){
            return BlockIDList.LOW_FERTILITY_SOIL;
        }


        return BlockIDList.MEDIUM_FERTILITY_SOIL;
    }

    public short getReducedSoilFertility(short blockID){
        switch (blockID){
            case BlockIDList.BARREN_SOIL, BlockIDList.LOW_FERTILITY_SOIL -> {
                return BlockIDList.BARREN_SOIL;
            }
            case BlockIDList.MEDIUM_FERTILITY_SOIL -> {
                return BlockIDList.LOW_FERTILITY_SOIL;
            }
            case BlockIDList.HIGH_FERTILITY_SOIL -> {
                return BlockIDList.MEDIUM_FERTILITY_SOIL;
            }

            default -> {
                return blockID;
            }
        }
    }
}

