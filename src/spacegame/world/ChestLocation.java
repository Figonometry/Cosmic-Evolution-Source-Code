package spacegame.world;

import spacegame.item.Inventory;

public final class ChestLocation {
    public short index;
    public Inventory inventory;
    public ChestLocation(short index, Inventory inventory){
        this.index = index;
        this.inventory = inventory;
    }
}
