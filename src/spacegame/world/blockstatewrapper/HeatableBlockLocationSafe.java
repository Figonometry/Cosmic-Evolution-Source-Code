package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.HeatableBlockLocation;

public class HeatableBlockLocationSafe {
    public volatile HeatableBlockLocation value;

    public HeatableBlockLocationSafe(HeatableBlockLocation value){
        this.value = value;
    }
}
