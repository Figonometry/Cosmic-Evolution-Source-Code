package spacegame.item;

public interface IDecayItem {
    void decayItem(ItemStack itemStack);
    long getDecayTime();


}
