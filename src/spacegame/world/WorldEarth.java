package spacegame.world;


import spacegame.core.SpaceGame;

import java.io.File;

public final class WorldEarth extends World {
    public static NoiseMap treeNoise;
    public static NoiseMap berryNoise;
    public static NoiseMap3D terrainNoise;
    public static NoiseMap3D secondaryTerrainNoise;
    public static NoiseMap dirtNoise;
    public static NoiseMap sampleNoise;
    public static NoiseMap continentalNoise;
    public static NoiseMap secondaryContinentalNoise;
    public static NoiseMap scaleNoise;
    public static NoiseMap secondaryScaleNoise;

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
        World.totalMaps = 10;
    }

    @Override
    public void initNoiseMaps() {
        World.worldLoadPhase = 0;
        World.noiseMapsCompleted = 0;
        treeNoise = new NoiseMap(255, 255, 2, 3, 1, 2, this.sg.save.seed);
        World.noiseMapsCompleted++;
        berryNoise = new NoiseMap(317, 317, 1, 1, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        dirtNoise = new NoiseMap(32, 32, 3, 2, 1, 2, this.sg.save.seed);
        World.noiseMapsCompleted++;
        terrainNoise = new NoiseMap3D(256, 256, 256, 16, this.sg.save.seed);
        World.noiseMapsCompleted++;
        sampleNoise = new NoiseMap(256, 256, 6, 7, 1, 12, this.sg.save.seed);
        World.noiseMapsCompleted++;
        continentalNoise = new NoiseMap(256, 256, 3, 1, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        secondaryTerrainNoise = new NoiseMap3D(345, 256, 345, 16, this.sg.save.seed);
        World.noiseMapsCompleted++;
        secondaryContinentalNoise = new NoiseMap(631, 631, 3, 1, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        scaleNoise = new NoiseMap(128, 128, 16, 0.011, 1, 0, this.sg.save.seed);
        World.noiseMapsCompleted++;
        secondaryScaleNoise = new NoiseMap(311, 311, 4, 0.011, 1, 0, this.sg.save.seed);
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

}
