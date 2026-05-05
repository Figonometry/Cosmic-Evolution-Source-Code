package spacegame.item;

import spacegame.core.Timer;

public final class ItemCookedGameMeat extends ItemFood implements IDecayItem {
    public ItemCookedGameMeat(short ID, int textureID, String filepath, float saturationIncrease) {
        super(ID, textureID, filepath, saturationIncrease);
    }

    @Override
    public void decayItem(ItemStack itemStack) {
        itemStack.item = Item.rot;
        itemStack.decayTime = 0L;
    }

    @Override
    public long getDecayTime() {
        return Timer.GAME_DAY;
    }
}
