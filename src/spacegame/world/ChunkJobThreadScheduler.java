package spacegame.world;

import spacegame.core.CosmicEvolution;
import spacegame.render.ThreadRebuildChunk;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public final class ChunkJobThreadScheduler implements Runnable {
    public static ConcurrentLinkedQueue<ChunkJob> chunkJobQueue = new ConcurrentLinkedQueue<>();
    private static final int MAX_CONCURRENT_REBUILDS = 5;
    public static final AtomicInteger activeRebuilds = new AtomicInteger(0);

    //Throughput will be about 100 tasks launched in a second with a cap on concurrent rebuilding threads
    @Override
    public void run() {
        while(CosmicEvolution.instance.running){

            ChunkJob chunkJob = chunkJobQueue.poll();
            if(chunkJob == null)continue;

            if(chunkJob.task instanceof ThreadRebuildChunk){
                if(activeRebuilds.get() >= MAX_CONCURRENT_REBUILDS) {
                    chunkJobQueue.add(chunkJob);
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    continue;
                }
                activeRebuilds.incrementAndGet();
            }

            CosmicEvolution.addJobToThreadPool(chunkJob);

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
