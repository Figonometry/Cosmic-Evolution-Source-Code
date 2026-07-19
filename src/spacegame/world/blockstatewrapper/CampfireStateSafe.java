package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.CampfireState;

public final class CampfireStateSafe {
    public volatile CampfireState value;

    public CampfireStateSafe(CampfireState value){
        this.value = value;
    }
}
