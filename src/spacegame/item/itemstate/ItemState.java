package spacegame.item.itemstate;

import spacegame.nbt.NBTTagCompound;

public abstract class ItemState {



    public ItemState copy(){
        if(this instanceof SeedState currentState){
            return new SeedState(currentState.canMutate, currentState.percentToTargetCrop, currentState.targetCrop);
        }
        return null;
    }


    public abstract NBTTagCompound getCompoundTag();

    public static ItemState loadFromCompoundTag(NBTTagCompound compoundTag){
        String type = compoundTag != null ? compoundTag.getString("Type") : "Null";

        switch (type){
            case "SeedState" -> {
                return new SeedState(compoundTag.getBoolean("canMutate"), compoundTag.getFloat("percentToTargetCrop"), compoundTag.getString("targetCrop"));
            }
        }

        return null;
    }


    public abstract String getClassName();

    public abstract boolean doStatesMeetMergeCriteria(ItemState incomingState);
}
