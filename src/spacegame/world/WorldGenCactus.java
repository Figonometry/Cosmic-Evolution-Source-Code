package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockSand;
import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;
import spacegame.world.worldtypes.earthlike.WorldEarth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public final class WorldGenCactus extends WorldGen{
    public HashSet<Chunk> touchedChunks = new HashSet<>();
    public HashMap<Long, Chunk> chunkCache = new HashMap<>();
    public WorldGenCactus(Chunk chunk, WorldEarth worldEarth, int index){
        if(!(Block.list[chunk.getBlockID(index)] instanceof BlockSand))return;
        this.worldEarth = worldEarth;
        this.index = index;
        this.chunk = chunk;
        this.seed = new LongHasher().hash(CosmicEvolution.instance.save.seed, String.valueOf(chunk.x + chunk.y + chunk.z + index));
        this.rand = new Random(this.seed);
        this.chunkMinX = this.chunk.x << 5;
        this.chunkMinY = this.chunk.y << 5;
        this.chunkMinZ = this.chunk.z << 5;
        this.chunkMaxX = (this.chunk.x << 5) + 31;
        this.chunkMaxY = (this.chunk.y << 5) + 31;
        this.chunkMaxZ = (this.chunk.z << 5) + 31;
        this.startGenerate();
    }



    @Override
    public void startGenerate() {
        this.generate();
    }

    @Override
    public void generate() {
        int x = this.chunk.getBlockXFromIndex(this.index);
        int y = this.chunk.getBlockYFromIndex(this.index);
        int z = this.chunk.getBlockZFromIndex(this.index);
        int height = this.rand.nextInt(1,6);
        Chunk chunk;
        for(int i = 0; i < height; i++){
            if(this.isBlockValid(x, y + i, z)){
                if(this.isBlockInCallingChunkExcludeEdge(x, y + i, z)){
                    this.chunk.setBlock(x, y + i, z, Block.cactus.ID);
                } else {
                    this.worldEarth.setBlock(x,y,z, Block.cactus.ID);
                }

                int chunkX = x >> 5;
                int chunkY = y + i >> 5;
                int chunkZ = z >> 5;

                long key = (((long)chunkX) << 42) ^ (((long)chunkY) << 21) ^ (long)chunkZ;

                chunk = this.chunkCache.get(key);
                if(chunk == null){
                    chunk = this.worldEarth.findChunkFromChunkCoordinates(chunkX, chunkY, chunkZ);
                    this.chunkCache.put(key, chunk);
                }

                this.touchedChunks.add(chunk);
                chunk.firstRender = true;
            }
        }

        for(Chunk rebuildChunk : this.touchedChunks){
            this.addChunkToRebuildQueue(rebuildChunk);
        }

        markAllChunksInRebuildQueueDirty();
    }

    private boolean isBlockValid(int x, int y, int z){
        if(this.isBlockInCallingChunk(x,y,z)){
            return this.chunk.getBlockID(x,y,z) == Block.air.ID;
        } else {
            return this.worldEarth.getBlockID(x, y, z) == Block.air.ID;
        }
    }

}
