package spacegame.item;

import spacegame.core.Timer;

public final class ItemRawGameMeat extends ItemFood implements IHeatable, IDecayItem {
    public ItemRawGameMeat(short ID, int textureID, String filepath, float saturationIncrease) {
        super(ID, textureID, filepath, saturationIncrease);
    }

    @Override
    public float getTargetTemperature() {
        return 155f;
    }

    @Override
    public long getCookTime() {
        return Timer.REAL_SECOND * 20;
    }

    @Override
    public short getOutputItem() {
        return Item.cookedGameMeat.ID;
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
