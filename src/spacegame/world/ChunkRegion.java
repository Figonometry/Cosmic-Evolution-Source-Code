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

    public static int getRegionChunkIndex(int x, int y, int z) {
        if (x < 0) {
            x *= -1;
        }

        if (y < 0) {
            y *= -1;
        }

        if (z < 0) {
            z *= -1;
        }

        x %= 8;
        y %= 8;
        z %= 8;

        return (x + (y << 6) + (z << 3));
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
