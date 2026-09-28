package spacegame.world.worldtypes.earthlike;


import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;
import spacegame.world.GeologicProvince;
import spacegame.world.NoiseMap2D;
import spacegame.world.NoiseMap3D;
import spacegame.world.worldtypes.ChunkTerrainHandler;
import spacegame.world.worldtypes.World;

import java.io.File;

public final class WorldEarth extends World {
    public NoiseMap2D treeNoise;
    public NoiseMap2D secondaryTreeNoise;
    public NoiseMap2D berryNoise;
    public NoiseMap3D terrainNoise;
    public NoiseMap3D secondaryTerrainNoise;
    public NoiseMap2D dirtNoise;
    public NoiseMap2D sampleNoise;
    public NoiseMap2D continentalNoise;
    public NoiseMap2D secondaryContinentalNoise;
    public NoiseMap2D scaleNoise;
    public NoiseMap2D secondaryScaleNoise;
    public NoiseMap2D temperatureNoise1;
    public NoiseMap2D temperatureNoise2;
    public NoiseMap2D rainfallNoise1;
    public NoiseMap2D rainfallNoise2;
    public NoiseMap2D globalElevationMap;
    public NoiseMap2D globalRainfallMap;
    public NoiseMap2D globalTemperatureMap;
    public NoiseMap2D treeDensityNoise1;
    public NoiseMap2D treeDensityNoise2;
    public NoiseMap3D tunnelNoise;
    public NoiseMap3D thicknessNoise;

    public WorldEarth(CosmicEvolution cosmicEvolution, int size) {
        super(cosmicEvolution,size);
        this.skyLightLevel = 15;
        this.skyColor = new float[]{0.52734375F, 0.8046875F, 0.91796875F};
        this.defaultSkyColor = new float[]{0.52734375F, 0.8046875F, 0.91796875F};
        this.skyLightColor = 16777215; //ANY COLOR CANNOT BE 0
        this.worldFolder = new File(this.ce.save.saveFolder + "/worlds/worldEarth");
        if(!this.worldFolder.exists()){
            this.worldFolder.mkdirs();
        }
        World.totalMaps = 19;
    }

    @Override
    public void initNoiseMaps() {
        LongHasher longHasher = new LongHasher();
        World.worldLoadPhase = 0;
        World.noiseMapsCompleted = 0;
        this.treeNoise = new NoiseMap2D(417, 417, 5, 1, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike1"));
        World.noiseMapsCompleted++;
        this.secondaryTreeNoise = new NoiseMap2D(631, 631, 3, 1, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike14"));
        World.noiseMapsCompleted++;
        this.berryNoise = new NoiseMap2D(317, 317, 1, 1, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike2"));
        World.noiseMapsCompleted++;
        this.dirtNoise = new NoiseMap2D(32, 32, 3, 2, 1, 2, longHasher.hash(this.ce.save.seed, "EarthLike3"));
        World.noiseMapsCompleted++;
        this.terrainNoise = new NoiseMap3D(256, 256, 256, 16, longHasher.hash(this.ce.save.seed, "EarthLike4"));
        World.noiseMapsCompleted++;
        this.sampleNoise = new NoiseMap2D(256, 256, 6, 7, 1, 12, longHasher.hash(this.ce.save.seed, "EarthLike5"));
        World.noiseMapsCompleted++;
        this.continentalNoise = new NoiseMap2D(256, 256, 3, 1, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike6"));
        this.continentalNoise.scaleByExponent(5);
        World.noiseMapsCompleted++;
        this.secondaryTerrainNoise = new NoiseMap3D(345, 256, 345, 16, longHasher.hash(this.ce.save.seed, "EarthLike7"));
        World.noiseMapsCompleted++;
        this.secondaryContinentalNoise = new NoiseMap2D(631, 631, 3, 1, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike8"));
        this.secondaryContinentalNoise.scaleByExponent(5);
        World.noiseMapsCompleted++;
        this.scaleNoise = new NoiseMap2D(128, 128, 16, 0.011, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike9"));
        World.noiseMapsCompleted++;
        this.secondaryScaleNoise = new NoiseMap2D(311, 311, 4, 0.011, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike10"));
        World.noiseMapsCompleted++;
        this.temperatureNoise1 = new NoiseMap2D(715, 715, 1, 0.5, 1, 0.5, longHasher.hash(this.ce.save.seed, "EarthLike11"));
        World.noiseMapsCompleted++;
        this.temperatureNoise2 = new NoiseMap2D(1379, 1379, 1, 0.5, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike12"));
        World.noiseMapsCompleted++;
        this.rainfallNoise1 = new NoiseMap2D(937, 937, 1, 0.5, 1, 0.5, longHasher.hash(this.ce.save.seed, "EarthLike13"));
        World.noiseMapsCompleted++;
        this.rainfallNoise2 = new NoiseMap2D(1579, 1579, 1, 0.5, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike14"));
        World.noiseMapsCompleted++;
        this.treeDensityNoise1 = new NoiseMap2D(935, 935, 6, 32, 1, 32, longHasher.hash(this.ce.save.seed, "EarthLike16"));
        World.noiseMapsCompleted++;
        this.treeDensityNoise2 = new NoiseMap2D(438, 438, 5, 32, 1, 32, longHasher.hash(this.ce.save.seed, "EarthLike17"));
        World.noiseMapsCompleted++;
        this.tunnelNoise = new NoiseMap3D(256,256,256, 5, longHasher.hash(this.ce.save.seed, "EarthLike18"));
        World.noiseMapsCompleted++;
        this.thicknessNoise = new NoiseMap3D(128, 128, 128, 6, longHasher.hash(this.ce.save.seed, "EarthLike19"));
        World.noiseMapsCompleted++;

        World.worldLoadPhase = 1;
    }


    @Override
    public void loadGeologicProvinces(){
        LongHasher longHasher = new LongHasher();
        this.chunkTerrainHandler.geologicProvinces[0] = new GeologicProvince(longHasher.hash(this.ce.save.seed, "EarthGeologicProvince1"), -256, this.chunkTerrainHandler.geologicRegistry);
        this.chunkTerrainHandler.geologicProvinces[1] = new GeologicProvince(longHasher.hash(this.ce.save.seed, "EarthGeologicProvince2"), -192, this.chunkTerrainHandler.geologicRegistry);
        this.chunkTerrainHandler.geologicProvinces[2] = new GeologicProvince(longHasher.hash(this.ce.save.seed, "EarthGeologicProvince3"), -128, this.chunkTerrainHandler.geologicRegistry);
        this.chunkTerrainHandler.geologicProvinces[3] = new GeologicProvince(longHasher.hash(this.ce.save.seed, "EarthGeologicProvince4"), -64, this.chunkTerrainHandler.geologicRegistry);
        this.chunkTerrainHandler.geologicProvinces[4] = new GeologicProvince(longHasher.hash(this.ce.save.seed, "EarthGeologicProvince5"), 0, this.chunkTerrainHandler.geologicRegistry);
        this.chunkTerrainHandler.geologicProvinces[5] = new GeologicProvince(longHasher.hash(this.ce.save.seed, "EarthGeologicProvince6"), 64, this.chunkTerrainHandler.geologicRegistry);
        this.chunkTerrainHandler.geologicProvinces[6] = new GeologicProvince(longHasher.hash(this.ce.save.seed, "EarthGeologicProvince7"), 192, this.chunkTerrainHandler.geologicRegistry);
        this.chunkTerrainHandler.geologicProvinces[7] = new GeologicProvince(longHasher.hash(this.ce.save.seed, "EarthGeologicProvince8"), 256, this.chunkTerrainHandler.geologicRegistry);
    }

    @Override
    public ChunkTerrainHandler getChunkTerrainHandler() {
        return new ChunkWorldEarthTerrainHandler(this.chunkController, this);
    }

    @Override
    public void saveWorld() {
        this.chunkController.saveAllRegions();
        this.saveWeatherSystemsToFile();
    }

    @Override
    public void loadWorld() {
        this.loadWeatherSystemsFromFile();
    }

    @Override
    public void saveWorldWithoutUnload(){
        this.chunkController.saveAllRegionsWithoutUnload();
    }

    public void tick() {
        this.chunkController.tick();
        if (this.delayWhenExitingUI > 0) {
            this.delayWhenExitingUI--;
        }
        if(!this.paused) {
            super.tick();
        }
    }


    public double convertBlockXToGlobalMap(int x) {
        double blocksPerPixel = (double) this.size / 4096;
        return (x + ((double) this.size / 2)) / blocksPerPixel;
    }

    public double convertBlockZToGlobalMap(int z) {
        double blocksPerPixel = (double) (this.size * 2) / 8192;
        return (z + ((double) this.size / 2)) / blocksPerPixel;
    }






}
