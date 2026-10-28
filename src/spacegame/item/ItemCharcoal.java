package spacegame.item;

import spacegame.core.Timer;

public final class ItemCharcoal extends Item implements IFuel {
    public ItemCharcoal(short ID, String modelFilePath, String filepath) {
        super(ID, modelFilePath, filepath);
    }

    @Override
    public float getHeatingTemperature() {
        return 2000f;
    }

    @Override
    public long getBurnTime() {
        return Timer.REAL_MINUTE;
    }

    @Override
    public short getFuelItemID() {
        return this.ID;
    }
}
