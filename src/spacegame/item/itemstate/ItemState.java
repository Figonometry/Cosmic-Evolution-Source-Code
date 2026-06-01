package spacegame.item.itemstate;

import spacegame.nbt.NBTTagCompound;

public abstract class ItemState {



    public ItemState copy(){
        if(this instanceof SeedState currentState){
            return new SeedState(currentState.canMutate, currentState.percentToTargetCrop, currentState.targetCrop);
        }
        return null;
    }


    public NBTTagCompound getCompoundTag(){
        if(this instanceof SeedState seedState){
            NBTTagCompound returnTag = new NBTTagCompound();
            returnTag.setString("Type", "SeedState");
            returnTag.setBoolean("canMutate",seedState.canMutate);
            returnTag.setFloat("percentToTargetCrop", seedState.percentToTargetCrop);
            returnTag.setString("targetCrop", seedState.targetCrop);
            return returnTag;
        }

        return null;
    }

    public static ItemState loadFromCompoundTag(NBTTagCompound compoundTag){
        String type = compoundTag != null ? compoundTag.getString("Type") : "Null";

        switch (type){
            case "SeedState" -> {
                return new SeedState(compoundTag.getBoolean("canMutate"), compoundTag.getFloat("percentToTargetCrop"), compoundTag.getString("targetCrop"));
            }
        }

        return null;
    }
}
