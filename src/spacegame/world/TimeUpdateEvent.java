package spacegame.world;

public final class TimeUpdateEvent {
    public int index;
    public long updateTime;

    public TimeUpdateEvent(int index, long updateTime){
        this.index = index;
        this.updateTime = updateTime;
    }


}
