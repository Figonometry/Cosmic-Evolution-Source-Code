package spacegame.world.threads;

import spacegame.core.CosmicEvolution;
import spacegame.world.Chunk;
import spacegame.world.worldtypes.World;

public final class ThreadPopulateChunk implements Runnable {
    private Chunk chunk;
    private World world;

    public ThreadPopulateChunk(Chunk chunk, World world){
        this.chunk = chunk;
        this.world = world;
    }
    @Override
    public void run() {
        try {
            this.world.chunkTerrainHandler.populateChunk(this.chunk);

            synchronized (this.world.chunkController.lightingUpdateChunks) { //Once isPopulated immediatly pass to the lighting thread
                this.chunk.hasDirtyLighting = true;
                this.world.chunkController.lightingUpdateChunks.add(this.chunk);
            }
        } catch (Exception e) {
           e.printStackTrace();
        } finally {
            CosmicEvolution.threadJobs.decrementAndGet();
        }
    }
}
