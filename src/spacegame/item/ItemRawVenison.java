package spacegame.item;

import spacegame.core.Timer;

public final class ItemRawVenison extends ItemFood implements IHeatable {
    public ItemRawVenison(short ID, int textureID, String filepath, float saturationIncrease) {
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
        return Item.cookedVenison.ID;
    }
}
