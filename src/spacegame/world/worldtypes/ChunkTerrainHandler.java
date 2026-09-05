package spacegame.world.worldtypes;

import spacegame.world.*;

public abstract class ChunkTerrainHandler {
    public ChunkController controller;
    public double solidNoiseThreshold = 0;
    public GeologicRegistry geologicRegistry;
    public GeologicProvince[] geologicProvinces;
    public World world;


    public ChunkTerrainHandler(ChunkController chunkController, World world){
        this.controller = chunkController;
        this.world = world;
    }


    public abstract void setTerrain(short[] blocks, Chunk chunk);


    public abstract double getTerrainNoise(int x, int y, int z);


    public abstract void populateChunk(Chunk chunk);

    protected abstract GeologicProvince getGeologicProvince(int x, int y, int z);

    protected abstract short getStoneType(int x, int y, int z);

    protected abstract short getSandType(int x, int y, int z);

    protected abstract short getGravelType(int x, int y, int z);
}
