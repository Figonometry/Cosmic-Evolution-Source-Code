package spacegame.world;

public final class ChunkRegion {
    public final int x;
    public final int y;
    public final int z;
    public Chunk[] chunks = new Chunk[512]; //Chunks are 8x8x8 in the same order as blocks in a chunk, x then z, then y

    public ChunkRegion(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public void addChunk(Chunk chunk) {
        this.chunks[getRegionChunkIndex(chunk.x, chunk.y, chunk.z)] = chunk;
    }

    public void removeChunk(Chunk chunk) {
        this.chunks[getRegionChunkIndex(chunk.x, chunk.y, chunk.z)] = null;
    }

    public Chunk getChunk(int x, int y, int z){
        return this.chunks[getRegionChunkIndex(x,y,z)];
    }

    public static int getRegionChunkIndex(int x, int y, int z) {
        return ((x & 7) + ((y & 7) << 6) + ((z & 7) << 3));
    }

    public static long regionKey(int rx, int ry, int rz) {
        long key = 0L;
        key |= ((long)(rx & 0x1FFFFF)) << 42;
        key |= ((long)(ry & 0x1FFFFF)) << 21;
        key |= ((long)(rz & 0x1FFFFF));
        return key;
    }



    public boolean isEmpty() {
        Chunk chunk;
        for(int i = 0; i < this.chunks.length; i++){
            chunk = this.chunks[i];
            if(chunk != null){
                return false;
            }
        }
        return true;
    }

}
