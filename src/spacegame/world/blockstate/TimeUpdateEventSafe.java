package spacegame.world.blockstate;

public final class TimeUpdateEventSafe {
    public volatile TimeUpdateEvent value;

    public TimeUpdateEventSafe(TimeUpdateEvent value){
        this.value = value;
    }
}
