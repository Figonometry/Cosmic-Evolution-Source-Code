package spacegame.world;


import spacegame.core.SpaceGame;

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

    public WorldEarth(SpaceGame spaceGame, int size) {
        super(spaceGame,size);
        this.activeWorldFace = new WorldFace(this.sg, this);
        this.skyLightLevel = 15;
        this.skyColor = new float[]{0.52734375F, 0.8046875F, 0.91796875F};
        this.defaultSkyColor = new float[]{0.52734375F, 0.8046875F, 0.91796875F};
        this.skyLightColor = new float[]{1, 1, 1, 0}; //ANY COLOR CANNOT BE 0
        this.worldFolder = new File(this.sg.save.saveFolder + "/worlds/worldEarth");
        if(!this.worldFolder.exists()){
            this.worldFolder.mkdirs();
        }
        World.totalMaps = 14;
    }

    @Override
    public void initNoiseMaps() {
        World.worldLoadPhase = 0;
        World.noiseMapsCompleted = 0;
        this.treeNoise = new NoiseMap2D(255, 255, 2, 3, 1, 2, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.berryNoise = new NoiseMap2D(317, 317, 1, 1, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.dirtNoise = new NoiseMap2D(32, 32, 3, 2, 1, 2, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.terrainNoise = new NoiseMap3D(256, 256, 256, 16, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.sampleNoise = new NoiseMap2D(256, 256, 6, 7, 1, 12, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.continentalNoise = new NoiseMap2D(256, 256, 3, 1, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.secondaryTerrainNoise = new NoiseMap3D(345, 256, 345, 16, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.secondaryContinentalNoise = new NoiseMap2D(631, 631, 3, 1, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.scaleNoise = new NoiseMap2D(128, 128, 16, 0.011, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.secondaryScaleNoise = new NoiseMap2D(311, 311, 4, 0.011, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.temperatureNoise1 = new NoiseMap2D(715, 715, 1, 0.5, 1, 0.5, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.temperatureNoise2 = new NoiseMap2D(1379, 1379, 1, 0.5, 1, 0.5, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.rainfallNoise1 = new NoiseMap2D(937, 937, 1, 0.5, 1, 0.5, this.sg.save.seed);
        World.noiseMapsCompleted++;
        this.rainfallNoise2 = new NoiseMap2D(1579, 1579, 1, 0.5, 1, 0.5, this.sg.save.seed);
        World.noiseMapsCompleted++;
        World.worldLoadPhase = 1;
    }

    @Override
    public void saveWorld() {
        this.activeWorldFace.chunkController.saveAllRegions();
    }

    @Override
    public void loadWorld() {

    }

    @Override
    public void saveWorldWithoutUnload(){
        this.activeWorldFace.chunkController.saveAllRegionsWithoutUnload();
    }

    public double getRainfall(int x, int z){
        return ((this.rainfallNoise1.getNoiseRaw(x,z) + this.rainfallNoise2.getNoiseRaw(x,z)) * 0.5);
    }

    public double getTemperature(int x, int y, int z){
        //This will have to be modulated by the vertical distance when above sea level and based on the distance north/south of the equator, it should be subtle enough that the player does not notice it
        return ((this.temperatureNoise1.getNoiseRaw(x,z) + this.temperatureNoise2.getNoiseRaw(x,z)) * 0.5);
    }

}
