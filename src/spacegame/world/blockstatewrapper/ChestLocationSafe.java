package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.ChestLocation;

public final class ChestLocationSafe {
    public volatile ChestLocation value;

    public ChestLocationSafe(ChestLocation value){
        this.value = value;
    }
}
