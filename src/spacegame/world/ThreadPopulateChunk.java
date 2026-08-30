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
        this.world.chunkController.chunkEarthTerrainHandler.populateChunk(this.chunk);

        synchronized (this.world.chunkController.lightingUpdateChunks){ //Once populated immediatly pass to the lighting thread
            this.chunk.dirtyLighting = true;
            this.world.chunkController.lightingUpdateChunks.add(this.chunk);
        }

        CosmicEvolution.threadJobs.decrementAndGet();
    }
}
