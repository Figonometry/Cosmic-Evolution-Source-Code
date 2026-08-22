package spacegame.world.blockstate;

import spacegame.core.CosmicEvolution;
import spacegame.world.Chunk;

public final class DoorTransition extends BlockState {
    public int x;
    public int y;
    public int z;
    public boolean startedOpen;
    public boolean startedClosed;
    public boolean hingeLeft;
    public boolean hingeRight;
    public long timeStarted;
    public long completeTime;
    public static final long timeToComplete = 60L;

    public DoorTransition(int x, int y, int z, long timeStarted, boolean startedOpen, boolean startedClosed, boolean hingeLeft, boolean hingeRight){
        this.x = x;
        this.y = y;
        this.z = z;
        this.timeStarted = timeStarted;
        this.startedOpen = startedOpen;
        this.startedClosed = startedClosed;
        this.hingeLeft = hingeLeft;
        this.hingeRight = hingeRight;

        this.completeTime = this.timeStarted + timeToComplete;
    }


    @Override
    public void onTick(Chunk callingChunk){
        if(this.completeTime <= CosmicEvolution.instance.save.time){
            callingChunk.removeBlockState(this.x, this.y, this.z, MultiState.DOOR_TRANSITION_STATE);
            callingChunk.markDirty();
        }
    }
}
