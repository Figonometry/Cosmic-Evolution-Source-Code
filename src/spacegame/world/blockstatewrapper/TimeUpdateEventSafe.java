package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.TimeUpdateEvent;

public final class TimeUpdateEventSafe {
    public volatile TimeUpdateEvent value;

    public TimeUpdateEventSafe(TimeUpdateEvent value){
        this.value = value;
    }
}
