package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockGrass;
import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;
import spacegame.util.MathUtil;
import spacegame.world.worldtypes.earthlike.WorldEarth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public final class WorldGenBerryBush extends WorldGen{
    public HashSet<Chunk> touchedChunks = new HashSet<>();
    public HashMap<Long, Chunk> chunkCache = new HashMap<>();

    public WorldGenBerryBush(Chunk chunk, WorldEarth worldEarth, int index) {
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

    private boolean isBlockValid(int x, int y, int z){
        short lowerBlockID;
        if(this.isBlockInCallingChunk(x,y,z)){
            lowerBlockID = this.chunk.getBlockID(x,y,z);
        } else {
            lowerBlockID = this.worldEarth.getBlockID(x,y,z);
        }

        short upperBlockID;
        if(this.isBlockInCallingChunk(x,y + 1,z)){
            upperBlockID = this.chunk.getBlockID(x, y + 1, z);
        } else {
            upperBlockID = this.worldEarth.getBlockID(x,y + 1,z);
        }

        return Block.list[lowerBlockID] instanceof BlockGrass && upperBlockID == Block.air.ID;
    }

    private boolean doesBlockIntersectSphere(int x, int y, int z, int startX, int startY, int startZ, int radiusSq){
        return MathUtil.distance3DSquared(x,y,z,startX,startY,startZ) <= radiusSq;
    }

    @Override
    public void generate() {

        int x = this.chunk.getBlockXFromIndex(this.index);
        int y = this.chunk.getBlockYFromIndex(this.index);
        int z = this.chunk.getBlockZFromIndex(this.index);
        int radius = this.rand.nextInt(4, 9);
        final int xStart = x;
        final int yStart = y;
        final int zStart = z;
        final int boxStartX = x - radius;
        final int boxStartY = y - radius;
        final int boxStartZ = z - radius;
        final int boxEndX = x + radius;
        final int boxEndY = y + radius;
        final int boxEndZ = z + radius;

        boolean canGenerate = false;
        Chunk chunk;

        for(x = boxStartX; x <= boxEndX; x++){
            for(y = boxStartY; y <= boxEndY; y++){
                for(z = boxStartZ; z <= boxEndZ; z++){
                    canGenerate = ((x * 734287L + y * 912931L + z * 4217L + this.seed) & 7) == 0;
                    if(this.doesBlockIntersectSphere(x,y,z, xStart, yStart, zStart, radius * radius) && this.isBlockValid(x,y,z) && canGenerate){
                        if(this.isBlockInCallingChunkExcludeEdge(x, y + 1, z)){
                            this.chunk.setBlock(x, y + 1, z, Block.berryBush.ID);
                        } else {
                            this.worldEarth.setBlock(x, y + 1, z, Block.berryBush.ID);
                        }

                        int chunkX = x >> 5;
                        int chunkY = y >> 5;
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
            }
        }


        for(Chunk rebuildChunk : this.touchedChunks){
            this.addChunkToRebuildQueue(rebuildChunk);
        }

        markAllChunksInRebuildQueueDirty();
    }
}
