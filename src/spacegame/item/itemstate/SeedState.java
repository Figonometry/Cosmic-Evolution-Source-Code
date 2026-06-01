package spacegame.item.itemstate;

public final class SeedState extends ItemState {
    public boolean canMutate;
    public float percentToTargetCrop;
    public String targetCrop;

    public SeedState(boolean canMutate, float percentToTargetCrop, String targetCrop){
        this.canMutate = canMutate;
        this.percentToTargetCrop = percentToTargetCrop;
        this.targetCrop = targetCrop;
    }
}
