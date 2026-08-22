package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstate.InWorldCraftingItem;
import spacegame.world.blockstate.MultiState;


public class CropStateIO {

    public void saveCropStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        CropState cropState;
        int totalCount = chunk.getBlockStateCount(MultiState.CROP_STATE);
        CropState[] cropStates = this.getAllCropStatesInArray(chunk, totalCount);
        int cropStateCount = 0;
        NBTTagCompound[] cropStateTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < cropStateTags.length; i++){

            cropState = cropStates[i];
            cropStateTags[i] = new NBTTagCompound();
            cropStateTags[i].setInteger("index", cropState.index);
            cropStateTags[i].setString("name", cropState.name);
            cropStateTags[i].setBoolean("canMutate", cropState.canMutate);
            cropStateTags[i].setString("targetCrop", cropState.targetCrop);
            cropStateTags[i].setInteger("growthStage", cropState.growthStage);
            cropStateTags[i].setFloat("percentToTargetCrop", cropState.percentToTargetCrop);

            nbtTagCompound.setTag("cropState" + cropStateCount, cropStateTags[i]);
            cropStateCount++;
        }
        nbtTagCompound.setInteger("cropStateCount", cropStateCount);
    }

    public void loadCropStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int cropStateCount = nbtTagCompound.getInteger("cropStateCount");
        NBTTagCompound cropStateLoadedTag;
        for (int i = 0; i < cropStateCount; i++) {
            cropStateLoadedTag = nbtTagCompound.getCompoundTag("cropState" + i);
            int index = cropStateLoadedTag.getInteger("index");
            String name = cropStateLoadedTag.getString("name");
            boolean canMutate = cropStateLoadedTag.getBoolean("canMutate");
            String targetCrop = cropStateLoadedTag.getString("targetCrop");
            int growthStage = cropStateLoadedTag.getInteger("growthStage");
            float percentToTargetCrop = cropStateLoadedTag.getFloat("percentToTargetCrop");
            chunk.addBlockState(index, MultiState.CROP_STATE, new CropState(index,name,canMutate,targetCrop,growthStage,percentToTargetCrop));
        }
    }
    private CropState[] getAllCropStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.CROP_STATE, totalCount);
        CropState[] returnArray = new CropState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (CropState) base[i];
        }


        return returnArray;
    }

}
