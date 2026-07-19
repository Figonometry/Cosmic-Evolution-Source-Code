package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.CropState;
import spacegame.world.blockstatewrapper.CropStateSafe;

import java.util.Iterator;
import java.util.Map;

public class CropStateIO {

    public void saveCropStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        CropState cropState;
        CropState[] cropStates = this.getAllCropStatesInArray(chunk);
        int cropStateCount = 0;
        NBTTagCompound[] cropStateTags = new NBTTagCompound[chunk.cropStates.size()];
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
            chunk.addCropState(new CropState(index,name,canMutate,targetCrop,growthStage,percentToTargetCrop), index);
        }
    }
    private CropState[] getAllCropStatesInArray(Chunk chunk){
        int index = 0;
        CropState cropState;
        CropState[] cropStates1 = new CropState[chunk.cropStates.size()];
        Iterator<Map.Entry<Integer, CropStateSafe>> iterator = chunk.cropStates.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, CropStateSafe> entry = iterator.next();
            cropState = entry.getValue().value;
            if(cropState != null){
                cropStates1[index] = cropState;
                index++;
            }
        }
        return cropStates1;
    }

}
