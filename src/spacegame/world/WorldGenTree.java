package spacegame.world;

import spacegame.block.Block;
import spacegame.block.BlockGrass;
import spacegame.core.CosmicEvolution;
import spacegame.util.LongHasher;
import spacegame.world.blockstate.LogState;
import spacegame.world.blockstate.MultiState;
import spacegame.world.worldtypes.earthlike.WorldEarth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class WorldGenTree extends WorldGen {
    public HashSet<Chunk> touchedChunks = new HashSet<>();
    public HashMap<Long, Chunk> chunkCache = new HashMap<>();
    public boolean decayIntoDirt;
    public boolean fromWorldGen;
    public int height;
    public int size;
    public int startingX;
    public int startingY;
    public int startingZ;
    public int trunkTop;

    public WorldGenTree(Chunk chunk, WorldEarth worldEarth, int index, boolean fromWorldGen) {
        if(!(Block.list[chunk.getBlockID(index)] instanceof BlockGrass))return; //Guard clause to prevent trees from generating when they shouldnt
        this.worldEarth = worldEarth;
        this.index = index;
        this.chunk = chunk;
        this.fromWorldGen = fromWorldGen;

        this.chunkMinX = this.chunk.x << 5;
        this.chunkMinY = this.chunk.y << 5;
        this.chunkMinZ = this.chunk.z << 5;
        this.chunkMaxX = (this.chunk.x << 5) + 31;
        this.chunkMaxY = (this.chunk.y << 5) + 31;
        this.chunkMaxZ = (this.chunk.z << 5) + 31;

        this.seed = new LongHasher().hash(CosmicEvolution.instance.save.seed, String.valueOf(chunk.x + chunk.y + chunk.z + index));

        this.rand = new Random(this.seed);
        this.startGenerate();
    }

    public WorldGenTree(Chunk chunk, WorldEarth worldEarth, int index) {
        if(!(Block.list[chunk.getBlockID(index)] instanceof BlockGrass))return; //Guard clause to prevent trees from generating when they shouldnt
        this.worldEarth = worldEarth;
        this.index = index;
        this.chunk = chunk;
        this.rand = new Random();
        this.startGenerate();
    }



    @Override
    public void startGenerate() {
        this.startingX = this.chunk.getBlockXFromIndex(index);
        this.startingY = this.chunk.getBlockYFromIndex(index) + 1;
        this.startingZ = this.chunk.getBlockZFromIndex(index);
        this.height = this.rand.nextInt(6, 11);
        this.size = this.rand.nextInt(13, 17);
        this.generate();
    }


    @Override
    public void generate(){

        int x = this.startingX;
        int y = this.startingY;
        int z = this.startingZ;

        if(!this.verifyTrunk(x,y,z))return;
        if(!this.verifyLeaves(x, this.trunkTop - 2, z))return;
        this.generateTrunk();
        this.generateLeaves(x, this.trunkTop - 2, z);

        int numberOfSticks = rand.nextInt(4);
         x = this.startingX;
         z = this.startingZ;
        boolean negative;
        for(int i = 0; i < numberOfSticks; i++){
            if(!this.fromWorldGen)break;
            negative = rand.nextBoolean();
            x = x + (negative ? rand.nextInt(-7, -1) : rand.nextInt(1, 7));
            negative = rand.nextBoolean();
            z = z + (negative ? rand.nextInt(-7, -1) : rand.nextInt(1, 7));
            y = this.worldEarth.chunkController.findChunkSkyLightMap(x >> 5, z >> 5).getHeightValue(x,z);

            if(Block.list[this.worldEarth.getBlockID(x,y,z)] instanceof BlockGrass && this.worldEarth.getBlockID(x, y + 1, z) == Block.air.ID){
                this.worldEarth.setBlockAndNotify(x, y + 1, z, Block.itemStick.ID, false);
            }
        }

        for(Chunk rebuildChunk : this.touchedChunks){
            this.addChunkToRebuildQueue(rebuildChunk);
        }

        markAllChunksInRebuildQueueDirty();
    }


    private boolean verifyTrunk(int x, int y, int z){
        for(int i = 0; i < this.height; i++){
            if(!this.canBlockGenerate(x,y,z))return false;
            y++;
        }
        this.trunkTop = y;

        return true;
    }

    private boolean verifyLeaves(int x, int y, int z){
        int radius = this.height/2;
        int radiusSq = radius * radius;
        final int xStart = x;
        final int yStart = y;
        final int zStart = z;
        final int boxStartX = x - radius;
        final int boxStartY = y - radius;
        final int boxStartZ = z - radius;
        final int boxEndX = x + radius;
        final int boxEndY = y + radius;
        final int boxEndZ = z + radius;

        this.rand = new Random(this.seed);

        for(x = boxStartX; x <= boxEndX; x++){
            for(y = boxStartY; y <= boxEndY; y++){
                for(z = boxStartZ; z <= boxEndZ; z++){
                    if(this.doesBlockIntersectHemisphere(x,y,z, xStart, yStart, zStart, radiusSq, this.seed) && !this.canBlockGenerate(x,y,z)){
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void generateTrunk(){
        int x = startingX;
        int y = startingY;
        int z = startingZ;
        if(this.size == 16){
            this.decayIntoDirt = true;
        }
        Chunk chunk;
        boolean decreaseLogSize = false;
        boolean inCallingChunk;
        for(int i = 0; i < this.height; i++){
            if(decreaseLogSize){
                this.size--;
                decreaseLogSize = false;
            } else {
                decreaseLogSize = true;
            }

            inCallingChunk = this.isBlockInCallingChunkExcludeEdge(x,y,z);

            if(inCallingChunk){
                this.chunk.setBlock(x,y,z, Block.oakLog.ID);
                this.chunk.addBlockState(x,y,z, MultiState.LOG_STATE, new LogState(LogState.FACE_DIRECTION_TOP_AND_BOTTOM, this.size, Chunk.getBlockIndexFromCoordinates(x,y,z)));
            } else {
                this.worldEarth.setBlock(x,y,z, Block.oakLog.ID);
                this.worldEarth.addBlockState(x,y,z, MultiState.LOG_STATE, new LogState(LogState.FACE_DIRECTION_TOP_AND_BOTTOM, this.size, Chunk.getBlockIndexFromCoordinates(x,y,z)));
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

            y++;
        }
    }

    private void generateLeaves(int x, int y, int z){
        int radius = height/2;
        int radiusSq = radius * radius;
        final int xStart = x;
        final int yStart = y;
        final int zStart = z;
        final int boxStartX = x - radius;
        final int boxStartY = y - radius;
        final int boxStartZ = z - radius;
        final int boxEndX = x + radius;
        final int boxEndY = y + radius;
        final int boxEndZ = z + radius;
        boolean inCallingChunk = false;
        Chunk chunk;

        this.rand = new Random(this.seed);

        for(x = boxStartX; x <= boxEndX; x++){
            for(y = boxStartY; y <= boxEndY; y++){
                for(z = boxStartZ; z <= boxEndZ; z++){
                    if(this.doesBlockIntersectHemisphere(x,y,z, xStart, yStart, zStart, radiusSq, this.seed)){
                        inCallingChunk = this.isBlockInCallingChunkExcludeEdge(x,y,z);
                        if(inCallingChunk){
                            this.chunk.setBlock(x,y,z, Block.leaf.ID);
                        } else {
                            this.worldEarth.setBlock(x,y,z, Block.leaf.ID);
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





    private boolean doesBlockIntersectHemisphere(
            int x, int y, int z,
            int startX, int startY, int startZ,
            int radiusSq,
            long seed)
    {
        // Compute squared distance (integer math, no double)
        int dx = x - startX;
        int dy = y - startY;
        int dz = z - startZ;
        int distSq = dx*dx + dy*dy + dz*dz;

        // --- Bottom ring randomness ---
        // Equivalent to rand.nextInt(10) == 0 but MUCH faster.
        // Tunable: &7 = 1/8 chance, &15 = 1/16 chance, etc.
        boolean bottomRandom = ((dx * 734287 + dy * 912931 + dz * 4217 + (int)seed) & 7) == 0;

        // y == startY - 1 → bottom ring
        if (y == startY - 1) {
            return bottomRandom && distSq <= radiusSq;
        }

        // y < startY - 1 → below canopy entirely
        if (y < startY - 1) {
            return false;
        }

        // Main hemisphere
        return distSq <= radiusSq;
    }


    private boolean canBlockGenerate(int x, int y, int z){
        short blockID = this.isBlockInCallingChunk(x,y,z) ? this.chunk.getBlockID(x,y,z) : this.worldEarth.getBlockID(x,y,z);
        return blockID == Block.air.ID || blockID == Block.oakLog.ID || blockID == Block.leaf.ID;
    }

}
