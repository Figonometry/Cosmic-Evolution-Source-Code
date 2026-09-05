package spacegame.item.itemstate;

import spacegame.nbt.NBTTagCompound;

public final class SeedState extends ItemState {
    public boolean canMutate;
    public float percentToTargetCrop;
    public String targetCrop;

    public SeedState(boolean canMutate, float percentToTargetCrop, String targetCrop){
        this.canMutate = canMutate;
        this.percentToTargetCrop = percentToTargetCrop;
        this.targetCrop = targetCrop;
    }


    public NBTTagCompound getCompoundTag(){
            NBTTagCompound returnTag = new NBTTagCompound();
            returnTag.setString("Type", "SeedState");
            returnTag.setBoolean("canMutate",this.canMutate);
            returnTag.setFloat("percentToTargetCrop", this.percentToTargetCrop);
            returnTag.setString("targetCrop", this.targetCrop);
            return returnTag;
    }

    @Override
    public String getClassName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean doStatesMeetMergeCriteria(ItemState incomingState) {
        SeedState incomingSeedState = (SeedState) incomingState;
        return this.canMutate == incomingSeedState.canMutate && this.targetCrop.equals(incomingSeedState.targetCrop);
    }


}
