package spacegame.core;

import spacegame.render.Shader;

public final class Timer {
    private static final long NS_PER_SECOND = 1000000000L;
    private static final long MAX_NS_PER_UPDATE = 1000000000L;
    private static final int MAX_TICKS_PER_UPDATE = 100;
    private float ticksPerSecond;
    public static long elapsedTime = 0L;
    public static long elapseFrames = 0L;
    public static final long GAME_DAY = 216000;
    public static final long GAME_HOUR = GAME_DAY / 24;
    public static final long GAME_MINUTE = GAME_HOUR / 60;
    public static final long REAL_SECOND = 60;
    public static final long REAL_MINUTE = REAL_SECOND * 60;
    public static final long REAL_HOUR = REAL_MINUTE * 60;
    public static final long REAL_DAY  = REAL_HOUR * 24;
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
        if(CosmicEvolution.instance.save != null){
            if(!CosmicEvolution.instance.save.activeWorld.paused){
                CosmicEvolution.instance.save.time += this.ticks;
            }
        }
        if (this.ticks > 100) {
            this.ticks = 100;
        }

        this.passedTime -= (float) this.ticks;
        this.a = this.passedTime;


        Shader.terrainShader.uploadDouble("time", (double) Timer.elapsedTime % 8388608);
    }


}
