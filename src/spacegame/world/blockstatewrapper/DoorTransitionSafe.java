package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.DoorTransition;

public final class DoorTransitionSafe {
    public volatile DoorTransition value;

    public DoorTransitionSafe(DoorTransition value){
        this.value = value;
    }
}
