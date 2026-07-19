package spacegame.world.blockstate;

public final class CropState {
    public int index;
    public String name;
    public boolean canMutate;
    public String targetCrop = "";
    public int growthStage;
    public float percentToTargetCrop;
    public int damageValue; //This needs to be stored to be passed onto child seeds from this crop along with the canMutate bool

    public CropState(int index, String name, boolean canMutate, String targetCrop, int growthStage, float percentToTargetCrop){
        this.index = index;
        this.name = name;
        this.canMutate = canMutate;
        this.targetCrop = targetCrop;
        this.growthStage = growthStage;
        this.percentToTargetCrop = percentToTargetCrop;
    }


}
