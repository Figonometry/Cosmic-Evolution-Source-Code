package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.TilledSoilState;

public final class TilledSoilStateSafe {
    public volatile TilledSoilState value;

    public TilledSoilStateSafe(TilledSoilState value){
        this.value = value;
    }
}
