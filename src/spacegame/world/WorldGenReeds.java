package spacegame.world;

import spacegame.block.Block;
import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;
import spacegame.util.MathUtil;
import spacegame.world.worldtypes.earthlike.WorldEarth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public final class WorldGenReeds extends WorldGen {
    public HashSet<Chunk> touchedChunks = new HashSet<>();
    public HashMap<Long, Chunk> chunkCache = new HashMap<>();
    public int radius;
    public int startX;
    public int startY;
    public int startZ;
    public WorldGenReeds(Chunk chunk, WorldEarth worldEarth, int index){
        if(chunk.blocks[index] != Block.water.ID)return;

        int x = chunk.getBlockXFromIndex(index);
        int y = chunk.getBlockYFromIndex(index);
        int z = chunk.getBlockZFromIndex(index);

        if(worldEarth.getBlockID(x, y + 1, z) != Block.air.ID || !Block.list[worldEarth.getBlockID(x, y - 1, z)].isSolid)return;


        this.worldEarth = worldEarth;
        this.index = index;
        this.chunk = chunk;
        this.seed = new LongHasher().hash(CosmicEvolution.instance.save.seed, String.valueOf(chunk.x + chunk.y + chunk.z + index));
        this.rand = new Random(this.seed);
        this.radius = this.rand.nextInt(3,6);
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



    private boolean isBlockSuitableToGenerateReeds(int x, int y, int z){
       if(this.isBlockInCallingChunk(x,y,z) && this.isBlockInCallingChunk(x, y + 1, z)){
           return this.chunk.blocks[Chunk.getBlockIndexFromCoordinates(x,y,z)] == Block.water.ID && this.chunk.blocks[Chunk.getBlockIndexFromCoordinates(x, y + 1, z)] == Block.air.ID && Block.list[this.chunk.blocks[Chunk.getBlockIndexFromCoordinates(x,y,z)]].isSolid;
       } else {
           return worldEarth.getBlockID(x, y, z) == Block.water.ID && worldEarth.getBlockID(x, y + 1, z) == Block.air.ID && Block.list[worldEarth.getBlockID(x, y - 1, z)].isSolid;
       }
    }


    private boolean doesBlockIntersectSphere(int x, int y, int z, int startX, int startY, int startZ, int radiusSq){
        return MathUtil.distance3DSquared(x,y,z,startX,startY,startZ) <= radiusSq;
    }

    @Override
    public void generate() {
        final int xStart = this.startX;
        final int yStart = this.startY;
        final int zStart = this.startZ;
        final int boxStartX = this.startX - radius;
        final int boxStartY = this.startY - radius;
        final int boxStartZ = this.startZ - radius;
        final int boxEndX = this.startX + radius;
        final int boxEndY = this.startY + radius;
        final int boxEndZ = this.startZ + radius;

        Chunk chunk;

        for(int x = boxStartX; x <= boxEndX; x++){
            for(int y = boxStartY; y <= boxEndY; y++){
                for(int z = boxStartZ; z <= boxEndZ; z++){
                    if(this.doesBlockIntersectSphere(x,y,z, xStart, yStart, zStart, this.radius * this.radius) && this.rand.nextInt(4) == 0 && this.isBlockSuitableToGenerateReeds(x,y,z)){
                        if(this.isBlockInCallingChunk(x,y,z)){
                            this.chunk.blocks[Chunk.getBlockIndexFromCoordinates(x,y,z)] = Block.reedLower.ID;
                        } else {
                            this.worldEarth.setBlock(x,y,z, Block.reedLower.ID);
                        }


                        if(this.isBlockInCallingChunk(x, y + 1, z)){
                            this.chunk.blocks[Chunk.getBlockIndexFromCoordinates(x,y + 1,z)] = Block.reedUpper.ID;
                        } else {
                            this.worldEarth.setBlock(x,y + 1,z, Block.reedUpper.ID);
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

                        chunkX = x >> 5;
                        chunkY = y + 1 >> 5;
                        chunkZ = z >> 5;

                         key = (((long)chunkX) << 42) ^ (((long)chunkY) << 21) ^ (long)chunkZ;

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
