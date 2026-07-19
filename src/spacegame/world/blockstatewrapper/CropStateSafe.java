package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.CropState;

public final class CropStateSafe {
    public volatile CropState value;

    public CropStateSafe(CropState value){
        this.value = value;
    }
}
