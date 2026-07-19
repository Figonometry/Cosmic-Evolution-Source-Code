package spacegame.world.blockstatewrapper;

import spacegame.world.blockstate.InWorldCraftingItem;

public final class InWorldCraftingItemSafe {
    public volatile InWorldCraftingItem value;

    public InWorldCraftingItemSafe(InWorldCraftingItem value){
        this.value = value;
    }
}
