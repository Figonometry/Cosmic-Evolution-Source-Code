package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.BerryBushState;
import spacegame.world.blockstate.BlockState;
import spacegame.world.blockstate.CampfireState;
import spacegame.world.blockstate.MultiState;

public final class CampfireStateIO {
    public void saveCampfireStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        CampfireState campfireState;
        int totalCount = chunk.getBlockStateCount(MultiState.CAMPFIRE_STATE);
        CampfireState[] campfireStates = this.getAllCampfireStatesInArray(chunk, totalCount);
        int campfireStateCount = 0;
        NBTTagCompound[] campfireStatesTags = new NBTTagCompound[totalCount];
        for(int i = 0; i < campfireStatesTags.length; i++){
            campfireState = campfireStates[i];
            campfireStatesTags[i] = new NBTTagCompound();
            campfireStatesTags[i].setInteger("index", campfireState.index);
            campfireStatesTags[i].setBoolean("isLit", campfireState.isLit);
            campfireStatesTags[i].setInteger("logCount", campfireState.logCount);
            campfireStatesTags[i].setInteger("cookingStickCount", campfireState.cookingStickCount);

            nbtTagCompound.setTag("campfireState" + campfireStateCount, campfireStatesTags[i]);
            campfireStateCount++;
        }
        nbtTagCompound.setInteger("campfireStateCount", campfireStateCount);
    }


    public void loadCampfireStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        int campfireStateCount = nbtTagCompound.getInteger("campfireStateCount");
        NBTTagCompound campfireStateLoadedTag;
        for (int i = 0; i < campfireStateCount; i++) {
            campfireStateLoadedTag = nbtTagCompound.getCompoundTag("campfireState" + i);
            int index = campfireStateLoadedTag.getInteger("index");
            boolean isLit = campfireStateLoadedTag.getBoolean("isLit");
            int logCount = campfireStateLoadedTag.getInteger("logCount");
            int cookingStickCount = campfireStateLoadedTag.getInteger("cookingStickCount");


            chunk.addBlockState(index, MultiState.CAMPFIRE_STATE, new CampfireState(index, isLit, logCount, cookingStickCount));
        }
    }


    public CampfireState[] getAllCampfireStatesInArray(Chunk chunk, int totalCount){
        BlockState[] base = chunk.getAllBlockStatesOfType(MultiState.CAMPFIRE_STATE, totalCount);
        CampfireState[] returnArray = new CampfireState[base.length];

        for (int i = 0; i < base.length; i++) {
            returnArray[i] = (CampfireState) base[i];
        }


        return returnArray;
    }
}
