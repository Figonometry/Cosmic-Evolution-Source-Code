package spacegame.world.blockstateio;

import spacegame.nbt.NBTTagCompound;
import spacegame.world.Chunk;
import spacegame.world.blockstate.CampfireState;
import spacegame.world.blockstate.TilledSoilState;
import spacegame.world.blockstatewrapper.CampfireStateSafe;

import java.util.Iterator;
import java.util.Map;

public final class CampfireStateIO {
    public void saveCampfireStates(Chunk chunk, NBTTagCompound nbtTagCompound){
        CampfireState campfireState;
        CampfireState[] campfireStates = this.getAllCampfireStatesInArray(chunk);
        int campfireStateCount = 0;
        NBTTagCompound[] campfireStatesTags = new NBTTagCompound[chunk.campfireStates.size()];
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


            chunk.addCampfireState(new CampfireState(index, isLit, logCount, cookingStickCount), index);
        }
    }


    public CampfireState[] getAllCampfireStatesInArray(Chunk chunk){
        int index = 0;
        CampfireState campfireState;
        CampfireState[] campfireStates1 = new CampfireState[chunk.campfireStates.size()];
        Iterator<Map.Entry<Integer, CampfireStateSafe>> iterator = chunk.campfireStates.entrySet().iterator();
        while(iterator.hasNext()){
            Map.Entry<Integer, CampfireStateSafe> entry = iterator.next();
            campfireState = entry.getValue().value;
            if(campfireState != null){
                campfireStates1[index] = campfireState;
                index++;
            }
        }
        return campfireStates1;
    }
}
