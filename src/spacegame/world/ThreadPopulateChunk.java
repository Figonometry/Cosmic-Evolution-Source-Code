package spacegame.world;

import spacegame.core.CosmicEvolution;

public final class ThreadPopulateChunk implements Runnable {
    private Chunk chunk;
    private World world;

    public ThreadPopulateChunk(Chunk chunk, World world){
        this.chunk = chunk;
        this.world = world;
    }
    @Override
    public void run() {
        this.world.chunkController.chunkTerrainHandler.populateChunk(this.chunk);
        CosmicEvolution.threadJobs.decrementAndGet();
    }
}
