package spacegame.world;

public final class ChunkJob implements Runnable, Comparable<ChunkJob> {
    public final float priority;
    public final Runnable task;

    public ChunkJob(float priority, Runnable task){
        this.priority = priority;
        this.task = task;
    }
    @Override
    public int compareTo(ChunkJob o) {
        return Float.compare(this.priority, o.priority); //Distance from player in 3d
    }

    @Override
    public void run() {
        this.task.run();
    }
}
