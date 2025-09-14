package spacegame.core;

public final class Timer {
    private static final long NS_PER_SECOND = 1000000000L;
    private static final long MAX_NS_PER_UPDATE = 1000000000L;
    private static final int MAX_TICKS_PER_UPDATE = 100;
    private float ticksPerSecond;
    public static long elapsedTime = 0L;
    public static long elapseFrames = 0L;
    private long lastTime;
    public int ticks;
    public float a;
    public float timeScale = 1.0F;
    public float passedTime = 0.0F;

    protected Timer(float ticksPerSecond) {
        this.ticksPerSecond = ticksPerSecond;
        this.lastTime = System.nanoTime();
    }

    protected void advanceTime() {
        long now = System.nanoTime();
        long passedNs = now - this.lastTime;
        this.lastTime = now;
        if (passedNs < 0L) {
            passedNs = 0L;
        }

        if (passedNs > 1000000000L) {
            passedNs = 1000000000L;
        }

        this.passedTime += (float) passedNs * this.timeScale * this.ticksPerSecond / 1.0E9F;
        this.ticks = (int) this.passedTime;
        elapsedTime += this.ticks;
        if(SpaceGame.instance.save != null){
            if(!SpaceGame.instance.save.activeWorld.paused){
                SpaceGame.instance.save.time += this.ticks;
            }
        }
        if (this.ticks > 100) {
            this.ticks = 100;
        }

        this.passedTime -= (float) this.ticks;
        this.a = this.passedTime;
    }


}
