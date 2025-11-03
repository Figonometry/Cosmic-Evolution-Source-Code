package spacegame.world;


import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;

import java.io.File;

public final class WorldEarth extends World {
    public NoiseMap2D treeNoise;
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

    public WorldEarth(CosmicEvolution cosmicEvolution, int size) {
        super(cosmicEvolution,size);
        this.skyLightLevel = 15;
        this.skyColor = new float[]{0.52734375F, 0.8046875F, 0.91796875F};
        this.defaultSkyColor = new float[]{0.52734375F, 0.8046875F, 0.91796875F};
        this.skyLightColor = new float[]{1, 1, 1, 0}; //ANY COLOR CANNOT BE 0
        this.worldFolder = new File(this.ce.save.saveFolder + "/worlds/worldEarth");
        if(!this.worldFolder.exists()){
            this.worldFolder.mkdirs();
        }
        World.totalMaps = 14;
    }

    @Override
    public void initNoiseMaps() {
        LongHasher longHasher = new LongHasher();
        World.worldLoadPhase = 0;
        World.noiseMapsCompleted = 0;
        this.treeNoise = new NoiseMap2D(255, 255, 2, 3, 1, 2, longHasher.hash(this.ce.save.seed, "EarthLike1"));
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
        World.noiseMapsCompleted++;
        this.secondaryTerrainNoise = new NoiseMap3D(345, 256, 345, 16, longHasher.hash(this.ce.save.seed, "EarthLike7"));
        World.noiseMapsCompleted++;
        this.secondaryContinentalNoise = new NoiseMap2D(631, 631, 3, 1, 1, 0, longHasher.hash(this.ce.save.seed, "EarthLike8"));
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
        World.worldLoadPhase = 1;
    }

    @Override
    public void saveWorld() {
        this.chunkController.saveAllRegions();
    }

    @Override
    public void loadWorld() {

    }

    @Override
    public void saveWorldWithoutUnload(){
        this.chunkController.saveAllRegionsWithoutUnload();
    }

    public void tick() {
        this.chunkController.tick();
        if(this.delayWhenExitingUI > 0) {
            this.delayWhenExitingUI--;
        }
    }


    public int convertBlockXToGlobalMap(int x) {
        double blocksPerPixel = (double) this.size / 4096;
        return  (int)((x + (this.size / 2)) / blocksPerPixel);
    }

    public int convertBlockZToGlobalMap(int z) {
        double blocksPerPixel = (double) this.size / 8192;
        return (int)((z + (this.size / 2)) / blocksPerPixel);
    }






}
