package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockGrass;
import spacegame.block.BlockSoil;
import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;
import spacegame.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public final class WorldGenClayBlob extends WorldGen {
    public HashSet<Chunk> touchedChunks = new HashSet<>();
    public HashMap<Long, Chunk> chunkCache = new HashMap<>();
    private int startX;
    private int startY;
    private int startZ;
    private int radius;

    public WorldGenClayBlob(Chunk chunk, WorldEarth earth, int index){
        if(!(Block.list[chunk.blocks[index]] instanceof BlockGrass))return;
        this.worldEarth = earth;
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
        this.startX = this.chunk.getBlockXFromIndex(index);
        this.startY = this.chunk.getBlockYFromIndex(index);
        this.startZ = this.chunk.getBlockZFromIndex(index);
        this.radius = this.rand.nextInt(3, 6);
        this.generate();
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

        for(int x = boxStartX; x <= boxEndX; x++){
            for(int y = boxStartY; y <= boxEndY; y++){
                for(int z = boxStartZ; z <= boxEndZ; z++){
                    if(this.doesBlockIntersectSphere(x,y,z, xStart, yStart, zStart, this.radius * this.radius)){
                        if(Block.list[this.worldEarth.getBlockID(x,y,z)] instanceof BlockGrass){
                            if(this.isBlockInCallingChunkExcludeEdge(x,y,z)){
                                this.chunk.blocks[Chunk.getBlockIndexFromCoordinates(x,y,z)] = Block.clayWithGrassLargePatches.ID; //Replace with clay replacement logic
                            } else {
                                this.worldEarth.setBlock(x,y,z, Block.clayWithGrassLargePatches.ID);
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

                        } else if(Block.list[this.worldEarth.getBlockID(x,y,z)] instanceof BlockSoil){
                            if(this.isBlockInCallingChunkExcludeEdge(x,y,z)){
                                this.chunk.blocks[Chunk.getBlockIndexFromCoordinates(x,y,z)] = Block.clay.ID;
                            } else {
                                this.worldEarth.setBlock(x,y,z, Block.clay.ID);
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
        }


        for(Chunk rebuildChunk : this.touchedChunks){
            this.addChunkToRebuildQueue(rebuildChunk);
        }

        markAllChunksInRebuildQueueDirty();
    }


}

