package spacegame.world;

public final class TimeUpdateEvent {
    public short index;
    public long updateTime;

    public TimeUpdateEvent(short index, long updateTime){
        this.index = index;
        this.updateTime = updateTime;
    }


}
