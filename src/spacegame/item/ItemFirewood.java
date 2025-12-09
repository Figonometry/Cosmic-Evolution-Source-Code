package spacegame.item;

import spacegame.core.Timer;

public final class ItemFirewood extends Item implements IFuel {
    public ItemFirewood(short ID, int textureID, String filepath) {
        super(ID, textureID, filepath);
    }

    @Override
    public float getHeatingTemperature() {
        return 300f;
    }

    @Override
    public long getBurnTime() {
        return 120 * Timer.REAL_SECOND;
    }

    @Override
    public short getFuelItemID() {
        return this.ID;
    }
}
