package spacegame.world;

import java.util.HashSet;
import java.util.Random;

public abstract class WorldGen {
    public Chunk chunk;
    public int index;
    public Random rand;
    public Long seed;
    public WorldEarth worldEarth;
    public static HashSet<Chunk> chunksToRebuild = new HashSet<>();
    protected int chunkMinX;
    protected int chunkMinY;
    protected int chunkMinZ;
    protected int chunkMaxX;
    protected int chunkMaxY;
    protected int chunkMaxZ;

    public abstract void startGenerate();

    public abstract void generate();


    public void addChunkToRebuildQueue(Chunk chunk) {
        if (chunk != null) {
            chunksToRebuild.add(chunk);
        }
    }


    public static void markAllChunksInRebuildQueueDirty() {
        for (Chunk c : chunksToRebuild) {
            c.markDirty();
        }
        chunksToRebuild.clear();
    }

    protected boolean isBlockInCallingChunkExcludeEdge(int x, int y, int z){
        return x > this.chunkMinX && x < this.chunkMaxX && y > this.chunkMinY && y < this.chunkMaxY && z > this.chunkMinZ && z < this.chunkMaxZ;
    }

    protected boolean isBlockInCallingChunk(int x, int y, int z){
        return x >= this.chunkMinX && x <= this.chunkMaxX && y >= this.chunkMinY && y <= this.chunkMaxY && z >= this.chunkMinZ && z <= this.chunkMaxZ;
    }

}
