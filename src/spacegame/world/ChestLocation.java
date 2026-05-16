package spacegame.world;

import spacegame.item.Inventory;

public final class ChestLocation {
    public int index;
    public Inventory inventory;
    public ChestLocation(int index, Inventory inventory){
        this.index = index;
        this.inventory = inventory;
    }
}
