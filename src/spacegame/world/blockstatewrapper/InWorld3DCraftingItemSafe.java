package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.InWorld3DCraftingItem;

public class InWorld3DCraftingItemSafe {
    public volatile InWorld3DCraftingItem value;

    public InWorld3DCraftingItemSafe(InWorld3DCraftingItem value){
        this.value = value;
    }
}
