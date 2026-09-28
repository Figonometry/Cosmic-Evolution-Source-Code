package spacegame.world.worldtypes.earthlike;

import org.joml.SimplexNoise;
import spacegame.block.*;
import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;
import spacegame.util.MathUtil;
import spacegame.world.*;
import spacegame.world.worldtypes.ChunkTerrainHandler;
import spacegame.world.worldtypes.World;

import java.util.Random;

public final class ChunkWorldEarthTerrainHandler extends ChunkTerrainHandler {
    public WorldEarth earth;
    public ChunkWorldEarthTerrainHandler(ChunkController controller, World world) {
        super(controller, world);
        this.earth = (WorldEarth) this.world;
        this.geologicProvinces = new GeologicProvince[8];
        this.geologicRegistry = new GeologicRegistry();
    }

    public void setTerrain(Chunk chunk) {
        ChunkColumnSkylightMap lightMap = this.controller.findChunkSkyLightMap(chunk.x, chunk.z);
        Chunk lowerChunk = this.controller.findChunkFromChunkCoordinates(chunk.x, chunk.y - 1, chunk.z);

        double noise;
        int x = 0;
        int y = 0;
        int z = 0;
        int dirtDepth = 0;
        int tickableIndex = 0;
        chunk.tickableBlockIndex = new short[32768];
        boolean chunkContainsOnlyAir = true;
        boolean isDesert;
        for (int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++) {
            x = chunk.getBlockXFromIndex(i);
            y = chunk.getBlockYFromIndex(i);
            z = chunk.getBlockZFromIndex(i);
            noise = this.getTerrainNoise(x, y, z);
            isDesert = this.isDesert(x, y, z);
            if (noise >= this.solidNoiseThreshold) {
                chunk.setBlock(i, this.getStoneType(x, y, z));
                chunkContainsOnlyAir = false;
                if (Block.list[chunk.getBlockID(i)].isSolid) {
                    if (lightMap.isHeightGreater(x, y, z)) {
                        lightMap.updateLightMap(x, y, z);
                        chunk.setBlockSkyLightValue(x,y,z, (byte) 0);
                        chunk.updateSkylight = true;
                    }
                }
            } else if (y <= 0) {
                chunk.updateSkylight = true;
                chunk.setBlock(i, Block.water.ID);
                chunk.containsWater = true;
                chunkContainsOnlyAir = false;
            } else {
                chunk.containsAir = true;
                chunk.setBlockSkyLightValue(x,y,z, (byte) 15);
                chunk.setBlock(i, Block.air.ID);
            }

            if (y > this.getBeachHeight(x, z) && y < 128) {
                if (this.getTerrainNoise(x, y, z) >= this.solidNoiseThreshold) {
                    if (this.getTerrainNoise(x, y + 1, z) < this.solidNoiseThreshold) {
                        chunk.setBlock(i, isDesert ? this.getSandType(x, y, z) : this.getGrassType(x, y, z));
                        dirtDepth = this.getDirtHeight(x, z);
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                chunk.setBlock(i - (1024 * j), isDesert ? this.getGravelType(chunk.getBlockXFromIndex(i - (1024 * j)), chunk.getBlockYFromIndex(i - (1024 * j)), chunk.getBlockZFromIndex(i - (1024 * j))) : this.getReducedSoilFertility(this.getSoilType(chunk.getBlockXFromIndex(i - (1024 * j)), chunk.getBlockYFromIndex(i - (1024 * j)), chunk.getBlockZFromIndex(i - (1024 * j)))));
                            } else {
                                if (lowerChunk != null) {
                                    if (lowerChunk.getBlockID(i - (1024 * j) + 32767) == this.getStoneType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767))) {
                                        lowerChunk.setBlock(i - (1024 * j) + 32767, isDesert ? this.getGravelType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767)) : this.getReducedSoilFertility(this.getSoilType(chunk.getBlockXFromIndex(i - (1024 * j) + 32767), chunk.getBlockYFromIndex(i - (1024 * j) + 32767), chunk.getBlockZFromIndex(i - (1024 * j) + 32767))));
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (y >= 128) {
                if (this.getTerrainNoise(x, y, z) >= this.solidNoiseThreshold) {
                    if (this.getTerrainNoise(x, y + 1, z) < this.solidNoiseThreshold) {
                        chunk.setBlock(i, Block.snow.ID);
                        dirtDepth = this.getDirtHeight(x, z);
                        dirtDepth *= 2;
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                chunk.setBlock(i - (1024 * j), Block.snow.ID);
                            } else {
                                if (lowerChunk != null) {
                                    if (lowerChunk.getBlockID(i - (1024 * j) + 32767) == this.getStoneType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767))) {
                                        lowerChunk.setBlock(i - (1024 * j) + 32767, Block.snow.ID);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (this.getTerrainNoise(x, y, z) >= this.solidNoiseThreshold) {
                    if (this.getTerrainNoise(x, y + 1, z) < this.solidNoiseThreshold) {
                        chunk.setBlock(i, this.getSandType(x, y, z));
                        dirtDepth = this.getDirtHeight(x, z);
                        dirtDepth *= 2;
                        for (int j = 1; j <= dirtDepth; j++) {
                            if (i - (1024 * j) > 0) {
                                chunk.setBlock(i - (1024 * j), this.getSandType(chunk.getBlockXFromIndex(i - (1024 * j)), chunk.getBlockYFromIndex(i - (1024 * j)), chunk.getBlockZFromIndex(i - (1024 * j))));
                            } else {
                                if (lowerChunk != null) {
                                    if (lowerChunk.getBlockID(i - (1024 * j) + 32767) == this.getStoneType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767))) {
                                        lowerChunk.setBlock(i - (1024 * j) + 32767, this.getSandType(lowerChunk.getBlockXFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockYFromIndex(i - (1024 * j) + 32767), lowerChunk.getBlockZFromIndex(i - (1024 * j) + 32767)));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (Block.list[chunk.getBlockID(i)] instanceof ITickable) {
                chunk.tickableBlockIndex[tickableIndex] = (short) i;
                tickableIndex++;
            }
        }
        boolean empty = tickableIndex == 0;
        chunk.truncateTickableIndexArray(tickableIndex + 1, empty);

        chunk.chunkContainsOnlyAir = chunkContainsOnlyAir;

        if(chunk.chunkContainsOnlyAir){
            chunk.isPopulated = true;
            chunk.hasDirtyLighting = false;
        }

        if(!chunk.chunkContainsOnlyAir) {
            this.carveCaves(chunk);
        }

    }


    private void carveCaves(Chunk chunk) {

        final int CS = 32;

        for (int ly = 0; ly < CS; ly++) {
            for (int lz = 0; lz < CS; lz++) {
                for (int lx = 0; lx < CS; lx++) {

                    int id = chunk.getBlockID(lx, ly, lz);
                    if (id == Block.air.ID || Block.list[id] instanceof BlockWater)
                        continue;

                    // --- WATER AVOIDANCE: ABOVE + SIDES ---
                    boolean waterNearby = false;

                    // Check above
                    for (int ay = ly + 1; ay < CS; ay++) {
                        int aboveID = chunk.getBlockID(lx, ay, lz);
                        if (Block.list[aboveID] instanceof BlockWater) {
                            waterNearby = true;
                            break;
                        }
                    }

                    // Check sides (N/S/E/W)
                    if (!waterNearby) {
                        int[][] sides = {
                                {lx + 1, ly, lz},
                                {lx - 1, ly, lz},
                                {lx, ly, lz + 1},
                                {lx, ly, lz - 1}
                        };

                        for (int[] s : sides) {
                            int sx = s[0], sy = s[1], sz = s[2];
                            if (sx >= 0 && sx < CS &&
                                    sy >= 0 && sy < CS &&
                                    sz >= 0 && sz < CS)
                            {
                                int sideID = chunk.getBlockID(sx, sy, sz);
                                if (Block.list[sideID] instanceof BlockWater) {
                                    waterNearby = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (waterNearby)
                        continue;
                    // ---------------------------------------------------

                    int wx = chunk.chunkMinX + lx;
                    int wy = chunk.chunkMinY + ly;
                    int wz = chunk.chunkMinZ + lz;

                    // Vertical warp for connectivity
                    double warpY = SimplexNoise.noise((float) (wx * 0.02), (float) (wz * 0.02), 0.0F) * 6.0;

                    // Higher frequencies → tighter, maze-like tunnels
                    double n1 = SimplexNoise.noise((float) (wx * 0.035), (float) ((wy + warpY) * 0.035), (float) (wz * 0.035));
                    double n2 = SimplexNoise.noise((float) (wx * 0.07), (float) ((wy + warpY) * 0.07), (float) (wz * 0.07));

                    // Vertical continuity field
                    double v = SimplexNoise.noise((float) (wx * 0.03), (float) (wy * 0.15), (float) (wz * 0.03));

                    // Blend fields
                    double a = (n1 * 0.55 + n2 * 0.25 + v * 0.55) * 0.75;

                    // Thickness noise
                    double b = SimplexNoise.noise((float) (wx * 0.20), (float) (wy * 0.20), (float) (wz * 0.20));
                    b = Math.max(0.0, Math.min(b, 1.0));

                    // Smaller radius → tighter tunnels
                    double radius = 0.06 + b * 0.10;

                    // Softer depth attenuation
                    double depthFactor = Math.max(0.25, (32 - ly) / 32.0);

                    double surfaceFade = Math.min(1.0, (ly / 16.0)); // fade out top 8 blocks
                    double sdf = ((a * depthFactor * surfaceFade) - radius);


                    // Inverted carve condition
                    if (sdf > 0.015f) {

                        for (int dx = 0; dx < 2; dx++)
                            for (int dy = 0; dy < 2; dy++)
                                for (int dz = 0; dz < 2; dz++) {

                                    int cx = lx + dx;
                                    int cy = ly + dy;
                                    int cz = lz + dz;

                                    if (cx >= 0 && cx < CS &&
                                            cy >= 0 && cy < CS &&
                                            cz >= 0 && cz < CS)
                                    {
                                        chunk.setBlock(cx, cy, cz, Block.air.ID);
                                    }
                                }
                    }
                }
            }
        }
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
        return  (this.earth.terrainNoise.getNoiseForTerrain(x,y,z,4, this.getContinentalNoise(x,z), this.getYScaleNoise(x,z)) + this.earth.secondaryTerrainNoise.getNoiseForTerrain(x,y,z, this.earth.sampleNoise.getNoiseIntCasted(x >> 5, z >> 5), this.getContinentalNoise(x,z), this.getYScaleNoise(x,z))) / 2;
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

    //Re-use a noisemap for this, it's not worth holding another noisemap just for this in memory
    public int getBeachHeight(int x, int z){
        return this.earth.dirtNoise.getNoiseIntCasted(x,z);
    }

    public void populateChunk(Chunk chunk) {
        //retrieve a list of all grass blocks, and maybe other blocks to not have to loop all 32k blocks at once
        Random rand = new Random(CosmicEvolution.instance.save.seed & new LongHasher().hash(CosmicEvolution.instance.save.seed, String.valueOf(chunk.x & chunk.y * chunk.z)));
        WorldGenTree worldGenTree;
        int rockCount = rand.nextInt(6);
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

        for(int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++){
            if(Block.list[chunk.getBlockID(i)] instanceof BlockGrass){
                grassIndicesRaw[grassIndex] = (short) i;
                grassIndex++;
            }
            if(Block.list[chunk.getBlockID(i)] instanceof BlockSand && chunk.parentWorld.getBlockID(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i) + 1, chunk.getBlockZFromIndex(i)) == Block.air.ID && this.isDesert(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i), chunk.getBlockZFromIndex(i))){
                surfaceSandIndices[sandIndex] = (short) i;
                sandIndex++;
            }
            if(chunk.getBlockID(i) == Block.water.ID && chunk.parentWorld.getBlockID(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i) + 1, chunk.getBlockZFromIndex(i)) == Block.air.ID && Block.list[chunk.parentWorld.getBlockID(chunk.getBlockXFromIndex(i), chunk.getBlockYFromIndex(i) - 1, chunk.getBlockZFromIndex(i))].isSolid){
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

            //There is a 25% chance that the stone placed is flint instead
            if(this.world.getBlockID(x,y,z) == Block.air.ID && Block.list[this.world.getBlockID(x, y - 1, z)] instanceof BlockGrass){
                this.world.setBlockAndNotify(x,y,z, rand.nextInt(5) == 0 ? Block.flintItemStone.ID : this.getItemStoneType(x,y,z), false);
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


        for(int i = 0; i < Chunk.NUMBER_OF_BLOCKS; i++) {
            x = chunk.getBlockXFromIndex(i);
            y = chunk.getBlockYFromIndex(i);
            z = chunk.getBlockZFromIndex(i);
            if (this.earth.doesBlockHaveSkyAccess(x, y + 1, z) && this.isFrozenBiome(x, y, z) && this.earth.getBlockID(x, y + 1 , z) == Block.air.ID && (Block.list[chunk.getBlockID(i)].isSolid || Block.list[chunk.getBlockID(i)] instanceof BlockWater)) { //place snow and ice
                if (Block.list[chunk.getBlockID(i)] instanceof BlockWater) {
                    chunk.setBlock(i, Block.ice.ID);
                } else {
                    if (chunk.isBlockInCallingChunkExcludeEdge(x, y + 1, z)) {
                        chunk.setBlock(i + 1024, Block.snowLayer.ID);
                    } else {
                        this.earth.setBlock(x, y + 1, z, Block.snowLayer.ID);
                        chunk.firstRender = true;
                    }
                }
            }
        }

        chunk.isPopulated = true;
    }

    protected GeologicProvince getGeologicProvince(int x, int y, int z){
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






    protected short getStoneType(int x, int y, int z){
        return this.geologicRegistry.getStoneTypeID(this.getGeologicProvince(x,y,z).getRockType(x,z));
    }

    protected short getSandType(int x, int y, int z){
        return this.geologicRegistry.getSandTypeID(this.getGeologicProvince(x,y,z).getRockType(x,z));
    }

    protected short getGravelType(int x, int y, int z){
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

